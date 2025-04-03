package com.ang.primeval.editor;

import java.io.File;

public interface PEditorInterface {
	void save(File file);
	void open(File file);
	void newFile();
	void exit();
	void undo();
	void redo();
	void newSector();
	void newCorner();
}
