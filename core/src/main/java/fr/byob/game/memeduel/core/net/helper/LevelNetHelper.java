package fr.byob.game.memeduel.core.net.helper;

import playn.core.util.Callback;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.net.ContentType;
import fr.byob.game.memeduel.core.net.NetUtils;
import fr.byob.game.memeduel.core.net.WebResource;
import fr.byob.game.memeduel.domain.Level;
import fr.byob.game.memeduel.domain.User;

public class LevelNetHelper {

	private final static String URL = NetUtils.BASE_URL + "level";

	public void add(final User user, final Level level, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/add").header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).data(LevelBuilder.toJSON(level)).post(callback);
	}

	public void update(final User user, final Level level, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/update").header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).data(LevelBuilder.toJSON(level)).post(callback);
	}

	public void owned(final User user, final String owner, final int limit, final int offset, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/owned/" + owner + "/" + limit + "/" + offset).header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).get(callback);
	}

	public void demo(final int limit, final int offset, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/demo/" + limit + "/" + offset).contentType(ContentType.APPLICATION_JSON).get(callback);
	}

	public void latest(final User user, final int limit, final int offset, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/latest/" + limit + "/" + offset).header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).get(callback);
	}

	public void delete(final User user, final Level level, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/delete/" + level.getId()).header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).get(callback);
	}

}
