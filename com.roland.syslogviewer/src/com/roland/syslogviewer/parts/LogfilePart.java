package com.roland.syslogviewer.parts;

import java.util.LinkedList;

import com.roland.syslog.model.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.e4.ui.model.application.ui.basic.*;

public class LogfilePart {
	private MPart boundedPart;
	public final static String LOG_FILE_KEY = "logfile";
	private LogTableViewer logViewer = new LogTableViewer();

	@PostConstruct
	public void createComposite(Composite parent, MPart part) {
		logViewer.initializeTable(parent);
		logViewer.openLogFile(part.getTransientData().get(LOG_FILE_KEY).toString());
		boundedPart = part;
	}

	@Focus
	public void setFocus() {
		logViewer.setFocus();
	}
	
	public void searchString(String str){
		logViewer.search(str);
	}
}
