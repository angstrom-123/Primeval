package com.ang.Hittables;

import com.ang.Maths.*;
import com.ang.Graphics.Colour;

public class Cube extends Hittable {
	private Interval x;
	private Interval y;
	private Interval z;
	private Colour albedo;

	public Cube(Vec3 position, double size, Colour albedo) {
		this.x = new Interval(position.x() - (size / 2), 
				position.x() + (size / 2));
		this.y = new Interval(position.y() - (size / 2), 
				position.y() + (size / 2));
		this.z = new Interval(position.z() - (size / 2), 
				position.z() + (size / 2));
		this.albedo = albedo;
	}

	public Interval axisInterval(int axisIndex) {
		switch (axisIndex) {
		case 0:
			return x;
		
		case 1:
			return y;

		case 2:
			return z;

		default:
			return Interval.empty();

		}
	}

	@Override
	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		for (int axis = 0; axis < 2; axis++) {
			Interval axisInterval = axisInterval(axis);
			double rayAxisInverse = 1.0 / r.axis(axis); 
			double low = (axisInterval.min() - r.origin().axis(axis)) 
					* rayAxisInverse;
			double high = (axisInterval.max() - r.origin().axis(axis)) 
					* rayAxisInverse;
			if (low < high) {
				if (low > tInterval.min()) {
					tInterval.setMin(low);
				}
				if (high < tInterval.max()) {
					tInterval.setMax(high);
				}
			} else {
				if (high > tInterval.min()) {
					tInterval.setMin(high);
				}
				if (low < tInterval.max()) {
					tInterval.setMax(low);
				}
			}
			if (tInterval.max() < tInterval.min()) {
				return false;

			}
		}
		Vec2 pLow = r.origin().add((r.direction().mul(tInterval.min())));
		Vec2 pHigh = r.origin().add((r.direction().mul(tInterval.max())));
		double pLowDist = pLow.sub(r.origin()).lengthSquared();
		double pHighDist = pHigh.sub(r.origin()).lengthSquared();
		if (pLowDist < pHighDist) {
			rec.setT(tInterval.min());
		} else {
			rec.setT(tInterval.max());
		}
		rec.setColour(albedo);
		return true;

	}

	@Override
	public String toString() {
		return (x.toString() + "\n"
				+ y.toString() + "\n"
				+ z.toString());

	}
}
