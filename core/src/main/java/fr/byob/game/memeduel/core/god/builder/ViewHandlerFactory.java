package fr.byob.game.memeduel.core.god.builder;

import fr.byob.game.memeduel.core.view.handler.ViewHandler;

public interface ViewHandlerFactory<T extends ViewHandler> {
	T newViewHandler();
}
