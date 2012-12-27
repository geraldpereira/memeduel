package fr.byob.game.memeduel.core.view.object;

import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;

public class DynamicViewObject extends AbstractViewObject {

	private final ModelObject modelObject;

	public DynamicViewObject(final ModelObject modelObject, final ImageHandler imageHandler, final LayerHandler layerHandler) {
		super(imageHandler, layerHandler);
		this.modelObject = modelObject;
	}


	public ModelObject getModelObject() {
		return this.modelObject;
	}

	@Override
	public void paint(final float alpha) {
		if (!this.modelObject.isValid()) {
			this.layerHandler.repaint();
			this.modelObject.validate();
		}
		super.paint(alpha);
	}

	@Override
	public DynamicGameObjectDefinition getGOD() {
		return this.modelObject.getGOD();
	}
}
