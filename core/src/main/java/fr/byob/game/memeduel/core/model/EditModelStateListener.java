package fr.byob.game.memeduel.core.model;


public interface EditModelStateListener {

	void sleepingStateChanged(boolean sleeping);

	void percentageChanged(float percentage);

}
