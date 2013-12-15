package com.roland.syslog.model;


/**
 * Only provide a interface for SyslogFileReader to initialize the LogContainer
 * object.
 */
class SyslogContainer extends LogContainer {
	public boolean addLogItem(ILogItem item){
		return itemList.add(item);		
	}

	/**Which is used only in show result in a new Part*/
	static public LogContainer createContainerFromSet(ILogSet logs){
		LogContainer result = new SyslogContainer();
		result.itemList.addAll(logs.getLogItemList());
		return result;
	}
	
}
