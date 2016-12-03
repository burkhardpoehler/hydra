/**
 * 
 */
package com.hydra.project.editors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Poehler
 *
 */
public class MyFilterSelectionDialog extends Dialog{
	 
	protected MyFilterSelectionDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	 
	 
	 @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite container = (Composite) super.createDialogArea(parent);
	    Button button1 = new Button(container, SWT.PUSH);
	    button1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
	        false));
	    button1.setText("Parameter einfügen");
	    button1.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println("Pressed");
	      }
	    });

	    return container;
	  }

	  // overriding this methods allows you to set the
	  // title of the custom dialog
	  @Override
	  protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("Selection dialog");
	  }

	  @Override
	  protected Point getInitialSize() {
	    return new Point(450, 300);
	  }
		    
}
