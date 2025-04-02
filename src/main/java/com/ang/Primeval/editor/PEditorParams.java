package com.ang.primeval.editor;

import com.ang.primeval.graphics.PColour;

public class PEditorParams {
	public static final double 	ASPECT_RATIO 		= 16.0 / 9.0;
	public static final int 	CORNER_SIZE 		= 8;
	public static double 		scale 				= 8.0;
	public static int 			width 				= 600;
	public static int 			height 				= (int) Math.round(
													(double) width / ASPECT_RATIO);
	public static PColour 		backgroundColour 	= new PColour(0.9, 0.9, 0.9);
	public static PColour 		lineColour 			= new PColour(0.0, 0.0, 0.0);
	public static PColour 		cornerColour 		= new PColour(0.5, 0.6, 1.0);
	public static PColour 		selectCornerColour	= new PColour(0.0, 0.0, 0.5);
}
