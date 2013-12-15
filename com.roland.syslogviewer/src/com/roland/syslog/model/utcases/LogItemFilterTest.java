package com.roland.syslog.model.utcases;

import java.util.Arrays;
import java.util.EnumSet;

import hirondelle.date4j.DateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.roland.syslog.model.ILogItem.Field;
import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.Severity;
import com.roland.syslog.model.SyslogFileReader;
import static com.roland.syslog.model.utcases.UTHelper.*;

public class LogItemFilterTest {
	private static LogContainer logs;

	@BeforeClass
	public static void testSetup(){
		String fileName = System.getProperty("user.dir")
				+ "\\src\\com\\roland\\syslog\\model\\utcases\\LogItemFilterTest.txt";
		logs = SyslogFileReader.read(fileName);
	}
	
	@Test
	public void testDatetimeFilter(){
		DateTime begin = new DateTime("2012-09-23 13:39:47.000382");
		DateTime end = new DateTime("2012-09-30 00:00:00.083656");
		ILogSet result = logs.betweenTime(begin, end);
		int[] target_idx = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				            19, 20, 21, 22, 23, 24, 25, 26, 27, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		
		result = logs.betweenTime(begin, begin);
		target_idx = new int[]{1};
		assertResult(result, target_idx);

		begin =  new DateTime("2012-09-29 13:39:47.099982");
		end = new DateTime("2012-10-01 12:00:00.883656");
		result = logs.betweenTime(begin, end);
		target_idx = new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);

		begin =  new DateTime("2012-09-23 13:39:57.000324");
		end = new DateTime("2012-09-25 12:00:00.883656");
		result = logs.betweenTime(begin, end);
		target_idx = new int[]{3};
		assertResult(result, target_idx);	
		
		begin =  new DateTime("2012-09-23 13:39:57.000024");
		end = new DateTime("2012-09-25 12:00:00.883656");
		result = logs.betweenTime(begin, end);
		target_idx = new int[]{3};
		assertResult(result, target_idx);	

		begin =  new DateTime("2012-09-23 13:39:57.008324");
		end = new DateTime("2012-09-25 12:00:00.883656");
		result = logs.betweenTime(begin, end);
		target_idx = new int[]{3};
		assertTrue(result.getLogItemList().size() == 0);
		
		result = logs.betweenTime("09-23 13:39:57.000024", "09-25 12:00:00.883656",
				  ILogSet.DATETIME_DIGITAL_FORMAT);
		target_idx = new int[]{3};
		assertResult(result, target_idx);

