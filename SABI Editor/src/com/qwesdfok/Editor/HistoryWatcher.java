package com.qwesdfok.Editor;

import java.util.ArrayList;

/**
 * Created by qwesd on 2016/2/26.
 */
public class HistoryWatcher extends Thread
{
	private int historyMaxSize = 5;
	private int historyIndex = -1;
	private int historyCount = 0;
	private char inputChar = ' ';
	private long time = System.currentTimeMillis();
	private boolean running = true;
	private boolean idleSaved = false;
	private boolean inputted = false;
	private boolean typing = false;
	private MainWindow mainWindow;
	private ArrayList<String> historyBuffer = new ArrayList<>();

	public HistoryWatcher(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

	public String fetchUndoData()
	{
		if (typing)
			historyIndex--;
		else
			historyIndex -= 2;
		if (historyIndex < 0)
		{
			historyIndex = 0;
			return "";
		}
		return historyBuffer.get(historyIndex);
	}

	public String fetchRedoData()
	{
		if (historyIndex >= historyCount - 1)
			return null;
		historyIndex++;
		return historyBuffer.get(historyIndex);
	}

	@Override
	public void run()
	{
		try
		{
			while (running)
			{
				if (inputted)
				{
					idleSaved = false;
					inputted = false;
					time = System.currentTimeMillis();
					//If undo & input, remove invalid history
					if (historyIndex != historyCount - 1)
					{
						for (int i = 0; i < historyCount - 1 - historyIndex; i++)
						{
							historyBuffer.remove(historyIndex + 1);
						}
						historyCount = historyIndex + 1;
					}
					if (("" + inputChar).matches("\\s|;|}"))
					{
						pushToBuffer();
						typing = false;
					} else
					{
						typing = true;
					}
				} else
				{
					if (System.currentTimeMillis() - time >= 1000)
					{
						time = System.currentTimeMillis();
						if (!idleSaved)
						{
							pushToBuffer();
							idleSaved = true;
						}
					}
				}
				Thread.sleep(50);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void pushToBuffer()
	{
		String data = mainWindow.fetchAllInput();
		if (historyBuffer.size() != 0 && data.equals(historyBuffer.get(historyBuffer.size() - 1)))
			return;
		if (historyBuffer.size() >= historyMaxSize)
		{
			historyBuffer.remove(0);
		}
		historyBuffer.add(data);
		historyIndex++;
		if (historyIndex >= historyMaxSize)
			historyIndex = historyMaxSize - 1;
		historyCount++;
		if (historyCount > historyMaxSize)
			historyCount = historyMaxSize;
	}

	public char getInputChar()
	{
		return inputChar;
	}

	public void setInputChar(char inputChar)
	{
		this.inputChar = inputChar;
	}

	public boolean isInputted()
	{
		return inputted;
	}

	public void setInputted(boolean inputted)
	{
		this.inputted = inputted;
	}

	public boolean isRunning()
	{
		return running;
	}

	public void setRunning(boolean running)
	{
		this.running = running;
	}

	public int getHistoryMaxSize()
	{
		return historyMaxSize;
	}

	public void setHistoryMaxSize(int historyMaxSize)
	{
		this.historyMaxSize = historyMaxSize;
	}
}
