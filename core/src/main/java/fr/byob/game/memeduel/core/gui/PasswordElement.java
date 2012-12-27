package fr.byob.game.memeduel.core.gui;

import playn.core.Canvas;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.Gradient;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Path;
import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import tripleplay.ui.Element;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.Updatable;
import fr.byob.game.memeduel.core.ViewUtils;

public final class PasswordElement extends Element<PasswordElement> implements Updatable {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public final static class Builder {
		private pythagoras.i.Dimension passwordSize = new pythagoras.i.Dimension(
				4, 4);
		private int passwordLength = 8;
		private Dimension cellSize = new Dimension(50f, 50f);
		private int lineColor = 0xFFFF0000;
		private int lineStroke = 10;
		private double sensitivity = 10;

		private Builder() {
		}

		private void reset() {
			passwordLength = 8;
			cellSize.setSize(50f, 50f);
			lineColor = 0xFFFF0000;
			lineStroke = 10;
			sensitivity = 10;
		}

		public Builder passwordSize(final pythagoras.i.Dimension passwordSize) {
			this.passwordSize = passwordSize;
			return this;
		}

		public Builder passwordLength(final int passwordLength) {
			this.passwordLength = passwordLength;
			return this;
		}

		public Builder lineColor(final int lineColor) {
			this.lineColor = lineColor;
			return this;
		}

		public Builder lineStroke(final int lineStroke) {
			this.lineStroke = lineStroke;
			return this;
		}

		public Builder iconSize(final Dimension iconSize) {
			this.cellSize = iconSize;
			return this;
		}

		public Builder sensitivity(final double sensitivity) {
			this.sensitivity = sensitivity;
			return this;
		}

		public PasswordElement build() {
			return new PasswordElement(this.passwordSize, this.passwordLength, this.cellSize,
					this.lineColor, this.lineStroke, this.sensitivity);
		}
	}

	private enum State {
		NONE {
			@Override
			public State update(final float delta, final Dot[][] dots) {
				return NONE;
			}
		},
		FADING_IN {
			@Override
			public State update(final float delta, final Dot[][] dots) {
				State state = NONE;
				for (final Dot[] dot2 : dots) {
					for (final Dot element : dot2) {
						final Dot dot = element;
						if (!dot.isActive()) {
							final ImageLayer layer = dot.getImageLayer();
							float alpha = layer.alpha();
							if (alpha < 1) {
								alpha += 1 / delta;
								alpha = Math.min(1, alpha);
								layer.setAlpha(alpha);
								state = FADING_IN;
							}
						}
					}
				}
				return state;
			}
		},
		FADING_OUT {
			@Override
			public State update(final float delta, final Dot[][] dots) {
				State state = NONE;
				for (final Dot[] dot2 : dots) {
					for (final Dot element : dot2) {
						final Dot dot = element;
						if (!dot.isActive()){
							final ImageLayer layer = dot.getImageLayer();
							float alpha = layer.alpha();
							if (alpha > 0.1f) {
								alpha -= 1 / delta;
								alpha = Math.max(0.1f, alpha);
								layer.setAlpha(alpha);
								state = FADING_OUT;
							}
						}
					}
				}
				return state;
			}
		};

		public abstract State update(final float delta, final Dot[][] dots);
	}

	private final CanvasImage previousLinesCanvasImage;
	private final CanvasImage currentLineCanvasImage;
	private final Dimension globalSize;
	private final Dimension iconSize;
	private final int passwordLength;
	private final Dot[][] dots;
	private final Dot[] activeDots;
	private final Image[] activeIcons;
	private final Image icon;
	private int lastActiveDotIndex;
	private final double sensitivity;
	private State state;

