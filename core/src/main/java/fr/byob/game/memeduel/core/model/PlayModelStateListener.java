package fr.byob.game.memeduel.core.model;

import fr.byob.game.memeduel.core.model.PlayModel.GameState;


public interface PlayModelStateListener {

	void gameStateChanged(GameState gameState);

}
