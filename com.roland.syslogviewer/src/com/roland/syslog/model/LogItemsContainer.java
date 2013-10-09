package com.roland.syslog.model;

import java.util.*;

public class LogItemsContainer {
	public LogItemsContainer(){
		itemList = new LinkedList<LogItem>();
	}
	
	public boolean add(LogItem item){
		return itemList.add(item);
	}
	
	public List<LogItem> getLogItemList(){
		return itemList;
	}

	private List<LogItem> itemList;
}
