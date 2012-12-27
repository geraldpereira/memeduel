package fr.byob.game.memeduel.core.gui.screen.duel;

import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MenuScreen;

public class WelcomeDuelScreen extends MenuScreen {

	@Override
	protected Group menu() {

		// A duel mode with the cannon balls defined in the levels
		final Button mode1 = ButtonBuilder.instance().text("PLAY FAIR").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				// TODO sore the mode somewhere, like in the gameContext ?
			}
		}).build();

		final Button mode2 = ButtonBuilder.instance().text("MODE 2").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
			}
		}).build();

		final Button mode3 = ButtonBuilder.instance().text("MODE 3").slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
			}
		}).build();

		final Group welcomeMenu = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()).offEqualize()).add(mode1, mode2, mode3);
		return welcomeMenu;
	}

	@Override
	protected Label title() {
		return LabelBuilder.instance().text("WELCOME LORDS").styles(GameStyles.getInstance().getTitleStyles()).constraint(AxisLayout.stretched()).build();
	}

}