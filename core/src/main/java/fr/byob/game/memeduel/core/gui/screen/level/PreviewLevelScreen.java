package fr.byob.game.memeduel.core.gui.screen.level;

import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.god.MemeDuelGODLoader;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MenuScreen;
import fr.byob.game.memeduel.core.gui.screen.load.EditLevelLoadingScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PlayLevelLoadingScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PreviewLevelLoadingScreen;
import fr.byob.game.memeduel.core.net.ListQueryBuffer;
import fr.byob.game.memeduel.core.view.StandAloneView;
import fr.byob.game.memeduel.domain.Level;
import fr.byob.game.memeduel.domain.User;

public class PreviewLevelScreen extends MenuScreen {

	private final ListQueryBuffer<Level> buffer;
	private final MemeDuelGODLoader loader;
	private final String title;

	private Button previous;
	private Button next;

	public PreviewLevelScreen(final String title, final ListQueryBuffer<Level> buffer, final MemeDuelGODLoader loader) {
		this.buffer = buffer;
		this.loader = loader;
		this.title = title;
	}

	@Override
	protected Group menu() {
		final StandAloneView view = new StandAloneView(loader);
		view.addStyles(Style.HALIGN.center);
		view.addStyles(Style.VALIGN.bottom);
		view.setConstraint(AxisLayout.fixed());
		// To display it behind the buttons
		view.layer.setDepth(-2);

		final Label title = LabelBuilder.instance().text(buffer.getCurrentItem().getTitle()).constraint(AxisLayout.stretched()).build();
		title.layer.setDepth(100);

		final Group group = new Group(AxisLayout.vertical().gap(0)).add(title, view, new Shim(width(), 35));
		return group;
	}

	@Override
	protected Group west() {
		previous = ButtonBuilder.instance().text("<").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen(title, PreviewLevelLoadingScreen.Action.PREVIOUS, buffer);

				final GameStack stack = GameStack.instance();
				stack.replace(loadingScreeen, stack.slide().right());
				loadingScreeen.load();
			}
		}).build();
		previous.setEnabled(buffer.hasPrevious());
		final Group group = new Group(AxisLayout.vertical().gap(0).offStretch(), GameStyles.getInstance().getBordersBackgroundStyle());
		group.add(previous);
		return group;
	}

	@Override
	protected Group east() {
		next = ButtonBuilder.instance().text(">").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen(title, PreviewLevelLoadingScreen.Action.NEXT, buffer);

				final GameStack stack = GameStack.instance();
				stack.replace(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		}).build();
		next.setEnabled(buffer.hasNext());
		final Group group = new Group(AxisLayout.vertical().gap(0).offStretch(), GameStyles.getInstance().getBordersBackgroundStyle());
		group.add(next);
		return group;
	}

	@Override
	protected Group buttons() {
		final Group group = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap()).offStretch(), GameStyles.getInstance().getBordersBackgroundStyle());
		final boolean owner = isOwner();

		// .icon(MemeDuelLoader.TEST)
		group.add(ButtonBuilder.instance().text("PLAY").slot(new Slot<Button>() {

			@Override
			public void onEmit(final Button event) {
				final GameStack stack = GameStack.instance();
				GameContext.instance().setValue(MemeDuel.LEVEL, buffer.getCurrentItem());
				final PlayLevelLoadingScreen loadingScreeen = new PlayLevelLoadingScreen("PlayLevelScreenGroup");

				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}

		}).build());

		// .icon(MemeDuelLoader.EDIT)
		if (owner) {
			group.add(ButtonBuilder.instance().text("EDIT").slot(new Slot<Button>() {
				@Override
				public void onEmit(final Button event) {
					final GameStack stack = GameStack.instance();

					GameContext.instance().setValue(MemeDuel.LEVEL, buffer.getCurrentItem());
					final EditLevelLoadingScreen loadingScreeen = new EditLevelLoadingScreen("EditLevelScreenGroup");
					// Remove this screen as the level may change, it is simpler
					// for us to return to the welcome panel when it is done!
					stack.replace(loadingScreeen, stack.slide());
					loadingScreeen.load();
				}
			}).build());
			// .icon(MemeDuelLoader.TRASH)
			group.add(ButtonBuilder.instance().text("DELETE").slot(new Slot<Button>() {
				@Override
				public void onEmit(final Button event) {
					final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen(title, PreviewLevelLoadingScreen.Action.DELETE, buffer);
					final GameStack stack = GameStack.instance();
					stack.replace(loadingScreeen, stack.slide());
					loadingScreeen.load();
				}
			}).build());
		}
		return group;
	}

	private boolean isOwner() {
		final Level level = buffer.getCurrentItem();
		final User user = GameContext.instance().getValue(MemeDuel.USER);
		return user.getLogin().equals(level.getOwner());
	}

	@Override
	protected Label title() {
		return LabelBuilder.instance().text(title).styles(GameStyles.getInstance().getTitleStyles()).constraint(AxisLayout.stretched()).build();
	}
}