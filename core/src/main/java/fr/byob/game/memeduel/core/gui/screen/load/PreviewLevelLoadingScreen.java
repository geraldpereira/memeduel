package fr.byob.game.memeduel.core.gui.screen.load;

import playn.core.PlayN;
import playn.core.util.Callback;
import tripleplay.game.ScreenStack.Transition;
import tripleplay.game.trans.SlideTransition;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.MemeDuelLoader;
import fr.byob.game.memeduel.core.controller.GameMode;
import fr.byob.game.memeduel.core.god.AllGODLoader;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.screen.MessageScreen;
import fr.byob.game.memeduel.core.gui.screen.level.PreviewLevelScreen;
import fr.byob.game.memeduel.core.net.ListQueryBuffer;
import fr.byob.game.memeduel.domain.Level;

public class PreviewLevelLoadingScreen extends LoadingScreen<Level> {

	public enum Action {
		DELETE {
			@Override
			public void load(final PreviewLevelLoadingScreen screen) {
				screen.buffer.deleteCurrentItem(screen);
			}

			@Override
			public Transition newTransition() {
				return new SlideTransition(GameStack.instance()).down();
			}
		},

		NEXT {
			@Override
			public void load(final PreviewLevelLoadingScreen screen) {
				screen.buffer.getNextItem(screen);
			}

			@Override
			public Transition newTransition() {
				return new SlideTransition(GameStack.instance()).left();
			}
		},
		PREVIOUS {
			@Override
			public void load(final PreviewLevelLoadingScreen screen) {
				screen.buffer.getPreviousItem(screen);
			}

			@Override
			public Transition newTransition() {
				return new SlideTransition(GameStack.instance()).right();
			}
		};

		public abstract void load(final PreviewLevelLoadingScreen screen);

		public abstract Transition newTransition();
	}

	private final ListQueryBuffer<Level> buffer;
	private final Action action;
	private final String title;

	public PreviewLevelLoadingScreen(final String title, final Action action, final ListQueryBuffer<Level> buffer) {
		this.buffer = buffer;
		this.action = action;
		this.title = title;
	}

	@Override
	public void doLoad() {
		action.load(this);
	}

	@Override
	public void onSuccess(final Level result) {
		final MemeDuelGODLoader loader = new MemeDuelGODLoader();
		loader.loadFromContent(new Callback<AllGODLoader>() {
			@Override
			public void onSuccess(final AllGODLoader result) {
				GameStack.instance().replace(new PreviewLevelScreen(title, buffer, (MemeDuelGODLoader) result), action.newTransition());
			}

			@Override
			public void onFailure(final Throwable cause) {
				PreviewLevelLoadingScreen.this.onFailure(cause);

			}
		}, GameMode.PREVIEW.getGameOptionsFileName(), result.getContent());


	}

	@Override
	public void onFailure(final Throwable cause) {
		PlayN.log().debug(cause.getMessage());
		GameStack.instance().replace(new MessageScreen(null, "NO MORE LEVELS", "BUT HERE IS A POTATO", MemeDuelLoader.POTATO));
	}

	@Override
	protected String text() {
		return "LOADING LEVEL PREVIEW ...";
	}

}
