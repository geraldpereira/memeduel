package fr.byob.game.memeduel.core.net.helper;

import playn.core.Json;
import playn.core.Json.Object;
import playn.core.Json.Writer;
import playn.core.PlayN;
import fr.byob.game.memeduel.domain.Level;

public class LevelBuilder {
	private Long id;
	private String owner;
	private String content;
	private String title;

	public LevelBuilder owner(final String owner) {
		this.owner = owner;
		return this;
	}

	public LevelBuilder content(final String content) {
		this.content = content;
		return this;
	}

	public LevelBuilder title(final String title) {
		this.title = title;
		return this;
	}

	public LevelBuilder id(final Long id) {
		this.id = id;
		return this;
	}

	public LevelBuilder json(final String json) {
		final Object object = PlayN.json().parse(json);
		return json(object);
	}

	public LevelBuilder json(final Json.Object object) {
		this.owner = object.getString("owner");
		this.content = object.getString("content");
		this.title = object.getString("title");
		// Hummmm
		this.id = Long.valueOf((long) object.getNumber("id"));
		return this;
	}

	public Level build() {
		return new Level(id, owner, content, title);
	}

	public static String toJSON(final Level level) {
		final Writer writer = PlayN.json().newWriter();
		writer.object();
		writer.value("id", level.getId());
		writer.value("owner", level.getOwner());
		writer.value("content", level.getContent());
		writer.value("title", level.getTitle());
		writer.end();
		final String userJSON = writer.write();
		return userJSON;
	}

}