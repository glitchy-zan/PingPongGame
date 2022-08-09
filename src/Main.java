import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    Stage window;
    Canvas canvas;
    GraphicsContext gc;
    Timeline t1;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_R = 15;
    private int ballXspeed = 1;
    private int ballYspeed = 1;
    private double ballXposition = WIDTH / 2;
    private double ballYposition = WIDTH / 2;
    private double playerOneXPos = 0;
    private double playerTwoXPos = WIDTH - PLAYER_WIDTH;
    private double playerOneYPos = HEIGHT / 2;
    private double playerTwoYPos = HEIGHT / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        t1 = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        t1.setCycleCount(Timeline.INDEFINITE);

        // mouse control
        /*
        canvas.setOnMouseMoved(e -> {
            playerOneYPos = e.getY();
            playerTwoYPos = e.getY();
        });
         */

        canvas.setOnMouseClicked(e -> gameStarted = true);

        StackPane layout = new StackPane(canvas);
        Scene scene = new Scene(layout);

        // key control
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) playerOneYPos -= 5;
            else if (e.getCode() == KeyCode.S) playerOneYPos += 5;

            if (e.getCode() == KeyCode.UP) playerTwoYPos -= 5;
            else if (e.getCode() == KeyCode.DOWN) playerTwoYPos += 5;

        });

        window.setTitle("Ping Pong");
        window.setScene(scene);
        window.show();

        t1.play();
    }

    private void run(GraphicsContext gc) {
        // set background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // set text
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));

        if (gameStarted) {
            ballXposition += ballXspeed;
            ballYposition += ballYspeed;
            gc.fillOval(ballXposition, ballYposition, BALL_R, BALL_R);

            // computer AI hehe


        } else {
            gc.fillText("Start on click", WIDTH / 2, HEIGHT / 2);
            gc.setTextAlign(TextAlignment.CENTER);

            // reset ball position
            ballXposition = WIDTH / 2;
            ballYposition = HEIGHT / 2;

            // reset speed and direction
            ballXspeed = 1;
            ballYspeed = 1;
        }

        // draw players
        gc.fillRect(playerOneXPos, playerOneYPos, PLAYER_WIDTH, PLAYER_HEIGHT);
        gc.fillRect(playerTwoXPos, playerTwoYPos, PLAYER_WIDTH, PLAYER_HEIGHT);

        // draw score
        gc.fillText(String.valueOf(scoreP1), WIDTH / 4, HEIGHT / 4);
        gc.fillText(String.valueOf(scoreP2), WIDTH / 4 * 3, HEIGHT / 4);

        // count score
        if (ballXposition >= WIDTH - BALL_R) {
            gameStarted = false;
            scoreP1++;
        }
        if (ballXposition <= 0) {
            gameStarted = false;
            scoreP2++;
        }

        // direct ball horizontally
        if (ballXposition >= WIDTH - PLAYER_WIDTH - BALL_R) {
            if (ballYposition >= playerTwoYPos && ballYposition <= playerTwoYPos + PLAYER_HEIGHT) {
                ballXspeed *= -1;
                if (ballXspeed < 0) ballXspeed -= 0.1;
                else ballXspeed += 0.1;
            }
        } else if (ballXposition <= PLAYER_WIDTH) {
            if (ballYposition >= playerOneYPos && ballYposition <= playerOneYPos + PLAYER_HEIGHT) {
                ballXspeed *= -1;
                if (ballXspeed < 0) ballXspeed -= 0.1;
                else ballXspeed += 0.1;
            }
        }

        // direct ball vertically
        if (ballYposition >= HEIGHT - BALL_R) ballYspeed *= -1;
        if (ballYposition <= 0) ballYspeed *= -1;

    }
}
