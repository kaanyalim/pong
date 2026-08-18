package com.example.pong.pong.scene;

import com.example.pong.pong.util.Args;
import com.example.pong.pong.PongApplication;
import com.example.pong.pong.PongContext;
import com.example.pong.pong.GameMode;
import com.example.pong.pong.ai.AIPaddle;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

import static com.example.pong.pong.PongApplication.RESOLUTION_HEIGHT;
import static com.example.pong.pong.PongApplication.RESOLUTION_WIDTH;
import static java.util.Objects.requireNonNull;


public class CourtScene extends AbstractScene {

	private static final int BOX_WIDTH = (RESOLUTION_WIDTH / 40);

	private static final int WALL_HEIGHT = BOX_WIDTH;

	private static final int PADDLE_HEIGHT = BOX_WIDTH * 5;

	private static final int EDGE_OFFSET = RESOLUTION_HEIGHT / 20;

	private static final double PADDLE_MOVEMENT_SPEED = 345.0;

	private static final double NUDGE = 0.01;

	private static int COUNTDOWN_TICKS = 50;


	private static final double DIRECTION_UP = -1.0;

	private static final double DIRECTION_DOWN = 1.0;

	private static final double DIRECTION_RIGHT = 1.0;

	private static final double DIRECTION_LEFT = -1.0;

	private static final double DIRECTION_NONE = 0.0;


	private static final double NUMBER_WIDTH = (RESOLUTION_WIDTH / 10);

	private static final double NUMBER_HEIGHT = (RESOLUTION_HEIGHT / 6);

	private static final double NUMBER_THICKNESS = NUMBER_HEIGHT / 5;


	private static final double BALL_INITIAL_SPEED = 400.0;

	private static final double BALL_SPEED_INCREASE = 50.0;


	private static final double BALL_MAX_SPEED = 800.0;
	private static final double MAX_BALL_Y_DIRECTION = 0.8;



	private final PongApplication application;
	private final PongContext ctx;

	private final Rectangle topWall;
	private final Rectangle bottomWall;

	private final Rectangle leftGoal;
	private final Rectangle rightGoal;

	private final Rectangle leftPaddle;
	private final Rectangle rightPaddle;

	private final Group leftScoreIndicator;
	private final Group rightScoreIndicator;

	private final Group centerLine;

	private final Rectangle ball;

	private final Random random = new Random();
	private AIPaddle aiPaddle;

	private double leftPaddleYDirection;
	private double rightPaddleYDirection;
	private double lastLeftPaddleDirection = DIRECTION_NONE;
	private double lastRightPaddleDirection = DIRECTION_NONE;

	private double ballMovementSpeed = BALL_INITIAL_SPEED;
	private double ballXDirection = DIRECTION_RIGHT;
	private double ballYDirection = DIRECTION_UP;

	private int countDown = COUNTDOWN_TICKS;

