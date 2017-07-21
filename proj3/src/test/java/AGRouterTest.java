import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class AGRouterTest extends AGMapTest {
    /**
     * Test the route-finding functionality by comparing the node id list item by item.
     * @throws Exception
     */
    @Test
    public void testShortestPath() throws Exception {
        for (TestParameters p : params) {
            LinkedList<Long> studentRouteResult = Router.shortestPath(graph,
                    p.routeParams.get("start_lon"), p.routeParams.get("start_lat"),
                    p.routeParams.get("end_lon"), p.routeParams.get("end_lat"));
            assertEquals("Found route differs for input: " + p.routeParams + ".\n",
                    p.routeResult, studentRouteResult);
        }
    }

    @Test
    public void shortestPath6() throws Exception {
        LinkedList<Long> path = Router.shortestPath(graph, -122.25994411376841,
                37.84296507876308, -122.26818786914781, 37.85862291344154);



        //[266433383, 53122181, 53099306, 53099304, 312431308, 312431297, 312431298, 1237053599, 53121454, 1237053615, 1237053647, 651063703, 1237053624, 53107946, 53085960, 1237053585, 1237053720, 53085999, 53088345, 1237053584, 53088338, 53106473, 1237053711, 53106431, 53121461, 53121462, 53096046, 53043874, 53096049, 53096051, 2820169740, 2820169756, 2820169751, 2820169731, 2820169753, 53082577, 623807841, 53064680, 3701955052, 2086760873, 53037909, 2086760870, 2086696624, 53037907, 275782472, 370473556, 2086667342]
    }
}
