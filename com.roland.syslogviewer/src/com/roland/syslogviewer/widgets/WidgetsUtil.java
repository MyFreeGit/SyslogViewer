package com.roland.syslogviewer.widgets;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import com.roland.syslog.model.ILogSet;

public class WidgetsUtil {
	public static void copyLogitemsToClipboard(ILogSet logs, int[] idx){
		if(idx.length != 0){
			Display display = Display.getCurrent();
			Clipboard clipboard = new Clipboard(display);
			StringBuilder sb = new StringBuilder();
			for(int i: idx){
				sb.append(logs.getLogItemList().get(i).toString());
				sb.append("\n");
			}
	        TextTransfer textTransfer = TextTransfer.getInstance();
	        Transfer[] transfers = new Transfer[]{textTransfer};
	        Object[] data = new Object[]{sb.toString()};
	        clipboard.setContents(data, transfers);
	        clipboard.dispose();	
		}
	}
}
