package fr.byob.game.memeduel.core.gui;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import tripleplay.ui.Element;
import fr.byob.game.memeduel.core.MathUtils;

public class CanvasElement extends Element<CanvasElement> {

	private final ImageLayer imageLayer;
	private final CanvasImage canvasImage;

	public CanvasElement(final Dimension size) {
		this.imageLayer = PlayN.graphics().createImageLayer();
		this.canvasImage = PlayN.graphics().createImage(MathUtils.ceil(size.width) - 1, MathUtils.ceil(size.height) - 1);
		this.imageLayer.setImage(this.canvasImage);
		this.layer.add(this.imageLayer);
		super.setSize(size.width, size.height);
	}

	public Canvas getCanvas() {
		return this.canvasImage.canvas();
	}

	public ImageLayer getImageLayer() {
		return this.imageLayer;
	}

	public void setSize(final Dimension size) {
		super.setSize(size.width, size.height);
	}

	@Override
	protected tripleplay.ui.Element<CanvasElement>.LayoutData createLayoutData(final float hintX, final float hintY) {
		return new LayoutData() {

			@Override
			public Dimension computeSize(final float hintX, final float hintY) {
				// float width = CanvasElement.this._size.width;
				// float height = CanvasElement.this._size.height;
				// if (hintX != 0) {
				// width = hintX;
				// }
				//
				// if (hintY != 0) {
				// height = hintY;
				// }

				return _size;// new Dimension(width, height);
			}
		};
	}
}
