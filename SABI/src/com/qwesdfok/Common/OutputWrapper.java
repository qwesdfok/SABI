package com.qwesdfok.Common;

import java.io.*;

/**
 * Created by qwesd on 2016/2/2.
 */
public class OutputWrapper implements OutputWrapperInterface
{
	private String fileName;
	private FileOutputStream outputStream;
	private boolean valid = false;

	public OutputWrapper(String fileName)
	{
		this.fileName = fileName;
		File fileTarget = new File(fileName);
		try
		{
			outputStream = new FileOutputStream(fileTarget);
			valid = true;
		} catch (FileNotFoundException e)
		{
			valid = false;
		}
	}

	public OutputWrapper(String fileName, boolean appendMode)
	{
		this.fileName = fileName;
		File fileTarget = new File(fileName);
		try
		{
			outputStream = new FileOutputStream(fileTarget,appendMode);
			valid = true;
		} catch (FileNotFoundException e)
		{
			valid = false;
		}
	}

	public OutputWrapper(File file)
	{
		this.fileName = file.getAbsolutePath();
		try
		{
			outputStream = new FileOutputStream(file);
			valid = true;
		} catch (FileNotFoundException e)
		{
			valid = false;
		}
	}

	public void rewrite(String newData) throws IOException
	{
		try
		{
			if(outputStream!=null)
				outputStream.close();
			outputStream = new FileOutputStream(fileName);
		} catch (FileNotFoundException e)
		{
			throw new IOException(e.getMessage());
		}
		write(newData);
	}

	@Override
	public void write(String data) throws IOException
	{
		outputStream.write(data.getBytes());
		outputStream.flush();
	}

	@Override
	public void close() throws IOException
	{
		outputStream.close();
	}

	@Override
	public String getFileName()
	{
		return fileName;
	}

	public boolean isValid()
	{
		return valid;
	}
}
