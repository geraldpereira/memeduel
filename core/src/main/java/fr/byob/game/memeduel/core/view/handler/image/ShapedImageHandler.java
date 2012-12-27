package fr.byob.game.memeduel.core.view.handler.image;

import fr.byob.game.memeduel.core.controller.SelectionType;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public abstract class ShapedImageHandler extends AbstractViewHandler<DynamicViewObject> implements ImageHandler {

	protected boolean isTextured;

	protected TextureDefinition getTextureDefinition() {
		return (TextureDefinition) this.viewObject.getModelObject().getGOD().getImageDefinition();
	}

	protected ShapeDefinition getShapeDefinition() {
		return ((B2DGameObjectDefiniton) this.viewObject.getModelObject().getGOD()).getShapeDefition();
	}

	protected int getStrokeColor() {
		if (this.viewObject.getModelObject() instanceof B2DModelObject) {
			final B2DModelObject b2dModelObject = (B2DModelObject) this.viewObject.getModelObject();
			if (b2dModelObject.isSelectable() && b2dModelObject.getSelectionHandler().getSelectionType() != SelectionType.NONE) {
				return b2dModelObject.getSelectionHandler().getSelectionType().getColor();
			}
		}

		return this.getTextureDefinition().getStrokeColor().asInt();
	}

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.isTextured = this.viewObject.getModelObject().getGOD().getImageDefinition() instanceof TextureDefinition;
	}
}
