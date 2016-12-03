package com.hydra.project.command;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

public class SaveAsFileCommand {
	public static File file;

	public static void Action(Shell shell, MyTreeItem myTreeItem){
		FileDialog fileDialog = new FileDialog(shell,SWT.OPEN);
		fileDialog.setFilterPath("C:/");
		fileDialog.setFilterExtensions(new String[] { "*.DB4O" });
		fileDialog.setFilterNames(new String[] { "HYDRA Dateien (*.DB4O)" });
		fileDialog.setText("Datei speichern unter Dialog");


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
		        if (!fileName.endsWith(".DB4O")) fileName = fileName+".DB4O";
	            // User has selected a file; see if it already exists
	        	if (TreeTools.isProjektVorhanden(fileName)){
	        	//Datei bereits geladen
	        		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING
	        				|SWT.RETRY |SWT.CANCEL);
	        		mb.setMessage("Datei " + fileName + " existiert bereits.");
	        		if (mb.open() == SWT.CANCEL) done = true;

	            }else{
	            	//Datei noch nicht vorhanden
	            	file = new File(fileName);								//neue Datei anlegen
	    			TreeTools.CopyMyTreeItemToFile(myTreeItem,file);		//Datei füllen
	    			TreeTools.addNewFile(file.toString());					//Projekt laden
	            	
	    			TreeTools.removeFile(myTreeItem);						//altes Projekt entfernen
	    			File file = new File(myTreeItem.getDatenbankName());
	    			if (file.exists()){
	    				file.delete();
	    				System.out.println("lösche Datei: " + myTreeItem.getDatenbankName());
	    			}
	    			break;
	            }
	        }
	    }		
	}
}
