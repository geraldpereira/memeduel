package fr.byob.game.memeduel.core.net.helper;

import playn.core.util.Callback;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.net.ContentType;
import fr.byob.game.memeduel.core.net.NetUtils;
import fr.byob.game.memeduel.core.net.WebResource;
import fr.byob.game.memeduel.domain.User;

public class UserNetHelper {

	private final static String URL = NetUtils.BASE_URL + "user";

	public void subscribe(final User user, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/add").contentType(ContentType.APPLICATION_JSON).data(UserBuilder.toJSON(user)).post(callback);
	}

	public void login(final User user, final Callback<String> callback) {
		final WebResource webResource = GameContext.instance().getWebResource();
		webResource.url(URL + "/get/" + user.getLogin()).header(NetUtils.getAuthHeaderKey(), NetUtils.getAuthHeaderValue(user)).contentType(ContentType.APPLICATION_JSON).data(UserBuilder.toJSON(user)).get(callback);
	}
}
