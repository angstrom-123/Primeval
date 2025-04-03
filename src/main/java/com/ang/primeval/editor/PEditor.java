package com.ang.primeval.editor;

import com.ang.primeval.hittables.*;
import com.ang.primeval.maths.*;
import com.ang.primeval.exceptions.*;
import com.ang.primeval.graphics.*;
import com.ang.primeval.inputs.*;
import com.ang.primeval.files.pmap.*;
import com.ang.primeval.PGlobal;

import java.io.File;

public class PEditor implements PMouseInputInterface, PEditorInterface {
	private PEditorParams params = new PEditorParams();
	private PMouseInputListener mil = new PMouseInputListener(this);
	private PRenderer renderer = new PRenderer(params.width, params.height, mil);
	private PEditorGUI gui = new PEditorGUI(renderer, this);
	private int selectedSectorIndex = -1;
	private int selectedCornerIndex = -1;
	private PVec2 viewPos = new PVec2(0.0, 0.0);
	private PVec2 viewPosAtDragStart = new PVec2(0.0, 0.0);
	private PVec2 dragStart = new PVec2(0.0, 0.0);
	private PPMapHandler mapHandler;

	public PEditor(String mapDirPath) {
		mapHandler = new PPMapHandler(mapDirPath);
		gui.init();
	}

	public void test() {
		File testFile = new File("/mapData/testMap.pmap");
		try {
			mapHandler.loadMapData(testFile);
		} catch (PFileLoadException e) {
			e.printStackTrace();
			return;

		}
		renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
		renderer.repaint();
	}

	@Override
	public void mouseScrolled(int units) {
		final double step = 0.05;
		params.scale *= (1 + (units * step));
		params.scale = Math.max(params.scale, 0.1);
		renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
		renderer.repaint();
	}

	@Override
	public void mouseMoved(int x, int y) {

	}

	@Override
	public void mouseDragged(int x, int y) {
		if ((selectedSectorIndex != -1) 
				&& (selectedCornerIndex != -1)) {
			renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
			renderer.writeTileAround(params.selectCornerColour, 
					params.CORNER_SIZE, params.CORNER_SIZE, x, y);
		} else {
			renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
			int dx = (int) dragStart.x() - x;
			int dy = (int) dragStart.y() - y;
			viewPos = new PVec2(viewPosAtDragStart.x() - dx, viewPosAtDragStart.y() - dy);
		}
		renderer.repaint();
	}

	@Override
	public void mousePressed(int x, int y) {
		findSelectedCorner(x, y);
		dragStart = new PVec2(x, y);
		viewPosAtDragStart = viewPos.copy();
	}

	@Override
	public void mouseReleased(int x, int y) {
		renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
		dragStart = new PVec2(0.0, 0.0);
		renderer.repaint();
	}

	@Override
	public void mouseExited() {

	}

	@Override
	public void newFile() {
		PPMapData temp = new PPMapData();
		temp.setWorld(new PSectorWorld(1000));
		temp.setFacing(new PVec2(0.0, 0.0));
		temp.setPosition(new PVec2(0.0, 0.0));
		mapHandler.setEditableMapData(temp);
		mapHandler.setSavedMapData(temp.copy());
		mapHandler.saveData().fileName = null;
		renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
		renderer.repaint();
	}

	@Override 
	public void open(File file) {
		try {
			mapHandler.loadMapData(file);
		} catch (PFileLoadException e) {
			e.printStackTrace();
		}
		renderer.writeMapData(mapHandler.saveData().editableMapData, params, viewPos);
		renderer.repaint();
	}

	@Override 
	public void save(File file) {
		try {
			if (file == null) {
				mapHandler.saveMap(new File(mapHandler.saveData().fileName));
			} else {
				mapHandler.saveMap(file);
			}
		} catch (PFileWriteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exit() {
		renderer.close();
	}

	@Override 
	public void undo() {
		System.out.println("undo");
	}

	@Override 
	public void redo() {
		System.out.println("redo");
	}

	@Override 
	public void newSector() {
		System.out.println("new sector");
	}

	@Override 
	public void newCorner() {
		System.out.println("new corner");
	}


	private void findSelectedCorner(int x, int y) {
		final PSector[] sectors = mapHandler.saveData().editableMapData.world().sectors();
		final int leeway = (int) Math.round((params.CORNER_SIZE / 2) * 1.8);
		for (int i = 0; i < sectors.length; i++) {
			PVec2[] corners = sectors[i].corners();
			for (int j = 0; j < corners.length; j++) {
				int[] coords = PGlobal.viewportToScreenspace(corners[j], 
						params.width, params.height, params.scale, viewPos);
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
}
