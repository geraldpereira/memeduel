package fr.byob.game.memeduel.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;
import fr.byob.game.memeduel.core.MemeDuel;

public class MemeDuelHtml extends HtmlGame {

	@Override
	public void start() {
		final HtmlPlatform platform = HtmlPlatform.register();
		platform.assets().setPathPrefix("memeduel/");
		PlayN.graphics().setSize(1024, 768);
		PlayN.run(new MemeDuel(new HtmlWebResource(platform), new HtmlBase64(), new org.jbox2d.common.Box2D()));
	}
}
