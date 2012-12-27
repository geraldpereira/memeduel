package fr.byob.game.memeduel.core.controller;

import playn.core.Json.Writer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.Touch;
import pythagoras.f.Vector;
import fr.byob.game.box2d.Disposable;
import fr.byob.game.memeduel.core.GameLoop;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.Updatable;
import fr.byob.game.memeduel.core.animation.AbstractAnimationManager;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelLevelGOD;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.sound.AbstractSoundManager;
import fr.byob.game.memeduel.core.view.AbstractView;

public abstract class AbstractController implements Updatable, Mouse.Listener, Touch.Listener, Pointer.Listener, Keyboard.Listener, Disposable {
	protected AbstractView view;
	protected AbstractModel model;
	protected AbstractAnimationManager animationManager;
	protected AbstractSoundManager soundManager;

	protected boolean hasScrolled = false;
	protected PointerPosition pointerPosition = null;
	protected Vector lastOffset = null;

	public AbstractController() {
		super();
	}

	protected void init() {
		this.pointerPosition = new PointerPosition(view);
	}

	@Override
	public void update(final float delta) {
		model.update(pointerPosition, delta);
		view.update(pointerPosition, delta);
	}

	public AbstractModel getModel() {
		return this.model;
	}

	public AbstractView getView() {
		return this.view;
	}

	public PointerPosition getPointerPosition() {
		return this.pointerPosition;
	}

	@Override
	public void onMouseWheelScroll(final WheelEvent event) {
		final float velocity = event.velocity();

		float drawScale = view.getCurrentDrawScale();
		drawScale -= velocity / 10;
		final AllGODLoader loader = model.getGodLoader();
		if (drawScale > loader.getGameGOD().getMaxDrawScale()) {
			drawScale = loader.getGameGOD().getMaxDrawScale();
		} else if (drawScale < loader.getGameGOD().getMinDrawScale()) {
			drawScale = loader.getGameGOD().getMinDrawScale();
		}
		view.focusOn(null, 5, drawScale, 5);
	}

	@Override
	public void onPointerStart(final Event event) {
		this.hasScrolled = false;
		this.pointerPosition.setPosition(event);
	}

	@Override
	public void onPointerDrag(final Event event) {
		this.pointerPosition.setPosition(event);
	}

	protected void scrollView(final Event event) {
		final Vector offset = this.pointerPosition.computeOffset(event);
		if (offset.x != 0 || offset.y != 0) {
			offset.x = -offset.x;
			offset.y = -offset.y;
			this.hasScrolled = true;
			view.updateScroll(offset);
		}
	}

	@Override
	public void onPointerEnd(final Event event) {
		this.pointerPosition.setPosition(event);
	}

	@Override
	public void onMouseDown(final ButtonEvent event) {
	}

	@Override
	public void onMouseUp(final ButtonEvent event) {
	}

	@Override
	public void onMouseMove(final MotionEvent event) {
	}

	@Override
	public void onKeyDown(final playn.core.Keyboard.Event event) {
	}

	@Override
	public void onKeyTyped(final TypedEvent event) {
	}

	@Override
	public void onKeyUp(final playn.core.Keyboard.Event event) {
	}

	float oldDist;

	@Override
	public void onTouchStart(final playn.core.Touch.Event[] touches) {
		if (touches.length == 2) {
			final Vector p1 = GamePool.instance().popVector().set(touches[0].x(), touches[0].y());
			final Vector p2 = GamePool.instance().popVector().set(touches[1].x(), touches[1].y());
			this.oldDist = p1.distance(p2);
			GamePool.instance().pushVector(2);
		}
	}

	@Override
	public void onTouchMove(final playn.core.Touch.Event[] touches) {
		if (touches.length == 2) {

			final Vector p1 = GamePool.instance().popVector().set(touches[0].x(), touches[0].y());
			final Vector p2 = GamePool.instance().popVector().set(touches[1].x(), touches[1].y());
			final float newDist = p1.distance(p2);
			GamePool.instance().pushVector(2);

			float drawScale = view.getCurrentDrawScale();
			drawScale = drawScale * (newDist / this.oldDist) / 10;
			final AllGODLoader loader = model.getGodLoader();
			if (drawScale > loader.getGameGOD().getMaxDrawScale()) {
				drawScale = loader.getGameGOD().getMaxDrawScale();
			} else if (drawScale < loader.getGameGOD().getMinDrawScale()) {
				drawScale = loader.getGameGOD().getMinDrawScale();
			}
			view.focusOn(null, 5, drawScale, 5);
		}
	}

	@Override
	public void onTouchEnd(final playn.core.Touch.Event[] touches) {
	}

	public void play() {
		GameLoop.instance().register(this);
		GameLoop.instance().register(view);
		GameLoop.instance().register(animationManager);
	}

	public void pause() {
		GameLoop.instance().deregister(this);
		GameLoop.instance().deregister(view);
		GameLoop.instance().deregister(animationManager);
	}

	public String getLevelContent() {
		final Writer writer = PlayN.json().newWriter();
		final MemeDuelLevelGOD level = new MemeDuelLevelGOD(null);
		if (level.saveFomModel(this.model, writer)) {
			final String levelJSON = writer.write();
			PlayN.log().info("test level " + levelJSON);
			return levelJSON;
		} else {
			// TODO We should display a warn message ?
			PlayN.log().error("test level => NOT SLEEPING");
			return null;
		}
	}

	@Override
	public void dispose() {
		view.dispose();
		model.dispose();
	}
}
