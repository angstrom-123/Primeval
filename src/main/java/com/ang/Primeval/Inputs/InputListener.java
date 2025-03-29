package com.ang.Primeval.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputListener implements KeyListener {
	private InputInterface ii;

	public InputListener(InputInterface ii) {
		this.ii = ii;	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			ii.pressed(key);
		}
		if (key == KeyEvent.VK_A) {
			ii.pressed(key);
		}
		if (key == KeyEvent.VK_S) {
			ii.pressed(key);
		}
		if (key == KeyEvent.VK_D) {
			ii.pressed(key);
		}
		if (key == KeyEvent.VK_LEFT) {
			ii.pressed(key);
		}
		if (key == KeyEvent.VK_RIGHT) {
			ii.pressed(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			ii.released(key);
		}
		if (key == KeyEvent.VK_A) {
			ii.released(key);
		}
		if (key == KeyEvent.VK_S) {
			ii.released(key);
		}
		if (key == KeyEvent.VK_D) {
			ii.released(key);
		}
		if (key == KeyEvent.VK_LEFT) {
			ii.released(key);
		}
		if (key == KeyEvent.VK_RIGHT) {
			ii.released(key);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
