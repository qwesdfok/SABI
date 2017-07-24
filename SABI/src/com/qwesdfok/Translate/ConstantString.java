package com.qwesdfok.Translate;

import com.qwesdfok.Parser.Env;

/**
 * Created by qwesd on 2016/2/15.
 */
public class ConstantString
{
	public String importStr = "import java.io.*;\nimport java.util.*;\nimport java.util.function.Function;\n";

	public String interfaceStr = "" +
			"interface InputWrapperInterface\n" +
			"{\n" +
			"int read()throws IOException;\n" +
			"boolean valid() throws IOException;" +
			"}\n" +

			"interface OutputWrapperInterface\n" +
			"{\n" +
			"void write(String data)throws IOException;\n" +
			"}\n";
	public String interfaceImplStr = "" +
			"class InputWrapper implements InputWrapperInterface\n" +
			"{\n" +
			"private FileInputStream inputStream;\n" +
			"private boolean eof = false;\n" +
			"public InputWrapper(String fileName)throws FileNotFoundException{inputStream = new FileInputStream(fileName);}\n" +
			"public int read()throws IOException{int data=inputStream.read();if(data==-1) {if(eof) throw new EOFException(\"Eof\");else eof = true;}return data;}\n" +
			"public boolean valid()throws IOException{return inputStream.available()!=0;}\n" +
			"}\n" +

