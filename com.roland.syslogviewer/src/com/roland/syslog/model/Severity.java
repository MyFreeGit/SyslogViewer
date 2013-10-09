package com.roland.syslog.model;

import java.util.HashMap;
import java.util.Map;

public enum Severity{
	emerg(0, "emerg"), alert(1, "alert"), crit(2, "crit"), err(3, "err"), 
	warn(4, "warn"), notice(5, "notice"), info(6, "info"), debug(7, "debug");
	private int value;
	private String name;
	private Severity(int val, String str){
		this.value = val;
		name = str;
	}
	@Override public String toString(){
		return name;
	}
	public int getServity(){
		return value;
	}
	
	public static Severity fromString(String str){
		return lookupTable.get(str.toLowerCase());
	}

	static private Map<String, Severity> lookupTable = new HashMap<String, Severity>();
	static {
		for(Severity item: Severity.values()){
			lookupTable.put(item.name(), item);
		}		
	}
	
}
