package com.ang;

import com.ang.Graphics.Colour;
import com.ang.Hittables.HitRecord;
import com.ang.Maths.Vec2;
import com.ang.Threads.UpdateWorker;

public class Global {
	public static UpdateWorker uw;
	public final static double INFINITY = Double.MAX_VALUE;
	public final static int INFINITY_INT = Integer.MAX_VALUE;

	public static Vec2[] reduceArray(Vec2[] array, int head) {
		Vec2[] out = new Vec2[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static Colour[] reduceArray(Colour[] array, int head) {
		Colour[] out = new Colour[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static int[] reduceArray(int[] array, int head) {
		int[] out = new int[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static HitRecord[] reduceArray(HitRecord[] array, int head) {
		HitRecord[] out = new HitRecord[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}
}
