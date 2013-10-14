package com.roland.syslogviewer.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;






import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Event;

public class ElementLocator {
	@Inject
	IEventBroker eventBroker;

	static Map<String, Object> lookupTable = null;
	static final String SEARCH_TOOL_ID = "com.roland.syslogviewer.toolcontrol.search";
	
	@Inject @Optional
	public void partActivation(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE)Event event){
	  // do something
	  System.out.println("Got Part");
	} 
	
	@PostConstruct
	void hookListeners(MApplication application, EModelService modelService) {
		if (application == null)
			System.out.println("application is null!");
		if (modelService == null)
			System.out.println("modelService is null!");
		if(lookupTable == null){
			lookupTable = new HashMap<String, Object>();
			List<MToolControl> objs = modelService.findElements(application,
				SEARCH_TOOL_ID, MToolControl.class, null);
			if(objs.size() != 0 ){
				System.out.println(SEARCH_TOOL_ID + " is added! + size is: " + String.valueOf(objs.size()));
				lookupTable.put(SEARCH_TOOL_ID, objs.get(0).getObject());
			}else{
				System.out.println("Cannot find " + SEARCH_TOOL_ID);
			}
		}
	}
	
	public static SearchToolItem getSearchTool(){
		System.out.println(lookupTable);
		Object obj = lookupTable.get(SEARCH_TOOL_ID);
		if(obj != null){
			return (SearchToolItem)obj;
		}
		return null;
	}
}