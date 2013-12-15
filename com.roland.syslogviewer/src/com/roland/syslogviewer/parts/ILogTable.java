package com.roland.syslogviewer.parts;

import org.eclipse.swt.widgets.Composite;

import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.LogContainer;

public interface ILogTable {
	public void initializeTable(Composite parent);
	public void showSyslog();
	public ILogSet search(String str);
	public void findNext(String str);
	public void findPrev(String str);
	public void setFocus();
	public void setBookmark(ILogSet bookmarks);
	public void nextBookmark();
	public void prevBookmark();
	public void gotoPosition(ILogSet logs);
	public LogContainer getSyslog();
}
