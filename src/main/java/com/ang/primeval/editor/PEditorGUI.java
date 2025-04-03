package com.ang.primeval.editor;

import java.awt.event.*;
import javax.swing.*;
import java.io.File;

import com.ang.primeval.graphics.PRenderer;

public class PEditorGUI implements ActionListener, ItemListener {
	private JFileChooser chooser = new JFileChooser();
	private JFrame frame;
	private PEditorInterface ei;

	public PEditorGUI(PRenderer renderer, PEditorInterface ei) {
		this.frame = renderer.frame();	
		this.ei = ei;
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
			int newSelection = JOptionPane.showConfirmDialog(frame, "Save before exiting?",
					"New", JOptionPane.YES_NO_OPTION);
			switch (newSelection) {
			case JOptionPane.YES_OPTION:
				ei.save(null);
				ei.newFile();
				break;

			case JOptionPane.NO_OPTION:
				ei.newFile();
				break;

			case JOptionPane.CANCEL_OPTION:
				break;

			}
			break;
			
		case "Open":
			int openSelection = chooser.showOpenDialog(frame);
			if (openSelection == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				ei.open(file);
			} 
			break;

		case "Save":
			ei.save(null);
			break;

		case "Save As":
			int saveAsSelection = chooser.showSaveDialog(frame);
			if (saveAsSelection == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				ei.save(file);
			} 
			break;

		case "Exit":
			int exitSelection = JOptionPane.showConfirmDialog(frame, "Save before exiting?",
					"Exit", JOptionPane.YES_NO_OPTION);
			switch (exitSelection) {
			case JOptionPane.YES_OPTION:
				ei.save(null);
				ei.exit();
				break;

			case JOptionPane.NO_OPTION:
				ei.exit();
				break;

			case JOptionPane.CANCEL_OPTION:
				break;

			}
			break;

		case "Undo":
			ei.undo();
			break;

		case "Redo":
			ei.redo();
			break;

		case "Sector":
			ei.newSector();
			break;

		case "Corner":
			ei.newCorner();
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
