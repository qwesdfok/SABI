package com.qwesdfok.Tokens;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Word extends Token implements Cloneable
{
	public static Word EQU = new Word("==", Tag.EQU);
	public static Word LAG = new Word(">", Tag.LAG);
	public static Word LSS = new Word("<", Tag.LSS);
	public static Word LAE = new Word(">=", Tag.LAE);
	public static Word LSE = new Word("<=", Tag.LSE);
	public static Word INE = new Word("!=", Tag.INE);
	public static Word OR = new Word("||", Tag.OR);
	public static Word AND = new Word("&&", Tag.AND);
	public static Word NOT = new Word("!", Tag.NOT);
	public static Word RSHIFT = new Word(">>", Tag.RSHIFT);
	public static Word LSHIFT = new Word("<<", Tag.LSHIFT);
	public static Word ASSIGN = new Word("=", Tag.ASSIGN);
	public static Word IMPORT = new Word("import", Tag.IMPORT);
	public static Word DEF = new Word("def", Tag.DEF);
	public static Word STRUCT = new Word("struct", Tag.STRUCT);
	public static Word MUTABLE = new Word("mutable", Tag.MUTABLE);
	public static Word IF = new Word("if", Tag.IF);
	public static Word ELSE = new Word("else", Tag.ELSE);
	public static Word SELF = new Word("self", Tag.SELF);
	public static Word BITOR = new Word("|", Tag.BITOR);
	public static Word BITAND = new Word("&", Tag.BITAND);
	public static Word BITNOR = new Word("^", Tag.BITNOR);

	private String str;

	public Word(String str,int tag)
	{
		super(tag);
		this.str = str;
	}

	@Override
	public String fetchTokenDescription()
	{
		return str;
	}

	@Override
	public Word clone()
	{
		return (Word)super.clone();
	}

	@Override
	public String toString()
	{
		return "Word:'" + str + '\'';
	}

	public String getStr()
	{
		return str;
	}

	public void setStr(String str)
	{
		this.str = str;
	}
}
