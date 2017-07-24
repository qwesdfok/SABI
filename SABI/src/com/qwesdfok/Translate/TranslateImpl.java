package com.qwesdfok.Translate;

import com.qwesdfok.Common.MyLib;
import com.qwesdfok.Common.OutputWrapperInterface;
import com.qwesdfok.Parser.Env;
import com.qwesdfok.Tokens.FunctionToken;
import com.qwesdfok.TreeNode.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by qwesd on 2016/2/16.
 */
public class TranslateImpl implements TranslateInterface
{
	private ConstantString data = new ConstantString();

	@Override
	public void writeLibData(OutputWrapperInterface outputWrapper, Env env) throws IOException
	{
		outputWrapper.write(data.importStr + data.interfaceStr + data.interfaceImplStr + data.rootClassHeadStr);
		outputWrapper.write(fetchConfigInfo(env));
		outputWrapper.write(fetchFunctionInfo(env));
		outputWrapper.write(data.rootClassTailStr + data.selfClassStr);
	}

	@Override
	public void writeDeclareCode(OutputWrapperInterface outputWrapper, NodeObject nodeObject) throws IOException
	{
		outputWrapper.write(nodeObject.fetchDeclareStr());
	}

	@Override
	public void writeRuntimeCode(OutputWrapperInterface outputWrapper, ListNode listNode) throws IOException
	{
		outputWrapper.write(data.mainClassHead);
		outputWrapper.write(data.mainFunction);
		outputWrapper.write(listNode.fetchDeclareStr());
		outputWrapper.write(listNode.fetchRuntimeReadStr());
		outputWrapper.write(data.mainPad);
		outputWrapper.write(listNode.fetchRuntimeWriteStr());
		outputWrapper.write(data.mainClassTail);
	}

