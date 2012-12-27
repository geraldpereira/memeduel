package fr.byob.game.memeduel.core.god.builder;

import playn.core.Json.Object;
import fr.byob.game.memeduel.core.god.AllGODLoader;

public interface GODBuilder<T> {

	public void reset();

	public T build();

	public GODBuilder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity);

	public GODBuilder<T> fromOther(final T other);
}
