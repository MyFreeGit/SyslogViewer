package com.roland.syslogviewer.parts;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.LogContainer;

public class TableItemsContainer extends LogContainer {
	public TableItemsContainer(LogContainer logContainer){
		for(ILogItem item: logContainer.getLogItemList()){
			add(new TableRowItem(item));
		}
	}
}
