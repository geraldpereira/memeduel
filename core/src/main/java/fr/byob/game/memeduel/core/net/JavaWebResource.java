package fr.byob.game.memeduel.core.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;

import playn.core.Platform;
import playn.core.util.Callback;

public class JavaWebResource extends WebResourceImpl {

	private static final int BUF_SIZE = 4096;

	public JavaWebResource(final Platform platform) {
		super(platform);
	}

	private enum Method {
		GET(true, false, false, 200), POST(true, true, true, 200), DELETE(true, false, false, 204);

		private final int successResponseCode;
		private final boolean doInput;
		private final boolean doOutput;
		private final boolean allowUserInteraction;

		private Method(final boolean doInput, final boolean doOutput, final boolean allowUserInteraction, final int successResponseCode) {
			this.doInput = doInput;
			this.doOutput = doOutput;
			this.allowUserInteraction = allowUserInteraction;
			this.successResponseCode = successResponseCode;
		}
	}

	private void call(final Method method, final Callback<String> callback) {
		new Thread("JavaWebResource.call(" + this.url + ")") {
			@Override
			public void run() {
				try {
					// open url connection
					final URL url = new URL(JavaWebResource.this.url);
					final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					// set up url connection to get retrieve information back
					connection.setRequestMethod(method.name());
					connection.setDoInput(method.doInput);
					connection.setDoOutput(method.doOutput);
					connection.setAllowUserInteraction(method.allowUserInteraction);

					for (final Entry<String, String> header : JavaWebResource.this.headers.entrySet()) {
						connection.setRequestProperty(header.getKey(), header.getValue());
					}
					if (method.doOutput) {
						connection.connect();
						if (JavaWebResource.this.data != null) {
							connection.getOutputStream().write(JavaWebResource.this.data.getBytes("UTF-8"));
							connection.getOutputStream().close();
						}
					}

					final String result;
					if (connection.getResponseCode() != method.successResponseCode) {
						result = JavaWebResource.this.readFully(new InputStreamReader(connection.getErrorStream()));
						notifyFailure(callback, new WebResourceException(parseErrorResponse(result)));
					} else {
						result = JavaWebResource.this.readFully(new InputStreamReader(connection.getInputStream()));
						notifySuccess(callback, result);
					}

					connection.disconnect();
				} catch (final MalformedURLException e) {
					notifyFailure(callback, new WebResourceException(e));
				} catch (final IOException e) {
					notifyFailure(callback, new WebResourceException(e));
				}
			}
		}.start();

	}

	@Override
	public void get(final Callback<String> callback) {
		call(Method.GET, callback);

	}

	@Override
	public void post(final Callback<String> callback) {
		call(Method.POST, callback);
	}

	@Override
	public void delete(final Callback<String> callback) {
		call(Method.DELETE, callback);
	}

	private String readFully(final Reader reader) throws IOException {
		final StringBuffer result = new StringBuffer();
		final char[] buf = new char[BUF_SIZE];
		int len = 0;
		while (-1 != (len = reader.read(buf))) {
			result.append(buf, 0, len);
		}
		return result.toString();
	}



}
