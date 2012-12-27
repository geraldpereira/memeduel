package fr.byob.game.memeduel.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import playn.core.PlayN;
import playn.core.Sound;

public class GameMusic {

	private final static Map<String, GameMusic> allSounds = new HashMap<String, GameMusic>();
	private Sound sound;

	public GameMusic(final String name, final String path) {
		this(name, path, 0.8f);
	}

	public GameMusic(final String name, final String path, final float volume) {
		if (allSounds.containsKey(name)) {
			PlayN.log().warn("GameMusic " + name + " has already been created!");
		}
		allSounds.put(name, this);
		sound = PlayN.assets().getSound(path);
		sound.setVolume(volume);
		sound.setLooping(true);
	}

	public Sound getSound() {
		return sound;
	}

	public void play() {
		sound.play();
	}

	public void stop() {
		sound.stop();
	}

	public static Collection<GameMusic> values() {
		return allSounds.values();
	}

	public static GameMusic valueOf(final String name) {
		return allSounds.get(name);
	}

	public static void setVolume(final float volume) {
		for (final GameMusic music : allSounds.values()) {
			music.getSound().setVolume(volume);
		}
	}


}