		result = logs.betweenTime("Sep 23 13:39:57.000024", "Sep 25 12:00:00.883656",
				  ILogSet.DATETIME_ORGINAL_FORMAT);
		target_idx = new int[]{3};
		assertResult(result, target_idx);

	}

	@Test
	public void testSeverityFilter(){
		ILogSet result = logs.filterWithSeverity(EnumSet.noneOf(Severity.class));
		assertTrue(result.isEmpty());
		result = logs.filterWithSeverity();
		assertTrue(result.isEmpty());

		result = logs.filterWithSeverity(EnumSet.of(Severity.info));
		int[] target_idx = {1, 2, 4, 6, 7, 10, 12, 13, 14, 15, 18,
	            19, 20, 21, 27, 29, 30, 31, 32, 33, 34};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info");
		assertResult(result, target_idx);
		
		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err));
		target_idx = new int[]{1, 2, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 18,
	            19, 20, 21, 24, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, Severity.warn));
		target_idx = new int[]{1, 2, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18,
	            19, 20, 21, 24, 25, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, 
				      Severity.warn, Severity.alert));
		target_idx = new int[]{1, 2, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18,
	            19, 20, 21, 22, 24, 25, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn", "alert");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, 
			      Severity.warn, Severity.alert, Severity.crit));
		target_idx = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 24, 25, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn", "alert", "crit");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, 
			      Severity.warn, Severity.alert, Severity.crit, Severity.emerg));
		target_idx = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 23, 24, 25, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn", "alert", "crit", "emerg");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, 
			      Severity.warn, Severity.alert, Severity.crit, Severity.emerg,
			      Severity.debug));
		target_idx = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn", "alert", "crit", "emerg", "debug");
		assertResult(result, target_idx);

		result = logs.filterWithSeverity(EnumSet.of(Severity.info, Severity.err, 
			      Severity.warn, Severity.alert, Severity.crit, Severity.emerg,
			      Severity.debug, Severity.notice));
		target_idx = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
				19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithSeverity("info", "err", "warn", "alert", "crit", "emerg", "debug", "notice");
		assertResult(result, target_idx);
	}

	@Test
	public void testFieldFilter(){
		ILogSet result = null;
		int[] target_idx = null;
		
/*		result = logs.filterWithStringOr(EnumSet.of(Field.Severity),
				                         new String[] {"info", "err"});
		target_idx = new int[]{1, 2, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 18,
	            19, 20, 21, 24, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);

		result = logs.filterWithStringAnd(Field.Text, new String[] {"Your", "PC", "World"});
		target_idx = new int[]{2, 5};
		assertResult(result, target_idx);
*/		result = logs.textContainsAllStrings("Your", "PC", "World");
		assertResult(result, new int[]{2, 5});
		result = logs.textContainsAllStrings("Your", "PC", "sings", "World");
		assertResult(result, new int[]{5});		
		
/*		result = logs.filterWithStringAnd(Field.Text,
                new String[] {"Your", "PC", "World"})
                     .filterWithStringAnd(Field.Severity, new String[] {"crit"});
		assertResult(result, new int[]{5});
*/		result = logs.textContainsAllStrings("Your", "PC", "World")
				     .filterWithSeverity("crit");
		assertResult(result, new int[]{5});
	}
	
	@Test
	public void testLogTextFilter(){
		ILogSet result = null;
		
		result = logs.textContainsAllStrings();
		assertTrue(result.isEmpty());
		result = logs.textContainsOneOfStrings();
		assertTrue(result.isEmpty());
		
		result = logs.textContainsAllStrings("sings", "Your PC");
		assertResult(result, new int[]{5, 13, 14});
		result = logs.textContainsAllStrings("sings", "Your PC", "Goodbye");
		assertResult(result, new int[]{5});
		result = logs.textContainsAllStrings("HAS", "err", "log");
		assertResult(result, new int[]{8});
		result = logs.textContainsAllStrings("System", "info");
		assertResult(result, new int[]{24});
		result = logs.textContainsAllStrings("sings", "Your PC", "Hello", "Rock");
		assertTrue(result.isEmpty());
		
		result = logs.textContainsOneOfStrings("sings", "Your PC");
		assertResult(result, new int[]{2, 5, 13, 14, 30});
		
		result = logs.textContainsOneOfStrings("OS", "kill", "PRB1", "PRB2");
		assertResult(result, new int[]{6});

		result = logs.textContainsOneOfStrings("sings", "Your PC", "says");
		assertResult(result, new int[]{2, 5, 13, 14, 29, 30});
		
		result = logs.textContainsOneOfStrings("sings", "Your PC", "says", "HAS");
		assertResult(result, new int[]{2, 5, 8, 13, 14, 19, 29, 30});

		result = logs.textContainsOneOfStrings("sings", "Your PC", "says", "HAS", "System Error");
		assertResult(result, new int[]{2, 5, 8, 13, 14, 19, 24, 29, 30});
	}
	
	@Test
	public void testPRB_RU_Filter(){
		ILogSet result = null;
		int[] target_idx = null;
				
		result = logs.filterWithRU("RU-1");
		target_idx = new int[]{1, 2, 4, 5, 9, 10, 11, 12, 15, 16, 18, 21, 22, 23, 25, 27, 28, 31, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithRU("RU-1", "RU-2");
		target_idx = new int[]{1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 15, 16, 18, 21,
				         22, 23, 25, 26, 27, 28, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithRU("RU-1", "RU-2", "RU-3");
		target_idx = new int[]{1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 14, 15, 16, 17, 18, 20, 21,
				         22, 23, 25, 26, 27, 28, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithRU();
		assertTrue(result.isEmpty());
		result = logs.filterWithRU("RU-5");
		assertTrue(result.isEmpty());
		
		result = logs.filterWithPRB("PRB1");
		target_idx = new int[]{3, 4, 7, 9, 10, 11, 12, 14, 18, 20, 22, 23, 25, 26, 27, 31, 32, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithPRB("PRB1", "PRB2");
		target_idx = new int[]{2, 3, 4, 5, 7, 9, 10, 11, 12, 14, 17, 18, 20, 22, 23, 
				        25, 26, 27, 28, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithPRB("PRB1", "PRB2", "PRB3");
		target_idx = new int[]{1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 14, 15, 17, 18, 20, 21, 22, 23, 
				        25, 26, 27, 28, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithPRB("PRB1", "PRB2", "PRB3", "PRB4");
		target_idx = new int[]{1, 2, 3, 4, 5, 7, 9, 10, 11, 12, 14, 15, 16, 17, 18, 20, 21, 22, 23, 
				        25, 26, 27, 28, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);
		result = logs.filterWithPRB();
		assertTrue(result.isEmpty());
		result = logs.filterWithPRB("PRB-5");
		assertTrue(result.isEmpty());
		
		result = logs.filterWithRU("RU-1").filterWithPRB("PRB1");
		assertResult(result, new int[]{4, 9, 10, 11, 12, 18, 22, 23, 25, 27, 31, 34, 35});
		result = logs.filterWithRU("RU-1").filterWithPRB("PRB1")
				     .textContainsAllStrings("working");
		assertResult(result, new int[]{18, 34});
		result = logs.filterWithRU("RU-1").filterWithPRB("PRB1")
				     .textContainsOneOfStrings("working", "rest", "burbling");
		assertResult(result, new int[]{10, 18, 23, 34});
		result = logs.filterWithRU("RU-1").filterWithPRB("PRB2", "PRB3");
		assertResult(result, new int[]{1, 2, 5, 15, 21, 28, 33});
		result = logs.filterWithPRB("PRB3").filterWithRU("RU-1");
		assertResult(result, new int[]{1, 15, 21});
		result = logs.filterWithPRB("PRB3").filterWithPRB("PRB1");
		assertTrue(result.isEmpty());	
	}
	
	@Test
	public void testSorter(){
		ILogSet result = null;
		int[] target_idx = null;
		
		result = logs.filterWithRU("RU-2").sort(Field.PRB);
		assertResult(result, new int[]{3, 7, 32, 26});

		result = logs.filterWithRU("RU-1").sort(Field.PRB);
		target_idx = new int[]{4, 9, 10, 11, 12, 18, 22, 31, 34, 35, 23, 25, 27, 2, 5, 33, 28, 1, 15, 21, 16};
		assertResult(result, target_idx);

		result = logs.filterWithRU("RU-2").sort(Field.Severity);
		assertResult(result, new int[]{3, 26, 7, 32});

		result = logs.filterWithPRB("PRB1").sort(Field.RU);
		assertResult(result, new int[]{4, 9, 10, 11, 12, 18, 22, 31, 34, 35, 23, 25, 27, 3, 7, 32, 26, 14, 20});
		//displayLogset(result);
		LogContainer cntr = SyslogFileReader.createFromLogSet(result);
		assertResult(cntr, new int[]{4, 9, 10, 11, 12, 18, 22, 31, 34, 35, 23, 25, 27, 3, 7, 32, 26, 14, 20});
		
	}

}
