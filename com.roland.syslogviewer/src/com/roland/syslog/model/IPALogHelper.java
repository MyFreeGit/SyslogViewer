package com.roland.syslog.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DingLi
 * IPALogHelper is used to get RU, PRB and LogText from one line of syslog. The analyze is based on regex
 */
public class IPALogHelper {
	public final static String IPA_LOG_PATTERN = "(^[A-Za-z]+\\-[0-9]+)\\s*(\\S+)\\:\\s*(.*)";
	public static boolean analyze(String text){
		matcher = ipaPattern.matcher(text);
		return matcher.find();
	}
	
	public static String getRU(){
		return matcher.group(1);
	}
	
	public static String getPRB(){
		return matcher.group(2);
	}
	
	public static String getLogText(){
		return matcher.group(3);
	}
	
	private static Pattern ipaPattern;
	private static Matcher matcher;

	static {
		new IPALogHelper();
	}

	private IPALogHelper(){
		ipaPattern = Pattern.compile(IPA_LOG_PATTERN);
	}
	
}
