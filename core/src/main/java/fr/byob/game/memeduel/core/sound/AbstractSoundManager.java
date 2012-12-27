package fr.byob.game.memeduel.core.sound;

import fr.byob.game.memeduel.core.GameSound;
import fr.byob.game.memeduel.core.model.ModelListener;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public abstract class AbstractSoundManager implements ModelListener {


	protected void playSound(final ModelObject modelObject, final GameSound gameSound) {
		gameSound.play();
	}

}
