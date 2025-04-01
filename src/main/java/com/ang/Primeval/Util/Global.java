package com.ang.Primeval.Util;

import com.ang.Primeval.Graphics.Colour;
import com.ang.Primeval.Core.Hittables.*;
import com.ang.Primeval.Core.Maths.Vec2;
import com.ang.Primeval.Core.Threads.UpdateWorker;

public class Global {
	public static UpdateWorker uw;
	public final static double INFINITY = Double.MAX_VALUE;
	public final static int INFINITY_INT = Integer.MAX_VALUE;

	public static String padRight(String str, int width) {
		String out = "";
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			out += str.charAt(i);
			len++;
		}
		for (int i = len; i < width; i++) {
			out += " ";
		}
		return out;

	}

	public static Sector[] reduceArray(Sector[] array, int head) {
		Sector[] out = new Sector[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

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

	public static HitRecord[] reduceArray(HitRecord[] array, int head) {
		HitRecord[] out = new HitRecord[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static String[] reduceArray(String[] array, int head) {
		String[] out = new String[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static char[] reduceArray(char[] array, int head) {
		char[] out = new char[head];
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
}
