package fr.byob.game.memeduel.core.net;

public class WebResourceException extends Exception {
	public WebResourceException(final String message) {
		super(message);
	}

	public WebResourceException(final Throwable t) {
		super(t.getMessage());
	}
}
