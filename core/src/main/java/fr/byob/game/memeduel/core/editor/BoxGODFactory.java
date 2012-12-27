package fr.byob.game.memeduel.core.editor;

import playn.core.Canvas;
import playn.core.Canvas.LineCap;
import playn.core.Canvas.LineJoin;
import playn.core.Pattern;
import pythagoras.f.Dimension;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.b2d.BoxDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;

public class BoxGODFactory extends ObjectGODFactory{

	private final Dimension currentBox;

	public BoxGODFactory(final Canvas canvas, final MemeDuelGODLoader loader, final String type, final Rectangle canvasViewport) {
		super(canvas, loader, type, canvasViewport);
		this.currentBox = new Dimension(loader.getGameGOD().getMinPolygonWidth() * 2, loader.getGameGOD().getMinPolygonHeight() * 2);
	}

	@Override
	public void handleClick(final Vector position) {
		float width = 2 * Math.abs(this.canvasViewport.centerX() - position.x);
		float height = 2 * Math.abs(this.canvasViewport.centerY() - position.y);

		// if (height > width) {
		// final float tmp = width;
		// width = height;
		// height = tmp;
		// }

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

		this.currentBox.setSize(width, height);

		this.draw();
	}


	@Override
	protected ShapeDefinition newShapeDefinition() {
		return BoxDefinition.boxBuilder().size(this.currentBox).build();
	}

	@Override
	public void draw() {
		final TextureDefinition imageDefinition = this.helper.getImageDefinition();

		final Pattern texture = imageDefinition.getImage().getImage().toPattern();

		final float x = this.canvasViewport.centerX() - this.toView(this.currentBox.width /2);
		final float y = this.canvasViewport.centerY() - this.toView(this.currentBox.height /2);
		final float width = this.toView(this.currentBox.width);
		final float height = this.toView(this.currentBox.height);

		this.canvas.clear();
		this.canvas.save();
		this.canvas.setLineCap(LineCap.ROUND);
		this.canvas.setLineJoin(LineJoin.ROUND);
		this.canvas.setStrokeColor(imageDefinition.getStrokeColor().asInt());
		this.canvas.setStrokeWidth(this.toView(imageDefinition.getStrokeWidth()));
		this.canvas.setFillPattern(texture);
		this.canvas.fillRect(x, y, width, height);
		this.canvas.strokeRect(x, y, width, height);
		this.canvas.restore();
	}
}
