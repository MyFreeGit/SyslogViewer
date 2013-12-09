package com.roland.syslogviewer.remote;

import java.io.Serializable;

/**
 * RemoteFileDescriptor is used for describe a syslog file which located on 
 * remote side. The transfer protocol can be sftp. 
 */
public class RemoteFileDescriptor implements Serializable{
	public static final String DEFAULT_USER = "root";
	public static final String DEFAULT_PASSWORD = "root";
	public static final String DEFAULT_REMOTE_FILE = "/srv/Log/log/syslog";
	public static final String DEFAULT_NAME = "New Acount";
	
	public static RemoteFileDescriptor createDefaultDescriptor(String name){
		return new RemoteFileDescriptor(name, null, DEFAULT_USER, DEFAULT_PASSWORD, DEFAULT_REMOTE_FILE);
	}
	public enum Protocol {
		SFTP("sftp");
		private String name;
		private Protocol(String name){
			this.name = name;
		}
		@Override public String toString(){
			return name;
		}
	}
	
	private String host = null;
	private String user = null;
	private String password = null;
	private String remoteFile = null;
	private Boolean isSaveToLocal = false;
	private String localFile = null;
	private String name = null;
	
	public RemoteFileDescriptor(String name, String host, String user, String password, String remoteFile){
		this.name = name;
		this.host = host;
		this.user = user;
		this.password = password;
		this.remoteFile = remoteFile;
		this.isSaveToLocal = false;
		this.localFile = null;
	}
	
	public RemoteFileDescriptor(String name){
		this.name = name;
	}
	
	public RemoteFileDescriptor setName(String name){
		this.name = name;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public RemoteFileDescriptor setHost(String host){
		this.host = host;
		return this;
	}
	
	public String getHost(){
		return host;
	}
	
	public RemoteFileDescriptor setUser(String user){
		this.user = user;
		return this;
	}
	
	public String getUser(){
		return user;
	}
	
	public RemoteFileDescriptor setPassword(String password){
		this.password = password;
		return this;
	}

	public String getPassword(){
		return password;
	}
	
	public RemoteFileDescriptor setRemoteFile(String remoteFile){
		this.remoteFile = remoteFile;
		return this;
	}

	public String getRemoteFile(){
		return remoteFile;
	}
	
	public RemoteFileDescriptor setSaveToLocal(boolean isSaveToLocal){
		this.isSaveToLocal = isSaveToLocal;
		if(isSaveToLocal == false){
			this.localFile = null;
		}
		return this;
	}
	
	public boolean needSaveToLocal(){
		return isSaveToLocal;
	}
	
	public RemoteFileDescriptor setLocalFile(String localFile){
		this.localFile = localFile;
		return this;
	}
	
	public String getLocalFile(){
		return localFile;
	}
}
