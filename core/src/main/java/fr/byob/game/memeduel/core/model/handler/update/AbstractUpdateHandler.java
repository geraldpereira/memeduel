package fr.byob.game.memeduel.core.model.handler.update;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.model.handler.AbstractModelHandler;
import fr.byob.game.memeduel.core.model.object.DefaultModelObject;

public abstract class AbstractUpdateHandler<T extends DefaultModelObject> extends AbstractModelHandler<T> implements UpdateHandler {

	protected final Vector initialPosition = new Vector();
	private final Vector previousPosition = new Vector();
	private float previousAngle;
	protected final Vector currentPosition = new Vector();
	protected float currentAngle;

	@Override
	public void init(final AllGODLoader godLoader) {
		final Vector position = this.modelObject.getGOD().getPosition();
		final float angle = this.modelObject.getGOD().getAngle();

		this.previousPosition.set(position);
		this.previousAngle = angle;
		this.currentPosition.set(position);
		this.currentAngle = angle;
		this.initialPosition.set(position);
	}

	@Override
	public void update(final float delta) {
		this.previousPosition.set(this.currentPosition);
		this.previousAngle = this.currentAngle;
		// les implémentations DOIVENT durcharger cette méthode pour mettre à
		// jour la position et l'angle actuels
	}


	@Override
	public void getPreviousPositionToOut(final Vector out) {
		out.set(previousPosition);
	}

	@Override
	public float getPreviousRotation() {
		return this.previousAngle;
	}

	@Override
	public void getCurrentPositionToOut(final Vector out) {
		out.set(currentPosition);
	}

	@Override
	public float getCurrentRotation() {
		return this.currentAngle;
	}

	@Override
	public float getCurrentTransparency() {
		// La valeur par défaut est complètement opaque
		return 1.0f;
	}

}
