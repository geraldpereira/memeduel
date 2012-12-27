package fr.byob.game.memeduel.core.gui.screen.user;

import playn.core.util.Callback;
import react.Slot;
import tripleplay.ui.Button;
import tripleplay.ui.Field;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.GameLoop;
import fr.byob.game.memeduel.core.GameStack;
import fr.byob.game.memeduel.core.GameStyles;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.gui.PasswordElement;
import fr.byob.game.memeduel.core.gui.builders.ButtonBuilder;
import fr.byob.game.memeduel.core.gui.builders.FieldBuilder;
import fr.byob.game.memeduel.core.gui.builders.LabelBuilder;
import fr.byob.game.memeduel.core.gui.screen.MenuScreen;
import fr.byob.game.memeduel.core.gui.screen.level.WelcomeScreen;
import fr.byob.game.memeduel.core.net.helper.UserBuilder;
import fr.byob.game.memeduel.domain.User;

public abstract class UserScreen extends MenuScreen implements Callback<String> {

	protected final static String EMAIL = "EMAIL";
	protected final static String LOGIN = "NICKNAME";

	protected UserScreen() {
	}

	protected UserScreen(final String groupID) {
		super(groupID);
	}

	private Field loginField;
	private PasswordElement passwordField;
	protected Label infoMessage;
	protected Field emailField;
	protected Button connect;

	@Override
	protected Group menu() {

		infoMessage = LabelBuilder.instance().text("Select nickname and password").build();

		loginField = FieldBuilder.instance().initialText(LOGIN).build();
		passwordField = PasswordElement.builder().build().setStyles(Style.HALIGN.center);

		loginField.text.update("Kojiro");
		passwordField.setPassword("0010203031211101");

		// If not in groups the layout sucks
		final Group loginGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(loginField);
		final Group passwordGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(passwordField);
		final Group infoGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(infoMessage);
		final Group loginMenu = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()));

		loginMenu.add(infoGroup, loginGroup);

		emailField = newEmailField();
		if (emailField != null) {
			final Group emailGroup = new Group(AxisLayout.horizontal().gap(GameStyles.getInstance().getGap())).add(emailField);
			loginMenu.add(emailGroup);
		}

		loginMenu.add(passwordGroup);
		return loginMenu;
	}

	@Override
	protected Group buttons() {
		connect = ButtonBuilder.instance().text(buttonText()).slot(new Slot<Button>() {
			@Override
			public void onEmit(final Button event) {
				event.text.update("LOADING ...");
				event.setEnabled(false);
				// back.setEnabled(false);
				final UserBuilder builder = new UserBuilder().password(passwordField.getPassword());
				if (!LOGIN.equals(loginField.text.get())) {
					builder.login(loginField.text.get());
				}

				if (emailField != null && !EMAIL.equals(emailField.text.get())) {
					builder.email(emailField.text.get());
				}
				callUserDAO(builder.build(), UserScreen.this);
			}
		}).build();

		final Group buttonsGroup = new Group(AxisLayout.vertical().gap(GameStyles.getInstance().getGap()), GameStyles.getInstance().getBordersBackgroundStyle()).add(connect);
		return buttonsGroup;
	}

	protected Field newEmailField() {
		return null;
	}

	protected abstract String buttonText();

	protected abstract void callUserDAO(User user, Callback<String> callback);

	@Override
	public void onSuccess(final String result) {
		final User user = new UserBuilder().json(result).build();
		GameContext.instance().setValue(MemeDuel.USER, user);

		final WelcomeScreen welcomeScreen = new WelcomeScreen();
		GameStack.instance().replace(welcomeScreen);
	}

	@Override
	public void onFailure(final Throwable cause) {
		connect.text.update(buttonText());
		connect.setEnabled(true);
		// back.setEnabled(true);
		infoMessage.text.update(cause.getMessage());
	}

	@Override
	public void wasShown() {
		super.wasShown();
		GameLoop.instance().register(passwordField);
	}

	@Override
	public void wasHidden() {
		super.wasHidden();
		GameLoop.instance().deregister(passwordField);
	}


}
