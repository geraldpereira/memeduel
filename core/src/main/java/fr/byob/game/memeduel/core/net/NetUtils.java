package fr.byob.game.memeduel.core.net;

import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.domain.User;

public class NetUtils {

	// public final static String BASE_URL = "http://localhost:8080/api/v1.0/";

	public final static String BASE_URL = "http://memeduel-server.appspot.com/api/v1.0/";

	public static String getAuthHeaderKey() {
		return "authorization";
	}

	public static String getAuthHeaderValue(final User user) {
		return "Basic " + GameContext.instance().getBase64().encode(user.getLogin() + ":" + user.getPassword());
	}
}
