package fr.byob.game.memeduel.core.gui.screen.level;

import playn.core.Keyboard.TextType;
import playn.core.PlayN;
import playn.core.util.Callback;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Field;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.FieldBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MenuScreen;
import fr.byob.game.memeduel.core.gui.screen.load.EditLevelLoadingScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PreviewLevelLoadingScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PreviewLevelLoadingScreen.Action;
import fr.byob.game.memeduel.core.net.LatestLevelBuffer;
import fr.byob.game.memeduel.core.net.OwnedLevelBuffer;
import fr.byob.game.memeduel.domain.Level;
import fr.byob.game.memeduel.domain.User;

public class WelcomeScreen extends MenuScreen {

	@Override
	protected Group menu() {
		final Button newCastle = ButtonBuilder.instance().text("NEW CASTLE").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final GameStack stack = GameStack.instance();
				final User user = GameContext.instance().getValue(MemeDuel.USER);
				final Level level = new Level(user.getLogin(), "{\"maxLifespanSum\" : 4000,\"size\" : {\"width\":0,\"height\":0},\"objects\": []}", "Castle of " + user.getLogin());
				GameContext.instance().setValue(MemeDuel.LEVEL, level);
				final EditLevelLoadingScreen loadingScreeen = new EditLevelLoadingScreen("NewLevelScreenGroup");

				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		}).build();

		final Button myCastles = ButtonBuilder.instance().text("MY CASTLES").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final GameStack stack = GameStack.instance();
				final User user = GameContext.instance().getValue(MemeDuel.USER);
				final OwnedLevelBuffer buffer = new OwnedLevelBuffer(user.getLogin());
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen("MY CASTLES", Action.NEXT, buffer);

				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();

			}
		}).build();

		final Button latestCastles = ButtonBuilder.instance().text("LATEST CASTLES").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final LatestLevelBuffer buffer = new LatestLevelBuffer();
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen("LATEST CASTLES", Action.NEXT, buffer);
				final GameStack stack = GameStack.instance();
				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		}).build();


		final String label = "LORD NAME";
		final TextType type = TextType.DEFAULT;
		final Field byOwnerField = FieldBuilder.instance().label(label).textType(type).build();

		final Button byOwnerButton = ButtonBuilder.instance().text("BY LORD").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final String text = byOwnerField.text.get();
				if (text == null || text.isEmpty()) {
					PlayN.keyboard().getText(type, label, text, new Callback<String>() {
						@Override
						public void onSuccess(final String result) {
							// null result is a canceled entry dialog.
							if (result != null) {
								byOwnerField.text.update(result);
								goToLoading();
							}
						}

						@Override
						public void onFailure(final Throwable cause) { /* noop */
						}
					});
				} else {
					goToLoading();
				}
			}

			private void goToLoading() {
				final OwnedLevelBuffer buffer = new OwnedLevelBuffer(byOwnerField.text.get());
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen(byOwnerField.text.get() + "'s CASTLES", Action.NEXT, buffer);
				final GameStack stack = GameStack.instance();
				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		}).build();


		final Group byOwnerGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(byOwnerButton, byOwnerField);

		final Group welcomeMenu = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()).offEqualize()).add(newCastle, myCastles, latestCastles, byOwnerGroup);
		return welcomeMenu;
	}

	@Override
	protected Label title() {
		return LabelBuilder.instance().text("WELCOME " + ((User) GameContext.instance().getValue(MemeDuel.USER)).getLogin()).styles(GameStyles.getInstance().getTitleStyles()).constraint(AxisLayout.stretched()).build();
	}

}