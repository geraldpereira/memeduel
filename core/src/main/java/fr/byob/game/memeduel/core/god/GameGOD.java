package fr.byob.game.memeduel.core.god;

import java.util.ArrayList;
import java.util.List;

import playn.core.Json;
import playn.core.Json.Object;
import pythagoras.f.Vector;
import fr.byob.game.memeduel.core.god.builder.SunBuilder;

public class GameGOD extends GODLoadable<GameGOD> implements GameObjectDefinition {

	private float minNormalImpulseSmoke = 1.0f;
	private float maxDrawScale = 1.0f;
	private float minDrawScale = 0.4f;
	private float maxAutofocusDrawScale = 0.8f;
	private float minAutofocusDrawScale = 0.4f;
	private float minPolygonWidth = 0.4f;
	private float maxPolygonWidth = 3.0f;
	private float minPolygonHeight = 0.2f;
	private float maxPolygonHeight = 3.0f;
	private float minPolygonRadius = 0.15f;
	private float maxPolygonRadius = 1.5f;

	private float worldWallWidth = 0.5f;

	private float sizeMinCircleFragment = 0.05f;
	private float sizeMinFragment = 0.05f;
	private float sizeMaxFragment = 0.25f;
	private float fragmentDecay = 0.000001f;
	private float fragmentDuration = 1.5f;

	private int explosionCuts = 6;

	private int maxPolygonVertices = 8;

	private final Vector gravity = new Vector(0.0f, 10.0f);

	protected WorldGOD world;
	protected List<LandscapeGOD> landscapes;
	protected List<CloudGOD> clouds;

	protected StaticGameObjectDefinition sun;
	protected SunGlowGOD sunGlow;

	private final AllGODLoader godLoader;

	public GameGOD(final AllGODLoader godLoader) {
		this.godLoader = godLoader;
	}

	@Override
	protected GameGOD loadFromJSON(final Object jsonEntity) {
		// parse the world
		if (jsonEntity.containsKey("minNormalImpulseSmoke")) {
			this.minNormalImpulseSmoke = jsonEntity.getNumber("minNormalImpulseSmoke");
		}
		if (jsonEntity.containsKey("maxDrawScale")) {
			this.maxDrawScale = jsonEntity.getNumber("maxDrawScale");
		}
		if (jsonEntity.containsKey("minDrawScale")) {
			this.minDrawScale = jsonEntity.getNumber("minDrawScale");
		}
		if (jsonEntity.containsKey("maxAutofocusDrawScale")) {
			this.maxAutofocusDrawScale = jsonEntity.getNumber("maxAutofocusDrawScale");
		}
		if (jsonEntity.containsKey("minAutofocusDrawScale")) {
			this.minAutofocusDrawScale = jsonEntity.getNumber("minAutofocusDrawScale");
		}
		if (jsonEntity.containsKey("minPolygonWidth")) {
			this.minPolygonWidth = jsonEntity.getNumber("minPolygonWidth");
		}
		if (jsonEntity.containsKey("maxPolygonWidth")) {
			this.maxPolygonWidth = jsonEntity.getNumber("maxPolygonWidth");
		}
		if (jsonEntity.containsKey("minPolygonHeight")) {
			this.minPolygonHeight = jsonEntity.getNumber("minPolygonHeight");
		}
		if (jsonEntity.containsKey("maxPolygonHeight")) {
			this.maxPolygonHeight = jsonEntity.getNumber("maxPolygonHeight");
		}
		if (jsonEntity.containsKey("minPolygonRadius")) {
			this.minPolygonRadius = jsonEntity.getNumber("minPolygonRadius");
		}
		if (jsonEntity.containsKey("maxPolygonRadius")) {
			this.maxPolygonRadius = jsonEntity.getNumber("maxPolygonRadius");
		}

		if (jsonEntity.containsKey("worldWallWidth")) {
			this.worldWallWidth = jsonEntity.getNumber("worldWallWidth");
		}

		if (jsonEntity.containsKey("sizeMinCircleFragment")) {
			this.sizeMinCircleFragment = jsonEntity.getNumber("sizeMinCircleFragment");
		}
		if (jsonEntity.containsKey("sizeMinFragment")) {
			this.sizeMinFragment = jsonEntity.getNumber("sizeMinFragment");
		}
		if (jsonEntity.containsKey("sizeMaxFragment")) {
			this.sizeMaxFragment = jsonEntity.getNumber("sizeMaxFragment");
		}
		if (jsonEntity.containsKey("fragmentDecay")) {
			this.fragmentDecay = jsonEntity.getNumber("fragmentDecay");
		}
		if (jsonEntity.containsKey("fragmentDuration")) {
			this.fragmentDuration = jsonEntity.getNumber("fragmentDuration");
		}

		if (jsonEntity.containsKey("explosionCuts")) {
			this.explosionCuts = jsonEntity.getInt("explosionCuts");
		}

		if (jsonEntity.containsKey("maxPolygonVertices")) {
			this.maxPolygonVertices = jsonEntity.getInt("maxPolygonVertices");
		}
		// Sets the defaut maximum number of vertices for a polygon
		// TODO Handle the maximum number of polygon vertices in the
		// box2D-android
		// Settings.maxPolygonVertices = this.maxPolygonVertices;

		if (jsonEntity.containsKey("gravity")) {
			final Object gravityJSON = jsonEntity.getObject("gravity");
			final float x = gravityJSON.getNumber("x");
			final float y = gravityJSON.getNumber("y");
			this.gravity.set(x, y);
		}

		final Json.Object worldEntity = jsonEntity.getObject("world");
		this.world = WorldGOD.worldBuilder().fromJSON(godLoader, worldEntity).build();

		final Json.Array landscapesEntities = jsonEntity.getArray("landscapes");
		this.landscapes = new ArrayList<LandscapeGOD>(landscapesEntities.length());
		for (int i = 0; i < landscapesEntities.length(); i++) {
			final Json.Object landscapeEntity = landscapesEntities.getObject(i);
			final LandscapeGOD landspace = new LandscapeGOD.Builder().fromJSON(godLoader, landscapeEntity).build();
			this.landscapes.add(landspace);
		}

		final Json.Array cloudEntities = jsonEntity.getArray("clouds");
		this.clouds = new ArrayList<CloudGOD>(cloudEntities.length());
		for (int i = 0; i < cloudEntities.length(); i++) {
			final Json.Object cloudEntity = cloudEntities.getObject(i);
			final CloudGOD cloud = new CloudGOD.Builder().fromJSON(godLoader, cloudEntity).build();
			this.clouds.add(cloud);
		}

		final Json.Object sunEntity = jsonEntity.getObject("sun");
		this.sun = new SunBuilder().fromJSON(godLoader, sunEntity).build();
		this.sunGlow = new SunGlowGOD.Builder().fromJSON(godLoader, sunEntity).build();
		return this;
	}

