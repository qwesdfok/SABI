package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Token;
import com.qwesdfok.TreeNode.NodeObject;

/**
 * Created by qwesd on 2016/2/4.
 */
public class BinaryOperationNode extends ExprNode
{
	private ExprNode leftNode;
	private Token operation;
	private ExprNode rightNode;

	public BinaryOperationNode(ExprNode leftNode, Token operation, ExprNode rightNode)
	{
		super(null);
		//TODO
		this.leftNode = leftNode;
		this.operation = operation;
		this.rightNode = rightNode;
	}

	public ExprNode getLeftNode()
	{
		return leftNode;
	}

	public void setLeftNode(ExprNode leftNode)
	{
		this.leftNode = leftNode;
	}

	public ExprNode getRightNode()
	{
		return rightNode;
	}

	public void setRightNode(ExprNode rightNode)
	{
		this.rightNode = rightNode;
	}

	public Token getOperation()
	{
		return operation;
	}

	public void setOperation(Token operation)
	{
		this.operation = operation;
	}
}
