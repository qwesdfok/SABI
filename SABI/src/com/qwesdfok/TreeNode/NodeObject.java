package com.qwesdfok.TreeNode;

import com.qwesdfok.Translate.TranslateInterface;

/**
 * Created by qwesd on 2016/2/4.
 */
public class NodeObject implements Cloneable
{
	protected static TranslateInterface translate;

	public static TranslateInterface getTranslate()
	{
		return translate;
	}

	public static void setTranslate(TranslateInterface translate)
	{
		NodeObject.translate = translate;
	}

	public String fetchDeclareStr()
	{
		return translate.fetchDeclareCode(this);
	}

	public String fetchExprStr()
	{
		return translate.fetchExpressionCode(this);
	}

	public String fetchRuntimeReadStr()
	{
		return translate.fetchRuntimeReadCode(this);
	}

	public String fetchRuntimeWriteStr()
	{
		return translate.fetchRuntimeWriteCode(this);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
