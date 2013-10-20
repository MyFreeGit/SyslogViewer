 
package com.roland.syslogviewer.handlers;

import java.util.LinkedList;
import java.util.List;

import com.roland.syslog.model.LogContainer;
import com.roland.syslogviewer.parts.*;
import com.roland.syslogviewer.widgets.SearchResultDialog;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.InputDialog;
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
			LogContainer result = logfilePart.search(str);
			SearchResultDialog dlg = new SearchResultDialog(shell);
			dlg.setResult(result);
			int ret = dlg.open();
			System.out.println("dlg.open() returned " + String.valueOf(ret));
			LogContainer selection = dlg.getSelection();
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