package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/5.
 */
public class ConstantStringNode extends ExprNode
{
	private String str;

	public ConstantStringNode(String str)
	{
		super(Type.STRING);
		this.str = str;
	}

	public String getStr()
	{
		return str;
	}

	public void setStr(String str)
	{
		this.str = str;
	}
}
