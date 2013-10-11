 
package com.roland.syslogviewer.handlers;

import com.roland.syslogviewer.parts.*;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

public class SearchHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Optional @Active MPart part) {
		InputDialog dlg = new InputDialog(shell, "Search", "Search What:", "", null);
		dlg.open();
		String str = dlg.getValue();
		if(part != null){
			//TODO: To initial search operation on active part
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			logfilePart.searchString(str);
		}
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		//TODO Your code goes here
		return !(part == null);
	}
		
}