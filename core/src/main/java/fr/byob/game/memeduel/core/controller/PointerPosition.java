package fr.byob.game.memeduel.core.controller;

import playn.core.Pointer.Event;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.view.AbstractView;

public class PointerPosition {

	private final Vector viewPosition = new Vector();
	private final Vector modelPosition = new Vector();
	private final Vector scrollModelPosition = new Vector();
	private final Vector offset = new Vector();
	private final AbstractView view;

	public PointerPosition(final AbstractView view) {
		this.view = view;
	}

	public void setPosition(final Event event) {

		this.viewPosition.x = event.x();
		this.viewPosition.y = event.y();

		this.modelPosition.x = this.view.toModel(this.viewPosition.x);
		this.modelPosition.y = this.view.toModel(this.viewPosition.y);
	}

	public Vector computeOffset(final Event event) {
		offset.set(this.view.toModel(event.x()) - this.modelPosition.x, this.view.toModel(event.y()) - this.modelPosition.y);
		return offset;
	};

	public Vector getModelPosition() {
		scrollModelPosition.set(this.view.getCurrentScroll().x + this.modelPosition.x, this.view.getCurrentScroll().y + this.modelPosition.y);
		return scrollModelPosition;
	}

	public Vector getViewPosition() {
		return this.viewPosition;
	}

}
