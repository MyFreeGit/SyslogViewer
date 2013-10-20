package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.LogContainer;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SearchResultDialog extends Dialog {
	private LogContainer result;
	private ListViewer listViewer;
	private LogContainer selection;

	public void setResult(LogContainer result) {
		this.result = result;
	}

	public SearchResultDialog(Shell parentShell) {
		super(parentShell);
		selection = new LogContainer();
	}
	
	public LogContainer getSelection(){
		return selection;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL
				| SWT.MULTI | SWT.VIRTUAL);
		listViewer.setContentProvider(new MyContentProvider());
		listViewer.setUseHashlookup(true);

		listViewer.setInput(result);
		listViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ILogItem) element).toString();
			}
		});
		return container;
	}

	@Override
	protected int getShellStyle() {
		return super.getShellStyle() | SWT.RESIZE;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button btnGoto = createButton(parent, 0, "Go To", false);
		btnGoto.setText("Go To");

		Button btnCopy = createButton(parent, 0, "Copy", false);
		btnCopy.setText("Copy");

		Button btnSelect = createButton(parent, 0, "Bookmark", false);
		btnSelect.setText("Bookmark");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setSelect();
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

	private void setSelect(){
		int[] idx = listViewer.getList().getSelectionIndices();
		if(selection.isEmpty() != true){
			selection.clear();
		}
		for(int i: idx){
			selection.add(result.getLogItemList().get(i));
		}
	}

	private class MyContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((LogContainer) inputElement).getLogItemList().toArray();
		}
	}

}