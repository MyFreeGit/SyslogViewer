package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.LogContainer;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SearchResultDialog extends Dialog {
	private LogContainer result;
	private ListViewer listViewer;
	private LogContainer selection;
	private final static int ID_BTN_CLIPBOARD = 10;

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

		Button btnCopy = createButton(parent, ID_BTN_CLIPBOARD, "Clipboard", false);
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				copySelectionToClipboard();
			}
		});
		btnCopy.setText("Clipboard");

		Button btnSelect = createButton(parent, 0, "Bookmark", false);
		btnSelect.setText("Bookmark");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setSelection();
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

	private void setSelection(){
		int[] idx = listViewer.getList().getSelectionIndices();
		if(selection.isEmpty() != true){
			selection.clear();
		}
		for(int i: idx){
			selection.add(result.getLogItemList().get(i));
		}
	}

	private void copySelectionToClipboard(){
		int[] idx = listViewer.getList().getSelectionIndices();
		if(idx.length != 0){
			Display display = Display.getCurrent();
			Clipboard clipboard = new Clipboard(display);
			StringBuilder sb = new StringBuilder();
			for(int i: idx){
				sb.append(result.getLogItemList().get(i).toString());
				sb.append("\n");
			}
	        TextTransfer textTransfer = TextTransfer.getInstance();
	        Transfer[] transfers = new Transfer[]{textTransfer};
	        Object[] data = new Object[]{sb.toString()};
	        clipboard.setContents(data, transfers);
	        clipboard.dispose();	
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