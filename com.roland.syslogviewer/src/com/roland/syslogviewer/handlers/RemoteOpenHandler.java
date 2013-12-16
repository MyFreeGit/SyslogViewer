 
package com.roland.syslogviewer.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslogviewer.parts.ElementLocator;
import com.roland.syslogviewer.remote.RemoteFileDescriptor;
import com.roland.syslogviewer.remote.RemoteSyslog;
import com.roland.syslogviewer.widgets.*;

public class RemoteOpenHandler {
	@Execute
	public void execute(Shell shell) {
		OpenRemoteFileDialog dialog = new OpenRemoteFileDialog(shell);
		if(dialog.open() == IDialogConstants.OK_ID){
			RemoteFileDescriptor dptr = dialog.getActiveDescriptor();
			if(dptr != null){
				ElementLocator.createLogContainer(dptr);
				MPart part = ElementLocator.createLogFilePart(dptr.getName() + "[" + dptr.getHost() + "]");
				part.setDirty(true);
			}
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