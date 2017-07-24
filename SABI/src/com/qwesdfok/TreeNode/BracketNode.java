package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/9.
 */
public class BracketNode extends ExprNode
{
	private String leftBracket;
	private ExprNode value;
	private String rightBracket;

	public BracketNode(String leftBracket, ExprNode value, String rightBracket)
	{
		super(null);
		this.leftBracket = leftBracket;
		this.value = value;
		this.rightBracket = rightBracket;
	}

	public String getLeftBracket()
	{
		return leftBracket;
	}

	public void setLeftBracket(String leftBracket)
	{
		this.leftBracket = leftBracket;
	}

	public ExprNode getValue()
	{
		return value;
	}

	public void setValue(ExprNode value)
	{
		this.value = value;
	}

	public String getRightBracket()
	{
		return rightBracket;
	}

	public void setRightBracket(String rightBracket)
	{
		this.rightBracket = rightBracket;
	}
}
