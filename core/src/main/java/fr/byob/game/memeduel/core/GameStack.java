package fr.byob.game.memeduel.core;

import playn.core.PlayN;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import fr.byob.game.memeduel.core.gui.screen.AbstractScreen;
import fr.byob.game.memeduel.core.gui.screen.AnimatedBackground;

public class GameStack extends ScreenStack implements Updatable, Paintable {

	private final static GameStack instance = new GameStack();

	private GameStack() {
		GameLoop.instance().register((Paintable) this);
		GameLoop.instance().register((Updatable) this);
	}

	public static GameStack instance() {
		return instance;
	}

	@Override
	protected void handleError(final RuntimeException error) {
		PlayN.log().warn("Screen failure", error);
	}

	@Override
	protected Transition defaultPushTransition() {
		return this.slide();
	}

	@Override
	protected Transition defaultPopTransition() {
		return this.slide().right();
	}

	public void removeGroup(final String groupID) {
		final AbstractScreen oldScreen = (AbstractScreen) top();
		PlayN.log().debug("Remove group " + groupID);
		remove(new GroupIDPredicate(groupID));
		topChanged(oldScreen, (AbstractScreen) top());
	}

	// ----> ScreenStack
	@Override
	public void replace(final Screen screen, final Transition trans) {
		final AbstractScreen oldScreen = (AbstractScreen) top();
		PlayN.log().debug("Replace " + screen);
		super.replace(screen, trans);
		topChanged(oldScreen, (AbstractScreen) top());
	}

	@Override
	public void push(final Screen screen, final Transition trans) {
		final AbstractScreen oldScreen = (AbstractScreen) top();
		PlayN.log().debug("Push " + screen);
		super.push(screen, trans);
		topChanged(oldScreen, (AbstractScreen) top());
	}

	@Override
	public void push(final Iterable<? extends Screen> screens, final Transition trans) {
		final AbstractScreen oldScreen = (AbstractScreen) top();
		PlayN.log().debug("Push " + screens);
		super.push(screens, trans);
		topChanged(oldScreen, (AbstractScreen) top());
	}

	@Override
	public boolean remove(final Screen screen, final Transition trans) {
		final AbstractScreen oldScreen = (AbstractScreen) top();
		PlayN.log().debug("Remove " + screen);
		final boolean returnVal = super.remove(screen, trans);
		topChanged(oldScreen, (AbstractScreen) top());
		return returnVal;
	}

	private void topChanged(final AbstractScreen oldScreen, final AbstractScreen newScreen) {
		final AnimatedBackground background = GameContext.instance().getValue(MemeDuel.ANIMATED_BACKGROUND);
		if (background != null) {
			if (newScreen.hasAnimatedBackground()) {
				background.start();
			} else {
				background.stop();
			}
		}
		if (oldScreen != null && oldScreen.getMusic() != newScreen.getMusic()) {
			if (oldScreen.getMusic() != null) {
				oldScreen.getMusic().stop();
			}
			if (newScreen.getMusic() != null) {
				newScreen.getMusic().play();
			}
		}
	}

	private static final class GroupIDPredicate implements ScreenStack.Predicate {

		private final String groupId;

		public GroupIDPredicate(final String groupID) {
			this.groupId = groupID;
		}

		@Override
		public boolean apply(final Screen screen) {
			if (screen instanceof AbstractScreen) {
				final AbstractScreen abstractScreen = (AbstractScreen) screen;
				if (abstractScreen.getGroupID() != null && groupId.equals(abstractScreen.getGroupID())) {
					return true;
				}
			}
			return false;
		}

	}

	@Override
	protected Screen top() {
		if (_screens.isEmpty()) {
			return null;
		}
		return super.top();
	}

	// <---- ScreenStack
}
