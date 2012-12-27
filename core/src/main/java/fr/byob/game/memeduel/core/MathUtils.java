package fr.byob.game.memeduel.core;

import java.util.Arrays;
import java.util.Comparator;

import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.common.Settings;

public class MathUtils extends fr.byob.game.box2d.common.MathUtils {
	/**
	 * Tourne le point par rapport à un angle
	 * 
	 * @public
	 */
	public static void rotatePointToOut(final float angle, final Vector pos, final Vector out) {
		final double cosAngle = Math.cos(angle);
		final double sinAngle = Math.sin(angle);

		// http://fr.wikipedia.org/wiki/Rotation_plane
		out.x = (float) (pos.x * cosAngle - pos.y * sinAngle);
		out.y = (float) (pos.x * sinAngle + pos.y * cosAngle);
	};

	/**
	 * Tourne le point par rapport à un centre et à un angle
	 * 
	 * @public
	 */
	public static void rotatePointToOut(final Vector center, final float angle, final Vector pos, final Vector out) {
		final Vector posToZero = GamePool.instance().popVector();
		posToZero.set(pos).subtractLocal(center);
		rotatePointToOut(angle, posToZero, out);
		out.addLocal(center);
	};

	public static float getArea(final Vector[] polygon, final int count) {
		float area = 0; // Accumulates area in the loop
		int j = count - 1; // The last vertex is the 'previous' one to the first

		for (int i = 0; i < count; i++) {
			area += abs((polygon[j].x * polygon[i].y) - (polygon[i].x * polygon[j].y));
			j = i;  //j is previous vertex to i
		}
		return area/2;
	};

	public static double computeAngleRadians(final Vector posA, final Vector posB) {
		final float nOpp = posA.x - posB.x; // Coté oposé
		final float nAdj = posB.y - posA.y; // COté adjacent
		float nTan = 0;
		if (nAdj != 0) {
			nTan = nOpp / nAdj;
		}
		double angleRaidans = Math.atan(nTan) + Math.PI / 2;
		if (nAdj > 0) {
			angleRaidans += Math.PI;
		}
		return angleRaidans;
	}

	public static void arrangeClockwise(final Vector[] polygon, final int size) {
		final Vector centerPos = GamePool.instance().popVector().set(0,0);

		for (int i = 0; i < size; i++) {
			final Vector point = polygon[i];
			centerPos.x += point.x;
			centerPos.y += point.y;
		}
		centerPos.x = centerPos.x / size;
		centerPos.y = centerPos.y / size;

		arrangeClockwise(polygon, size, centerPos);

		GamePool.instance().pushVector(1);
	};

	public static void arrangeClockwise(final Vector[] polygon, final int size, final Vector centerPos) {
		Arrays.sort(polygon, 0, size, new Comparator<Vector>() {
			@Override
			public int compare(final Vector a, final Vector b) {
				final double nAAngleRaidans = computeAngleRadians(a, centerPos);
				final double nBAngleRaidans = computeAngleRadians(b, centerPos);
				if (nAAngleRaidans < nBAngleRaidans) {
					return -1;
				} else {
					return 1;
				}
			}

		});
	};

	/**
	 * calcul le sens de l'angle ABC
	 */
	public static int counterClockWise(final Vector oPosA, final Vector oPosB, final Vector oPosC) {
		float dx1, dx2, dy1, dy2;

		dx1 = oPosB.x - oPosA.x;
		dy1 = oPosB.y - oPosA.y;
		dx2 = oPosC.x - oPosA.x;
		dy2 = oPosC.y - oPosA.y;

		if (dx1 * dy2 > dy1 * dx2) {
			return 1;
		}

		if (dx1 * dy2 < dy1 * dx2) {
			return -1;
		}

		if (dx1 * dx2 < 0.0 || dy1 * dy2 < 0.0) {
			return -1;
		}

		if (dx1 * dx1 + dy1 * dy1 < dx2 * dx2 + dy2 * dy2) {
			return 1;
		}

		return 0;
	}

