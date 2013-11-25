package com.roland.syslogviewer.widgets;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.ResultLogList;

public class SyslogListViewer {
	private ListViewer listViewer = null;
	private ILogSet logs;

	
	public SyslogListViewer(Composite container){
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.MULTI | SWT.VIRTUAL);
		listViewer.setContentProvider(new LogListViewerContentProvider());
		listViewer.setUseHashlookup(true);

		listViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ILogItem) element).toString();
			}
		});
	}
	
	public void setInput(ILogSet logs){
		listViewer.setInput(logs);
		this.logs = logs;
	}
	
	public Control getControl(){
		return listViewer.getList();
	}
	
	public void refresh(){
		listViewer.refresh();
	}
	
	public void copySelectionToClipboard(){
		int[] idx = listViewer.getList().getSelectionIndices();
		WidgetsUtil.copyLogitemsToClipboard(logs, idx);
	}
	
	public ILogSet getSelection(){
		ILogSet selection = new ResultLogList();
		int[] idx = listViewer.getList().getSelectionIndices();
		for(int i: idx){
			selection.add(logs.getLogItemList().get(i));
		}
		return selection;
	}
	
	private class LogListViewerContentProvider implements IStructuredContentProvider {
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
			return ((ILogSet) inputElement).getLogItemList().toArray();
		}

	}

}
