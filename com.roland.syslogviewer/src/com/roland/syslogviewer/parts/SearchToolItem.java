package com.roland.syslogviewer.parts;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class SearchToolItem {
	private Combo combo;

	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		combo = new Combo(comp, SWT.DROP_DOWN);
		GridDataFactory.fillDefaults().hint(300, SWT.DEFAULT).applyTo(combo);

		initComboTexts();
	}

	public String getText() {
		String text = combo.getText();
		if(!Arrays.asList(combo.getItems()).contains(text)){
			combo.add(text);		
		}
		System.out.println();
		ElementLocator.getPersistService().addSearchText(text);
		return text;
	}
	
	private void initComboTexts(){
		List<String> history = ElementLocator.getPersistService().getSearchHistory();
		for(String text : history){
			combo.add(text);
		}
	}
}