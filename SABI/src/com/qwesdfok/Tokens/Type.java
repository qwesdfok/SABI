package com.qwesdfok.Tokens;

import com.qwesdfok.Parser.Env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Type extends Token implements Cloneable
{
	public static Type PINTEGER = new Type("int", 4, new Env(null));
	public static Type PLONG = new Type("long", 8, new Env(null));
	public static Type PBOOLEAN = new Type("boolean", 1, new Env(null));
	public static Type PFLOAT = new Type("float", 4, new Env(null));
	public static Type PDOUBLE = new Type("double", 8, new Env(null));
	public static Type PSTRING = new Type("String", 0, new Env(null));
	public static Type INTEGER = new Type("SelfInteger", 4, new Env(null));
	public static Type LONG = new Type("SelfLong", 4, new Env(null));
	public static Type FLOAT = new Type("SelfFloat", 4, new Env(null));
	public static Type DOUBLE = new Type("SelfDouble", 4, new Env(null));
	public static Type BOOLEAN = new Type("SelfBoolean", 4, new Env(null));
	public static Type STRING = new Type("SelfString", 0, new Env(null));
	public static HashMap<Type, Type> primaryConvertTable = new HashMap<Type, Type>()
	{{
		put(PBOOLEAN, BOOLEAN);
		put(PINTEGER, INTEGER);
		put(PDOUBLE, DOUBLE);
		put(PLONG, LONG);
		put(PFLOAT, FLOAT);
		put(PSTRING, STRING);
	}};

	private String typeName;
	private Env env;
	private int length;

	public Type(String typeName, int length)
	{
		super(Tag.TYPE);
		this.typeName = typeName;
		this.length = length;
	}

	public Type(String typeName, int length, Env env)
	{
		super(Tag.TYPE);
		this.typeName = typeName;
		this.length = length;
		this.env = env;
	}

	public static Type fetchWrappedType(Type type)
	{
		for (Map.Entry<Type, Type> entry : primaryConvertTable.entrySet())
		{
			if (entry.getKey().typeName.equals(type.typeName))
				return new Type(entry.getValue().typeName, type.length, type.env);
		}
		return type;
	}

	public static Type fetchPrimaryType(Type type)
	{
		for (Map.Entry<Type, Type> entry : primaryConvertTable.entrySet())
		{
			if (entry.getValue().typeName.equals(type.typeName))
				return new Type(entry.getKey().typeName, type.length, type.env);
		}
		return type;
	}

	@Override
	public String fetchTokenDescription()
	{
		return typeName;
	}

	@Override
	public Type clone()
	{
		Object obj = super.clone();
		return (Type) obj;
	}

	@Override
	public String toString()
	{
		return "Type:" + typeName + "(" + length + ')';
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Type type = (Type) o;

		if (length != type.length) return false;
		return typeName != null ? typeName.equals(type.typeName) : type.typeName == null;

	}

	@Override
	public int hashCode()
	{
		int result = typeName != null ? typeName.hashCode() : 0;
		result = 31 * result + length;
		return result;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public Env getEnv()
	{
		return env;
	}

	public void setEnv(Env env)
	{
		this.env = env;
	}
}
