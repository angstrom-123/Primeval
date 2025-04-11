package com.ang.peEditor.selector;

import com.ang.peLib.exceptions.*;
import com.ang.peLib.files.PFileReader;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class PSelector {
	private final int			WIDTH 					= 400;
	private final int			HEIGHT 					= 300;
	private final int			BUTTON_WIDTH 			= WIDTH;
	private final int			BUTTON_HEIGHT 			= 25;
	private final int			FILE_CONTAINER_HEIGHT 	= HEIGHT - BUTTON_HEIGHT;
	private JFrame				frame;
	private JPanel				filesContainer;
	private JScrollPane			scrollPane;
	private JTextField			textField;
	private JPanel				inputContainer;
	private JButton				submitButton;
	private String				selection;
	private PSelectorType 		type;
	private JFrame 				parent;
	private String 				path;
	private PSelectorListener 	listener;

	public PSelector(PSelectorType type, JFrame parent, 
			String path, PSelectorListener listener) {
		this.type = type;
		this.parent = parent;
		this.path = path;
		this.listener = listener;
		init();
		initComponents();
	}

	public PSelectorType getType() {
		return type;

	}

	public String getSelection() {
		return selection;

	}

	public void show() {
		frame.setVisible(true);
	}

	private void init() {
		frame 			= new JFrame();
		filesContainer 	= new JPanel();
		scrollPane 		= new JScrollPane(filesContainer);
		textField 		= new JTextField(30);
		inputContainer 	= new JPanel();
		submitButton 	= new JButton("Open");
		selection 		= null;
	}

	private void initComponents() {
		// init main frame
		frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setLayout(new BorderLayout(0, 0));
		// init scrollable files container
		scrollPane.setPreferredSize(new Dimension(WIDTH, FILE_CONTAINER_HEIGHT));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		filesContainer.setLayout(null);
		// adding buttons for each file
		JButton[] buttons = createFileButtons();
		for (JButton b : buttons) filesContainer.add(b);
		filesContainer.setPreferredSize(new Dimension(
					WIDTH, BUTTON_HEIGHT * buttons.length));
		// init container for input bar at bottom of screen
		inputContainer.setLayout(new BorderLayout(0, 0));
		submitButton.setActionCommand(textField.getText());
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitPressed(textField.getText());
			}
		});
		inputContainer.add(submitButton, BorderLayout.LINE_END);
		inputContainer.add(textField, BorderLayout.LINE_START);
		// adding components to main frame
		frame.add(scrollPane, BorderLayout.PAGE_START);
		frame.add(inputContainer, BorderLayout.PAGE_END);
		// configuring main frame
		frame.pack();
		frame.setLocationRelativeTo(parent);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.setResizable(false);
		frame.requestFocusInWindow();
		// automatically close selector when the editor window is closed
		parent.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}

	private JButton[] createFileButtons() {
		String[] names = findFiles();
		JButton[] buttons = new JButton[names.length];
		for (int i = 0; i < names.length; i++) {
			JButton button = new JButton(names[i]);
			button.setActionCommand(names[i]);
			button.setBounds(0, i * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
			button.setBorder(null);
			button.setForeground(Color.BLACK);
			button.setBackground(Color.WHITE);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonPressed(e);
				}
			});
			buttons[i] = button;
		}
		return buttons;

	}

	private void buttonPressed(ActionEvent e) {
		selection = e.getActionCommand();
		textField.setText(e.getActionCommand());
	}

	private void submitPressed(String text) {
		selection = text;
		listener.selectionMade(this);	
		frame.dispose();
	}

	private String[] findFiles() {
		PFileReader dirReader = new PFileReader();
		try {
			return dirReader.readSubdirectories(path, true);

		} catch (PResourceException e) {
			e.printStackTrace();
			return new String[0];

		}
	}
}
