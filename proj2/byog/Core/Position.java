package byog.Core;

public class Position {
    private int xPos;
    private int yPos;

    public Position(int x, int y) {
        xPos = x;
        yPos = y;
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



}
