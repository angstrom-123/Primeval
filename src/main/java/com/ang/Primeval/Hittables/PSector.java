package com.ang.primeval.hittables;

import com.ang.primeval.PGlobal;
import com.ang.primeval.utils.PArrays;
import com.ang.primeval.graphics.PColour;
import com.ang.primeval.maths.*;

public class PSector {
	private PEdge[] walls;
	private PVec2[] corners;
	private double floorHeight;
	private double ceilingHeight;
	// TODO: implement light level
	private double lightLevel = 1.0; 

	public PSector(PVec2[] corners, int[] portalIndices) {
		this.corners = corners;
		walls = new PEdge[corners.length];
		int head = 0;
		for (int i = 0; i < corners.length; i++) {
			PEdge wall;
			int nextI = (i < corners.length - 1) ? i + 1 : 0;
			wall = new PEdge(corners[i], corners[nextI], new PColour(1.0, 1.0, 1.0));
			if (isPortal(i, nextI, portalIndices)) {
				wall.setAsPortal();
			}
			walls[head++] = wall;
		}
	}

	public void setHeight(double floorHeight, double ceilingHeight) {
		this.floorHeight = floorHeight;
		this.ceilingHeight = ceilingHeight;
	}

	public void setLightLevel(double lightLevel) {
		this.lightLevel = lightLevel;
	}

	public double floorHeight() {
		return floorHeight;

	}

	public double ceilingHeight() {
		return ceilingHeight;

	}

	public double lightLevel() {
		return lightLevel;

	}

	public PEdge[] walls() {
		return walls;

	}

	public PVec2[] corners() {
		return corners;

	}

	private boolean isPortal(int indexOne, int indexTwo, int[] portalIndices) {
		boolean foundOne = false;
		boolean foundTwo = false;
		for (int i : portalIndices) {
			if (!foundOne || !foundTwo) {
				if (i == indexOne) {
					foundOne = true;
				}
				if (i == indexTwo) {
					foundTwo = true;
				}
			}
		}
		return foundOne && foundTwo;

	}

	public boolean hit(PRay r, PInterval tInterval, PHitRecord rec) {
		boolean didHit = false;
		double closestHit = PGlobal.INFINITY;
		PHitRecord tempRec = new PHitRecord();
		for (int i = 0; i < walls.length; i++) {
			PEdge w = walls[i];
			PInterval bounds = new PInterval(tInterval.min(), closestHit);
			if (w.hit(r, bounds, tempRec)) {
				if ((tempRec.t() >= 0.0) && (tempRec.t() < tInterval.max())) {
					didHit = true;
					closestHit = tempRec.t();
					rec.setT(tempRec.t());
					rec.setColour(tempRec.colour());
					rec.setFloor(ceilingHeight);
					rec.setCeiling(ceilingHeight);
				}
			}
		}
		return didHit;

	}

	public PHitRecord[] allHits(PRay r, PInterval tInterval) {
		PHitRecord[] hits = new PHitRecord[walls.length];
		int head = 0;
		for (int i = 0; i < walls.length; i++) {
			PEdge w = walls[i];
			PHitRecord tempRec = new PHitRecord();
			if (w.hit(r, tInterval.copy(), tempRec)) {
				tempRec.setFloor(floorHeight);
				tempRec.setCeiling(ceilingHeight);
				hits[head++] = tempRec;
			}
		}
		return PArrays.reduceArray(hits, head);

	}

	@Override
	public String toString() {
		return ("Floor: " + floorHeight + "\n"
				+ "Ceiling: " + ceilingHeight + "\n"
				+ super.toString());

	}
}
