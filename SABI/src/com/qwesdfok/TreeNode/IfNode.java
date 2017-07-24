package com.qwesdfok.TreeNode;

import java.util.ArrayList;

/**
 * Created by qwesd on 2016/2/7.
 */
public class IfNode extends DefinitionNode
{
	private ExprNode condition;
	private ListNode trueDataDef;
	private boolean existElse=false;
	private ListNode falseDataDef;

	public IfNode(ExprNode condition, ListNode trueDataDef, boolean existElse, ListNode falseDataDef)
	{
		super(null);
		this.condition = condition;
		this.trueDataDef = trueDataDef;
		this.existElse = existElse;
		this.falseDataDef = falseDataDef;
	}

	public ListNode fetchAllDefinition()
	{
		if (existElse)
		{
			ListNode list = trueDataDef.clone();
			for (NodeObject nodeObject : falseDataDef)
			{
				if(!list.contains(nodeObject))
					list.add(nodeObject);
			}
			return list;
		}
		return trueDataDef;
	}

	public ExprNode getCondition()
	{
		return condition;
	}

	public void setCondition(ExprNode condition)
	{
		this.condition = condition;
	}

	public ListNode getTrueDataDef()
	{
		return trueDataDef;
	}

	public void setTrueDataDef(ListNode trueDataDef)
	{
		this.trueDataDef = trueDataDef;
	}

	public boolean isExistElse()
	{
		return existElse;
	}

	public void setExistElse(boolean existElse)
	{
		this.existElse = existElse;
	}

	public ListNode getFalseDataDef()
	{
		return falseDataDef;
	}

	public void setFalseDataDef(ListNode falseDataDef)
	{
		this.falseDataDef = falseDataDef;
	}
}
