package fr.byob.game.memeduel.core;

import fr.byob.game.memeduel.core.patch.AssetWatcher;

public class MemeDuelLoader extends ResourcesLoader {

	public static GameImage CANNON = new GameImage("CANNON", "images/cannon.png");
	public static GameImage CANNON_BALL = new GameImage("CANNON_BALL", "images/cannonBall.png");
	public static GameImage CANNON_BALL_DAMAGED = new GameImage("CANNON_BALL_DAMAGED", "images/cannonBallDamaged.png");
	public static GameImage CANNON_BALL_LARGE = new GameImage("CANNON_BALL_LARGE", "images/cannonBallLarge.png");
	public static GameImage CANNON_BALL_LARGE_DAMAGED = new GameImage("CANNON_BALL_LARGE_DAMAGED", "images/cannonBallLargeDamaged.png");
	public static GameImage CANNON_BALL_FRAG_SHELL = new GameImage("CANNON_BALL_FRAG_SHELL", "images/cannonBallFragShell.png");
	public static GameImage CANNON_BALL_FRAG_SHELL_DAMAGED = new GameImage("CANNON_BALL_FRAG_SHELL_DAMAGED", "images/cannonBallFragShellDamaged.png");
	public static GameImage CANNON_BALL_FRAG_SHOT = new GameImage("CANNON_BALL_FRAG_SHOT", "images/cannonBallFragShot.png");
	public static GameImage CANNON_BALL_FRAG_SHOT_DAMAGED = new GameImage("CANNON_BALL_FRAG_SHOT_DAMAGED", "images/cannonBallFragShot.png");

	public static GameImage CANNON_BALL_ICON = new GameImage("CANNON_BALL_ICON", "images/cannonBallIcon.png");
	public static GameImage CANNON_BALL_LARGE_ICON = new GameImage("CANNON_BALL_LARGE_ICON", "images/cannonBallLargeIcon.png");
	public static GameImage CANNON_BALL_FRAG_ICON = new GameImage("CANNON_BALL_FRAG_ICON", "images/cannonBallFragIcon.png");

	public static GameImage CANNON_BALL_TRACE_0 = new GameImage("CANNON_BALL_TRACE_0", "images/cannonBallTrace0.png");
	public static GameImage CANNON_BALL_TRACE_1 = new GameImage("CANNON_BALL_TRACE_1", "images/cannonBallTrace1.png");
	public static GameImage CANNON_BALL_TRACE_2 = new GameImage("CANNON_BALL_TRACE_2", "images/cannonBallTrace2.png");
	public static GameImage CANNON_BALL_TRACE_3 = new GameImage("CANNON_BALL_TRACE_3", "images/cannonBallTrace3.png");
	public static GameImage CANNON_BALL_TRACE_BONUS = new GameImage("CANNON_BALL_TRACE_BONUS", "images/cannonBallTraceBonus.png");
	public static GameImage CANNON_BASE = new GameImage("CANNON_BASE", "images/cannonBase.png");
	public static GameImage CANNON_BOLT = new GameImage("CANNON_BOLT", "images/cannonBolt.png");
	public static GameImage CANNON_MUZZLE_FLASH = new GameImage("CANNON_MUZZLE_FLASH", "images/cannonMuzzleFlash.png");
	public static GameImage CANNON_WHEEL = new GameImage("CANNON_WHEEL", "images/cannonWheel.png");


	public static GameImage CLOUD_I_LIED = new GameImage("CLOUD_I_LIED", "images/cloudILied.png");
	public static GameImage CLOUD_SWEET = new GameImage("CLOUD_SWEET", "images/cloudSweet.png");

	public static GameImage SUN = new GameImage("SUN", "images/sun.png");
	public static GameImage BACKGROUND = new GameImage("BACKGROUND", "images/background.png");
	public static GameImage FOREGROUND = new GameImage("FOREGROUND", "images/foreground.png");
	public static GameImage GROUND = new GameImage("GROUND", "images/ground.png");
	public static GameImage MIDDLEGROUND = new GameImage("MIDDLEGROUND", "images/middleground.png");
	public static GameImage MIDDLEGROUND2 = new GameImage("MIDDLEGROUND2", "images/middleground2.png");

