package com.qwesdfok.TreeNode;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by qwesd on 2016/2/7.
 */
public class ListNode extends NodeObject implements Iterable<NodeObject>,Cloneable
{
	private ArrayList<NodeObject> list = new ArrayList<>();

	public ListNode()
	{
	}

	public void add(NodeObject obj)
	{
		list.add(obj);
	}

	public NodeObject get(int index)
	{
		return list.get(index);
	}

	public int size()
	{
		return list.size();
	}

	public boolean contains(NodeObject nodeObject)
	{
		return list.contains(nodeObject);
	}

	@Override
	public Iterator<NodeObject> iterator()
	{
		return list.iterator();
	}

	@Override
	protected ListNode clone()
	{
		ListNode node = null;
		try
		{
			node = (ListNode) super.clone();
			node.list = ((ArrayList<NodeObject>) list.clone());
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return node;
	}
}
