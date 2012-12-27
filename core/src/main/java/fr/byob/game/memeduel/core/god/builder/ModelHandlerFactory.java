package fr.byob.game.memeduel.core.god.builder;

import fr.byob.game.memeduel.core.model.handler.ModelHandler;

public interface ModelHandlerFactory<T extends ModelHandler> {
	T newModelHandler();
}
