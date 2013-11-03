package com.roland.syslogviewer.parts;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.SyslogFileReader;

public class ElementLocator {
	@Inject
	static MApplication application;
	@Inject
	static EModelService modelService;

	static Map<String, Object> lookupTable = null;
	static private LogContainer ActiveSyslog = null;

	static final String SEARCH_SEARCH_TOOL_ID = "com.roland.syslogviewer.toolcontrol.search";
	static final String SEARCH_LOGFILE_PART_ID = "com.roland.syslogviewer.part.logfile.";
	static int partIndex = 0;
				
	@PostConstruct
	static void initLocator() {
		System.out.println("ElementLocator::initLocator() is called!");
		initLookupTable();
	}

	private static void initLookupTable() {
		if(lookupTable == null){
			lookupTable = new HashMap<String, Object>();
			List<MToolControl> objs = modelService.findElements(application,
				SEARCH_SEARCH_TOOL_ID, MToolControl.class, null);
			if(objs.size() != 0){
				/*When addon is created, the Model is created, but its  contribute class hasn't created yet. 
				 * Only insert the model class here.*/
				lookupTable.put(SEARCH_SEARCH_TOOL_ID, objs.get(0));
			}else{
				System.out.println("Cannot find " + SEARCH_SEARCH_TOOL_ID);
			}
		}
	}
	
	public static SearchToolItem getSearchTool(){
		Object obj = lookupTable.get(SEARCH_SEARCH_TOOL_ID);
		if(obj != null){
			return (SearchToolItem)((MToolControl)obj).getObject();
		}
		return null;
	}
	
	public static void createLogFilePart(File logFile){		
		List<MWindow> wins = modelService.findElements(application, "com.roland.syslog.mainwindow",
	            MWindow.class, null);
		if(wins.size() != 0){
			MWindow activeWin = wins.get(0);
			MPart part = modelService.createModelElement(MPart.class);
			part.setContributionURI("bundleclass://com.roland.syslogviewer/com.roland.syslogviewer.parts.LogfilePart");
		    EPartService ps = activeWin.getContext().get(EPartService.class);
			part.getTransientData().put(LogfilePart.LOG_FILE_KEY, logFile);
			part.setLabel(logFile.getName());
		    ps.showPart(part, EPartService.PartState.ACTIVATE);
		}
	}

	
	public static LogfilePart getActiveLogfilePart(){
		List<MPart> parts = modelService.findElements(application, "",
	            MPart.class, null);
		if(parts.size() != 0){
			for(MPart part : parts){
				if(part.isVisible() == true){
					return (LogfilePart)part.getObject();
				}
			}
		}else{
			System.out.println("Cannot find MPart!");
		}
		return null;
	}
	
	public static LogfilePart getActiveLogfilePart(MPart part){
		if(part == null){
			return null;
		}
		return (LogfilePart)part.getObject();
	}
	
	public static void registerActiveSysLog(LogContainer logs){
		ActiveSyslog = logs;
	}
	
	public static LogContainer getActiveSysLog(){
		return ActiveSyslog;
	}
	
	public static LogContainer createLogContainer(String fileName){
		if(fileName != null && !fileName.equals("")){
			ActiveSyslog = SyslogFileReader.read(fileName);
			return ActiveSyslog;
		}
		return null;
	}
}