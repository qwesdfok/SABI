package com.qwesdfok.TreeNode;

import com.qwesdfok.Tokens.Type;

/**
 * Created by qwesd on 2016/2/7.
 */
public class MutableNode extends DefinitionNode
{
	private Type type;
	private ExprNode expr;
	private String format;

	public MutableNode(Type type, String name, ExprNode expr,String format)
	{
		super(name);
		this.type = type;
		this.expr = expr;
		this.format = format;
	}

	public String fetchConfigStr()
	{
		return name + ".config(\"" + Type.fetchPrimaryType(type).getTypeName() + "\"," + expr.fetchExprStr() + "," + type.getTypeName() + "::getInstance);\n";
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public ExprNode getExpr()
	{
		return expr;
	}

	public void setExpr(ExprNode expr)
	{
		this.expr = expr;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}
}
