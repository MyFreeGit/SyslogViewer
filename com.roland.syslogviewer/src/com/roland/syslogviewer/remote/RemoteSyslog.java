package com.roland.syslogviewer.remote;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.SyslogFileReader;


public class RemoteSyslog {
	
	public static LogContainer read(RemoteFileDescriptor dptr){
		LogContainer syslog = null;
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(dptr.getUser(), dptr.getHost(), dptr.getPort());
			session.setPassword(dptr.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Crating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");
			InputStream out = null;
			out = sftpChannel.get(dptr.getRemoteFile());
			BufferedReader br = new BufferedReader(new InputStreamReader(out));
			syslog = SyslogFileReader.readFromStream(br);
			sftpChannel.disconnect();
			session.disconnect();
		} catch (JSchException | SftpException e) {
			System.out.println(e);
		} 
		return syslog;
	}

	public static void main(String[] args) {
		RemoteFileDescriptor dptr = RemoteFileDescriptor.createDefaultDescriptor();
		dptr.setHost("10.68.156.142").setRemoteFile("/root/DingLi/MyLog.txt");
		//System.out.printf("user=[%s], password=[%s]\n",dptr.getUser());
		LogContainer logs = read(dptr);
		if(logs == null){
			System.out.println("logs open failed!");
		}
	}
}
