import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class BoardTest {

    private Board board;

    // Setup, skapar en ny instans av Board samt sätter testing till true ( Game mode )
    @Before
    public void setUp() {
        Board.testing = true;

        board = new Board();
    }

    // test för att kontrollera om äpplet hamnar i ett hinder
    @Test
    public void testAppleAndObstacle() {
        for (int i = 0; i < 100; i++) { // i = # of iterations
            board.locateApple();

            int appleX = board.getAppleX();
            int appleY = board.getAppleY();
            int obstacleX = board.getObstacleX();
            int obstacleY = board.getObstacleY();

            boolean samePosition = (appleX == obstacleX) && (appleY == obstacleY);
            assertFalse("(Iteration " + i + ")", samePosition);
        }
    }

    // test för att kontrollera så att ormen inte hamnar på hindret
    @Test
    public void testObstacleNotOnSnake() {
        for (int i = 0; i < 100; i++) {
            board.locateApple();

            int obstacleX = board.getObstacleX();
            int obstacleY = board.getObstacleY();

            boolean obstacleOnSnake = false;

            for (int z = 0; z < board.getSnakeLength(); z++) {
                int snakeX = board.getSnakeX(z);
                int snakeY = board.getSnakeY(z);

                if (obstacleX == snakeX && obstacleY == snakeY) {
                    obstacleOnSnake = true;
                    break;
                }
            }

            assertFalse("(Iteration " + i + ")", obstacleOnSnake);
        }
    }

    // test för att kontrollera så att ormen faktiskt växer när den äter ett äpple
    @Test
    public void testSnakeMaxLength() {
        for (int i = 0; i < 897; i++) {
            board.snakeEatsApple();
        }
        assertEquals(900, board.getSnakeLength());
    }

    @Test
    public void testScore() {
        board.snakeEatsApple();
        assertEquals(1, board.getScore());
    }

    @Test
    public void testScorelol() {
        board.snakeEatsApple();
        assertEquals(2, board.getScore());
    }
}