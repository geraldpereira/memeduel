package fr.byob.game.memeduel.core.god;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.view.ZOrder;

/**
 * Only the ground of the static world is drawn
 * 
 * @author gpereira
 * 
 */
public class WorldGOD extends StaticGameObjectDefinition {

	private final static Builder builder = new Builder();

	public static Builder worldBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends StaticGameObjectDefinition.Builder<WorldGOD> {

		private final Dimension size = new Dimension();
		private float groundHeight;
		private float restitution = 0.2f;
		private float friction = 0.5f;

		private Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			size.setSize(0, 0);
			groundHeight = 0;
			restitution = 0.2f;
			friction = 0.5f;
		}

		public Builder size(final Dimension size) {
			this.size.setSize(size);
			return this;
		}

		public Builder size(final float width, final float height) {
			this.size.setSize(width, height);
			return this;
		}

		public Builder groundHeight(final float groundHeight) {
			this.groundHeight = groundHeight;
			return this;
		}

		public Builder restitution(final float restitution) {
			this.restitution = restitution;
			return this;
		}

		public Builder friction(final float friction) {
			this.friction = friction;
			return this;
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			super.fromJSON(godLoader, jsonEntity);

			final Json.Object sizeJSON = jsonEntity.getObject("size");
			final float width = sizeJSON.getNumber("width");
			final float height = sizeJSON.getNumber("height");
			this.groundHeight(jsonEntity.getNumber("groundHeight"));
			this.size(width, height);

			if (jsonEntity.containsKey("restitution")) {
				this.restitution(jsonEntity.getNumber("restitution"));
			}

			if (jsonEntity.containsKey("friction")) {
				this.friction(jsonEntity.getNumber("friction"));
			}

			this.imageDefinition(ImageDefinition.imageBuilder().fromJSON(godLoader, jsonEntity).build());
			this.layerDefinition(LayerDefinition.builder().layerSize(new Dimension(ViewUtils.toInitView(width), this.imageDefinition.getImage().getImage().height())).depth(100).zOrder(ZOrder.FRONT).repeatX(true).build());
			return this;
		}

		@Override
		public WorldGOD build() {
			return new WorldGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.size, this.groundHeight, this.restitution, this.friction);
		}

	}

	private final Dimension size;
	private final float groundHeight;
	private final float restitution;
	private final float friction;

	private WorldGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final Dimension size, final float groundHeight, final float restitution, final float friction) {
		super(position, angle, layerDefinition, imageDefinition);
		this.size = new Dimension(size);
		this.groundHeight = groundHeight;
		this.restitution = restitution;
		this.friction = friction;
	}

	public Dimension getSize() {
		return this.size;
	}

	public float getWidth() {
		return this.size.width;
	}

	public float getHeight() {
		return this.size.height;
	}

	public float getGroundHeight() {
		return this.groundHeight;
	}

	public float getRestitution() {
		return this.restitution;
	}

	public float getFriction() {
		return this.friction;
	}

	public float getGroundYPosition() {
		return size.height - groundHeight;
	}
}