	public static int toConvexPolygon(final Vector[] polygon, final int size) {
		int newSize = size;

		// Un triangle ne peut être concave
		if (size <= 3) {
			return size;
		}

		boolean bModified = false;
		do {
			bModified = false;
			for (int i = 0; i < newSize; i++) {
				Vector previous = null;
				final Vector current = polygon[i];
				Vector next = null;
				if (i == 0) {
					previous = polygon[newSize - 1];
				} else {
					previous = polygon[i - 1];
				}
				if (i == size - 1) {
					next = polygon[0];
				} else {
					next = polygon[i + 1];
				}

				final int nCC = counterClockWise(previous, current, next);

				// On supprime les points concaves et les doublons
				if (nCC == -1 || current.distance(next) <= Settings.EPSILON) {
					newSize = removeVertexFromPolygon(polygon, newSize, i);
					bModified = true;
				}

			}

		} while (bModified == true);

		return newSize;
	}

	public static boolean isConvexPolygon(final Vector[] polygon, final int size) {
		int newSize = size;

		// Un triangle ne peut être concave
		if (size <= 3) {
			return true;
		}

		for (int i = 0; i < newSize; i++) {
			Vector previous = null;
			final Vector current = polygon[i];
			Vector next = null;
			if (i == 0) {
				previous = polygon[newSize - 1];
			} else {
				previous = polygon[i - 1];
			}
			if (i == size - 1) {
				next = polygon[0];
			} else {
				next = polygon[i + 1];
			}

			final int nCC = counterClockWise(previous, current, next);

			// On supprime les points concaves et les doublons
			if (nCC == -1 || current.distance(next) <= Settings.EPSILON) {
				newSize = removeVertexFromPolygon(polygon, newSize, i);
				return false;
			}

		}
		return true;
	}

	public static Vector circleCenter(final Vector A, final Vector B, final Vector C) {
		final float yDelta_a = B.y - A.y;
		final float xDelta_a = B.x - A.x;
		final float yDelta_b = C.y - B.y;
		final float xDelta_b = C.x - B.x;
		final Vector center = tmp();

		final float aSlope = yDelta_a / xDelta_a;
		final float bSlope = yDelta_b / xDelta_b;
		center.x = (aSlope * bSlope * (A.y - C.y) + bSlope * (A.x + B.x) - aSlope * (B.x + C.x)) / (2 * (bSlope - aSlope));
		center.y = -1 * (center.x - (A.x + B.x) / 2) / aSlope + (A.y + B.y) / 2;

		return center;
	}

	private final static Dimension tmpDim = new Dimension();

	public final static Dimension tmpDim() {
		tmpDim.setSize(0, 0);
		return tmpDim;
	}

	public static void fillPolygon(final Vector[] polygon) {
		for (int i = 0; i < polygon.length; i++) {
			polygon[i] = new Vector();
		}
	}

	public static void resetPolygon(final Vector[] polygon) {
		for (final Vector vertice : polygon) {
			vertice.set(0, 0);
		}
	}

	public static Vector[] copyPolygon(final Vector[] from, final int count) {
		final Vector[] copy = new Vector[count];
		for (int i = 0; i < count; i++) {
			copy[i] = new Vector(from[i]);
		}
		return copy;
	}

	public static int copyPolygon(final Vector[] from, final Vector[] to) {
		for (int i = 0; i < from.length && i < to.length; i++) {
			to[i].set(from[i]);
		}
		return Math.min(from.length, to.length);
	}

	public static void copyPolygon(final Vector[] from, final int fromSize, final Vector[] to) {
		for (int i = 0; i < fromSize; i++) {
			to[i].set(from[i]);
		}
	}

	public static int removeVertexFromPolygon(final Vector[] polygon, final int size, final int vertexIndex) {
		for (int i = vertexIndex; i < size - 1; i++) {
			polygon[i].set(polygon[i + 1]);
		}
		polygon[size - 1].set(0, 0);
		return size - 1;
	}
}
