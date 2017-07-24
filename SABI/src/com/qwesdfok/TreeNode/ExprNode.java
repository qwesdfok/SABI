package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/6.
 */
public class ExprNode extends NodeObject
{
	private Type type;

	public ExprNode(Type type)
	{
		this.type = type;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}
}
