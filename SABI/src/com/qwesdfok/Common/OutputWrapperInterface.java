package com.qwesdfok.Common;

import java.io.IOException;

/**
 * Created by qwesd on 2016/2/23.
 */
public interface OutputWrapperInterface
{
	void write(String data) throws IOException;

	void close() throws IOException;

	String getFileName();
}
