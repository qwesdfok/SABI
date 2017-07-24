package com.qwesdfok.Editor;

import com.qwesdfok.Common.BuffedInputWrapper;
import com.qwesdfok.Common.BuffedOutputWrapper;
import com.qwesdfok.Common.InputWrapper;
import com.qwesdfok.Common.OutputWrapper;
import com.qwesdfok.Lexer.Lexer;
import com.qwesdfok.Parser.Parser;
import com.qwesdfok.Translate.TranslateImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by qwesd on 2016/2/23.
 */
public class Editor
{
	private int width = 800;
	private int height = 600;
	private boolean saved = false;
	private String encoding = "UTF-8";
	private HistoryWatcher historyWatcher;
	private OutputWrapper outputWrapper = null;
	private MainWindow mainWindow = new MainWindow(this, width, height);

	public Editor()
	{
		mainWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				if (saved)
					return;
				JDialog dialog = new JDialog(mainWindow.getInstance(), "Save?", true);
				JTextField textField = new JTextField("Whether to save this document?");
				JButton yesButton = new JButton("Yes");
				JButton noButton = new JButton("No");
				JPanel buttonPanel = new JPanel();
				textField.setEditable(false);
				dialog.setSize(200, 100);
				dialog.setLayout(new BorderLayout());
				dialog.setLocationRelativeTo(mainWindow.getInstance());
				buttonPanel.add(yesButton);
				buttonPanel.add(noButton);
				dialog.add(textField, BorderLayout.NORTH);
				dialog.add(buttonPanel, BorderLayout.SOUTH);
				yesButton.addActionListener((event) ->
				{
					try
					{
						if (outputWrapper == null)
						{
							File file = mainWindow.fetchFileSelected(false);
							if (file == null)
								return;
							outputWrapper = new OutputWrapper(file);
							outputWrapper.write(mainWindow.fetchAllInput());
						} else
						{
							outputWrapper.rewrite(mainWindow.fetchAllInput());
						}
						outputWrapper.close();
					} catch (Exception exc)
					{
						exc.printStackTrace();
					}
					dialog.dispose();
				});
				noButton.addActionListener((event) -> {
					dialog.dispose();
				});
				historyWatcher.setRunning(false);
				dialog.setVisible(true);
			}
		});
		mainWindow.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				saved = false;
				if (e.isControlDown() || e.isAltDown())
					return;
				historyWatcher.setInputChar(e.getKeyChar());
				historyWatcher.setInputted(true);
			}
		});
		mainWindow.addMenuItemActionListener(this::menuItemListenerFunction);
		historyWatcher = new HistoryWatcher(mainWindow);
		historyWatcher.start();
		showWindow();
	}

	public void showWindow()
	{
		mainWindow.setVisible(true);
	}

	public void showData(String input)
	{
		try
		{
			Parser parser = new Parser(new BuffedInputWrapper(input));
			parser.startParse();
			Lexer lexer = parser.getLexer();
			BuffedInputWrapper wrapper = new BuffedInputWrapper(input);
			StringBuilder sb = new StringBuilder();
			removeAllInput();
			int current = wrapper.read();
			boolean constantStr = false;
			while (current != -1)
			{
				if (("" + (char) current).matches("[A-Za-z_]"))
				{
					sb.append((char) current);
				} else
				{
					if (sb.length() != 0)
					{
						String str = sb.toString();
						sb.delete(0, sb.length());
						if (!constantStr)
						{
							if (lexer.getKeywordMap().containsKey(str))
								mainWindow.printToEditor(str, Color.BLUE);
							else if (lexer.getBuildInTypeMap().containsKey(str) || lexer.getCustomTypeMap().containsKey(str))
								mainWindow.printToEditor(str, new Color(128, 16, 255));
							else
								mainWindow.printToEditor(str, Color.BLACK);
						} else
						{
							mainWindow.printToEditor(str, Color.BLACK);
						}
					}
					if (current == '"')
						constantStr = !constantStr;
					mainWindow.printToEditor("" + (char) current, Color.BLACK);
				}
				current = wrapper.read();
			}
			if (sb.length() != 0)
				mainWindow.printToEditor(sb.toString(), Color.BLACK);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void removeAllInput()
	{
		try
		{
			mainWindow.fetchDocument().remove(0, mainWindow.fetchDocument().getLength());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void menuItemListenerFunction(ActionEvent event)
	{
		try
		{
			String input = mainWindow.fetchAllInput();
			if (input == null)
				return;
			if (event.getActionCommand().equals("New"))
			{
				if (input.length() != 0)
				{
					if (!saved)
					{
						if (outputWrapper == null)
						{
							File file = mainWindow.fetchFileSelected(false);
							if (file == null)
								return;
							outputWrapper = new OutputWrapper(file);
							outputWrapper.write(input);
							outputWrapper.close();
						} else
						{
							outputWrapper.rewrite(input);
							outputWrapper.close();
						}
					}
					mainWindow.changeOpenFilePath("EMPTY");
					mainWindow.changeSaveFilePath("EMPTY");
					outputWrapper = null;
					saved = false;
				}
				removeAllInput();
			} else if (event.getActionCommand().equals("Open"))
			{
				File file;
				file = mainWindow.fetchFileSelected(true);
				if (file == null)
					return;
				mainWindow.changeOpenFilePath(file.getAbsolutePath());
				mainWindow.changeSaveFilePath(file.getAbsolutePath());
				InputWrapper inputWrapper = new InputWrapper(file);
				inputWrapper.setEncoding(encoding);
				StringBuilder sb = new StringBuilder();
				int data = inputWrapper.read();
				while (data != -1)
				{
					sb.append((char) data);
					data = inputWrapper.read();
				}
				if (!saved && outputWrapper != null)
				{
					outputWrapper.rewrite(input);
					outputWrapper.close();
				}
				saved = true;
				outputWrapper = new OutputWrapper(inputWrapper.getFileName(), true);
				showData(sb.toString());
				historyWatcher.pushToBuffer();
				inputWrapper.close();
			} else if (event.getActionCommand().equals("Save"))
			{
				if (saved)
					return;
				if (outputWrapper == null)
				{
					File file = mainWindow.fetchFileSelected(false);
					if (file == null)
						return;
					mainWindow.changeSaveFilePath(file.getAbsolutePath());
					outputWrapper = new OutputWrapper(file);
					outputWrapper.write(input);
				} else
					outputWrapper.rewrite(input);
				saved = true;
			} else if (event.getActionCommand().equals("SaveAs"))
			{
				File file = mainWindow.fetchFileSelected(false);
				if (file == null)
					return;
				mainWindow.changeSaveFilePath(file.getAbsolutePath());
//				if(!saved)
//				{
//					JDialog dialog = new JDialog(mainWindow.getInstance(), "Save Old File?");
//					dialog.setLayout(new GridLayout(0, 2));
//					dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//					dialog.setSize(300, 100);
//					dialog.setLocationRelativeTo(mainWindow.getInstance());
//					JTextField saveField = new JTextField("Save Old File?");
//					JTextField emptyField = new JTextField("");
//					JButton confirm = new JButton("Confirm");
//					JButton cancel = new JButton("Cancel");
//					saveField.setEditable(false);
//					emptyField.setEditable(false);
//					dialog.add(saveField);
//					dialog.add(emptyField);
//					dialog.add(confirm);
//					dialog.add(cancel);
//					confirm.addActionListener(new ActionListener()
//					{
//						@Override
//						public void actionPerformed(ActionEvent e)
//						{
//							try
//							{
//								if (outputWrapper != null)
//								{
//									outputWrapper.rewrite(input);
//									outputWrapper.close();
//								} else
//								{
//									File saveFile = mainWindow.fetchFileSelected(false);
//									if (saveFile == null)
//										return;
//									outputWrapper = new OutputWrapper(file);
//									outputWrapper.write(input);
//									outputWrapper.close();
//									saved = true;
//								}
//							} catch (IOException exc)
//							{
//
//							}
//							dialog.dispose();
//						}
//					});
//					cancel.addActionListener((e) -> {
//						dialog.dispose();
//					});
//					dialog.setVisible(true);
//				}
				outputWrapper = new OutputWrapper(file);
				outputWrapper.write(input);
				saved = true;
			} else if (event.getActionCommand().equals("Settings"))
			{
				mainWindow.showSettingsWindow();
			} else if (event.getActionCommand().equals("Validate"))
			{
				showData(input);
				Parser parser = new Parser(new BuffedInputWrapper(input));
				parser.startParse();
				if (!parser.isValid())
					mainWindow.printToCommand("Error: " + parser.getLastError());
				else
					mainWindow.printToCommand("Validator: OK!");
			} else if (event.getActionCommand().equals("Generate"))
			{
				File file = mainWindow.fetchFileSelected(false);
				Parser parser = new Parser(new BuffedInputWrapper(input), new OutputWrapper(file), new TranslateImpl());
				parser.startParse();
				if (!parser.isValid())
					mainWindow.printToCommand("Error: " + parser.getLastError());
				else
					mainWindow.printToCommand("Translator: Done!");
			} else if (event.getActionCommand().equals("LookUp"))
			{
				BuffedOutputWrapper output = new BuffedOutputWrapper();
				Parser parser = new Parser(new BuffedInputWrapper(input), output, new TranslateImpl());
				parser.startParse();
				if (!parser.isValid())
					mainWindow.printToCommand("Error: " + parser.getLastError());
				else
				{
					mainWindow.printToCommand("Translator: Done!");
					mainWindow.showLookUpWindow(output.fetchData());
				}
			} else if (event.getActionCommand().equals("Undo"))
			{
				showData(historyWatcher.fetchUndoData());
			} else if (event.getActionCommand().equals("Redo"))
			{
				String data = historyWatcher.fetchRedoData();
				if (data == null)
					return;
				showData(data);
			}
		} catch (UnsupportedEncodingException e)
		{
			mainWindow.printToCommand(e.getMessage());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public HistoryWatcher getHistoryWatcher()
	{
		return historyWatcher;
	}

	public String getEncoding()
	{
		return encoding;
	}

	public void setEncoding(String encoding)
	{
		this.encoding = encoding;
	}
}
