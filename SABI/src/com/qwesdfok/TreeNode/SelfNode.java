package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/9.
 */
public class SelfNode extends DefinitionNode
{
	private Type type;

	public SelfNode(Type type, String name)
	{
		super(name);
		this.type = type;
	}

	public Type getType()
	{
		return type;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SelfNode selfNode = (SelfNode) o;

		if (type != null ? !type.equals(selfNode.type) : selfNode.type != null) return false;
		return name != null ? name.equals(selfNode.name) : selfNode.name == null;

	}

	@Override
	public int hashCode()
	{
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	public void setType(Type type)
	{
		this.type = type;
	}
}
