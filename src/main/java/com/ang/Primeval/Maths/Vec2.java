package com.ang.Primeval.Maths;

public class Vec2 {
	private double x = 0.0;
	private double y = 0.0;

	public Vec2() {}

	public Vec2 copy() {
		return new Vec2(x, y);

	}

	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() {
		return this.x;

	}

	public double y() {
		return this.y;

	}

	@Override
	public String toString() {
		return ("x: " + x + "\n"
				+ "y: " + y);

	}

	public Vec3 toVec3() {
		return new Vec3(x, y, 0.0);

	}	

	public void setAxis(int axis, double val) {
		switch (axis) {
		case 0:
			x = val;
			break;

		case 1:
			y = val;
			break;

		default:
			break;

		}
	}

	public Vec2 add(Vec2 v) {
		return new Vec2(this.x + v.x(), this.y + v.y());

	}

	public Vec2 sub(Vec2 v) {
		return new Vec2(this.x - v.x(), this.y - v.y());

	}

	public Vec2 mul(double t) {
		return new Vec2(this.x * t, this.y * t);

	}

	public Vec2 div(double t) {
		return mul(1 / t);

	}

	public Vec2 neg() {
		return new Vec2(-x, -y);

	}

	public double lengthSquared() {
		return (this.x * this.x) + (this.y * this.y);

	}

	public double length() {
		return Math.sqrt(lengthSquared());

	}

	public Vec2 unitVector() {
		return div(length());

	}

	public boolean nearZero() {
		double min = 1E-8;
		return (Math.abs(this.x) < min)	&& (Math.abs(this.y) < min);

	}

	public double axis(int axis) {
		switch (axis) {
		case 0:
			return x;

		case 1:
			return y;

		default:
			return 0.0;

		}
	}

	public static double cross(Vec2 u, Vec2 v) {
		return (u.x() * v.y()) - (u.y() * v.x());

	}

	public static double dot(Vec2 u, Vec2 v) {
		return (u.x() * v.x()) + (u.y() * v.y()); 

	}
}