	private PasswordElement(final pythagoras.i.Dimension passwordSize,
			final int passwordLength, final Dimension cellSize,
			final int lineColor, final int lineStroke, final double sensitivity) {

		this.passwordLength = passwordLength;
		this.globalSize = new Dimension(cellSize.width * passwordSize.width,
				cellSize.height * passwordSize.height);
		this.iconSize = cellSize;
		this.sensitivity = sensitivity;
		this.state = State.NONE;

		// Create the images
		this.activeIcons = new Image[passwordLength];
		for (int i = 0; i < passwordLength; i++) {
			this.activeIcons[i] = this.newDotIcon(i);
		}
		this.icon = this.newDotIcon(-1);

		// Initialize the background layers
		final ImageLayer previousLinesLayer = PlayN.graphics().createImageLayer();
		this.previousLinesCanvasImage = PlayN.graphics().createImage(
				this.globalSize.width, this.globalSize.height);
		previousLinesLayer.setImage(this.previousLinesCanvasImage);
		this.layer.add(previousLinesLayer);
		final Canvas previousLinesCanvas = this.previousLinesCanvasImage.canvas();
		previousLinesCanvas.setLineCap(LineCap.ROUND);
		previousLinesCanvas.setLineJoin(LineJoin.ROUND);
		previousLinesCanvas.setStrokeColor(lineColor);
		previousLinesCanvas.setStrokeWidth(lineStroke);

		final ImageLayer currentLineLayer = PlayN.graphics().createImageLayer();
		this.currentLineCanvasImage = PlayN.graphics().createImage(
				this.globalSize.width, this.globalSize.height);
		currentLineLayer.setImage(this.currentLineCanvasImage);
		this.layer.add(currentLineLayer);
		final Canvas currentLineCanvas = this.currentLineCanvasImage.canvas();
		currentLineCanvas.setLineCap(LineCap.ROUND);
		currentLineCanvas.setLineJoin(LineJoin.ROUND);
		currentLineCanvas.setStrokeColor(lineColor);
		currentLineCanvas.setStrokeWidth(lineStroke);

		// Create all the dots
		this.activeDots = new Dot[passwordLength];
		this.lastActiveDotIndex = -1;
		this.dots = new Dot[passwordSize.width][passwordSize.height];

		for (int row = 0; row < passwordSize.width; row++) {
			for (int col = 0; col < passwordSize.height; col++) {
				final Dot dot = new Dot(row, col);
				this.dots[row][col] = dot;
				this.layer.addAt(dot.getImageLayer(), row * cellSize.width, col
						* cellSize.height);
			}
		}

		// Create the input layer
		final ImageLayer inputLayer = PlayN.graphics().createImageLayer(
				PlayN.graphics().createImage(this.globalSize.width,
						this.globalSize.height));
		inputLayer.addListener(new InputListener());
		this.layer.add(inputLayer);
	}

	@Override
	public void update(final float delta) {
		this.state = this.state.update(delta, this.dots);
	}

	// Créer une dot factory à part
	private Image newDotIcon(final int index) {
		final float dotRadius = this.iconSize.width / 3.5f;
		final float centerX = this.iconSize.width / 2;
		final float centerY = this.iconSize.height / 2;

		final CanvasImage canvasImage = PlayN.graphics().createImage(
				this.iconSize.width, this.iconSize.height);
		final Canvas canvas = canvasImage.canvas();
		canvas.setStrokeColor(0xFF000000);
		canvas.setStrokeWidth(1.5f);
		int fillColor;
		if (index == -1) {
			fillColor = 0xFFEEEEEE;
		} else {
			fillColor = ViewUtils.getColorRedYelloGreen((float) index
					/ (float) this.passwordLength);
		}
		final Gradient gradient = PlayN.graphics().createRadialGradient(
				this.iconSize.width / 3, this.iconSize.height / 2.5f, dotRadius,
				new int[] { 0xFFFFFFFF, fillColor }, new float[] { 0f, 1f });
		canvas.setFillGradient(gradient);
		canvas.fillCircle(centerX, centerY, dotRadius);
		canvas.strokeCircle(centerX, centerY, dotRadius);
		return canvasImage;
	}

	@Override
	protected tripleplay.ui.Element<PasswordElement>.LayoutData createLayoutData(
			final float hintX, final float hintY) {
		return new LayoutData() {
			@Override
			public Dimension computeSize(final float hintX, final float hintY) {
				return globalSize;
			}
		};

	}

	public void reset() {
		this.lastActiveDotIndex = -1;
		for (final Dot dot : this.activeDots) {
			if (dot != null) {
				dot.reset();
			}
		}

		final Canvas previous = this.previousLinesCanvasImage.canvas();
		previous.clear();

		final Canvas current = this.currentLineCanvasImage.canvas();
		current.clear();
	}

	private void updatePreviousLinesCanvas(final Dot fromDot, final Dot toDot) {
		final Vector fromCenterPos = fromDot.getCenterPosition();
		final Vector toCenterPos = toDot.getCenterPosition();

		final Canvas canvas = this.previousLinesCanvasImage.canvas();
		canvas.setStrokeColor(ViewUtils.getColorRedYelloGreen((float) (this.lastActiveDotIndex - 1)
				/ (float) (this.passwordLength - 1)));
		final Path path = canvas.createPath();
		path.moveTo(fromCenterPos.x, fromCenterPos.y);
		path.lineTo(toCenterPos.x, toCenterPos.y);
		path.close();
		canvas.strokePath(path);

		// Clear the current line as it it going to change
		this.currentLineCanvasImage.canvas().clear();
	}

