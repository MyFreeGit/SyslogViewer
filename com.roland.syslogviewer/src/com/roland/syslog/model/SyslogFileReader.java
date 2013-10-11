package com.roland.syslog.model;

import java.io.*;

/*This file is just used for read syslog from a text based file.*/
public class SyslogFileReader {
	public static LogItemsContainer read(String fileName) {
		FileReader file = null;
		LogItemsContainer allItems = new LogItemsContainer();

		try {
			file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			String line = "";
			while ((line = reader.readLine()) != null) {
				if(!line.trim().equals("")){
					LogItem item = new LogItem(line);
					allItems.add(item);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					// Ignore issues during closing
				}
			}
		}
		return allItems;
	}

	private SyslogFileReader(){
		
	}
	
}
