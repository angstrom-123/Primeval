package com.ang.Primeval.Maths;

import com.ang.Primeval.PGlobal;

public class PInterval {
	double min;
	double max;

	public PInterval(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public PInterval copy() {
		return new PInterval(min, max);

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

	public static PInterval empty() {
		return new PInterval(PGlobal.INFINITY, -PGlobal.INFINITY);

	}

	public static PInterval universe() {
		return new PInterval(-PGlobal.INFINITY, PGlobal.INFINITY);

	}

	@Override
	public String toString() {
		return ("min:" + min + "\n" + "max:" + max);

	}
}
