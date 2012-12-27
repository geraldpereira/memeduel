package fr.byob.game.memeduel.core.view;


public class Color {
	private final int alpha, red, green, blue;

	public Color(final String colorStr) {
		alpha = Integer.valueOf(colorStr.substring(0, 2), 16);
		red = Integer.valueOf(colorStr.substring(2, 4), 16);
		green = Integer.valueOf(colorStr.substring(4, 6), 16);
		blue = Integer.valueOf(colorStr.substring(6, 8), 16);
	}

	protected Color(final int alpha, final int red, final int green, final int blue) {
		this.alpha = alpha;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int asInt() {
		return playn.core.Color.argb(alpha, red, green, blue);
	}

	private static final double FACTOR = 0.7;

	public Color brighter() {
		int r = red;
		int g = green;
		int b = blue;

		final int i = (int) (1.0 / (1.0 - FACTOR));
		if (r == 0 && g == 0 && b == 0) {
			return new Color(alpha, i, i, i);
		}
		if (r > 0 && r < i) {
			r = i;
		}
		if (g > 0 && g < i) {
			g = i;
		}
		if (b > 0 && b < i) {
			b = i;
		}

		return new Color(alpha, Math.min((int) (r / FACTOR), 255), Math.min((int) (g / FACTOR), 255), Math.min((int) (b / FACTOR), 255));
	}

	public Color darker() {
		return new Color(alpha, Math.max((int) (red * FACTOR), 0), Math.max((int) (green * FACTOR), 0), Math.max((int) (blue * FACTOR), 0));
	}
}
