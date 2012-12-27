package fr.byob.game.memeduel.core.controller;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.dynamics.Body;
import fr.byob.game.box2d.dynamics.BodyType;
import fr.byob.game.box2d.dynamics.joints.DistanceJoint;
import fr.byob.game.box2d.dynamics.joints.DistanceJointDef;
import fr.byob.game.box2d.dynamics.joints.JointEdge;
import fr.byob.game.box2d.dynamics.joints.MouseJoint;
import fr.byob.game.box2d.dynamics.joints.MouseJointDef;
import fr.byob.game.memeduel.core.GamePool;
import fr.byob.game.memeduel.core.model.AbstractModel;
import fr.byob.game.memeduel.core.model.Border;
import fr.byob.game.memeduel.core.model.ModelListener;
import fr.byob.game.memeduel.core.model.object.B2DModelObject;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class SelectionsHandler implements ModelListener {

	// When an object is selected, it is attached to the world background
	private final static String JOINT_ID = "SelectionJoint";

	private final AbstractModel model;
	private B2DModelObject mainSelection;
	private final List<SelectionsListener> listeners;

	// Used to handle object selection
	private MouseJoint mouseJoint;
	private boolean unselectObject;
	// true if the user dragged the objet
	private boolean wasDragged = false;
	// The previous state of the currently clicked object
	private SelectionType previousSelectionType = null;

	private boolean locked = false;

	public SelectionsHandler(final AbstractModel model) {
		this.listeners = new ArrayList<SelectionsListener>();
		// When an object is removed, we must unselect it
		model.addListener(this);
		this.model = model;
	}

	public void addListener(final SelectionsListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(final SelectionsListener listener) {
		this.listeners.remove(listener);
	}

	void fireSelectionChanged() {
		final boolean isEmpty = this.isEmpty();
		final boolean locked = this.isLocked();
		for (final SelectionsListener listener : this.listeners) {
			listener.selectionChanged(isEmpty, locked);
		}
	};

	public void handlePointerStart(final PointerPosition pointerPosition) {
		final B2DModelObject modelObject = this.model.getModelObjectAtMouse(pointerPosition);
		if (modelObject == null || !modelObject.isSelectable()) {
			return;
		}

		this.previousSelectionType = modelObject.getSelectionHandler().getSelectionType();
		if (this.previousSelectionType != SelectionType.MAIN_SELECTION) {
			// si mon objet courant n'est pas la selection principale, alors il
			// va le devenir
			// Il faut virer l'ancienne selection principale
			final B2DModelObject prevMainSelected = this.getMainSelection();
			if (prevMainSelected != null) {
				prevMainSelected.getSelectionHandler().setSelectionType(SelectionType.SELECTED);
			}
		}
		this.setMainSelection(modelObject);
		this.dragObject(pointerPosition, modelObject);
		this.fireSelectionChanged();
	}

	public boolean onPointerDrag() {
		final boolean isObjectDragged = this.isObjectDragged();
		if (isObjectDragged) {
			this.wasDragged = true;
		}
		return isObjectDragged;
	}

	public boolean onPointerEnd() {
		boolean wasSelected = false;

		if (this.undragObject()) {
			final B2DModelObject modelObject = this.getMainSelection();
			if (modelObject != null) {
				if (!this.wasDragged && this.previousSelectionType == SelectionType.MAIN_SELECTION) {
					// It was already the main selection
					// The user simply clicked on it
					// We must unselect it
					this.unselectObject(modelObject);
				} else {
					this.attachObject(modelObject);
				}
				wasSelected = true;
			}
		}
		this.fireSelectionChanged();
		this.previousSelectionType = null;
		this.wasDragged = false;
		return wasSelected;
	}

	private void attachObject(final B2DModelObject modelObject) {
		if (modelObject.getGOD().getB2DBodyDefition().getBodyType() != BodyType.DYNAMIC) {
			// Pas besoin pour les objets non dynamiques !
			return;
		}
		final Body body = modelObject.getB2DBodyHandler().getBody();
		// body.setType(BodyType.DYNAMIC);
		body.setAngularVelocity(0);
		body.setLinearVelocity(GamePool.ZERO_VECTOR);
		body.setFixedRotation(true);
		final DistanceJointDef attatchToGroundJointDef = new DistanceJointDef();
		attatchToGroundJointDef.initialize(body, this.model.getGroundBody(), body.getWorldCenter(), body.getWorldCenter());
		attatchToGroundJointDef.length = 0;
		attatchToGroundJointDef.frequencyHz = 1.0f;
		attatchToGroundJointDef.dampingRatio = 0.1f;
		final DistanceJoint attatchToGroundJoint = (DistanceJoint) this.model.getB2World().createJoint(attatchToGroundJointDef);
		attatchToGroundJoint.setUserData(JOINT_ID);
	}

	private void releaseObject(final B2DModelObject modelObject) {
		if (modelObject.getGOD().getB2DBodyDefition().getBodyType() != BodyType.DYNAMIC) {
			// Pas besoin pour les objets non dynamiques !
			return;
		}

		final Body body = modelObject.getB2DBodyHandler().getBody();
		if (body == null) {
			// The body is null when the modelObject has just been removed
			return;
		}
		body.setFixedRotation(modelObject.getGOD().getB2DBodyDefition().isFixedRotation());

		JointEdge joint = null;
		for (final JointEdge curJoint : body.getJointList()) {
			if (curJoint.joint.getUserData() == JOINT_ID) {
				joint = curJoint;
				break;
			}
		}

		if (joint != null) {
			this.model.getB2World().destroyJoint(joint.joint);
		}
	}

	public void unselectObject(final B2DModelObject modelObject) {
		if (!modelObject.isSelectable() || modelObject.getSelectionHandler().getSelectionType() == SelectionType.NONE) {
			return;
		}
		modelObject.getSelectionHandler().setSelectionType(SelectionType.NONE);
		this.releaseObject(modelObject);

		B2DModelObject nextMainSelected = null;
		for (final Body curB2Body : this.model.getB2World().getBodyList()) {
			final Object userData = curB2Body.getUserData();
			if (userData != null && userData instanceof B2DModelObject) {
				final B2DModelObject currentModelObject = (B2DModelObject) userData;
				if (currentModelObject.isSelectable() && currentModelObject.getSelectionHandler().getSelectionType() == SelectionType.SELECTED) {
					nextMainSelected = currentModelObject;
					break;
				}
			}
		}

		this.setMainSelection(nextMainSelected);
		this.fireSelectionChanged();
	}

	public void unselectAll() {
		for (final Body curB2Body : this.model.getB2World().getBodyList()) {
			final Object userData = curB2Body.getUserData();
			if (userData != null && userData instanceof B2DModelObject) {
				final B2DModelObject modelObject = (B2DModelObject) userData;
				if (modelObject.isSelectable()) {
					final SelectionType type = modelObject.getSelectionHandler().getSelectionType();
					if (type == SelectionType.MAIN_SELECTION || type == SelectionType.SELECTED) {
						modelObject.getSelectionHandler().setSelectionType(SelectionType.NONE);
						this.releaseObject(modelObject);
					}
				}
			}
		}
		this.setMainSelection(null);
		this.fireSelectionChanged();
	}

	private void setMainSelection(final B2DModelObject modelObject) {
		if (modelObject != null && modelObject.isSelectable()) {
			modelObject.getSelectionHandler().setSelectionType(SelectionType.MAIN_SELECTION);
		}
		this.mainSelection = modelObject;
	}

	public B2DModelObject getMainSelection() {
		return this.mainSelection;
	}

	public List<B2DModelObject> getSelection() {
		final List<B2DModelObject> selection = new ArrayList<B2DModelObject>();
		for (final Body curB2Body : this.model.getB2World().getBodyList()) {
			final Object userData = curB2Body.getUserData();
			if (userData != null && userData instanceof B2DModelObject) {
				final B2DModelObject modelObject = (B2DModelObject) userData;
				if (modelObject.isSelectable()) {
					final SelectionType type = modelObject.getSelectionHandler().getSelectionType();
					if (type == SelectionType.MAIN_SELECTION || type == SelectionType.SELECTED) {
						selection.add(modelObject);
					}
				}
			}
		}
		return selection;
	}

	public void lock() {
		for (final Body curB2Body : this.model.getB2World().getBodyList()) {
			// Uniquement les objets qui ne bougent pas
			if (!curB2Body.isAwake()) {
				final Object userData = curB2Body.getUserData();
				if (userData != null && userData instanceof B2DModelObject) {
					final B2DModelObject modelObject = (B2DModelObject) userData;
					final SelectionType type = modelObject.getSelectionHandler().getSelectionType();
					// Uniquement les objets non selectionnés dynamiques
					if (modelObject.getGOD().getB2DBodyDefition().getBodyType() == BodyType.DYNAMIC
							&& modelObject.isSelectable()
							&& type == SelectionType.NONE) {
						modelObject.getSelectionHandler().setSelectionType(SelectionType.LOCKED);
						this.attachObject(modelObject);
					}
				}
			}
		}
		this.locked = true;
		this.fireSelectionChanged();
	}

	public void unlock() {
		for (final Body curB2Body : this.model.getB2World().getBodyList()) {
			// Uniquement les objets qui ne bougent pas
			final Object userData = curB2Body.getUserData();
			if (userData != null && userData instanceof B2DModelObject) {
				final B2DModelObject modelObject = (B2DModelObject) userData;
				final SelectionType type = modelObject.getSelectionHandler().getSelectionType();
				// Uniquement les objets non selectionnés
				if (modelObject.isSelectable() && type == SelectionType.LOCKED) {
					modelObject.getSelectionHandler().setSelectionType(SelectionType.NONE);
					this.releaseObject(modelObject);
				}
			}
		}
		this.locked = false;
		this.fireSelectionChanged();
	}

	public boolean isLocked() {
		return this.locked;
	}

	public boolean isEmpty() {
		return this.mainSelection == null;
	}

	public void clear() {
		this.unselectAll();
	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		if (modelObject instanceof B2DModelObject) {
			this.unselectObject((B2DModelObject) modelObject);
		}
	}

	@Override
	public void objectAdded(final ModelObject modelObject) {
	}

	@Override
	public void objectDamaged(final ModelObject modelObject) {
	}

	@Override
	public void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
	}

	/**
	 * Met à jour l'objet sélectionné : - la position du joint si toujours
	 * sélectionné, - supprime le MouseJoint s'il vient d'être déselectionné
	 */
	public void update(final PointerPosition pointerPosition) {
		if (this.isObjectDragged()) {
			if (this.unselectObject) {
				final Body body = this.mouseJoint.getBodyB();
				final B2DModelObject modelObject = (B2DModelObject) body.getUserData();
				final BodyType type = modelObject.getGOD().getB2DBodyDefition().getBodyType();
				body.setType(type);
				body.getFixtureList().iterator().next().setDensity(modelObject.getGOD().getB2DBodyDefition().getDensity());
				body.resetMassData();

				body.setAwake(true);
				// this.prevBodyMass = 0;
				// this.prevBodyInvMass = 0;

				this.model.getB2World().destroyJoint(this.mouseJoint);
				this.mouseJoint = null;
				this.unselectObject = false;
			} else {
				this.mouseJoint.setTarget(pointerPosition.getModelPosition());
			}
		}
	}

	private void dragObject(final PointerPosition pointerPosition, final B2DModelObject modelObject) {
		if (this.mouseJoint == null) {
			final Body selectedBody = modelObject.getB2DBodyHandler().getBody();
			// Récupérer l'objet sélectionné
			if (selectedBody != null) {
				selectedBody.setType(BodyType.DYNAMIC);
				selectedBody.getFixtureList().iterator().next().setDensity(0.01f);
				selectedBody.resetMassData();

				this.releaseObject(modelObject);

				final float distance = pointerPosition.getModelPosition().distance(selectedBody.getWorldCenter());
				final Dimension size = modelObject.getGOD().getShapeDefition().getSize();
				final float maxFixedRotationDistance = (size.width + size.height) / 6;

				selectedBody.setFixedRotation(distance < maxFixedRotationDistance);

				// Créer le b2dMouseJoint permettant de déplacer l'objet
				// sélectionner
				final MouseJointDef b2dMouseJointDef = new MouseJointDef();
				b2dMouseJointDef.bodyA = this.model.getGroundBody();// this.world.getGroundBody();
				b2dMouseJointDef.bodyB = selectedBody;
				b2dMouseJointDef.target.set(pointerPosition.getModelPosition());
				b2dMouseJointDef.maxForce = 500.0f * selectedBody.getMass();
				this.mouseJoint = (MouseJoint) this.model.getB2World().createJoint(b2dMouseJointDef);
				selectedBody.setAwake(true);
			} else {
				this.mouseJoint = null;
			}
		}
	}

	/**
	 * Returns true if an object is selected
	 * 
	 * @return
	 */
	public boolean isObjectDragged() {
		return this.mouseJoint != null;
	}

	/**
	 * Stops the dragging of the selected object. Warn : sets only a flag that
	 * tells the engine to effectivelly unselect the object during next update
	 * 
	 * @return true if an object was selected
	 */
	private boolean undragObject() {
		final boolean wasSelected = this.isObjectDragged();
		if (wasSelected) {
			this.unselectObject = true;
		}
		return wasSelected;
	}

}
