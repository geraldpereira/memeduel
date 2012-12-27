package fr.byob.game.memeduel.core.gui.screen.load;

import tripleplay.game.Screen;
import fr.byob.game.memeduel.core.controller.PlayLevelController;
import fr.byob.game.memeduel.core.editor.CannonBallGODProvider;
import fr.byob.game.memeduel.core.editor.TestCannonBallGODProvider;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.gui.screen.level.CannonBall;
import fr.byob.game.memeduel.core.gui.screen.level.TestLevelScreen;

public class TestLevelLoadingScreen extends PlayLevelLoadingScreen {


	public TestLevelLoadingScreen(final String groupID) {
		super(groupID);
	}

	@Override
	protected CannonBallGODProvider newCannonBallGODProvider(final AllGODLoader result) {
		return new TestCannonBallGODProvider(result, CannonBall.CANNON_BALL_SMALL.name());
	}

	@Override
	protected Screen newNextScreen(final PlayLevelController controller) {
		return new TestLevelScreen(getGroupID(), controller);
	}

	@Override
	public boolean hasAnimatedBackground() {
		return false;
	}

}
