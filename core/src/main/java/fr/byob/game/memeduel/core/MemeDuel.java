package fr.byob.game.memeduel.core;

import tripleplay.game.ScreenStack;
import fr.byob.game.box2d.Box2D;
import fr.byob.game.memeduel.core.gui.screen.load.ResourcesLoadingScreen;
import fr.byob.game.memeduel.core.net.Base64;
import fr.byob.game.memeduel.core.net.WebResource;
import fr.byob.game.memeduel.core.net.helper.LevelNetHelper;
import fr.byob.game.memeduel.core.net.helper.UserNetHelper;

public class MemeDuel extends AbstractGame {

	// Internal debug
	public final static boolean debug = false;
	// Box2D debug (Slows the FPS)
	public final static boolean showDebugDraw = false;
	// show the objects textures
	public final static boolean showTextures = true;

	public final static String USER_NET_HELPER = "USER_NET_HELPER";
	public final static String LEVEL_NET_HELPER = "LEVEL_NET_HELPER";
	public final static String USER = "USER";
	// Currently edited/tested/played level
	public final static String LEVEL = "LEVEL";
	// Duel Mode
	public final static String USER_1 = "USER_1";
	public final static String USER_2 = "USER_2";
	public final static String LEVEL_1 = "LEVEL_1";
	public final static String LEVEL_2 = "LEVEL_2";
	public final static String ANIMATED_BACKGROUND = "ANIMATED_BACKGROUND";

	public MemeDuel(final WebResource webResource, final Base64 base64, final Box2D box2d) {
		GameContext.instance().setWebResource(webResource);
		GameContext.instance().setBase64(base64);
		GameContext.instance().setBox2D(box2d);
		GameContext.instance().setValue(USER_NET_HELPER, new UserNetHelper());
		GameContext.instance().setValue(LEVEL_NET_HELPER, new LevelNetHelper());
	}

	@Override
	public void init() {
		// Initialize the GameStyles
		new GameStyles.Builder().build();

		//		final ParticlesScreen screen = new ParticlesScreen();
		//		GameContext.getInstance().push(screen, ScreenStack.NOOP);
		//		screen.showTransitionCompleted();

		// Create the final loading screen
		final ResourcesLoadingScreen loadingScreen = new
				ResourcesLoadingScreen();
		GameStack.instance().push(loadingScreen, ScreenStack.NOOP);
		loadingScreen.load();
	}

}
