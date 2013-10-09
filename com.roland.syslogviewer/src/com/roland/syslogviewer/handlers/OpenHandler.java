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

import java.lang.reflect.InvocationTargetException;
import java.io.*;
import java.util.*;

import javax.inject.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.SWT.*;
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
		if(!fileName.equals("")){
			logFile = new File(fileName);
			createLogfilePart(application, modelService, activeWin);
		}
	}
		
	final static private void createLogfilePart(MApplication application,EModelService modelService, MWindow activeWin){
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setLabel(logFile.getName());
		part.setContributionURI("bundleclass://com.roland.syslogviewer/com.roland.syslogviewer.parts.LogfilePart");
		part.getTransientData().put(LogfilePart.LOG_FILE_KEY, logFile);
		List<MPartStack> stacks = modelService.findElements(application, null,
	            MPartStack.class, null);
	    stacks.get(0).getChildren().add(part);
	    EPartService ps = activeWin.getContext().get(EPartService.class);
	    ps.showPart(part, EPartService.PartState.ACTIVATE);

	}
}
