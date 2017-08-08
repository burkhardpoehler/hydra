package com.hydra.project.command;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.database.DBMitarbeiterTools;
import com.hydra.project.database.DBStundenTools;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;

public class OpenMitarbeiterFileCommand {

	public static void Action(Shell shell, MyTreeItem myTreeItem){
		String thisClass= "OpenMitarbeiterFileCommand";
		FileDialog fileDialog = new FileDialog(shell,SWT.OPEN);
		fileDialog.setFilterPath("C:/");
		fileDialog.setFilterExtensions(new String[] { "*.XLS; *.XLSX" });
		fileDialog.setFilterNames(new String[] { "Mitarbeiter (*.XLS *.XLSX)" });
		fileDialog.setText("Dynamics Exceldatei mit Mitarbeitern öffnen");

		// We store the selected file name in fileName
		String fileName = null;

		// The user has finished when one of the
		// following happens:
		// 1) The user dismisses the dialog by pressing Cancel
		// 2) The user agrees to overwrite existing file
		boolean done = false;
		while (!done) {
	    // Open the File Dialog
			fileName = fileDialog.open();
	        if (fileName == null) {
	        	// User has cancelled, so quit and return
	            done = true;
	        } else {
		        try {
					DBMitarbeiterTools.DatenbankFüllen(fileName);
					done = true;
				} catch (IOException e) {
					LogfileView.log(thisClass,"Exceltabelle konte nicht geöffnet werden",SWT.ICON_ERROR);
					e.printStackTrace();
				}
	        }
	    }		
	}
}
