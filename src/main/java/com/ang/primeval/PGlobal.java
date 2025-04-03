package com.ang.primeval;

import com.ang.primeval.threads.PUpdateWorker;
import com.ang.primeval.maths.PVec2;

public class PGlobal {
	public static PUpdateWorker uw;
	public final static double 	INFINITY 		= Double.MAX_VALUE;
	public final static int 	INFINITY_INT 	= Integer.MAX_VALUE;
	public final static boolean isWindows 		= System.getProperty("os.name")
														.startsWith("Windows");

	public static String toWindowsPath(String path) {
		String out = "";
		path = path.trim();
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == '\\') {
				out += "/";
			} else {
				out += path.charAt(i);
			}
		}
		return out;

	}

	public static int[] viewportToScreenspace(PVec2 p0, PVec2 p1, 
			int screenHeight, int screenWidth, double scale, PVec2 viewPos) {
		return new int[]{
				viewportToScreenspace(p0.x(), true, screenWidth, screenHeight, 
						scale, viewPos),
				viewportToScreenspace(p0.y(), false, screenWidth, screenHeight, 
						scale, viewPos),
				viewportToScreenspace(p1.x(), true, screenWidth, screenHeight, 
						scale, viewPos),
				viewportToScreenspace(p1.y(), false, screenWidth, screenHeight, 
						scale, viewPos)};

	}

	public static int[] viewportToScreenspace(PVec2 p, 
			int screenHeight, int screenWidth, double scale, PVec2 viewPos) {
		return new int[]{
				viewportToScreenspace(p.x(), true, screenWidth, screenHeight,
						scale, viewPos),
				viewportToScreenspace(p.y(), false, screenWidth, screenHeight,
						scale, viewPos)};

	}

	public static int viewportToScreenspace(double coord, boolean isX,
			int screenHeight, int screenWidth, double scale, PVec2 viewPos) {
		int scaled = (int) Math.round(coord * scale);
		int transpose = isX 
		? (screenWidth / 2) + (int) viewPos.x() 
		: (screenHeight / 2) + (int) viewPos.y();
		return scaled + transpose;

	}
}
