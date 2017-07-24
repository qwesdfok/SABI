package com.qwesdfok.Parser;

import com.qwesdfok.Common.*;
import com.qwesdfok.Lexer.Lexer;
import com.qwesdfok.Tokens.*;
import com.qwesdfok.Translate.TranslateInterface;
import com.qwesdfok.TreeNode.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

public class Parser
{
	private static boolean startedPrint = false;
	private Lexer lexer;
	private Token currentToken;
	private Env env = new Env(null);
	private TranslateInterface translate;
	private InputWrapperInterface inputWrapper;
	private OutputWrapperInterface outputWrapper;
	private boolean mainDef;
	private boolean print = true;
	private boolean valid = true;
	private String lastError;

	public Parser(InputWrapperInterface inputWrapper)
	{
		constructor(inputWrapper, null, null, true);
	}

	public Parser(InputWrapperInterface inputWrapper, OutputWrapperInterface outputWrapper, TranslateInterface translate)
	{
		constructor(inputWrapper, outputWrapper, translate, true);
	}

	public Parser(InputWrapperInterface inputWrapper, OutputWrapperInterface outputWrapper, TranslateInterface translate, boolean mainDef)
	{
		constructor(inputWrapper, outputWrapper, translate, mainDef);
	}

	private void constructor(InputWrapperInterface inputWrapper, OutputWrapperInterface outputWrapper, TranslateInterface translate, boolean mainDef)
	{
		this.inputWrapper = inputWrapper;
		this.outputWrapper = outputWrapper;
		this.lexer = new Lexer(inputWrapper, outputWrapper);
		this.mainDef = mainDef;
		this.translate = translate;
		if (translate == null || outputWrapper == null)
			print = false;
	}

	public void startParse() throws IOException
	{
/*
      Start -> [ Imports ] [ GlobalValues ] [ GlobalFunc ] [ StructDecls ] DataDef
      Imports -> 'Import' FileName | Imports | e
      GlobalFuncs -> FuncDef  GlobalFuncs | e
      StructDecls -> StructDecl  StructDecls
      StructDecl -> 'struct' '{' DataDef '}' | e
      DataDef -> Parts
      Parts -> Part [','] Parts | Part
      Part -> Mutable | Immutable | If | Control | SelfValDef | FuncDef |e
      Immutable -> Type id
      Type -> type ['(' length ')']
      Mutable -> 'volatile' Type id '(' Addition ')'
      If -> 'if' '(' Condition ')' '{' DataDef '}' ['else' '{' DataDef '}']
      Control -> CtrlVal '=' Expr
      CtrlVal -> _insepa_ | _output_ | _input_ | … | e
      Expr -> LogicalOr
      LogicalOr -> LogicalOr ‘||’ logicalAnd | e
      LogicalAnd -> LogicalAnd '&&' BitOp | e
      BirOp -> BitOp ‘&’ Equ | BitOp ‘|’ Equ | BitOp ‘^’ Equ | e
      Equ -> Equ ‘==’ Inequ | Equ ‘!=’ Inequ | e
      Inequ -> Inequ ‘<’ Shift | Inequ ‘>’ Shift |
             Inequ ‘<=’ Shift | Inequ ‘>=’ Shift | e
      Shift -> Shift ‘<<’ Adsu | shift ‘>>’ Adsu | e
      Adsu -> Adsu ‘+’ Mudi | Adsu ‘-’ Mudi | e
      Mudi -> Mudi ‘*’ Factor | Mudi ‘/’ Factor | Mudi ‘%’ Factor | e
      Factor -> ‘(’Expr‘)’ | id | id.id
*/
		try
		{
			NodeObject.setTranslate(translate);
			env.storeConfigInfo("_bigend_", "true");
			env.storeConfigInfo("_insepa_", ",");
			env.storeConfigInfo("_outsepa_", ";");
			env.storeConfigInfo("_nextsepa_", "\\r\\n");
			env.storeConfigInfo("_skip_", "\\r\\n");
			env.storeConfigInfo("_iterway_", "%n_%i");
			env.storeConfigInfo("_printway_", "%n:%t=%v");
			for (FunctionToken functionToken : FunctionToken.optionFunctionList)
				env.storeFunctionInfo(new FunctionNode(functionToken));
			move();
			if (!(currentToken.getTag() == Tag.FUNCTION || currentToken.getTag() == Tag.CONFIG
					|| currentToken.getTag() == Tag.TYPE || currentToken.getTag() == Tag.IMPORT || currentToken.getTag() == Tag.STRUCT))
			{
				valid = false;
				lastError = "Mismatch: Needed to start with function, configuration, import, struct or type.";
				return;
			}
			ListNode importData = readImportDataList();
			for (NodeObject n : importData)
			{
				File file = new File(inputWrapper.getFileName());
				InputWrapper nextInput = new InputWrapper(MyLib.combinePath(file.getParent(), ((ConstantStringNode) n).getStr()));
				Parser childParse = new Parser(nextInput, outputWrapper, translate, false);
				childParse.startParse();
				readTypeInfoFromParser(childParse);
			}
			readConfigDataList();
			readFunctionDataList();
			if (print && importData.size() == 0 && !startedPrint)
			{
				startedPrint = true;
				translate.writeLibData(outputWrapper, env);
			}
			ListNode structData = readStructDataList();
			if (print)
			{
				for (NodeObject nodeObject : structData)
					translate.writeDeclareCode(outputWrapper, nodeObject);
			}
			if (mainDef)
			{
				ListNode data = readDefDataList();
				if (print)
					translate.writeRuntimeCode(outputWrapper, data);
				inputWrapper.close();
				if (outputWrapper != null)
					outputWrapper.close();
				startedPrint = false;
			}
		} catch (RuntimeException e)
		{
			System.out.print(e.getMessage());
			valid = false;
		}
	}

