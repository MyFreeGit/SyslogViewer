 
package com.roland.syslogviewer.handlers;


import com.roland.syslog.model.ILogSet;
import com.roland.syslogviewer.parts.*;
import com.roland.syslogviewer.widgets.SearchResultDialog;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Shell;

public class SearchHandler {
	@Execute
	public void execute(@Optional @Active MPart part, Shell shell) {
		String str="";
		SearchToolItem item = ElementLocator.getSearchTool();
		if(item != null){
			str =item.getText();
		}else{
			System.out.println("Cannot find SearchToolItem");
		}
		if(part != null){
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			ILogSet result = logfilePart.search(str);
			SearchResultDialog dlg = new SearchResultDialog(shell);
			dlg.setResult(result);
			dlg.open();
			ILogSet selection = dlg.getSelection();
			if(!selection.isEmpty()){
				logfilePart.setBookmark(selection);
			}
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part != null);
	}
}