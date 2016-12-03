package com.hydra.project.editors;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.hydra.project.dialogs.MyImageDialog;


/**
 * An cell editor that uses a dialog.
 * Dialog cell editors usually have a label control on the left and a button on
 * the right. Pressing the button opens a dialog window (for example, a color dialog
 * or a file dialog) to change the cell editor's value.
 * The cell editor's value is the value of the dialog.
 * Editor shows Icons in a table.
 * @author Pöhler
 * 
 */
public class MyImageDialogCellEditor extends DialogCellEditor{

	public MyImageDialogCellEditor(Composite parent) {
	        super(parent);
	}
	  
	  
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
	
		// We store the selected file name in fileName
		String fileName = this.getValue().toString();
		MyImageDialog myImageDialog = new MyImageDialog(cellEditorWindow.getShell());
		//Open the File Dialog
		myImageDialog.setValue(fileName);
		myImageDialog.open();
		
		if (myImageDialog.getReturnCode()==0) {
			fileName = myImageDialog.getSelection();
        } 
		return fileName;
	}



}
