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
						if ((tempRec.sectorFloor() != 0.0) 
								&& (tempRec.sectorCeiling() != 0.0)) {
							rec.setFloor(tempRec.sectorFloor());
							rec.setCeiling(tempRec.sectorCeiling());
						}
					}
				}
			}
		}
		return didHit;

	}

	public HitRecord[] allHits(Ray r, Interval tInterval) {
		HitRecord[] hits = new HitRecord[100];
		int hitsHead = 0;
		for (int i = 0; i < head; i++) {
			Hittable h = hittables[i];
			if (h instanceof HittableList) {
				HittableList hl = (HittableList) h;
				for (HitRecord rec : hl.allHits(r, tInterval.copy())) {
					if ((rec.t() >= 0.0) & (rec.t() < tInterval.max())) {
						if (hl instanceof Sector) {
							Sector sec = (Sector) hl;
							rec.setFloor(sec.floorHeight());
							rec.setCeiling(sec.ceilingHeight());
						}
						hits[hitsHead++] = rec.copy();
					}
				}
			} else {
				HitRecord tempRec = new HitRecord();
				if (h.hit(r, tInterval.copy(), tempRec)) {
					if ((tempRec.t() >= 0.0) && (tempRec.t() < tInterval.max())) {
						hits[hitsHead++] = tempRec;
					}
				}
			}
		}
		return Global.reduceArray(hits, hitsHead);

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
