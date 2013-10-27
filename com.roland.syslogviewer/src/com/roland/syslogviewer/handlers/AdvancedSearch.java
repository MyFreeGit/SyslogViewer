 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

public class AdvancedSearch {
	@Execute
	public void execute(@Optional @Active MPart part, Shell shell) {
		//TODO Your code goes here
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part != null);
	}
		
}