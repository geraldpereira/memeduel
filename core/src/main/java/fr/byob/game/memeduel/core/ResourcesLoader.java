package fr.byob.game.memeduel.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import playn.core.Sound;
import fr.byob.game.memeduel.core.patch.AssetWatcher;

public abstract class ResourcesLoader implements Updatable {

	protected final AssetWatcher.Listener listener;

	private final static int BUFFER_SIZE = 5;

	private final AssetWatcher assetWatcher;
	private final List<GameImage> images = new ArrayList<GameImage>(getImageResources());
	private final List<GameSound> sounds = new ArrayList<GameSound>(getSoundResources());
	private final List<GameMusic> musics = new ArrayList<GameMusic>(getMusicResources());

	private int currentImagesIndex = 0;
	private int currentSoundsIndex = 0;
	private int currentMusicsIndex = 0;

	public ResourcesLoader(final AssetWatcher.Listener listener) {
		this.listener = listener;
		this.assetWatcher = new AssetWatcher(listener, images.size() + sounds.size());
	}

	public void loadResources() {
		assetWatcher.start();
		GameLoop.instance().register(this);
	}

	@Override
	public void update(final float delta) {

		int i = 0;
		for (; i < BUFFER_SIZE && currentImagesIndex < images.size(); i++) {
			assetWatcher.add(images.get(currentImagesIndex).getImage());
			currentImagesIndex++;
		}

		for (; i < BUFFER_SIZE && currentSoundsIndex < sounds.size(); i++) {
			final Sound[] curSounds = sounds.get(currentSoundsIndex).getSounds();
			for (final Sound sound : curSounds){
				assetWatcher.add(sound);
			}
			currentSoundsIndex++;
		}

		for (; i < BUFFER_SIZE && currentMusicsIndex < musics.size(); i++) {
			assetWatcher.add(musics.get(currentMusicsIndex).getSound());
			currentMusicsIndex++;
		}

		if (assetWatcher.isDone()) {
			GameLoop.instance().deregister(this);
		}
	}

	protected Collection<GameImage> getImageResources() {
		return GameImage.values();
	}

	protected Collection<GameSound> getSoundResources() {
		return GameSound.values();
	}

	protected Collection<GameMusic> getMusicResources() {
		return GameMusic.values();
	}

}
