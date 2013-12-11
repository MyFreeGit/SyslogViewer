package com.roland.syslogviewer.remote;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UTSerialization {
	private static RemoteFileDescriptorSet dptrSet = RemoteFileDescriptorSet.getInstance();
	@Test
	public void test() {
		final int dptrCount = 9;
		dptrSet.clear();
		String targetNameList[] = new String[dptrCount];
		for(int i = 0; i < dptrCount; i++){
			String name = RemoteFileDescriptor.DEFAULT_NAME + String.valueOf(i);
			RemoteFileDescriptor dptr = RemoteFileDescriptor.createDefaultDescriptor();
			dptrSet.addDescriptor(dptr);
			targetNameList[i] = name;
		}
		dptrSet.serialize();
		dptrSet.clear();
		String[] nameList = dptrSet.getAllDescriptorNames();
		assertTrue(nameList.length == 0);
		dptrSet.deserialize();
		nameList = dptrSet.getAllDescriptorNames();
		assertTrue(nameList.length == dptrCount);
		assertArrayEquals(nameList, targetNameList);
		String name = RemoteFileDescriptorSet.getInstance().generateDescriptorName();
		System.out.println(Arrays.toString(nameList));
		assertTrue(name.equals(RemoteFileDescriptor.DEFAULT_NAME + String.valueOf(dptrCount)));
	}

}
