package com.roland.syslogviewer.parts;


import com.roland.syslog.model.*;
import com.roland.syslogviewer.handlers.SaveHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

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
	
	@Persist
	public void save(Shell shell, MPart part, MDirtyable dirty) {
		SaveHandler.saveToFile(shell, part);
	}
	
	@PreDestroy
	public void preDestroy(MPart part){
		part.setDirty(false);
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
