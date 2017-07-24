package com.qwesdfok.Debug;

import com.qwesdfok.Common.InputWrapper;
import com.qwesdfok.Common.MyLib;
import com.qwesdfok.Common.OutputWrapper;
import com.qwesdfok.Parser.Parser;
import com.qwesdfok.Translate.TranslateImpl;

import java.io.IOException;

/**
 * Created by qwesd on 2016/2/2.
 */
public class Main
{
	public static void main(String[] argv)
	{
		System.out.print(System.getProperty("user.dir")+"\n");
//		String path = "D:\\lab\\JavaPro\\SABI\\src\\com\\qwesdfok\\Debug\\";
//		InputWrapper inputWrapper = new InputWrapper(path + "input");
//		OutputWrapper outputWrapper = new OutputWrapper(path + "output");
		InputWrapper inputWrapper = new InputWrapper(argv[0]);
		OutputWrapper outputWrapper = new OutputWrapper(argv[1]);
		Parser parser = new Parser(inputWrapper, outputWrapper,new TranslateImpl());
		try
		{
			if(argv.length==3)
				parser.setEncoding(argv[2]);
			parser.startParse();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.print("//End.\n");
	}
}
