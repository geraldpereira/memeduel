package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Image;
import pythagoras.f.Dimension;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;

public class ScaledImageHandler extends ShapedImageHandler {

	@Override
	public Image createImage() {
		final Dimension modelSize = getShapeDefinition().getSize();
		final Dimension viewSize = ViewUtils.modelToInitView(modelSize, MathUtils.tmpDim());

		GameImage image = null;
		if (this.viewObject.getModelObject().isDamageable()) {
			image = this.viewObject.getModelObject().getDamageHandler().getDamageMDImage();
		}
		if (image == null) {
			image = this.viewObject.getGOD().getImageDefinition().getImage();
		}

		final CanvasImage canvasImage = graphics().createImage(MathUtils.ceil(viewSize.width), MathUtils.ceil(viewSize.height));

		canvasImage.canvas().save();
		canvasImage.canvas().drawImage(image.getImage(), 0, 0, viewSize.width, viewSize.height);
		canvasImage.canvas().restore();
		return canvasImage;
	}

}
