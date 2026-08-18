package com.example.pong.pong.scene;

import com.example.pong.pong.GameMode;
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


public class ModeSelectionScene extends AbstractScene {

	private int selectedIndex = 0;
	private final PongApplication application;
	private final Text singleplayerText;
	private final Text multiplayerText;

	public ModeSelectionScene(PongApplication application) throws NullPointerException {
		super(new Group(), RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		this.application = requireNonNull(application, "The application cannot be null!");

		Text titleText = new Text("Spielmodus wählen:");
		titleText.setTextOrigin(VPos.CENTER);
		titleText.setFont(BIG_FONT);
		titleText.setFill(Color.WHITE);
		titleText.setLayoutY(RESOLUTION_HEIGHT / 6);

		singleplayerText = new Text("Singleplayer (gegen Bot)");
		singleplayerText.setTextOrigin(VPos.CENTER);
		singleplayerText.setFont(SMALL_FONT);
		singleplayerText.setLayoutY(RESOLUTION_HEIGHT / 2 - 40);
		singleplayerText.setFill(Color.YELLOW);

		multiplayerText = new Text("Multiplayer (2 Spieler)");
		multiplayerText.setTextOrigin(VPos.CENTER);
		multiplayerText.setFont(SMALL_FONT);
		multiplayerText.setLayoutY(RESOLUTION_HEIGHT / 2 + 40);
		multiplayerText.setFill(Color.WHITE);

		Text instructionsText = new Text("Pfeiltasten zum Navigieren, [ENTER] zum Bestätigen");
		instructionsText.setTextOrigin(VPos.CENTER);
		instructionsText.setFont(SMALL_FONT);
		instructionsText.setFill(Color.WHITE);
		instructionsText.setLayoutY(RESOLUTION_HEIGHT - 100);

		Parent root = getRoot();
		if (!(root instanceof Group)) {
			throw new AssertionError("The scene root is not a Group instance!");
		}

		Group rootGroup = (Group) root;
		ObservableList<Node> children = rootGroup.getChildren();
		children.add(titleText);
		children.add(singleplayerText);
		children.add(multiplayerText);
		children.add(instructionsText);

		centerText(titleText);
		centerText(singleplayerText);
		centerText(multiplayerText);
		centerText(instructionsText);

		setOnKeyReleased(x -> {
			if (x.getCode() == KeyCode.UP) {
				selectedIndex = (selectedIndex - 1 + 2) % 2;
				updateTextColor();
			} else if (x.getCode() == KeyCode.DOWN) {
				selectedIndex = (selectedIndex + 1) % 2;
				updateTextColor();
			} else if (x.getCode() == KeyCode.ENTER) {
				selectMode();
			}
		});

		setFill(Color.BLACK);
	}

	private void centerText(Text text) {
		text.setLayoutX((RESOLUTION_WIDTH - text.getLayoutBounds().getWidth()) / 2);
	}

	private void updateTextColor() {
		singleplayerText.setFill(selectedIndex == 0 ? Color.YELLOW : Color.WHITE);
		multiplayerText.setFill(selectedIndex == 1 ? Color.YELLOW : Color.WHITE);
	}

	private void selectMode() {
		Stage primaryStage = application.getPrimaryStage();

		if (selectedIndex == 0) {
			primaryStage.setScene(new DifficultySelectionScene(application));
		} else {
			application.getContext().setGameMode(GameMode.MULTIPLAYER);
			primaryStage.setScene(new CourtScene(application));
		}
	}

	@Override
	public void tick(double deltaTime) {
	}
}
