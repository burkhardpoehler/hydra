package com.hydra.project.dialogs;

import java.util.List;

import lifecycle.MyImageHandler;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class MyImageDialog extends Dialog{
	private TableViewer viewer1;
	private static String string = "";
	
	public MyImageDialog(Shell parentShell) {
	    super(parentShell);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite container = (Composite) super.createDialogArea(parent);

    	
	    TableViewer viewer1 = getViewer(container);
	    List imageList = MyImageHandler.getImageList();
	    viewer1.setInput(imageList);
	    viewer1.setSelection(new StructuredSelection(string),true);
	    return container;
	  }
	    
	  /**
		 * Sets the given value to the editor
		 * @param value
		 *            the value to set
		 * @author Poehler
		 */
	  public void setValue(String value){
		  string = value;
	  }
	   
	  /**
		 * Gets the selected value
		 * @return the string
		 *            
		 * @author Poehler           
		 */
	  public String getSelection(){
		  return string;
	  }
	  
	  // overriding this methods allows you to set the
	  // title of the custom dialog
	  @Override
	  protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("Dialog: Icon auswählen.");
	  }

	  @Override
	  protected Point getInitialSize() {
	    return new Point(400, 800);
	  }
	  
	  private static TableViewer getViewer(Composite container) {
	        final TableViewer viewer = new TableViewer(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);

	        viewer.setContentProvider(ArrayContentProvider.getInstance());
	        viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

	        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
					string = (String) sel.getFirstElement();
				}
			});
	        
	        TableViewerColumn col1 = new TableViewerColumn(viewer, SWT.NONE);
	        col1.getColumn().setWidth(300);
	        col1.getColumn().setText("Iconname alt :  " + string);
	        col1.setLabelProvider(new StyledCellLabelProvider() {
	            @Override
	            public void update(ViewerCell cell) {
	            	cell.setImage(MyImageHandler.getImage((String) cell.getElement()));
	                cell.setText((String) cell.getElement());
	            }
	        });
	        viewer.getTable().setHeaderVisible(true);
	        return viewer;
	    }
	  
}
