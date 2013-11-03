package com.roland.syslog.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.CharBuffer;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * This class is not designed for executing in concurrent environment!*/
public class PythonScriptRunner {
	public static ILogSet runScript(LogContainer logs, File script){
		initExecutionEnvironment(logs);
		try{
			return innerRunScript(logs, new FileReader(script), FileReader.class);			
		} catch (FileNotFoundException fe) {
			output.write(fe.getMessage());
		}
		return result;
	}

	public static ILogSet runScript(LogContainer logs, String script){
		initExecutionEnvironment(logs);
		return innerRunScript(logs, script, String.class);			
	}
	private static void initExecutionEnvironment(LogContainer logs){
		result.clear();
		pyEngine.put("SYSLOG", logs);
		pyEngine.put("RESULT", result);
		output.getBuffer().setLength(0);		
	}
	
	private static <T> ILogSet innerRunScript(LogContainer logs, Object script, Class<T> type) {
		try {
			if(type == FileReader.class){
				pyEngine.eval((FileReader)(script));
			}else if(type == String.class){
				pyEngine.eval((String)(script));
			}else{
				output.write("Only can run python script in String or File format!\n Not get: " + type.getName());
			}
		} catch (ScriptException se) {
			output.write(se.getMessage());
		}
		return result;
	}
	

	public static String getOutput(){
		output.flush();
		return output.toString();
	}
	public static boolean isSupported(){
		return pyEngine == null;
	}
	
	private PythonScriptRunner() {
	}

	private static ScriptEngineManager scriptEngineMgr;
	private static ScriptEngine pyEngine = null;
	private static StringWriter output = null;
	private static ILogSet result = null;
	static {
		scriptEngineMgr = new ScriptEngineManager();
		pyEngine = scriptEngineMgr.getEngineByName("python");
		result = new ResultLogList();
		if (pyEngine == null) {
			System.out.println("No script engine found for python");
		}else{
			output = new StringWriter();
			ScriptContext ctx = pyEngine.getContext();
			ctx.setWriter(output);
		}
	}

	public static String readScript(String fileName){
		FileReader reader = null;
		File file = new File(fileName);
		int fileLength = (int)(file.length());
		if(fileLength == 0){
			return "";
		}

		CharBuffer cb = CharBuffer.allocate(fileLength + 1);
		try {
			reader = new FileReader(fileName);
			reader.read(cb);
			cb.flip();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Ignore issues during closing
				}
			}
		}
		return cb.toString();
	}
	
	
}
