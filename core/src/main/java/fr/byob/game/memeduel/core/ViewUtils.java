package fr.byob.game.memeduel.core;

import playn.core.Color;
import pythagoras.f.Dimension;
import pythagoras.f.Vector;

public class ViewUtils {
	public static float toInitModel(final float modelValue) {
		return modelValue / ViewUtils.initDrawScale * ViewUtils.physUnitPerScreenUnit;
	}

	public static float toInitView(final float modelValue) {
		return modelValue * ViewUtils.initDrawScale / ViewUtils.physUnitPerScreenUnit;
	}

	public static Vector modelToInitView(final Vector modelValue, final Vector viewValue) {
		return viewValue.set(toInitView(modelValue.x), toInitView(modelValue.y));
	}

	public static Dimension modelToInitView(final Dimension modelValue, final Dimension viewValue) {
		viewValue.setSize(toInitView(modelValue.width), toInitView(modelValue.height));
		return viewValue;
	}

	public static float getInitPhysUnitPerScreenUnit() {
		return ViewUtils.initDrawScale / ViewUtils.physUnitPerScreenUnit;
	}

	public final static float initDrawScale = 1.0f;
	public final static float physUnitPerScreenUnit = 1 / 50f;

	public static int getColorRedToGreen(final float percentage) {
		// évolution constante du rouge de 200 à 0
		// évolution constante du vert de 0 à 200
		final int red = (int) (200 - percentage * 200);
		final int green = (int) (percentage * 200);
		final int blue = 0;
		return Color.rgb(red, green, blue);
	}

	public static int getColorGreenToRed(final float percentage) {
		return getColorRedToGreen(1 - percentage);
	}

	public static int getColorRedYelloGreen(final float percentage) {
		// évolution constante du rouge de 200 à 0
		// évolution constante du vert de 0 à 200
		int red = 0;
		int green = 0;
		final int blue = 0;
		if (percentage <= 0.5f) {
			// De 0 à 50 % (rouge vers jaune) :
			// évolution constante du vert de 0 à 253
			// pas d'évolution du rouge
			red = 254;
			green = (int) (100 * percentage / 50 * 253); // De 0 a 253
		} else {
			// De 50 à 100 % (jaune vers vert) :
			// évolution constante du vert de 253 à 214
			// évolution constante du rouge de 254 à 86
			red = (int) (254 - (100 * percentage - 50) / 50 * 168); // De 254 a
			// 86 evo de
			// 168
			green = (int) (253 - (100 * percentage - 50) / 50 * 39); // De 253 a
			// 214
			// evo
			// de 39
		}
		return Color.rgb(red, green, blue);
	}

	public static int getColorGreenYelloRed(final float percentage) {
		return getColorRedYelloGreen(1 - percentage);
	}

}
