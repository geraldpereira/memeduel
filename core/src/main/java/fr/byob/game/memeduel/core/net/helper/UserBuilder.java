package fr.byob.game.memeduel.core.net.helper;

import playn.core.Json.Object;
import playn.core.Json.Writer;
import playn.core.PlayN;
import fr.byob.game.memeduel.domain.User;

public class UserBuilder {
	private String login;
	private String password;
	private String email;

	public UserBuilder login(final String login) {
		this.login = login;
		return this;
	}

	public UserBuilder password(final String password) {
		this.password = password;
		return this;
	}

	public UserBuilder email(final String email) {
		this.email = email;
		return this;
	}

	public UserBuilder json(final String json) {
		final Object object = PlayN.json().parse(json);
		this.login = object.getString("login");
		this.password = object.getString("password");
		this.email = object.getString("email");
		return this;
	}

	public User build() {
		return new User(login, password, email);
	}

	public static String toJSON(final User user) {
		final Writer writer = PlayN.json().newWriter();
		writer.object();
		writer.value("login", user.getLogin());
		writer.value("password", user.getPassword());
		writer.value("email", user.getEmail());
		writer.end();
		final String userJSON = writer.write();
		return userJSON;
	}

}