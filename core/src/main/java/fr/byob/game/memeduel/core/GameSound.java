package fr.byob.game.memeduel.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import playn.core.PlayN;
import playn.core.Sound;

public class GameSound {

	private final static int AMOUNT = 3;
	private final static Map<String, GameSound> allSounds = new HashMap<String, GameSound>();
	private static boolean mute = true;

	private Sound[] sounds;
	private int currentSoundIndex;

	public GameSound(final String name, final String path) {
		this(name, path, 1.0f);
	}

	public GameSound(final String name, final String path, final float volume) {
		this(name, path, volume, false, AMOUNT);
	}

	public GameSound(final String name, final String path, final float volume, final int amount) {
		this(name, path, volume, false, amount);
	}

	public GameSound(final String name, final String path, final float volume, final boolean looping) {
		this(name, path, volume, looping, AMOUNT);
	}

	public GameSound(final String name, final String path, final float volume, final boolean looping, final int amount) {
		if (allSounds.containsKey(name)) {
			PlayN.log().warn("GameSound" + name + " has already been created!");
		}
		allSounds.put(name, this);
		sounds = new Sound[amount];
		for (int i = 0; i < amount; i++) {
			final Sound sound = PlayN.assets().getSound(path);
			sound.setVolume(volume);
			sound.setLooping(looping);
			sounds[i] = sound;
		}
		currentSoundIndex = 0;
	}

	public Sound[] getSounds() {
		return this.sounds;
	}

	private Sound getCurrentSound() {
		return sounds[currentSoundIndex];
	}

	public void play() {
		if (mute) {
			return;
		}
		final Sound sound = getCurrentSound();
		if (sound.isPlaying()) {
			currentSoundIndex++;
			if (currentSoundIndex >= sounds.length) {
				currentSoundIndex = 0;
			}
		} else {
			sound.play();
		}
	}

	public void stop() {
		for (final Sound sound : sounds) {
			sound.stop();
		}
	}

	public static Collection<GameSound> values() {
		return allSounds.values();
	}

	public static GameSound valueOf(final String name) {
		return allSounds.get(name);
	}

	public static void mute() {
		mute = true;
	}

	public static void unmute() {
		mute = false;
	}

}