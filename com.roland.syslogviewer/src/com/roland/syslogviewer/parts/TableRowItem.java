package com.roland.syslogviewer.parts;
import hirondelle.date4j.DateTime;

import java.util.EnumSet;

import com.roland.syslog.model.*;

public class TableRowItem implements ILogItem{

	public TableRowItem(ILogItem logItem){
		this.logItem = logItem;
	}
	@Override public DateTime getTimeStamp() {
		return logItem.getTimeStamp();
	}

	@Override public String getPureTime() {
		return logItem.getPureTime();
	}

	@Override public Severity getSeverity() {
		return logItem.getSeverity();
	}

	@Override public String getLogText() {
		return logItem.getLogText();
	}

	@Override public String getRU() {
		return logItem.getRU();
	}

	@Override public String getPRB() {
		return logItem.getPRB();
	}

	@Override public boolean containField(Field field) {
		return logItem.containField(field);
	}

	@Override public int getIndex() {
		return logItem.getIndex();
	}
	
	@Override public String toString(){
		return logItem.toString();
	}
	private ILogItem logItem;
}
