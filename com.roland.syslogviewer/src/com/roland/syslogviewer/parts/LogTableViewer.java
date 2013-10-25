package com.roland.syslogviewer.parts;

import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.roland.syslog.model.ILogItem;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.LogItem;
import com.roland.syslog.model.SyslogFileReader;
import com.roland.syslogviewer.widgets.WidgetsUtil;

public class LogTableViewer implements ILogTable {

	private TableViewer tableViewer = null;
	private LogContainer tblItems;

	ColumnInitializer columnInitializer = new ColumnInitializer();
	static private final Image BOOKMARK = ResourceManager.getPluginImage("com.roland.syslogviewer", "icons/bookmark_obj.gif");

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
	public LogContainer search(String str) {
		return tblItems.findAll(str);
	}


	@Override
	public void findNext(String str) {
		setSelectionToItem(tblItems.findNext(str));
	}

	@Override
	public void findPrev(String str) {
		setSelectionToItem(tblItems.findPrev(str));		
	}

	@Override
	public void setBookmark(LogContainer bookmarks) {
		if(!bookmarks.isEmpty()){
			//tblItems.unselectAll();
			tblItems.setSelected(bookmarks);
			tableViewer.refresh();
		}
		
	}

	@Override
	public void nextBookmark() {
		setSelectionToItem(tblItems.navNext());
	}

	@Override
	public void prevBookmark() {
		setSelectionToItem(tblItems.navPrev());
	}


	@Override
	public void copySelectionToClipboard() {
		int[] idx = tableViewer.getTable().getSelectionIndices();
		WidgetsUtil.copyLogitemsToClipboard(tblItems, idx);
		
	}

	@Override
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	private void setSelectionToItem(ILogItem item) {
		if(item != null){
			int idx[] = new int[1];
			idx[0] = tblItems.getLogItemList().indexOf(item);
			tableViewer.reveal(item);
			tableViewer.getTable().setSelection(idx);
			tableViewer.getTable().setFocus();
		}
	}

	private void showLogfileByTableView(LogContainer logs) {
		assert (tableViewer != null);
		final Table table = tableViewer.getTable();
		tableViewer.setLabelProvider(new LabelProvider());

		columnInitializer.createColumn();

		tableViewer.setContentProvider(new MyLazyContentProvider(tableViewer));
		tableViewer.setUseHashlookup(true);

		ILogItem items[] = logs.getLogItemList().toArray(new ILogItem[0]);
		tableViewer.setInput(items);
		tableViewer.setItemCount(items.length); 
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableViewer.addDoubleClickListener(new DoubleClickLinster());
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
	
	private class DoubleClickLinster implements IDoubleClickListener{
		@Override
		public void doubleClick(DoubleClickEvent event) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			ILogItem item = (ILogItem)selection.getFirstElement();
			if(item != null){
				if(item.isSelected()){
					item.unselect();
				}else{
					item.select();
				}
				setSelectionToItem(item);
			}
		}
		
	}
	
	private class ColumnInitializer {
		public void createColumn() {
			TableViewerColumn col = createTableViewerColumn("", 18);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					return null;
				}
				  @Override
			      public Image getImage(Object element) {
			        if (((ILogItem) element).isSelected()) {
			          return BOOKMARK;
			        } else {
			          return null;
			        }
			      }

			});
			
			col = createTableViewerColumn("", 50);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return String.valueOf(i.getIndex());
				}
			});

			col = createTableViewerColumn(LogItem.Field.TimeStamp.toString(), 105);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					ILogItem i = (ILogItem) element;
					return i.getPureTime();
				}
			});

			col = createTableViewerColumn(LogItem.Field.Severity.toString(), 60);
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

			col = createTableViewerColumn(LogItem.Field.Text.toString(), 1000);
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
