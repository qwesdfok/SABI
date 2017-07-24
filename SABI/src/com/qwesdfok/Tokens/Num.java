package com.qwesdfok.Tokens;

/**
 * Created by qwesd on 2016/2/3.
 */
public class Num extends Token
{
	private int value;

	public Num(int value)
	{
		super(Tag.NUMBER);
		this.value = value;
	}

	@Override
	public String fetchTokenDescription()
	{
		return ""+value;
	}

	@Override
	public String toString()
	{
		return "Num: "+value;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}
}
