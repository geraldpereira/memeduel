package fr.byob.game.memeduel.core.editor;

import playn.core.Canvas;
import pythagoras.f.Rectangle;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.helper.ObjectLoadHelper;
import fr.byob.game.memeduel.core.god.image.TextureDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.B2DUpdateDefinition;

public abstract class ObjectGODFactory implements B2DGODFactory<B2DGameObjectDefiniton> {

	protected final Canvas canvas;
	protected ObjectLoadHelper helper;
	protected Rectangle canvasViewport;
	private float drawFactor;
	protected final MemeDuelGODLoader loader;

	public ObjectGODFactory(final Canvas canvas, final MemeDuelGODLoader loader, final String type, final Rectangle canvasViewport) {
		this.loader = loader;
		this.helper = loader.getGameOptionsGOD().getLoadHelper(type);
		this.canvas = canvas;
		this.setCanvasViewport(canvasViewport);
	}

	@Override
	public void setType(final String type) {
		this.helper = loader.getGameOptionsGOD().getLoadHelper(type);
	}

	@Override
	public void setCanvasViewport(final Rectangle canvasViewport) {
		this.canvasViewport = canvasViewport;
		final float widthDrawFactor = canvasViewport.width / loader.getGameGOD().getMaxPolygonWidth();
		final float heightDrawFactor = canvasViewport.height / loader.getGameGOD().getMaxPolygonHeight();
		this.drawFactor = Math.min(widthDrawFactor, heightDrawFactor);
	}

	protected float toView(final float value) {
		return value * this.drawFactor;
	}

	protected float toModel(final float value) {
		return value / this.drawFactor;
	}

	@Override
	public B2DGameObjectDefiniton getGOD(final Vector position, final float angle) {
		final TextureDefinition imageDefinition = this.helper.getImageDefinition();
		final B2DBodyDefinition b2dBodyDefinition = this.helper.getB2DBodyDefinition();
		final DamageDefinition damageDefinition = this.helper.getDamageDefinition();
		final ShapeDefinition shapeDefinition = this.newShapeDefinition();

		final LayerDefinition layerDefinition = LayerDefinition.builder().fromTexturedShapeDefinition(shapeDefinition).build();

		return B2DGameObjectDefiniton.b2dBuilder()
				.selectable(this.helper.isSelectable())
				.shapeDefinition(shapeDefinition)
				.b2dBodyDefinition(b2dBodyDefinition)
				.updateDefinition(B2DUpdateDefinition.builder().build())
				.damageDefinition(damageDefinition)
				.layerDefinition(layerDefinition)
				.imageDefinition(imageDefinition)
				.position(position)
				.angle(angle)
				.build();
	}

	protected abstract ShapeDefinition newShapeDefinition();
}