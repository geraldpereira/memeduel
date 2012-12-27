package fr.byob.game.memeduel.core.view.handler.layer;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.AbstractViewHandler;
import fr.byob.game.memeduel.core.view.object.ViewObject;

public abstract class AbstractLayerHandler<T extends ViewObject> extends AbstractViewHandler<T> implements LayerHandler {
	protected ImageLayer layer;


	@Override
	public void initLayer() {
		final LayerDefinition layerDefinition = this.getGOD().getLayerDefinition();
		final Image image = this.viewObject.getImageHandler().createImage();

		this.layer = PlayN.graphics().createImageLayer(image);
		this.layer.setWidth(layerDefinition.getLayerSize().width);
		this.layer.setHeight(layerDefinition.getLayerSize().height);
		this.layer.setOrigin(layerDefinition.getLayerOrigin().x, layerDefinition.getLayerOrigin().y);
		this.layer.setScale(layerDefinition.getLayerScale().x, layerDefinition.getLayerScale().y);
		this.layer.setTranslation(this.getGOD().getPosition().x, this.getGOD().getPosition().y);
		this.layer.setRotation(this.getGOD().getAngle());
		this.layer.setDepth(layerDefinition.getDepth());
		this.layer.setRepeatX(layerDefinition.isRepeatX());
	}

	@Override
	public void repaint() {
		final Image image = this.viewObject.getImageHandler().createImage();
		this.layer.setImage(image);
	}

	@Override
	public void addToView(final View view) {
		final LayerDefinition layerDefinition = this.getGOD().getLayerDefinition();
		layerDefinition.getZOrder().addToView(this.layer, view);
	}

	@Override
	public void removeFromView(final View view) {
		final LayerDefinition layerDefinition = this.getGOD().getLayerDefinition();
		layerDefinition.getZOrder().removeFromView(this.layer, view);
	}

	@Override
	public ImageLayer getLayer() {
		return this.layer;
	}

}
