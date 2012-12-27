package fr.byob.game.memeduel.core.controller;

public enum SelectionType {
	NONE(-1), SELECTED(0xFF0000FF), MAIN_SELECTION(0xFFFF0000), LOCKED(0xFF00FF00);

	private int color;

	private SelectionType(final int color) {
		this.color = color;
	}

	public int getColor() {
		return this.color;
	}

}
