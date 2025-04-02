package com.ang.Primeval.Editor;

import com.ang.Primeval.Hittables.*;
import com.ang.Primeval.Maths.*;
import com.ang.Primeval.Exceptions.*;
import com.ang.Primeval.Graphics.*;
import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.Files.PFileReader;
import com.ang.Primeval.Files.PMAP.*;
import com.ang.Primeval.Files.TTF.*;

public class PMapEditor implements PMouseInputInterface {
	private final double ASPECT_RATIO = 16.0 / 9.0;
	private final int CORNER_SIZE = 8;
	private double scale = 8.0;
	private PVec2 viewPosition = new PVec2(0.0, 0.0);
	private PVec2 viewPositionAtDragStart = new PVec2(0.0, 0.0);
	private PVec2 dragStart = new PVec2(0.0, 0.0);
	private int width = 600;
	private int height = (int) Math.round((double) width / ASPECT_RATIO);
	private PMouseInputListener mil = new PMouseInputListener(this);
	private PRenderer renderer = new PRenderer(width, height, mil);
	private PColour backgroundColour = new PColour(0.9, 0.9, 0.9);
	private PColour lineColour = new PColour(0.0, 0.0, 0.0);
	private PColour cornerColour = new PColour(0.5, 0.6, 1.0);
	private PColour selectedCornerColour = new PColour(0.0, 0.0, 0.5);
	private PMapData mapData;
	private int selectedSectorIndex;
	private int selectedCornerIndex;
	private String mapDirPath;
	private String fontDirPath;

	public PMapEditor(String mapDirPath, String fontDirPath) {
		this.mapDirPath = mapDirPath;
		this.fontDirPath = fontDirPath;
	}

	public void test() {
		mapData = loadMap("testMap.pmap");
		drawMapData();
		loadFont("Inconsolata/static/Inconsolata-Regular.ttf");
	}

	public void newMap() { // TODO: Implement

	}

	private void loadFont(String fileName) {
		byte[] bytes;
		PFileReader reader = new PFileReader(fontDirPath);
		try {
			bytes = reader.readFileAsBytes(fileName);
		} catch (PFileReadException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		
		}
		PTTFParser parser = new PTTFParser(fontDirPath + fileName);
		try {
			parser.readTtl(bytes);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;

		}
	}

	private PMapData loadMap(String fileName) {
		String[] lines;
		PFileReader reader = new PFileReader(mapDirPath);
		try {
			lines = reader.readFile(fileName);
		} catch (PFileReadException e) {
			System.err.println(e.getMessage());
			return null;

		}
		PMapData mapData;
		PMapParser parser = new PMapParser(mapDirPath + fileName);
		try {
			mapData = parser.parseMapData(lines);
		} catch (PMapParseException e) {
			System.err.println(e.getMessage());
			return null;

		}
		return mapData;

	}

	private void drawMapData() {
		renderer.writeTile(backgroundColour, width, height, 0, 0);	
		PSectorWorld world = mapData.world();
		for (PSector sec : world.sectors()) {
			PVec2[] corners = sec.corners();
			for (int i = 0; i < corners.length; i++) {
				int nextIndex;
				if (i < corners.length - 1) {
					nextIndex = i + 1;
				} else {
					nextIndex = 0;
				}
				int[] coords = viewportToScreenspace(corners[i], corners[nextIndex]);
				renderer.writeLine(lineColour, coords[0], coords[1], 
						coords[2], coords[3]);
			}
			for (int i = 0; i < corners.length; i++) {
				int[] coords = viewportToScreenspace(corners[i]);
				renderer.writeTileAround(cornerColour, CORNER_SIZE, CORNER_SIZE, 
						coords[0], coords[1]);
			}
		}
		renderer.repaint();
	}

	@Override
	public void mouseScrolled(int units) {
		final double step = 0.05;
		scale *= (1 + (units * step));
		scale = Math.max(scale, 0.1);
		drawMapData();
	}

	@Override
	public void mouseMoved(int x, int y) {

	}

	@Override
	public void mouseDragged(int x, int y) {
		if ((selectedSectorIndex != -1) && (selectedCornerIndex != -1)) {
			drawMapData();
			renderer.writeTileAround(selectedCornerColour, CORNER_SIZE, CORNER_SIZE, x, y);
		} else {
			drawMapData();
			int dx = (int) dragStart.x() - x;
			int dy = (int) dragStart.y() - y;
			viewPosition = new PVec2(viewPositionAtDragStart.x() - dx, 
					viewPositionAtDragStart.y() - dy);
		}
	}

	@Override
	public void mousePressed(int x, int y) {
		findSelectedCorner(x, y);
		dragStart = new PVec2(x, y);
		viewPositionAtDragStart = viewPosition.copy();
	}

	@Override
	public void mouseReleased(int x, int y) {
		drawMapData();
		dragStart = new PVec2(0.0, 0.0);
	}

	@Override
	public void mouseExited() {

	}

	private void findSelectedCorner(int x, int y) {
		final PSector[] sectors = mapData.world().sectors();
		final int leeway = (int) Math.round((CORNER_SIZE / 2) * 1.8);
		for (int i = 0; i < sectors.length; i++) {
			PVec2[] corners = sectors[i].corners();
			for (int j = 0; j < corners.length; j++) {
				int[] coords = viewportToScreenspace(corners[j]);
				int minX = coords[0] - leeway;
				int maxX = coords[0] + leeway;
				int minY = coords[1] - leeway;
				int maxY = coords[1] + leeway;
				if ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) {
					selectedSectorIndex = i;
					selectedCornerIndex = j;
					return;

				}
			}
		}
		selectedSectorIndex = -1;
		selectedCornerIndex = -1;
	}

	private int[] viewportToScreenspace(PVec2 p0, PVec2 p1) {
		return new int[]{viewportToScreenspace(p0.x(), true),
				viewportToScreenspace(p0.y(), false),
				viewportToScreenspace(p1.x(), true),
				viewportToScreenspace(p1.y(), false)};

	}

	private int[] viewportToScreenspace(PVec2 p) {
		return new int[]{viewportToScreenspace(p.x(), true),
				viewportToScreenspace(p.y(), false)};

	}

	private int[] viewportToScreenspace(double x, double y) {
		return new int[]{viewportToScreenspace(x, true),
				viewportToScreenspace(y, false)};

	}

	private int viewportToScreenspace(double coord, boolean isX) {
		int scaled = (int) Math.round(coord * scale);
		int transpose = isX 
		? (width / 2) + (int) viewPosition.x() 
		: (height / 2) + (int) viewPosition.y();
		return scaled + transpose;

	}

	private boolean inBounds(int x, int y) {
		if ((x < 0) || (x > width) || (y < 0) || (y > height)) {
			return false;

		}
		return true;

	}
}