	public void config(boolean mainDef, boolean print, String encoding) throws IOException
	{
		this.mainDef = mainDef;
		this.print = print;
		inputWrapper.setEncoding(encoding);
	}

	private void move()
	{
		try
		{
			currentToken = lexer.scan();
		} catch (IOException e)
		{
			errorOccur("IOException");
		}
	}

	private void match(int tag)
	{
		if (currentToken.getTag() == tag)
			move();
		else
			errorOccur("Mismatch\n");
	}

	private void write(String data) throws IOException
	{
		if (!print || data == null)
			return;
		outputWrapper.write(data);
	}

	private void errorOccur(String data)
	{
		try
		{
			valid = false;
			lastError = data + "\nLine: " + lexer.getLineNumber() + ", Token: " + lexer.getLexNumber() + "\n";
			write(lastError);
		} catch (Exception e)
		{
			System.out.print("ErrorOccurs.\n");
		}
		throw new RuntimeException(data);
	}

	private ListNode readImportDataList()
	{
		ListNode res = new ListNode();
		while (currentToken.getTag() == Tag.IMPORT)
		{
			match(Tag.IMPORT);
			res.add(new ConstantStringNode((((Word) currentToken).getStr())));
			move();
		}
		return res;
	}

	private ListNode readConfigDataList()
	{
		ListNode res = new ListNode();
		while (currentToken.getTag() == Tag.CONFIG)
		{
			Word name = ((Word) currentToken);
			match(Tag.CONFIG);
			match('=');
			res.add(new ConstantStringNode(((Word) currentToken).getStr()));
			env.storeConfigInfo(name.getStr(), ((Word) currentToken).getStr());
			move();
		}
		return res;
	}

	private ListNode readFunctionDataList()
	{
		for (FunctionToken functionToken : FunctionToken.abstractFunctionList)
			env.storeFunctionInfo(new FunctionNode(functionToken));
		ListNode res = new ListNode();
		while (currentToken.getTag() == Tag.FUNCTION)
		{
			FunctionNode functionNode = new FunctionNode(((FunctionToken) currentToken));
			env.storeFunctionInfo(functionNode);
			res.add(functionNode);
			move();
		}
		return res;
	}

	private ListNode readStructDataList()
	{
		ListNode structList = new ListNode();
		while (currentToken.getTag() == Tag.STRUCT)
		{
			Env save = env;
			env = new Env(env);
			move();
			Word name = ((Word) currentToken);
			move();
			match('{');
			readConfigDataList();
			readFunctionDataList();
			ListNode dataDefList = readDefDataList();
			StructNode structNode = new StructNode(name.getStr(), env, dataDefList);
			structNode.setEnv(env);
			lexer.storeTypeEnv(structNode.getName(), env);
			if (print)
			{
				for (Map.Entry<String, FunctionNode> entry : env.getFunctionMap().entrySet())
					translate.generateFunctionCode(entry.getValue(), structNode);
			}
			match('}');
			env = save;
			structList.add(structNode);
		}
		return structList;
	}

	private ListNode readDefDataList()
	{
		ListNode res = new ListNode();
		while (currentToken.getTag() == Tag.MUTABLE || currentToken.getTag() == Tag.TYPE
				|| currentToken.getTag() == Tag.IF || currentToken.getTag() == Tag.SELF)
		{
			if (currentToken.getTag() == Tag.MUTABLE)
			{
				match(Tag.MUTABLE);
				Type type = Type.fetchWrappedType((Type) currentToken);
				move();
				Word name = ((Word) currentToken);
				move();
				match('(');
				ExprNode expr = fetchExpr();
				match(')');
				if (currentToken.getTag() == ',')
					move();
				String format = null;
				if (currentToken.getTag() == '[')
				{
					move();
					format = ((Word) currentToken).getStr();
					match(Tag.CSTR);
					match(']');
				}
				MutableNode mutableNode = new MutableNode(type, name.getStr(), expr, format);
				IdNode idNode = new IdNode(type, name.getStr());
				env.storeIdInfo(idNode);
				res.add(mutableNode);
			} else if (currentToken.getTag() == Tag.TYPE)
			{
				Type type = Type.fetchWrappedType((Type) currentToken);
				move();
				Word name = ((Word) currentToken);
				move();
				if (currentToken.getTag() == ',')
					move();
				ImmutableNode immutableNode = new ImmutableNode(type, name.getStr());
				IdNode idNode = new IdNode(type, name.getStr());
				env.storeIdInfo(idNode);
				res.add(immutableNode);
			} else if (currentToken.getTag() == Tag.IF)
			{
				move();
				match('(');
				ExprNode condition = fetchExpr();
				match(')');
				match('{');
				ListNode trueData = readDefDataList();
				match('}');
				ListNode falseData = null;
				if (currentToken.getTag() == Tag.ELSE)
				{
					move();
					match('{');
					falseData = readDefDataList();
					match('}');
				}
				IfNode ifNode = new IfNode(condition, trueData, falseData != null, falseData);
				res.add(ifNode);
			} else if (currentToken.getTag() == Tag.SELF)
			{
				move();
				Type type = ((Type) currentToken);
				move();
				Word name = ((Word) currentToken);
				move();
				SelfNode selfNode = new SelfNode(type, name.getStr());
				env.storeSelfInfo(selfNode);
			}
		}
		return res;
	}

