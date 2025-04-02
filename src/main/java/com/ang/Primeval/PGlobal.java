package com.ang.primeval;

import com.ang.primeval.threads.PUpdateWorker;

public class PGlobal {
	public static PUpdateWorker uw;
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
}
