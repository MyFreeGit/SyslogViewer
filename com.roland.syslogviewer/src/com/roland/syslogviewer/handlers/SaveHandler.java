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

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslogviewer.parts.LogfilePart;

public class SaveHandler {
	@CanExecute
	public boolean canExecute(
			@Named(IServiceConstants.ACTIVE_PART) MDirtyable dirtyable) {
		if (dirtyable == null) {
			return false;
		}
		return dirtyable.isDirty();
	}

	@Execute
	public void execute(
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Active MPart part) {
		saveToFile(shell, part);
	}
	
	static public void saveToFile(Shell shell, MPart part){
		if(part.isDirty()==true){
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			dialog.setFileName(part.getLabel());
			String fileName  = dialog.open();
			if(fileName != null && fileName.isEmpty() != true){
				LogfilePart logfile = (LogfilePart)part.getObject();
				logfile.getSyslog().saveToFile(fileName);
				part.setDirty(false);
			}
		}
	}
}
