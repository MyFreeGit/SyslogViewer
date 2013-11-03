 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslog.model.ILogSet;
import com.roland.syslogviewer.parts.ElementLocator;
import com.roland.syslogviewer.parts.LogfilePart;
import com.roland.syslogviewer.widgets.RunScriptDialog;

public class RunScriptHandler {
	@Execute
	public void execute(@Optional @Active MPart part, Shell shell) {
		RunScriptDialog dlg = new RunScriptDialog(shell);
		dlg.setLogContainer(ElementLocator.getActiveSysLog());
		dlg.open();
		ILogSet bookmarks = dlg.getSelection();
		if( bookmarks != null && !bookmarks.isEmpty()){
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			logfilePart.setBookmark(bookmarks);
		}
	}
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part != null);
	}
		
}