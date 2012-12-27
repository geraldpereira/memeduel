package fr.byob.game.memeduel.core.gui.screen;

import playn.core.Font;
import playn.core.PlayN;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.load.PreviewLevelLoadingScreen;
import fr.byob.game.memeduel.core.gui.screen.load.PreviewLevelLoadingScreen.Action;
import fr.byob.game.memeduel.core.gui.screen.user.LoginScreen;
import fr.byob.game.memeduel.core.gui.screen.user.SubscribeScreen;
import fr.byob.game.memeduel.core.net.DemoLevelBuffer;

public class IntroScreen extends MenuScreen {

	@Override
	public void wasAdded() {
		// TODO Auto-generated method stub
		super.wasAdded();
	}

	@Override
	protected Group menu() {
		final Button oneLord = ButtonBuilder.instance().text("ONE LORD").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				GameStack.instance().push(new LoginScreen());
			}
		}).build();

		final Button duel = ButtonBuilder.instance().text("DUEL").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				GameStack.instance().push(new LoginScreen("LORD 1 LOGIN"));
				// TODO Et le next sceen ? back par group id aussi ...
			}
		}).build().setEnabled(false);

		final Button demo = ButtonBuilder.instance().text("DEMO").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				final GameStack stack = GameStack.instance();
				final DemoLevelBuffer buffer = new DemoLevelBuffer();
				final PreviewLevelLoadingScreen loadingScreeen = new PreviewLevelLoadingScreen("DEMO CASTLES", Action.NEXT, buffer);
				stack.push(loadingScreeen, stack.slide());
				loadingScreeen.load();
			}
		}).build().setEnabled(false);

		final Button subscribe = ButtonBuilder.instance().text("NEW ACCOUNT").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				GameStack.instance().push(new SubscribeScreen());
			}
		}).build();

		final Group introMenu = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()).offEqualize()).add(oneLord, duel, demo, subscribe);

		// final Button disabled = new ButtonBuilder().text("DISABLED").build();
		// disabled.setEnabled(false);
		// final Field field = new
		// FieldBuilder().label("FIELD").initialText("FIELD").build();
		// final Field fieldDis = new
		// FieldBuilder().label("FIELD").initialText("FIELD").build();
		// fieldDis.setEnabled(false);
		// introMenu.add(new ButtonBuilder().text("TEST").slot(new
		// Slot<Button>() {
		// @Override
		// public void onEmit(final Button event) {
		// GameContext.getInstance().push(new MessageScreen("groupID", "TITLE",
		// MemeDuelLoader.GAME_END_WTF));
		// }
		// }).build());
		// introMenu.add(new ButtonBuilder().text("TEST2").slot(new
		// Slot<Button>() {
		// @Override
		// public void onEmit(final Button event) {
		// GameContext.getInstance().push(new MessageScreen(null,
		// "NO MORE LEVELS", "BUT HERE IS A POTATO", MemeDuelLoader.POTATO));
		// }
		// }).build());

		return introMenu;
	}

	@Override
	protected Label title() {
		return LabelBuilder.instance().text("MEME DUEL").constraint(AxisLayout.stretched()).styles(GameStyles.getInstance().getLabelStyles().add((Style.FONT.is(PlayN.graphics().createFont("Meme", Font.Style.BOLD, 75))))).build();
	}

	@Override
	protected Button back() {
		return null;
	}
}
