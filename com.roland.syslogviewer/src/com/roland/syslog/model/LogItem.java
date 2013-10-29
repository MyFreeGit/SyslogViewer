package com.roland.syslog.model;
import java.util.*;

import hirondelle.date4j.DateTime;

/**
 * @author DingLi
 * Class is used to record one line of syslog.
 * Notice this class isn't synchronized!
 */
public class LogItem implements ILogItem{

	public LogItem(String plainText){
		this.originalText = plainText;
		fields = EnumSet.noneOf(Field.class);

		this.logText = plainText;
		this.fields.add(Field.Text);

		this.index = counter++;
		initBasicLogItems();
		initIPALogItems();
		
		this.isSelected = false;
	}

	@Override public DateTime getTimeStamp(){
    	return timeStamp;
    }
	
	/**
	 * Return the XX:YY.FFFFFFF part of the DateTime. That can save the display space.*/
	@Override public String getPureTime(){
		String s = timeStamp.toString();
		return s.substring(s.indexOf(' ') + 1);
	}
	
	@Override public Severity getSeverity(){
		return severity;
	}
	
	@Override public String getLogText(){
		return logText;
	}
	
	@Override public String getRU(){
		return ru;
	}
	
	@Override public String getPRB(){
		return prb;
	}
	
	@Override public boolean containField(Field field){
		return fields.contains(field);
	}

	@Override
	public int getIndex() {
		return index;
	}
		
	@Override public String toString(){
		return originalText;
	}
	
	private int index;
	private static int counter = 1;
    private String originalText;
    private DateTime timeStamp;
    private Severity severity;
    private String ru;
    private String prb;
    private EnumSet<Field> fields;
    private String logText; //After all meaningful fields are chopped out, the pure text
    private boolean isSelected;
    
    //This object is addition information for display the log item in different JFace viewer
    //The Model package doesn't care for it. In UT this filed isn't covered. 
    private Object attachedObj = null; 
    
    public final Object getObject() {
		return attachedObj;
	}

	public final void setObject(Object attachedObj) {
		this.attachedObj = attachedObj;
	}

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

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void select() {
		isSelected = true;
		
	}

	@Override
	public void unselect() {
		isSelected = false;
	}

}
