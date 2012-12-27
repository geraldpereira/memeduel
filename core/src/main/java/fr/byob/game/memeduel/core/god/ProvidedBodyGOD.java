package fr.byob.game.memeduel.core.god;

import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.World;
import fr.byob.game.memeduel.core.god.b2d.B2DBodyDefinition;
import fr.byob.game.memeduel.core.god.b2d.ShapeDefinition;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.handler.b2d.AbstractBodyHandler;
import fr.byob.game.memeduel.core.model.handler.b2d.B2DBodyHandler;
import fr.byob.game.memeduel.core.model.handler.damage.DamageHandler;
import fr.byob.game.memeduel.core.model.handler.selection.DefaultSelectionHandler;
import fr.byob.game.memeduel.core.model.handler.selection.SelectionHandler;
import fr.byob.game.memeduel.core.model.handler.update.UpdateHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;
import fr.byob.game.memeduel.core.view.View;
import fr.byob.game.memeduel.core.view.handler.image.DefaultImageHandler;
import fr.byob.game.memeduel.core.view.handler.layer.InterpolateLayerHandler;
import fr.byob.game.memeduel.core.view.object.DynamicViewObject;

public class ProvidedBodyGOD extends B2DGameObjectDefiniton {

	private final static Builder builder = new Builder();

	public static Builder providedBuilder() {
		builder.reset();
		return builder;
	}

	public static class Builder extends B2DGameObjectDefiniton.Builder<ProvidedBodyGOD> {

		protected Body body;

		private Builder() {
		}

		@Override
		public void reset() {
			body = null;
		}

		public Builder body(final Body body) {
			this.body = body;
			return this;
		}

		@Override
		public ProvidedBodyGOD build() {
			return new ProvidedBodyGOD(this.position, this.angle, this.layerDefinition, this.imageDefinition, this.updateDefinition, this.damageDefinition, this.b2dBodyDefinition, this.shapeDefinition, this.selectable, this.body);
		}

	}

	private final Body curBody;

	protected ProvidedBodyGOD(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition, final B2DBodyDefinition b2dBodyDefinition,
			final ShapeDefinition shapeDefinition, final boolean selectable, final Body curBody) {
		super(position, angle, layerDefinition, imageDefinition, updateDefinition, damageDefinition, b2dBodyDefinition, shapeDefinition, selectable);
		this.curBody = curBody;
	}

	@Override
	public B2DModelObject newModelObject(final AbstractModel model) {
		final UpdateHandler updateHandler = this.updateDefinition.newModelHandler();
		final B2DBodyHandler b2dBodyHandler = new AbstractBodyHandler() {
			@Override
			public void addBody(final World world) {
				this.body = ProvidedBodyGOD.this.curBody;
			}
		};

		DamageHandler damageHandler = null;
		if (this.damageDefinition != null) {
			damageHandler = this.damageDefinition.newModelHandler();
		}

		SelectionHandler selectionHandler = null;
		if (this.selectable) {
			selectionHandler = new DefaultSelectionHandler();
		}

		final B2DModelObject modelObject = new B2DModelObject(model.newId(), this, updateHandler, damageHandler, selectionHandler, b2dBodyHandler);
		modelObject.init(model);
		return modelObject;
	}

	@Override
	public DynamicViewObject newViewObject(final View view, final ModelObject modelObject) {
		final DynamicViewObject viewObject = new DynamicViewObject(modelObject, new DefaultImageHandler(), new InterpolateLayerHandler());
		viewObject.addToView(view);
		return viewObject;
	}
}
