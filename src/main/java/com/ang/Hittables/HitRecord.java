package com.ang.Hittables;

import com.ang.Graphics.Colour;

public class HitRecord {
	private double t;
	private Colour colour;
	private double sectorFloor;
	private double sectorCeiling;

	public HitRecord() {}

	public HitRecord copy() {
		HitRecord temp = new HitRecord();
		temp.setT(t);
		temp.setColour(colour);
		temp.setFloor(sectorFloor);
		temp.setCeiling(sectorCeiling);
		return temp;

	}

	public double t() {
		return t;

	}

	public Colour colour() {
		return colour;

	}

	public double sectorFloor() {
		return sectorFloor;

	}

	public double sectorCeiling() {
		return sectorCeiling;

	}

	public void setT(double t) {
		this.t = t;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}

	public void setFloor(double floorHeight) {
		this.sectorFloor = floorHeight;
	}

	public void setCeiling(double ceilingHeight) {
		this.sectorCeiling = ceilingHeight;
	}
}
