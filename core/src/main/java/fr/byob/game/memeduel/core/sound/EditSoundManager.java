package fr.byob.game.memeduel.core.sound;

import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.MemeDuelUtils;
import fr.byob.game.memeduel.core.controller.SelectionsListener;
import fr.byob.game.memeduel.core.model.Border;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class EditSoundManager extends AbstractSoundManager implements SelectionsListener {

	@Override
	public void objectAdded(final ModelObject modelObject) {
		if (MemeDuelUtils.isObject(modelObject)) {
			MemeDuelLoader.OBJECT_ADDED.play();
		}
	}

	@Override
	public void objectRemoved(final ModelObject modelObject) {
		if (MemeDuelUtils.isObject(modelObject)) {
			MemeDuelLoader.OBJECTS_REMOVED.play();
		}
	}

	@Override
	public void objectDamaged(final ModelObject modelObject) {
	}

	@Override
	public void objectHitBorder(final ModelObject modelObject, final Border border, final Vector position) {
		if (modelObject != null && border == Border.GROUND) {
			this.playSound(modelObject, MemeDuelLoader.SAND_POUF);
		}
	}

	@Override
	public void selectionChanged(final boolean isEmpty, final boolean isLocked) {
	}

}
