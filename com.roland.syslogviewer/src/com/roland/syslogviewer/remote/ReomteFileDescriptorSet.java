package com.roland.syslogviewer.remote;

import java.util.List;

/**
 * ReomteFileDescriptorSet represents the Remote Files that saved in the 
 * current configuration.
 */
public class ReomteFileDescriptorSet {
	private static List<RemoteFileDescriptor> list = null;
	private ReomteFileDescriptorSet(){
		
	}
	
	public static String[] getAllDescriptorNames(){
		String []names = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			names[i] = list.get(i).getName();
		}
		return names;
	}

	static{
		
	}
}
