package com.roland.syslogviewer.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SearchToolItem {
	private Text text;
	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		text = new Text(comp, SWT.SEARCH | SWT.ICON_SEARCH | SWT.CANCEL
				| SWT.BORDER);
		text.setMessage("");
		GridDataFactory.fillDefaults().hint(130, SWT.DEFAULT).applyTo(text);
	}
	
	public String getText(){
		return text.getText();
	}
}