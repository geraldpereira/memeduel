package fr.byob.game.memeduel.core.model.handler.damage;

import java.util.List;

import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition.DamageSlice;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;

public class LifespanDamageHandler extends AbstractModelHandler<B2DModelObject> implements DamageHandler {
	protected float lifespan;
	protected float initialLifespan;

	public LifespanDamageHandler() {
	}

	@Override
	public void init() {
		// Si la masse de l'objet est 0, il doit quand meme avoir une durée de
		// vie !
		this.lifespan = computeInitialLifespan(this.modelObject);
		this.initialLifespan = this.lifespan;
	}

	public static float computeInitialLifespan(final B2DModelObject modelObject) {
		final LifespanDefinition damageDefiniton = (LifespanDefinition) modelObject.getGOD().getDamageDefinition();
		return (float) (Math.log(1 + modelObject.getB2DBodyHandler().getBody().getMass()) * damageDefiniton.getLifespanFactor());
	}

	@Override
	public boolean damage(final float damageAmount) {
		// TODO Check if still needed ? Maybe for some special objects ?
		// if ((damageAmount / initialLifespan) < ((LifespanDefinition)
		// modelObject.getGOD().getDamageDefinition()).getMinDamagePercentage())
		// {
		// return false;
		// }
		final GameImage oldDamageImage = this.getDamageMDImage();
		this.lifespan -= damageAmount;
		final GameImage damageImage = this.getDamageMDImage();
		if (damageImage != oldDamageImage) {
			this.modelObject.invalidate();
			return true;
		}
		return false;
	};

	@Override
	public boolean isDestroyed() {
		return this.lifespan <= 0;
	};

	@Override
	public void destroy() {
		this.lifespan = -1;
	};

	@Override
	public boolean isDamaged() {
		return this.lifespan < this.initialLifespan;
	}

	@Override
	public boolean update(final float delta) {
		return false;
	};

	public float getLifespanPercentage() {
		return this.lifespan / this.initialLifespan * 100;
	};

	public void setLifespanPercentage(final float percentage) {
		this.lifespan = this.initialLifespan * percentage / 100;
	}

	@Override
	public GameImage getDamageMDImage() {
		final LifespanDefinition damageDefiniton = (LifespanDefinition) this.modelObject.getGOD().getDamageDefinition();
		final List<DamageSlice> damageSlices = damageDefiniton.getDamageSlices();
		final float lifespanPercentage = this.getLifespanPercentage();
		if (damageSlices != null) {
			for (final DamageSlice slice : damageSlices) {
				final GameImage image = slice.getImage(lifespanPercentage);
				if (image != null) {
					return image;
				}
			}
		}
		return null;
	}

	public float getInitialLifespan() {
		return this.initialLifespan;
	}

}
