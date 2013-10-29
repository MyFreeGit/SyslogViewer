package com.roland.syslog.model.utcases;

import java.util.Arrays;
import java.util.EnumSet;

import hirondelle.date4j.DateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.Severity;
import com.roland.syslog.model.SyslogFileReader;

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
		
		result = logs.filterWithStringOr(EnumSet.of(ILogItem.Field.Severity),
				                         new String[] {"info", "err"});
		target_idx = new int[]{1, 2, 4, 6, 7, 8, 10, 11, 12, 13, 14, 15, 18,
	            19, 20, 21, 24, 27, 29, 30, 31, 32, 33, 34, 35};
		assertResult(result, target_idx);

		result = logs.filterWithStringAnd(ILogItem.Field.Text,
                new String[] {"Your", "PC", "World"});
		target_idx = new int[]{2, 5};
		assertResult(result, target_idx);

		result = logs.filterWithStringAnd(ILogItem.Field.Text,
                new String[] {"Your", "PC", "World"})
                     .filterWithStringAnd(ILogItem.Field.Severity, new String[] {"crit"});
		target_idx = new int[]{5};
		assertResult(result, target_idx);
	}
	
	private void assertResult(ILogSet result, int[] target_idx){
		int[] result_idx = new int[target_idx.length];
		int idx = 0;
		for(ILogItem item : result.getLogItemList()){
			result_idx[idx++] = item.getIndex();
		}
		System.out.println(Arrays.toString(result_idx));
		assertArrayEquals(target_idx, result_idx);
	}
}
