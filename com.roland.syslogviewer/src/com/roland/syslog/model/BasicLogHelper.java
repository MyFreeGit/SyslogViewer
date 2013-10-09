package com.roland.syslog.model;

import hirondelle.date4j.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DingLi
 * This class is used to read the datetime, severity and LogText from one line of syslog. The datetime and severity
 * is every line of log has. The LogText is passed for IPALogHelper to have further check.
 */
public class BasicLogHelper {
	/*Syslog isn't contains year information, so all year information is set to 2012 for simplicity*/
	public final static String defaultYear = "2012"; 
    public final static String BASIC_LOG_PATTERN = "([a-zA-Z]+)\\s*([0-9]+)\\s*([0-9]+:[0-9]+:[0-9]+\\.[0-9]+)\\s*((?i)emerg|alert|crit|err|warn|notice|info|debug)\\s*(.*)";
    
	public static boolean analyze(String text){
		matcher = pattern.matcher(text);
		return matcher.find();
	}

	public static DateTime getDataTime(){
		StringBuilder sb = new StringBuilder(64);
		String month = monthAbbrToNumber(matcher.group(1));
		sb.append("2012-").append(month).append("-");
		if(matcher.group(2).length() == 1){
			sb.append("0");
		}
		sb.append(matcher.group(2)).append(" ").append(matcher.group(3));
		return new DateTime(sb.toString());		
	}
	
	public static Severity getSeverity(){
		return Severity.fromString(matcher.group(4));
	}
	
	public static String getLogText(){
		return matcher.group(5);
	}

    static private Map<String, String> lookupTable;
	static private Pattern pattern;
	static private Matcher matcher;
	
	static {
		lookupTable = new HashMap<String, String>();
		lookupTable.put("JAN", "01");
		lookupTable.put("FEB", "02");
		lookupTable.put("MAR", "03");
		lookupTable.put("APR", "04");
		lookupTable.put("MAY", "05");
		lookupTable.put("JUN", "06");
		lookupTable.put("JUL", "07");
		lookupTable.put("AUG", "08");
		lookupTable.put("SEP", "09");
		lookupTable.put("OCT", "10");
		lookupTable.put("NOV", "11");
		lookupTable.put("DEC", "12");

		pattern = Pattern.compile(BASIC_LOG_PATTERN);
	}
	
	public static final String monthAbbrToNumber(String month){
	    	return lookupTable.get(month.toUpperCase());
	}
	
}
