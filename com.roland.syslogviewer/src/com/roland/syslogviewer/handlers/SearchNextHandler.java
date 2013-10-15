 
package com.roland.syslogviewer.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslogviewer.parts.ElementLocator;
import com.roland.syslogviewer.parts.LogfilePart;
import com.roland.syslogviewer.parts.SearchToolItem;

public class SearchNextHandler {
	@Execute
	public void execute(@Optional @Active MPart part) {
		String str="";
		SearchToolItem item = ElementLocator.getSearchTool();
		if(item != null){
			str =item.getText();
		}
		if(part != null){
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			logfilePart.findNext(str);
		}	
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		//TODO Your code goes here
		return (part != null);
	}
		
}