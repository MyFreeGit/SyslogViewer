package com.roland.syslog.model;

public interface ILogNavigator {

	public void setPosition(ILogItem pos);
	
	public ILogSet findAll(String str);
	
	public ILogItem findNext(String str);
	 
	public ILogItem findPrev(String str);
	
	public ILogItem navNext();
	
	public ILogItem navPrev();

}
