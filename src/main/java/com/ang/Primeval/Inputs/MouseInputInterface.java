package com.ang.Primeval.Inputs;

public interface MouseInputInterface {
	void mouseScrolled(int units);
	void mouseMoved(int x, int y);
	void mouseDragged(int x, int y);
	void mousePressed(int x, int y);
	void mouseReleased(int x, int y);
	void mouseExited();
}
