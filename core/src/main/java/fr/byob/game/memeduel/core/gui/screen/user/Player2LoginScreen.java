package fr.byob.game.memeduel.core.gui.screen.user;

import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.screen.duel.WelcomeDuelScreen;
import fr.byob.game.memeduel.core.net.helper.UserBuilder;
import fr.byob.game.memeduel.domain.User;

public class Player2LoginScreen extends LoginScreen {

	@Override
	protected String titleText() {
		return "LORD TWO LOGIN";
	}

	@Override
	public void onSuccess(final String result) {
		final User user = new UserBuilder().json(result).build();
		GameContext.instance().setValue(MemeDuel.USER_2, user);

		final WelcomeDuelScreen welcomeScreen = new WelcomeDuelScreen();
		GameStack.instance().replace(welcomeScreen);
	}

}
