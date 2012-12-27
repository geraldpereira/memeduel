package fr.byob.game.memeduel.core.editor;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.box2d.common.Settings;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;

public class TriangleGODFactory extends PolygonGODFactory {

	public TriangleGODFactory(final Canvas canvas, final MemeDuelGODLoader loader, final String type, final Rectangle canvasViewport) {
		super(canvas, loader, type, canvasViewport);
	}

	@Override
	protected void initPolygon() {
		final Dimension size = GamePool.instance().popDimension();
		size.setSize(loader.getGameGOD().getMinPolygonWidth() * 2, loader.getGameGOD().getMinPolygonHeight() * 3);

		final Vector[] polygon = GamePool.instance().popVectors(Settings.maxPolygonVertices);
		polygon[0].set(0, -size.height / 2);
		polygon[1].set(size.width / 2, size.height / 2);
		polygon[2].set(-size.width / 2, size.height / 2);

		setCurrentPolygon(polygon, 3);

		GamePool.instance().pushDimension(1);
		GamePool.instance().pushVector(Settings.maxPolygonVertices);
	}

	@Override
	public void handleClick(final Vector position) {
		float width = 2 * Math.abs(this.canvasViewport.centerX() - position.x);
		float height = 2 * Math.abs(this.canvasViewport.centerY() - position.y);

		width = this.toModel(width);
		height = this.toModel(height);

		if (width > loader.getGameGOD().getMaxPolygonWidth()) {
			width = loader.getGameGOD().getMaxPolygonWidth();
		} else if (width < loader.getGameGOD().getMinPolygonWidth()) {
			width = loader.getGameGOD().getMinPolygonWidth();
		}

		if (height > loader.getGameGOD().getMaxPolygonHeight()) {
			height = loader.getGameGOD().getMaxPolygonHeight();
		} else if (height < loader.getGameGOD().getMinPolygonHeight()) {
			height = loader.getGameGOD().getMinPolygonHeight();
		}

		final Vector[] polygon = GamePool.instance().popVectors(Settings.maxPolygonVertices);

		polygon[0].set(0, -height / 2);
		polygon[1].set(width / 2, height / 2);
		polygon[2].set(-width / 2, height / 2);

		this.setCurrentPolygon(polygon, 3);
		this.draw();

		GamePool.instance().pushVector(Settings.maxPolygonVertices);
	}
}
