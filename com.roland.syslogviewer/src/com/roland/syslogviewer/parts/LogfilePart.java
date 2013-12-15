package com.roland.syslogviewer.parts;


import com.roland.syslog.model.*;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.Composite;

public class LogfilePart {
	private ILogTable logViewer = new LogTableViewer();

	@PostConstruct
	public void createComposite(Composite parent, MPart part) {
		logViewer.initializeTable(parent);
		logViewer.showSyslog();
	}

	@Focus
	public void setFocus(MPart part) {
		logViewer.setFocus();
		ElementLocator.setActiveSyslog(logViewer.getSyslog());
	}
	
	public ILogSet search(String str){
		return logViewer.search(str);
	}

	public void findNext(String str) {
		logViewer.findNext(str);		
	}

	public void findPrev(String str) {
		logViewer.findPrev(str);		
	}
		
	public void setBookmark(ILogSet bookmarks){
		logViewer.setBookmark(bookmarks);
	}
	public void nextBookmark() {
		logViewer.nextBookmark();
	}

	public void prevBookmark() {
		logViewer.prevBookmark();
	}
	
	public void gotoPosition(ILogSet logs){
		logViewer.gotoPosition(logs);
	}
	
	public LogContainer getSyslog(){
		return logViewer.getSyslog();
	}

}
