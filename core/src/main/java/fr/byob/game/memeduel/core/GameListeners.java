package fr.byob.game.memeduel.core;

import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.PlayN;
import playn.core.Touch;

public class GameListeners {

	private final static GameListeners instance = new GameListeners();


	private Touch.Listener touchListener;
	private Mouse.Listener mouseListener;
	private Keyboard.Listener keyListener;

	private GameListeners() {
	}

	public static GameListeners instance() {
		return instance;
	}


	public void setListener(final Touch.Listener listener) {
		touchListener = listener;
		PlayN.touch().setListener(listener);
	}

	public void removeListener(final Touch.Listener listener) {
		if (listener == touchListener) {
			PlayN.touch().setListener(null);
		}
	}

	public void setListener(final Mouse.Listener listener) {
		mouseListener = listener;
		PlayN.mouse().setListener(listener);
	}

	public void removeListener(final Mouse.Listener listener) {
		if (listener == mouseListener) {
			PlayN.mouse().setListener(null);
		}
	}

	public void setListener(final Keyboard.Listener listener) {
		keyListener = listener;
		PlayN.keyboard().setListener(listener);
	}

	public void removeListener(final Keyboard.Listener listener) {
		if (listener == keyListener) {
			PlayN.keyboard().setListener(null);
		}
	}
}
