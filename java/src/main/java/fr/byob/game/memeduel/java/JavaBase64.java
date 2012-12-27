package fr.byob.game.memeduel.java;

import fr.byob.game.memeduel.core.net.Base64;


public class JavaBase64 implements Base64 {

	@Override
	public String encode(final String value) {
		return new String(com.sun.jersey.core.util.Base64.encode(value));
	}

	@Override
	public String decode(final String value) {
		return com.sun.jersey.core.util.Base64.base64Decode(value);
	}

}
