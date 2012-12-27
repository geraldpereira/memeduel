/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package fr.byob.game.memeduel.core.god;

import playn.core.util.Callback;
import pythagoras.f.Vector;

public abstract class AllGODLoader {

	protected GameGOD gameGOD = null;
	protected GameOptionsGOD gameOptionsGOD = null;
	protected LevelGOD levelGOD = null;

	protected Vector b2dObjectsOffset;

	public AllGODLoader() {
	}

	public void loadFromFile(final Callback<AllGODLoader> callback, final String gameFileName, final String gameOptionsFileName, final String levelFileName) {
		gameGOD = new GameGOD(this);
		gameGOD.loadFromFile(gameFileName, new Callback<GameGOD>() {
			@Override
			public void onSuccess(final GameGOD resource) {
				loadFromFile(callback, gameOptionsFileName, levelFileName);
			}

			@Override
			public void onFailure(final Throwable err) {
				callback.onFailure(err);
			}
		});
	}

	public void loadFromContent(final Callback<AllGODLoader> callback, final String gameFileName, final String gameOptionsFileName, final String levelContent) {
		gameGOD = new GameGOD(this);
		gameGOD.loadFromFile(gameFileName, new Callback<GameGOD>() {
			@Override
			public void onSuccess(final GameGOD resource) {
				loadFromContent(callback, gameOptionsFileName, levelContent);
			}

			@Override
			public void onFailure(final Throwable err) {
				callback.onFailure(err);
			}
		});
	}

	public void loadFromContent(final Callback<AllGODLoader> callback, final String gameOptionsFileName, final String levelContent) {
		if (gameGOD == null) {
			gameGOD = new GameGOD(this);
		}
		gameOptionsGOD = newGameOptionsGOD();
		gameOptionsGOD.loadFromFile(gameOptionsFileName, new Callback<GameOptionsGOD>() {
			@Override
			public void onSuccess(final GameOptionsGOD result) {
				gameOptionsGOD = result;
				levelGOD = newLevelGOD();
				levelGOD.loadFromContent(levelContent);
				callback.onSuccess(AllGODLoader.this);
			}

			@Override
			public void onFailure(final Throwable cause) {
				callback.onFailure(cause);
			}
		});
	}

	public void loadFromFile(final Callback<AllGODLoader> callback, final String gameOptionsFileName, final String levelFileName) {
		if (gameGOD == null) {
			gameGOD = new GameGOD(this);
		}
		gameOptionsGOD = newGameOptionsGOD();
		gameOptionsGOD.loadFromFile(gameOptionsFileName, new Callback<GameOptionsGOD>() {
			@Override
			public void onSuccess(final GameOptionsGOD result) {
				gameOptionsGOD = result;
				levelGOD = newLevelGOD();
				levelGOD.loadFromFile(levelFileName, new Callback<LevelGOD>() {

					@Override
					public void onSuccess(final LevelGOD result) {
						callback.onSuccess(AllGODLoader.this);
					}

					@Override
					public void onFailure(final Throwable cause) {
						callback.onFailure(cause);
					}
				});

			}

			@Override
			public void onFailure(final Throwable cause) {
				callback.onFailure(cause);
			}
		});
	}


	public Vector getB2dObjectsOffset() {
		if (b2dObjectsOffset == null) {
			float xOffset = gameOptionsGOD.getXOffset();
			float yOffset = 0;
			if (gameGOD.getWorld() != null) {
				xOffset += ((gameGOD.getWorld().getWidth() - xOffset) / 2) - (levelGOD.getSize().width / 2);
				yOffset = gameGOD.getWorld().getGroundYPosition() - levelGOD.getSize().height;
			}
			b2dObjectsOffset = new Vector (xOffset, yOffset);
		}
		return b2dObjectsOffset;
	}

	protected abstract GameOptionsGOD newGameOptionsGOD();

	protected abstract LevelGOD newLevelGOD();

	public GameGOD getGameGOD() {
		return this.gameGOD;
	}

	public GameOptionsGOD getGameOptionsGOD() {
		return this.gameOptionsGOD;
	}

	public LevelGOD getLevelGOD() {
		return levelGOD;
	}

}

