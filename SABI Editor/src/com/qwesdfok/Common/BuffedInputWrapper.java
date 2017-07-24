package com.qwesdfok.Common;

import java.io.IOException;

/**
 * Created by qwesd on 2016/2/23.
 */
public class BuffedInputWrapper implements InputWrapperInterface
{
	private StringBuilder buffer;
	private int index = 0;

	public BuffedInputWrapper()
	{
		buffer = new StringBuilder();
	}
	public BuffedInputWrapper(String data)
	{
		buffer = new StringBuilder(data);
	}

	public BuffedInputWrapper(StringBuilder buffer)
	{
		if (buffer == null)
			buffer = new StringBuilder();
		this.buffer = new StringBuilder(buffer);
	}

	@Override
	public int read() throws IOException
	{
		if (index >= buffer.length())
			return -1;
		index++;
		return (int) buffer.charAt(index - 1);
	}

	@Override
	public void close() throws IOException
	{
		buffer.delete(0, buffer.length());
		index=0;
	}

	@Override
	public String getFileName()
	{
		return null;
	}

	@Override
	public void setEncoding(String encoding)
	{
	}

	@Override
	public String getEncoding()
	{
		return "UTF-16";
	}
}
