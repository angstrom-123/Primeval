package com.ang.primeval.editor;

import com.ang.primeval.graphics.PColour;

public class PEditorParams {
	public final double 	ASPECT_RATIO 		= 16.0 / 9.0;
	public final int 	CORNER_SIZE 		= 8;
	public double 		scale 				= 8.0;
	public int 			width 				= 600;
	public int 			height 				= (int) Math.round((double) width 
		     								/ ASPECT_RATIO);
	public PColour 		backgroundColour 	= new PColour(0.9, 0.9, 0.9);
	public PColour 		lineColour 			= new PColour(0.0, 0.0, 0.0);
	public PColour 		cornerColour 		= new PColour(0.5, 0.6, 1.0);
	public PColour 		selectCornerColour	= new PColour(0.0, 0.0, 0.5);
}
