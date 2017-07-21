import java.util.*;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */


//    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
//                                                double stlat, double destlon, double destlat) {
//        Long start = g.closest(stlon, stlat);
//        Long destination = g.closest(destlon, destlat);
//        GraphDB.Node star = g.getNodes().get(start);
//        final GraphDB.Node dest = g.getNodes().get(destination);
//        GraphDB.Node dest2 = g.getNodes().get(destination);
//
//        HashSet<GraphDB.Node> visited = new HashSet<>();
//        LinkedList<Long> ret = new LinkedList<>();
//        PriorityQueue<GraphDB.Node> fringe = new PriorityQueue<>(new Comparator<GraphDB.Node>() {
//            public int compare(GraphDB.Node n1, GraphDB.Node n2) {
//                Double dist = n1.getCost() + n1.getDistToFinal();
//                Double dist2 = n2.getCost() + n2.getDistToFinal();
//                return (dist).compareTo(dist2);
//            }
//        });
//        fringe.add(star);
//
//        while (!fringe.isEmpty()) {
//            GraphDB.Node curr = fringe.poll();
//            visited.add(curr);
//            if (curr.getCost() == Double.POSITIVE_INFINITY) {
//                curr.setCost(0.0);
//                curr.setDistToFinal(g.distance(curr.getId(), dest.getId()));
//            }
//            for (Long id : curr.getAdjacent()) {
//                GraphDB.Node n = g.getNodes().get(id);
//                n.setDistToFinal(g.distance(n.getId(), dest.getId()));
//                if (!visited.contains(n)) {
//                    if (n.getCost() > curr.getCost() + g.distance(curr.getId(), id)) {
//                        n.setCost(curr.getCost() + g.distance(curr.getId(), id));
//                        n.setPrev(curr);
//                    }
//                    if (!fringe.contains(n)) {
//                        fringe.add(n);
//                    }
//                }
//                if (n.equals(dest)) {
//                    break;
//                }
//            }
//        }
//        while (dest2 != null) {
//            ret.addFirst(dest2.getId());
//            dest2 = dest2.getPrev();
//        }
//        for (GraphDB.Node n : visited) {
//            n.setCost(Double.POSITIVE_INFINITY);
//            n.setPrev(null);
//            n.setDistToFinal(0.0);
//        }
//
//        return ret;
//    }









    public static LinkedList<Long> shortestPath(GraphDB g, double stlon,
                                                double stlat, double destlon, double destlat) {
        class Node {
            private GraphDB.Node n;
            private Node prev;
            private Double cost;

            public Node(GraphDB.Node n) {
                this.n = n;
                this.cost = Double.POSITIVE_INFINITY;
            }

            public void setPrev(Node n) {
                this.prev = n;
            }
            public void setCost(Double cost) {
                this.cost = cost;
            }
            public Long getId() {
                return this.n.getId();
            }
            public Double getCost() {
                return this.cost;
            }
            public ArrayList<Long> getAdjacent() {
                return this.n.getAdjacent();
            }
            public Node getPrev() {
                return this.prev;
            }
        }

        Long start = g.closest(stlon, stlat);
        Long destination = g.closest(destlon, destlat);
        GraphDB.Node star = g.getNodes().get(start);
        GraphDB.Node dest = g.getNodes().get(destination);
        Node s = new Node(star);
        Node d = new Node(dest);

        HashSet<Long> visited = new HashSet<>();
        LinkedList<Long> ret = new LinkedList<>();
        PriorityQueue<Node> fringe = new PriorityQueue<>(new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return (n1.getCost()).compareTo(n2.getCost());
            }
        });
        fringe.add(s);

        while (!fringe.isEmpty()) {
            Node curr = fringe.poll();
            visited.add(curr.getId());
            if (curr.getCost() == Double.POSITIVE_INFINITY) {
                curr.setCost(0.0);
            }
            for (Long id : curr.getAdjacent()) {
                if (!visited.contains(id)) {
                    Node n = new Node(g.getNodes().get(id));
                    if (n.getCost() > curr.getCost() + g.distance(curr.getId(), id)) {
                        n.setCost(curr.getCost() + g.distance(curr.getId(), id));
                        n.setPrev(curr);
                    }
                    if (!visited.contains(n.getId()) && !fringe.contains(n)) {
                        fringe.add(n);
                    }
                    if (n.equals(d)) {
                        break;
                    }
                }
            }
        }
        while (d != null) {
            ret.addFirst(d.getId());
            d = d.getPrev();
        }
        return ret;
    }
}
