package fr.byob.game.memeduel.core.gui.screen.load;

import playn.core.PlayN;
import tripleplay.game.Screen;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameImage;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.controller.GameMode;
import fr.byob.game.memeduel.core.controller.PlayLevelController;
import fr.byob.game.memeduel.core.editor.CannonBallGODProvider;
import fr.byob.game.memeduel.core.editor.PlayCannonBallGODProvider;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.screen.MessageScreen;
import fr.byob.game.memeduel.core.gui.screen.level.PlayLevelScreen;
import fr.byob.game.memeduel.domain.Level;

public class PlayLevelLoadingScreen extends LoadingScreen<AllGODLoader> {


	public PlayLevelLoadingScreen(final String groupID) {
		super(groupID);
	}

	@Override
	public void doLoad() {
		final MemeDuelGODLoader loader = new MemeDuelGODLoader();
		final Level level = GameContext.instance().getValue(MemeDuel.LEVEL);
		loader.loadFromGameModeAndLevelContent(this,GameMode.PLAY, level.getContent());
	}

	@Override
	public void onSuccess(final AllGODLoader result) {
		final CannonBallGODProvider cannonBallGODProvider = newCannonBallGODProvider(result);
		final PlayLevelController controller = new PlayLevelController((MemeDuelGODLoader) result, cannonBallGODProvider);
		final Screen screen = newNextScreen(controller);
		GameStack.instance().replace(screen);
	}

	protected CannonBallGODProvider newCannonBallGODProvider(final AllGODLoader result) {
		return new PlayCannonBallGODProvider(result, ((MemeDuelGODLoader) result).getLevelGOD().getAvailableCannonBalls());
	}

	protected Screen newNextScreen(final PlayLevelController controller) {
		return new PlayLevelScreen(getGroupID(), controller);
	}

	@Override
	public void onFailure(final Throwable cause) {
		PlayN.log().debug(cause.getMessage());
		GameStack.instance().replace(new MessageScreen("ERROR", cause.getMessage()));
	}

	@Override
	protected String text() {
		return "LOADING LEVEL ...";
	}

	@Override
	protected GameImage icon() {
		final String tipName = "TIP_PLAY_" + Math.round(1f * Math.random());
		return GameImage.valueOf(tipName);
	}

	@Override
	public boolean hasAnimatedBackground() {
		return false;
	}
}
