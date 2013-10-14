 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

public class SearchPrevHandler {
	@Execute
	public void execute() {
		//TODO Your code goes here
	}
		
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		//TODO Your code goes here
		return (part != null);
	}
		
}