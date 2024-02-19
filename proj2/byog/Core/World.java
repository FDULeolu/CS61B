package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;
import java.util.Random;

public class World implements Serializable {

    private static final long serialVersionUID = 123123123123123L;
    private Random r;
    private int WIDTH;
    private int HEIGHT;
    private TETile[][] world;

    /** Initialize a new world */
    public void createWorld(int w, int h, int seed) {
        world = new TETile[w][h];
        r = new Random(seed);
        WIDTH = w;
        HEIGHT = h;
    }

    /** Load the existing world, or create a new world */
    public static World loadWorld() {
        File f = new File("./RandomWorld/world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream((fs));
                World loadWorld = (World) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found!");
                System.exit(0);
            }
        }
        return null;
    }

    /** Save the word instance that have been generalized */
    public static void saveWorld(World w) {
        File f = new File("./RandomWorld/world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /** A Nested Toolkit Class for generalizing world */
    private class GeneralizeHelper {

        /** Returns true if the position is out of the limitation */
        private boolean isOut(Position p) {
            if (p.getXPos() < 1 || p.getXPos() > WIDTH - 2
                    || p.getYPos() < 1 || p.getYPos() > HEIGHT - 2) {
                return true;
            }
            return false;
        }

        /** Choose a position to start random walk and make sure there is enough space to
         *  generate the wall */
        private Position startPosition() {
            int x = 2 + r.nextInt(WIDTH - 1);
            int y = 2 + r.nextInt(HEIGHT - 1);
            return new Position(x, y);
        }

        /** GO up, down, left or right randomly and the "PATH" should
         *  leave enough space to generate the wall */
        private Position randomWalk(Position p) {
            int chooseDirection = r.nextInt(4);
            Position newPos;

            // Choose a direction randomly
            switch (chooseDirection) {
                case 0: {
                    if ((chooseDirection + p.getDirection()) == 3) {
                        return randomWalk(p);
                    }
                    newPos = new Position(p.goUp());
                    newPos.setDirection(chooseDirection);
                    break;
                }
                case 1: {
                    if ((chooseDirection + p.getDirection()) == 3) {
                        return randomWalk(p);
                    }
                    newPos = new Position(p.goLeft());
                    newPos.setDirection(chooseDirection);
                    break;
                }
                case 2: {
                    if ((chooseDirection + p.getDirection()) == 3) {
                        return randomWalk(p);
                    }
                    newPos = new Position(p.goRight());
                    newPos.setDirection(chooseDirection);
                    break;
                }
                default: {
                    if ((chooseDirection + p.getDirection()) == 3) {
                        return randomWalk(p);
                    }
                    newPos = new Position(p.goDown());
                    newPos.setDirection(chooseDirection);
                    break;
                }
            }

            // Leave enough space to generate the wall
            if (isOut(newPos)) {
                return randomWalk(p);
            }

            return newPos;
        }

        /** Generate walls around the path */
        private void generateWalls(Position p, TETile[][] t) {
            Position[] positions = new Position[8];
            int x = p.getXPos();
            int y = p.getYPos();
            positions[0] = new Position(x - 1, y + 1);
            positions[1] = new Position(x , y + 1);
            positions[2] = new Position(x + 1, y + 1);
            positions[3] = new Position(x - 1, y);
            positions[4] = new Position(x + 1, y);
            positions[5] = new Position(x - 1, y - 1);
            positions[6] = new Position(x , y - 1);
            positions[7] = new Position(x + 1, y - 1);

            for (int i = 0; i < 8; i++) {
                int xpos = positions[i].getXPos();
                int ypos = positions[i].getYPos();
                if (t[xpos][ypos].equals(Tileset.NOTHING)) {
                    t[xpos][ypos] = Tileset.WALL;
                }
            }
        }
    }

    /** Generalize a new world randomly (Version 1.0)*/
    public void generalizeWorld() {

        // Fill the TETile 2D Array with `Nothing`
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }

        // Generalize "Path" and "Room" randomly
        double roomRatio = 0.8 + 0.2 * r.nextDouble();
        int distance = 1;
        GeneralizeHelper gh = new GeneralizeHelper();
        Position ptr = gh.startPosition();
        world[ptr.getXPos()][ptr.getYPos()] = Tileset.FLOOR;
        while ((double) distance / (WIDTH * HEIGHT) < roomRatio) {
            ptr = gh.randomWalk(ptr);
//            System.out.println(ptr.toString());
            world[ptr.getXPos()][ptr.getYPos()] = Tileset.FLOOR;
            distance++;
        }

        // Generalize "Walls" around the "Path"
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    gh.generateWalls(new Position(i, j), world);
                }
            }
        }

    }

    public TETile[][] getWorld() {
        return world;
    }


}
