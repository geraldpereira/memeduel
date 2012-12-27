package fr.byob.game.memeduel.core.controller;

import playn.core.PlayN;
import playn.core.Pointer.Event;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.ViewUtils;
import fr.byob.game.memeduel.core.animation.PlayAnimationManager;
import fr.byob.game.memeduel.core.editor.CannonBallGODProvider;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGameOptionsGOD;
import fr.byob.game.memeduel.core.god.WorldGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.PlayModel;
import fr.byob.game.memeduel.core.model.PlayModel.GameState;
import fr.byob.game.memeduel.core.model.cannon.CannonBallModelObject;
import fr.byob.game.memeduel.core.model.cannon.CannonListener;
import fr.byob.game.memeduel.core.model.cannon.CannonModelObject;
import fr.byob.game.memeduel.core.sound.TestSoundManager;
import fr.byob.game.memeduel.core.view.MemeDuelView;

public class PlayLevelController extends AbstractController implements CannonListener {


	private enum Focus {
		CANNON(5, 0.8f) {
			@Override
			public void newFocusPointToOut(final AbstractModel model, final Vector out) {
				final WorldGOD game = model.getGodLoader().getGameGOD().getWorld();
				final MemeDuelGameOptionsGOD optionsGod = (MemeDuelGameOptionsGOD) model.getGodLoader().getGameOptionsGOD();
				out.set(optionsGod.getCannonGOD().getXPosition(), game.getHeight());
			}
		},
		MIDDLE(3, 0.4f) {
			@Override
			public void newFocusPointToOut(final AbstractModel model, final Vector out) {
				final WorldGOD game = model.getGodLoader().getGameGOD().getWorld();
				// - 2 pour voir le cannon
				out.set(game.getWidth() / 2 - 5.5f, game.getHeight());
			}
		},
		CASTLE(3, 0.6f) {
			@Override
			public void newFocusPointToOut(final AbstractModel model, final Vector out) {
				final WorldGOD game = model.getGodLoader().getGameGOD().getWorld();
				final MemeDuelGameOptionsGOD optionsGod = (MemeDuelGameOptionsGOD) model.getGodLoader().getGameOptionsGOD();
				final float windowModelWidth = ViewUtils.toInitModel(PlayN.graphics().width());

				out.set(optionsGod.getXOffset() + windowModelWidth, game.getHeight());
			}
		};

		private final int invSpeed;
		private final float drawScale;
		private Vector focusPoint;

		private Focus(final int invSpeed, final float drawScale) {
			this.invSpeed = invSpeed;
			this.drawScale = drawScale;
		}

		public Focus getNext() {
			int index = this.ordinal() + 1;
			if (index >= values().length) {
				index = 0;
			}
			return values()[index];
		}

		public abstract void newFocusPointToOut(final AbstractModel model, final Vector out);

		public void focusOn(final AbstractController controller) {
			if (this.focusPoint == null) {
				this.focusPoint = new Vector();
				this.newFocusPointToOut(controller.getModel(), focusPoint);
			}
			controller.getView().focusOn(this.focusPoint, this.invSpeed, this.drawScale, this.invSpeed);
		}
	}

	private final static float DEFAULT_FOCUS_SWITCH_DELAY = 1; // en secondes

	private float focusSwitchDelay = 0;

	private Focus focus;

	public PlayLevelController(final MemeDuelGODLoader godLoader, final CannonBallGODProvider cannonBallGODProvider) {
		super();
		init(godLoader, cannonBallGODProvider);
	}

	protected void init(final MemeDuelGODLoader godLoader, final CannonBallGODProvider cannonBallGODProvider) {
		model = new PlayModel();
		view = new MemeDuelView(model);
		soundManager = new TestSoundManager();
		animationManager = new PlayAnimationManager(model, view);

		// Load the hole shit
		view.loadViewObjects(godLoader);
		model.loadModelObjects(godLoader);
		view.modelLoaded();

		model.addListener(soundManager);
		model.addListener(animationManager);

		final CannonModelObject cannon = getCannon();
		cannon.addListener(this);
		cannon.addListener((MemeDuelView) view);
		cannon.addListener((CannonListener) soundManager);
		cannon.addListener((CannonListener) animationManager);
		cannon.setCannonBallGODProvider(cannonBallGODProvider);
		super.init();
		setFocus(Focus.CASTLE);
	}

	@Override
	public PlayModel getModel() {
		return (PlayModel) super.getModel();
	}

	public CannonModelObject getCannon() {
		return getModel().getCannon();
	}

	private void setFocus(final Focus focus) {
		this.focus = focus;
		focus.focusOn(this);
	}


	@Override
	public void onPointerStart(final Event event) {
		super.onPointerStart(event);
		if (getModel().getGameState() == GameState.RUNNING) {
			getModel().getCannon().load(this.pointerPosition);
		}
	}

	@Override
	public void onPointerDrag(final Event event) {
		final CannonModelObject cannon = getCannon();
		if (!cannon.isLoaded() && !cannon.isFiring()) {
			this.scrollView(event);
		}
		super.onPointerDrag(event);
	}


	@Override
	public void onPointerEnd(final Event event) {
		final CannonModelObject cannon = getCannon();
		this.pointerPosition.setPosition(event);
		if (cannon.isLoaded()) {
			cannon.fire(this.pointerPosition);
		} else if (!this.hasScrolled && !cannon.isFiring()) {
			if (this.focusSwitchDelay <= 0) {
				this.setFocus(this.focus.getNext());
			}
		} else if (!this.hasScrolled && cannon.isFiring()) {
			cannon.applyClickBonus(this.pointerPosition.getModelPosition());
		}
	}


	@Override
	public void update(final float delta) {
		super.update(delta);
		final CannonModelObject cannon = getCannon();
		if (this.focusSwitchDelay > 0) {
			this.focusSwitchDelay -= 1 / delta;
		}

		if (cannon.isFiring()) {
			final CannonBallModelObject cannonBall = cannon.getCurrentCannonBall();

			final Vector position = GamePool.instance().popVector();
			cannonBall.getUpdateHandler().getCurrentPositionToOut(position);
			// Pour voir a droite du boulet ca aide à viser
			position.x += 5;
			getView().focusOn(position, 10, -1, 5);
			GamePool.instance().pushVector(1);
		}
	}


	@Override
	public void cannonBallDamaged() {
		this.setFocus(Focus.CASTLE);
		this.focusSwitchDelay = DEFAULT_FOCUS_SWITCH_DELAY;
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

	public void onTouchCancel(final playn.core.Touch.Event[] touches) {
	}

	public void onPointerCancel(final Event event) {
	}

}
