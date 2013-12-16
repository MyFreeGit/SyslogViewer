package com.roland.syslogviewer.widgets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.roland.syslog.model.ILogSet;
import com.roland.syslog.model.LogContainer;
import com.roland.syslog.model.PythonScriptRunner;
import com.roland.syslogviewer.parts.ElementLocator;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class RunScriptDialog extends Dialog {
	private Text txtScript;
	private Text txtOutput;
	private Button btnRun;
	private Button btnGoto;
	private Button btnBookmark;
	private Button btnNewViewer;
	private SyslogListViewer listViewer;
	public final static int BUTTON_ID_GOTO = 100;
	public final static int BUTTON_ID_NEW_VIEWER = 110;
	public final static int BUTTON_ID_BOOKMARK = 120;
	private final static int BUTTON_ID_EXECUTE_SCRIPT = 101;
	private final static int BUTTON_ID_OPEN_SCRIPT = 102;
	private final static int BUTTON_ID_SAVE_SCRIPT = 103;
	private ILogSet selection = null;
	private LogContainer logs = null;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public RunScriptDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
	}

	public void setLogContainer(LogContainer logs){
		this.logs = logs;
	}
	
	public ILogSet getSelection(){
		return selection;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(container, SWT.VERTICAL);
		sashForm.setLayout(new FillLayout());
	
		Composite child1 = new Composite(sashForm, SWT.NONE);
		child1.setLayout(new FillLayout());
		
		txtScript = new Text(child1, SWT.BORDER | SWT.MULTI);
		txtScript.setText(PythonScriptRunner.getScriptTemplate());
		
		Composite child2 = new Composite(sashForm, SWT.NONE);
		child2.setLayout(new FillLayout());
		
		TabFolder tabFolder = new TabFolder(child2, SWT.NONE);
		
		TabItem tbtmResult = new TabItem(tabFolder, SWT.NONE);
		tbtmResult.setText("Result");
		
		listViewer = new SyslogListViewer(tabFolder);
		tbtmResult.setControl(listViewer.getControl());
		
		TabItem tbtmOutput = new TabItem(tabFolder, SWT.NONE);
		tbtmOutput.setText("Output");
		
		txtOutput = new Text(tabFolder, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		tbtmOutput.setControl(txtOutput);

		sashForm.setWeights(new int[] {40,60});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button btnOpen = createButton(parent, BUTTON_ID_OPEN_SCRIPT, "Open Script...", false);
		btnOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				openPythonScript();
			}
		});
		Button btnSave = createButton(parent, BUTTON_ID_SAVE_SCRIPT, "Save Script...", false);
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				savePythonScript();
			}
		});

		btnRun = createButton(parent, BUTTON_ID_EXECUTE_SCRIPT, "Run", false);
		btnRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				runPythonScript();
			}
		});
		btnNewViewer = createButton(parent, BUTTON_ID_NEW_VIEWER, "New Viewer", false);
		btnNewViewer.setEnabled(false);
		btnNewViewer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = listViewer.getAllListItem();
			}
		});
		btnNewViewer.setText("New Viewer");

		btnGoto = createButton(parent, BUTTON_ID_GOTO, "Goto", false);
		btnGoto.setEnabled(false);
		btnGoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = listViewer.getSelection();
				setReturnCode(BUTTON_ID_GOTO);
				close();
			}
		});

		btnBookmark = createButton(parent, BUTTON_ID_BOOKMARK, "Bookmark", 	false);
		btnBookmark.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selection = listViewer.getSelection();
			}
		});
		btnBookmark.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	private void openPythonScript(){
		FileDialog dialog = new FileDialog(getParentShell(), SWT.OPEN);
		String [] filterNames = new String [] {"Python Script", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.py", "*"};
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath(ElementLocator.getPersistService().getScriptPath());
		
		String fileName = dialog.open();
		if(fileName != null){
			String script = PythonScriptRunner.readScript(fileName);
			txtScript.setText(script);
			File file = new File(fileName);
			ElementLocator.getPersistService().setScriptPath(file.getPath());
		}
	}

	private void savePythonScript(){
		FileDialog dialog = new FileDialog(getParentShell(), SWT.SAVE);
		String [] filterNames = new String [] {"Python Script", "All Files (*)"};
		String [] filterExtensions = new String [] {"*.py", "*"};
		dialog.setFilterNames (filterNames);
		dialog.setFilterExtensions (filterExtensions);
		dialog.setFilterPath(ElementLocator.getPersistService().getScriptPath());

		String fileName = dialog.open();
		FileWriter output = null;
		try {
			output = new FileWriter(fileName);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write(txtScript.getText());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void runPythonScript(){
		String script = txtScript.getText();
		ILogSet result = PythonScriptRunner.runScript(logs, script);
		String output = PythonScriptRunner.getOutput();
		txtOutput.setText(output);

		if(result.isEmpty()){
			btnBookmark.setEnabled(false);
			btnGoto.setEnabled(false);
			btnNewViewer.setEnabled(false);
		}else{
			btnBookmark.setEnabled(true);
			btnGoto.setEnabled(true);
			btnNewViewer.setEnabled(true);
		}
		listViewer.setInput(result);
		listViewer.refresh();
		
	}
	
	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Run a python script");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}

	@Override
	protected void buttonPressed(int buttonId){
		super.buttonPressed(buttonId);
		if(buttonId == BUTTON_ID_OPEN_SCRIPT || buttonId == BUTTON_ID_SAVE_SCRIPT 
				|| buttonId == BUTTON_ID_EXECUTE_SCRIPT){
			return;
		}
		setReturnCode(buttonId);
		close();
	}

}
