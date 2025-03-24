package com.ang.Hittables;

import com.ang.Maths.*;
import com.ang.Global;

public class HittableList extends Hittable {
	private Hittable[] hittables;
	private int maxHittables;
	private int head = 0;

	public HittableList(int maxHittables) {
		this.maxHittables = maxHittables;
		hittables = new Hittable[maxHittables];
	}
	
	public void addHittable(Hittable h) {
		if (head < maxHittables) {
			hittables[head++] = h;
		} else {
			System.out.println("Failed to add hittable to list");
		}
	}

	public int maxHittables() {
		return maxHittables;

	}

	@Override
	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		boolean didHit = false;
		double closestHit = Global.INFINITY;
		HitRecord tempRec = new HitRecord();
		for (int i = 0; i < head; i++) {
			Hittable h = hittables[i];
			Interval bounds = new Interval(tInterval.min(), closestHit);
			if (h.hit(r, bounds, tempRec)) {
				if (tempRec.t() >= 0.0) {
					if (tempRec.t() < tInterval.max()) {
						didHit = true;
						closestHit = tempRec.t();
						rec.setT(tempRec.t());
						rec.setColour(tempRec.colour());
					}
				}
			}
		}
		return didHit;

	}

	@Override 
	public String toString() {
		String out = "";
		for (int i = 0; i < head; i++) {
			out += "(\n" + hittables[i].toString() + "\n)";
		}
		return out;

	}
}
