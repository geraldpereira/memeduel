package fr.byob.game.memeduel.core;

import fr.byob.game.memeduel.core.model.handler.damage.DecayDamageHandler;
import fr.byob.game.memeduel.core.model.object.ModelObject;

public class MemeDuelUtils {

	public final static String MEME_MEDIUM = "MEME_MEDIUM";
	public final static String MEME_LARGE = "MEME_LARGE";

	public final static String CANNON_BALL_SMALL = "CANNON_BALL_SMALL";
	public final static String CANNON_BALL_LARGE = "CANNON_BALL_LARGE";
	public final static String CANNON_BALL_FRAG = "CANNON_BALL_FRAG";
	public final static String CANNON_BALL_FRAG_SHOT = "CANNON_BALL_FRAG_SHOT";

	public final static String OBJECT_WOOD = "OBJECT_WOOD";
	public final static String OBJECT_STONE = "OBJECT_STONE";
	public final static String OBJECT_GLASS = "OBJECT_GLASS";
	public final static String OBJECT_MUD = "OBJECT_MUD";

	public static boolean isObject(final ModelObject modelObject) {
		return modelObject != null && modelObject.getTypeId().startsWith("OBJECT");
	}

	public static boolean isObjectNotFragment(final ModelObject modelObject) {
		if (isObject(modelObject)) {
			if (modelObject.isDamageable() && !(modelObject.getDamageHandler() instanceof DecayDamageHandler)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMeme(final ModelObject modelObject) {
		return modelObject != null && modelObject.getTypeId().startsWith("MEME");
	}

	public static boolean isCannonBall(final ModelObject modelObject) {
		return modelObject != null && modelObject.getTypeId().startsWith("CANNON_BALL");
	}
}
