package fr.byob.game.memeduel.core.net;

import playn.core.util.Callback;

/**
 * Performs HTTP requests It can be is used to call a REST services.
 * 
 * @author Gérald Pereira
 * 
 */
public interface WebResource {

	/**
	 * Sets the content type. You can also call
	 * header("Content-Type","application/json") It is used only if you do a
	 * post() to specify the type of the data sent.
	 * 
	 * @param
	 * @return this WebResource
	 */
	public WebResource contentType(ContentType type);

	/**
	 * Sets the http url to call
	 * 
	 * @param url
	 * @return this WebResource
	 */
	public WebResource url(String url);

	/**
	 * Adds a header
	 * 
	 * @param key
	 *            the header name
	 * @param value
	 *            the header value
	 * @return this WebResource
	 */
	public WebResource header(String key, String value);

	/**
	 * Sets the POST request data
	 * 
	 * @param data
	 * @return
	 */
	public WebResource data(String data);

	/**
	 * Performs an HTTP GET request. Warning : you must call the method url()
	 * before doing a get().
	 * 
	 * @param callback
	 */
	public void get(Callback<String> callback);

	/**
	 * Performs an HTTP POST request.
	 * 
	 * Warning : you must call the methods url(), contentType() and data()
	 * before doing a post().
	 * 
	 * @param callback
	 */
	public void post(Callback<String> callback);

	/**
	 * Performs an HTTP DELETE request. Warning : you must call the method url()
	 * before doing a get().
	 * 
	 * @param callback
	 */
	public void delete(Callback<String> callback);


}
