package fr.byob.game.memeduel.core.god;

import java.util.HashMap;
import java.util.Map;

import fr.byob.game.memeduel.core.god.helper.LoadHelper;


public abstract class GameOptionsGOD extends GODLoadable<GameOptionsGOD> implements GameObjectDefinition {

	protected float xOffset;
	protected final Map<String, LoadHelper> helpers = new HashMap<String, LoadHelper>();

	public <T extends LoadHelper> T getLoadHelper(final String type) {
		return (T) helpers.get(type);
	}

	public Map<String, LoadHelper> getLoadHelpers() {
		return helpers;
	}

	public float getXOffset() {
		return this.xOffset;
	}

}
