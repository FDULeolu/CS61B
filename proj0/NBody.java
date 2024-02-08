/** A class that will actually run the simulation */
public class NBody {

    /** Return the radius of the universe from the input file */
    public static double readRadius(String fileName){
        In in = new In(fileName);
        int numOfPlanets = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    /** Return an array of Planets from the input file */
    public static Planet[] readPlanets(String fileName){
        In in = new In(fileName);
        int numOfPlanets = in.readInt();
        double radius = in.readDouble();
        Planet[] allPlanets = new Planet[numOfPlanets];
        for (int i = 0; i < numOfPlanets; i += 1){
            allPlanets[i] = new Planet();
            allPlanets[i].xxPos = in.readDouble();
            allPlanets[i].yyPos = in.readDouble();
            allPlanets[i].xxVel = in.readDouble();
            allPlanets[i].yyVel = in.readDouble();
            allPlanets[i].mass = in.readDouble();
            allPlanets[i].imgFileName = in.readString();
        }
        return allPlanets;
    }

    public static void main(String[] args){
        /** Check whether the input is illegal */
        if (args.length != 3){
            System.out.println("You should type in correct information including T, dt and filename!");
        }
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);

        String imageToDraw = "images/starfield.jpg";

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        StdDraw.enableDoubleBuffering();

        int time = 0;
        while (time < T){
            double[] xForces = new double[allPlanets.length];
            double[] yForces = new double[allPlanets.length];
            for (int i = 0; i < allPlanets.length; i += 1){
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, imageToDraw, 2 * radius, 2 * radius);
            for (Planet p : allPlanets){
                p.draw();
            }

            StdDraw.show();
            StdDraw.pause(10);

            time += dt;
        }
    }
}
