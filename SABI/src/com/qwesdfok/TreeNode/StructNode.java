package com.qwesdfok.TreeNode;

import com.qwesdfok.Parser.Env;
import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/6.
 */
public class StructNode extends DefinitionNode
{
	private Env env;
	private ListNode dataDef;

	public StructNode(String name, Env env, ListNode dataDef)
	{
		super(name);
		this.env = env;
		this.dataDef = dataDef;
	}

	public Env getEnv()
	{
		return env;
	}

	public void setEnv(Env env)
	{
		this.env = env;
	}

	public ListNode getDataDef()
	{
		return dataDef;
	}

	public void setDataDef(ListNode dataDef)
	{
		this.dataDef = dataDef;
	}
}