	public List<LandscapeGOD> getLandscapes() {
		return this.landscapes;
	}

	public List<CloudGOD> getClouds() {
		return this.clouds;
	}

	public StaticGameObjectDefinition getSun() {
		return this.sun;
	}

	public SunGlowGOD getSunGlow() {
		return this.sunGlow;
	}

	public float getMinNormalImpulseSmoke() {
		return this.minNormalImpulseSmoke;
	}

	public float getMaxDrawScale() {
		return this.maxDrawScale;
	}

	public float getMinDrawScale() {
		return this.minDrawScale;
	}

	public float getMaxAutofocusDrawScale() {
		return this.maxAutofocusDrawScale;
	}

	public float getMinAutofocusDrawScale() {
		return this.minAutofocusDrawScale;
	}

	public float getMinPolygonWidth() {
		return this.minPolygonWidth;
	}

	public float getMaxPolygonWidth() {
		return this.maxPolygonWidth;
	}

	public float getMinPolygonHeight() {
		return this.minPolygonHeight;
	}

	public float getMaxPolygonHeight() {
		return this.maxPolygonHeight;
	}

	public float getMinPolygonRadius() {
		return this.minPolygonRadius;
	}

	public float getMaxPolygonRadius() {
		return this.maxPolygonRadius;
	}

	public float getWorldWallWidth() {
		return this.worldWallWidth;
	}

	public float getSizeMinCircleFragment() {
		return this.sizeMinCircleFragment;
	}

	public float getSizeMinFragment() {
		return this.sizeMinFragment;
	}

	public float getSizeMaxFragment() {
		return this.sizeMaxFragment;
	}

	public float getFragmentDecay() {
		return this.fragmentDecay;
	}

	public int getMaxPolygonVertices() {
		return this.maxPolygonVertices;
	}

	public WorldGOD getWorld() {
		return this.world;
	}

	public float getFragmentDuration() {
		return this.fragmentDuration;
	}

	public int getExplosionCuts() {
		return this.explosionCuts;
	}

	public Vector getGravity() {
		return this.gravity;
	}

}