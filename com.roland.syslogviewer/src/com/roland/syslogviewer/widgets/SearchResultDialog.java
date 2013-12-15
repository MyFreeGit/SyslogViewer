package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.ResultLogList;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SearchResultDialog extends Dialog {
	private ILogSet result;
	private SyslogListViewer listViewer;
	private ILogSet selection;
	public final static int ID_BTN_NEW_VIEWER = 1000;
	public final static int ID_BTN_GOTO = 1001;

	public void setResult(ILogSet result) {
		this.result = result;
	}

	public SearchResultDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
		selection = new ResultLogList();
	}
	
	public ILogSet getSelection(){
		return selection;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		listViewer = new SyslogListViewer(container);
		listViewer.setInput(result);
		return container;
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button btnGoto = createButton(parent, ID_BTN_GOTO, "Go To", false);
		btnGoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = listViewer.getSelection();
				setReturnCode(ID_BTN_GOTO);
				close();
			}
		});
		btnGoto.setText("Go To");

		Button btnCopy = createButton(parent, ID_BTN_NEW_VIEWER, "New Viewer", false);
		btnCopy.setText("New Viewer");

		Button btnSelect = createButton(parent, 0, "Bookmark", false);
		btnSelect.setText("Bookmark");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = listViewer.getSelection();
			}
		});

		Button btnSelectAll = createButton(parent, IDialogConstants.OK_ID,
				"Bookmark All", true);
		btnSelectAll.setText("Bookmark All");
		btnSelectAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = result;
			}
		});
		
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 480);
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Find Result");
	}
	
	@Override
	protected void buttonPressed(int buttonId){
		super.buttonPressed(buttonId);
		setReturnCode(buttonId);
		close();
	}
	
}