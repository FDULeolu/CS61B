import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

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
    private final Map<Long, Node> nodes = new LinkedHashMap<>();
    private final Map<Long, Way> ways = new LinkedHashMap<>();
    private final Map<Long, NameNode> nameNodes = new LinkedHashMap<>();
    private final Map<String, List<Long>> locations = new LinkedHashMap<>();
    private final Trie trieForNodeName = new Trie();
    private final KdTree kdTreeForNearestNeighbor = new KdTree();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();

        for (long node : nodes.keySet()) {
            addToKdTree(nodes.get(node));
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Iterator<Long> it = nodes.keySet().iterator();
        while (it.hasNext()) {
            Long node = it.next();
            if (nodes.get(node).adjs.isEmpty()) {
                it.remove();
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).adjs;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        return kdTreeForNearestNeighbor.nearest(lon, lat);
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    public void addNode(Node n) {
        nodes.put(n.id, n);
    }

    public void addWay(Way w) {
        ways.put(w.id, w);
    }

    public void addToKdTree(GraphDB.Node n) {
        kdTreeForNearestNeighbor.insert(n);
    }

    public Node getNode(long nodeId) {
        return nodes.get(nodeId);
    }

    public void addLocation(String name, long id) {
        if (locations.containsKey(name)) {
            locations.get(name).add(id);
        } else {
            locations.put(name, new ArrayList<>(Arrays.asList(id)));
        }
    }

    /** Add n2Id to n1's adjs set */
    public void addAdj(long n1Id, long n2Id) {
        nodes.get(n1Id).adjs.add(n2Id);
    }

    public void addCleanNameToTire(String cleanName, String name) {
        trieForNodeName.add(cleanName, name);
    }

    public List<String> getLocationsByPrefix(String prefix) {
        return collectFromTrie(prefix);
    }

    public List<String> collectFromTrie(String prefix) {
        Trie.TrieNode prefixEnd = trieForNodeName.findNode(prefix);
        List<String> res = new ArrayList<>();
        if (prefixEnd == null) {
            return res;
        }
        if (prefixEnd.isWord()) {
            res.addAll(prefixEnd.getNames());
        }
        for (char c : prefixEnd.getChildren().keySet()) {
            colHelper(prefix + c, res, prefixEnd.getChildren().get(c));
        }
        return res;
    }

    private void colHelper(String s, List<String> res, Trie.TrieNode node) {
        if (node.isWord()) {
            res.addAll(node.getNames());
        }
        for (char c : node.getChildren().keySet()) {
            colHelper(s + c, res, node.getChildren().get(c));
        }
    }

    private Map<String, Object> getNameNodeAsMap(long id) {
        NameNode n = nameNodes.get(id);
        Map<String, Object> res = new HashMap<>();
        res.put("id", n.id);
        res.put("lon", n.lon);
        res.put("lat", n.lat);
        res.put("name", n.name);
        return res;
    }

    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> res = new LinkedList<>();
        if (!locations.containsKey(locationName)) {
            return res;
        }
        for (long id : locations.get(locationName)) {
            res.add(getNameNodeAsMap(id));
        }
        return res;
    }

    public Comparator<Long> getComparator() {
        return new NodeComparator();
    }

    public List<Long> getWays(long nodeId) {
        return nodes.get(nodeId).wayIds;
    }

    static class Node {
        long id;
        double lat;
        double lon;
        double priority = 0;
        double distTo = 0;
        List<Long> wayIds;
        Set<Long> adjs;

        public Node(long id, double lon, double lat) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            wayIds = new ArrayList<>();
            adjs = new HashSet<>();
        }


    }

    public void changePriority(long v, double newPriority) {
        nodes.get(v).priority = newPriority;
    }

    public void changeDistTo(long v, double newDist) {
        nodes.get(v).distTo = newDist;
    }

    public double getDistTo(long v) {
        return nodes.get(v).distTo;
    }

    class NodeComparator implements Comparator<Long> {

        @Override
        public int compare(Long o1, Long o2) {
            return Double.compare(nodes.get(o1).priority, nodes.get(o2).priority);
        }
    }

    public String getWayName(long w) {
        return ways.get(w).name;
    }

    static class NameNode extends Node {

        private String name;
        public NameNode(long id, double lon, double lat, String name) {
            super(id, lon, lat);
            this.name = name;
        }
    }

    public void addNameNode(NameNode n) {
        nameNodes.put(n.id, n);
    }

    public String getNodeName(long id) {
        return nameNodes.get(id).name;
    }

    static class Way {
        String name;
        long id;
        String maxSpeed;
        String highWay;
        public Way(long id) {
            this.id = id;
        }
    }
}
