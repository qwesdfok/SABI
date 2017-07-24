package com.qwesdfok.Tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qwesd on 2016/2/5.
 */
public class FunctionToken extends Token
{
	public static int STATIC = 0x1;
	public static int PUBLIC = 0x2;
	public static int PRIVATE = 0x4;
	public static int PROTECTED = 0x8;
	public static int OVERRIDE = 0x10;
	public static int ABSTRACT = 0x20;
	public static FunctionToken TOINT = new FunctionToken(PUBLIC | ABSTRACT, "int", "toInt", "()", ";");
	public static FunctionToken GETOUTSTR = new FunctionToken(PUBLIC | ABSTRACT, "String", "getOutStr", "()", ";");
	public static FunctionToken FORMATSTR = new FunctionToken(PUBLIC | ABSTRACT, "String", "formatStr", "(String f,String sepa)", ";");
	public static FunctionToken READ = new FunctionToken(PUBLIC | ABSTRACT, "void", "read", "()throws IOException", ";");
	public static FunctionToken WRITE = new FunctionToken(PUBLIC | ABSTRACT, "void", "write", "()throws IOException", ";");
	public static FunctionToken ISEMPTY = new FunctionToken(PUBLIC, "boolean", "isEmpty", "()", "{return false;}");
	public static FunctionToken GETPRINTSTR = new FunctionToken(PUBLIC, "String", "getPrintStr", "()", "{return _printway_.replaceAll(\"%[vV]\", getOutStr()).replaceAll(\"%[nN]\", getName()).replaceAll(\"%[tT]\", getTypeName()).replaceAll(\"(%X)\",formatStr(\"%X\",\"\")).replaceAll(\"(%x)\",formatStr(\"%x\",\"\")).replaceAll(\"(%H)\",formatStr(\"%H\",\"\")).replaceAll(\"(%h)\",formatStr(\"%h\",\"\")).replaceAll(\"%%\",\"%\");}");
	public static FunctionToken PREFUNCTION = new FunctionToken(PUBLIC, "void", "preFunction", "()", "{}");
	public static FunctionToken INNERFUNCTION = new FunctionToken(PUBLIC, "void", "innerFunction", "()", "{}");
	public static FunctionToken ENDFUNCTION = new FunctionToken(PUBLIC, "void", "endFunction", "()", "{}");
	public static FunctionToken INVALIDFUNCTION = new FunctionToken(PUBLIC, "void", "invalidFunction", "()", "{}");
	public static ArrayList<FunctionToken> abstractFunctionList = new ArrayList<>(Arrays.asList(TOINT, GETOUTSTR, FORMATSTR, READ, WRITE));
	public static ArrayList<FunctionToken> optionFunctionList = new ArrayList<>(Arrays.asList(PREFUNCTION, INNERFUNCTION, ENDFUNCTION, INVALIDFUNCTION, GETPRINTSTR, ISEMPTY));

	public static HashMap<Integer, String> modifyMap = new HashMap<Integer, String>()
	{{
		put(0x1, "static");
		put(0x2, "public");
		put(0x4, "private");
		put(0x8, "protected");
		put(0x10, "@Override");
		put(0x20, "abstract");
	}};
	public static HashMap<String, Integer> modifyReverseMap = new HashMap<String, Integer>()
	{{
		put("static", 0x1);
		put("public", 0x2);
		put("private", 0x4);
		put("protected", 0x8);
		put("override", 0x10);
		put("abstract", 0x20);
	}};
	private int modify;
	private String type;
	private String name;
	private String signature;
	private String body;

	public FunctionToken(int modify, String type, String name, String signature, String body)
	{
		super(Tag.FUNCTION);
		this.type = type;
		this.modify = modify;
		this.name = name;
		this.signature = signature;
		this.body = body;
	}

	public String fetchModifyString()
	{
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, String> entry : modifyMap.entrySet())
		{
			if ((modify & entry.getKey()) != 0)
			{
				sb.append(entry.getValue());
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public boolean whetherOverride(FunctionToken parentFunction)
	{
		return name.equals(parentFunction.getName()) && body.equals(parentFunction.getBody());
	}

	@Override
	public FunctionToken clone()
	{
		return (FunctionToken) super.clone();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FunctionToken that = (FunctionToken) o;

		if (modify != that.modify) return false;
		if (type != null ? !type.equals(that.type) : that.type != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		return signature != null ? signature.equals(that.signature) : that.signature == null;

	}

	@Override
	public int hashCode()
	{
		int result = modify;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (signature != null ? signature.hashCode() : 0);
		return result;
	}

	public int getModify()
	{
		return modify;
	}

	public void setModify(int modify)
	{
		this.modify = modify;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
}
