package fr.byob.game.memeduel.core.gui.screen.level;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MemeDuelLoader;

public enum CannonBall {
	CANNON_BALL_SMALL(MemeDuelLoader.CANNON_BALL_ICON), CANNON_BALL_LARGE(MemeDuelLoader.CANNON_BALL_LARGE_ICON), CANNON_BALL_FRAG(MemeDuelLoader.CANNON_BALL_FRAG_ICON);

	private final GameImage icon;

	private CannonBall(final GameImage icon) {
		this.icon = icon;
	}

	public GameImage getIcon() {
		return this.icon;
	}
}