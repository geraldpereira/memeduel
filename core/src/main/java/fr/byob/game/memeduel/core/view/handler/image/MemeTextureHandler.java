package fr.byob.game.memeduel.core.view.handler.image;

import playn.core.CanvasImage;
import playn.core.Image;
import pythagoras.f.Dimension;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.controller.SelectionType;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public class MemeTextureHandler extends ScaledImageHandler {

	@Override
	public Image createImage() {
		final CanvasImage canvasImage = (CanvasImage) super.createImage();
		final Dimension modelSize = getShapeDefinition().getSize();
		final float viewRadius = ViewUtils.modelToInitView(modelSize, MathUtils.tmpDim()).width / 2;
		final float strokeWidth = ViewUtils.toInitView(this.getTextureDefinition().getStrokeWidth());

		canvasImage.canvas().save();
		if (this.viewObject.getModelObject() instanceof B2DModelObject) {
			final B2DModelObject b2dModelObject = (B2DModelObject) this.viewObject.getModelObject();
			if (b2dModelObject.isSelectable() && b2dModelObject.getSelectionHandler().getSelectionType() != SelectionType.NONE) {
				canvasImage.canvas().setStrokeColor(b2dModelObject.getSelectionHandler().getSelectionType().getColor());
				canvasImage.canvas().setStrokeWidth(strokeWidth);
				canvasImage.canvas().strokeCircle(viewRadius, viewRadius, viewRadius - strokeWidth / 2);
			}
		}

		canvasImage.canvas().restore();
		return canvasImage;
	}

	// @Override
	// public Image createImage() {
	//
	// final float modelRadius = this.circleDefinition.getRadius();
	// final int viewDiameter = MathUtils.ceil(ViewUtils.toInitView(2 *
	// modelRadius));
	// final float viewRadius = ViewUtils.toInitView(modelRadius);
	//
	// GameImage image = null;
	// if (this.viewObject.getModelObject().isDamageable()) {
	// image =
	// this.viewObject.getModelObject().getDamageHandler().getDamageMDImage();
	// }
	// if (image == null) {
	// image = this.viewObject.getGOD().getImageDefinition().getImage();
	// }
	//
	// final CanvasImage canvasImage = graphics().createImage(viewDiameter,
	// viewDiameter);
	//
	// final float strokeWidth =
	// ViewUtils.toInitView(this.getTextureDefinition().getStrokeWidth());
	//
	// canvasImage.canvas().save();
	// canvasImage.canvas().drawImage(image.getImage(), 0, 0, viewDiameter,
	// viewDiameter);
	// if (this.b2dModelObject.isSelectable() &&
	// this.b2dModelObject.getSelectionHandler().getSelectionType() !=
	// SelectionType.NONE) {
	// canvasImage.canvas().setStrokeColor(this.b2dModelObject.getSelectionHandler().getSelectionType().getColor());
	// canvasImage.canvas().setStrokeWidth(strokeWidth);
	// canvasImage.canvas().strokeCircle(viewRadius, viewRadius, viewRadius -
	// strokeWidth / 2);
	// }
	//
	// canvasImage.canvas().restore();
	// return canvasImage;
	// }

}
