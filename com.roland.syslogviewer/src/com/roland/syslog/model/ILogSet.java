package com.roland.syslog.model;

import java.util.List;

public interface ILogSet {
	public boolean isEmpty();

	public void clear();

	public boolean add(ILogItem item);
	
	public List<ILogItem> getLogItemList();
	
	public void selectAll();
	
	public void unselectAll();
	
	public int getSelectCount();
	
	public void setSelected(ILogSet logs);

}
