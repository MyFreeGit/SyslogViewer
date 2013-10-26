package com.roland.syslog.model;

import java.util.List;

public abstract class AbstractLogSet implements ILogSet {
	
	protected List<ILogItem> itemList = null;
	
	public AbstractLogSet(){
		itemList = createLogList();
	}
 
	/*The subclass must overrides createLogList() to provides the itemList's implementation.
	 *It is useful for adjust the different collection implementation to improve performance.*/
	abstract protected List<ILogItem> createLogList();

	@Override public boolean isEmpty(){
		return !(itemList.size() > 0);
	}
	
	@Override public void clear(){
		itemList.clear();
	}
	
	@Override public boolean add(ILogItem item){
		return itemList.add(item);
	}
	
	@Override public List<ILogItem> getLogItemList(){
		return itemList;
	}

	@Override public void selectAll(){
		for(ILogItem item : itemList){
			item.select();
		}
	}
	
	@Override public void unselectAll(){
		for(ILogItem item : itemList){
			item.unselect();
		}	
	}
	
	@Override public int getSelectCount(){
		int count = 0;
		for(ILogItem item : itemList){
			if(item.isSelected()){
				count++; 
			}
		}
		return count;
	}
	
	@Override public void setSelected(ILogSet logs){
		for(ILogItem item: logs.getLogItemList()){
			item.select();
		}
	}	
	

}