	public static GameImage TARGET_ICON = new GameImage("TARGET_ICON", "images/targetIcon.png");
	public static GameImage MEME_MEDIUM = new GameImage("MEME_MEDIUM", "images/memeMedium.png");
	public static GameImage MEME_MEDIUM_75 = new GameImage("MEME_MEDIUM_75", "images/memeMedium75.png");
	public static GameImage MEME_MEDIUM_50 = new GameImage("MEME_MEDIUM_50", "images/memeMedium50.png");
	public static GameImage MEME_MEDIUM_25 = new GameImage("MEME_MEDIUM_25", "images/memeMedium25.png");
	public static GameImage MEME_MEDIUM_ICON = new GameImage("MEME_MEDIUM_ICON", "images/memeMediumIcon.png");

	public static GameImage MEME_LARGE = new GameImage("MEME_LARGE", "images/memeLarge.png");
	public static GameImage MEME_LARGE_75 = new GameImage("MEME_LARGE_75", "images/memeLarge75.png");
	public static GameImage MEME_LARGE_50 = new GameImage("MEME_LARGE_50", "images/memeLarge50.png");
	public static GameImage MEME_LARGE_25 = new GameImage("MEME_LARGE_25", "images/memeLarge25.png");
	public static GameImage MEME_LARGE_ICON = new GameImage("MEME_LARGE_ICON", "images/memeLargeIcon.png");

	public static GameImage MEME = new GameImage("MEME", "images/meme.png");
	public static GameImage MEME_CHALLENGE_ACCEPTED = new GameImage("MEME_CHALLENGE_ACCEPTED", "images/memeChallengeAccepted.png");
	public static GameImage MEME_CLOSE_ENOUGH = new GameImage("MEME_CLOSE_ENOUGH", "images/memeCloseEnough.png");
	public static GameImage MEME_FU = new GameImage("MEME_FU", "images/memeFU.png");
	public static GameImage MEME_OKAY = new GameImage("MEME_OKAY", "images/memeOkay.png");
	public static GameImage MEME_POKER = new GameImage("MEME_POKER", "images/memePoker.png");
	public static GameImage MEME_TROLL = new GameImage("MEME_TROLL", "images/memeTroll.png");



	public static GameImage SAND_PARTICLES = new GameImage("SAND_PARTICLES", "images/sandParticles.png");
	public static GameImage SMOKE_RING = new GameImage("SMOKE_RING", "images/smokeRing.png");

	public static GameImage MUD = new GameImage("MUD", "images/mud.png");
	public static GameImage MUD_ICON = new GameImage("MUD_ICON", "images/mudIcon.png");
	public static GameImage GLASS = new GameImage("GLASS", "images/glass.png");
	public static GameImage GLASS_25 = new GameImage("GLASS_25", "images/glass25.png");
	public static GameImage GLASS_50 = new GameImage("GLASS_50", "images/glass50.png");
	public static GameImage GLASS_75 = new GameImage("GLASS_75", "images/glass75.png");
	public static GameImage GLASS_ICON = new GameImage("GLASS_ICON", "images/glassIcon.png");
	public static GameImage STONE = new GameImage("STONE", "images/stone.png");
	public static GameImage STONE_25 = new GameImage("STONE_25", "images/stone25.png");
	public static GameImage STONE_50 = new GameImage("STONE_50", "images/stone50.png");
	public static GameImage STONE_75 = new GameImage("STONE_75", "images/stone75.png");
	public static GameImage STONE_ICON = new GameImage("STONE_ICON", "images/stoneIcon.png");
	public static GameImage STONE_PARTICLES = new GameImage("STONE_PARTICLES", "images/stoneParticles.png");
	public static GameImage WOOD = new GameImage("WOOD", "images/wood.png");
	public static GameImage WOOD_25 = new GameImage("WOOD_25", "images/wood25.png");
	public static GameImage WOOD_50 = new GameImage("WOOD_50", "images/wood50.png");
	public static GameImage WOOD_75 = new GameImage("WOOD_75", "images/wood75.png");
	public static GameImage WOOD_ICON = new GameImage("WOOD_ICON", "images/woodIcon.png");
	public static GameImage WOOD_PARTICLES = new GameImage("WOOD_PARTICLES", "images/woodParticles.png");


