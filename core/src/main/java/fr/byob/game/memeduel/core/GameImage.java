package fr.byob.game.memeduel.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import playn.core.Image;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

public class GameImage {
	private final static Map<String, GameImage> images = new HashMap<String, GameImage>();

	private Image image;
	private final String path;
	private final String name;

	public GameImage(final String name, final String path) {
		this.path = path;
		this.name = name;
		if (images.containsKey(name)) {
			PlayN.log().warn("GameImage " + name + " has already been created!");
		}
		images.put(name, this);
	}

	/**
	 * Image transient
	 * 
	 * @param image
	 */
	public GameImage(final Image image) {
		this.image = image;
		this.path = null;
		this.name = null;
	}

	public Image getImage() {
		if (this.image == null) {
			this.image = PlayN.assets().getImage(this.path);
		}
		return this.image;
	}

	public String name() {
		return this.name;
	}

	public Dimension dimensionToOut(final Dimension out) {
		out.setSize(this.getImage().width(), this.getImage().height());
		return out;
	}

	public Vector centerPositionToOut(final Vector out) {
		out.set(this.getImage().width() / 2, this.getImage().height() / 2);
		return out;
	}

	public static Collection<GameImage> values() {
		return images.values();
	}

	public static GameImage valueOf(final String name) {
		return images.get(name);
	}

	public static Image getImage(final String name) {
		return images.get(name).getImage();
	}
}