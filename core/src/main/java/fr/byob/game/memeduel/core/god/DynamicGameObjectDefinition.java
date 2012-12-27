package fr.byob.game.memeduel.core.god;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.damage.DamageDefinition;
import fr.byob.game.memeduel.core.god.image.ImageDefinition;
import fr.byob.game.memeduel.core.god.layer.LayerDefinition;
import fr.byob.game.memeduel.core.god.update.UpdateDefinition;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;


public abstract class DynamicGameObjectDefinition extends StaticGameObjectDefinition {

	public abstract static class Builder<T extends DynamicGameObjectDefinition> extends StaticGameObjectDefinition.Builder<T> {

		protected UpdateDefinition updateDefinition;
		protected DamageDefinition damageDefinition;

		protected Builder() {
		}

		@Override
		public void reset() {
			super.reset();
			updateDefinition = null;
			damageDefinition = null;
		}

		public Builder<T> updateDefinition(final UpdateDefinition updateDefinition) {
			this.updateDefinition = updateDefinition;
			return this;
		}

		public Builder<T> damageDefinition(final DamageDefinition damageDefinition) {
			this.damageDefinition = damageDefinition;
			return this;
		}

		@Override
		public Builder<T> fromOther(final T other) {
			super.fromOther(other);
			return this.updateDefinition(other.getUpdateDefinition()).damageDefinition(other.getDamageDefinition());
		}


	}

	protected final UpdateDefinition updateDefinition;
	protected final DamageDefinition damageDefinition;

	protected DynamicGameObjectDefinition(final Vector position, final float angle, final LayerDefinition layerDefinition, final ImageDefinition imageDefinition, final UpdateDefinition updateDefinition, final DamageDefinition damageDefinition) {
		super(position, angle, layerDefinition, imageDefinition);
		this.updateDefinition = updateDefinition;
		this.damageDefinition = damageDefinition;
	}

	public UpdateDefinition getUpdateDefinition() {
		return this.updateDefinition;
	}

	public DamageDefinition getDamageDefinition() {
		return this.damageDefinition;
	}

	public DefaultModelObject newModelObject(final AbstractModel model) {
		final DefaultModelObject modelObject = new DefaultModelObject(model.newId(), this, this.updateDefinition.newModelHandler());
		modelObject.init(model);
		return modelObject;
	}

}
