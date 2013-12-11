/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.roland.syslogviewer.handlers;

import com.roland.syslogviewer.parts.*;

import java.io.*;
import java.util.*;

import javax.inject.*;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.workbench.modeling.*;

public class OpenHandler {
	private static File logFile;
	
	@Execute
	public void execute(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, 
			EModelService modelService, MApplication application, MWindow activeWin){
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String fileName  = dialog.open();
		if(fileName != null && !fileName.equals("")){
			//createLogfilePart(application, modelService, activeWin);
			ElementLocator.createLogContainer(fileName);
			ElementLocator.createLogFilePart(fileName);
		}
	}
	
	/* Todo: due to opening a big syslog consumes much memory, to simplify the implementation
	 * only allow open the syslog once. User want open another syslog, he shall close it and
	 * open is again.*/
	@CanExecute
	public boolean canExecute(@Optional @Active MPart part) {
		return (part == null);
	}

}