	public CourtScene(PongApplication application) throws NullPointerException {
		super(new Group(), RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		this.application = requireNonNull(application, "The application cannot be null!");
		this.ctx = requireNonNull(application.getContext(), "The context cannot be null!");

		if (ctx.getGameMode() == GameMode.SINGLE_PLAYER) {
			aiPaddle = new AIPaddle(ctx.getDifficulty(), PADDLE_MOVEMENT_SPEED, PADDLE_HEIGHT);
		}

		topWall = new Rectangle();
		topWall.setLayoutX(0);
		topWall.setLayoutY(0);
		topWall.setWidth(RESOLUTION_WIDTH);
		topWall.setHeight(WALL_HEIGHT);
		topWall.setFill(Color.WHITE);

		bottomWall = new Rectangle();
		bottomWall.setLayoutX(0);
		bottomWall.setLayoutY(RESOLUTION_HEIGHT - WALL_HEIGHT);
		bottomWall.setWidth(RESOLUTION_WIDTH);
		bottomWall.setHeight(WALL_HEIGHT);
		bottomWall.setFill(Color.WHITE);

		leftGoal = new Rectangle();
		leftGoal.setLayoutX(-RESOLUTION_WIDTH);
		leftGoal.setLayoutY(0);
		leftGoal.setWidth(RESOLUTION_WIDTH - BOX_WIDTH);
		leftGoal.setHeight(RESOLUTION_HEIGHT);

		rightGoal = new Rectangle();
		rightGoal.setLayoutX(RESOLUTION_WIDTH + BOX_WIDTH);
		rightGoal.setLayoutY(0);
		rightGoal.setWidth(RESOLUTION_WIDTH);
		rightGoal.setHeight(RESOLUTION_HEIGHT);

		leftPaddle = new Rectangle();
		leftPaddle.setLayoutX(EDGE_OFFSET);
		leftPaddle.setLayoutY(RESOLUTION_HEIGHT / 2 - PADDLE_HEIGHT / 2);
		leftPaddle.setWidth(BOX_WIDTH);
		leftPaddle.setHeight(PADDLE_HEIGHT);
		leftPaddle.setFill(Color.WHITE);

		rightPaddle = new Rectangle();
		rightPaddle.setLayoutX(RESOLUTION_WIDTH - EDGE_OFFSET - BOX_WIDTH);
		rightPaddle.setLayoutY(leftPaddle.getLayoutY());
		rightPaddle.setWidth(BOX_WIDTH);
		rightPaddle.setHeight(PADDLE_HEIGHT);
		rightPaddle.setFill(Color.WHITE);

		leftScoreIndicator = new Group();
		leftScoreIndicator.setLayoutX(RESOLUTION_WIDTH / 2 - (70 + RESOLUTION_WIDTH / 10));
		leftScoreIndicator.setLayoutY(RESOLUTION_HEIGHT / 10);

		rightScoreIndicator = new Group();
		rightScoreIndicator.setLayoutX(RESOLUTION_WIDTH / 2 + 70);
		rightScoreIndicator.setLayoutY(RESOLUTION_HEIGHT / 10);

		centerLine = new Group();
		centerLine.setLayoutX(RESOLUTION_WIDTH / 2 - BOX_WIDTH / 2);
		for (double y = WALL_HEIGHT; y < RESOLUTION_HEIGHT; y += (1.93 * BOX_WIDTH)) {
			Rectangle box = new Rectangle(0, y, BOX_WIDTH, BOX_WIDTH);
			box.setFill(Color.WHITE);
			centerLine.getChildren().add(box);
		}

		ball = new Rectangle();
		ball.setFill(Color.WHITE);
		ball.setLayoutX(RESOLUTION_WIDTH / 2 - BOX_WIDTH / 2);
		ball.setLayoutY(RESOLUTION_HEIGHT / 2 - BOX_WIDTH / 2);
		ball.setWidth(BOX_WIDTH);
		ball.setHeight(BOX_WIDTH);

		Parent root = getRoot();
		if (!(root instanceof Group)) {
			throw new AssertionError("The scene root is not a Group instance!");
		}

		Group rootGroup = (Group) root;
		ObservableList<Node> children = rootGroup.getChildren();
		children.add(topWall);
		children.add(bottomWall);
		children.add(leftGoal);
		children.add(rightGoal);
		children.add(leftPaddle);
		children.add(rightPaddle);
		children.add(leftScoreIndicator);
		children.add(rightScoreIndicator);
		children.add(centerLine);
		children.add(ball);

		setFill(Color.BLACK);

		if (ctx.getGameMode() == GameMode.MULTIPLAYER) {
			setOnKeyPressed(x -> {
				if (x.getCode() == KeyCode.ESCAPE) {
					application.getContext().reset();
					Stage primaryStage = application.getPrimaryStage();
					primaryStage.setScene(new WelcomeScene(application));
					return;
				}
				switch (x.getCode()) {
					case UP:
						rightPaddleYDirection = DIRECTION_UP;
						break;
					case DOWN:
						rightPaddleYDirection = DIRECTION_DOWN;
						break;
					case W:
						leftPaddleYDirection = DIRECTION_UP;
						break;
					case S:
						leftPaddleYDirection = DIRECTION_DOWN;
						break;
					default:
						break;
				}
			});

			setOnKeyReleased(x -> {
				switch (x.getCode()) {
					case UP:
						if (rightPaddleYDirection == DIRECTION_UP) {
							rightPaddleYDirection = DIRECTION_NONE;
						}
						break;
					case DOWN:
						if (rightPaddleYDirection == DIRECTION_DOWN) {
							rightPaddleYDirection = DIRECTION_NONE;
						}
						break;
					case W:
						if (leftPaddleYDirection == DIRECTION_UP) {
							leftPaddleYDirection = DIRECTION_NONE;
						}
						break;
					case S:
						if (leftPaddleYDirection == DIRECTION_DOWN) {
							leftPaddleYDirection = DIRECTION_NONE;
						}
						break;
					default:
						break;
				}
			});
		} else {
			setOnKeyPressed(x -> {
				if (x.getCode() == KeyCode.ESCAPE) {
					application.getContext().reset();
					Stage primaryStage = application.getPrimaryStage();
					primaryStage.setScene(new WelcomeScene(application));
					return;
				}
				switch (x.getCode()) {
					case W:
						leftPaddleYDirection = DIRECTION_UP;
						break;
					case S:
						leftPaddleYDirection = DIRECTION_DOWN;
						break;
					default:
						break;
				}
			});

			setOnKeyReleased(x -> {
				switch (x.getCode()) {
					case W:
						if (leftPaddleYDirection == DIRECTION_UP) {
							leftPaddleYDirection = DIRECTION_NONE;
						}
						break;
					case S:
						if (leftPaddleYDirection == DIRECTION_DOWN) {
							leftPaddleYDirection = DIRECTION_NONE;
						}
						break;
					default:
						break;
				}
			});
		}

		setPlayerScore(1, 0);
		setPlayerScore(2, 0);
	}

	@Override
	public void tick(double deltaTime) {
		if (countDown > 0) {
			countDown--;
			return;
		}

		// Update ball position with delta-time based movement
		double ballMovement = ballMovementSpeed * deltaTime;
		ball.setLayoutX(ball.getLayoutX() + ballMovement * ballXDirection);
		ball.setLayoutY(ball.getLayoutY() + ballMovement * ballYDirection);

		// Update paddle positions with delta-time based movement
		double paddleMovement = PADDLE_MOVEMENT_SPEED * deltaTime;
		leftPaddle.setLayoutY(leftPaddle.getLayoutY() + (leftPaddleYDirection * paddleMovement));
		
		if (ctx.getGameMode() == GameMode.SINGLE_PLAYER) {
			aiPaddle.update(rightPaddle, ball, deltaTime);
		} else {
			rightPaddle.setLayoutY(rightPaddle.getLayoutY() + (rightPaddleYDirection * paddleMovement));
		}

		// Store paddle directions for spin effect
		lastLeftPaddleDirection = leftPaddleYDirection;
		lastRightPaddleDirection = rightPaddleYDirection;

		Bounds topWallBounds = topWall.getBoundsInParent();
		Bounds bottomWallBounds = bottomWall.getBoundsInParent();

		// Constrain paddles to screen bounds
		Bounds rightPaddleBounds = rightPaddle.getBoundsInParent();
		if (rightPaddleBounds.intersects(topWallBounds)) {
			rightPaddle.setLayoutY(topWall.getLayoutY() + topWall.getHeight() + NUDGE);
		} else if (rightPaddleBounds.intersects(bottomWallBounds)) {
			rightPaddle.setLayoutY(bottomWallBounds.getMinY() - rightPaddle.getHeight() - NUDGE);
		}

		Bounds leftPaddleBounds = leftPaddle.getBoundsInParent();
		if (leftPaddleBounds.intersects(topWallBounds)) {
			leftPaddle.setLayoutY(topWall.getLayoutY() + topWall.getHeight() + NUDGE);
		} else if (leftPaddleBounds.intersects(bottomWallBounds)) {
			leftPaddle.setLayoutY(bottomWallBounds.getMinY() - leftPaddle.getHeight() - NUDGE);
		}

		// Ball collision detection
		Bounds ballBounds = ball.getBoundsInParent();
		if (ballBounds.intersects(leftPaddleBounds)) {
			handlePaddleCollision(ball, leftPaddle, ballBounds, leftPaddleBounds, DIRECTION_RIGHT, lastLeftPaddleDirection);
		} else if (ballBounds.intersects(rightPaddleBounds)) {
			handlePaddleCollision(ball, rightPaddle, ballBounds, rightPaddleBounds, DIRECTION_LEFT, lastRightPaddleDirection);
		} else if (ballBounds.intersects(topWallBounds)) {
			ball.setLayoutY(topWall.getLayoutY() + topWall.getHeight() + NUDGE);
			ballYDirection = DIRECTION_DOWN;
		} else if (ballBounds.intersects(bottomWallBounds)) {
			ball.setLayoutY(bottomWallBounds.getMinY() - ball.getHeight() - NUDGE);
			ballYDirection = DIRECTION_UP;
		} else {
			// Goal detection
			Bounds leftGoalBounds = leftGoal.getBoundsInParent();
			Bounds rightGoalBounds = rightGoal.getBoundsInParent();

			if (ballBounds.intersects(leftGoalBounds)) {
				int player1Score = ctx.incPlayer1Score();
				if (player1Score >= 10) {
					Stage primaryStage = application.getPrimaryStage();
					primaryStage.setScene(new EndGameScene(application));
				} else {
					setPlayerScore(1, player1Score);
					reset();
				}
			} else if (ballBounds.intersects(rightGoalBounds)) {
				int player2Score = ctx.incPlayer2Score();
				if (player2Score >= 10) {
					Stage primaryStage = application.getPrimaryStage();
					primaryStage.setScene(new EndGameScene(application));
				} else {
					setPlayerScore(2, player2Score);
					reset();
				}
			}
		}
	}

	private void handlePaddleCollision(Rectangle ball, Rectangle paddle, Bounds ballBounds, Bounds paddleBounds, double hitDirection, double paddleDirection) {
		// Position ball correctly
		if (hitDirection == DIRECTION_RIGHT) {
			ball.setLayoutX(paddleBounds.getMaxX() + NUDGE);
		} else {
			ball.setLayoutX(paddleBounds.getMinX() - ball.getWidth() - NUDGE);
		}

		// Set horizontal direction
		ballXDirection = hitDirection;

		// Calculate angle based on where ball hit the paddle
		double ballCenterY = ballBounds.getCenterY();
		double paddleCenterY = paddleBounds.getCenterY();
		double paddleTop = paddleBounds.getMinY();
		double paddleBottom = paddleBounds.getMaxY();
		double paddleHeight = paddleBottom - paddleTop;

		// Normalized position on paddle (-1 to 1, where -1 is top and 1 is bottom)
		double hitPosition = (ballCenterY - paddleCenterY) / (paddleHeight / 2);
		hitPosition = Math.max(-1, Math.min(1, hitPosition));

		// Set Y direction based on hit position
		ballYDirection = hitPosition * MAX_BALL_Y_DIRECTION;

		// Apply spin effect based on paddle movement direction
		if (paddleDirection == DIRECTION_UP) {
			ballYDirection -= 0.2;
		} else if (paddleDirection == DIRECTION_DOWN) {
			ballYDirection += 0.2;
		}

		// Clamp to max
		ballYDirection = Math.max(-MAX_BALL_Y_DIRECTION, Math.min(MAX_BALL_Y_DIRECTION, ballYDirection));

		// Increase speed
		ballMovementSpeed += BALL_SPEED_INCREASE;
		ballMovementSpeed = Math.min(ballMovementSpeed, BALL_MAX_SPEED);
	}



	private void reset() {
		// set the ball back into the middle of the scene.
		ball.setLayoutX(RESOLUTION_WIDTH / 2 - BOX_WIDTH / 2);
		ball.setLayoutY(RESOLUTION_HEIGHT / 2 - BOX_WIDTH / 2);

		// randomise a new direction for the ball.
		int randomValue = random.nextInt(3);
		switch (randomValue) {
			case 0:
				ballXDirection = DIRECTION_LEFT;
				ballYDirection = DIRECTION_UP;
				break;
			case 1:
				ballXDirection = DIRECTION_LEFT;
				ballYDirection = DIRECTION_DOWN;
				break;
			case 2:
				ballXDirection = DIRECTION_RIGHT;
				ballYDirection = DIRECTION_UP;
				break;
			case 3:
				ballXDirection = DIRECTION_RIGHT;
				ballYDirection = DIRECTION_DOWN;
				break;
			default:
				throw new IllegalStateException("Unsupport direction random: " + randomValue);
		}

		ballMovementSpeed = BALL_INITIAL_SPEED;

		leftPaddle.setLayoutY(RESOLUTION_HEIGHT / 2 - PADDLE_HEIGHT / 2);
		rightPaddle.setLayoutY(leftPaddle.getLayoutY());

		countDown = COUNTDOWN_TICKS;
	}


	private void setPlayerScore(int player, int score) throws IllegalArgumentException {
		Args.isBetween(player, 1, 2, "The number must be either one or two!");
		Args.isBetween(score, 0, 9, "The score must be within the [0..9] range!");

		if (player == 1) {
			ObservableList<Node> children = rightScoreIndicator.getChildren();
			children.clear();
			children.add(createNumberGroup(score));
		} else if (player == 2) {
			ObservableList<Node> children = leftScoreIndicator.getChildren();
			children.clear();
			children.add(createNumberGroup(score));
		}
	}


	private static Rectangle whiteRect(double x, double y, double w, double h) throws IllegalArgumentException {
		Args.isGte(x, 0, "The x-coordinate must be equal or higher than zero!");
		Args.isGte(y, 0, "The y-coordinate must be equal or higher than zero!");
		Args.isGte(w, 0, "The width must be equal or higher than zero!");
		Args.isGte(h, 0, "The heigh must be equal or higher than zero!");

		Rectangle rectangle = new Rectangle(x, y, w, h);
		rectangle.setFill(Color.WHITE);
		return rectangle;
	}


	private static Group createNumberGroup(int number) throws IllegalArgumentException {
		Args.isBetween(number, 0, 9, "The number must be within the [0..9] range!");

		Group group = new Group();
		ObservableList<Node> children = group.getChildren();
		switch (number) {
			case 0:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			case 1:
				children.add(whiteRect(NUMBER_WIDTH / 2 - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				break;
			case 2:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			case 3:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				break;
			case 4:
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				break;
			case 5:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, NUMBER_HEIGHT / 2, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			case 6:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, NUMBER_HEIGHT / 2, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			case 7:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				break;
			case 8:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			case 9:
				children.add(whiteRect(0, 0, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(0, 0, NUMBER_THICKNESS, NUMBER_HEIGHT / 2));
				children.add(whiteRect(0, NUMBER_HEIGHT / 2 - NUMBER_THICKNESS / 2, NUMBER_WIDTH, NUMBER_THICKNESS));
				children.add(whiteRect(NUMBER_WIDTH - NUMBER_THICKNESS, 0, NUMBER_THICKNESS, NUMBER_HEIGHT));
				children.add(whiteRect(0, NUMBER_HEIGHT - NUMBER_THICKNESS, NUMBER_WIDTH, NUMBER_THICKNESS));
				break;
			default:
				break;
		}

		return group;
	}

}
