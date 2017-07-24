package com.qwesdfok.TreeNode;

/**
 * Created by qwesd on 2016/2/7.
 */
public class DefinitionNode extends NodeObject
{
	protected String name;

	public DefinitionNode(String name)
	{
		this.name = name;
	}

	public String fetchFormatStr(String format)
	{
		return format.replaceAll("%n", getName()).replaceAll("%%", "%");
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
