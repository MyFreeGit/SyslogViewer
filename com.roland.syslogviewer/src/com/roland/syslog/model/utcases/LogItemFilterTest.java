package com.roland.syslog.model.utcases;

import org.junit.BeforeClass;

import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.SyslogFileReader;

public class LogItemFilterTest {
	private LogContainer logs;

	@BeforeClass
	public void testSetup(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\syslog_BasicLogTest.txt";
		logs = SyslogFileReader.read(fileName);
	}
}
