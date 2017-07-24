package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/7.
 */
public class ImmutableNode extends DefinitionNode
{
	private Type type;

	public ImmutableNode(Type type, String name)
	{
		super(name);
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
