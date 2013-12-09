package com.roland.syslog.model;

import java.io.*;
import java.nio.CharBuffer;

/*This file is just used for read syslog from a text based file.*/
public class SyslogFileReader {
	public static LogContainer read(String fileName) {
		LogContainer allItems;
		FileReader file = null;

		try {
			file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			allItems = readFromStream(reader);
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
	
	public static LogContainer readFromStream(BufferedReader reader){
		SyslogContainer allItems = new SyslogContainer();
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				if(!line.trim().equals("")){
					LogItem item = new LogItem(line);
					allItems.addLogItem(item);
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allItems;	
	}
	
	private SyslogFileReader(){
		
	}
	
}
