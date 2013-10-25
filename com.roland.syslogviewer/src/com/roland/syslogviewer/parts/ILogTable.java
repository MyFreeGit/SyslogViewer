package com.roland.syslogviewer.parts;

import org.eclipse.swt.widgets.Composite;

import com.roland.syslog.model.LogContainer;

public interface ILogTable {
	public void initializeTable(Composite parent);
	public void openLogFile(String fileName);
	public LogContainer search(String str);
	public void findNext(String str);
	public void findPrev(String str);
	public void setFocus();
	public void setBookmark(LogContainer bookmarks);
	public void nextBookmark();
	public void prevBookmark();
	public void copySelectionToClipboard();
}
