package com.ang.Primeval.Maths;

public class Ray {
	private Vec2 origin;
	private Vec2 direction;

	public Ray(Vec2 origin, Vec2 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Vec2 origin() {
		return origin;

	}

	public Vec2 direction() {
		return direction;

	}

	public double axis(int axis) {
		switch (axis) {
		case 0:
			return direction.x();

		case 1:
			return direction.y();

		default:
			return 0.0;

		}
	}

	public Vec2 at(double t) {
		return origin.add(direction.mul(t));

	}
}
