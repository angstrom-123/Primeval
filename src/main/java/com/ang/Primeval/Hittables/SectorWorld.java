package com.ang.Primeval.Hittables;

import com.ang.Primeval.Maths.*;
import com.ang.Primeval.Global;

public class SectorWorld {
	private Sector[] sectors;
	private int maxSectors;
	private int head = 0;

	public SectorWorld(int maxSectors) {
		this.maxSectors = maxSectors;
		sectors = new Sector[maxSectors];
	}
	
	public void addSector(Sector sec) {
		if (head < maxSectors) {
			sectors[head++] = sec;
		} else {
			System.out.println("Failed to add sector to world");
		}
	}

	public int maxSectors() {
		return maxSectors;

	}

	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		boolean didHit = false;
		double closestHit = Global.INFINITY;
		HitRecord tempRec = new HitRecord();
		for (int i = 0; i < head; i++) {
			Sector sec = sectors[i];
			Interval bounds = new Interval(tInterval.min(), closestHit);
			if (sec.hit(r, bounds, tempRec)) {
				if ((tempRec.t() >= 0.0) && (tempRec.t() < tInterval.max())) {
					didHit = true;
					closestHit = tempRec.t();
					rec.setT(tempRec.t());
					rec.setColour(tempRec.colour());
					rec.setFloor(tempRec.sectorFloor());
					rec.setCeiling(tempRec.sectorCeiling());
				}
			}
		}
		return didHit;

	}

	public HitRecord[] allHits(Ray r, Interval tInterval) {
		HitRecord[] hits = new HitRecord[100];
		int hitsHead = 0;
		for (int i = 0; i < head; i++) {
			Sector sec = sectors[i];
			for (HitRecord rec : sec.allHits(r, tInterval.copy())) {
				if ((rec.t() >= 0.0) & (rec.t() < tInterval.max())) {
					hits[hitsHead++] = rec;
				}
			}
		}
		return Global.reduceArray(hits, hitsHead);

	}

	@Override 
	public String toString() {
		String out = "";
		for (int i = 0; i < head; i++) {
			out += "(\n" + sectors[i].toString() + "\n)";
		}
		return out;

	}
}
