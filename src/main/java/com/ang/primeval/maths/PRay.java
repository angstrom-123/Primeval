package com.ang.primeval.maths;

public class PRay {
	private PVec2 origin;
	private PVec2 direction;

	public PRay(PVec2 origin, PVec2 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public PVec2 origin() {
		return origin;

	}

	public PVec2 direction() {
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

	public PVec2 at(double t) {
		return origin.add(direction.mul(t));

	}
}
