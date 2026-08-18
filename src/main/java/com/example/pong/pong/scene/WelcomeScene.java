package com.example.pong.pong.scene;

import com.example.pong.pong.PongApplication;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.pong.pong.PongApplication.*;
import static java.util.Objects.requireNonNull;


public class WelcomeScene extends AbstractScene {

	private final Text topicText;
	private final Text leftControlsTopicText;
	private final Text leftControlsText;
	private final Text rightControlsTopicText;
	private final Text rightControlsText;
	private final Text proceedInstructionsText;

	public WelcomeScene(PongApplication application) throws NullPointerException {
		super(new Group(), RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		requireNonNull(application, "The application cannot be null!");

		topicText = new Text("JavaFX Pong");
		topicText.setTextOrigin(VPos.CENTER);
		topicText.setFont(BIG_FONT);
		topicText.setLayoutX((RESOLUTION_WIDTH - topicText.prefWidth(-1)) / 2);
		topicText.setLayoutY(RESOLUTION_HEIGHT / 6);
		topicText.setFill(Color.WHITE);

		leftControlsTopicText = new Text("Steuerung linker Spieler:");
		leftControlsTopicText.setTextOrigin(VPos.CENTER);
		leftControlsTopicText.setFont(SMALL_FONT);
		leftControlsTopicText.setLayoutX((RESOLUTION_WIDTH - leftControlsTopicText.prefWidth(-1)) / 2);
		leftControlsTopicText.setLayoutY(topicText.getLayoutY() + 100);
		leftControlsTopicText.setFill(Color.WHITE);

		leftControlsText = new Text("W & S");
		leftControlsText.setTextOrigin(VPos.CENTER);
		leftControlsText.setFont(SMALL_FONT);
		leftControlsText.setLayoutX((RESOLUTION_WIDTH - leftControlsText.prefWidth(-1)) / 2);
		leftControlsText.setLayoutY(leftControlsTopicText.getLayoutY() + 40);
		leftControlsText.setFill(Color.WHITE);

		rightControlsTopicText = new Text("Steuerung rechter Spieler:");
		rightControlsTopicText.setTextOrigin(VPos.CENTER);
		rightControlsTopicText.setFont(SMALL_FONT);
		rightControlsTopicText.setLayoutX((RESOLUTION_WIDTH - rightControlsTopicText.prefWidth(-1)) / 2);
		rightControlsTopicText.setLayoutY(leftControlsText.getLayoutY() + 60);
		rightControlsTopicText.setFill(Color.WHITE);

		rightControlsText = new Text("Pfeil-taste oben & Pfeil-taste unten");
		rightControlsText.setTextOrigin(VPos.CENTER);
		rightControlsText.setFont(SMALL_FONT);
		rightControlsText.setLayoutX((RESOLUTION_WIDTH - rightControlsText.prefWidth(-1)) / 2);
		rightControlsText.setLayoutY(rightControlsTopicText.getLayoutY() + 40);
		rightControlsText.setFill(Color.WHITE);

		proceedInstructionsText = new Text("Drücke [ENTER] um das Spiel zu starten");
		proceedInstructionsText.setTextOrigin(VPos.CENTER);
		proceedInstructionsText.setFont(SMALL_FONT);
		proceedInstructionsText.setLayoutX((RESOLUTION_WIDTH - proceedInstructionsText.prefWidth(-1)) / 2);
		proceedInstructionsText.setLayoutY(rightControlsTopicText.getLayoutY() + 160);
		proceedInstructionsText.setFill(Color.WHITE);

		Text escapeInstructionsText = new Text("Während des Spiels: [ESC] um zum Menü zu gehen");
		escapeInstructionsText.setTextOrigin(VPos.CENTER);
		escapeInstructionsText.setFont(SMALL_FONT);
		escapeInstructionsText.setLayoutX((RESOLUTION_WIDTH - escapeInstructionsText.prefWidth(-1)) / 2);
		escapeInstructionsText.setLayoutY(proceedInstructionsText.getLayoutY() + 50);
		escapeInstructionsText.setFill(Color.YELLOW);

		Parent root = getRoot();
		if (!(root instanceof Group)) {
			throw new AssertionError("The scene root is not a Group instance!");
		}

		Group rootGroup = (Group) root;
		ObservableList<Node> children = rootGroup.getChildren();
		children.add(topicText);
		children.add(leftControlsTopicText);
		children.add(leftControlsText);
		children.add(rightControlsTopicText);
		children.add(rightControlsText);
		children.add(proceedInstructionsText);
		children.add(escapeInstructionsText);

		setOnKeyReleased(x -> {
			if (x.getCode() == KeyCode.ENTER) {

				Stage primaryStage = application.getPrimaryStage();
				primaryStage.setScene(new ModeSelectionScene(application));
			}
		});

		setFill(Color.BLACK);
	}

	@Override
	public void tick(double deltaTime) {
	}

}
