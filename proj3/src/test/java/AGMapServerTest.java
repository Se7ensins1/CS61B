import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

/** Coming Soon. You'll need to repull from skeleton when this file is available. */
public class AGMapServerTest {

    @Test
    public void testGetMapRaster() throws Exception {
        Rasterer x = new Rasterer("img/");
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.2104604264636);
        params.put("ullon", -122.30410170759153);
        params.put("w", 1085.0);
        params.put("h", 566.0);
        params.put("lrlat", 37.8318576119893);
        params.put("ullat", 37.870213571328854);
        Map<String, Object> h = x.getMapRaster(params);
        String[][] d = (String[][]) h.get("render_grid");
        for(int i = 0; i < d.length; i++ ){
            for (int j = 0; j < d[0].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void test1234() throws Exception {
        Rasterer x = new Rasterer("img/");
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.20908713544797);
        params.put("ullon", -122.3027284165759);
        params.put("w", 305.0);
        params.put("h", 300.0);
        params.put("lrlat", 37.848731523430196);
        params.put("ullat", 37.88708748276975);
        Map<String, Object> h = x.getMapRaster(params);
        String[][] d = (String[][]) h.get("render_grid");
        for(int i = 0; i < d.length; i++ ){
            for (int j = 0; j < d[0].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void test() throws Exception {
        Rasterer x = new Rasterer("img/");
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.24053369025242);
        params.put("ullon", -122.24163047377972);
        params.put("w", 892.0);
        params.put("h", 875.0);
        params.put("lrlat", 37.87548268822065);
        params.put("ullat", 37.87655856892288);
        Map<String, Object> h = x.getMapRaster(params);
        String[][] d = (String[][]) h.get("render_grid");
        for(int i = 0; i < d.length; i++ ){
            for (int j = 0; j < d[0].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
        Assert.assertEquals(-122.24212646484375, h.get("raster_ul_lon"));
    }

    @Test
    public void testTwelveImages() throws Exception {
        Rasterer x = new Rasterer("img/");
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.2104604264636);
        params.put("ullon", -122.30410170759153);
        params.put("w", 1085.0);
        params.put("h", 566.0);
        params.put("lrlat", 37.8318576119893);
        params.put("ullat", 37.870213571328854);
        Map<String, Object> h = x.getMapRaster(params);
        String[][] d = (String[][]) h.get("render_grid");
        for(int i = 0; i < d.length; i++ ){
            for (int j = 0; j < d[0].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void testWoosley() throws Exception {
        Rasterer x = new Rasterer("img/");
        HashMap<String, Double> params = new HashMap<>();
        params.put("lrlon", -122.2709603651743);
        params.put("ullon", -122.27292105996419);
        params.put("w", 731.0);
        params.put("h", 348.0);
        params.put("lrlat", 37.8510975982183);
        params.put("ullat", 37.85183456121798);
        Map<String, Object> h = x.getMapRaster(params);
        String[][] d = (String[][]) h.get("render_grid");
        for(int i = 0; i < d.length; i++ ){
            for (int j = 0; j < d[0].length; j++) {
                System.out.print(d[i][j] + " ");
            }
            System.out.println();
        }
    }
}