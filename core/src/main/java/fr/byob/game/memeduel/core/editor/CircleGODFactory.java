package fr.byob.game.memeduel.core.editor;

import playn.core.Canvas;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.Pattern;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.b2d.CircleDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;

public class CircleGODFactory extends ObjectGODFactory{

	private float currentRadius;

	public CircleGODFactory(final Canvas canvas, final MemeDuelGODLoader loader, final String type, final Rectangle canvasViewport) {
		super(canvas, loader, type, canvasViewport);
		this.currentRadius = loader.getGameGOD().getMinPolygonRadius() * 2;
	}

	@Override
	public void handleClick(final Vector position) {
		final Vector canvasCenter = GamePool.instance().popVector().set(this.canvasViewport.center().x, this.canvasViewport.center().y);
		float radius = position.distance(canvasCenter);
		GamePool.instance().pushVector(1);

		radius = this.toModel(radius);

		if (radius > loader.getGameGOD().getMaxPolygonRadius()) {
			radius = loader.getGameGOD().getMaxPolygonRadius();
		} else if (radius < loader.getGameGOD().getMinPolygonRadius()) {
			radius = loader.getGameGOD().getMinPolygonRadius();
		}

		this.currentRadius = radius;

		this.draw();
	}

	@Override
	protected ShapeDefinition newShapeDefinition() {
		return CircleDefinition.circleBuilder().radius(this.currentRadius).build();
	}

	@Override
	public void draw() {
		final TextureDefinition imageDefinition = this.helper.getImageDefinition();

		final Pattern texture = imageDefinition.getImage().getImage().toPattern();

		final float x = this.canvasViewport.centerX();
		final float y = this.canvasViewport.centerY();
		final float radius = this.toView(this.currentRadius);

		this.canvas.clear();
		this.canvas.save();
		this.canvas.setLineCap(LineCap.ROUND);
		this.canvas.setLineJoin(LineJoin.ROUND);
		this.canvas.setStrokeColor(imageDefinition.getStrokeColor().asInt());
		this.canvas.setStrokeWidth(this.toView(imageDefinition.getStrokeWidth()));
		this.canvas.setFillPattern(texture);
		this.canvas.fillCircle(x, y, radius);
		this.canvas.strokeCircle(x, y, radius);
		this.canvas.restore();
	}

}
