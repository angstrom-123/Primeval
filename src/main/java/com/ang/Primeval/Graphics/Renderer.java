package com.ang.Primeval.Graphics;

import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.Util.Global;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Renderer {
	private JFrame frame = new JFrame();
	private int width;
	private int height;
	private BufferedImage img;
	private ImagePanel imgPanel;

	public Renderer(int width, int height, Object listener) {
		this.width = width;
		this.height = height;
		this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.imgPanel = new ImagePanel(img);
		init(listener);
	}

	public int windowWidth() {
		return frame.getWidth();

	}

	public int windowHeight() {
		return frame.getHeight();

	}

	private void init(Object listener) {
		imgPanel.setPreferredSize(new Dimension(width, height));
		frame.getContentPane().add(imgPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		imgPanel.setFocusable(true);
		imgPanel.requestFocusInWindow();
		if (listener instanceof MovementInputListener) {
			MovementInputListener il = (MovementInputListener) listener;
			imgPanel.addKeyListener(il);
		} else if (listener instanceof MouseInputListener) {
			MouseInputListener mil = (MouseInputListener) listener;
			imgPanel.addMouseMotionListener(mil);
			imgPanel.addMouseListener(mil);
			imgPanel.addMouseWheelListener(mil);
		} else {
			System.err.println("Invalid listener supplied to renderer init");
			Global.uw.doStop();
			frame.dispose();
			return;

		}
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (Global.uw != null) {
					Global.uw.doStop();
				}
				frame.dispose();
			}
		});
	}

	public void writePixel(Colour colour, int x, int y) {
		int col = processToInt(colour);
		if (!inBounds(x, y)) {
			return;

		}
		img.setRGB(x, y, col);
	}

	public void writeColumn(Colour colour, int x, int bottom, int top) {
		int columnColour = processToInt(colour);
		// fill in pixels from the top, so higher pixels have a smaller y value
		for (int y = 0; y < height; y++) {
			if ((y >= top) && (y <= bottom)) {
				if (!inBounds(x, y)) {
					continue;

				}
				img.setRGB(x, y, columnColour);
			}
		}
	}

	public void writeTile(Colour colour, int width, int height, int x, int y) {
		int tileColour = processToInt(colour);
		for (int j = y; j < y + height; j++) {
			for (int i = x; i < x + width; i++) {
				if (!inBounds(i, j)) {
					continue;

				}
				img.setRGB(i, j, tileColour);
			}
		}
	}

	public void writeTileAround(Colour colour, int width, int height, int x, int y) {
		int tileColour = processToInt(colour);
		for (int j = y - (height / 2); j < y + (height / 2); j++) {
			for (int i = x - (width / 2); i < x + (width / 2); i++) {
				if (!inBounds(i, j)) {
					continue;

				}
				img.setRGB(i, j, tileColour);
			}
		}
	}

	public void writeLine(Colour colour, int x0, int y0, int x1, int y1) {
		int lineColour = processToInt(colour);
		if (Math.abs(y1 - y0) < Math.abs(x1 - x0)) {
			if (x0 > x1) {
				writeLineLow(lineColour, x1, y1, x0, y0);
			} else {
				writeLineLow(lineColour, x0, y0, x1, y1);
			}
		} else {
			if (y0 > y1) {
				writeLineHigh(lineColour, x1, y1, x0, y0);
			} else {
				writeLineHigh(lineColour, x0, y0, x1, y1);
			}
		}
	}

	private void writeLineLow(int colour, int x0, int y0, int x1, int y1) {
		int dx = x1 - x0;
		int dy = y1 - y0;
		int yIncrement = 1;
		if (dy < 0) {
			yIncrement = -1;
			dy = -dy;
		}
		int error = (2 * dy) - dx;
		int y = y0;
		for (int x = x0; x < x1; x++) {
			if (inBounds(x, y)) {
				img.setRGB(x, y, colour);
			}
			if (error > 0) {
				y += yIncrement;
				error += 2 * (dy - dx);
			} else {
				error += 2 * dy;
			}
		}
	}

	private void writeLineHigh(int colour, int x0, int y0, int x1, int y1) {
		int dx = x1 - x0;
		int dy = y1 - y0;
		int xIncrement = 1;
		if (dx < 0) {
			xIncrement = -1;
			dx = -dx;
		}
		int error = (2 * dx) - dy;
		int x = x0;
		for (int y = y0; y < y1; y++) {
			if (inBounds(x, y)) {
				img.setRGB(x, y, colour);
			}
			if (error > 0) {
				x += xIncrement;
				error += 2 * (dx - dy);
			} else {
				error += 2 * dx;
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

	private boolean inBounds(int x, int y) {
		if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
			return false;

		}
		return true;

	}
}
