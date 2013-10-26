package com.roland.syslog.model;

import java.util.LinkedList;
import java.util.List;

public class ResultLogList extends AbstractLogSet {

	@Override
	protected List<ILogItem> createLogList() {
		return new LinkedList<ILogItem>();
	}

}
