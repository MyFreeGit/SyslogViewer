package com.roland.syslog.model.utcases;

import hirondelle.date4j.DateTime;

import java.util.EnumSet;

import com.roland.syslog.model.Severity;
import com.roland.syslog.model.ILogItem.Field;

class LogItemData {
	public DateTime timeStamp;
	public Severity severity;
	public String ru;
	public String prb;
	public EnumSet<Field> fields;
	public String logText; // After all meaningful fields are chopped out, the
							// pure text
	public String pureTime;

	public boolean containField(Field field) {
		return fields.contains(field);
	}

}
