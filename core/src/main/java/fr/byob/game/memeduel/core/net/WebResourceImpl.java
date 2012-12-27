package fr.byob.game.memeduel.core.net;

import java.util.HashMap;
import java.util.Map;

import playn.core.Platform;
import playn.core.PlayN;
import playn.core.util.Callback;

/**
 * Default WebResource implementation
 * 
 * @author Gérald Pereira
 * 
 */
public abstract class WebResourceImpl implements WebResource {

	private final static String CONTENT_TYPE = "Content-type";

	protected final Platform platform;
	protected String url;
	protected final Map<String, String> headers;
	protected String data;

	public WebResourceImpl(final Platform platform) {
		this.platform = platform;
		this.headers = new HashMap<String, String>();
		this.contentType(ContentType.APPLICATION_JSON);
	}

	@Override
	public WebResource contentType(final ContentType type) {
		this.headers.put(CONTENT_TYPE, type.getType());
		return this;
	}

	@Override
	public WebResource url(final String url) {
		this.url = url;
		return this;
	}

	@Override
	public WebResource data(final String data) {
		this.data = data;
		return this;
	}

	@Override
	public WebResource header(final String key, final String value) {
		this.headers.put(key, value);
		return this;
	}

	protected void notifySuccess(final Callback<String> callback,
			final String result) {
		this.platform.invokeLater(new Runnable() {
			@Override
			public void run() {
				callback.onSuccess(result);
			}
		});
	}

	protected void notifyFailure(final Callback<String> callback,
			final Throwable cause) {
		this.platform.invokeLater(new Runnable() {
			@Override
			public void run() {
				PlayN.log().error(cause.getMessage());
				callback.onFailure(cause);
			}
		});
	}

	protected static String parseErrorResponse(final String rawErrorResponse) {
		if (rawErrorResponse.trim().startsWith("<html>")) {
			final int start = rawErrorResponse.indexOf("<pre>");
			final int end = rawErrorResponse.indexOf("</pre>");
			if (start != -1 && end != -1) {
				return rawErrorResponse.substring(start + 5, end).trim();
			} else {
				final int start2 = rawErrorResponse.indexOf("<h1>Error:");
				final int end2 = rawErrorResponse.indexOf("</h1>");
				if (start2 != -1 && end2 != -1) {
					return rawErrorResponse.substring(start2 + 10, end2).trim();
				}
			}
		}
		return rawErrorResponse;
	}

}
