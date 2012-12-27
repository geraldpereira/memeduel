package fr.byob.game.memeduel.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import playn.core.Platform;
import playn.core.util.Callback;
import fr.byob.game.memeduel.core.net.WebResourceException;
import fr.byob.game.memeduel.core.net.WebResourceImpl;

public class AndroidWebResourceBCK extends WebResourceImpl {

	public AndroidWebResourceBCK(final Platform platform) {
		super(platform);
	}

	@Override
	public void get(final Callback<String> callback) {
		new Thread("AndroidWebResource.call (" + url + ")") {
			@Override
			public void run() {
				try {
					final HttpClient httpclient = new DefaultHttpClient();
					final HttpRequestBase req = new HttpGet(new URI(url));
					for (final Entry<String, String> header : AndroidWebResourceBCK.this.headers.entrySet()) {
						req.setHeader(header.getKey(), header.getValue());
					}

					final HttpResponse response = httpclient.execute(req);
					if (response.getStatusLine().getStatusCode() != 200) {
						notifyFailure(callback, new WebResourceException(parseErrorResponse(EntityUtils.toString(response.getEntity()))));
					} else {
						notifySuccess(callback, EntityUtils.toString(response.getEntity()));
					}
				} catch (final Exception e) {
					notifyFailure(callback, e);
				}
			}
		}.start();

	}

	@Override
	public void delete(final Callback<String> callback) {
		new Thread("AndroidWebResource.call (" + url + ")") {
			@Override
			public void run() {
				final HttpClient httpclient = new DefaultHttpClient();
				final HttpRequestBase req = new HttpDelete(url);
				for (final Entry<String, String> header : AndroidWebResourceBCK.this.headers.entrySet()) {
					req.setHeader(header.getKey(), header.getValue());
				}
				try {
					final HttpResponse response = httpclient.execute(req);
					if (response.getStatusLine().getStatusCode() != 204) {
						notifyFailure(callback, new WebResourceException(parseErrorResponse(EntityUtils.toString(response.getEntity()))));
					} else {
						notifySuccess(callback, EntityUtils.toString(response.getEntity()));
					}
				} catch (final Exception e) {
					notifyFailure(callback, e);
				}
			}
		}.start();

	}

	@Override
	public void post(final Callback<String> callback) {
		new Thread("AndroidWebResource.call (" + url + ")") {
			@Override
			public void run() {
				final HttpClient httpclient = new DefaultHttpClient();
				final HttpPost req = new HttpPost(url);
				for (final Entry<String, String> header : AndroidWebResourceBCK.this.headers.entrySet()) {
					req.setHeader(header.getKey(), header.getValue());
				}
				if (data != null) {
					try {
						req.setEntity(new StringEntity(data));
					} catch (final UnsupportedEncodingException e) {
						notifyFailure(callback, e);
					}
				}
				try {
					final HttpResponse response = httpclient.execute(req);
					if (response.getStatusLine().getStatusCode() != 200) {
						notifyFailure(callback, new WebResourceException(parseErrorResponse(EntityUtils.toString(response.getEntity()))));
					} else {
						notifySuccess(callback, EntityUtils.toString(response.getEntity()));
					}
				} catch (final Exception e) {
					notifyFailure(callback, e);
				}
			}
		}.start();

	}

	private void doHttp(final boolean isPost, final String url, final String data, final Callback<String> callback) {
		new Thread("AndroidNet.doHttp") {
			@Override
			public void run() {
				final HttpClient httpclient = new DefaultHttpClient();
				HttpRequestBase req = null;
				if (isPost) {
					final HttpPost httppost = new HttpPost(url);
					if (data != null) {
						try {
							httppost.setEntity(new StringEntity(data));
						} catch (final UnsupportedEncodingException e) {
							notifyFailure(callback, e);
						}
					}
					req = httppost;
				} else {
					req = new HttpGet(url);
				}
				try {
					final HttpResponse response = httpclient.execute(req);
					notifySuccess(callback, EntityUtils.toString(response.getEntity()));
				} catch (final Exception e) {
					notifyFailure(callback, e);
				}
			}
		}.start();
	}

	public static void main(final String[] args) throws ClientProtocolException, IOException {
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpRequestBase req = new HttpGet("http://memeduel-server.appspot.com/api/v1.0/user/get/Kojiro");
		req.setHeader("Content-type", "application/json");
		req.setHeader("authorization", "Basic S29qaXJvOjAwMTAyMDMwMzEyMTExMDE=");
		final HttpResponse response = httpclient.execute(req);
		System.out.println(EntityUtils.toString(response.getEntity()));
	}

}
