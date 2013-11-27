package com.roland.syslog.model;

import hirondelle.date4j.DateTime;

import java.util.EnumSet;
import java.util.List;

import com.roland.syslog.model.ILogItem.Field;

/**
 * <P>The object implements ILogSet contains a List<ILogItem> which represent a
 * set of log items.</p>
 * <p>
 * The global variable SYSLOG and RESULT are the application use for communicate
 * with the python script. Those two global variables are all based on ILogSet.
 * All the function which declared in this interface can be called in the script.
 * But user shall not call add, addAll, clear and sort function on SYSLOG! This 
 * application assumes that the SYSLOG is read-only. And user cannot add or 
 * remove items from the List that SYSLOG.getLogItemList().</p>
 * <p>
 * Those functions' name start with filterWith return the ILogSet too. So user
 * can call those filter function continuously. For example:</p>
 * <code>
 *   ILogSet result = filterWithSeverity("err").filterWithRU("RU-1", "RU-2");
 *  </code>
 * <p>This example the result contains the those error logs which written on RU-1
 * or RU-2.</p>
 * <p>
 * Those filter function supports non-constant count of parameters. The relation
 * between those parameters are OR. The relation between the two filter routines
 * are AND. Combine with different filter routine can perform complex filter
 * function.</p>
 * 
 * <p>User can use below python code to go through the ILogSet based variable.</p>
 * <code>
 * from com.roland.syslog.model.ILogItem import *
 * for item in SYSLOG.getLogItemList():
 *     print item.getFieldValue(Field.TimeStamp)
 * </code>
 */
public interface ILogSet {
	/** Whether the set is empty.*/
	public boolean isEmpty();

	/** Clear the log set. Never call it in script!*/
	public void clear();

	/**
	 * Add one item needs to be added to current set.
	 */
	public boolean add(ILogItem item);

	public List<ILogItem> getLogItemList();

	/**Bookmark all log items in the LogSet. Not suggest to be used in script.*/
	public void selectAll();

	/**Unbookmark all log items in the LogSet. Not suggest to be used in script.*/
	public void unselectAll();

	/**Get how many items are bookmarked in the set.*/
	public int getSelectCount();

	/**Bookmark given items.*/
	public void setSelected(ILogSet logs);
	
	/**Copy a LogSet's content*/
	public boolean addAll(ILogSet logs);

	/**
	 * The range is [begin ... end], The item's timestamp is equal to begin or
	 * end is selected.
	 */
	public ILogSet betweenTime(DateTime begin, DateTime end);

	/**The DateTime string is directly get from syslog. The format is "Sep 23 13:39:57.000324"*/
	public final static int DATETIME_ORGINAL_FORMAT = 100;
	/**The DateTime string is "MM:DD HH:MM:SS.XXXXXX" (year information isn't needed here.)*/
	public final static int DATETIME_DIGITAL_FORMAT = 101;

	/**
	 * When parameter format == DATETIME_ORGINAL_FORMAT 
	 * <P>The DateTime string is directly get from syslog the format is "Sep 23 13:39:57.000324"</P>
	 * When parameter format == DATETIME_DIGITAL_FORMAT  
	 * <P>The DateTime string is "MM:DD HH:MM:SS.XXXXXX" (year information isn't needed here.)</p>
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
	 * Sort the ILogSet by selected field as first level compare field. If the 
	 * two logs are equal with the given field, then compare them with TimeStamp
	 * field.
	 * It doesn't recommend to sort on LogContainer when is contains million of logs.*/
	public ILogSet sort(Field sortBy);
}
