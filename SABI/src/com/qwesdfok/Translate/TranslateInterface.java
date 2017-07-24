package com.qwesdfok.Translate;

import com.qwesdfok.Common.OutputWrapperInterface;
import com.qwesdfok.Parser.Env;
import com.qwesdfok.TreeNode.FunctionNode;
import com.qwesdfok.TreeNode.ListNode;
import com.qwesdfok.TreeNode.NodeObject;
import com.qwesdfok.TreeNode.StructNode;

import java.io.IOException;

/**
 * Created by qwesd on 2016/2/16.
 */
public interface TranslateInterface
{
	void writeLibData(OutputWrapperInterface outputWrapper, Env env) throws IOException;

	void writeDeclareCode(OutputWrapperInterface outputWrapper, NodeObject nodeObject) throws IOException;

	void writeRuntimeCode(OutputWrapperInterface outputWrapper, ListNode listNode) throws IOException;

	void generateFunctionCode(FunctionNode functionNode, StructNode structNode);

	String fetchDeclareCode(NodeObject nodeObject);

	String fetchExpressionCode(NodeObject nodeObject);

	String fetchRuntimeReadCode(NodeObject nodeObject);

	String fetchRuntimeWriteCode(NodeObject nodeObject);
}
