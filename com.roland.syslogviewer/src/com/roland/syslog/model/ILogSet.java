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

	/**
	 * The range is [begin ... end], The item's timestamp is equal to begin or
	 * end is selected.
	 */
	public ILogSet betweenTime(DateTime begin, DateTime end);

	public final static int DATETIME_ORGINAL_FORMAT = 100;
	public final static int DATETIME_DIGITAL_FORMAT = 101;

	/**
	 * When parameter format == DATETIME_ORGINAL_FORMAT 
	 * <P>The DateTime string is directly get from syslog the format is "Sep 23 13:39:57.000324"</P>
	 * When parameter format == DATETIME_DIGITAL_FORMAT  
	 * <P>The DateTime string is "MM:DD HH:MM:SS.XXXXXX" (year information isn't needed here.</p>
	 * 
	 * @throws IllegalStateException
	 *             when DateTime format isn't correct
	 */
	public ILogSet betweenTime(String begin, String end, int format);

	/**
	 * Filter all LogItems with one of given severities. 
	 * If parameter is null, empty LogSet is returned.*/
	public ILogSet filterWithSeverity(EnumSet<Severity> severities);
	/**
	 * Filter all LogItems with one of given severities.
	 * If parameter is null, empty LogSet is returned.*/
	public ILogSet filterWithSeverity(String ...strings);

	/**
	 * Filter all LogItems writes on one of given RUs.
	 * If parameter is null, empty LogSet is returned.*/
	public ILogSet filterWithRU(String ...RUs);
	
	/**
	 * Filter all LogItems written by one of given PRBs. 
	 * If parameter is null, empty LogSet is returned.*/
	public ILogSet filterWithPRB(String ...PRBs);
	
	/**
	 * Get all LogItems whose log text contains one of given string.
	 * If parameter is null, empty LogSet is returned.*/
	public ILogSet textContainsOneOfStrings(String ...strings);
	
	/**
	 * Get all LogItems whose log text contains all of given string.
	 * If parameter is null, empty LogSet is returned. */
	public ILogSet textContainsAllStrings(String ...strings);
	
	/**
	 * Filter all LogItems the given String based field contains all the strings 
	 * which listed in String[] strings.*/
	public ILogSet filterWithStringAnd(Field field, String[] strings);

	/**
	 * Filter all LogItems the given String based fields contains one string 
	 * which listed in String[] strings.*/
	public ILogSet filterWithStringOr(EnumSet<Field> fields, String[] strings);

}
