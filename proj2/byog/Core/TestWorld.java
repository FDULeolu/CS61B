package byog.Core;

import byog.TileEngine.TERenderer;

public class TestWorld {
    public static void testGeneralizeWorld() {
        World w = new World(80, 30, 44777777);
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        w.generalizeWorld();
        ter.renderFrame(w.getWorld());
    }

    public static void main(String[] args) {
        testGeneralizeWorld();
    }
}
