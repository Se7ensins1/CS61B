import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public class QuadTree {
        private int depth;
        private String node;
        private QuadTree parent;
        private QuadTree[] children;
        private Double ullon;
        private Double ullat;
        private Double lrlon;
        private Double lrlat;
        private Double ldpp;

        private QuadTree(String num, QuadTree parent) {
            if (num.equals("root")) {
                num = "";
                this.ullon = MapServer.ROOT_ULLON;
                this.ullat = MapServer.ROOT_ULLAT;
                this.lrlon = MapServer.ROOT_LRLON;
                this.lrlat = MapServer.ROOT_LRLAT;
            }
            this.node = num;
            this.depth = num.length();
            this.parent = parent;

            if (parent != null) {
                this.ullon = ullon(num.charAt(num.length() - 1));
                this.ullat = ullat(num.charAt(num.length() - 1));
                this.lrlon = lrlon(num.charAt(num.length() - 1));
                this.lrlat = lrlat(num.charAt(num.length() - 1));
            }
            this.ldpp = (lrlon - ullon) / 256;
            if (this.depth < 7) {
                this.children = new QuadTree[4];
                children[0] = new QuadTree(num + "1", this);
                children[1] = new QuadTree(num + "2", this);
                children[2] = new QuadTree(num + "3", this);
                children[3] = new QuadTree(num + "4", this);
            }
        }
        private double ullon(Character num) {
            if (num.equals('1') || num.equals('3')) {
                return parent.ullon;
            }
            return (parent.ullon + parent.lrlon) / 2;
        }
        private double ullat(Character num) {
            if (num.equals('1') || num.equals('2')) {
                return parent.ullat;
            }
            return (parent.ullat + parent.lrlat) / 2;
        }
        private double lrlon(Character num) {
            if (num.equals('2') || num.equals('4')) {
                return parent.lrlon;
            }
            return (parent.lrlon + parent.ullon) / 2;
        }
        private double lrlat(Character num) {
            if (num.equals('3') || num.equals('4')) {
                return parent.lrlat;
            }
            return (parent.lrlat + parent.ullat) / 2;
        }
    }

    private QuadTree raster;
    private String root;

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        this.raster = new QuadTree("root", null);
        this.root = imgRoot;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        ArrayDeque<QuadTree> fringe = new ArrayDeque<>();
        fringe.addFirst(this.raster);
        ArrayList<QuadTree> toReturn = new ArrayList<>();

        while (!fringe.isEmpty()) {
            QuadTree child = fringe.removeFirst();
            if (intersectsTile(child, params)) {
                if (!lonDPPsmallerThanQuery(child, params)) {
                    for (QuadTree children : child.children) {
                        fringe.addFirst(children);
                    }
                } else if (lonDPPsmallerThanQuery(child, params)) {
                    toReturn.add(0, child);
                }
            }
        }

        boolean success = true;
        if (!lonDPPsmallerThanQuery(toReturn.get(0), params)) {
            success = false;
        }
        HashMap<String, Object> results = new HashMap<>();
        results.put("render_grid", toChart(toReturn));
        results.put("raster_ul_lon", toReturn.get(0).ullon);
        results.put("raster_ul_lat", toReturn.get(0).ullat);
        results.put("raster_lr_lon", toReturn.get(toReturn.size() - 1).lrlon);
        results.put("raster_lr_lat", toReturn.get(toReturn.size() - 1).lrlat);
        results.put("depth", toReturn.get(0).depth);
        results.put("query_success", success);

        return results;
    }

    private String[][] toChart(ArrayList<QuadTree> used) {
        HashSet<Double> uniqueLat = new HashSet<>();
        for (QuadTree u : used) {
            uniqueLat.add(u.ullat);
        }
        int lat = uniqueLat.size();
        int lon = used.size() / lat;
        String[][] ret = new String[lat][lon];
        Collections.sort(used, new Comparator<QuadTree>() {
            public int compare(QuadTree o1, QuadTree o2) {
                return (o2.lrlat).compareTo(o1.lrlat);
            }
        });

        int t = 0;
        for (int i = 0; i < ret.length; i++) {
            for (int j = 0; j < ret[0].length; j++) {
                ret[i][j] = this.root + used.get(t).node + ".png";
                t += 1;
            }
        }
        return ret;
    }

    private static Double lonDPP(Double lrlon, Double ullon, Double w) {
        return (lrlon - ullon) / (w);
    }

    private static boolean intersectsTile(QuadTree child, Map<String, Double> params) {
        Double queryLrlon = params.get("lrlon");
        Double queryUllon = params.get("ullon");
        Double queryUllat = params.get("ullat");
        Double queryLrlat = params.get("lrlat");

        return !(child.lrlon < queryUllon || child.ullon > queryLrlon
                || child.ullat < queryLrlat || child.lrlat > queryUllat);
    }

    private static boolean lonDPPsmallerThanQuery(QuadTree child, Map<String, Double> params) {
        if (child.children == null) {
            return true;
        }
        Double queryLrlon = params.get("lrlon");
        Double queryUllon = params.get("ullon");
        Double queryW = params.get("w");

        Double queryLDPP = lonDPP(queryLrlon, queryUllon, queryW);

        return child.ldpp < queryLDPP;
    }
}
