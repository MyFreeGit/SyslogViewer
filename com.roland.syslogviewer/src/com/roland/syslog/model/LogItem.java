package com.roland.syslog.model;
import java.util.*;

import hirondelle.date4j.DateTime;

/**
 * @author DingLi
 * Class is used to record one line of syslog.
 */
public class LogItem {
	/**
	 * Used to indicates the log items contains how man fields.
	 */
	public enum Field{
		TimeStamp("TimeStamp"), Severity("Severity"), RU("RU"), PRB("PRB"), Text("Text");
		private String name;
		private Field(String str){
			name = str;
		}
		@Override public String toString(){
			return name;
		}
	}
	public LogItem(String plainText){
		this.originalText = plainText;
		fields = EnumSet.noneOf(Field.class);

		this.logText = plainText;
		this.fields.add(Field.Text);

		initBasicLogItems();
		initIPALogItems();
	}

	public DateTime getTimeStamp(){
    	return timeStamp;
    }
	
	/**
	 * Return the XX:YY.FFFFFFF part of the DateTime. That can save the display space.*/
	public String getPureTime(){
		String s = timeStamp.toString();
		return s.substring(s.indexOf(' ') + 1);
	}
	
	public Severity getSeverity(){
		return severity;
	}
	
	public String getLogText(){
		return logText;
	}
	
	public String getRU(){
		return ru;
	}
	
	public String getPRB(){
		return prb;
	}
	
	public boolean containField(Field field){
		return fields.contains(field);
	}
		
	@Override public String toString(){
		return originalText;
	}
	
    private String originalText;
    private DateTime timeStamp;
    private Severity severity;
    private String ru;
    private String prb;
    private EnumSet<Field> fields;
    private String logText; //After all meaningful fields are chopped out, the pure text   

    private boolean initBasicLogItems(){
    	boolean result = BasicLogHelper.analyze(originalText);
    	if(result){
    		this.timeStamp = BasicLogHelper.getDataTime();
    		this.fields.add(Field.TimeStamp);
    		
    		this.severity = BasicLogHelper.getSeverity();
    		this.fields.add(Field.Severity);
    		
    		this.logText = BasicLogHelper.getLogText();
    	}
    	return result;
    }
    
    private boolean initIPALogItems(){
    	boolean result = IPALogHelper.analyze(logText);
    	if(result){
    		this.ru = IPALogHelper.getRU();
    		this.fields.add(Field.RU);
    		
    		this.prb = IPALogHelper.getPRB();
    		this.fields.add(Field.PRB);
    		
    		this.logText = IPALogHelper.getLogText();
    	}
    	return result;
    }
}
