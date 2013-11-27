package com.roland.syslog.model;

import java.util.Comparator;

import hirondelle.date4j.DateTime;

/**
 * Present each line of log in syslog.
 *
 */
public interface ILogItem {
	/**
	 * Used to indicates the log items contains how many fields.
	 */
	public static enum Field{
		TimeStamp("TimeStamp"), Severity("Severity"), RU("RU"), PRB("PRB"), Text("Text");
		private String name;
		private Field(String str){
			name = str;
		}
		@Override public String toString(){
			return name;
		}
	}
	
	static class LogComparator implements Comparator<ILogItem>{
		public LogComparator(Field firstCompareField){
			firstCmpField = firstCompareField;
		}
		public LogComparator(){
			firstCmpField = Field.TimeStamp;
		}
		
		@Override
		public int compare(ILogItem arg0, ILogItem arg1) {
			int result = 0;
			if(firstCmpField == Field.Severity){
				result = arg0.getSeverity().compareTo(arg1.getSeverity());
			}else if(firstCmpField != Field.TimeStamp){
				result = arg0.getFieldValue(firstCmpField).compareTo(
						           arg1.getFieldValue(firstCmpField));
			}
			if(result != 0){
				return result;
			}else{
				return arg0.getTimeStamp().compareTo(arg1.getTimeStamp());
			}
		}
		
		private Field firstCmpField;

	}
	public enum Selection{
		Selected, UnSelected;
	}
	/**The index is start from 1!*/
	public int getIndex();
	/**Get give filed's value.*/
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
