package com.ang.Primeval.Graphics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.swing.JFrame;

import com.ang.Primeval.Inputs.InputListener;
import com.ang.Primeval.Global;

public class Renderer {
	private JFrame frame = new JFrame();
	private int width;
	private int height;
	private BufferedImage img;
	private ImagePanel imgPanel;

	public Renderer(int width, int height, InputListener il) {
		this.width = width;
		this.height = height;
		this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.imgPanel = new ImagePanel(img);
		init(il);
	}
	
	private void init(InputListener il) {
		imgPanel.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().add(imgPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		imgPanel.setFocusable(true);
		imgPanel.requestFocusInWindow();
		imgPanel.addKeyListener(il);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Global.uw.doStop();
				frame.dispose();
			}
		});
	}

	public void writePixel(Colour colour, int x, int y) {
		int col = processToInt(colour);
		img.setRGB(x, y, col);
	}

	public void writeColumn(Colour columnColour, int x, int bottom, int top) {
		int colColour = processToInt(columnColour);
		// fill in pixels from the top, so higher pixels have a smaller y value
		for (int y = 0; y < height; y++) {
			if ((y >= top) && (y <= bottom)) {
				img.setRGB(x, y, colColour);
			}
		}
	}

	public void writeTile(Colour tileColour, int width, int height, int x, int y) {
		int tColour = processToInt(tileColour);
		for (int j = y; j < y + height; j++) {
			for (int i = x; i < x + width; i++) {
				img.setRGB(i, j, tColour);
			}
		}
	}

	public void repaint() {
		frame.repaint();
	}

	private int processToInt(Colour c) {
		// convert from linear to gamma space
		double r = c.r() > 0 ? Math.sqrt(c.r()) : 0.0;
		double g = c.g() > 0 ? Math.sqrt(c.g()) : 0.0;
		double b = c.b() > 0 ? Math.sqrt(c.b()) : 0.0;
		// normalize and round
		int rComponent = (int) Math.min(r * 255, 255);
		int gComponent = (int) Math.min(g * 255, 255);
		int bComponent = (int) Math.min(b * 255, 255);
		// convert to BufferedImage.TYPE_INT_RGB
		return (rComponent << 16) | (gComponent << 8) | (bComponent);

	}
}
