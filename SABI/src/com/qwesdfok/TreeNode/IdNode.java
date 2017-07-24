package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/4.
 */
public class IdNode extends ExprNode
{
	private String name;

	public IdNode(Type type, String name)
	{
		super(type);
		this.name = name;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		IdNode idNode = (IdNode) o;

		return name != null ? name.equals(idNode.name) : idNode.name == null;

	}

	@Override
	public int hashCode()
	{
		return name != null ? name.hashCode() : 0;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
