package com.qwesdfok.Common;

import java.io.IOException;

/**
 * Created by qwesd on 2016/2/23.
 */
public interface InputWrapperInterface
{
	int read() throws IOException;

	void close() throws IOException;

	void setEncoding(String encoding)throws IOException;

	String getEncoding();

	String getFileName();
}
