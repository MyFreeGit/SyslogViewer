 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;


public class HelpHandler {
	@Execute
	public void execute() {
		File helpFile = new File(System.getProperty("user.dir") + File.separator +
				"Helps" + File.separator + "SyslogViewer Help.html");
		try {
			System.out.println("Before open help file");
			Desktop.getDesktop().open(helpFile);
			System.out.println("After open help file");
		} catch (IOException e) {
			System.out.println("Exception Catched!");
			e.printStackTrace();
		}
	}
		
}