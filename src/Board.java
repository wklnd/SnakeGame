import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;


public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140; // Ticks / game-speed ?

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private int obstacle_x;
    private int obstacle_y;


    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image obstacle;

    private String playerName;
    private int score; // Antal Poäng, incrementeras vid varje äten äpple
    private Functions functions;

    public static boolean testing = false; // Gamemode för testing

    public Board() {

        initBoard();
    }

    /*
    * Initialize the game board
     */
    private void initBoard() {

        Functions functions = new Functions();

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    /*
    * Load images
     */
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();

        ImageIcon iio = new ImageIcon("src/resources/obstacle.png");
        obstacle = iio.getImage();
    }

    /*
    * Initialize the game
     */
    private void initGame() {

        if (testing == true) {
            playerName = "Test";
        } else {
            playerName = JOptionPane.showInputDialog("Skriv ditt namn: ");

        }

        dots = 3;
        score = 0;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    /*
    * Paint the game board
    * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    /*
    * creates graphics of game-objects on screen
    * @param g Graphics
    * */
    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);
            g.drawImage(obstacle, obstacle_x, obstacle_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }
    }

    /*
    * Game over
    * Writes game over text
    * Displays highscores
    * @param g Graphics
     */
    private void gameOver(Graphics g) {

        functions.setStats(playerName, score);
        functions.fileRead();
        functions.highScoreComparison();
        functions.updateHighScoreFile();

        String msg = "Game Over";
        String score_string = playerName + ", du fick : " + score + " poäng";
        String highScore_string = functions.returnArrayList();
        String highscoreText = "**** Leaderboard ****";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 -
                80);
        g.drawString(score_string, (B_WIDTH - metr.stringWidth(score_string)) / 2, B_HEIGHT / 2 - 60);
        g.drawString(highscoreText, (B_WIDTH - metr.stringWidth(highscoreText)) / 2, B_HEIGHT / 2 - 40);
        String[] highScores = highScore_string.split("\n");
        int lineHeight = g.getFontMetrics().getHeight();
        int y = B_HEIGHT / 2 - 20;

        for (String highScore : highScores) {
            g.drawString(highScore, (B_WIDTH - metr.stringWidth(highScore)) / 2, y);
            y += lineHeight;
        }
    }


    /*
    * Check if the snake has eaten an apple, if so, add a dot to the snake
     */
    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            score++; // Increment score
            locateApple();
        }
    }

    /*
    * Move the snake
     */
    void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    /*
    * check for collision with walls or obstacles to end the game
    * */
    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if ((x[0] == obstacle_x) && (y[0] == obstacle_y)) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }


    }

    /*
    * Places the apple in a random location on the map
     */
    void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        createObstacle();
    }

    /*
    * Creates an obstacle in a random location on the map
    * Can not create obstacle on top of the snake or the apple
    * */
    private void createObstacle() {
        boolean isOnSnake;
        boolean isOnApple;

        do {
            int r = (int) (Math.random() * RAND_POS);
            obstacle_x = r * DOT_SIZE;

            r = (int) (Math.random() * RAND_POS);
            obstacle_y = r * DOT_SIZE;

            isOnApple = (obstacle_x == apple_x) && (obstacle_y == apple_y);

            isOnSnake = false;
            for (int i = 0; i < dots; i++) {
                if (x[i] == obstacle_x && y[i] == obstacle_y) {
                    isOnSnake = true;
                    break;
                }
            }

        } while (isOnApple || isOnSnake);
    }

    /*
    * Function to update the game after every new frame
    *
    * */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    /**
     * Getters for apple and obstacle positions
     * @return int x or y position of apple, obstacle or snake
     */
    public int getAppleX() {
        return apple_x;
    }

    public int getAppleY() {
        return apple_y;
    }

    public int getObstacleX() {
        return obstacle_x;
    }

    public int getObstacleY() {
        return obstacle_y;
    }

    public boolean isInGame() {
        return inGame;
    }

    public int getSnakeX(int index) {
        return x[index];
    }

    public int getSnakeY(int index) {
        return y[index];
    }

    public int getSnakeLength() {
        return dots;
    }

    /*
    * Function used for testing
    * Identical to the "checkApple" function, expect without check for positions
     */
    public void snakeEatsApple() {
        dots++;
        score++;
        checkApple();
    }

    /*
    * Function for setting keybindings
    * and deciding direction of movement for the snake
    * */
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}