package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.FunctionToken;
import com.qwesdfok.Tokens.Word;

/**
 * Created by qwesd on 2016/2/5.
 */
public class FunctionNode extends DefinitionNode
{
	private FunctionToken functionToken;

	public FunctionNode(FunctionToken functionToken)
	{
		super(functionToken.getName());
		this.functionToken = functionToken;
	}

	public String fetchModifyStr()
	{
		return functionToken.fetchModifyString();
	}

	public int getModify()
	{
		return functionToken.getModify();
	}

	public void setModify(int modify)
	{
		functionToken.setModify(modify);
	}

	public String getType()
	{
		return functionToken.getType();
	}

	public void setType(String type)
	{
		functionToken.setType(type);
	}

	public void setName(String name)
	{
		super.setName(name);
		functionToken.setName(name);
	}

	public String getSignature()
	{
		return functionToken.getSignature();
	}

	public void setSignature(String signature)
	{
		functionToken.setSignature(signature);
	}

	public String getBody()
	{
		return functionToken.getBody();
	}

	public void setBody(String body)
	{
		functionToken.setBody(body);
	}

	public FunctionToken getFunctionToken()
	{
		return functionToken;
	}

	public void setFunctionToken(FunctionToken functionToken)
	{
		this.functionToken = functionToken;
	}
}
