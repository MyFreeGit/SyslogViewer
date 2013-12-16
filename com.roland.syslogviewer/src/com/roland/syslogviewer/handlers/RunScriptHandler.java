 
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
		if(part == null){	
			return;
		}
		RunScriptDialog dlg = new RunScriptDialog(shell);
		dlg.setLogContainer(ElementLocator.getActiveSysLog());
		int result = dlg.open();
		ILogSet selection = dlg.getSelection();
		if( selection != null && !selection.isEmpty()){
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			if(result == RunScriptDialog.BUTTON_ID_GOTO){
				logfilePart.gotoPosition(selection);
			}else if(result == RunScriptDialog.BUTTON_ID_BOOKMARK){
				logfilePart.setBookmark(selection);
			}else if(result == RunScriptDialog.BUTTON_ID_NEW_VIEWER){
				createLogfilePartViaLogSet(selection);
			}
		}
	}

	private void createLogfilePartViaLogSet(ILogSet logs){
		if(logs.isEmpty()){
			return;
		}
		ElementLocator.createLogContainer(logs);
		MPart part = ElementLocator.createLogFilePart("Result of script");
		part.setDirty(true);
		part.setCloseable(true);
	}

	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part != null);
	}
		
}