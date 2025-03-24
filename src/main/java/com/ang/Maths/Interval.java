package com.ang.Maths;

import com.ang.Global;

public class Interval {
	double min;
	double max;

	public Interval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public boolean contains(double val) {
		if (min < max) {
			if ((val >= min) && (val <= max)) {
				return true;

			}
		} else if (max < min) {
			if ((val <= min) && (val >= max)) {
				return true;

			}
		}
		return false;

	}

	public double min() {
		return min;

	}

	public double max() {
		return max;

	}

	public void setMin(double t) {
		min = t;
	}

	public void setMax(double t) {
		max = t;
	}

	public static Interval empty() {
		return new Interval(Global.INFINITY, -Global.INFINITY);

	}

	public static Interval universe() {
		return new Interval(-Global.INFINITY, Global.INFINITY);

	}

	@Override
	public String toString() {
		return ("min:" + min + "\n" + "max:" + max);

	}
}
