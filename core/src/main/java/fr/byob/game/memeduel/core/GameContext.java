package fr.byob.game.memeduel.core;

import java.util.HashMap;
import java.util.Map;

import fr.byob.game.box2d.Box2D;
import fr.byob.game.memeduel.core.net.Base64;
import fr.byob.game.memeduel.core.net.WebResource;

public class GameContext {

	public final static String WEB_RESOURCE = "WEB_RESOURCE";
	public final static String BASE_64 = "BASE_64";
	public final static String BOX2D = "BOX2D";

	private final static GameContext instance = new GameContext();

	protected final Map<String, Object> values;

	private GameContext() {
		values = new HashMap<String, Object>();
	}

	public static GameContext instance() {
		return instance;
	}

	public <T> T getValue(final String key) {
		return (T) values.get(key);
	}

	public void setValue(final String key, final Object value) {
		values.put(key, value);
	}

	public Box2D getBox2D() {
		return getValue(BOX2D);
	}

	public void setBox2D(final Box2D box2D) {
		setValue(BOX2D, box2D);
	}

	public WebResource getWebResource() {
		return getValue(WEB_RESOURCE);
	}

	public void setWebResource(final WebResource webResource) {
		setValue(WEB_RESOURCE, webResource);
	}

	public Base64 getBase64() {
		return getValue(BASE_64);
	}

	public void setBase64(final Base64 base64) {
		setValue(BASE_64, base64);
	}
}
