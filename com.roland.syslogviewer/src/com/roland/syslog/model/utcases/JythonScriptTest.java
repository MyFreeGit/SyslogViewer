package com.roland.syslog.model.utcases;

import java.io.File;

import javax.script.ScriptException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;


import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.PythonScriptRunner;
import com.roland.syslog.model.ResultLogList;
import com.roland.syslog.model.SyslogFileReader;

import static com.roland.syslog.model.utcases.UTHelper.*;
import static org.junit.Assert.*;

public class JythonScriptTest {
	private static LogContainer logs = null;
	
	@BeforeClass
	public static void testSetup(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\LogItemFilterTest.txt";
		logs = SyslogFileReader.read(fileName);
	}
	
	@Test
	public void testScript(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\TestScript.py";
		File script = new File(fileName);
		ILogSet result = PythonScriptRunner.runScript(logs, script);
		String output = PythonScriptRunner.getOutput();
		assertTrue(output.equals("Hello World!\n"));
		int x = result.getLogItemList().size();
		int []target_idx = new int[x];
		for(int i = 0; i < result.getLogItemList().size(); i++){
			target_idx[i] = x--;
		}
		UTHelper.assertResult(result, target_idx);

		//Run the script twice.
		result = PythonScriptRunner.runScript(logs, script);
		output = PythonScriptRunner.getOutput();
		assertTrue(output.equals("Hello World!\n"));
		UTHelper.assertResult(result, target_idx);
		
		String scriptString = "list = SYSLOG.getLogItemList();\n"
				+ "size = list.size();\n" + "print \"Hello World!\"\n"
				+ "for i in range(size):\n"
				+ "\tRESULT.add(list.get(size - i - 1));";
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		assertTrue(output.equals("Hello World!\n"));
		UTHelper.assertResult(result, target_idx);
	
		scriptString = PythonScriptRunner.getScriptTemplate();
		System.out.print(scriptString);
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		System.out.println(output);
		assertTrue(output.equals("Hello World!\n"));
		for(int i = 0; i < result.getLogItemList().size(); i++){
			target_idx[i] = i+1;
		}		
		UTHelper.assertResult(result, target_idx);
	}
		
	@Test
	public void testLogContainerReadOnly(){
		ILogSet result;
		String output, scriptString;
		final String exception = "java.lang.UnsupportedOperationException";
		int []target_idx = new int[logs.getLogItemList().size()];
		for(int i = 0; i < logs.getLogItemList().size(); i++){
			target_idx[i] = i + 1;
		}

		scriptString = "list = SYSLOG.clear();\n";
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		assertTrue(output.contains(exception));
		UTHelper.assertResult(logs, target_idx);
		
		scriptString = "list = SYSLOG.addAll(SYSLOG);\n";
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		assertTrue(output.contains(exception));
		UTHelper.assertResult(logs, target_idx);
		
		scriptString = "from com.roland.syslog.model import ILogItem;\n"
				+ "list = SYSLOG.sort(ILogItem.Field.TimeStamp);\n";
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		assertTrue(output.contains(exception));
		UTHelper.assertResult(logs, target_idx);
		
		scriptString = "SYSLOG.add(SYSLOG.getLogItemList().get(0));\n";
		result = PythonScriptRunner.runScript(logs, scriptString);
		output = PythonScriptRunner.getOutput();
		System.out.println(output);
	    assertTrue(output.contains(exception));
		UTHelper.assertResult(logs, target_idx);

}
}
