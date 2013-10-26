package com.roland.syslog.model.utcases;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;

import com.roland.syslog.model.*;
import com.roland.syslog.model.ILogItem.Field;

import hirondelle.date4j.DateTime;

public class LogItemTest {
	private static LogContainer logContainer;
	private static List<LogItemData> targetData;

	@BeforeClass
	public static void testSetup() {
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\syslog_BasicLogTest.txt";
		logContainer = SyslogFileReader.read(fileName);
		targetData = initTestSyslogFileReader();
	}

	@Test
	public void testSyslogFileReader() {
		List<ILogItem> items = logContainer.getLogItemList();
		assertTrue(items.size() == 12);

		for (int i = 0; i < targetData.size(); i++) {
			assertLogItem(items.get(i), targetData.get(i));
		}
	}

	@Test
	public void TestLogContainer(){
		System.out.println("============ Begin of TestLogContainer() ==============");
		ILogItem item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(1));
		item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(2));
		item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(10));
		item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(1));
		item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(2));
		item = logContainer.findNext("CFPU");
		assertLogItem(item, targetData.get(10));

		item = logContainer.findPrev("CFPU");
		assertLogItem(item, targetData.get(2));
		item = logContainer.findPrev("CFPU");
		assertLogItem(item, targetData.get(1));
		item = logContainer.findPrev("CFPU");
		assertLogItem(item, targetData.get(10));

		item = logContainer.findNext("xyz");
		System.out.println(item);
		assertTrue(item == null);
		
		ILogSet result = logContainer.findAll("warn");
		assertTrue(result.getLogItemList().size() == 2);
		assertLogItem(result.getLogItemList().get(0), targetData.get(4));
		assertLogItem(result.getLogItemList().get(1), targetData.get(9));
		
		result = logContainer.findAll("xyz");
		assertTrue(result.getLogItemList().size() == 0);

		System.out.println("------------ Begin test of nav selected item ------------------");
		logContainer.unselectAll();
		assertTrue(logContainer.getSelectCount() == 0);
		
		logContainer.getLogItemList().get(1).select();
		logContainer.getLogItemList().get(2).select();
		logContainer.getLogItemList().get(5).select();
		logContainer.getLogItemList().get(10).select();
		logContainer.getLogItemList().get(11).select();
		assertTrue(logContainer.getSelectCount() == 5);
		
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(1));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(2));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(5));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(10));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(11));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(1));
	
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(11));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(10));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(5));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(2));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(1));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(11));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(10));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(5));

		item = logContainer.navNext();
		assertLogItem(item, targetData.get(10));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(11));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(1));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(2));
		item = logContainer.findNext("crit");
		assertLogItem(item, targetData.get(11));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(1));
		item = logContainer.findNext("crit");
		assertLogItem(item, targetData.get(2));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(5));
		item = logContainer.findPrev("alert");
		assertLogItem(item, targetData.get(1));
		
		
		logContainer.getLogItemList().get(0).select();
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(0));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(11));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(0));

		logContainer.setPosition(logContainer.getLogItemList().get(1));
		item = logContainer.navNext();
		assertLogItem(item, targetData.get(2));
		logContainer.setPosition(logContainer.getLogItemList().get(6));
		item = logContainer.navPrev();
		assertLogItem(item, targetData.get(5));

		logContainer.setPosition(null);
		logContainer.unselectAll();
		assertTrue(logContainer.getSelectCount() == 0);
		System.out.println("============= End of TestLogContainer() ===============");
	}

	private void assertLogItem(ILogItem item, LogItemData data) {
		System.out.println(item.toString());
		for (LogItem.Field field : LogItem.Field.values()) {
			assertTrue(item.containField(field) == data.containField(field));				
			switch (field) {
			case TimeStamp:
				if (data.containField(field)) {
					assertTrue(item.getTimeStamp().equals(data.timeStamp));
					assertTrue(item.getPureTime().equals(data.pureTime));
					System.out.println(item.getTimeStamp() + " -- " + item.getPureTime());
				}
				break;
			case Severity:
				if (data.containField(field)) {
					assertTrue(item.getSeverity() == data.severity);
					System.out.println(item.getSeverity());
				}
				break;
			case RU:
				if (data.containField(field)) {
					assertTrue(item.getRU().equals(data.ru));
					System.out.println(item.getRU());
				}
				break;
			case PRB:
				if (data.containField(field)) {
					assertTrue(item.getPRB().equals(data.prb));
					System.out.println(item.getPRB());
				}
				break;
			case Text:
				if (data.containField(field)) {
					assertTrue(item.getLogText().equals(data.logText));
					System.out.println(item.getLogText());
				}
				break;
			}
		}
		System.out.println("");
	}
	
	private static List<LogItemData> initTestSyslogFileReader() {
		LinkedList<LogItemData> list = new LinkedList<LogItemData>();
		LogItemData item;
		item = new LogItemData();
		item.timeStamp = new DateTime("2012-01-01 14:46:33.006819");
		item.severity = Severity.emerg;
		item.logText = "lmp-1-2-1.managementnet.localdomain dhclient: DHCPREQUEST on eth0.800 to 169.254.240.4 port 67";
		item.fields = EnumSet.of(Field.TimeStamp, Field.Severity, Field.Text);
		item.pureTime = "14:46:33.006819";
		list.add(item);
		
		item = new LogItemData();
		item.timeStamp = new DateTime("2012-02-29 14:46:33.029897");
		item.severity = Severity.alert;
		item.ru = "CFPU-0";
		item.prb = "dhcpd";
		item.logText = "DHCPREQUEST for 169.254.240.21 from 00:d0:c9:be:6e:00 (BCNMB-A) via xaui0";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:46:33.029897";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-03-03 14:46:33.030212");
		item.severity = Severity.crit;
		item.ru = "CFPU-11";
		item.prb = "HASClusterManag[3792]";
		item.logText = "INFO Rejecting switchover request; Operation target is not an active standby RG. Target=/CSPU-0";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:46:33.030212";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-04-04 14:46:33.110757");
		item.severity = Severity.err;
		item.ru = "EITP-1";
		item.prb = "kernel";
		item.logText = "dmxmsg: dmxmsg_print_msg: LOG_TRANS_FAIL, 352 callbacks suppressed";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:46:33.110757";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-05-05 14:46:33.798753");
		item.severity = Severity.warn;
		item.logText = "syslogd 1.4.1: restart (remote reception).";
		item.fields = EnumSet.of(Field.TimeStamp, Field.Severity, Field.Text);
		item.pureTime = "14:46:33.798753";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-06-06 14:46:33.861342");
		item.severity = Severity.notice;
		item.ru = "CSPU-0";
		item.prb = "FUnitStateAgent";
		item.logText = "RU:OMU-0|PNAME:IL_UnitStateAgent|LPID:20089|PID:0x3DA 0x1 0x0|TIME:2013-04-09 14:47:55:730326|TEXT:REAS_NOTIFY_HAND|DATA:|unit state synchronous not complete status: 69";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:46:33.861342";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-07-07 14:46:33.873048");
		item.severity = Severity.debug;
		item.logText = "lmp-1-2-1.managementnet.localdomain dhclient: bound to 169.254.240.21 -- renewal in 279 seconds.";
		item.fields = EnumSet.of(Field.TimeStamp, Field.Severity, Field.Text);
		item.pureTime = "14:46:33.873048";
		list.add(item);
		
		item = new LogItemData();
		item.timeStamp = new DateTime("2012-08-08 14:47:00.032745");
		item.severity = Severity.info;
		item.ru = "USCP-0";
		item.prb = "fmpprb";
		item.logText = "RU:OMU-0|PNAME:IL_Fmpprb|LPID:20121|PID:0x677 0x1 0x0|TIME:2013-04-09 14:47:00:032609|TEXT:data for pm9 is ready now|DATA:";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:47:00.032745";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-09-09 14:47:01.356376");
		item.severity = Severity.notice;
		item.ru = "CSCP-1";
		item.prb = "crond[11196]";
		item.logText = "(root) CMD (nice /opt/nokiasiemens/bin/timestamp.sh)";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:47:01.356376";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-10-10 14:47:01.979672");
		item.severity = Severity.warn;
		item.ru = "CSPU-6";
		item.prb = "lemana";
		item.logText = ":2013-04-09 14:48:00.178214 [LIBGEN: INFO] process: 0001/0744/0000/00:thread 1 begin sleep...";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:47:01.979672";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-11-11 14:47:16.114741");
		item.severity = Severity.err;
		item.ru = "CFPU-0";
		item.prb = "ILSIGIUBCLI";
		item.logText = ":2013-04-09 14:47:16.114310 [LIBGEN: INFO] process: 0100/fdab/0000/00:Starting TNSDL family 'clipro' with number 0xFDAB and linux PID 8321 (config match:0,0)";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:47:16.114741";
		list.add(item);

		item = new LogItemData();
		item.timeStamp = new DateTime("2012-12-12 14:47:16.137754");
		item.severity = Severity.crit;
		item.ru = "CSPU-3";
		item.prb = "mfsprb1x";
		item.logText = ":2013-04-09 14:48:04.731256 [LIBGEN: INFO] process: 000c/0a3c/0000/00:Starting TNSDL family 'mfsmas' with number 0xA3C and linux PID 4049 (config match:0,0)";
		item.fields = EnumSet.allOf(Field.class);
		item.pureTime = "14:47:16.137754";
		list.add(item);

		return list;
	}
}
