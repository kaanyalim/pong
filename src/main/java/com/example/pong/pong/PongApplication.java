package com.example.pong.pong;

import com.example.pong.pong.scene.AbstractScene;
import com.example.pong.pong.scene.WelcomeScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PongApplication extends Application {

	public static final int RESOLUTION_WIDTH = 800;

	public static final int RESOLUTION_HEIGHT = 600;

	public static final String FONT_NAME = "Arial";

	public static final Font BIG_FONT = new Font(FONT_NAME, 32);

	public static final Font SMALL_FONT = new Font(FONT_NAME, 18);

	private Stage primaryStage;
	private AnimationTimer mainLoop;
	private PongContext context;
	private long lastFrameTime = 0;

	@Override
	public void init() throws Exception {
		// ... something here?
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// store the primary stage reference.
		this.primaryStage = primaryStage;

		// construct the context for the game.
		context = new PongContext();

		// set definitions for the primary stage.
		primaryStage.setTitle("JavaFX Pong - Karavin Mert, Yalim Kaan");
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setScene(new WelcomeScene(this));

		// construct and start a main loop with delta-time based movement
		mainLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {
				double deltaTime = (lastFrameTime == 0) ? 0.016 : (now - lastFrameTime) / 1_000_000_000.0;
				lastFrameTime = now;

				Scene scene = primaryStage.getScene();
				if (scene instanceof AbstractScene) {
					((AbstractScene) scene).tick(deltaTime);
				}
			}

		};
		mainLoop.start();

	}

	@Override
	public void stop() throws Exception {
		mainLoop.stop();
		super.stop();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public PongContext getContext() {
		return context;
	}

	public static void main(String args[]) {
		launch(args);
	}

}
