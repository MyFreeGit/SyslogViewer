package com.roland.syslog.model.utcases;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.ILogSet;

public class UTHelper {
	public static void assertResult(ILogSet result, int[] target_idx){
		int[] result_idx = new int[target_idx.length];
		int idx = 0;
		for(ILogItem item : result.getLogItemList()){
			result_idx[idx++] = item.getIndex();
		}
		System.out.println(Arrays.toString(result_idx));
		assertArrayEquals(target_idx, result_idx);
	}

	public static void displayLogset(ILogSet logs) {
		for(ILogItem item : logs.getLogItemList()){
			System.out.println(String.valueOf(item.getIndex()) + ": " + item.toString());
		}
	}

}
