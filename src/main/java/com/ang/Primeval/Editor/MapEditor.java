package com.ang.Primeval.Editor;

import com.ang.Primeval.Core.Exceptions.*;
import com.ang.Primeval.Core.Graphics.*;
import com.ang.Primeval.Core.Hittables.*;
import com.ang.Primeval.Core.Maths.*;
import com.ang.Primeval.Inputs.*;
import com.ang.Primeval.Util.Loaders.FileReader;
import com.ang.Primeval.Util.Loaders.Pmap.*;
import com.ang.Primeval.Util.Loaders.Ttl.*;

public class MapEditor implements MouseInputInterface {
	private final double SCALE = 10.0;
	private final double ASPECT_RATIO = 16.0 / 9.0;
	private final int WIDTH = 600;
	private final int HEIGHT = (int) Math.round((double) WIDTH / ASPECT_RATIO);
	private MouseInputListener mil = new MouseInputListener(this);
	private Renderer renderer = new Renderer(WIDTH, HEIGHT, mil);
	private Colour backgroundColour = new Colour(0.9, 0.9, 0.9);
	private Colour lineColour = new Colour(0.0, 0.0, 0.0);
	private String mapDirPath;

	public MapEditor(String mapDirPath) {
		this.mapDirPath = mapDirPath;
	}

	public void test() {
		MapData mapData = loadMap("testMap.pmap");
		drawMapData(mapData);
	}

	private MapData loadMap(String fileName) {
		String[] lines;
		FileReader reader = new FileReader(mapDirPath);
		try {
			lines = reader.readFile(fileName);
		} catch (MapReadException e) {
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

	private void drawMapData(MapData mapData) {
		renderer.writeTile(backgroundColour, WIDTH, HEIGHT, 0, 0);	
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
				renderer.writeLine(lineColour, coords[0], 
						coords[1], coords[2], coords[3]);
				System.out.println("Drawing line between: \n"
						+ coords[0] + " " + coords[1] + "\n"
						+ coords[2] + " " + coords[3]);
			}
		}
		renderer.repaint();
	}

	@Override
	public void mouseMoved(int x, int y) {
		System.out.println("Mouse moved to " + x + " " + y);
	}

	@Override
	public void mouseDragged(int x, int y) {
		System.out.println("Mouse dragged to " + x + " " + y);
	}

	private int[] viewportToScreenspace(Vec2 p0, Vec2 p1) {
		return new int[]{viewportToScreenspace(p0.x(), true),
				viewportToScreenspace(p0.y(), false),
				viewportToScreenspace(p1.x(), true),
				viewportToScreenspace(p1.y(), false)};

	}

	private int viewportToScreenspace(double coord, boolean isX) {
		int scaled = (int) Math.round(coord * SCALE);
		int transpose = isX ? WIDTH / 2 : HEIGHT / 2;
		return scaled + transpose;

	}
}
