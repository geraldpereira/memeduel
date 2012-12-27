package fr.byob.game.memeduel.core.view.object;

import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.ImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;

public abstract class AbstractViewObject implements ViewObject {

	protected final ImageHandler imageHandler;
	protected final LayerHandler layerHandler;

	public AbstractViewObject(final ImageHandler imageHandler, final LayerHandler layerHandler) {
		this.imageHandler = imageHandler;
		this.layerHandler = layerHandler;
	}

	@Override
	public void addToView(final View view) {
		this.imageHandler.setViewObject(this);
		this.layerHandler.setViewObject(this);
		this.layerHandler.initLayer();
		this.layerHandler.addToView(view);
	}


	@Override
	public void removeFromView(final View view) {
		this.layerHandler.removeFromView(view);
	}

	@Override
	public ImageHandler getImageHandler() {
		return this.imageHandler;
	}

	@Override
	public LayerHandler getLayerHandler() {
		return this.layerHandler;
	}

	@Override
	public void paint(final float alpha) {
		this.layerHandler.paint(alpha);
	}

}
