package com.qwesdfok.Editor;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by qwesd on 2016/2/25.
 */
public class MainWindow
{
	private int fontSize = 12;
	private HistoryWatcher historyWatcher;
	private Editor editor;
	private JFrame mainWindow = new JFrame("SABI Editor");
	private JMenuItem fileNewItem = new JMenuItem("New");
	private JMenuItem fileOpenItem = new JMenuItem("Open");
	private JMenuItem fileSaveItem = new JMenuItem("Save");
	private JMenuItem fileSaveAsItem = new JMenuItem("SaveAs");
	private JMenuItem fileSettingsItem = new JMenuItem("Settings");
	private JMenuItem projectValidateItem = new JMenuItem("Validate");
	private JMenuItem projectGenerateItem = new JMenuItem("Generate");
	private JMenuItem projectLookUpItem = new JMenuItem("LookUp");
	private JMenuItem editUndoItem = new JMenuItem("Undo");
	private JMenuItem editRedoItem = new JMenuItem("Redo");
	private JTextPane editorArea = new JTextPane();
	private JTextPane commandArea = new JTextPane();
	private JTextField openPathField = new JTextField();
	private JTextField savePathField = new JTextField();

	public MainWindow(Editor editor, int width, int height)
	{
		this.editor = editor;
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu projectMenu = new JMenu("Project");
		JMenu editMenu = new JMenu("Edit");
		GridBagLayout layout = new GridBagLayout();
		mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainWindow.setSize(width, height);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		addToMainWindow(menuBar, constraints, 0, 0, 1, 1, 1.0, 0.0);
		constraints.insets = new Insets(0, 0, 5, 0);
		addToMainWindow(new JScrollPane(editorArea), constraints, 0, 1, 1, 3, 1.0, 1.0);
		addToMainWindow(new JScrollPane(commandArea), constraints, 0, 4, 1, 1, 1.0, 0.3);
		addToMainWindow(openPathField, constraints, 0, 5, 1, 1, 1.0, 0.0);
		addToMainWindow(savePathField, constraints, 0, 6, 1, 1, 1.0, 0.0);

		openPathField.setEditable(false);
		savePathField.setEditable(false);
		changeOpenFilePath("EMPTY");
		changeSaveFilePath("EMPTY");
		menuBar.add(fileMenu);
		menuBar.add(projectMenu);
		menuBar.add(editMenu);

		fileMenu.add(fileNewItem);
		fileMenu.add(fileOpenItem);
		fileMenu.add(fileSaveItem);
		fileMenu.add(fileSaveAsItem);
		fileMenu.add(fileSettingsItem);

		projectMenu.add(projectValidateItem);
		projectMenu.add(projectGenerateItem);
		projectMenu.add(projectLookUpItem);

		editMenu.add(editUndoItem);
		editMenu.add(editRedoItem);

		fileNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		fileOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		fileSaveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		fileSettingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		projectValidateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
		projectGenerateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		projectLookUpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
		editUndoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editRedoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
	}

	public void addMenuItemActionListener(ActionListener actionListener)
	{
		fileNewItem.addActionListener(actionListener);
		fileOpenItem.addActionListener(actionListener);
		fileSaveItem.addActionListener(actionListener);
		fileSaveAsItem.addActionListener(actionListener);
		fileSettingsItem.addActionListener(actionListener);
		projectValidateItem.addActionListener(actionListener);
		projectGenerateItem.addActionListener(actionListener);
		projectLookUpItem.addActionListener(actionListener);
		editUndoItem.addActionListener(actionListener);
		editRedoItem.addActionListener(actionListener);
	}

	public void addWindowListener(WindowListener listener)
	{
		mainWindow.addWindowListener(listener);
	}

	public void addKeyListener(KeyListener listener)
	{
		editorArea.addKeyListener(listener);

	}

	public void setVisible(boolean show)
	{
		mainWindow.setVisible(show);
	}

	public String fetchAllInput()
	{
		try
		{
			return editorArea.getDocument().getText(0, editorArea.getDocument().getLength());
		} catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public File fetchFileSelected(boolean open)
	{
		JFileChooser fileChooser = new JFileChooser();
		if (open)
			fileChooser.showOpenDialog(mainWindow);
		else
			fileChooser.showSaveDialog(mainWindow);
		return fileChooser.getSelectedFile();
	}

	public Document fetchDocument()
	{
		return editorArea.getDocument();
	}

	public void printToCommand(String data)
	{
		if (!data.endsWith("\n"))
			data = data + "\n";
		printToComponent(commandArea, data, Color.BLACK);
	}

	public void printToEditor(String data, Color color)
	{
		printToComponent(editorArea, data, color);
	}

	public JFrame getInstance()
	{
		return mainWindow;
	}

	public void changeOpenFilePath(String path)
	{
		openPathField.setText("OpenPath: " + path);
	}

	public void changeSaveFilePath(String data)
	{
		savePathField.setText("SavePath: " + data);
	}

	public void showSettingsWindow()
	{
		this.historyWatcher = editor.getHistoryWatcher();
		JDialog dialog = new JDialog(mainWindow, "Settings", true);
		dialog.setSize(300, 120);
		dialog.setLocationRelativeTo(mainWindow);
		dialog.setLayout(new GridLayout(0, 2));
		JTextField fontSizeField = new JTextField("" + fontSize);
		JTextField bufferSizeField = new JTextField("" + historyWatcher.getHistoryMaxSize());
		JTextField encodingField = new JTextField(editor.getEncoding());
		JButton confirmButton = new JButton("Confirm");
		JButton cancelButton = new JButton("Cancel");
		dialog.add(new JLabel("FontSize: "));
		dialog.add(fontSizeField);
		dialog.add(new JLabel("BufferSize: "));
		dialog.add(bufferSizeField);
		dialog.add(new JLabel("Encoding: "));
		dialog.add(encodingField);
		dialog.add(confirmButton);
		dialog.add(cancelButton);
		confirmButton.addActionListener((e) -> {
			fontSize = Integer.parseInt(fontSizeField.getText());
			if (fontSize < 0)
				fontSize = -fontSize;
			historyWatcher.setHistoryMaxSize(Integer.parseInt(bufferSizeField.getText()));
			if (historyWatcher.getHistoryMaxSize() < 0)
				historyWatcher.setHistoryMaxSize(-historyWatcher.getHistoryMaxSize());
			editor.setEncoding(encodingField.getText());
			editor.showData(fetchAllInput());
			printToCommand("Setting applied.");
		});
		cancelButton.addActionListener((e) -> {
			dialog.dispose();
		});
		dialog.setVisible(true);
	}

	public void showLookUpWindow(String output)
	{
		JDialog dialog = new JDialog(mainWindow, "LookUp", true);
		dialog.setSize(800, 600);
		dialog.setLocationRelativeTo(mainWindow);
		JTextArea textArea = new JTextArea(output);
		dialog.add(new JScrollPane(textArea));
		dialog.setVisible(true);
	}

	private void printToComponent(JTextPane pane, String data, Color color)
	{
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, color);
		StyleConstants.setFontSize(attributeSet, fontSize);
		pane.setCharacterAttributes(attributeSet, true);
		pane.replaceSelection(data);
	}

	private void addToMainWindow(Component component, GridBagConstraints constraints, int x, int y, int w, int h, double dx, double dy)
	{
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = w;
		constraints.gridheight = h;
		constraints.weightx = dx;
		constraints.weighty = dy;
		mainWindow.add(component, constraints);
	}
}
