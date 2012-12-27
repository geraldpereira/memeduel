package fr.byob.game.memeduel.android;

import playn.android.GameActivity;
import playn.core.Font;
import playn.core.PlayN;

import com.badlogic.gdx.physics.box2d.Box2D;

import fr.byob.game.memeduel.core.MemeDuel;
import fr.byob.game.memeduel.core.net.JavaWebResource;

public class MemeDuelActivity extends GameActivity {

	static {
		System.loadLibrary("box2d-android-nativelib");
	}

	@Override
	public void main(){
		this.platform().assets().setPathPrefix("fr/byob/game/memeduel/resources");
		platform().graphics().registerFont("text/impact.otf", "Meme", Font.Style.PLAIN);
		platform().graphics().registerFont("text/impact.otf", "Meme", Font.Style.BOLD);
		PlayN.run(new MemeDuel(new JavaWebResource(this.platform()), new AndroidBase64(), new Box2D()));
	}
}
