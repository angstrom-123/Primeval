package com.ang.Primeval.Graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private BufferedImage image;

	public ImagePanel(BufferedImage image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
