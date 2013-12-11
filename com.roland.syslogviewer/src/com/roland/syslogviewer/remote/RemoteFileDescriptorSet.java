package com.roland.syslogviewer.remote;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ReomteFileDescriptorSet represents the Remote Files that saved in the 
 * current configuration.
 */
public class RemoteFileDescriptorSet {
	private List<RemoteFileDescriptor> list = null;
	private static RemoteFileDescriptorSet instance = null;
	static private final String PROPERTY_FILE_NAME = "remote.property";

	private RemoteFileDescriptorSet(){
		list = new LinkedList<RemoteFileDescriptor>();
	}
	
	public void addDescriptor(RemoteFileDescriptor descriptor){
		list.add(descriptor);
	}
	
	public void removeDescriptor(RemoteFileDescriptor descriptor){
		list.remove(descriptor);
	}
	
	public void clear(){
		list.clear();
	}
	
	public Object[] toArray(){
		return list.toArray();
	}
	
	public static RemoteFileDescriptorSet getInstance(){
		if(instance == null){
			instance = new RemoteFileDescriptorSet();
			instance.deserialize();
		}
		return instance;
	}

	public String generateDescriptorName(){
		List<String> names = Arrays.asList(getAllDescriptorNames());
		for(int i = 0; i < list.size() + 1; i++){
			StringBuffer sb = new StringBuffer(RemoteFileDescriptor.DEFAULT_NAME);
			sb.append(i);
			if(!names.contains(sb.toString())){
				return sb.toString();
			}
		}
		return RemoteFileDescriptor.DEFAULT_NAME + String.valueOf(System.currentTimeMillis());
	}

	public String[] getAllDescriptorNames(){
		String []names = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			names[i] = list.get(i).getName();
		}
		return names;
	}

	private File getPropertyFile(){
		File file = new File(PROPERTY_FILE_NAME);
		if(file.exists()){
			return file;				
		}
		try{
			file.createNewFile();
		}catch(IOException ex) {
			System.out.println("Creating property file failed!");
    		ex.printStackTrace();
        }	
		return file;
	}

	public void deserialize() {
		File file = getPropertyFile();
		if(file.length() == 0){
			return;
		}
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			list = (List<RemoteFileDescriptor>) in.readObject();
			in.close();
			fileIn.close();
		} catch (EOFException e){
			return;
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("List<RemoteFileDescriptor> not found");
			c.printStackTrace();
			return;
		}
	}

	public void serialize() {
		File file = getPropertyFile();
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(list);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		}
	}
}
