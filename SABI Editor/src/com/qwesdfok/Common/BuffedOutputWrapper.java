package com.qwesdfok.Common;

import java.io.IOException;

/**
 * Created by qwesd on 2016/3/10.
 */
public class BuffedOutputWrapper implements OutputWrapperInterface
{
	private StringBuilder buffer;

	public BuffedOutputWrapper()
	{
		this.buffer = new StringBuilder();
	}
	public BuffedOutputWrapper(StringBuilder buffer)
	{
		if(buffer==null)
			buffer = new StringBuilder();
		this.buffer = buffer;
	}

	public String fetchData()
	{
		return buffer.toString();
	}

	@Override
	public void write(String data) throws IOException
	{
		buffer.append(data);
	}

	@Override
	public void close() throws IOException
	{
	}

	@Override
	public String getFileName()
	{
		return null;
	}
}
