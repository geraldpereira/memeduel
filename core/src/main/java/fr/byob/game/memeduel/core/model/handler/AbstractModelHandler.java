package fr.byob.game.memeduel.core.model.handler;

import fr.byob.game.memeduel.core.model.object.DefaultModelObject;


public class AbstractModelHandler<T extends DefaultModelObject> implements ModelHandler {
	protected T modelObject;

	@SuppressWarnings("unchecked")
	@Override
	public void setModelObject(final DefaultModelObject modelObject) {
		this.modelObject = (T) modelObject;
	}
}
