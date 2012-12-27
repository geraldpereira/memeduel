package fr.byob.game.memeduel.core.model;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Vector;
import react.Slot;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.controller.PointerPosition;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.DynamicGameObjectDefinition;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGameOptionsGOD;
import fr.byob.game.memeduel.core.god.cannon.CannonBallDefinition;
import fr.byob.game.memeduel.core.god.cannon.CannonBallGOD;
import fr.byob.game.memeduel.core.god.cannon.CannonGOD;
import fr.byob.game.memeduel.core.god.damage.LifespanDefinition;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.core.model.cannon.CannonModelObject;
import fr.byob.game.memeduel.core.model.handler.damage.LifespanDamageHandler;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class PlayModel extends AbstractModel implements CannonListener {

	private final static float GAME_END_COUNTDOWN_DURATION = 5;// en secondes
	private final static float GAME_START_COUNTDOWN_DURATION = 2;// en
	// secondes

	private CannonModelObject cannon;
	private float gameEndCountDown = GAME_END_COUNTDOWN_DURATION;
	private float gameStartCountDown = GAME_START_COUNTDOWN_DURATION;
	private GameState gameState = GameState.STARTING;

	protected final List<PlayModelStateListener> stateListeners = new ArrayList<PlayModelStateListener>();

	protected int memeCounter = 0;

	private float score;
	private Slot<String> scoreSlot;

	public enum GameState {
		STARTING, RUNNING, ENDING, LOST, WON;
	}

	public PlayModel() {
	}

	@Override
	public void loadModelObjects(final AllGODLoader resource) {
		super.loadModelObjects(resource);


		final MemeDuelGODLoader godLoader = (MemeDuelGODLoader) resource;
		final MemeDuelGameOptionsGOD options = godLoader.getGameOptionsGOD();

		final CannonGOD cannonGOD = options.getCannonGOD();
		this.cannon = new CannonModelObject(this, cannonGOD);
		this.cannon.addListener(this);
	}

	@Override
	public ModelObject addObject(final DynamicGameObjectDefinition god) {
		final ModelObject modelObject = super.addObject(god);
		if (modelObject != null) {
			if (MemeDuelUtils.isMeme(modelObject)) {
				this.memeCounter++;
			}
		}
		return modelObject;
	}

	@Override
	protected ModelObject removeObject(final int index) {
		final ModelObject modelObject = super.removeObject(index);

		if (modelObject != null) {
			if (MemeDuelUtils.isMeme(modelObject)) {
				this.memeCounter--;
				this.updateScore(1000 * ((LifespanDamageHandler) modelObject.getDamageHandler()).getInitialLifespan());
			} else if (MemeDuelUtils.isObjectNotFragment(modelObject)) {
				this.updateScore(100 * ((LifespanDamageHandler) modelObject.getDamageHandler()).getInitialLifespan());
			}
			if (this.memeCounter <= 0) {
				// Pu de meme, on va finir la partie
				this.setGameState(GameState.ENDING);
				this.updateScore(5000 * this.cannon.getCannonBallGODProvider().getRemainingCannonBallCount());
			}
		}
		return modelObject;
	}


	@Override
	public void update(final PointerPosition pointerPosition, final float delta) {
		if (this.gameState == GameState.LOST || this.gameState == GameState.WON) {
			return;
		}
		if (this.gameState == GameState.STARTING) {
			gameStartCountDown -= 1 / delta;

			if (gameStartCountDown <= 0 || this.isSleeping()) {
				this.setGameState(GameState.RUNNING);
				this.world.setContactListener(this);
			}
		} else if (this.gameState == GameState.ENDING) {
			this.gameEndCountDown -= 1 / delta;

			if ((this.gameEndCountDown <= 0 || this.sleeping) && this.memeCounter <= 0) {
				// Plus de meme, C'est gagné !
				this.setGameState(GameState.WON);
				return;
			}
			if ((this.gameEndCountDown <= 0 || this.sleeping) && this.cannon.getCannonBallGODProvider().isEmpty()) {
				// Plus de boulet, C'est perdu !
				this.setGameState(GameState.LOST);
				return;
			}
		}

		super.update(pointerPosition, delta);

		this.cannon.update(pointerPosition, delta);
	};

	public CannonModelObject getCannon() {
		return this.cannon;
	}

	private void setGameState(final GameState gameState) {
		this.gameState = gameState;
		this.fireGameStateChanged(gameState);
	}

	public GameState getGameState() {
		return gameState;
	}

	protected void fireGameStateChanged(final GameState gameState) {
		final List<PlayModelStateListener> copy = new ArrayList<PlayModelStateListener>(this.stateListeners);
		for (final PlayModelStateListener listener : copy) {
			listener.gameStateChanged(gameState);
		}
	};

	public void addStateListener(final PlayModelStateListener listener) {
		this.stateListeners.add(listener);
	}

	public void removeStateListener(final PlayModelStateListener listener) {
		this.stateListeners.remove(listener);
	}

	@Override
	public void cannonBallDamaged() {
		if (this.cannon.getCannonBallGODProvider().isEmpty()) {
			this.setGameState(GameState.ENDING);
		}
	}

	@Override
	public void cannonBallFired(final Vector position, final float angle) {
	}

	@Override
	public void cannonBallMoved(final Vector position, final float angle) {
	}

	@Override
	public void cannonBallBonusActivated(final Vector position, final float angle) {
	}

	private void updateScore(final float value){
		final String oldScore = ""+(int)this.score;
		this.score += value;
		this.scoreSlot.onChange("" + (int) this.score, oldScore);
	}

	public void setScoreSlot(final Slot<String> slot) {
		this.scoreSlot = slot;
	}

	public float getScore() {
		return this.score;
	}

	@Override
	public MemeDuelGODLoader getGodLoader() {
		return (MemeDuelGODLoader) super.getGodLoader();
	}

	public int getRemainingTargets() {
		return memeCounter;
	}

	@Override
	protected void damageObject(final float damageAmount, final Body b2dBody, final Body b2dOtherBody) {
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


}
