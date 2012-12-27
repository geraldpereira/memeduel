package fr.byob.game.memeduel.core.controller;


public enum GameMode {
	EDIT("json/gameEdit.json", "json/gameEditOptions.json"), TEST("json/gameTest.json", "json/gameTestOptions.json"), PLAY("json/gameTest.json", "json/gameTestOptions.json"), PREVIEW(null, "json/gamePreviewOptions.json");

	private final String gameFileName;
	private final String gameOptionsFileName;

	private GameMode(final String gameFileName, final String gameOptionsFileName) {
		this.gameFileName = gameFileName;
		this.gameOptionsFileName = gameOptionsFileName;
	}

	public String getGameFileName() {
		return this.gameFileName;
	}

	public String getGameOptionsFileName() {
		return this.gameOptionsFileName;
	}
}
