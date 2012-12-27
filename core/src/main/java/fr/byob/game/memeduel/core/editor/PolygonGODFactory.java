package fr.byob.game.memeduel.core.editor;

import playn.core.Canvas;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.Path;
import playn.core.Pattern;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.box2d.common.Settings;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.b2d.PolygonDefinition;
import fr.byob.game.memeduel.core.god.b2d.PolygonDefinition.Builder;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;
public class PolygonGODFactory extends ObjectGODFactory{

	protected final Vector[] currentPolygon;
	protected int currentSize;
	protected final Dimension size;
	protected final Vector centerPosition;
	protected final Vector minOffset;

	public PolygonGODFactory(final Canvas canvas, final MemeDuelGODLoader loader, final String type, final Rectangle canvasViewport) {
		super(canvas, loader, type, canvasViewport);

		currentPolygon = new Vector[Settings.maxPolygonVertices];
		MathUtils.fillPolygon(currentPolygon);
		size = new Dimension();
		centerPosition = new Vector();
		minOffset = new Vector();

		this.initPolygon();
	}

	protected void initPolygon() {
		final Dimension size = GamePool.instance().popDimension();
		size.setSize(loader.getGameGOD().getMinPolygonWidth() * 2, loader.getGameGOD().getMinPolygonHeight() * 3);

		final Vector[] polygon = GamePool.instance().popVectors(Settings.maxPolygonVertices);
		polygon[0].set(0, -size.height / 2);
		polygon[1].set(size.width / 2, 0);
		polygon[2].set(size.width / 3, size.height / 2);
		polygon[3].set(-size.width / 3, size.height / 2);
		polygon[4].set(-size.width / 2, 0);

		setCurrentPolygon(polygon, 5);

		GamePool.instance().pushDimension(1);
		GamePool.instance().pushVector(Settings.maxPolygonVertices);
	}

	@Override
	public void handleClick(final Vector eventPostition) {
		if (!this.canvasViewport.contains(eventPostition.x, eventPostition.y)) {
			return;
		}

		final Vector[] polygon = GamePool.instance().popVectors(Settings.maxPolygonVertices);
		MathUtils.copyPolygon(currentPolygon, polygon);
		int size = currentSize;
		final Vector newPoint = GamePool.instance().popVector();
		newPoint.set(this.toModel(eventPostition.x - this.canvasViewport.centerX()), this.toModel(eventPostition.y - this.canvasViewport.centerY()));

		// Check if we must remove a point
		boolean removePoint = false;
		int i = 0;
		for (; i < size; i++) {
			if (polygon[i].distance(newPoint) < 0.2) {
				if (size < 4) {
					PlayN.log().error("PolygonGODFactory.setCurrentPolygon() TOO FEW POINTS");
					return;
				}
				removePoint = true;
				break;
			}
		}

		if (removePoint) {
			size = MathUtils.removeVertexFromPolygon(polygon, size, i);
		} else {
			if (size + 1 >= Settings.maxPolygonVertices) {
				// We reached the maximun number of points
				PlayN.log().error("PolygonGODFactory.handleClick() TOO MANY POINTS");
				return;
			}
			polygon[size].set(newPoint);
			size++;

			MathUtils.arrangeClockwise(polygon, size);
			size = MathUtils.toConvexPolygon(polygon, size);
		}

		final boolean updated = this.setCurrentPolygon(polygon, size);
		if (updated) {
			this.draw();
		}

		GamePool.instance().pushVector(Settings.maxPolygonVertices + 1);
	}

	/**
	 * Poygon must be arranged clockwise and convex
	 */
	protected boolean setCurrentPolygon(final Vector[] polygon, final int size) {

		final Vector minPos = GamePool.instance().popVector().set(Float.MAX_VALUE, Float.MAX_VALUE);
		final Vector maxPos = GamePool.instance().popVector().set(0, 0);
		final Vector centerPos = GamePool.instance().popVector().set(0, 0);
		for (int i = 0; i < size; i++) {
			final Vector point = polygon[i];
			centerPos.x += point.x;
			centerPos.y += point.y;
			minPos.x = Math.min(minPos.x, point.x);
			minPos.y = Math.min(minPos.y, point.y);
			maxPos.x = Math.max(maxPos.x, point.x);
			maxPos.y = Math.max(maxPos.y, point.y);
		}
		centerPos.x = centerPos.x / size;
		centerPos.y = centerPos.y / size;

		final float width = maxPos.x - minPos.x;
		final float height = maxPos.y - minPos.y;

		if (width > loader.getGameGOD().getMaxPolygonWidth() || width < loader.getGameGOD().getMinPolygonWidth()) {
			PlayN.log().error("PolygonGODFactory.handleClick() WRONG WIDTH");
			return false;
		}
		if (height > loader.getGameGOD().getMaxPolygonHeight() || height < loader.getGameGOD().getMinPolygonHeight()) {
			PlayN.log().error("PolygonGODFactory.handleClick() WRONG HEIGHT");
			return false;
		}

		MathUtils.copyPolygon(polygon, currentPolygon);
		this.size.setSize(width, height);
		this.centerPosition.set(centerPos);
		this.minOffset.set(minPos);
		this.currentSize = size;
		GamePool.instance().pushVector(3);

		return true;
	}

	@Override
	protected ShapeDefinition newShapeDefinition() {
		final Builder builder = PolygonDefinition.polygonBuilder();
		for (int i = 0; i < this.currentSize; i++) {
			builder.vertex(currentPolygon[i].x - this.centerPosition.x, currentPolygon[i].y - this.centerPosition.y);
		}
		return builder.centerPosition(this.centerPosition.x - this.minOffset.x, this.centerPosition.y - this.minOffset.y).size(this.size).build();
	}

	@Override
	public void draw() {
		final TextureDefinition imageDefinition = this.helper.getImageDefinition();

		final Pattern texture = imageDefinition.getImage().getImage().toPattern();
		this.canvas.clear();
		this.canvas.save();
		this.canvas.setLineCap(LineCap.ROUND);
		this.canvas.setLineJoin(LineJoin.ROUND);
		this.canvas.setStrokeColor(imageDefinition.getStrokeColor().asInt());
		this.canvas.setStrokeWidth(this.toView(imageDefinition.getStrokeWidth()));
		this.canvas.setFillPattern(texture);

		final Path path = this.canvas.createPath();
		path.moveTo(this.canvasViewport.centerX() + this.toView(this.currentPolygon[0].x), this.canvasViewport.centerY() + this.toView(this.currentPolygon[0].y));
		for (int i = 1; i < currentSize; i++) {
			path.lineTo(this.canvasViewport.centerX() + this.toView(this.currentPolygon[i].x), this.canvasViewport.centerY() + this.toView(this.currentPolygon[i].y));
		}
		path.close();

		this.canvas.fillPath(path);
		this.canvas.strokePath(path);
		this.canvas.restore();
	}

}
