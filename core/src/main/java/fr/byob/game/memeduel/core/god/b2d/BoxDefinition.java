package fr.byob.game.memeduel.core.god.b2d;

import playn.core.Json;
import playn.core.Json.Object;
import playn.core.Json.Writer;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.b2d.BoxBodyHandler;
import fr.byob.game.memeduel.core.view.handler.image.BoxImageHandler;
import fr.byob.game.memeduel.core.view.handler.image.ShapedImageHandler;

public class BoxDefinition implements ShapeDefinition {

	private final static Builder<BoxDefinition> builder = new Builder<BoxDefinition>();

	public static Builder<BoxDefinition> boxBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder<T extends BoxDefinition> implements GODBuilder<T> {

		protected final Dimension size = new Dimension();

		protected Builder() {
		}

		@Override
		public void reset() {
			size.setSize(0, 0);
		}

		public Builder<T> size(final Dimension size) {
			this.size.setSize(size);
			return this;
		}

		public Builder<T> size(final float width, final float height) {
			this.size.setSize(width, height);
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			return (T) new BoxDefinition(this.size);
		}

		@Override
		public Builder<T> fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			final Json.Object sizeJSON = jsonEntity.getObject("size");
			final float width = sizeJSON.getNumber("width");
			final float height = sizeJSON.getNumber("height");
			return this.size(width, height);
		}

		@Override
		public Builder<T> fromOther(final T other) {
			return this.size(other.getSize());
		}

	}

	protected final Dimension size;
	protected final Vector centerPosition;

	protected BoxDefinition(final float width, final float height) {
		this.size = new Dimension(width, height);
		this.centerPosition = new Vector(this.size.width / 2, this.size.height / 2);
	}

	protected BoxDefinition(final Dimension size) {
		this.size = new Dimension(size);
		this.centerPosition = new Vector(this.size.width / 2, this.size.height / 2);
	}

	@Override
	public Dimension getSize() {
		return this.size;
	}

	@Override
	public Shape getShapeType() {
		return Shape.BOX;
	}

	@Override
	public void save(final Writer writer) {
		writer.value("shape", Shape.BOX.name());
		writer.object("size");
		writer.value("width", this.size.width);
		writer.value("height", this.size.height);
		writer.end();
	}

	@Override
	public Vector getCenterPosition() {
		return centerPosition;
	}

	@Override
	public B2DBodyHandler newModelHandler() {
		return new BoxBodyHandler();
	}

	@Override
	public ShapedImageHandler newViewHandler() {
		return new BoxImageHandler();
	}
}
