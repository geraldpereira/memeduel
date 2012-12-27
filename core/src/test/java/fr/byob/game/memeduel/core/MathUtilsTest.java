package fr.byob.game.memeduel.core;

import junit.framework.Assert;

import org.junit.Test;

import pythagoras.f.Vector;

public class MathUtilsTest {

	@Test
	public void clockwise() {
		final Vector[] polygon = new Vector[4];
		MathUtils.fillPolygon(polygon);

		polygon[0].set(-1, -1);
		polygon[1].set(1, 1);
		polygon[2].set(-1, 1);
		polygon[3].set(1, -1);

		MathUtils.arrangeClockwise(polygon, 4);

		final Vector[] result = new Vector[4];
		MathUtils.fillPolygon(result);

		result[0].set(1, 1);
		result[1].set(-1, 1);
		result[2].set(-1, -1);
		result[3].set(1, -1);

		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(result[i], polygon[i]);
		}
	}

	@Test
	public void clockwise2() {
		final Vector[] polygon = new Vector[4];
		MathUtils.fillPolygon(polygon);

		polygon[0].set(1, 1);
		polygon[1].set(-1, 1);
		polygon[2].set(1, -1);
		polygon[3].set(-1, -1);

		MathUtils.arrangeClockwise(polygon, 4);

		final Vector[] result = new Vector[4];
		MathUtils.fillPolygon(result);

		result[0].set(1, 1);
		result[1].set(-1, 1);
		result[2].set(-1, -1);
		result[3].set(1, -1);

		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(result[i], polygon[i]);
		}
	}

	@Test
	public void convex1() {
		final Vector[] polygon = new Vector[4];
		MathUtils.fillPolygon(polygon);

		polygon[0].set(-0.25f, -0.25f);
		polygon[1].set(-1, 1);
		polygon[2].set(-1, -1);
		polygon[3].set(1, -1);

		final int size = MathUtils.toConvexPolygon(polygon, 4);

		Assert.assertEquals(3, size);

		final Vector[] result = new Vector[3];
		MathUtils.fillPolygon(result);

		result[0].set(-1, 1);
		result[1].set(-1, -1);
		result[2].set(1, -1);

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(polygon[i], result[i]);
		}
	}

	@Test
	public void convex2() {
		final Vector[] polygon = new Vector[4];
		MathUtils.fillPolygon(polygon);

		polygon[0].set(1, 1);
		polygon[1].set(0.25f, -0.25f);
		polygon[2].set(-1, -1);
		polygon[3].set(1, -1);

		final int size = MathUtils.toConvexPolygon(polygon, 4);

		Assert.assertEquals(3, size);

		final Vector[] result = new Vector[3];
		MathUtils.fillPolygon(result);

		result[0].set(1, 1);
		result[1].set(-1, -1);
		result[2].set(1, -1);

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(polygon[i], result[i]);
		}
	}

	@Test
	public void convex3() {
		final Vector[] polygon = new Vector[8];
		MathUtils.fillPolygon(polygon);

		//		[-0.187+0.346, -0.187+0.346, -0.015-0.386, +0.535-0.386, -0.145+0.08, +0.0+0.0, +0.0+0.0, +0.0+0.0]
		polygon[0].set(-0.187f, 0.346f);
		polygon[1].set(-0.187f, 0.346f);
		polygon[2].set(-0.015f, -0.386f);
		polygon[3].set(0.535f, -0.386f);
		polygon[4].set(-0.145f, 0.08f);
		polygon[5].set(0, 0);
		polygon[6].set(0, 0);
		polygon[7].set(0, 0);

		final int size = MathUtils.toConvexPolygon(polygon, 5);

		Assert.assertEquals(3, size);

		final Vector[] result = new Vector[3];
		MathUtils.fillPolygon(result);
		result[0].set(-0.187f, 0.346f);
		result[1].set(-0.015f, -0.386f);
		result[2].set(0.535f, -0.386f);

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(polygon[i], result[i]);
		}
	}



	@Test
	public void convex4() {
		final Vector[] polygon = new Vector[5];
		MathUtils.fillPolygon(polygon);

		// [+0.553+1.86, -0.581-0.93, +0.028-0.93, +0.028-0.93, +0.028-0.93]
		polygon[0].set(0.553f, 1.86f);
		polygon[1].set(-0.581f, -0.93f);
		polygon[2].set(0.028f, -0.93f);
		polygon[3].set(0.028f, -0.93f);
		polygon[4].set(0.028f, -0.93f);

		final int size = MathUtils.toConvexPolygon(polygon, 5);

		Assert.assertEquals(3, size);

		final Vector[] result = new Vector[3];
		MathUtils.fillPolygon(result);
		result[0].set(0.553f, 1.86f);
		result[1].set(-0.581f, -0.93f);
		result[2].set(0.028f, -0.93f);

		for (int i = 0; i < 3; i++) {
			Assert.assertEquals(polygon[i], result[i]);
		}
	}

	@Test
	public void convex5() {
		final Vector[] polygon = new Vector[5];
		MathUtils.fillPolygon(polygon);

		// [+0.235+0.579, -0.47-0.289, -0.47-0.289, -0.47-0.289, +0.235-0.289]
		polygon[0].set(0.235f, 0.579f);
		polygon[1].set(-0.47f, -0.289f);
		polygon[2].set(-0.47f, -0.289f);
		polygon[3].set(-0.47f, -0.289f);
		polygon[4].set(0.235f, -0.289f);

		final int size = MathUtils.toConvexPolygon(polygon, 5);

		Assert.assertEquals(3, size);

		final Vector[] result = new Vector[5];
		MathUtils.fillPolygon(result);
		result[0].set(0.235f, 0.579f);
		result[1].set(-0.47f, -0.289f);
		result[2].set(0.235f, -0.289f);
		result[3].set(0, 0);
		result[4].set(0, 0);

		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(polygon[i], result[i]);
		}
	}

	@Test
	public void area() {
		final Vector[] polygon = new Vector[4];
		MathUtils.fillPolygon(polygon);

		polygon[0].set(1, 1);
		polygon[1].set(-1, 1);
		polygon[2].set(-1, -1);
		polygon[3].set(1, -1);


		Assert.assertEquals(4.0f, MathUtils.getArea(polygon, 4));
	}

}
