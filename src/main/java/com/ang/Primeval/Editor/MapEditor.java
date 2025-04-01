package com.ang.Primeval.Editor;

import com.ang.Primeval.Core.Hittables.*;
import com.ang.Primeval.Core.Maths.*;
import com.ang.Primeval.Exceptions.*;
import com.ang.Primeval.Graphics.*;
import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.Util.Loaders.FileReader;
import com.ang.Primeval.Util.Loaders.Pmap.*;
import com.ang.Primeval.Util.Loaders.Ttf.*;

public class MapEditor implements MouseInputInterface {
	private final double ASPECT_RATIO = 16.0 / 9.0;
	private final int CORNER_SIZE = 8;
	private double scale = 8.0;
	private Vec2 viewPosition = new Vec2(0.0, 0.0);
	private Vec2 viewPositionAtDragStart = new Vec2(0.0, 0.0);
	private Vec2 dragStart = new Vec2(0.0, 0.0);
	private int width = 600;
	private int height = (int) Math.round((double) width / ASPECT_RATIO);
	private MouseInputListener mil = new MouseInputListener(this);
	private Renderer renderer = new Renderer(width, height, mil);
	private Colour backgroundColour = new Colour(0.9, 0.9, 0.9);
	private Colour lineColour = new Colour(0.0, 0.0, 0.0);
	private Colour cornerColour = new Colour(0.5, 0.6, 1.0);
	private Colour selectedCornerColour = new Colour(0.0, 0.0, 0.5);
	private MapData mapData;
	private int selectedSectorIndex;
	private int selectedCornerIndex;
	private String mapDirPath;
	private String fontDirPath;

	public MapEditor(String mapDirPath, String fontDirPath) {
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
		FileReader reader = new FileReader(fontDirPath);
		try {
			bytes = reader.readFileAsBytes(fileName);
		} catch (FileReadException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		
		}
		TtfParser parser = new TtfParser(fontDirPath + fileName);
		try {
			parser.readTtl(bytes);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;

		}
	}

	private MapData loadMap(String fileName) {
		String[] lines;
		FileReader reader = new FileReader(mapDirPath);
		try {
			lines = reader.readFile(fileName);
		} catch (FileReadException e) {
			System.err.println(e.getMessage());
			return null;

		}
		MapData mapData;
		PmapParser parser = new PmapParser(mapDirPath + fileName);
		try {
			mapData = parser.parseMapData(lines);
		} catch (MapParseException e) {
			System.err.println(e.getMessage());
			return null;

		}
		return mapData;

	}

	private void drawMapData() {
		renderer.writeTile(backgroundColour, width, height, 0, 0);	
		SectorWorld world = mapData.world();
		for (Sector sec : world.sectors()) {
			Vec2[] corners = sec.corners();
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
		//scale += (step * units);
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
			viewPosition = new Vec2(viewPositionAtDragStart.x() - dx, 
					viewPositionAtDragStart.y() - dy);
		}
	}

	@Override
	public void mousePressed(int x, int y) {
		findSelectedCorner(x, y);
		dragStart = new Vec2(x, y);
		viewPositionAtDragStart = viewPosition.copy();
	}

	@Override
	public void mouseReleased(int x, int y) {
		drawMapData();
		dragStart = new Vec2(0.0, 0.0);
	}

	@Override
	public void mouseExited() {

	}

	private void findSelectedCorner(int x, int y) {
		Sector[] sectors = mapData.world().sectors();
		for (int i = 0; i < sectors.length; i++) {
			Vec2[] corners = sectors[i].corners();
			for (int j = 0; j < corners.length; j++) {
				int[] coords = viewportToScreenspace(corners[j]);
				int minX = coords[0] - (CORNER_SIZE / 2);
				int maxX = coords[0] + (CORNER_SIZE / 2);
				int minY = coords[1] - (CORNER_SIZE / 2);
				int maxY = coords[1] + (CORNER_SIZE / 2);
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

	private int[] viewportToScreenspace(Vec2 p0, Vec2 p1) {
		return new int[]{viewportToScreenspace(p0.x(), true),
				viewportToScreenspace(p0.y(), false),
				viewportToScreenspace(p1.x(), true),
				viewportToScreenspace(p1.y(), false)};

	}

	private int[] viewportToScreenspace(Vec2 p) {
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
