package com.roland.syslog.model;


/**
 * Only provide a interface for SyslogFileReader to initialize the LogContainer
 * object.
 */
class SyslogContainer extends LogContainer {
	public boolean addLogItem(ILogItem item){
		return itemList.add(item);		
	}
}
