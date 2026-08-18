package com.example.pong.pong;


public class PongContext {

	private int player1Score = 0;
	private int player2Score = 0;
	private GameMode gameMode = GameMode.MULTIPLAYER;
	private Difficulty difficulty = Difficulty.MEDIUM;

	public PongContext() {
		// ...
	}

	public void reset() {
		player1Score = 0;
		player2Score = 0;
	}

	public int getPlayer1Score() {
		return player1Score;
	}

	public void setPlayer1Score(int player1Score) {
		this.player1Score = player1Score;
	}

	public int incPlayer1Score() {
		player1Score++;
		return player1Score;
	}

	public int getPlayer2Score() {
		return player2Score;
	}

	public void setPlayer2Score(int player2Score) {
		this.player2Score = player2Score;
	}

	public int incPlayer2Score() {
		player2Score++;
		return player2Score;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

}
