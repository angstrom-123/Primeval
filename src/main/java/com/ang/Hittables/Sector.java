package com.ang.Hittables;

import com.ang.Maths.*;
import com.ang.Graphics.Colour;

public class Sector extends HittableList {
	private double floorHeight = -0.5;
	private double ceilingHeight = 1.0;
	// TODO: implement light level
	//private double lightLevel = 1.0; 

	public Sector(Vec2[] corners, int[] portalCorners) {
		super(corners.length + 1);
		for (int i = 0; i < corners.length; i++) {
			Edge wall;
			if (i < corners.length - 1) {
				wall = new Edge(corners[i], corners[i + 1],
						new Colour(1.0, 1.0, 1.0));
				if (isPortal(i, i + 1, portalCorners)) {
					wall.setAsPortal();
				}
			} else {
				wall = new Edge(corners[i], corners[0],
						new Colour(1.0, 1.0, 1.0));
				if (isPortal(i, 0, portalCorners)) {
					wall.setAsPortal();
				}
			}
			super.addHittable(wall);
		}
	}

	public double floorHeight() {
		return floorHeight;

	}

	public double ceilingHeight() {
		return ceilingHeight;

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

	@Override
	public boolean hit(Ray r, Interval tInterval, HitRecord rec) {
		if (super.hit(r, tInterval, rec)) {
			rec.setFloor(floorHeight);
			rec.setCeiling(ceilingHeight);
			return true;

		}
		return false;

	}
}
