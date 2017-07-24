package com.qwesdfok.Tokens;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Token implements Cloneable
{
	private int tag;

	public Token(int tag)
	{
		this.tag = tag;
	}

	public String fetchTokenDescription()
	{
		return "" + (char) tag;
	}

	@Override
	public Token clone()
	{
		try
		{
			return (Token) super.clone();
		} catch (CloneNotSupportedException e)
		{
			return null;
		}
	}

	@Override
	public String toString()
	{
		return "Token " + tag;
	}

	public int getTag()
	{
		return tag;
	}

	public void setTag(int tag)
	{
		this.tag = tag;
	}
}
