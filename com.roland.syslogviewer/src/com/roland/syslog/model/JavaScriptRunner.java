package com.roland.syslog.model;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.io.*;

public class JavaScriptRunner {
	public static void run() throws ScriptException,
			NoSuchMethodException {
		LogContainer container = readLog();
		LogContainer result = new LogContainer();
		jsEngine.put("LogContainer", container);
		jsEngine.put("result", result);
		try{
			File script = getScript();
			System.out.println(script);
			jsEngine.eval(new FileReader(script));
			result = (LogContainer)jsEngine.get("result");
			System.out.println("-----The result of script is:-----");
			System.out.printf("-----The size of result is %d-----\n", result.getLogItemList().size());
			for (ILogItem item :result.getLogItemList()){
				System.out.println(item.getLogText());
			}

		}catch (ScriptException se) {
			System.out.println(se.getMessage());			
		}catch (FileNotFoundException fe){
			System.out.println(fe.getMessage());
			System.out.println("The script file cannot be found");
			System.exit(1);
		}
		
	}
	
	private static File getScript(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\TestScript.js";
		return new File(fileName);
		
	}
	
	private static LogContainer readLog(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\syslog_BasicLogTest.txt";
		return SyslogFileReader.read(fileName);
		
	}
		
	
	private JavaScriptRunner(){
	}
	
	private static ScriptEngineManager scriptEngineMgr;
	private static ScriptEngine jsEngine;
	static {
		scriptEngineMgr = new ScriptEngineManager();
		jsEngine = scriptEngineMgr.getEngineByName("JavaScript");

		if (jsEngine == null) {
			System.out.println("No script engine found for JavaScript");
			System.exit(1);
		}	
	}

}
