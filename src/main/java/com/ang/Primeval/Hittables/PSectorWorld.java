package com.ang.primeval.hittables;

import com.ang.primeval.maths.*;
import com.ang.primeval.PGlobal;
import com.ang.primeval.utils.PArrays;

public class PSectorWorld {
	private PSector[] sectors;
	private int maxSectors;
	private int head = 0;

	public PSectorWorld(int maxSectors) {
		this.maxSectors = maxSectors;
		sectors = new PSector[maxSectors];
	}
	
	public void addSector(PSector sec) {
		if (head < maxSectors) {
			sectors[head++] = sec;
		} else {
			System.out.println("Failed to add sector to world");
		}
	}

	public int maxSectors() {
		return maxSectors;

	}

	public PSector[] sectors() {
		return PArrays.reduceArray(sectors, head);

	}

	public boolean hit(PRay r, PInterval tInterval, PHitRecord rec) {
		boolean didHit = false;
		double closestHit = PGlobal.INFINITY;
		PHitRecord tempRec = new PHitRecord();
		for (int i = 0; i < head; i++) {
			PSector sec = sectors[i];
			PInterval bounds = new PInterval(tInterval.min(), closestHit);
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

	public PHitRecord[] allHits(PRay r, PInterval tInterval) {
		PHitRecord[] hits = new PHitRecord[100];
		int hitsHead = 0;
		for (int i = 0; i < head; i++) {
			PSector sec = sectors[i];
			for (PHitRecord rec : sec.allHits(r, tInterval.copy())) {
				if ((rec.t() >= 0.0) & (rec.t() < tInterval.max())) {
					hits[hitsHead++] = rec;
				}
			}
		}
		return PArrays.reduceArray(hits, hitsHead);

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
