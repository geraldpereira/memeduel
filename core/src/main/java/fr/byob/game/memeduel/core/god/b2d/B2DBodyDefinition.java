package fr.byob.game.memeduel.core.god.b2d;

import playn.core.Json.Object;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;

public class B2DBodyDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<B2DBodyDefinition> {

		private String id;
		private float density;
		private float friction;
		private float restitution;
		private BodyType bodyType;
		private boolean bullet;
		private boolean fixedRotation;

		private Builder() {
		}

		@Override
		public void reset() {
			id = null;
			density = 0;
			friction = 0;
			restitution = 0;
			bodyType = null;
			bullet = true;
			fixedRotation = true;
		}

		public Builder id(final String id) {
			this.id = id;
			return this;
		}

		public Builder density(final float density) {
			this.density = density;
			return this;
		}

		public Builder friction(final float friction) {
			this.friction = friction;
			return this;
		}

		public Builder restitution(final float restitution) {
			this.restitution = restitution;
			return this;
		}

		public Builder bodyType(final BodyType bodyType) {
			this.bodyType = bodyType;
			return this;
		}

		public Builder bullet(final boolean bullet) {
			this.bullet = bullet;
			return this;
		}

		public Builder fixedRotation(final boolean fixedRotation) {
			this.fixedRotation = fixedRotation;
			return this;
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			this.id(jsonEntity.getString("type")).density(jsonEntity.getNumber("density")).friction(jsonEntity.getNumber("friction")).restitution(jsonEntity.getNumber("restitution"));
			final String bodyTypeStr = jsonEntity.getString("bodyType");
			this.bodyType(BodyType.valueOf(bodyTypeStr));
			if (this.bodyType == BodyType.DYNAMIC) {
				this.bullet(jsonEntity.getBoolean("bullet"));
			} else {
				this.bullet(false);
			}

			if (jsonEntity.containsKey("fixedRotation")) {
				this.fixedRotation(jsonEntity.getBoolean("fixedRotation"));
			} else {
				this.fixedRotation(false);
			}

			return this;
		}

		@Override
		public Builder fromOther(final B2DBodyDefinition other) {
			return this.id(other.getId()).density(other.getDensity()).friction(other.getFriction()).restitution(other.getRestitution()).bodyType(other.getBodyType()).bullet(other.isBullet()).fixedRotation(other.isFixedRotation());
		}

		@Override
		public B2DBodyDefinition build() {
			return new B2DBodyDefinition(this.id, this.density, this.friction, this.restitution, this.bodyType, this.bullet, this.fixedRotation);
		}

	}

	private final String id;
	private final float density;
	private final float friction;
	private final float restitution;
	private final BodyType bodyType;
	private final boolean bullet;
	private final boolean fixedRotation;

	private B2DBodyDefinition(final String id, final float density, final float friction, final float restitution, final BodyType bodyType, final boolean bullet, final boolean fixedRotation) {
		super();
		this.id = id;
		this.density = density;
		this.friction = friction;
		this.restitution = restitution;
		this.bodyType = bodyType;
		this.bullet = bullet;
		this.fixedRotation = fixedRotation;
	}

	public float getDensity() {
		return this.density;
	}

	public float getFriction() {
		return this.friction;
	}

	public float getRestitution() {
		return this.restitution;
	}

	public BodyType getBodyType() {
		return this.bodyType;
	}

	public boolean isBullet() {
		return this.bullet;
	}

	public String getId() {
		return this.id;
	}

	public boolean isFixedRotation() {
		return this.fixedRotation;
	}

}
