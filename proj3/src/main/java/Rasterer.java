import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;


/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private int depth;
    private boolean querySuccess;
    private double ullon;
    private double lrlon;
    private double ullat;
    private double lrlat;
    private double w;
    private double h;

    private double lonPerGrid;
    private double latPerGrid;

    private String[][] renderGrid;
    private Map<String, Double> raster;


    public Rasterer() {
        depth = -1;
        querySuccess = false;
        raster = new HashMap<>();
        raster.put("raster_ul_lon", null);
        raster.put("raster_ul_lat", null);
        raster.put("raster_lr_lon", null);
        raster.put("raster_lr_lat", null);
    }

    private void loadParams(Map<String, Double> params) {
        ullon = params.get("ullon");
        lrlon = params.get("lrlon");
        ullat = params.get("ullat");
        lrlat = params.get("lrlat");
        w = params.get("w");
        h = params.get("h");

//        if (ullon < MapServer.ROOT_ULLON) {
//            ullon = MapServer.ROOT_ULLON;
//        }
//        if (lrlon > MapServer.ROOT_LRLON) {
//            lrlon = MapServer.ROOT_LRLON;
//        }
//        if (ullat > MapServer.ROOT_ULLAT) {
//            ullat = MapServer.ROOT_ULLAT;
//        }
//        if (lrlat < MapServer.ROOT_LRLAT) {
//            lrlat = MapServer.ROOT_LRLAT;
//        }
    }

    private double calLonDPP() {
        return (lrlon - ullon) / w;
    }

    private void calDepth() {
        //Approximate calculation
        double LonDPP = calLonDPP();
        depth = -1;
        double zerothLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        for (int i = 0; i < 8; i++) {
            if (zerothLonDPP / Math.pow(2, i) <= LonDPP) {
                depth = i;
                break;
            }
        }
        if (depth == -1) {
            depth = 7;
        }
    }

    private void calGridDistance() {
        lonPerGrid = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        latPerGrid = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
    }

    private String pictureName(int x, int y) {
        return "d" + depth + "_x" + x + "_y" + y + ".png";
    }

    private void initialize(Map<String, Double> params) {
        loadParams(params);
        calDepth();
        calGridDistance();
    }

    private void choosePicture() {
        int upLeftX = (int) Math.floor((ullon - MapServer.ROOT_ULLON) / lonPerGrid);
        int upLeftY = (int) Math.floor((MapServer.ROOT_ULLAT - ullat) / latPerGrid);
        int lowRightX = (int) Math.floor((lrlon - MapServer.ROOT_ULLON) / lonPerGrid);
        int lowRightY = (int) Math.floor((MapServer.ROOT_ULLAT - lrlat) / latPerGrid);

        renderGrid = new String[lowRightY - upLeftY + 1][lowRightX - upLeftX + 1];
        for (int i = upLeftX; i <= lowRightX; i++) {
            for (int j = upLeftY; j <= lowRightY; j++) {
                renderGrid[j - upLeftY][i - upLeftX] = pictureName(i, j);
            }
         }

        raster.put("raster_ul_lon", MapServer.ROOT_ULLON + upLeftX * lonPerGrid);
        raster.put("raster_ul_lat", MapServer.ROOT_ULLAT - upLeftY * latPerGrid);
        raster.put("raster_lr_lon", MapServer.ROOT_ULLON + (lowRightX + 1) * lonPerGrid);
        raster.put("raster_lr_lat", MapServer.ROOT_ULLAT - (lowRightY + 1) * latPerGrid);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
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
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> result = new HashMap<>();
        try {
            initialize(params);
            choosePicture();
        } catch (Exception e) {
            result.put("render_grid", null);
            result.putAll(raster);
            result.put("depth", depth);
            result.put("query_success", querySuccess);
            System.out.println(e);
            return result;
        }
        System.out.println("Depth = " + depth);
        querySuccess = true;
        result.put("render_grid", renderGrid);
        result.putAll(raster);
        result.put("depth", depth);
        result.put("query_success", querySuccess);
        return result;
    }

}
