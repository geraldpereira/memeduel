package fr.byob.game.memeduel.core.view.handler;

import fr.byob.game.memeduel.core.god.StaticGameObjectDefinition;
import fr.byob.game.memeduel.core.view.object.ViewObject;


public class AbstractViewHandler<T extends ViewObject> implements ViewHandler {
	protected T viewObject;

	@SuppressWarnings("unchecked")
	@Override
	public void setViewObject(final ViewObject viewObject) {
		this.viewObject = (T) viewObject;
	}

	public StaticGameObjectDefinition getGOD() {
		return this.viewObject.getGOD();
	}
}