	public static GameImage BACK = new GameImage("BACK", "images/backIcon.png");
	public static GameImage LOCK = new GameImage("LOCK", "images/lock.png");
	public static GameImage UNLOCK = new GameImage("UNLOCK", "images/unlock.png");
	public static GameImage TEST = new GameImage("TEST", "images/test.png");
	public static GameImage TEST_DISABLED = new GameImage("TEST_DISABLED", "images/testDisabled.png");
	public static GameImage TRASH = new GameImage("TRASH", "images/trash.png");
	public static GameImage TRASH_DISABLED = new GameImage("TRASH_DISABLED", "images/trashDisabled.png");
	public static GameImage BOX = new GameImage("BOX", "images/box.png");
	public static GameImage CIRCLE = new GameImage("CIRCLE", "images/circle.png");
	public static GameImage EDIT = new GameImage("EDIT", "images/edit.png");
	public static GameImage EDITOR = new GameImage("EDITOR", "images/editor.png");
	public static GameImage EDITOR_DISABLED = new GameImage("EDITOR_DISABLED", "images/editorDisabled.png");
	public static GameImage POLYGON = new GameImage("POLYGON", "images/polygon.png");
	public static GameImage TRIANGLE = new GameImage("TRIANGLE", "images/triangle.png");
	public static GameImage RETRY = new GameImage("RETRY", "images/retry.png");

	public static GameImage GAME_END_BATMAN = new GameImage("GAME_END_BATMAN", "images/BATMAN.png");
	public static GameImage GAME_END_WTF = new GameImage("GAME_END_WTF", "images/WTF.png");
	public static GameImage GAME_END_WIN = new GameImage("GAME_END_WIN", "images/FullOfWin.png");
	public static GameImage POTATO = new GameImage("POTATO", "images/potato.png");

	public static GameImage TIP_PLAY_0 = new GameImage("TIP_PLAY_0", "images/tipCannonballBonus.png");
	public static GameImage TIP_PLAY_1 = new GameImage("TIP_PLAY_1", "images/tipCannon.png");

	public static GameSound CANNON_FIRE = new GameSound("CANNON_FIRE", "sounds/cannonFire", 1.0f, 1);
	public static GameSound CANNON_BALL_BONUS = new GameSound("CANNON_BALL_BONUS", "sounds/cannonBallBonus", 1.0f, 1);
	public static GameSound CANNON_BALL_DAMAGE = new GameSound("CANNON_BALL_DAMAGE", "sounds/cannonBallDamaged", 1.0f, 3);
	public static GameSound CANNON_BALL_MOVED = new GameSound("CANNON_BALL_MOVED", "sounds/cannonBallMoved", 1.0f, 1);
	public static GameSound OBJECT_WOOD_DAMAGE = new GameSound("OBJECT_WOOD_DAMAGE", "sounds/woodDamage", 1.0f, 3);
	public static GameSound OBJECT_WOOD_EXPLODE = new GameSound("OBJECT_WOOD_EXPLODE", "sounds/woodExplode", 1.0f, 3);
	public static GameSound OBJECT_STONE_DAMAGE = new GameSound("OBJECT_STONE_DAMAGE", "sounds/stoneDamage", 1.0f, 3);
	public static GameSound OBJECT_STONE_EXPLODE = new GameSound("OBJECT_STONE_EXPLODE", "sounds/stoneExplode", 1.0f, 3);
	public static GameSound OBJECT_GLASS_DAMAGE = new GameSound("OBJECT_GLASS_DAMAGE", "sounds/glassDamage", 1.0f, 3);
	public static GameSound OBJECT_GLASS_EXPLODE = new GameSound("OBJECT_GLASS_EXPLODE", "sounds/glassExplode", 1.0f, 3);
	public static GameSound SAND_POUF = new GameSound("SAND_POUF", "sounds/sandPouf", 1.0f, 5);
	public static GameSound OBJECT_ADDED = new GameSound("OBJECT_ADDED", "sounds/objectAdded", 1.0f, 1);
	public static GameSound OBJECTS_REMOVED = new GameSound("OBJECTS_REMOVED", "sounds/objectsRemoved", 1.0f, 1);

	public static GameMusic MUSIC_MENU = new GameMusic("MUSIC_MENU", "sounds/musicMenu", 0.5f);
	public static GameMusic MUSIC_EDIT = new GameMusic("MUSIC_EDIT", "sounds/musicEdit", 0.5f);


	public MemeDuelLoader(final AssetWatcher.Listener listener) {
		super(listener);
	}

}
