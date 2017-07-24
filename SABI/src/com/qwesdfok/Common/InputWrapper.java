package com.qwesdfok.Common;

import java.io.*;

/**
 * Created by qwesd on 2016/2/2.
 */
public class InputWrapper implements InputWrapperInterface
{
	private boolean valid = false;
	private String encoding = "UTF-16";
	private String fileName;
	private FileInputStream inputStream = null;
	private InputStreamReader streamReader = null;

	public InputWrapper(String fileName)
	{
		this.fileName = fileName;
		try
		{
			File file = new File(fileName);
			if (!file.exists())
				return;
			inputStream = new FileInputStream(fileName);
			streamReader = new InputStreamReader(inputStream);
			valid = true;
		} catch (FileNotFoundException e)
		{
			valid = false;
		}
	}

	public InputWrapper(File file)
	{
		this.fileName = file.getAbsolutePath();
		try
		{
			if (!file.exists())
				return;
			inputStream = new FileInputStream(fileName);
			streamReader = new InputStreamReader(inputStream);
			valid = true;
		} catch (FileNotFoundException e)
		{
			valid = false;
		}
	}

	@Override
	public int read() throws IOException
	{
		return  streamReader.read();
	}

	@Override
	public void close() throws IOException
	{
		streamReader.close();
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

	@Override
	public String getEncoding()
	{
		return encoding;
	}

	@Override
	public void setEncoding(String encoding)throws IOException
	{
		this.encoding = encoding;
		streamReader = new InputStreamReader(inputStream, encoding);
	}
}
