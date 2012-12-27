package fr.byob.game.memeduel.core.net;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.net.helper.LevelBuilder;
import fr.byob.game.memeduel.core.net.helper.LevelNetHelper;
import fr.byob.game.memeduel.domain.Level;
import fr.byob.game.memeduel.domain.User;

public class LatestLevelBuffer extends ListQueryBuffer<Level> {


	public LatestLevelBuffer() {
		super(5);
	}

	@Override
	protected void queryList(final int limit, final int offset, final Callback<Level[]> callBack) {
		final User user = GameContext.instance().getValue(MemeDuel.USER);
		final LevelNetHelper dao = GameContext.instance().getValue(MemeDuel.LEVEL_NET_HELPER);
		dao.latest(user, limit, offset, new Callback<String>() {
			@Override
			public void onSuccess(final String result) {
				final Json.Array array = PlayN.json().parseArray(result);

				final Level[] levels = new Level[array.length()];
				for (int i = 0; i < array.length(); i++) {
					levels[i] = new LevelBuilder().json(array.getObject(i)).build();
				}
				callBack.onSuccess(levels);
			}

			@Override
			public void onFailure(final Throwable cause) {
				callBack.onFailure(cause);
			}
		});
	}

	@Override
	protected void queryDelete(final Level value, final Callback<String> callBack) {
		final User user = GameContext.instance().getValue(MemeDuel.USER);
		final LevelNetHelper dao = GameContext.instance().getValue(MemeDuel.LEVEL_NET_HELPER);
		dao.delete(user, value, callBack);
	}

}
