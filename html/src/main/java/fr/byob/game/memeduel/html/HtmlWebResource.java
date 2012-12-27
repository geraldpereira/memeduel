package fr.byob.game.memeduel.html;

import java.util.Map.Entry;

import playn.core.Platform;
import playn.core.PlayN;
import playn.core.util.Callback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import fr.byob.game.memeduel.core.net.WebResourceException;
import fr.byob.game.memeduel.core.net.WebResourceImpl;

/**
 * 
 * Note : To call an HTTP url from localhost (HTML application deployed for
 * test), you must add the following headers in the response :
 * Access-Control-Allow-Origin : * Access-Control-Allow-Methods : PUT, GET,
 * POST, DELETE, OPTIONS Access-Control-Allow-Headers : Content-Type
 * 
 * @author Gérald Pereira
 * 
 */
public class HtmlWebResource extends WebResourceImpl {

	public HtmlWebResource(final Platform platform) {
		super(platform);
	}

	@Override
	public void get(final Callback<String> callback) {
		this.call(RequestBuilder.GET, callback);
	}

	@Override
	public void post(final Callback<String> callback) {
		this.call(RequestBuilder.POST, callback);
	}

	@Override
	public void delete(final Callback<String> callback) {
		this.call(RequestBuilder.DELETE, callback);
	}

	public void call(final Method methodType, final Callback<String> callback) {
		try {
			PlayN.log().info(methodType.toString());

			final RequestBuilder builder = new RequestBuilder(methodType, url);
			for (final Entry<String, String> header : headers.entrySet()) {
				builder.setHeader(header.getKey(), header.getValue());
			}

			builder.setCallback(new RequestCallback() {

				@Override
				public void onResponseReceived(final Request request, final Response response) {
					if (200 == response.getStatusCode() || (204 == response.getStatusCode() && methodType == RequestBuilder.DELETE)) {
						notifySuccess(callback, response.getText());
					} else {
						notifyFailure(callback, new WebResourceException(parseErrorResponse(response.getText())));
					}
				}

				@Override
				public void onError(final Request request, final Throwable exception) {
					notifyFailure(callback, exception);
				}
			});

			if (data != null) {
				builder.setRequestData(data);
			}

			builder.send();
		} catch (final Exception e) {
			callback.onFailure(e);
		}
	}

}
