package com.ang.primeval.editor;

import java.awt.event.*;
import javax.swing.*;

import com.ang.primeval.graphics.PRenderer;

public class PEditorGUI implements ActionListener, ItemListener {
	private JFrame frame;

	public PEditorGUI(PRenderer renderer) {
		this.frame = renderer.frame();	
	}

	public void init() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createEditMenu());
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.validate();
		frame.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "New":
			System.out.println("Clicked new");
			break;
			
		case "Open":
			System.out.println("Clicked open");
			break;

		case "Save":
			System.out.println("Clicked save");
			break;

		case "Save As":
			System.out.println("Clicked save as");
			break;

		case "Exit":
			System.out.println("Clicked exit");
			break;

		case "Undo":
			System.out.println("Clicked undo");
			break;

		case "Redo":
			System.out.println("Clicked redo");
			break;

		case "Sector":
			System.out.println("Clicked sector");
			break;

		case "Corner":
			System.out.println("Clicked corner");
			break;

		default:
			break;

		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {

	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem itemNew = new JMenuItem("New");
		JMenuItem itemOpen = new JMenuItem("Open");
		JMenuItem itemSave = new JMenuItem("Save");
		JMenuItem itemSaveAs = new JMenuItem("Save As");
		JMenuItem itemExit = new JMenuItem("Exit");
		itemNew.addActionListener(this);
		itemOpen.addActionListener(this);
		itemSave.addActionListener(this);
		itemSaveAs.addActionListener(this);
		itemExit.addActionListener(this);
		fileMenu.add(itemNew);
		fileMenu.add(itemOpen);
		fileMenu.add(itemSave);
		fileMenu.add(itemSaveAs);
		fileMenu.add(itemExit);
		return fileMenu;

	}

	private JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		JMenu submenuNew = new JMenu("New");
		JMenu submenuPreferences = new JMenu("Preferences");
		JMenuItem itemUndo = new JMenuItem("Undo");
		JMenuItem itemRedo = new JMenuItem("Redo");
		JMenuItem itemSector = new JMenuItem("Sector");
		JMenuItem itemCorner = new JMenuItem("Corner");
		// TODO: preferences submenu
		itemUndo.addActionListener(this);
		itemRedo.addActionListener(this);
		itemSector.addActionListener(this);
		itemCorner.addActionListener(this);
		editMenu.add(itemUndo);
		editMenu.add(itemRedo);
		editMenu.add(submenuNew);
		editMenu.add(submenuPreferences);
		submenuNew.add(itemSector);
		submenuNew.add(itemCorner);
		return editMenu;

	}
}
