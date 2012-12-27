package fr.byob.game.memeduel.core;

import playn.core.Game;

public abstract class AbstractGame implements Game {

	@Override
	public void update(final float delta) {
		GameLoop.instance().update(delta);
	}

	@Override
	public void paint(final float alpha) {
		GameLoop.instance().paint(alpha);
	}

	@Override
	public int updateRate() {
		return 32;
	}

}
