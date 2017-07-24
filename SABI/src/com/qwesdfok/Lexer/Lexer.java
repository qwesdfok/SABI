package com.qwesdfok.Lexer;

import com.qwesdfok.Common.InputWrapperInterface;
import com.qwesdfok.Common.OutputWrapperInterface;
import com.qwesdfok.Parser.Env;
import com.qwesdfok.Tokens.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Lexer
{
	private InputWrapperInterface inputWrapper;
	private OutputWrapperInterface outputWrapper;
	private HashMap<String, Token> keywordMap = new HashMap<>();
	private HashMap<String, Type> buildInTypeMap = new HashMap<>();
	private HashMap<String, Type> customTypeMap = new HashMap<>();
	private int current = ' ';
	private int next = ' ';
	private int lineNumber = 1;
	private int lexNumber = 0;
	private boolean isCustomType = false;
	private boolean eof = false;

	public Lexer(InputWrapperInterface inputWrapper, OutputWrapperInterface outputWrapper)
	{
		this.inputWrapper = inputWrapper;
		this.outputWrapper = outputWrapper;
		buildInTypeMap.put("int", Type.PINTEGER);
		buildInTypeMap.put("double", Type.PDOUBLE);
		buildInTypeMap.put("float", Type.PFLOAT);
		buildInTypeMap.put("long", Type.PLONG);
		buildInTypeMap.put("boolean", Type.PBOOLEAN);
		buildInTypeMap.put("string", Type.PSTRING);
		buildInTypeMap.put("String", Type.PSTRING);
		keywordMap.put("import", Word.IMPORT);
		keywordMap.put("def", Word.DEF);
		keywordMap.put("mutable", Word.MUTABLE);
		keywordMap.put("struct", Word.STRUCT);
		keywordMap.put("if", Word.IF);
		keywordMap.put("else", Word.ELSE);
		keywordMap.put("self", Word.SELF);
		keywordMap.put("_input_", new Word("_input_", Tag.CONFIG));
		keywordMap.put("_output_", new Word("_output_", Tag.CONFIG));
		keywordMap.put("_insepa_", new Word("_insepa_", Tag.CONFIG));
		keywordMap.put("_outsepa_", new Word("_outsepa_", Tag.CONFIG));
		keywordMap.put("_nextsepa_", new Word("_nextsepa_", Tag.CONFIG));
		keywordMap.put("_bigend_", new Word("_bigend_", Tag.CONFIG));
		keywordMap.put("_skip_", new Word("_skip_", Tag.CONFIG));
		keywordMap.put("_iterway_", new Word("_iterway_", Tag.CONFIG));
		keywordMap.put("_encoding_", new Word("_encoding_", Tag.CONFIG));
		keywordMap.put("_printway_", new Word("_printway_", Tag.CONFIG));
	}

	private int nextCharacter() throws IOException
	{
		int data = inputWrapper.read();
		if (data == -1)
		{
			if (eof)
				throw new RuntimeException("End of file.");
			else
				eof = true;
		}
		return data;
	}

	private void readToCurrent() throws IOException
	{
		if (next == ' ')
			current = nextCharacter();
		else
		{
			current = next;
			next = ' ';
		}
	}

	private boolean whetherSpace(int c)
	{
		boolean b1 = c == ' ' || c == '\t' || c == '\r' || c == '\n';
		if (c == '\n')
		{
			lineNumber += 1;
			lexNumber = 1;
		}
		return b1;
	}

	private boolean whetherDigit(int v)
	{
		return '0' <= v && v <= '9';
	}

	private boolean whetherLetter(int v)
	{
		return 'A' <= v && v <= 'Z' || 'a' <= v && v <= 'z';
	}

	private int fetchInteger() throws IOException
	{
		while (whetherSpace(current))
			readToCurrent();
		if (!whetherDigit(current))
			throw new RuntimeException("Digit expected.");
		if (whetherSpace(next))
			next = nextCharacter();
		if (current == '0' && (next == 'x' || next == 'X'))
		{
			readToCurrent();
			readToCurrent();
			StringBuilder nbuf = new StringBuilder();
			ArrayList<Character> hex = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F'));
			while (hex.contains(((char) current)))
			{
				nbuf.append((char) current);
				readToCurrent();
			}
			return Integer.parseInt(nbuf.toString(), 16);
		} else
		{
			int num = 0;
			while (whetherDigit(current))
			{
				num = num * 10 + current - '0';
				readToCurrent();
			}
			return num;
		}
	}

	private String fetchWordString() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		while (whetherSpace(current))
			readToCurrent();
		if (whetherLetter(current) || current == '_')
		{
			while (whetherLetter(current) || current == '_' || whetherDigit(current))
			{
				sb.append((char) current);
				readToCurrent();
			}
		}
		else
			throw new RuntimeException("Mismatch Word");
		return sb.toString();
	}

	public void storeTypeEnv(String typeName, Env env)
	{
		customTypeMap.get(typeName).setEnv(env);
	}

	public Token scan() throws IOException
	{
		lexNumber += 1;
		if (!whetherSpace(next))
		{
			current = next;
			next = ' ';
		}
		while (whetherSpace(current))
			readToCurrent();
		if (current == -1)
			return new Token(Tag.EOF);
		//num
		if (whetherDigit(current))
		{
			return new Num(fetchInteger());
		}
		//word
		if (whetherLetter(current) || current == '_')
		{
			String str = fetchWordString();
			Token res = keywordMap.get(str);
			if (res == null) res = buildInTypeMap.get(str);
			//type(num)
			if ((buildInTypeMap.containsKey(str) || customTypeMap.containsKey(str)) && current == '(')
			{
				res = res.clone();
				readToCurrent();
				int num = fetchInteger();
				if (current == ')')
					readToCurrent();
				else
					throw new RuntimeException("Mismatch ')'");
				((Type) res).setLength(num);
			}
			//function
			if (str.equals("def"))
			{
				int deep = 0;
				StringBuilder buf = new StringBuilder();
				int modify = 0;
				str = fetchWordString();
				FunctionToken template = null;
				String name, type, signature, body;
				for (FunctionToken functionToken : FunctionToken.abstractFunctionList)
				{
					if (functionToken.getName().equals(str))
						template = functionToken;
				}
				for (FunctionToken functionToken : FunctionToken.optionFunctionList)
				{
					if (functionToken.getName().equals(str))
						template = functionToken;
				}
				if (template == null)
				{
					while (FunctionToken.modifyReverseMap.containsKey(str))
					{
						modify |= FunctionToken.modifyReverseMap.get(str);
						str = fetchWordString();
					}
					type = str;
					name = fetchWordString();
					while (whetherSpace(current))
						readToCurrent();
					do
					{
						buf.append((char) current);
						if (current == '(') deep += 1;
						else if (current == ')') deep -= 1;
						readToCurrent();
					} while (deep != 0);
					signature = buf.toString();
					buf.delete(0, buf.length());
				} else
				{
					type = template.getType();
					name = template.getName();
					signature = template.getSignature();
					modify = FunctionToken.PUBLIC;
				}
				if (whetherSpace(current))
					readToCurrent();
				do
				{
					buf.append((char) current);
					if (current == '{') deep += 1;
					else if (current == '}') deep -= 1;
					readToCurrent();
				} while (deep != 0);
				body = buf.toString();
				return new FunctionToken(modify, type, name, signature, body);
			}
			//struct new type
			if (str.equals(Word.STRUCT.getStr()))
			{
				isCustomType = true;
				return res;
			}
			//other keyword
			if (res != null) return res;
			//custom type
			res = customTypeMap.get(str);
			if (res != null) return res;
			//custom type
			if (isCustomType)
			{
				customTypeMap.put(str, new Type(str, 0));
				isCustomType = false;
			}
			//id
			res = new Word(str, Tag.ID);
			return res;
		}
		//ConstantString
		if (current == '"')
		{
			StringBuilder buf = new StringBuilder();
			readToCurrent();
			while (current != '"')
			{
				buf.append((char) current);
				readToCurrent();
			}
			current = ' ';
			return new Word(buf.toString(), Tag.CSTR);
		}
		next = nextCharacter();
		//region BinaryOperation
		switch (current)
		{
			case '>':
			{
				next = nextCharacter();
				if (next == '=')
				{
					readToCurrent();
					return Word.LAE;
				} else if (next == '>')
				{
					readToCurrent();
					return Word.RSHIFT;
				} else
				{
					return Word.LAG;
				}
			}
			case '=':
			{
				if (next == '=')
				{
					readToCurrent();
					return Word.EQU;
				} else
				{
					return Word.ASSIGN;
				}
			}
			case '<':
			{
				if (next == '=')
				{
					readToCurrent();
					return Word.LSE;
				} else if (next == '<')
				{
					readToCurrent();
					return Word.LSHIFT;
				} else
				{
					return Word.LSS;
				}
			}
			case '!':
			{
				if (next == '=')
				{
					readToCurrent();
					return Word.INE;
				} else
				{
					return Word.NOT;
				}
			}
			case '&':
			{
				if (next == '&')
				{
					readToCurrent();
					return Word.AND;
				} else return Word.BITAND;
			}
			case '|':
			{
				if (next == '|')
				{
					readToCurrent();
					return Word.OR;
				} else return Word.BITOR;
			}
			case '^':
			{
				return Word.BITNOR;
			}
			default:
		}
		//endregion
		Token res = new Token(current);
		current = ' ';
		return res;
	}

	public InputWrapperInterface getInputWrapper()
	{
		return inputWrapper;
	}

	public OutputWrapperInterface getOutputWrapper()
	{
		return outputWrapper;
	}

	public HashMap<String, Type> getCustomTypeMap()
	{
		return customTypeMap;
	}

	public HashMap<String, Type> getBuildInTypeMap()
	{
		return buildInTypeMap;
	}

	public HashMap<String, Token> getKeywordMap()
	{
		return keywordMap;
	}

	public void setCustomTypeMap(HashMap<String, Type> customTypeMap)
	{
		this.customTypeMap = customTypeMap;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public int getLexNumber()
	{
		return lexNumber;
	}
}
