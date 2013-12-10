package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import com.roland.syslogviewer.remote.*;

public class OpenRemoteFileDialog extends Dialog {
	private Text txtAccount;
	private Text txtHost;
	private Text txtRemoteFile;
	private Text txtUser;
	private Text txtPassword;
	private Text txtLocalFile;
	
	private Button btnRemoveAccount;
	private Button btnSaveToLocal;
	private Button btnSaveAccount;
	
	private RemoteFileDescriptor activeDescriptor = null;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public OpenRemoteFileDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		
		btnSaveToLocal = new Button(container, SWT.CHECK);
		btnSaveToLocal.setEnabled(false);
		btnSaveToLocal.setBounds(222, 370, 99, 16);
		btnSaveToLocal.setText("Save To Local");
		
		List lstAccount = new List(container, SWT.BORDER);
		lstAccount.setBounds(10, 10, 194, 351);
		
		Button btnAddAccount = new Button(container, SWT.NONE);
		btnAddAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				addNewAccount();
			}
		});
		btnAddAccount.setBounds(10, 367, 194, 23);
		btnAddAccount.setText("Add Account...");
		
		btnRemoveAccount = new Button(container, SWT.NONE);
		btnRemoveAccount.setEnabled(false);
		btnRemoveAccount.setBounds(10, 426, 194, 23);
		btnRemoveAccount.setText("Remove Account");
		
		Label lblAccount = new Label(container, SWT.NONE);
		lblAccount.setBounds(222, 10, 49, 13);
		lblAccount.setText("Account:");
		
		txtAccount = new Text(container, SWT.BORDER);
		txtAccount.setBounds(221, 29, 263, 19);
		
		Label lblProtocol = new Label(container, SWT.NONE);
		lblProtocol.setBounds(222, 71, 49, 13);
		lblProtocol.setText("Protocol:");
		
		Combo cmbProtocol = new Combo(container, SWT.NONE);
		cmbProtocol.setEnabled(false);
		cmbProtocol.setItems(new String[] {"SFTP"});
		cmbProtocol.setBounds(222, 90, 262, 21);
		cmbProtocol.select(0);
		
		Label lblHost = new Label(container, SWT.NONE);
		lblHost.setBounds(222, 134, 49, 13);
		lblHost.setText("Host:");
		
		txtHost = new Text(container, SWT.BORDER);
		txtHost.setBounds(222, 153, 262, 19);
		
		Label lblRemoteFile = new Label(container, SWT.NONE);
		lblRemoteFile.setBounds(222, 195, 72, 13);
		lblRemoteFile.setText("Remote File:");
		
		txtRemoteFile = new Text(container, SWT.BORDER);
		txtRemoteFile.setBounds(221, 214, 263, 19);
		
		Label lblUsername = new Label(container, SWT.NONE);
		lblUsername.setBounds(222, 254, 49, 13);
		lblUsername.setText("Username:");
		
		txtUser = new Text(container, SWT.BORDER);
		txtUser.setBounds(221, 273, 263, 19);
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setBounds(222, 313, 49, 13);
		lblPassword.setText("Password:");
		
		txtPassword = new Text(container, SWT.BORDER);
		txtPassword.setBounds(221, 332, 263, 19);
		
		btnSaveAccount = new Button(container, SWT.NONE);
		btnSaveAccount.setEnabled(false);
		btnSaveAccount.setBounds(10, 396, 194, 23);
		btnSaveAccount.setText("Save Account");
		
		Group group = new Group(container, SWT.NONE);
		group.setBounds(222, 374, 262, 79);
		
		Label lblLocalFile = new Label(group, SWT.NONE);
		lblLocalFile.setBounds(10, 22, 78, 13);
		lblLocalFile.setText("Local File:");
		
		txtLocalFile = new Text(group, SWT.BORDER);
		txtLocalFile.setEnabled(false);
		txtLocalFile.setBounds(10, 41, 242, 19);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Open Remote Syslog");
	}


	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(500, 535);
	}
	
	@Override
	protected void okPressed() {
		super.okPressed();
		System.out.println("OK button pressed");
	}
	
	private void addNewAccount(){
		activeDescriptor = RemoteFileDescriptor.createDefaultDescriptor("BCN");
		activeDescriptor.setHost("10.68.156.142").setRemoteFile("/root/DingLi/MyLog.txt");
		setDescriptorToGUI(activeDescriptor);
	}
	
	public RemoteFileDescriptor getActiveDescriptor(){
		return activeDescriptor;
	}
	
	private void setDescriptorToGUI(RemoteFileDescriptor descriptor){
		txtAccount.setText(descriptor.getName());
		txtHost.setText(descriptor.getHost());
		txtRemoteFile.setText(descriptor.getRemoteFile());
		txtUser.setText(descriptor.getUser());
		txtPassword.setText(descriptor.getPassword());
		btnSaveToLocal.setSelection(descriptor.needSaveToLocal());
		if(descriptor.needSaveToLocal() == true){
			txtLocalFile.setText(descriptor.getLocalFile());
		}
	}
	
	private void setActiveDescriptor(RemoteFileDescriptor descriptor){
		if(descriptor == null){
			btnSaveAccount.setEnabled(false);
			btnRemoveAccount.setEnabled(false);
		}else{
			btnSaveAccount.setEnabled(true);
			btnRemoveAccount.setEnabled(true);			
		}
	}
}
