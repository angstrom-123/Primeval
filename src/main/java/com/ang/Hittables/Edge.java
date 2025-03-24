package com.ang.Hittables;

import com.ang.Maths.*;
import com.ang.Graphics.Colour;

public class Edge extends Hittable {
	private Vec2 p0;
	private Vec2 p1;
	private Interval x;
	private Interval y;
	private Colour albedo;

	public Edge(Vec2 p0, Vec2 p1, Colour albedo) {
		this.p0 = p0;
		this.p1 = p1;
		this.x = new Interval(Math.min(p0.x(), p1.x()), Math.max(p0.x(), p1.x()));
		this.y = new Interval(Math.min(p0.y(), p1.y()), Math.max(p0.y(), p1.y()));
		this.albedo = albedo;
	}

	public void flip() {
		Vec2 temp = p0.copy();;
		p0 = p1.copy();
		p1 = temp;
	}

	public Interval axisInterval(int axis) {
		switch (axis) {
		case 0:
			return x;

		case 1:
			return y;

		default:
			return Interval.empty();

		}
	}

	@Override
	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
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


		//double clippingDistance = 1000.0;
		//Vec2 q0 = r.origin().copy();
		//Vec2 q1 = r.at(clippingDistance).copy(); 
		//if (((p1.y() - p0.y()) / (p1.x() - p0.x())) < 0.0) {
		//	Vec2 temp = p1.copy();
		//	p1 = p0.copy();
		//	p1 = temp;
		//}
		//Vec3 l1 = new Vec3(p1.x() - p0.x(), q0.x() -q1.x(), q0.x() - p0.x());
		//Vec3 l2 = new Vec3(p1.y() - p0.y(), q0.y() -q1.y(), q0.y() - p0.y());
		//Vec2 pCoords = solve(l1, l2);
		//if ((pCoords.x() < 0.0) || (pCoords.x() > 1.0) 
		//		|| (pCoords.y() < 0.0) || (pCoords.y() > 1.0)) {
		//	return false;
		//
		//}
		//double t = clippingDistance * pCoords.y();
		//rec.setT(t);
		//rec.setColour(albedo);
		//tInterval.setMax(t);
		//return true;

	}

	//private Vec2 solve(Vec3 l1, Vec3 l2) {
	//	boolean tl = l1.x() == 0.0;
	//	boolean tr = l1.y() == 0.0;
	//	boolean bl = l2.x() == 0.0;
	//	boolean br = l2.y() == 0.0;
	//	if ((tl && bl) || (tr && br)) {
	//		return new Vec2(-1.0, -1.0);
	//
	//	}
	//	if (tl && br) {
	//		l1 = l1.div(l1.y());
	//		l2 = l2.div(l2.x());
	//		double y = l1.z();
	//		double x = l2.z() - (l2.y() * y);
	//		return new Vec2(x, y);
	//
	//	}
	//	if (tr && bl) {
	//		l1 = l1.div(l1.y());
	//		l2 = l2.div(l2.x());
	//		double y = l2.z();
	//		double x = l1.z() - (l1.y() * y);
	//		return new Vec2(x, y);
	//
	//	} 
	//	if (tl || br) {
	//		Vec3 temp = l1.copy();
	//		l1 = l2.copy();
	//		l2 = temp;
	//	}
	//	if (!tr && !tl && !br && !bl) {
	//		l2 = l2.sub(l1.mul(l2.x() / l1.x()));
	//		l1 = l1.sub(l2.mul(l1.y() / l2.y()));
	//	}
	//	// solve
	//	l1 = l1.div(l1.x());
	//	l2 = l2.div(l2.y());
	//	double y = l2.z();
	//	double x = l1.z() - (l1.y() * y);
	//	return new Vec2(x, y);
	//
	//}

	@Override
	public String toString() {
		return ("p0:\n" + p0.toString() + "\n" + "p1:\n" + p1.toString());

	}
}
