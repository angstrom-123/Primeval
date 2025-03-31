package com.ang.Primeval.Core.Hittables;

import com.ang.Primeval.Core.Maths.*;
import com.ang.Primeval.Graphics.Colour;

public class Edge {
	private Vec2 p0;
	private Vec2 p1;
	private Colour albedo;
	private boolean portal;

	public Edge(Vec2 p0, Vec2 p1, Colour albedo) {
		this.p0 = p0;
		this.p1 = p1;
		this.albedo = albedo;
	}

	public Vec2 p0() {
		return p0;

	}

	public Vec2 p1() {
		return p1;

	}
	
	public void setAsPortal() {
		portal = true;
	}

	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		if (portal) {
			return false;

		}
		Vec2 v1 = r.origin().sub(p0);
		Vec2 v2 = p1.sub(p0);
		Vec2 v3 = new Vec2(-r.direction().y(), r.direction().x());
		double t1 = Vec2.cross(v2, v1) / Vec2.dot(v2, v3);
		double t2 = Vec2.dot(v1, v3) / Vec2.dot(v2, v3);
		if ((t1 >= 0.0) && (t2 >= 0.0) && (t2 <= 1.0)) {
			if (t1 < tInterval.max()) {
				tInterval.setMax(t1);
				rec.setT(t1);
				rec.setColour(albedo);
				return true;

			}
		}
		return false;

	}

	@Override
	public String toString() {
		return ("p0:\n" + p0.toString() + "\n" + "p1:\n" + p1.toString());

	}
}
