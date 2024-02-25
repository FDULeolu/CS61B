package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.rand = new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {

        StdDraw.clear();
        StdDraw.clear(Color.black);

        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(width / 2, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) throws InterruptedException {
        for (char s : letters.toCharArray()) {
            drawFrame(String.valueOf(s));
            StdDraw.pause(750);
            drawFrame(" ");
            StdDraw.pause(750);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder inputString = new StringBuilder();
        while (inputString.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                inputString.append(StdDraw.nextKeyTyped());
            }
            drawFrame(inputString.toString());
        }
        StdDraw.pause(1000);
        return inputString.toString();
    }

    public void startGame() throws InterruptedException {
        round = 1;
        gameOver = false;
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round:" + round);
            StdDraw.pause(1000);
            String generatedString = generateRandomString(round);
            flashSequence(generatedString);
            playerTurn = true;
            String answerString = solicitNCharsInput(round);
            if (!answerString.equals(generatedString)) {
                gameOver = true;
            }
            round++;
        }
        drawFrame("Game Over! Your level is " + round);
    }

}