			"class OutputWrapper implements OutputWrapperInterface\n" +
			"{\n" +
			"private FileOutputStream outputStream;\n" +
			"public OutputWrapper(String fileName)throws FileNotFoundException{outputStream = new FileOutputStream(fileName);}\n" +
			"public void write(String data)throws IOException{outputStream.write(data.getBytes());}\n" +
			"}\n";
	public String rootClassHeadStr = "" +
			"abstract class RootClass\n" +
			"{\n" +
			"protected static InputWrapper inputWrapper;\n" +
			"protected static OutputWrapper outputWrapper;\n" +
			"protected static long offset=0;\n" +
			"protected int length=0;\n" +
			"protected boolean valid=true;\n" +
			"protected String name;\n";
	public String rootClassTailStr = "" +
			"public RootClass(String name, int length){this.length=length;this.name=name;}\n" +
			"public static long readLong(int length) throws IOException {long res = 0;int index = 0;while (length > 0){if (_bigend_.equalsIgnoreCase(\"true\"))res += ((long)inputWrapper.read() << ((length - 1) * 8));else res += ((long)inputWrapper.read() << (index * 8));index++;length--;}return res;}\n" +
			"public static int readInteger(int length)throws IOException{return (int)readLong(length);}\n" +
			"public static float readFloat(int length)throws IOException{return Float.intBitsToFloat(readInteger(length));}\n" +
			"public static boolean readBoolean(int length)throws IOException{return (readInteger(length))!=0;}\n" +
			"public static double readDouble(int length)throws IOException{return Double.longBitsToDouble(readLong(length));}\n" +
			"public static String readString(int length)throws IOException {StringBuilder sb = new StringBuilder();if (length == 0) {int d = inputWrapper.read();while (d!='\\0') {sb.append((char) d);d = inputWrapper.read();}return sb.toString();}while (length != 0) {sb.append((char) inputWrapper.read());length--;}return sb.toString();}\n" +
			"public static String toHexStr(long data,int len,boolean isX){StringBuilder sb = new StringBuilder();char[] tablex={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};char[] tableX={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};for(int i=0;i<len*2;i++){if(isX)sb.append(tableX[(int)data&0xf]); else sb.append(tablex[(int)data&0xf]);data=data>>>4;}return sb.reverse().toString();}\n" +
			"public static void setInputWrapper(InputWrapper inputWrapper){RootClass.inputWrapper=inputWrapper;}\n" +
			"public static InputWrapper getInputWrapper(){return RootClass.inputWrapper;}\n" +
			"public static void setOutputWrapper(OutputWrapper outputWrapper){RootClass.outputWrapper=outputWrapper;}\n" +
			"public static OutputWrapper getOutputWrapper(){return RootClass.outputWrapper;}\n" +
			"public int getLength(){return length;}\n" +
			"public void setLength(int length){this.length=length;}\n" +
			"public String getName(){return name;}\n" +
			"public void setName(String name){this.name=name;}\n" +
			"public String getTypeName(){return null;}\n" +
			"public boolean isValid(){return valid;}\n" +
			"}\n";
	public String selfClassStr = "" +
			"class MutableType<T extends RootClass> extends RootClass \n" +
			"{\n" +
			"private ArrayList<T> dataList = new ArrayList<>();\n" +
			"private Function<Integer, T> getInstanceFunction;\n" +
			"private long count;\n" +
			"private String typeName;\n" +
			"public MutableType(String name, int length){super(name, length);}\n" +
			"public MutableType(String name, int length, String _iterway_){super(name, length);this._iterway_=_iterway_;}\n" +
			"public int toInt(){int res = 0;for (T t : dataList) {res += t.toInt();}return res;}\n" +
			"public void read() throws IOException {dataList.clear();if(count<0) count = Long.MAX_VALUE;for (int i = 0; i < count; i++){T obj = getInstanceFunction.apply(length);String res = _iterway_.replaceAll(\"%[iI]\",\"%0i\");String[] ni = res.replaceAll(\".*?(%\\\\d+[iI])((.*?)(?=%\\\\d+i)|.*$)\", \"$1\\n\").split(\"\\n\");for (int t = 0; t < ni.length; t++)res = res.replaceAll(ni[t], Integer.toString(i + Integer.parseInt(ni[t].substring(1, ni[t].length() - 1))));res = res.replaceAll(\"%[iI]\", Long.toString(i)).replaceAll(\"%[hH]\", Long.toString(i, 16)).replaceAll(\"%[xX]\", \"0x\" + Long.toString(i, 16)).replaceAll(\"%[nN]\", \"name\").replaceAll(\"%[tT]\", \"typeName\").replaceAll(\"%%\", \"%\");obj.setName(res.replaceAll(\"%[nN]\",\"name\").replaceAll(\"%[tT]\",\"typeName\").replaceAll(\"%%\",\"%\"));obj.read();dataList.add(obj);if(count==Long.MAX_VALUE&&obj.isEmpty()) break;}}\n" +
			"public void write() throws IOException {outputWrapper.write(getPrintStr());}\n" +
			"public String getOutStr() {StringBuilder sb = new StringBuilder();for (T t : dataList) {sb.append(t.getOutStr());sb.append(_insepa_);}if(dataList.size()>0) sb.delete(sb.length()-_insepa_.length(),sb.length());return sb.toString();}\n" +
			"public String formatStr(String f, String sepa) {StringBuilder sb = new StringBuilder();for (T t : dataList) {sb.append(t.formatStr(f, sepa));sb.append(sepa);}if(dataList.size()>0) sb.delete(sb.length() - sepa.length(), sb.length());return sb.toString();}\n" +
			"public void config(String typeName,int count, Function<Integer, T> function) {this.typeName=typeName;this.count = count;getInstanceFunction = function;}\n" +
			"public String getTypeName(){return typeName;}\n" +
			"}\n" +

			"class SelfInteger extends RootClass\n" +
			"{\n" +
			"private int v;\n" +
			"public SelfInteger(String name, int length){super(name, length);}\n" +
			"public static SelfInteger getInstance(int length){return new SelfInteger(null,length);}\n" +
			"public int toInt(){return v;}\n" +
			"public String getOutStr(){return Integer.toString(v);}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%X\",\"0X%H\").replaceAll(\"%x\",\"0x%h\").replaceAll(\"%h\",toHexStr(v,length,false)).replaceAll(\"%H\",toHexStr(v,length,true)).replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{v=readInteger(length);offset+=length;}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"int\";}\n" +
			"}\n" +

			"class SelfFloat extends RootClass\n" +
			"{\n" +
			"private float v;\n" +
			"public SelfFloat(String name, int length){super(name,length);}\n" +
			"public static SelfFloat getInstance(int length){return new SelfFloat(null,length);}\n" +
			"public int toInt(){return (int)v;}\n" +
			"public String getOutStr(){return Float.toString(v);}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%[xX]\",\"%v\").replaceAll(\"%[hH]\",\"%v\").replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{offset+=length;v=readFloat(length);}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"float\";}\n" +
			"}\n" +

