package com.roland.syslogviewer.parts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.SyslogFileReader;
import com.roland.syslogviewer.remote.RemoteFileDescriptor;
import com.roland.syslogviewer.remote.RemoteSyslog;

public class ElementLocator{
	@Inject
	static MApplication application;
	@Inject
	static EModelService modelService;

	static Map<String, Object> lookupTable = null;
	static private LogContainer ActiveSyslog = null;
	static private PersistedBuffer persistService = null;
	
	static final String SEARCH_SEARCH_TOOL_ID = "com.roland.syslogviewer.toolcontrol.search";
	static final String SEARCH_LOGFILE_PART_ID = "com.roland.syslogviewer.part.logfile.";
	static int partIndex = 0;
	
				
	@PostConstruct
	static void initLocator() {
		initLookupTable();
		if(persistService == null){
			persistService = new PersistedBuffer();
		}
	}

	@PreDestroy
	public static void persistState() {
		persistService.save();
	} 
	
	public static IPersistServiceProvider getPersistService(){
		return persistService;
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
	
	public static void createLogFilePart(){		
		List<MWindow> wins = modelService.findElements(application, "com.roland.syslog.mainwindow",
	            MWindow.class, null);
		if(wins.size() != 0){
			MWindow activeWin = wins.get(0);
			MPart part = modelService.createModelElement(MPart.class);
			part.setContributionURI("bundleclass://com.roland.syslogviewer/com.roland.syslogviewer.parts.LogfilePart");
		    EPartService ps = activeWin.getContext().get(EPartService.class);
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

	public static LogContainer createLogContainer(RemoteFileDescriptor dptr){
		ActiveSyslog = RemoteSyslog.read(dptr);
		return ActiveSyslog;		
	}

	private static class PersistedBuffer implements IPersistServiceProvider{
		static private final int MAX_BUFFER_COUNT = 10;
		static private final String KEY_HISTORY_HEAD = "SearchHistory";
		static private final String PROPERTY_FILE_NAME = "history.property";
		static private final String KEY_SCRIPT_PATH = "ScriptPath";
		private static List<String> history = null;
		private static String scriptPath = null;
		
		public PersistedBuffer(){
			history = new LinkedList<String>();
			Properties prop = new Properties();
			File file = getPropertyFile();
			if(file == null){
				return;
			}
			try{
		   		prop.load(new FileInputStream(PROPERTY_FILE_NAME));
		   		readSearchHistory(prop);
		   		scriptPath = prop.getProperty(KEY_SCRIPT_PATH);
			}catch(IOException ex) {
	    		ex.printStackTrace();
	        }	 
		}
				
		@Override
		public List<String> getSearchHistory() {
			return history;
		}

		@Override
		public void addSearchText(String text) {
			if(!history.contains(text)){
				if(history.size() == MAX_BUFFER_COUNT){
					history.remove(0);
				}
				history.add(text);
			}			
		}
		@Override
		public void setScriptPath(String text) {
			scriptPath = text;
		}

		@Override
		public String getScriptPath() {
			return scriptPath;
		}

		void save() {
			if(history.size() == 0){
				return;
			}
			File file = getPropertyFile();
			if(file == null){
				return;
			}
			Properties prop = new Properties();
			saveSearchHistory(prop);
			if(scriptPath != null){
				prop.setProperty(KEY_SCRIPT_PATH, scriptPath);
			}
			try {
				prop.store(new FileOutputStream(file), null);
			}catch (IOException ex) {
	    		ex.printStackTrace();
	        }

		}

		private void saveSearchHistory(Properties prop) {
			for(int idx = 0; idx < history.size(); idx++){
				String value = history.get(idx);
				String key = getKey(idx);
				prop.setProperty(key, value);
			}
		}

		private void readSearchHistory(Properties prop) {
			for(int i = 0; i < MAX_BUFFER_COUNT; i++){
				String val = prop.getProperty(getKey(i));
				if (val != null){
					history.add(val);
				}else{
					break;
				}
			}
		}

		private File getPropertyFile(){
			File file = new File(PROPERTY_FILE_NAME);
			if(file.exists()){
				return file;				
			}
			try{
				file.createNewFile();
			}catch(IOException ex) {
				System.out.println("Creating property file failed!");
	    		ex.printStackTrace();
	        }	
			return file;
		}
		
		private String getKey(int idx) {
			return KEY_HISTORY_HEAD + String.valueOf(idx);
		}
		
	}
	
}