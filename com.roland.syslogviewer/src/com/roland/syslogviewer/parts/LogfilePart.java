package com.roland.syslogviewer.parts;

import java.util.LinkedList;

import com.roland.syslog.model.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.e4.ui.model.application.ui.basic.*;

public class LogfilePart {
	private TableViewer tableViewer;
	private MPart boundedPart;
	public final static String LOG_FILE_KEY = "logfile";

	@PostConstruct
	public void createComposite(Composite parent, MPart part) {
		parent.setLayout(new GridLayout());
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		readLogfile(part.getTransientData().get(LOG_FILE_KEY).toString());
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		boundedPart = part;
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}
	
	public void searchString(String str){
		System.out.println(boundedPart.getLabel() + "\tUser want to search :" + str);
	}
	
	private void readLogfile(String fileName) {
		if (!fileName.equals("")) {
			LogContainer logs = SyslogFileReader.read(fileName);
			if (!logs.getLogItemList().isEmpty()) {
				showLogfileByTableView(logs);
			}
		}
	}

	private void showLogfileByTableView(LogContainer logs) {
		final Table table = tableViewer.getTable();
		tableViewer.setLabelProvider(new LabelProvider());
		tableViewer.setContentProvider(new MyContentProvider());

		TableViewerColumn col = createTableViewerColumn("", 80);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        count++;
		        return String.valueOf(count);
		      }
		      private int count = 0;
		});

		col = createTableViewerColumn(LogItem.Field.TimeStamp.toString(), 100);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        LogItem i = (LogItem) element;
		        return i.getPureTime();
		      }
		});

		col = createTableViewerColumn(LogItem.Field.Severity.toString(), 80);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        LogItem i = (LogItem) element;
		        return i.getSeverity().toString();
		      }
		});
		
		col = createTableViewerColumn(LogItem.Field.RU.toString(), 100);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        LogItem i = (LogItem) element;
		        return i.getRU();
		      }
		});

		col = createTableViewerColumn(LogItem.Field.PRB.toString(), 120);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        LogItem i = (LogItem) element;
		        return i.getPRB();
		      }
		});
		
		col = createTableViewerColumn(LogItem.Field.Text.toString(), 500);
		col.setLabelProvider(new ColumnLabelProvider() {
		      @Override public String getText(Object element) {
		        LogItem i = (LogItem) element;
		        return i.getLogText();
		      }
		});
		
		LogItem items[] = logs.getLogItemList().toArray(new LogItem[0]);
		tableViewer.setInput(items);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

	}

	private TableViewerColumn createTableViewerColumn(String title, int width) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}
	
	private class MyContentProvider implements IStructuredContentProvider {

		@Override public Object[] getElements(Object inputElement) {
			return (LogItem[])inputElement;
		}

		@Override public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

	}
}