	@Override
	public String fetchDeclareCode(NodeObject nodeObject)
	{
		if (nodeObject instanceof StructNode)
		{
			StructNode structNode = ((StructNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			String head = "class " + structNode.getName() + " extends RootClass\n{\n";
			sb.append(head);
			sb.append(fetchConfigInfo(structNode.getEnv()));
			sb.append(structNode.getDataDef().fetchDeclareStr());
			String commonFunction = "public " + structNode.getName() + "(String name, int length){super(name, length);}\n" +
					"public static " + structNode.getName() + " getInstance(int length){return new " + structNode.getName() + "(null,length);}\n" +
					"public String getTypeName(){return \""+ structNode.getName()+"\";}\n";
			sb.append(commonFunction);
			sb.append(fetchFunctionInfo(structNode.getEnv()));
			sb.append("}//struct end\n");
			return sb.toString();
		} else if (nodeObject instanceof SelfNode)
		{
			SelfNode selfNode = ((SelfNode) nodeObject);
			return selfNode.getType().getTypeName() + " " + selfNode.getName() + "=new " + selfNode.getType().getTypeName() + ";";
		} else if (nodeObject instanceof ImmutableNode)
		{
			ImmutableNode immutableNode = ((ImmutableNode) nodeObject);
			return immutableNode.getType().getTypeName() + " " + immutableNode.getName() + "=new " + immutableNode.getType().getTypeName() + "(\"" + immutableNode.getName() + "\", " + immutableNode.getType().getLength() + ");";
		} else if (nodeObject instanceof MutableNode)
		{
			MutableNode mutableNode = ((MutableNode) nodeObject);
			String commonPart = "MutableType<" + mutableNode.getType().getTypeName() + "> " + mutableNode.getName() + "= new MutableType<>(\"" + mutableNode.getName() + "\"," + mutableNode.getType().getLength();
			if (mutableNode.getFormat() == null)
				commonPart = commonPart + ");";
			else
				commonPart = commonPart + "," + MyLib.reshape(mutableNode.getFormat()) + ");";
			return commonPart;
		} else if (nodeObject instanceof IfNode)
		{
			IfNode ifNode = ((IfNode) nodeObject);
			ListNode data = ifNode.fetchAllDefinition();
			return data.fetchDeclareStr();
		} else if (nodeObject instanceof ListNode)
		{
			ListNode listNode = ((ListNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			for (NodeObject n : listNode)
			{
				String d = n.fetchDeclareStr() + "\n";
				sb.append(d);
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public String fetchExpressionCode(NodeObject nodeObject)
	{
		if (nodeObject instanceof BinaryOperationNode)
		{
			BinaryOperationNode binaryNode = ((BinaryOperationNode) nodeObject);
			return binaryNode.getLeftNode().fetchExprStr() + binaryNode.getOperation().fetchTokenDescription() + binaryNode.getRightNode().fetchExprStr();
		} else if (nodeObject instanceof BracketNode)
		{
			BracketNode bracketNode = ((BracketNode) nodeObject);
			return bracketNode.getLeftBracket() + bracketNode.getValue().fetchExprStr() + bracketNode.getRightBracket();
		} else if (nodeObject instanceof ConstantIntegerNode)
		{
			ConstantIntegerNode integerNode = ((ConstantIntegerNode) nodeObject);
			return "" + integerNode.getValue();
		} else if (nodeObject instanceof ConstantStringNode)
		{
			ConstantStringNode stringNode = ((ConstantStringNode) nodeObject);
			return stringNode.getStr();
		} else if (nodeObject instanceof IdNode)
		{
			IdNode idNode = ((IdNode) nodeObject);
			return idNode.getName() + ".toInt()";
		}
		return "";
	}

	@Override
	public String fetchRuntimeReadCode(NodeObject nodeObject)
	{
		if (nodeObject instanceof ImmutableNode)
		{
			ImmutableNode immutableNode = ((ImmutableNode) nodeObject);
			return "read(" + immutableNode.getName() + ");\n";
		} else if (nodeObject instanceof MutableNode)
		{
			MutableNode mutableNode = ((MutableNode) nodeObject);
			return mutableNode.fetchConfigStr() + "read(" + mutableNode.getName() + ");\n";
		} else if (nodeObject instanceof IfNode)
		{
			IfNode ifNode = ((IfNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			sb.append("if(");
			sb.append(ifNode.getCondition().fetchExprStr());
			sb.append(")\n{\n");
			sb.append(ifNode.getTrueDataDef().fetchRuntimeReadStr());
			sb.append("}\n");
			if (ifNode.isExistElse())
			{
				sb.append("else\n{\n");
				sb.append(ifNode.getFalseDataDef().fetchRuntimeReadStr());
				sb.append("}\n");
			}
			return sb.toString();
		} else if (nodeObject instanceof ListNode)
		{
			ListNode listNode = ((ListNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			sb.append("try {while (true) {if(!inputWrapper.valid()) throw new EOFException(\"\");integrity = false;");
			for (NodeObject object : listNode)
			{
				sb.append(object.fetchRuntimeReadStr());
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public String fetchRuntimeWriteCode(NodeObject nodeObject)
	{
		if (nodeObject instanceof ImmutableNode)
		{
			ImmutableNode immutableNode = ((ImmutableNode) nodeObject);
			return "write(" + immutableNode.getName() + ");\n";
		} else if (nodeObject instanceof MutableNode)
		{
			MutableNode mutableNode = ((MutableNode) nodeObject);
			return "write(" + mutableNode.getName() + ");\n";
		} else if (nodeObject instanceof IfNode)
		{
			IfNode ifNode = ((IfNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			sb.append("if(");
			sb.append(ifNode.getCondition().fetchExprStr());
			sb.append(")\n{\n");
			sb.append(ifNode.getTrueDataDef().fetchRuntimeWriteStr());
			sb.append("}\n");
			if (ifNode.isExistElse())
			{
				sb.append("else\n{\n");
				sb.append(ifNode.getFalseDataDef().fetchRuntimeWriteStr());
				sb.append("}\n");
			}
			return sb.toString();
		} else if (nodeObject instanceof ListNode)
		{
			ListNode listNode = ((ListNode) nodeObject);
			StringBuilder sb = new StringBuilder();
			for (NodeObject object : listNode)
			{
				String data = object.fetchRuntimeWriteStr() + "write(RootClass._outsepa_);\n";
				sb.append(data);
			}
			sb.delete(sb.length() - "write(RootClass._outsepa_);\n".length(), sb.length());
			sb.append("write(RootClass._nextsepa_);\n}} catch (EOFException eof) {if(!integrity) throw new RuntimeException(\"Data has wrong structure.\");System.out.print(\"Finish read.\\n\");}");
			return sb.toString();
		}
		return "";
	}

	@Override
	public void generateFunctionCode(FunctionNode functionNode, StructNode structNode)
	{
		StringBuilder sb = new StringBuilder();
		FunctionToken functionToken = new FunctionToken(FunctionToken.PUBLIC, functionNode.getType(), functionNode.getName(), functionNode.getSignature(), null);
		if (functionNode.getFunctionToken().whetherOverride(FunctionToken.TOINT))
		{
			sb.append("{return ");
			for (NodeObject nodeObject : structNode.getDataDef())
				sb.append(((DefinitionNode) nodeObject).fetchFormatStr("%n.toInt()+"));
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";}");
		} else if (functionNode.getFunctionToken().whetherOverride(FunctionToken.GETOUTSTR))
		{
			sb.append("{return ");
			for (NodeObject nodeObject : structNode.getDataDef())
				sb.append(((DefinitionNode) nodeObject).fetchFormatStr("%n.getOutStr()+"));
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";}");
		} else if (functionNode.getFunctionToken().whetherOverride(FunctionToken.READ))
		{
			sb.append("{");
			for (NodeObject nodeObject : structNode.getDataDef())
			{
				if (nodeObject instanceof MutableNode)
				{
					MutableNode mutableNode = (MutableNode) nodeObject;
					sb.append(mutableNode.fetchConfigStr());
					sb.append(mutableNode.fetchFormatStr("%n.read();"));
				} else
					sb.append(((DefinitionNode) nodeObject).fetchFormatStr("%n.read();"));
			}
			sb.append("}");
		} else if (functionNode.getFunctionToken().whetherOverride(FunctionToken.WRITE))
		{
			sb.append("{");
			for (NodeObject nodeObject : structNode.getDataDef())
				sb.append(((DefinitionNode) nodeObject).fetchFormatStr("%n.write();outputWrapper.write(_insepa_);"));
			sb.delete(sb.length() - "outputWrapper.write(_insepa_);".length(), sb.length());
			sb.append("}");
		} else if (functionNode.getFunctionToken().whetherOverride(FunctionToken.FORMATSTR))
		{
			sb.append("{return ");
			for (NodeObject nodeObject : structNode.getDataDef())
				sb.append(((DefinitionNode) nodeObject).fetchFormatStr("%n.formatStr(f,sepa)+sepa+"));
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";}");
		} else
		{
			sb.append(functionNode.getBody());
		}
		functionToken.setBody(sb.toString());
		functionNode.setFunctionToken(functionToken);
	}

	private String fetchConfigInfo(Env env)
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : env.getConfigMap().entrySet())
		{
			String data = Env.staticConfigList.contains(entry.getKey()) ? "public static String " + entry.getKey() + "=" : "public String " + entry.getKey() + "=";
			if (entry.getKey().equalsIgnoreCase("_input_") || entry.getKey().equalsIgnoreCase("_output_"))
				data = data + MyLib.reshape(entry.getValue());
			else
				data = data + "\"" + entry.getValue() + "\"";
			sb.append(data);
			sb.append(";\n");
		}
		return sb.toString();
	}

	private String fetchFunctionInfo(Env env)
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, FunctionNode> entry : env.getFunctionMap().entrySet())
		{
			FunctionNode f = entry.getValue();
			String data = f.fetchModifyStr() + f.getType() + " " + f.getName() + " " + f.getSignature() + " " + f.getBody() + "\n";
			sb.append(data);
		}
		return sb.toString();
	}
}
