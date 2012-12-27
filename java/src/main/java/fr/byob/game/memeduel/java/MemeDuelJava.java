package fr.byob.game.memeduel.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;
import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.net.JavaWebResource;

public class MemeDuelJava {

	public static void main(final String[] args) {
		final JavaPlatform platform = JavaPlatform.register();
		platform.assets().setPathPrefix("fr/byob/game/memeduel/resources");
		// platform.graphics().setSize(1280, 800);
		// platform.graphics().setSize(1024, 768);
		// platform.graphics().setSize(400, 350);
		platform.graphics().registerFont("Meme", "text/impact.ttf");
		PlayN.run(new MemeDuel(new JavaWebResource(platform), new JavaBase64(), new org.jbox2d.common.Box2D()));
	}
}
