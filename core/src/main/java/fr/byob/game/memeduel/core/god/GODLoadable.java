package fr.byob.game.memeduel.core.god;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.util.Callback;

public abstract class GODLoadable<T extends GameObjectDefinition> {

	public void loadFromFile(final String fileName, final Callback<T> callback) {
		PlayN.assets().getText(fileName, new ResourceCallback<String>() {
			@Override
			public void done(final String resource) {
				final T t = loadFromContent(resource);
				callback.onSuccess(t);
			}

			@Override
			public void error(final Throwable err) {
				callback.onFailure(err);
			}
		});
	}

	public T loadFromContent(final String json) {
		final Json.Object object = PlayN.json().parse(json);
		return loadFromJSON(object);
	}

	protected abstract T loadFromJSON(final Json.Object object);
}
