package fr.byob.game.memeduel.android;

import fr.byob.game.memeduel.core.net.Base64;

public class AndroidBase64 implements Base64 {

	@Override
	public String encode(final String value) {
		return android.util.Base64.encodeToString(value.getBytes(),
				android.util.Base64.DEFAULT);
	}

	@Override
	public String decode(final String value) {
		return new String(android.util.Base64.decode(value,
				android.util.Base64.DEFAULT));
	}

}
