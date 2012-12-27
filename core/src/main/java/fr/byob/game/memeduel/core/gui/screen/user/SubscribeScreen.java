package fr.byob.game.memeduel.core.gui.screen.user;

import playn.core.Keyboard.TextType;
import playn.core.util.Callback;
import tripleplay.ui.Field;
import tripleplay.ui.Label;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.builders.FieldBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.net.helper.UserNetHelper;
import fr.byob.game.memeduel.domain.User;

public class SubscribeScreen extends UserScreen {

	@Override
	protected Label title() {
		return LabelBuilder.instance().text("NEW ACCOUNT").styles(GameStyles.getInstance().getTitleStyles()).constraint(AxisLayout.stretched()).build();
	}

	@Override
	protected String buttonText() {
		return "CREATE";
	}

	@Override
	protected Field newEmailField() {
		final Field emailField = FieldBuilder.instance().textType(TextType.EMAIL).initialText(EMAIL).build();
		return emailField;
	}

	@Override
	protected void callUserDAO(final User user, final Callback<String> callback) {
		final UserNetHelper dao = GameContext.instance().getValue(MemeDuel.USER_NET_HELPER);
		dao.subscribe(user, callback);
	}

}
