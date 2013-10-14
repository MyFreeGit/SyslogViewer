 
package com.roland.syslogviewer.handlers;

import java.util.List;

import com.roland.syslogviewer.parts.*;

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
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			MApplication application, EModelService modelService, MWindow activeWin,
			@Optional @Active MPart part, 
			@Optional @Active MToolControl item) {
		List<MToolBar> objs = modelService.findElements(activeWin,
				"", MToolBar.class, null);
		if(objs.size() == 0){
			System.out.println("objs.size() = 0!");
		}else{
			System.out.println(objs.get(0));
		}
		String str="";
		//SearchToolItem item = ElementLocator.getSearchTool();
		if(item != null){
			str =((SearchToolItem)item.getObject()).getText();
		}else{
			System.out.println("Cannot find SearchToolItem");
		}
		System.out.println("Search String is : " + str);
		if(part != null){
			//TODO: To initial search operation on active part
			LogfilePart logfilePart = (LogfilePart)part.getObject();
			logfilePart.searchString(str);
		}
	}
	
	
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		//TODO Your code goes here
		return (part != null);
	}
}