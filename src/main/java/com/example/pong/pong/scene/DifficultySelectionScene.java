package com.example.pong.pong.scene;

import com.example.pong.pong.Difficulty;
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


public class DifficultySelectionScene extends AbstractScene {

	private int selectedIndex = 0;
	private final PongApplication application;
	private final Text easyText;
	private final Text mediumText;
	private final Text hardText;

	public DifficultySelectionScene(PongApplication application) throws NullPointerException {
		super(new Group(), RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		this.application = requireNonNull(application, "The application cannot be null!");

		Text titleText = new Text("Schwierigkeitsstufe wählen:");
		titleText.setTextOrigin(VPos.CENTER);
		titleText.setFont(BIG_FONT);
		titleText.setFill(Color.WHITE);
		titleText.setLayoutY(RESOLUTION_HEIGHT / 6);

		easyText = new Text("Easy");
		easyText.setTextOrigin(VPos.CENTER);
		easyText.setFont(SMALL_FONT);
		easyText.setLayoutY(RESOLUTION_HEIGHT / 2 - 60);
		easyText.setFill(Color.YELLOW);

		mediumText = new Text("Medium");
		mediumText.setTextOrigin(VPos.CENTER);
		mediumText.setFont(SMALL_FONT);
		mediumText.setLayoutY(RESOLUTION_HEIGHT / 2);
		mediumText.setFill(Color.WHITE);

		hardText = new Text("Hard");
		hardText.setTextOrigin(VPos.CENTER);
		hardText.setFont(SMALL_FONT);
		hardText.setLayoutY(RESOLUTION_HEIGHT / 2 + 60);
		hardText.setFill(Color.WHITE);

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
		children.add(easyText);
		children.add(mediumText);
		children.add(hardText);
		children.add(instructionsText);

		centerText(titleText);
		centerText(easyText);
		centerText(mediumText);
		centerText(hardText);
		centerText(instructionsText);

		setOnKeyReleased(x -> {
			if (x.getCode() == KeyCode.UP) {
				selectedIndex = (selectedIndex - 1 + 3) % 3;
				updateTextColor();
			} else if (x.getCode() == KeyCode.DOWN) {
				selectedIndex = (selectedIndex + 1) % 3;
				updateTextColor();
			} else if (x.getCode() == KeyCode.ENTER) {
				selectDifficulty();
			}
		});

		setFill(Color.BLACK);
	}

	private void centerText(Text text) {
		text.setLayoutX((RESOLUTION_WIDTH - text.getLayoutBounds().getWidth()) / 2);
	}

	private void updateTextColor() {
		easyText.setFill(selectedIndex == 0 ? Color.YELLOW : Color.WHITE);
		mediumText.setFill(selectedIndex == 1 ? Color.YELLOW : Color.WHITE);
		hardText.setFill(selectedIndex == 2 ? Color.YELLOW : Color.WHITE);
	}

	private void selectDifficulty() {
		Difficulty[] difficulties = {Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD};
		application.getContext().setDifficulty(difficulties[selectedIndex]);
		application.getContext().setGameMode(GameMode.SINGLE_PLAYER);

		Stage primaryStage = application.getPrimaryStage();
		primaryStage.setScene(new CourtScene(application));
	}

	@Override
	public void tick(double deltaTime) {
	}
}
