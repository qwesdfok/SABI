package com.qwesdfok.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by qwesd on 2015/11/20.
 */
public class MyLib
{
	private static int logSetting = 0xffffffff;
	public static int TRACE_LOG = 0x1;
	public static int DEBUG_LOG = 0x2;
	public static int INFO_LOG = 0x4;
	public static int WARNING_LOG = 0x8;
	public static int ERROR_LOG = 0x10;
	public static int OUTPUT_LOG = 0x20;

	public static String byteToHexStr(byte[] bytes)
	{
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		char str[] = new char[bytes.length * 2];
		int k = 0;
		for (int i = 0; i < bytes.length; i++)
		{
			byte byte0 = bytes[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	public static void outputLog(String output)
	{
		if ((logSetting & OUTPUT_LOG) != 0) wrapper("[OUTPUT] " + output);
	}

	public static void simplyLog(String data)
	{
		if (!data.endsWith("\n"))
			data = data + "\n";
		writeToStdOut(data);
	}

	public static void traceLog(String trace)
	{
		if ((logSetting & TRACE_LOG) != 0) wrapper("[TRACE] " + trace);
	}

	public static void debugLog(String debug)
	{
		if ((logSetting & DEBUG_LOG) != 0) wrapper("[DEBUG] " + debug);
	}

	public static void infoLog(String info)
	{
		if ((logSetting & INFO_LOG) != 0) wrapper("[INFO] " + info);
	}

	public static void warningLog(String warning)
	{
		if ((logSetting & WARNING_LOG) != 0) wrapper("[WARNING] " + warning);
	}

	public static void errorLog(String error)
	{
		if ((logSetting & ERROR_LOG) != 0) wrapper("[ERROR] " + error);
	}

	private static void wrapper(String str)
	{
		Calendar calendar = Calendar.getInstance();
		if (!str.endsWith("\n"))
			str = str + "\n";
		writeToStdOut(String.format("[%04d-%02d-%02d|%02d:%02d:%02d] %s",
				calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND), str));
	}

	private static void writeToStdOut(String str)
	{
		System.out.print(str);
	}

	public static void enableLog(int logIndex)
	{
		logSetting |= logIndex;
	}

	public static void disableLog(int logIndex)
	{
		logSetting &= ~logIndex;
	}

	public static String combinePath(String abs, String rel)
	{
		if (!(abs.startsWith("\\") || abs.startsWith("/") || abs.matches("[A-Za-z].*")))
			abs = System.getProperty("user.dir");
		String[] data1 = abs.split("\\\\|/");
		String[] data2 = rel.split("\\\\|/");
		ArrayList<String> temp = new ArrayList<String>(Arrays.asList(data1));
		for (String s : data2)
		{
			if (s.equals(".."))
			{
				if (temp.size() < 1)
					return null;
				temp.remove(temp.size() - 1);
			} else if (!s.equals("."))
			{
				temp.add(s);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < temp.size() - 1; i++)
		{
			sb.append(temp.get(i) + "\\");
		}
		if (temp.size() > 0)
		{
			sb.append(temp.get(temp.size() - 1));
		}
		return sb.toString();
	}

	static public String reshape(String s)
	{
		if (s == null)
			return "null";
//		return "\"" + s.replaceAll("\\\\", "\\\\\\\\") + "\"";
		return "\"" + s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\0", "\\0").replaceAll("\\t", "\\\\t").replaceAll("\\n", "\\\\n").replaceAll("\\r", "\\\\r") + "\"";
	}
}