	private void readTypeInfoFromParser(Parser otherParser)
	{
		lexer.getCustomTypeMap().putAll(otherParser.lexer.getCustomTypeMap());
	}

	private ExprNode fetchExpr()
	{
		return logicalOr();
	}

	private ExprNode logicalOr()
	{
		ExprNode e = logicalAnd();
		return binaryOperationRecurse(e, new Token[]{Word.OR}, this::logicalAnd);
	}

	private ExprNode logicalAnd()
	{
		ExprNode e = bitOR();
		return binaryOperationRecurse(e, new Token[]{Word.AND}, this::bitOR);
	}

	private ExprNode bitOR()
	{
		ExprNode e = bitNor();
		return binaryOperationRecurse(e, new Token[]{Word.BITOR}, this::bitNor);
	}

	private ExprNode bitNor()
	{
		ExprNode e = bitAnd();
		return binaryOperationRecurse(e, new Token[]{Word.BITNOR}, this::bitAnd);
	}

	private ExprNode bitAnd()
	{
		ExprNode e = equality();
		return binaryOperationRecurse(e, new Token[]{Word.BITAND}, this::equality);
	}

	private ExprNode equality()
	{
		ExprNode e = inequality();
		return binaryOperationRecurse(e, new Token[]{Word.EQU, Word.INE}, this::inequality);
	}

	private ExprNode inequality()
	{
		ExprNode e = shift();
		return binaryOperationRecurse(e, new Token[]{Word.LAG, Word.LSS, Word.LAE, Word.LSE}, this::shift);
	}

	private ExprNode shift()
	{
		ExprNode e = addMinus();
		return binaryOperationRecurse(e, new Token[]{Word.LSHIFT, Word.RSHIFT}, this::addMinus);
	}

	private ExprNode addMinus()
	{
		ExprNode e = produceDivide();
		return binaryOperationRecurse(e, new Token[]{new Token('+'), new Token('-')}, this::produceDivide);
	}

	private ExprNode produceDivide()
	{
		ExprNode e = pointOperation();
		return binaryOperationRecurse(e, new Token[]{new Token('*'), new Token('/'), new Token('%')}, this::pointOperation);
	}

	private ExprNode pointOperation()
	{
		ExprNode e = factory();
		while (currentToken.getTag() == '.')
		{
			move();
			Env save = env;
			env = e.getType().getEnv();
			IdNode e2 = ((IdNode) factory());
			IdNode idNode = ((IdNode) e);
			idNode.setType(e2.getType());
			idNode.setName(idNode.getName() + "." + e2.getName());
			env = save;
		}
		return e;
	}

	private ExprNode factory()
	{
		if (currentToken.getTag() == '(')
		{
			match('(');
			ExprNode e = fetchExpr();
			match(')');
			return e;
		}
		ExprNode res = currentToken instanceof Num ? new ConstantIntegerNode(((Num) currentToken).getValue()) : env.fetchIdInfo(((Word) currentToken).getStr());
		move();
		return res;
	}

	private ExprNode binaryOperationRecurse(ExprNode e1, Token[] operation, Supplier<ExprNode> method)
	{
		for (Token token : operation)
		{
			if (currentToken.getTag() == token.getTag())
			{
				Token o = currentToken;
				move();
				ExprNode e2 = method.get();
				return binaryOperationRecurse(new BinaryOperationNode(e1, o, e2), operation, method);
			}
		}
		return e1;
	}

	public boolean isValid()
	{
		return valid;
	}

	public boolean isPrint()
	{
		return print;
	}

	public void setPrint(boolean print)
	{
		this.print = print;
	}

	public String getLastError()
	{
		return lastError;
	}

	public Lexer getLexer()
	{
		return lexer;
	}

	public String getEncoding()
	{
		return inputWrapper.getEncoding();
	}

	public void setEncoding(String encoding) throws IOException
	{
		inputWrapper.setEncoding(encoding);
	}
}
