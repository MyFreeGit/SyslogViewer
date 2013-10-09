package com.roland.syslog.model.utcases;

import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;

import com.roland.syslog.model.*;
import com.roland.syslog.model.LogItem.Field;

import hirondelle.date4j.DateTime;

public class LogItemTest {
	private class LogItemData {
		public DateTime timeStamp;
		public Severity severity;
		public String ru;
		public String prb;
		public EnumSet<Field> fields;
		public String logText; // After all meaningful fields are chopped out, the pure text
		public String pureTime;

		public boolean containField(Field field) {
			return fields.contains(field);
		}

	}

	@BeforeClass
	public static void testSetup() {
	}

	@Test
	public void testSyslogFileReader() {
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\syslog_BasicLogTest.txt";
		LogItemsContainer container = SyslogFileReader.read(fileName);
		List<LogItem> items = container.getLogItemList();
		assertTrue(items.size() == 12);

		List<LogItemData> results = initTestSyslogFileReader();

		for (int i = 0; i < results.size(); i++) {
			for (LogItem.Field field : LogItem.Field.values()) {
				assertTrue(items.get(i).containField(field) == results.get(i).containField(field));
				switch (field) {
				case TimeStamp:
					if (results.get(i).containField(field)) {
						assertTrue(items.get(i).getTimeStamp().equals(results.get(i).timeStamp));
						assertTrue(items.get(i).getPureTime().equals(results.get(i).pureTime));
						System.out.println(items.get(i).getTimeStamp() + " -- " + items.get(i).getPureTime());
					}
					break;
				case Severity:
					if (results.get(i).containField(field)) {
						assertTrue(items.get(i).getSeverity() == results.get(i).severity);
						System.out.println(items.get(i).getSeverity());
					}
					break;
				case RU:
					if (results.get(i).containField(field)) {
						assertTrue(items.get(i).getRU().equals(results.get(i).ru));
						System.out.println(items.get(i).getRU());
					}
					break;
				case PRB:
					if (results.get(i).containField(field)) {
						assertTrue(items.get(i).getPRB().equals(results.get(i).prb));
						System.out.println(items.get(i).getPRB());
					}
					break;
				case Text:
					if (results.get(i).containField(field)) {
						assertTrue(items.get(i).getLogText().equals(results.get(i).logText));
						System.out.println(items.get(i).getLogText());
					}
					break;
				}
			}
			System.out.println();
		}
	}

	private List<LogItemData> initTestSyslogFileReader() {
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
