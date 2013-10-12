package com.roland.syslog.model;

import java.util.*;

public class LogContainer {
	public LogContainer(){
		itemList = new LinkedList<ILogItem>();
	}
	
	public boolean add(ILogItem item){
		return itemList.add(item);
	}
	
	public List<ILogItem> getLogItemList(){
		return itemList;
	}

	private List<ILogItem> itemList;
	
	public ILogItem findFirst(String str){
		for(ILogItem item : itemList){
			if(item.toString().contains(str)){
				return item;
			}
		}
		return null;
	}
	
	public LogContainer findAll(String str){
		LogContainer result = new LogContainer();
		for(ILogItem item : itemList){
			if(item.toString().contains(str)){
				result.add(item);
			}
		}
		return result;
	}
}
