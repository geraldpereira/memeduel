package fr.byob.game.memeduel.core.gui.screen.user;

import playn.core.util.Callback;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.net.helper.UserNetHelper;
import fr.byob.game.memeduel.domain.User;

public class LoginScreen extends UserScreen {

	public LoginScreen(){
	}

	public LoginScreen(final String groupID) {
		super(groupID);
	}

	@Override
	protected Label title() {
		return LabelBuilder.instance().text(titleText()).styles(GameStyles.getInstance().getTitleStyles()).constraint(AxisLayout.stretched()).build();
	}

	@Override
	protected String buttonText() {
		return "CONNECT";
	}

	@Override
	protected void callUserDAO(final User user, final Callback<String> callback) {
		final UserNetHelper dao = GameContext.instance().getValue(MemeDuel.USER_NET_HELPER);
		dao.login(user, callback);
	}

	protected String titleText() {
		return "LOGIN";
	}

}
