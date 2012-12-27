package fr.byob.game.memeduel.core.god.b2d;

import playn.core.Json.Object;
import playn.core.Json.Writer;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.b2d.CircleBodyHandler;
import fr.byob.game.memeduel.core.view.handler.image.CircleImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ShapedImageHandler;


public class CircleDefinition extends BoxDefinition {

	private final static Builder builder = new Builder();

	public static Builder circleBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<CircleDefinition> {

		private float radius;

		private Builder() {
		}

		@Override
		public void reset() {
			radius = 0;
		}

		public Builder radius(final float radius) {
			this.radius = radius;
			return this;
		}

		@Override
		public CircleDefinition build() {
			return new CircleDefinition(this.radius);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			return this.radius(jsonEntity.getNumber("radius"));
		}

		@Override
		public Builder fromOther(final CircleDefinition other) {
			return this.radius(other.getRadius());
		}

	}

	private final float radius;

	protected CircleDefinition(final float radius) {
		super(2 * radius, 2 * radius);
		this.radius = radius;
	}

	public float getRadius() {
		return this.radius;
	}

	@Override
	public Shape getShapeType() {
		return Shape.CIRCLE;
	}

	@Override
	public void save(final Writer writer) {
		writer.value("shape", Shape.CIRCLE.name());
		writer.value("radius", this.radius);
	}

	@Override
	public B2DBodyHandler newModelHandler() {
		return new CircleBodyHandler();
	}

	@Override
	public ShapedImageHandler newViewHandler() {
		return new CircleImageHandler();
	}
}
