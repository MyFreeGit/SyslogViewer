package com.roland.syslogviewer.parts;

import org.eclipse.swt.widgets.Composite;

public interface ILogTable {
	public void initializeTable(Composite parent);
	public void openLogFile(String fileName);
	public void search(String str);
	public void findNext(String str);
	public void findPrev(String str);
	public void setFocus();
}