			"class SelfBoolean extends RootClass\n" +
			"{\n" +
			"private boolean v;\n" +
			"public SelfBoolean(String name, int length){super(name,length);}\n" +
			"public static SelfBoolean getInstance(int length){return new SelfBoolean(null,length);}\n" +
			"public int toInt(){return v?1:0;}\n" +
			"public String getOutStr(){return Boolean.toString(v);}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%[xX]\",\"%v\").replaceAll(\"%[hH]\",\"%v\").replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{offset+=length;v=readBoolean(length);}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"boolean\";}\n" +
			"}\n" +

			"class SelfLong extends RootClass\n" +
			"{\n" +
			"private long v;\n" +
			"public SelfLong(String name,int length){super(name,length);}\n" +
			"public static SelfLong getInstance(int length){return new SelfLong(null,length);}\n" +
			"public int toInt(){return (int)v;}\n" +
			"public String getOutStr(){return Long.toString(v);}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%X\",\"0X%H\").replaceAll(\"%x\",\"0x%h\").replaceAll(\"%h\",toHexStr(v,length,false)).replaceAll(\"%H\",toHexStr(v,length,true)).replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{offset+=length;v=readLong(length);}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"long\";}\n" +
			"}\n" +

			"class SelfDouble extends RootClass\n" +
			"{\n" +
			"private double v;\n" +
			"public SelfDouble(String name, int length){super(name,length);}\n" +
			"public static SelfDouble getInstance(int length){return new SelfDouble(null,length);}\n" +
			"public int toInt(){return (int)v;}\n" +
			"public String getOutStr(){return Double.toString(v);}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%[xX]\",\"%v\").replaceAll(\"%[hH]\",\"%v\").replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{offset+=length;v=readDouble(length);}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"double\";}\n" +
			"}\n" +

			"class SelfString extends RootClass\n" +
			"{\n" +
			"private String v;\n" +
			"public SelfString(String name, int length){super(name, length);}\n" +
			"public static SelfString getInstance(int length){return new SelfString(null,length);}\n" +
			"public int toInt(){return 0;}\n" +
			"public String getOutStr(){return v;}\n" +
			"public String formatStr(String f,String sepa){return f.replaceAll(\"%[xX]\",\"%v\").replaceAll(\"%[hH]\",\"%v\").replaceAll(\"%[tT]\",getTypeName()).replaceAll(\"%[vV]\",getOutStr()).replaceAll(\"%[nN]\",name).replaceAll(\"%%\",\"%\");}\n" +
			"public void read()throws IOException{v=readString(length);length = v.length();offset+=v.length();}\n" +
			"public void write()throws IOException{outputWrapper.write(getPrintStr());}\n" +
			"public String getTypeName(){return \"String\";}\n" +
			"}\n";
	public String mainClassHead = "" +
			"public class MainClass\n{\n" +
			"public static InputWrapper inputWrapper;\n" +
			"public static OutputWrapper outputWrapper;\n" +
			"public static int read()throws IOException{return inputWrapper.read();}\n" +
			"public static void write(String data)throws IOException{outputWrapper.write(data);}\n" +
			"public static void skip(String data)throws IOException{for (int i = 0; i < data.length(); i++) if(inputWrapper.read()!=data.charAt(i)) throw new RuntimeException(\"Skip not match. Expected ASCII char is \" + (int) data.charAt(i)+\" in offset \"+RootClass.offset);else RootClass.offset+=1;}\n" +
			"public static void read(RootClass obj)throws IOException{obj.preFunction();obj.read();}\n" +
			"public static void write(RootClass obj)throws IOException{obj.innerFunction();if(obj.isValid())obj.write();else obj.invalidFunction();obj.endFunction();}\n";
	public String mainFunction = "" +
			"public static void main(String[]argv)\n{\ntry\n{\n" +
			"boolean integrity=false;\n" +
			"inputWrapper=new InputWrapper(RootClass._input_);\n" +
			"outputWrapper = new OutputWrapper(RootClass._output_);\n" +
			"RootClass.setInputWrapper(inputWrapper);\n" +
			"RootClass.setOutputWrapper(outputWrapper);\n";

	public String mainPad = "skip(RootClass._skip_);\nintegrity = true;\n";

	public String mainClassTail = "}catch(Exception e){e.printStackTrace();}\n}//main-function\n}//main-class\n";
}
