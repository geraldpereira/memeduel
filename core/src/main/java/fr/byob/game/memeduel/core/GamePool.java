package fr.byob.game.memeduel.core;

import pythagoras.f.Dimension;
import pythagoras.f.Vector;
import fr.byob.game.box2d.pooling.IOrderedStack;
import fr.byob.game.box2d.pooling.OrderedStack;
import fr.byob.game.box2d.pooling.OrderedStackVector;

public class GamePool {

	private final static GamePool instance = new GamePool();

	public final static Vector ZERO_VECTOR = new Vector(0, 0);

	private final IOrderedStack<Vector> vectors = new OrderedStackVector(1000);
	private final IOrderedStack<Dimension> dimensions = new OrderedStackDimension(200);

	private GamePool() {
	}

	public static GamePool instance() {
		return instance;
	}

	public Vector popVector() {
		return vectors.pop();
	}

	public Vector[] popVectors(final int argNum) {
		return vectors.pop(argNum);
	}

	public void pushVector(final int argNum) {
		vectors.push(argNum);
	}

	public Dimension popDimension() {
		return dimensions.pop();
	}

	public Dimension[] popDimensions(final int argNum) {
		return dimensions.pop(argNum);
	}

	public void pushDimension(final int argNum) {
		dimensions.push(argNum);
	}

	private static class OrderedStackDimension extends OrderedStack<Dimension> {

		public OrderedStackDimension(final int argStackSize) {
			super(argStackSize);
		}

		@Override
		protected Dimension[] newArray(final int argSize) {
			return new Dimension[argSize];
		}

		@Override
		protected Dimension newObject() {
			return new Dimension();
		}
	}
}
