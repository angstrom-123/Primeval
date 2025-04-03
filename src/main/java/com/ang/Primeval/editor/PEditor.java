package com.ang.primeval.editor;

import com.ang.primeval.hittables.*;
import com.ang.primeval.maths.*;
import com.ang.primeval.exceptions.*;
import com.ang.primeval.graphics.*;
import com.ang.primeval.inputs.*;
import com.ang.primeval.files.PFileReader;
import com.ang.primeval.files.pmap.*;

public class PEditor implements PMouseInputInterface, PEditorInterface {
	private PMouseInputListener mil = new PMouseInputListener(this);
	private PRenderer renderer = new PRenderer(PEditorParams.width, 
			PEditorParams.height, mil);
	private PEditorGUI gui = new PEditorGUI(renderer);
	private int selectedSectorIndex = -1;
	private int selectedCornerIndex = -1;
	private PVec2 viewPos = new PVec2(0.0, 0.0);
	private PVec2 viewPosAtDragStart = new PVec2(0.0, 0.0);
	private PVec2 dragStart = new PVec2(0.0, 0.0);
	private PPMapData editableMapData;
	private PPMapData savedMapData;
	private String mapDirPath;

	public PEditor(String mapDirPath) {
		this.mapDirPath = mapDirPath;
		gui.init();
	}

	public void test() {
		savedMapData = loadMapData("testMap.pmap");
		editableMapData = savedMapData.copy();
		drawMapData();
	}

	@Override
	public void mouseScrolled(int units) {
		final double step = 0.05;
		PEditorParams.scale *= (1 + (units * step));
		PEditorParams.scale = Math.max(PEditorParams.scale, 0.1);
		drawMapData();
	}

	@Override
	public void mouseMoved(int x, int y) {

	}

	@Override
	public void mouseDragged(int x, int y) {
		if ((selectedSectorIndex != -1) 
				&& (selectedCornerIndex != -1)) {
			drawMapData();
			renderer.writeTileAround(PEditorParams.selectCornerColour, 
					PEditorParams.CORNER_SIZE, PEditorParams.CORNER_SIZE, x, y);
		} else {
			drawMapData();
			int dx = (int) dragStart.x() - x;
			int dy = (int) dragStart.y() - y;
			viewPos= new PVec2(viewPosAtDragStart.x() - dx, viewPosAtDragStart.y() - dy);
		}
	}

	@Override
	public void mousePressed(int x, int y) {
		findSelectedCorner(x, y);
		dragStart = new PVec2(x, y);
		viewPosAtDragStart = viewPos.copy();
	}

	@Override
	public void mouseReleased(int x, int y) {
		drawMapData();
		dragStart = new PVec2(0.0, 0.0);
	}

	@Override
	public void mouseExited() {

	}

	@Override
	public void newFile() {

	}

	@Override 
	public void open(String fileName) {

	}

	@Override 
	public void save(String fileName) {
		
	}

	@Override
	public void exit() {

	}

	@Override 
	public void undo() {

	}

	@Override 
	public void redo() {

	}

	@Override 
	public void newSector() {

	}

	@Override 
	public void newCorner() {

	}

	private void saveMap(String fileName) {
		PPMapWriter writer = new PPMapWriter(mapDirPath);
		try {
			writer.writePMap(fileName, editableMapData);
			savedMapData = editableMapData.copy();
		} catch (PPMapWriteException e) {
			System.err.println(e.getMessage());	
			e.printStackTrace();
		}
	}

	private PPMapData loadMapData(String fileName) {
		String[] lines;
		PFileReader reader = new PFileReader(mapDirPath);
		try {
			lines = reader.readFile(fileName);
		} catch (PFileReadException e) {
			System.err.println(e.getMessage());
			return null;

		}
		PPMapData mapData;
		PPMapParser parser = new PPMapParser(mapDirPath + fileName);
		try {
			mapData = parser.parseMapData(lines);
		} catch (PPMapParseException e) {
			System.err.println(e.getMessage());
			return null;

		}
		return mapData;

	}

	private void drawMapData() {
		renderer.writeTile(PEditorParams.backgroundColour, PEditorParams.width, 
				PEditorParams.height, 0, 0);	
		PSectorWorld world = editableMapData.world();
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
				renderer.writeLine(PEditorParams.lineColour, coords[0], coords[1], 
						coords[2], coords[3]);
			}
			for (int i = 0; i < corners.length; i++) {
				int[] coords = viewportToScreenspace(corners[i]);
				renderer.writeTileAround(PEditorParams.cornerColour, 
						PEditorParams.CORNER_SIZE, PEditorParams.CORNER_SIZE, 
						coords[0], coords[1]);
			}
		}
		renderer.repaint();
	}

	private void findSelectedCorner(int x, int y) {
		final PSector[] sectors = editableMapData.world().sectors();
		final int leeway = (int) Math.round((PEditorParams.CORNER_SIZE / 2) * 1.8);
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

	private int viewportToScreenspace(double coord, boolean isX) {
		int scaled = (int) Math.round(coord * PEditorParams.scale);
		int transpose = isX 
		? (PEditorParams.width / 2) + (int) viewPos.x() 
		: (PEditorParams.height / 2) + (int) viewPos.y();
		return scaled + transpose;

	}
}
