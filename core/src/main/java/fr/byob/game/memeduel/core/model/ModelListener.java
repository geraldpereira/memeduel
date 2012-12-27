package fr.byob.game.memeduel.core.model;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public interface ModelListener {

	void objectAdded(ModelObject modelObject);

	void objectRemoved(ModelObject modelObject);

	void objectDamaged(ModelObject modelObject);

	void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position);

}
