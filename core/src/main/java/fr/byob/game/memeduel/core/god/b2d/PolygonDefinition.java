package fr.byob.game.memeduel.core.god.b2d;

import playn.core.Json;
import playn.core.Json.Object;
import playn.core.Json.Writer;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.Box2D;
import fr.byob.game.box2d.collision.shapes.PolygonShape;
import fr.byob.game.box2d.common.Settings;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.b2d.PolygonBodyHandler;
import fr.byob.game.memeduel.core.view.handler.image.PolygonImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ShapedImageHandler;

public class PolygonDefinition extends BoxDefinition {

	private final static Builder builder = new Builder();

	public static Builder polygonBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends BoxDefinition.Builder<PolygonDefinition> {

		private final Vector[] points = new Vector[Settings.maxPolygonVertices];
		private final Vector centerPosition = new Vector();
		private int count;

		private Builder() {
			MathUtils.fillPolygon(points);
		}

		@Override
		public void reset() {
			super.reset();
			count = 0;
			centerPosition.set(0, 0);
			MathUtils.resetPolygon(points);
		}

		public Builder vertices(final Vector[] points) {

			if (MemeDuel.debug && !MathUtils.isConvexPolygon(points, points.length)) {
				PlayN.log().error("malformed polygon!");
			}

			count = MathUtils.copyPolygon(points, this.points);
			return this;
		}

		public Builder vertices(final Vector[] points, final int count) {

			if (MemeDuel.debug && !MathUtils.isConvexPolygon(points, points.length)) {
				PlayN.log().error("malformed polygon!");
			}

			MathUtils.copyPolygon(points, this.points);

			this.count = count;
			return this;
		}

		public Builder vertex(final float x, final float y) {
			points[count].set(x, y);
			count++;
			return this;
		}

		public Builder centerPosition(final Vector centerPosition) {
			this.centerPosition.set(centerPosition);
			return this;
		}

		public Builder centerPosition(final float x, final float y) {
			this.centerPosition.set(x, y);
			return this;
		}

		@Override
		public PolygonDefinition build() {
			return new PolygonDefinition(this.size, this.centerPosition, points, count);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);

			final Json.Object centerPositionJSON = jsonEntity.getObject("centerPosition");
			final float x = centerPositionJSON.getNumber("x");
			final float y = centerPositionJSON.getNumber("y");
			this.centerPosition(x, y);

			// final boolean isFragment = godJSON.getBoolean("isFragment");

			final Json.Array pointsJSON = jsonEntity.getArray("points");
			for (int i = 0; i < pointsJSON.length(); i++) {
				final Json.Object pointJSON = pointsJSON.getObject(i);
				final float xPoint = pointJSON.getNumber("x");
				final float yPoint = pointJSON.getNumber("y");
				this.vertex(xPoint, yPoint);
			}

			return this;
		}

		@Override
		public Builder fromOther(final PolygonDefinition other) {
			super.fromOther(other);

			final Vector[] vertices = GamePool.instance().popVectors(count);
			for (int i = 0; i < other.getPolygonShape().getVertexCount(); i++) {
				other.getPolygonShape().getVertex(i, vertices[i]);
			}

			this.vertices(vertices).centerPosition(other.getCenterPosition());

			GamePool.instance().pushVector(count);
			return this;
		}

	}

	protected final PolygonShape polygonShape;

	protected PolygonDefinition(final Dimension size, final Vector centerPosition, final Vector[] points, final int count) {
		super(size);
		this.centerPosition.set(centerPosition);
		final Box2D box2d = GameContext.instance().getBox2D();
		this.polygonShape = box2d.newPolygonShape();
		this.polygonShape.set(points, count);
	}

	public PolygonShape getPolygonShape() {
		return polygonShape;
	}

	@Override
	public Vector getCenterPosition() {
		return this.centerPosition;
	}

	@Override
	public Shape getShapeType() {
		return Shape.POLYGON;
	}

	@Override
	public void save(final Writer writer) {
		writer.value("shape", Shape.POLYGON.name());

		writer.object("size");
		writer.value("width", this.size.width);
		writer.value("height", this.size.height);
		writer.end();

		writer.object("centerPosition");
		writer.value("x", this.centerPosition.x);
		writer.value("y", this.centerPosition.y);
		writer.end();

		writer.array("points");
		final Vector point = GamePool.instance().popVector();
		for (int i = 0; i < polygonShape.getVertexCount(); i++) {
			polygonShape.getVertex(i, point);
			writer.object();
			writer.value("x", point.x);
			writer.value("y", point.y);
			writer.end();
		}

		GamePool.instance().pushVector(1);
		writer.end();
	}

	@Override
	public B2DBodyHandler newModelHandler() {
		return new PolygonBodyHandler();
	}

	@Override
	public ShapedImageHandler newViewHandler() {
		return new PolygonImageHandler();
	}
}
