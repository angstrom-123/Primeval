package com.ang.Primeval.Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseInputListener implements MouseMotionListener {
	private MouseInputInterface mouseInterface;

	public MouseInputListener(MouseInputInterface mouseInterface) {
		this.mouseInterface = mouseInterface;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseInterface.mouseMoved(e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseInterface.mouseDragged(e.getX(), e.getY());
	}
}
