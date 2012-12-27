package fr.byob.game.memeduel.core.model;

import java.util.ArrayList;
import java.util.List;

import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.cannon.CannonBallDefinition;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.god.meme.MemeGOD;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class EditModel extends AbstractModel {

	private float maxLifespanSum;

	private float lifespanSum;

	private int memeCount = 0;

	protected final List<EditModelStateListener> stateListeners = new ArrayList<EditModelStateListener>();

	public EditModel() {
	}

	@Override
	public void loadModelObjects(final AllGODLoader resource) {
		final MemeDuelGODLoader godLoader = (MemeDuelGODLoader) resource;

		this.maxLifespanSum = godLoader.getLevelGOD().getMaxLifespanSum();

		super.loadModelObjects(resource);

		this.world.setContactListener(this);
	}

	public void addStateListener(final EditModelStateListener listener) {
		this.stateListeners.add(listener);
	}

	public void removeStateListener(final EditModelStateListener listener) {
		this.stateListeners.remove(listener);
	}

	public float getPercentage() {
		return Math.min(1f, this.lifespanSum / this.maxLifespanSum);
	}


	public void fireSleepingStateChanged(final boolean sleeping) {
		for (final EditModelStateListener listener : this.stateListeners) {
			listener.sleepingStateChanged(sleeping);
		}
	};

	public void firePercentageChanged(final float percentage) {
		for (final EditModelStateListener listener : this.stateListeners) {
			listener.percentageChanged(percentage);
		}
	};

	@Override
	public ModelObject addObject(final DynamicGameObjectDefinition god) {
		if (this.lifespanSum >= this.maxLifespanSum) {
			return null;
		}
		final ModelObject modelObject = super.addObject(god);

		if (modelObject != null && modelObject.isDamageable() && modelObject.getDamageHandler() instanceof LifespanDamageHandler) {
			this.lifespanSum += ((LifespanDamageHandler) modelObject.getDamageHandler()).getInitialLifespan();
			this.firePercentageChanged(this.getPercentage());
		}

		if (god instanceof MemeGOD) {
			memeCount++;
		}
		return modelObject;
	}

	@Override
	protected ModelObject removeObject(final int index) {
		final ModelObject modelObject = super.removeObject(index);
		if (modelObject != null && modelObject.isDamageable() && modelObject.getDamageHandler() instanceof LifespanDamageHandler) {
			this.lifespanSum -= ((LifespanDamageHandler) modelObject.getDamageHandler()).getInitialLifespan();
			this.firePercentageChanged(this.getPercentage());
		}

		if (MemeDuelUtils.isMeme(modelObject)) {
			memeCount--;
			fireSleepingStateChanged(sleeping);
		}

		return modelObject;
	}

	@Override
	public void update(final PointerPosition pointerPosition, final float delta) {
		final boolean sleeping = this.sleeping;

		super.update(pointerPosition, delta);

		if (sleeping != this.sleeping) {
			// The sleeping state of the world as just changed
			this.fireSleepingStateChanged(this.sleeping);
		}

	};

	@Override
	public MemeDuelGODLoader getGodLoader() {
		return (MemeDuelGODLoader) super.getGodLoader();
	}

	public int getMemeCount() {
		return memeCount;
	}

	@Override
	protected void damageObject(final float damageAmount, final Body b2dBody, final Body b2dOtherBody) {
	}

	protected void damageObject2(final float damageAmount, final Body b2dBody, final Body b2dOtherBody) {
		float damage = damageAmount;
		if (b2dBody != null) {
			final Object userData = b2dBody.getUserData();
			if (userData instanceof ModelObject) { // Si l'objet
				// Si un objet touche un mur ou le ciel, on le detruit
				final B2DModelObject modelObject = (B2DModelObject) userData;
				if (modelObject.isDamageable() && modelObject.getDamageHandler() instanceof LifespanDamageHandler) {
					final LifespanDamageHandler thisDamageHandler = (LifespanDamageHandler) modelObject.getDamageHandler();

					final Object otherUserData = b2dOtherBody.getUserData();
					if (otherUserData instanceof Border && otherUserData != Border.GROUND) {
						// The sides of the world DESTROYS every objects
						damage = thisDamageHandler.getInitialLifespan();
					} else if (otherUserData instanceof B2DModelObject) {
						final B2DModelObject otherModelObject = (B2DModelObject) otherUserData;
						if (!otherModelObject.getTypeId().equals(modelObject.getTypeId()) && otherModelObject.isDamageable() && otherModelObject.getGOD().getDamageDefinition() instanceof LifespanDefinition) {
							damage *= ((LifespanDefinition) otherModelObject.getGOD().getDamageDefinition()).getOtherDamageFactor();
							if (MemeDuelUtils.isCannonBall(otherModelObject)){
								final CannonBallDefinition cnDef = ((CannonBallGOD)otherModelObject.getGOD()).getCannonBallDefinition();
								// Apply a minimum damage by percentage on first
								// hit (objet not damaged)
								if (!otherModelObject.getDamageHandler().isDamaged()) {
									damage = Math.max(thisDamageHandler.getInitialLifespan() * cnDef.getMinOtherDamagePercentage(), damage);
								}
							}
						}
					}
					if (modelObject.getDamageHandler().damage(damage)) {
						// this.updateScore(damage);
						this.fireObjectDamaged(modelObject);
					}
				}
			}
		}
	};

	public float getMaxLifespanSum() {
		return maxLifespanSum;
	}

}
