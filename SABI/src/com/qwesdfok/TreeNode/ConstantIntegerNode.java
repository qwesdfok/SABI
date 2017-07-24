package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/7.
 */
public class ConstantIntegerNode extends ExprNode
{
	private int value;

	public ConstantIntegerNode(int value)
	{
		super(Type.INTEGER);
		this.value = value;
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
