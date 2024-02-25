public class Planet {
    private static final double G = 6.67e-11;
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    /** The constructor of the Planet instance */
    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }


    /** Another constructor that give a copy of input instance */
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /** Calculate the distance between two planets */
    public double calcDistance(Planet p){
        return Math.sqrt((this.xxPos - p.xxPos) * (this.xxPos - p.xxPos)
                + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos));
    }

    /** Calculate the force between two planets */
    public double calcForceExertedBy(Planet p){
        if (this.calcDistance(p) == 0){
            return 0;
        }
        return G * this.mass * p.mass / this.calcDistance(p) / this.calcDistance(p);
    }

    /** Calculate the force exerted in the X direction */
    public double calcForceExertedByX(Planet p){
        if (this.calcDistance(p) == 0){
            return 0;
        }
        return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
    }

    /** Calculate the force exerted in the Y direction */
    public double calcForceExertedByY(Planet p){
        if (this.calcDistance(p) == 0){
            return 0;
        }
        return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
    }

    /** Take in an array of Planets and calculate the net X force
     * exerted by all planets in that array upon the current Planet */
    public double calcNetForceExertedByX(Planet[] allPlanets){
        double netForceX = 0;
        for (Planet p : allPlanets){
            netForceX += calcForceExertedByX(p);
        }
        return netForceX;
    }

    /** Take in an array of Planets and calculate the net Y force
     * exerted by all planets in that array upon the current Planet */
    public double calcNetForceExertedByY(Planet[] allPlanets){
        double netForceY = 0;
        for (Planet p : allPlanets){
            netForceY += calcForceExertedByY(p);
        }
        return netForceY;
    }

    /** Calculate how much the forces exerted on the planet will cause that planet to accelerate,
     * and the resulting change in the planet’s velocity and position in a small period of time */
    public void update(double dt, double fx, double fy){
        double ax = fx / this.mass;
        double ay = fy / this.mass;
        this.xxVel += ax * dt;
        this.yyVel += ay * dt;
        this.xxPos += this.xxVel * dt;
        this.yyPos += this.yyVel * dt;
    }

    /** Draw a planet at its position */
    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + imgFileName);
    }
}
