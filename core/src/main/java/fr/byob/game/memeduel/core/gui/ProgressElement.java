package fr.byob.game.memeduel.core.gui;

import playn.core.Canvas;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Path;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import tripleplay.ui.Element;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;

public class ProgressElement extends Element<ProgressElement> {

	public enum Mode {
		PROGRESS {
			@Override
			public int getFillColor(final float percentage) {
				return ViewUtils.getColorRedYelloGreen(percentage);
			}
		},
		GAUGE {
			@Override
			public int getFillColor(final float percentage) {
				return ViewUtils.getColorGreenYelloRed(percentage);
			}
		};
		public abstract int getFillColor(float percentage);
	}

	private final static float STROKE_WIDTH = 1;

	private final ImageLayer imageLayer;
	private final CanvasImage canvasImage;
	private final Rectangle gaugeBounds;
	private final Dimension size;
	private Mode mode;

	public ProgressElement(final Mode mode, final float width, final float height) {
		this.mode = mode;
		this.imageLayer = PlayN.graphics().createImageLayer();
		this.canvasImage = PlayN.graphics().createImage(MathUtils.ceil(width), MathUtils.ceil(height));
		this.imageLayer.setImage(this.canvasImage);
		this.layer.add(this.imageLayer);
		this.size = new Dimension(width, height);
		// Used to avoid cropping the strokes
		this.gaugeBounds = new Rectangle(STROKE_WIDTH, STROKE_WIDTH, size.width - 2 * STROKE_WIDTH, size.height - 2 * STROKE_WIDTH);
		this.setPercentage(0);
	}

	/**
	 * @param percentage
	 *            from 0.0 to 1.0
	 */
	public void setPercentage(final float percentage) {

		final float width = percentage * this.gaugeBounds.width;
		final Canvas canvas = this.canvasImage.canvas();

		canvas.save();
		canvas.setFillColor(0xFFFFFFFF);
		canvas.fillRect(this.gaugeBounds.x, this.gaugeBounds.y, this.gaugeBounds.width, this.gaugeBounds.height);
		canvas.setFillColor(mode.getFillColor(percentage));
		canvas.fillRect(this.gaugeBounds.x, this.gaugeBounds.y, width, this.gaugeBounds.height);
		canvas.setStrokeColor(0xFF000000);
		canvas.setStrokeWidth(STROKE_WIDTH);
		canvas.setLineJoin(LineJoin.ROUND);
		canvas.setLineCap(LineCap.ROUND);
		canvas.strokeRect(this.gaugeBounds.x, this.gaugeBounds.y, this.gaugeBounds.width, this.gaugeBounds.height);

		final Path middleLine = canvas.createPath();
		middleLine.moveTo(this.gaugeBounds.x + this.gaugeBounds.width / 2, this.gaugeBounds.y + this.gaugeBounds.height / 2);
		middleLine.lineTo(this.gaugeBounds.x + this.gaugeBounds.width / 2, this.gaugeBounds.y + this.gaugeBounds.height);
		canvas.strokePath(middleLine);

		final Path leftLine = canvas.createPath();
		leftLine.moveTo(this.gaugeBounds.x + this.gaugeBounds.width / 4, this.gaugeBounds.y + 2 * this.gaugeBounds.height / 3);
		leftLine.lineTo(this.gaugeBounds.x + this.gaugeBounds.width / 4, this.gaugeBounds.y + this.gaugeBounds.height);
		canvas.strokePath(leftLine);

		final Path rightLine = canvas.createPath();
		rightLine.moveTo(this.gaugeBounds.x + 3 * this.gaugeBounds.width / 4, this.gaugeBounds.y + 2 * this.gaugeBounds.height / 3);
		rightLine.lineTo(this.gaugeBounds.x + 3 * this.gaugeBounds.width / 4, this.gaugeBounds.y + this.gaugeBounds.height);
		canvas.strokePath(rightLine);

		canvas.restore();
	}

	@Override
	protected tripleplay.ui.Element<ProgressElement>.LayoutData createLayoutData(final float hintX, final float hintY) {
		return new LayoutData() {
			@Override
			public Dimension computeSize(final float hintX, final float hintY) {
				return size;
			}
		};
	}
}
