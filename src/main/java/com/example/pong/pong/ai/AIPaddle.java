package com.example.pong.pong.ai;

import com.example.pong.pong.Difficulty;
import javafx.scene.shape.Rectangle;

public class AIPaddle {

    private static final double DIRECTION_UP = -1.0;
    private static final double DIRECTION_DOWN = 1.0;

    private final Difficulty difficulty;
    private final double paddleMovementSpeed;
    private final double paddleHeight;

    public AIPaddle(Difficulty difficulty, double basePaddleMovementSpeed, double paddleHeight) {
        this.difficulty = difficulty;
        this.paddleMovementSpeed = basePaddleMovementSpeed * difficulty.getSpeedMultiplier();
        this.paddleHeight = paddleHeight;
    }

    public void update(Rectangle paddle, Rectangle ball, double deltaTime) {
        double ballCenterY = ball.getLayoutY() + ball.getHeight() / 2;
        double paddleCenterY = paddle.getLayoutY() + paddleHeight / 2;

        double movement = paddleMovementSpeed * deltaTime;
        if (ballCenterY < paddleCenterY - 5) {
            paddle.setLayoutY(paddle.getLayoutY() + (DIRECTION_UP * movement));
        } else if (ballCenterY > paddleCenterY + 5) {
            paddle.setLayoutY(paddle.getLayoutY() + (DIRECTION_DOWN * movement));
        }
    }
}
