package fr.byob.game.memeduel.core.gui.screen.load;

import playn.core.PlayN;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.controller.EditLevelController;
import fr.byob.game.memeduel.core.controller.GameMode;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.screen.MessageScreen;
import fr.byob.game.memeduel.core.gui.screen.level.EditLevelScreen;
import fr.byob.game.memeduel.domain.Level;

public class EditLevelLoadingScreen extends LoadingScreen<AllGODLoader> {

	public EditLevelLoadingScreen(final String groupID) {
		super(groupID);
	}

	@Override
	public void doLoad() {
		final MemeDuelGODLoader loader = new MemeDuelGODLoader();
		loader.loadFromGameModeAndLevelContent(this, GameMode.EDIT, ((Level) GameContext.instance().getValue(MemeDuel.LEVEL)).getContent());
	}

	@Override
	public void onSuccess(final AllGODLoader result) {
		final EditLevelController controller = new EditLevelController((MemeDuelGODLoader) result);
		final EditLevelScreen editLevelScreen = new EditLevelScreen(getGroupID(), controller);
		controller.setGODProvider(editLevelScreen);
		GameStack.instance().replace(editLevelScreen);
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
	public boolean hasAnimatedBackground() {
		return false;
	}
}
