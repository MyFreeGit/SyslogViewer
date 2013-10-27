package com.roland.syslogviewer.widgets;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;

public class AdvancedSearchDialog extends Dialog {
	private Text text;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AdvancedSearchDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		
		Group grpBetweenTime = new Group(container, SWT.NONE);
		grpBetweenTime.setText("Between Time");
		grpBetweenTime.setLayout(null);
		GridData gd_grpBetweenTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpBetweenTime.heightHint = 72;
		gd_grpBetweenTime.widthHint = 426;
		grpBetweenTime.setLayoutData(gd_grpBetweenTime);
		
		Button btnCheckButton = new Button(grpBetweenTime, SWT.CHECK);
		btnCheckButton.setBounds(8, 18, 57, 16);
		btnCheckButton.setText("Select");
		
		text = new Text(grpBetweenTime, SWT.BORDER);
		text.setBounds(185, 17, 210, 18);
		
		Spinner spinner = new Spinner(grpBetweenTime, SWT.BORDER);
		spinner.setMaximum(23);
		spinner.setBounds(90, 39, 40, 21);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(681, 365);
	}
}
