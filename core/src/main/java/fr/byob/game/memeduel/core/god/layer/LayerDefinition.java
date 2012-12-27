package fr.byob.game.memeduel.core.god.layer;

import playn.core.Json.Object;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.MathUtils;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.builder.GODBuilder;
import fr.byob.game.memeduel.core.god.builder.ViewHandlerFactory;
import fr.byob.game.memeduel.core.view.ZOrder;
import fr.byob.game.memeduel.core.view.handler.layer.LayerHandler;
import fr.byob.game.memeduel.core.view.handler.layer.StaticLayerHandler;

//public LayerDefinition(final GameImage image,final float depth, final ZOrder zOrder) {
//	this(image.newDimension(), depth, zOrder);
//}
//
//public LayerDefinition(final Dimension layerSize, final float depth, final ZOrder zOrder) {
//	this(layerSize, DEFAULT_LAYER_ORIGIN, depth, zOrder);
//}
//
//public LayerDefinition(final Dimension layerSize, final Vector layerOrigin, final float depth, final ZOrder zOrder) {
//	this(layerSize, layerOrigin, DEFAULT_LAYER_SCALE, depth, zOrder, false);
//}
//
//// Box2d object layer def constructor
//public LayerDefinition(final ShapeDefinition shapeDefinition) {
//	final Dimension viewSize = ViewUtils.toInitView(shapeDefinition.getSize());
//	this.depth = 50;
//	this.zOrder = ZOrder.DYNAMIC;
//	this.repeatX = false;
//	this.layerSize = viewSize;
//	this.layerOrigin = ViewUtils.toInitView(shapeDefinition.getCenterPosition());
//	this.layerScale = new Vector (shapeDefinition.getSize().width / viewSize.width, shapeDefinition.getSize().height / viewSize.height);
//}

public class LayerDefinition implements ViewHandlerFactory<LayerHandler> {
	public final static Vector DEFAULT_LAYER_SCALE = new Vector (1 / ViewUtils.getInitPhysUnitPerScreenUnit(), 1 / ViewUtils.getInitPhysUnitPerScreenUnit());
	public final static Vector DEFAULT_LAYER_ORIGIN = new Vector (0, 0);

	private final static Builder builder = new Builder();

	public static Builder builder() {
		builder.reset();
		return builder;
	}

	public static class Builder implements GODBuilder<LayerDefinition> {

		private float depth = 50;
		private ZOrder zOrder = ZOrder.DYNAMIC;
		private boolean repeatX = false;

		private final Dimension layerSize = new Dimension();
		private final Vector layerOrigin = new Vector().set(DEFAULT_LAYER_ORIGIN);
		private final Vector layerScale = new Vector().set(DEFAULT_LAYER_SCALE);

		private Builder() {
		}

		@Override
		public void reset() {
			depth = 50;
			zOrder = ZOrder.DYNAMIC;
			repeatX = false;
			layerSize.setSize(0, 0);
			layerOrigin.set(DEFAULT_LAYER_ORIGIN);
			layerScale.set(DEFAULT_LAYER_SCALE);
		}

		public Builder depth(final float depth) {
			this.depth = depth;
			return this;
		}

		public Builder zOrder(final ZOrder zOrder) {
			this.zOrder = zOrder;
			return this;
		}

		public Builder repeatX(final boolean repeatX) {
			this.repeatX = repeatX;
			return this;
		}

		public Builder layerSize(final Dimension layerSize) {
			this.layerSize.setSize(layerSize);
			return this;
		}

		public Builder layerSize(final float w, final float h) {
			this.layerSize.setSize(w, h);
			return this;
		}

		public Builder layerSize(final GameImage image) {
			image.dimensionToOut(this.layerSize);
			return this;
		}

		public Builder layerOrigin(final Vector layerOrigin) {
			this.layerOrigin.set(layerOrigin);
			return this;
		}

		public Builder layerOrigin(final float x, final float y) {
			this.layerOrigin.set(x, y);
			return this;
		}

		public Builder layerOrigin(final GameImage image) {
			image.centerPositionToOut(this.layerOrigin);
			return this;
		}

		public Builder layerScale(final Vector layerScale) {
			this.layerScale.set(layerScale);
			return this;
		}

		public Builder layerScale(final float x, final float y) {
			this.layerScale.set(x, y);
			return this;
		}

		@Override
		public LayerDefinition build() {
			return new LayerDefinition(this.layerSize, this.layerOrigin, this.layerScale, this.depth, this.zOrder, this.repeatX);
		}

		@Override
		public Builder fromJSON(final AllGODLoader godLoader, final Object jsonEntity) {
			return this;
		}

		@Override
		public Builder fromOther(final LayerDefinition other) {
			return this.depth(other.getDepth())
					.zOrder(other.getZOrder())
					.repeatX(other.isRepeatX())
					.layerSize(other.getLayerSize())
					.layerOrigin(other.getLayerOrigin())
					.layerScale(other.getLayerScale());
		}

		public Builder fromTexturedShapeDefinition(final ShapeDefinition shapeDefinition) {
			final Dimension viewSize = GamePool.instance().popDimension();
			ViewUtils.modelToInitView(shapeDefinition.getSize(), viewSize);
			fromShapeDefinition(shapeDefinition).layerScale(shapeDefinition.getSize().width / viewSize.width, shapeDefinition.getSize().height / viewSize.height);
			GamePool.instance().pushDimension(1);
			return this;
		}

		public Builder fromShapeDefinition(final ShapeDefinition shapeDefinition) {
			final Dimension viewSize = ViewUtils.modelToInitView(shapeDefinition.getSize(), MathUtils.tmpDim());

			final Vector layerOrigin = GamePool.instance().popVector();

			ViewUtils.modelToInitView(shapeDefinition.getCenterPosition(),layerOrigin);

			this.depth(50)
			.zOrder(ZOrder.DYNAMIC)
			.repeatX(false)
			.layerSize(viewSize)
			.layerOrigin(layerOrigin);

			GamePool.instance().pushVector(1);

			return this;
		}

	}

	private final float depth;
	private final ZOrder zOrder;
	private final boolean repeatX;

	private final Dimension layerSize;
	private final Vector layerOrigin;
	private final Vector layerScale;

	private LayerDefinition(final Dimension layerSize, final Vector layerOrigin, final Vector layerScale, final float depth, final ZOrder zOrder, final boolean repeatX) {
		this.depth = depth;
		this.zOrder = zOrder;
		this.repeatX = repeatX;
		this.layerSize = new Dimension(layerSize);
		this.layerOrigin = new Vector(layerOrigin);
		this.layerScale = new Vector(layerScale);
	}

	public float getDepth() {
		return this.depth;
	}

	public ZOrder getZOrder() {
		return this.zOrder;
	}

	public boolean isRepeatX() {
		return this.repeatX;
	}

	public Dimension getLayerSize() {
		return this.layerSize;
	}

	public Vector getLayerOrigin() {
		return this.layerOrigin;
	}

	public Vector getLayerScale() {
		return this.layerScale;
	}

	@Override
	public LayerHandler newViewHandler() {
		return new StaticLayerHandler();
	}

}
