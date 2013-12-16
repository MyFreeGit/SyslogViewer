package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import com.roland.syslogviewer.remote.*;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;

public class OpenRemoteFileDialog extends Dialog {
	private Text txtAccount;
	private Text txtHost;
	private Text txtRemoteFile;
	private Text txtUser;
	private Text txtPassword;
	
	private Button btnRemoveAccount;
	private Button btnSaveAccount;
	
	private ListViewer lvDptr;
	private RemoteFileDescriptor activeDescriptor = null;
	

	public RemoteFileDescriptor getActiveDescriptor(){
		return activeDescriptor;
	}
	
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
		
		Button btnAddAccount = new Button(container, SWT.NONE);
		btnAddAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				addNewAccount();
			}
		});
		btnAddAccount.setBounds(10, 288, 194, 23);
		btnAddAccount.setText("Add Account...");
		
		btnRemoveAccount = new Button(container, SWT.NONE);
		btnRemoveAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				removeAccount();
			}
		});
		btnRemoveAccount.setEnabled(false);
		btnRemoveAccount.setBounds(10, 346, 194, 23);
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
		cmbProtocol.setBounds(222, 92, 262, 21);
		cmbProtocol.select(0);
		
		Label lblHost = new Label(container, SWT.NONE);
		lblHost.setBounds(222, 134, 49, 13);
		lblHost.setText("Host:");
		
		txtHost = new Text(container, SWT.BORDER);
		txtHost.setBounds(222, 156, 262, 19);
		
		Label lblRemoteFile = new Label(container, SWT.NONE);
		lblRemoteFile.setBounds(222, 195, 72, 13);
		lblRemoteFile.setText("Remote File:");
		
		txtRemoteFile = new Text(container, SWT.BORDER);
		txtRemoteFile.setBounds(221, 219, 263, 19);
		
		Label lblUsername = new Label(container, SWT.NONE);
		lblUsername.setBounds(222, 258, 72, 13);
		lblUsername.setText("Username:");
		
		txtUser = new Text(container, SWT.BORDER);
		txtUser.setBounds(221, 282, 263, 19);
		
		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setBounds(222, 322, 72, 13);
		lblPassword.setText("Password:");
		
		txtPassword = new Text(container, SWT.BORDER);
		txtPassword.setBounds(221, 345, 263, 19);
		
		btnSaveAccount = new Button(container, SWT.NONE);
		btnSaveAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				saveAccounts();
			}
		});
		btnSaveAccount.setEnabled(false);
		btnSaveAccount.setBounds(10, 317, 194, 23);
		btnSaveAccount.setText("Save Account");
		
		lvDptr = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		List listCtrl = lvDptr.getList();
		listCtrl.setBounds(10, 6, 194, 276);
		initListViewer(lvDptr);
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
		return new Point(500, 450);
	}
	
	@Override
	protected void okPressed() {
		super.okPressed();
		System.out.println("OK button pressed");
	}
	
	private void saveAccounts(){
		setGUIToActiveDescriptor();
		RemoteFileDescriptorSet.getInstance().serialize();
		lvDptr.refresh();
	}

	private void addNewAccount(){
		activeDescriptor = RemoteFileDescriptor.createDefaultDescriptor();
		RemoteFileDescriptorSet.getInstance().addDescriptor(activeDescriptor);
		setActiveDescriptor(activeDescriptor);
	}
	
	private void removeAccount(){
		if(activeDescriptor != null){
			RemoteFileDescriptorSet.getInstance().removeDescriptor(activeDescriptor);
			setActiveDescriptor(null);
			RemoteFileDescriptorSet.getInstance().serialize();
		}else{
			showErrorMsg("Error", "No Item can be removed!");
		}
	}

	private void setDescriptorToGUI(RemoteFileDescriptor descriptor){
		if(descriptor != null){
			txtAccount.setText(descriptor.getName());
			txtHost.setText(descriptor.getHost());
			txtRemoteFile.setText(descriptor.getRemoteFile());
			txtUser.setText(descriptor.getUser());
			txtPassword.setText(descriptor.getPassword());
		}else{
			txtAccount.setText("");
			txtHost.setText("");
			txtRemoteFile.setText("");
			txtUser.setText("");
			txtPassword.setText("");
		}
		lvDptr.refresh();
	}

	private void showErrorMsg(String title, String msg){
		MessageBox dialog = new MessageBox(getParentShell(), SWT.ICON_ERROR | SWT.OK);
		dialog.setText(title);
		dialog.setMessage(msg);
        dialog.open(); 
		
	}
	private void setGUIToActiveDescriptor(){
		if(activeDescriptor != null){
			activeDescriptor.setName(txtAccount.getText());
			activeDescriptor.setHost(txtHost.getText());
			activeDescriptor.setRemoteFile(txtRemoteFile.getText());
			activeDescriptor.setUser(txtUser.getText());
			activeDescriptor.setPassword(txtPassword.getText());
		}else{
			showErrorMsg("Error", "Can not save the remote syslog information!");
		}
	}

	private void setActiveDescriptor(RemoteFileDescriptor descriptor){
		activeDescriptor = descriptor;
		if(descriptor == null){
			btnSaveAccount.setEnabled(false);
			btnRemoveAccount.setEnabled(false);
			btnSaveAccount.setEnabled(false);
			btnRemoveAccount.setEnabled(false);
		}else{
			btnSaveAccount.setEnabled(true);
			btnRemoveAccount.setEnabled(true);		
			btnSaveAccount.setEnabled(true);
			btnRemoveAccount.setEnabled(true);
		}
		setDescriptorToGUI(descriptor);
	}
	
	private void initListViewer(ListViewer listViewer){
		listViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object inputElement) {
				return ((RemoteFileDescriptorSet)inputElement).toArray();
			}
			@Override
			public void dispose() {			
			}
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}	
		});
		
		listViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((RemoteFileDescriptor)element).getName();
			}
		});
		
		listViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				List listCtrl = lvDptr.getList();
				if(listCtrl.getSelectionCount() > 0){
					RemoteFileDescriptor dptr = (RemoteFileDescriptor)lvDptr.getElementAt(listCtrl.getSelectionIndices()[0]);
					setActiveDescriptor(dptr);
				}
			}
		});
		listViewer.setInput(RemoteFileDescriptorSet.getInstance());
	}
}
