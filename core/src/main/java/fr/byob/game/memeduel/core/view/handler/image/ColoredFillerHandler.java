package fr.byob.game.memeduel.core.view.handler.image;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Image;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.ColoredFillerGOD;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.StaticViewObject;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public class ColoredFillerHandler extends AbstractViewHandler<StaticViewObject> implements ImageHandler {

	private LayerDefinition layerDefinition;

	@Override
	public void setViewObject(final ViewObject viewObject) {
		super.setViewObject(viewObject);
		this.layerDefinition = this.viewObject.getGOD().getLayerDefinition();
	}

	@Override
	public Image createImage() {
		final int width = MathUtils.ceil(this.layerDefinition.getLayerSize().width);
		final int height = MathUtils.ceil(this.layerDefinition.getLayerSize().height);
		final CanvasImage image = graphics().createImage(width, height);
		image.canvas().save();
		image.canvas().setFillColor(((ColoredFillerGOD) this.viewObject.getGOD()).getFillColor());
		image.canvas().fillRect(0, 0, width, height);
		image.canvas().restore();
		return image;
	}

}
