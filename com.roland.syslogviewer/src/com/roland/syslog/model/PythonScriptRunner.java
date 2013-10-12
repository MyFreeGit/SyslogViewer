package com.roland.syslog.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class PythonScriptRunner {
	public static void main(String[] args) throws ScriptException,
			NoSuchMethodException {
		LogContainer container = readLog();
		LogContainer result = new LogContainer();
		pyEngine.put("LogContainer", container);
		pyEngine.put("result", result);
		//pyEngine.put("LogItemsContainer", LogItemsContainer.class);
		try {
			File script = getScript();
			System.out.println(script);
			pyEngine.eval(new FileReader(script));
			result = (LogContainer) pyEngine.get("result");
			System.out.println("-----The result of script is:-----");
			System.out.printf("-----The size of result is %d-----\n", result
					.getLogItemList().size());
			for (ILogItem item : result.getLogItemList()) {
				System.out.println(item.getLogText());
			}

		} catch (ScriptException se) {
			System.out.println(se.getMessage());
		} catch (FileNotFoundException fe) {
			System.out.println(fe.getMessage());
			System.out.println("The script file cannot be found");
			System.exit(1);
		}

	}

	private static File getScript() {
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\TestScript.py";
		return new File(fileName);

	}

	private static LogContainer readLog() {
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\syslog_BasicLogTest.txt";
		return SyslogFileReader.read(fileName);

	}

	private PythonScriptRunner() {
	}

	private static ScriptEngineManager scriptEngineMgr;
	private static ScriptEngine pyEngine;
	static {
		scriptEngineMgr = new ScriptEngineManager();
		pyEngine = scriptEngineMgr.getEngineByName("python");

		if (pyEngine == null) {
			System.out.println("No script engine found for python");
			System.exit(1);
		}
	}

}
