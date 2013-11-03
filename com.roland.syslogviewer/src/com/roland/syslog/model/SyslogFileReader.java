package com.roland.syslog.model;

import java.io.*;
import java.nio.CharBuffer;

/*This file is just used for read syslog from a text based file.*/
public class SyslogFileReader {
	public static LogContainer read(String fileName) {
		FileReader file = null;
		LogContainer allItems = new LogContainer();

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
			reader.close();
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
