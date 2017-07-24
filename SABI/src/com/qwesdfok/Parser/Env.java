package com.qwesdfok.Parser;

import com.qwesdfok.Tokens.Type;
import com.qwesdfok.TreeNode.FunctionNode;
import com.qwesdfok.TreeNode.IdNode;
import com.qwesdfok.TreeNode.SelfNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by qwesd on 2016/2/4.
 */
public class Env
{
	public static ArrayList<String> staticConfigList = new ArrayList<>(Arrays.asList("_input_", "_output_", "_bigend_", "_outsepa_", "_nextsepa_", "_skip_"));
	private HashMap<String, IdNode> idMap = new HashMap<>();
	private HashMap<String, SelfNode> selfMap = new HashMap<>();
	private HashMap<String, FunctionNode> functionMap = new HashMap<>();
	private HashMap<String, String> configMap = new HashMap<>();
	private Env parent;
	private Env firstChildren;
	private Env lastChildren;
	private Env sibling;

	public Env(Env parent)
	{
		this.parent = parent;
		if (parent != null)
		{
			if (parent.firstChildren == null)
			{
				parent.firstChildren = this;
				parent.lastChildren = this;
			} else
			{
				parent.lastChildren.sibling = this;
				parent.lastChildren = this;
			}
		}
	}

	public IdNode fetchIdInfo(String name)
	{
		Env p = this;
		while (p != null)
		{
			if (p.idMap.containsKey(name))
			{
				return p.idMap.get(name);
			}
			p = p.parent;
		}
		return null;
	}

	public void storeIdInfo(IdNode node)
	{
		idMap.put(node.getName(), node);
	}

	public FunctionNode fetchFunctionInfo(String name)
	{
		return functionMap.get(name);
	}

	public void storeFunctionInfo(FunctionNode functionNode)
	{
		functionMap.put(functionNode.getName(), functionNode);
	}

	public SelfNode fetchSelfInfo(String name)
	{
		return selfMap.get(name);
	}

	public void storeSelfInfo(SelfNode selfNode)
	{
		selfMap.put(selfNode.getName(), selfNode);
	}

	public String fetchConfigInfo(String name)
	{
		return configMap.get(name);
	}

	public void storeConfigInfo(String name, String value)
	{
		configMap.put(name, value);
	}

	public HashMap<String, IdNode> getIdMap()
	{
		return idMap;
	}

	public HashMap<String, SelfNode> getSelfMap()
	{
		return selfMap;
	}

	public HashMap<String, FunctionNode> getFunctionMap()
	{
		return functionMap;
	}

	public HashMap<String, String> getConfigMap()
	{
		return configMap;
	}
}
