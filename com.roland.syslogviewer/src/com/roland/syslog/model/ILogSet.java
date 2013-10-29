package com.roland.syslog.model;

import hirondelle.date4j.DateTime;

import java.util.EnumSet;
import java.util.List;

import com.roland.syslog.model.ILogItem.Field;

public interface ILogSet {
	public boolean isEmpty();

	public void clear();

	public boolean add(ILogItem item);
	
	public List<ILogItem> getLogItemList();
	
	public void selectAll();
	
	public void unselectAll();
	
	public int getSelectCount();
	
	public void setSelected(ILogSet logs);
	
	/*The range is [begin ... end], The item's timestamp is equal to begin or end is selected.*/
	public ILogSet betweenTime(DateTime begin, DateTime end);
	
	/*The DateTime string is directly get from syslog the format is "Sep 23 13:39:57.000324"*/
	public final static int DATETIME_ORGINAL_FORMAT = 100;
	/*The DateTime string is "MM:DD HH:MM:SS.XXXXXX" the year information isn't needed here.*/
	public final static int DATETIME_DIGITAL_FORMAT = 101;
	public ILogSet betweenTime(String begin, String end, int format);
	
	public ILogSet filterWithSeverity(EnumSet<Severity> severities);
}
