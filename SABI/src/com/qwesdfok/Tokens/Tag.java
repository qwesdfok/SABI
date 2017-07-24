package com.qwesdfok.Tokens;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Tag
{
	private static int index = 256;

	private static int getIndex()
	{
		return index++;
	}

	public static int EOF = getIndex();
	public static int ID = getIndex();
	public static int TYPE = getIndex();
	public static int NUMBER = getIndex();
	public static int CSTR = getIndex();
	public static int EQU = getIndex();
	public static int LSS = '<';
	public static int LAG = '>';
	public static int LAE = getIndex();
	public static int LSE = getIndex();
	public static int INE = getIndex();
	public static int OR = getIndex();
	public static int AND = getIndex();
	public static int NOT = '!';
	public static int RSHIFT = getIndex();
	public static int LSHIFT = getIndex();
	public static int ASSIGN = '=';
	public static int IMPORT = getIndex();
	public static int CONFIG = getIndex();
	public static int DEF = getIndex();
	public static int FUNCTION = getIndex();
	public static int STRUCT = getIndex();
	public static int MUTABLE = getIndex();
	public static int IF = getIndex();
	public static int ELSE = getIndex();
	public static int SELF = getIndex();
	public static int BITOR = '|';
	public static int BITAND = '&';
	public static int BITNOR = '^';
}
