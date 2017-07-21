import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.List;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    private HashMap<Node, ArrayList<Long>> graph;
    private HashMap<String, ArrayList<Node>> locations;
    private HashMap<Long, Node> nodes;
    private VocabTree pref;

    public class VocabTree {
        private HashMap<String, VocabTree> words;
        private String charTill;

        public VocabTree() {
            this.words = new HashMap<>();
        }

        public String getCharTill() {
            return this.charTill;
        }

        public void insertWord(String word, String origWord) {
            if (word.equals("")) {
                this.charTill = origWord;
                return;
            }
            String chair = Character.toString(word.charAt(0));
            if (!this.words.containsKey(chair)) {
                this.words.put(chair, new VocabTree());
            }
            VocabTree t = this.words.get(word.substring(0, 1));
            t.insertWord(word.substring(1), origWord);
        }

        public VocabTree getWords(String word) {
            if (word.equals("")) {
                return this;
            }
            VocabTree t = this.words.get(word.substring(0, 1));
            return t.getWords(word.substring(1));
        }
    }

    public class Node {
        private Long id;
        private String locName;
        private Double lat;
        private Double lon;
        private ArrayList<Long> adjacent;
        private Double cost;
        private Node prev;
        private Double distToFinal;

        private Node(Long id, Double lat, Double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.adjacent = new ArrayList<>();
            this.cost = Double.POSITIVE_INFINITY;
        }

        public Long getId() {
            return this.id;
        }
        public Double getCost() {
            return this.cost;
        }
        public Node getPrev() {
            return this.prev;
        }
        public String getLocName() {
            return this.locName;
        }
        public Double getLat() {
            return this.lat;
        }
        public Double getLon() {
            return this.lon;
        }
        public Double getDistToFinal() {
            return distToFinal;
        }

        public void setDistToFinal(Double dist) {
            this.distToFinal = dist;
        }
        public void setPrev(Node prev) {
            this.prev = prev;
        }
        public void setCost(Double cost) {
            this.cost = cost;
        }
        public ArrayList<Long> getAdjacent() {
            return this.adjacent;
        }
        public void addAdjacent(Long n) {
            this.adjacent.add(n);
        }
        public void setLocName(String n) {
            this.locName = n;
        }
    }

    public class Way {
        private Long id;
        private String name;
        private boolean flag;
        private int maxSpeed;
        private ArrayList<Node> connections;

        public Way(Long id) {
            this.id = id;
            this.connections = new ArrayList<>();
            this.flag = false;
        }

        public ArrayList<Node> getConnections() {
            return this.connections;
        }
        public void setMaxSpeed(int speed) {
            this.maxSpeed = speed;
        }
        public void setFlag() {
            this.flag = true;
        }
        public boolean getFlag() {
            return this.flag;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void addConnection(Node nodeID) {
            this.connections.add(nodeID);
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        this.nodes = new HashMap<>();
        this.graph = new HashMap<>();
        this.locations = new HashMap<>();
        this.pref = new VocabTree();
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public HashMap<Long, Node> getNodes() {
        return this.nodes;
    }
    public VocabTree getPref() {
        return this.pref;
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public List<Map<String, Object>> locations(String loc) {
        List<Map<String, Object>> locs = new ArrayList<>();
        loc = cleanString(loc);
        for (Node n : this.locations.get(loc)) {
            HashMap<String, Object> dets = new HashMap<>();
            dets.put("name", n.getLocName());
            dets.put("lon", n.getLon());
            dets.put("id", n.getId());
            dets.put("lat", n.getLat());
            locs.add(dets);
        }
        return locs;
    }

    public List<String> prefixes(String prefix) {
        prefix = cleanString(prefix);

        VocabTree vocab = this.getPref().getWords(prefix);

        List<String> words = new ArrayList<>();
        ArrayDeque<VocabTree> fringe = new ArrayDeque<>();
        fringe.add(vocab);

        while (!fringe.isEmpty()) {
            VocabTree curr = fringe.removeFirst();
            if (curr.getCharTill() != null) {
                words.add(curr.getCharTill());
            }
            if (!curr.words.isEmpty()) {
                for (VocabTree t : curr.words.values()) {
                    fringe.addFirst(t);
                }
            }
        }

        return words;
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        ArrayList<Node> removed = new ArrayList<>();
        for (Long n : this.nodes.keySet()) {
            Node no = this.nodes.get(n);
            if (no.adjacent.isEmpty()) {
                removed.add(no);
            }
        }
        for (Node n : removed) {
            this.graph.remove(n);
            nodes.remove(n.id);
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return this.nodes.keySet();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        return this.nodes.get(v).adjacent;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        double lonDiff = lon(v) - lon(w);
        double latDiff = lat(v) - lat(w);
        double pows = Math.pow(lonDiff, 2) + Math.pow(latDiff, 2);
        return Math.sqrt(pows);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double minDist = 100000.0;
        Node minNode = null;
        for (Node node : this.nodes.values()) {
            double dist = Math.sqrt(Math.pow((node.lon - lon), 2) + Math.pow((node.lat - lat), 2));
            if (dist < minDist) {
                minDist = dist;
                minNode = node;
            }
        }
        return minNode.id;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return this.nodes.get(v).lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return this.nodes.get(v).lat;
    }

    public Node addNode(Long id, Double lat, Double lon) {
        Node node = new Node(id, lat, lon);
        this.nodes.put(node.id, node);
        this.graph.put(node, new ArrayList<>());
        return node;
    }

    public Way addWay(Long id) {
        Way way = new Way(id);
        return way;
    }

    public void addLocation(Node n) {
        String cleaned = cleanString(n.locName);
        ArrayList<Node> arrayNode =  new ArrayList<>();
        if (this.locations.containsKey(cleaned)) {
            this.locations.get(cleaned).add(n);
        } else {
            arrayNode.add(n);
            this.locations.put(cleaned, arrayNode);
        }
    }
}
