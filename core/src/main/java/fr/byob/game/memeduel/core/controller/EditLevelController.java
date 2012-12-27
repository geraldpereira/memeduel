package fr.byob.game.memeduel.core.controller;

import java.util.List;

import playn.core.Mouse.WheelEvent;
import playn.core.Pointer.Event;
import fr.byob.game.memeduel.core.animation.EditAnimationManager;
import fr.byob.game.memeduel.core.editor.B2DGODProvider;
import fr.byob.game.memeduel.core.god.B2DGameObjectDefiniton;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.model.EditModel;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.sound.EditSoundManager;
import fr.byob.game.memeduel.core.view.MemeDuelView;

public class EditLevelController extends AbstractController {

	private SelectionsHandler selectionHandler;
	private B2DGODProvider<B2DGameObjectDefiniton> godProvider;

	public EditLevelController(final MemeDuelGODLoader godLoader) {
		super();
		init(godLoader);
	}

	protected void init(final MemeDuelGODLoader godLoader) {
		model = new EditModel();
		view = new MemeDuelView(model);
		soundManager = new EditSoundManager();
		animationManager = new EditAnimationManager(model, view);

		// Load the hole shit
		view.loadViewObjects(godLoader);
		model.loadModelObjects(godLoader);
		getView().modelLoaded();

		model.addListener(soundManager);
		model.addListener(animationManager);

		this.selectionHandler = new SelectionsHandler(model);
		selectionHandler.addListener((SelectionsListener) soundManager);

		super.init();
	}

	@Override
	public MemeDuelView getView() {
		return (MemeDuelView) super.getView();
	}

	@Override
	public EditModel getModel() {
		return (EditModel) super.getModel();
	}

	@Override
	public void onMouseWheelScroll(final WheelEvent event) {
		if (!this.selectionHandler.isObjectDragged()) {
			super.onMouseWheelScroll(event);
		}
	}

	@Override
	public void onPointerStart(final Event event) {
		super.onPointerStart(event);
		this.selectionHandler.handlePointerStart(this.pointerPosition);
	}

	@Override
	public void onPointerDrag(final Event event) {
		if (!this.selectionHandler.onPointerDrag()) {
			this.scrollView(event);
		}
		super.onPointerDrag(event);
	}

	@Override
	public void onPointerEnd(final Event event) {
		this.pointerPosition.setPosition(event);
		if (!this.hasScrolled) {
			final boolean wasSelected = this.selectionHandler.onPointerEnd();
			if (!this.selectionHandler.isEmpty() && !wasSelected) {
				// It was a simple click
				this.selectionHandler.unselectAll();
			} else if (!wasSelected) {
				final B2DGameObjectDefiniton b2dGameObjectDefiniton = godProvider.getGOD(this.pointerPosition.getModelPosition(), 0);
				this.model.addObject(b2dGameObjectDefiniton);
			}
		}

	}

	public void trashSelection() {
		if (this.selectionHandler.isEmpty()) {
			return;
		}
		final List<B2DModelObject> selection = this.selectionHandler.getSelection();
		for (final B2DModelObject modelObject : selection) {
			if (modelObject.isDamageable()) {
				modelObject.getDamageHandler().destroy();
			} else {
				this.selectionHandler.unselectObject(modelObject);
			}
		}
	}

	public void lock() {
		this.selectionHandler.lock();
	}

	public void unlock() {
		this.selectionHandler.lock();
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
		this.selectionHandler.update(this.pointerPosition);
	}

	public SelectionsHandler getSelectionHandler() {
		return this.selectionHandler;
	}

	public void setGODProvider(final B2DGODProvider<B2DGameObjectDefiniton> godProvider) {
		this.godProvider = godProvider;
	}

	public void onTouchCancel(final playn.core.Touch.Event[] touches) {

	}

	public void onPointerCancel(final Event event) {
	}

}
