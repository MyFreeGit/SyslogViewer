 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import com.roland.syslogviewer.parts.LogfilePart;

public class CopyHandler {
	@Execute
	public void execute(@Optional @Active MPart part) {
		if(part != null){
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			logfilePart.copySelectionToClipboard();
		}
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part != null);
	}
		
}