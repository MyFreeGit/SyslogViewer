package com.roland.syslogviewer.parts;

import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.LogItem;
import com.roland.syslog.model.SyslogFileReader;

public class LogTableViewer implements ILogTable {

	private TableViewer tableViewer = null;
	private LogContainer tblItems;
	ColumnInitializer columnInitializer = new ColumnInitializer();

	@Override
	public void initializeTable(Composite parent) {
		if (tableViewer == null) {
			parent.setLayout(new GridLayout());
			tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
			tableViewer.getTable().setLayoutData(
					new GridData(GridData.FILL_BOTH));
		}
	}

	@Override
	public void openLogFile(String fileName) {
		if (!fileName.equals("")) {
			tblItems = SyslogFileReader.read(fileName);
			if (!tblItems.getLogItemList().isEmpty()) {
				showLogfileByTableView(tblItems);
			}
		}
	}

	@Override
	public void search(String str) {
		//LogContainer result = tblItems.findAll(str);
	}


	@Override
	public void findNext(String str) {
		ILogItem result = tblItems.findNext(str);
		if(result != null){
			int idx[] = new int[1];
			idx[0] = tblItems.getLogItemList().indexOf(result);
			tableViewer.reveal(result);
			tableViewer.getTable().setSelection(idx);
			tableViewer.getTable().setFocus();
		}
	}

	@Override
	public void findPrev(String str) {
		ILogItem result = tblItems.findPrev(str);
		if(result != null){
			int idx[] = new int[1];
			idx[0] = tblItems.getLogItemList().indexOf(result);
			tableViewer.reveal(result);
			tableViewer.getTable().setSelection(idx);
			tableViewer.getTable().setFocus();
		}		
	}
	
	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	private void showLogfileByTableView(LogContainer logs) {
		assert (tableViewer != null);
		final Table table = tableViewer.getTable();
		tableViewer.setLabelProvider(new LabelProvider());
		tableViewer.setContentProvider(new MyContentProvider());

		columnInitializer.createColumn();

		tableViewer.setContentProvider(new MyLazyContentProvider(tableViewer));
		tableViewer.setUseHashlookup(true);

		ILogItem items[] = logs.getLogItemList().toArray(new ILogItem[0]);
		tableViewer.setInput(items);
		tableViewer.setItemCount(items.length); 
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

	}

	private class MyContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			return (ILogItem[]) inputElement;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}
	}

	private class MyLazyContentProvider implements ILazyContentProvider  {
		private TableViewer viewer;
		private ILogItem[] items;

		public MyLazyContentProvider(TableViewer viewer){
			this.viewer = viewer;
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			items = (ILogItem[])newInput;
		}

		@Override
		public void updateElement(int index) {
			 viewer.replace(items[index], index);		
		}
	}

	private class ColumnInitializer {
		public void createColumn() {
			TableViewerColumn col = createTableViewerColumn("", 80);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return String.valueOf(i.getIndex());
				}
			});

			col = createTableViewerColumn(LogItem.Field.TimeStamp.toString(),
					100);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getPureTime();
				}
			});

			col = createTableViewerColumn(LogItem.Field.Severity.toString(), 80);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getSeverity().toString();
				}
			});

			col = createTableViewerColumn(LogItem.Field.RU.toString(), 100);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getRU();
				}
			});

			col = createTableViewerColumn(LogItem.Field.PRB.toString(), 120);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getPRB();
				}
			});

			col = createTableViewerColumn(LogItem.Field.Text.toString(), 500);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getLogText();
				}
			});
		}

		private TableViewerColumn createTableViewerColumn(String title,
				int width) {
			final TableViewerColumn viewerColumn = new TableViewerColumn(
					tableViewer, SWT.NONE);
			final TableColumn column = viewerColumn.getColumn();
			column.setText(title);
			column.setWidth(width);
			column.setResizable(true);
			column.setMoveable(true);
			return viewerColumn;

		}
	}

}
