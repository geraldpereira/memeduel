package fr.byob.game.memeduel.core.god;

import playn.core.util.Callback;
import fr.byob.game.memeduel.core.controller.GameMode;

public class MemeDuelGODLoader extends AllGODLoader {

	private GameMode mode = null;

	public void loadFromGameModeAndLevelFile(final Callback<AllGODLoader> callback, final GameMode mode, final String levelFileName) {
		this.mode = mode;
		super.loadFromFile(callback, mode.getGameFileName(), mode.getGameOptionsFileName(), levelFileName);
	}

	public void loadFromGameModeAndLevelContent(final Callback<AllGODLoader> callback, final GameMode mode, final String levelContent) {
		this.mode = mode;
		super.loadFromContent(callback, mode.getGameFileName(), mode.getGameOptionsFileName(), levelContent);
	}

	@Override
	public MemeDuelLevelGOD getLevelGOD() {
		return (MemeDuelLevelGOD) super.getLevelGOD();
	}

	@Override
	public MemeDuelGameOptionsGOD getGameOptionsGOD() {
		return (MemeDuelGameOptionsGOD) super.getGameOptionsGOD();
	}

	public GameMode getGameMode() {
		return this.mode;
	}

	@Override
	protected MemeDuelGameOptionsGOD newGameOptionsGOD() {
		return new MemeDuelGameOptionsGOD(this);
	}

	@Override
	protected MemeDuelLevelGOD newLevelGOD() {
		return new MemeDuelLevelGOD(this);
	}

}
