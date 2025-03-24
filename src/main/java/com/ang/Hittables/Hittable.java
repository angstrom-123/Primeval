package com.ang.Hittables;

import com.ang.Maths.*;

public abstract class Hittable {
	public abstract boolean hit(Ray r, Interval tInterval, HitRecord rec);
	
	@Override
	public abstract String toString();
}
