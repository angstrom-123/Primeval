package com.ang.primeval.utils;

import com.ang.primeval.hittables.PSector;
import com.ang.primeval.hittables.PHitRecord;
import com.ang.primeval.maths.PVec2;
import com.ang.primeval.graphics.PColour;

public class PArrays {
	public static PSector[] reduceArray(PSector[] array, int head) {
		PSector[] out = new PSector[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static PVec2[] reduceArray(PVec2[] array, int head) {
		PVec2[] out = new PVec2[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static PColour[] reduceArray(PColour[] array, int head) {
		PColour[] out = new PColour[head];
		for (int i = 0; i < out.length; i++) {
			out[i] = array[i];
		}
		return out;

	}

	public static PHitRecord[] reduceArray(PHitRecord[] array, int head) {
		PHitRecord[] out = new PHitRecord[head];
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
