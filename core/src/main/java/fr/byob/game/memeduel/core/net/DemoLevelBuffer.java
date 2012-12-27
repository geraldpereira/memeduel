package fr.byob.game.memeduel.core.net;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;
import fr.byob.game.memeduel.core.GameContext;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.net.helper.LevelBuilder;
import fr.byob.game.memeduel.core.net.helper.LevelNetHelper;
import fr.byob.game.memeduel.domain.Level;

public class DemoLevelBuffer extends ListQueryBuffer<Level> {

	public DemoLevelBuffer() {
		super(5);
	}

	@Override
	protected void queryList(final int limit, final int offset, final Callback<Level[]> callBack) {
		final LevelNetHelper dao = GameContext.instance().getValue(MemeDuel.LEVEL_NET_HELPER);
		dao.demo(limit, offset, new Callback<String>() {
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
		callBack.onFailure(new UnsupportedOperationException("Cannot delete demo levels"));
	}

}
