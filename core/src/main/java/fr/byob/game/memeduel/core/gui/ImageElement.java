package fr.byob.game.memeduel.core.gui;

import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import tripleplay.ui.Element;
import fr.byob.game.memeduel.core.GameImage;

public class ImageElement extends Element<ImageElement> {

	private final GameImage image;

	public ImageElement(final GameImage image) {
		this.image = image;
		final ImageLayer imageLayer = PlayN.graphics().createImageLayer(image.getImage());
		imageLayer.setImage(image.getImage());
		this.layer.add(imageLayer);
		super.setSize(image.getImage().width(), image.getImage().height());
	}


	@Override
	protected tripleplay.ui.Element<ImageElement>.LayoutData createLayoutData(final float hintX, final float hintY) {
		return new LayoutData() {

			@Override
			public Dimension computeSize(final float hintX, final float hintY) {
				return new Dimension(image.getImage().width(), image.getImage().height());
			}
		};
	}
}
