package byog.Core;

public class Position {
    private int xPos;
    private int yPos;
    /** 0 represents up, 1 represents left, 2 represents
     *  right and 3 represents down */
    private int direction;
    public Position(int x, int y) {
        xPos = x;
        yPos = y;
        direction = 0;
    }

    public Position(Position p) {
        xPos = p.xPos;
        yPos = p.yPos;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Position goUp() {
        return new Position(xPos, yPos + 1);
    }

    public Position goDown() {
        return new Position(xPos, yPos - 1);
    }

    public Position goLeft() {
        return new Position(xPos - 1, yPos);
    }

    public Position goRight() {
        return new Position(xPos + 1, yPos);
    }

    public String toString() {
        StringBuilder returnSB = new StringBuilder("(");
        returnSB.append(xPos);
        returnSB.append(", ");
        returnSB.append(yPos);
        returnSB.append(")");
        return returnSB.toString();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int d) {
        direction = d;
    }

}
