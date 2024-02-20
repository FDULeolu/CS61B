package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.enableDoubleBuffering();
        World w = new World();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Draw welcome page
        Font font50 = new Font("Arial", Font.PLAIN, 50);
        Font font20 = new Font("Arial", Font.PLAIN, 20);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        StdDraw.setFont(font50);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.8, "CS61B: THE GAME");

        StdDraw.setFont(font20);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.45, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.4, "Quit (Q)");
        StdDraw.show();


        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'l': {
                        w = World.loadWorld();
                        ter.renderFrame(w.getWorld());
                        break;
                    }
                    case 'n': {
                        char in = 'n';
                        String inputString = "";
                        while (in != 's') {
                            StdDraw.clear(StdDraw.BLACK);
                            StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "Enter a number you like!");
                            StdDraw.text(WIDTH / 2, HEIGHT * 0.45, "(Ending with an 's' when you're done)");
                            StdDraw.text(WIDTH / 2, HEIGHT * 0.3, inputString);
                            StdDraw.show();
                            if (StdDraw.hasNextKeyTyped()) {
                                in = StdDraw.nextKeyTyped();
                                if (in == '\b' && inputString.length() > 0) {
                                    StringBuilder sb = new StringBuilder(inputString);
                                    sb.deleteCharAt(sb.length() - 1);
                                    inputString = sb.toString();
                                    continue;
                                }
                                inputString += in;
                            }
                        }
                        w.createWorld(WIDTH, HEIGHT, getSeed(inputString));
                        w.generalizeWorld();
                        ter.renderFrame(w.getWorld());
                        break;
                    }
                    case 'q': {
                        if (w.isHasGeneralized()) {
                            World.saveWorld(w);
                        }
                        System.exit(0);
                    }
                    default:
                }
            }
        }

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame;
        World world = new World();
        if (input.charAt(0) == 'l') {
            world = World.loadWorld();
            finalWorldFrame = world.getWorld();
            return finalWorldFrame;
        } else {
            int seed = getSeed(input);
            world.createWorld(HEIGHT, WIDTH, seed);
            world.generalizeWorld();
        }

        // save the generalized world
        if (input.charAt(input.length() - 1) == 'q') {
            World.saveWorld(world);
        }
        finalWorldFrame = world.getWorld();
        return finalWorldFrame;
    }

    private static int getSeed(String input) {
        int ptr1 = 0;
        int ptr2 = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'n') {
                ptr1 = i;
            } else if (input.charAt(i) == 's') {
                ptr2 = i;
            }
        }
        int seed;
        try {
            String stringNum = input.substring(ptr1 + 1, ptr2);
            seed = Integer.parseInt(stringNum);
        } catch (Exception e) {
            throw new RuntimeException("Illegal input!");
        }
        return seed;
    }
}

