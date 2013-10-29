package com.roland.syslog.model;

import hirondelle.date4j.DateTime;

public interface ILogItem {
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
	
	public enum Selection{
		Selected, UnSelected;
	}
	public int getIndex();
	public String getFieldValue(Field field);
	public DateTime getTimeStamp();
	public String getPureTime();
	public Severity getSeverity();
	public String getLogText();
	public String getRU();
	public String getPRB();
	public boolean containField(Field field);
	public boolean isSelected();
	public void select();
	public void unselect();
}
