package fr.byob.game.memeduel.core.god.cannon;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Dimension;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.GameObjectDefinition;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;

public class CannonGOD implements GameObjectDefinition {

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<CannonGOD> {

		private GameImage cannonImage;
		private final Dimension cannonDimension = new Dimension();

		private GameImage boltImage;
		private final Dimension boltDimension = new Dimension();

		private GameImage wheelImage;
		private float wheelRadius;// in pixels

		private GameImage baseImage;
		private final Dimension baseDimension = new Dimension(); // in pixels
		private float baseThickness;// in pixels

		private float xPosition; // in meters

		private Builder() {
		}

		@Override
		public void reset() {
			cannonImage = null;
			cannonDimension.setSize(0, 0);
			boltImage = null;
			boltDimension.setSize(0, 0);
			wheelImage = null;
			wheelRadius = 0;
			baseImage = null;
			baseDimension.setSize(0, 0);
			baseThickness = 0;
			xPosition = 0;
		}

		@Override
		public CannonGOD build() {
			return new CannonGOD(cannonImage, cannonDimension, boltImage, boltDimension, wheelImage, wheelRadius, baseImage, baseDimension, baseThickness, xPosition);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			final String cannonImage = jsonEntity.getString("cannonImage");
			final Json.Object cannonSizeJSON = jsonEntity.getObject("cannonDimension");
			final float canonWidth = cannonSizeJSON.getInt("width");
			final float cannonHeight = cannonSizeJSON.getInt("height");

			final String boltImage = jsonEntity.getString("boltImage");
			final Json.Object boltSizeJSON = jsonEntity.getObject("boltDimension");
			final float boltWidth = boltSizeJSON.getInt("width");
			final float boltHeight = boltSizeJSON.getInt("height");

			final String wheelImage = jsonEntity.getString("wheelImage");
			this.wheelRadius = jsonEntity.getNumber("wheelRadius");

			final String baseImage = jsonEntity.getString("baseImage");
			final Json.Object baseSizeJSON = jsonEntity.getObject("baseDimension");
			final float baseWidth = baseSizeJSON.getInt("width");
			final float baseHeight = baseSizeJSON.getInt("height");
			this.baseThickness = jsonEntity.getNumber("baseThickness");

			this.xPosition = jsonEntity.getNumber("xPosition");

			this.cannonImage = GameImage.valueOf(cannonImage);
			this.cannonDimension.setSize(canonWidth, cannonHeight);
			this.boltImage = GameImage.valueOf(boltImage);
			this.boltDimension.setSize(boltWidth, boltHeight);
			this.wheelImage = GameImage.valueOf(wheelImage);
			this.baseImage = GameImage.valueOf(baseImage);
			this.baseDimension.setSize(baseWidth, baseHeight);

			return this;
		}

		@Override
		public Builder fromOther(final CannonGOD other) {
			throw new UnsupportedOperationException("Ne need to clone cannons until now!");
		}

	}

	private final GameImage cannonImage;
	private final Dimension cannonDimension;

	private final GameImage boltImage;
	private final Dimension boltDimension;

	private final GameImage wheelImage;
	private final float wheelRadius;// in pixels

	private final GameImage baseImage;
	private final Dimension baseDimension; // in pixels
	private final float baseThickness;// in pixels

	private final float xPosition; // in meters

	private CannonGOD(final GameImage cannonImage, final Dimension cannonDimension, final GameImage boltImage, final Dimension boltDimension, final GameImage wheelImage, final float wheelRadius, final GameImage baseImage, final Dimension baseDimension, final float baseThickness,
			final float xPosition) {
		super();
		this.cannonImage = cannonImage;
		this.cannonDimension = cannonDimension;
		this.boltImage = boltImage;
		this.boltDimension = boltDimension;
		this.wheelImage = wheelImage;
		this.wheelRadius = wheelRadius;
		this.baseImage = baseImage;
		this.baseDimension = baseDimension;
		this.baseThickness = baseThickness;
		this.xPosition = xPosition;
	}

	public GameImage getCannonMDImage() {
		return this.cannonImage;
	}

	public Dimension getCannonDimension() {
		return this.cannonDimension;
	}

	public GameImage getBoltMDImage() {
		return this.boltImage;
	}

	public Dimension getBoltDimension() {
		return this.boltDimension;
	}

	public GameImage getWheelMDImage() {
		return this.wheelImage;
	}

	public float getWheelRadius() {
		return this.wheelRadius;
	}

	public GameImage getBaseMDImage() {
		return this.baseImage;
	}

	public Dimension getBaseDimension() {
		return this.baseDimension;
	}

	public float getBaseThickness() {
		return this.baseThickness;
	}

	public float getXPosition() {
		return this.xPosition;
	}

	public float getDepth() {
		return 80;
	}

}
