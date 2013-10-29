package com.roland.syslog.model;

import hirondelle.date4j.DateTime;

import java.util.EnumSet;
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

	public ILogSet betweenTime(DateTime begin, DateTime end){
		ILogSet result = new ResultLogList();
		for(ILogItem item : itemList){
			if(item.getTimeStamp().lteq(end) && item.getTimeStamp().gteq(begin)){
				result.add(item);
			}
		}
		return result;
	}
	
	public ILogSet betweenTime(String begin, String end, int format){
		ILogSet result = new ResultLogList();
		
		if(format == DATETIME_ORGINAL_FORMAT){
			if(BasicLogHelper.analyzeDateTime(begin)){
				DateTime dt1 = BasicLogHelper.getDataTime();
				DateTime dt2;
				if(BasicLogHelper.analyzeDateTime(end)){
					dt2 = BasicLogHelper.getDataTime();
					return betweenTime(dt1, dt2);
				}
			}
		}
		if(format == DATETIME_DIGITAL_FORMAT){
			DateTime dt1 = new DateTime(BasicLogHelper.DEFAULT_YEAR + "-" + begin);
			DateTime dt2 = new DateTime(BasicLogHelper.DEFAULT_YEAR + "-" + end);
			return betweenTime(dt1, dt2);
		}
		return result;
	}

	public ILogSet filterWithSeverity(EnumSet<Severity> severities){
		ILogSet result = new ResultLogList();
		for(ILogItem item : itemList){
			if(severities.contains(item.getSeverity())){
				result.add(item);
			}
		}
		return result;
	}

}
