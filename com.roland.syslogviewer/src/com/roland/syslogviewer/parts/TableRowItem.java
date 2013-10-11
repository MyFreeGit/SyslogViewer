package com.roland.syslogviewer.parts;
import java.util.EnumSet;

import com.roland.syslog.model.*;
import com.roland.syslog.model.LogItem.Field;

public class TableRowItem {
	enum Flag{
		BookMarked, HighLighted;
	}
	private int number;
	private LogItem item;
	private EnumSet<Flag> flags;
	
}
