package com.ang.primeval.editor;

public interface PEditorInterface {
	void save(String fileName);
	void open(String fileName);
	void newFile();
	void exit();
	void undo();
	void redo();
	void newSector();
	void newCorner();
}
