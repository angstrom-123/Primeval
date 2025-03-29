package com.ang.Primeval.Hittables;

import com.ang.Primeval.Global;
import com.ang.Primeval.Graphics.Colour;
import com.ang.Primeval.Maths.*;

public class Sector {
	private Edge[] walls;
	private double floorHeight;
	private double ceilingHeight;
	// TODO: implement light level
	private double lightLevel = 1.0; 

	public Sector(Vec2[] corners, int[] portalIndices) {
		walls = new Edge[corners.length];
		int head = 0;
		for (int i = 0; i < corners.length; i++) {
			Edge wall;
			int nextI = (i < corners.length - 1) ? i + 1 : 0;
			wall = new Edge(corners[i], corners[nextI], new Colour(1.0, 1.0, 1.0));
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

	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		boolean didHit = false;
		double closestHit = Global.INFINITY;
		HitRecord tempRec = new HitRecord();
		for (int i = 0; i < walls.length; i++) {
			Edge w = walls[i];
			Interval bounds = new Interval(tInterval.min(), closestHit);
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

	public HitRecord[] allHits(Ray r, Interval tInterval) {
		HitRecord[] hits = new HitRecord[walls.length];
		int head = 0;
		for (int i = 0; i < walls.length; i++) {
			Edge w = walls[i];
			HitRecord tempRec = new HitRecord();
			if (w.hit(r, tInterval.copy(), tempRec)) {
				tempRec.setFloor(floorHeight);
				tempRec.setCeiling(ceilingHeight);
				hits[head++] = tempRec;
			}
		}
		return Global.reduceArray(hits, head);

	}

	@Override
	public String toString() {
		return ("Floor: " + floorHeight + "\n"
				+ "Ceiling: " + ceilingHeight + "\n"
				+ super.toString());

	}
}