	private void updateCurrentLineCanvas(final Dot fromDot, final Vector position) {
		final Vector fromCenterPos = fromDot.getCenterPosition();
		final Canvas canvas = this.currentLineCanvasImage.canvas();
		canvas.clear();
		canvas.setStrokeColor(ViewUtils.getColorRedYelloGreen((float) this.lastActiveDotIndex
				/ (float) (this.passwordLength - 1)));
		final Path path = canvas.createPath();
		path.moveTo(fromCenterPos.x, fromCenterPos.y);
		path.lineTo(position.x, position.y);
		path.close();
		canvas.strokePath(path);
	}

	private class InputListener implements Listener {
		@Override
		public void onPointerStart(final Event event) {
			reset();
			state = State.FADING_IN;
			this.update(event);
		}

		@Override
		public void onPointerEnd(final Event event) {
			final Canvas canvas = currentLineCanvasImage.canvas();
			canvas.clear();
			if (lastActiveDotIndex != -1) {
				state = State.FADING_OUT;
			}
		}

		@Override
		public void onPointerDrag(final Event event) {
			this.update(event);
			if (lastActiveDotIndex == passwordLength - 1) {
				state = State.FADING_OUT;
			}
		}

		public void onPointerCancel(final Event event) {
		}

		private void update(final Event event) {
			if (lastActiveDotIndex + 1 >= passwordLength) {
				// Maximum number of points reached
				return;
			}
			final Vector eventPoint = GamePool.instance().popVector().set(event.localX(), event.localY());
			if (lastActiveDotIndex > -1) {
				updateCurrentLineCanvas(activeDots[lastActiveDotIndex],
						eventPoint);
			}

			// Find the current dot
			final int row = (int) (event.localX() / iconSize.width);
			final int col = (int) (event.localY() / iconSize.height);

			if (row < 0 || row >= dots.length) {
				GamePool.instance().pushVector(1);
				return;
			}
			if (col < 0 || col >= dots[0].length) {
				GamePool.instance().pushVector(1);
				return;
			}

			final Dot dot = dots[row][col];
			if (!dot.isActive()) {
				// Check if the pointer is near the center of the dot
				final Vector centerPoint = dot.getCenterPosition();
				if (MathUtils.distance(eventPoint, centerPoint) > sensitivity) {
					GamePool.instance().pushVector(1);
					return;
				}

				lastActiveDotIndex++;
				activeDots[lastActiveDotIndex] = dot;
				dot.setActive(lastActiveDotIndex);
				if (lastActiveDotIndex > 0) {
					updatePreviousLinesCanvas(activeDots[lastActiveDotIndex - 1], dot);
				}
			}

			GamePool.instance().pushVector(1);
		}
	}

	private class Dot {
		private boolean active;
		private final ImageLayer imageLayer;
		private final int row;
		private final int col;
		private final Vector centerPosition;

		public Dot(final int row, final int col) {
			this.active = false;
			this.imageLayer = PlayN.graphics().createImageLayer(icon);
			this.row = row;
			this.col = col;
			this.centerPosition = new Vector(iconSize.width * this.row + iconSize.width / 2, iconSize.height * this.col + iconSize.height / 2);
		}

		public void reset() {
			this.active = false;
			this.imageLayer.setImage(icon);
		}

		public void setActive(final int index) {
			this.active = true;
			this.imageLayer.setAlpha(1f);
			this.imageLayer.setImage(activeIcons[index]);
		}

		public boolean isActive() {
			return this.active;
		}

		public ImageLayer getImageLayer() {
			return this.imageLayer;
		}

		public Vector getCenterPosition() {
			return centerPosition;
		}
	}

	public String getPassword(){
		if (this.lastActiveDotIndex == -1){
			return null;
		}
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i <= this.lastActiveDotIndex; i++){
			final Dot dot = this.activeDots[i];
			builder.append(dot.col);
			builder.append(dot.row);
		}

		return builder.toString();
	}

	public void setPassword(final String password) {

		final char[] pwd = password.toCharArray();

		for (int i = 0; i < pwd.length - 1; i += 2) {
			final int col = Integer.parseInt("" + pwd[i]);
			final int row = Integer.parseInt("" + pwd[i + 1]);

			final Dot dot = dots[row][col];
			if (!dot.isActive()) {
				lastActiveDotIndex++;
				activeDots[lastActiveDotIndex] = dot;
				dot.setActive(lastActiveDotIndex);
				if (lastActiveDotIndex > 0) {
					updatePreviousLinesCanvas(activeDots[lastActiveDotIndex - 1], dot);
				}
			}

		}
	}

}
