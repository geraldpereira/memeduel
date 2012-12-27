package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Gradient;
import playn.core.Image;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class SunGlowImageHandler extends AbstractViewHandler<DynamicViewObject> implements ImageHandler {

	LayerDefinition layerDefinition;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.layerDefinition = this.viewObject.getModelObject().getGOD().getLayerDefinition();
	}

	public void setLayerDefinition(final LayerDefinition layerDefinition) {
		this.layerDefinition = layerDefinition;
	}

	@Override
	public Image createImage() {
		final int width = MathUtils.ceil(this.layerDefinition.getLayerSize().width);
		final int height = MathUtils.ceil(this.layerDefinition.getLayerSize().height);
		final int radius = Math.min(width, height) / 2;

		final CanvasImage image = graphics().createImage(width, height);
		final Gradient gradient = graphics().createRadialGradient(width / 2, height / 2, radius, new int[] { 0xFFFF7800, 0x00FFD100 }, new float[] { 0f, 1f });

		image.canvas().save();
		image.canvas().setFillGradient(gradient);
		image.canvas().fillCircle(width / 2, height / 2, radius);
		image.canvas().restore();
		return image;
	}

}
