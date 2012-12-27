/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package fr.byob.game.memeduel.core.patch;

import playn.core.Asserts;
import playn.core.Image;
import playn.core.ResourceCallback;
import playn.core.Sound;

/**
 * A utility class that helps keep track of image loading.
 * <p>
 * To use: create a new {@link AssetWatcher}, then add images using
 * {@link AssetWatcher#add(Image)} and finally call {@link AssetWatcher#start()}.
 */
public class AssetWatcher {
	/**
	 * Listener interface for AssetWatcher.
	 */
	public interface Listener {
		/**
		 * Called when all assets are done loading (or had an error).
		 */
		void done();

		/**
		 * Called when assets are done loading.
		 */
		void progress(float percentage);

		/**
		 * Called for each asset that failed to load.
		 */
		void error(Throwable e);
	}

	private final int total;
	private int loaded, errors;
	private boolean start;
	private final Listener listener;

	@SuppressWarnings("rawtypes")
	private ResourceCallback callback = new ResourceCallback() {
		@Override
		public void done(final Object resource) {
			++loaded;
			maybeDone();
		}

		@Override
		public void error(final Throwable e) {
			++errors;
			if (listener != null) {
				listener.error(e);
			}
			maybeDone();
		}
	};

	/**
	 * Creates a new watcher without a listener.
	 */
	public AssetWatcher(final int expectedTotal) {
		this(null, expectedTotal);
		start();
	}

	/**
	 * Creates a new watcher with the given listener.
	 * <p>
	 * Note: must call {@link AssetWatcher#start()} after adding your resources.
	 */
	public AssetWatcher(final Listener listener, final int total) {
		this.listener = listener;
		this.total = total;
	}

	/**
	 * Adds an image resource to be watched.
	 */
	@SuppressWarnings("unchecked")
	public void add(final Image image) {
		Asserts.checkState(!start || listener == null);
		image.addCallback(callback);
	}

	/**
	 * Adds a sound resource to be watched.
	 */
	@SuppressWarnings("unchecked")
	public void add(final Sound sound) {
		Asserts.checkState(!start || listener == null);
		sound.addCallback(callback);
	}

	/**
	 * Whether all resources have completed loading, either successfully or in
	 * error.
	 */
	public boolean isDone() {
		return start && (loaded + errors == total);
	}

	/**
	 * Done adding resources; {@link Listener#done()} will be called as soon as
	 * all assets are done being loaded.
	 * 
	 * There is no need to call this method if there is no listener.
	 * {@link #isDone()} will return <code>true</code> as soon as all pending
	 * assets are loaded.
	 */
	public void start() {
		start = true;
		maybeDone();
	}

	private void maybeDone() {
		if (isDone()) {
			if (listener != null) {
				listener.done();
			}
		} else {
			listener.progress(((float) (loaded + errors)) / (float) total);
		}
	}
}
