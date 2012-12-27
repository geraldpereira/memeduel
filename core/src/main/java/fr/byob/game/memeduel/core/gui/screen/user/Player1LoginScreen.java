package fr.byob.game.memeduel.core.gui.screen.user;

import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.net.helper.UserBuilder;
import fr.byob.game.memeduel.domain.User;

public class Player1LoginScreen extends LoginScreen {

	@Override
	protected String titleText() {
		return "LORD ONE LOGIN";
	}

	@Override
	public void onSuccess(final String result) {
		final User user = new UserBuilder().json(result).build();
		GameContext.instance().setValue(MemeDuel.USER_1, user);

		final Player2LoginScreen player2LoginScreen = new Player2LoginScreen();
		GameStack.instance().replace(player2LoginScreen);
	}

}
