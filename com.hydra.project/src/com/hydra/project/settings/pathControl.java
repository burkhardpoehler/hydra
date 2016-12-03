/**
 * 
 */
package com.hydra.project.settings;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Poehler
 * Behandelt die Pfadeinstellungen
 *
 */
public class pathControl {

	public static String checkPath(Shell shell, String path, String subject){
		File file = null;
		
//		switch (subject){
//		case "Basisdaten": searchString= "Basisdaten";
//			break;
//		}
		
		
		FileDialog fileDialog = new FileDialog(shell,SWT.OPEN);
		fileDialog.setFilterPath("C:/");
		if (!path.isEmpty()){
			file = new File(path);
			if (file.exists()){
				fileDialog.setFilterPath(path);
			}
		}
		String string1 =  subject + ".DB4O";		
		String[] stringArray1= new String[] {string1};
		fileDialog.setFilterExtensions(stringArray1);
		
		String string2 =  subject + "(" + subject + ".DB4O" + ")";		
		String[] stringArray2= new String[] {string2};		
		fileDialog.setFilterNames(stringArray2);
		fileDialog.setText(subject + ": Pfad einstellen.");

		// We store the selected file name in fileName
		String fileName = null;

		boolean done = false;
		while (!done) {
	    // Open the File Dialog
			fileName = fileDialog.open();

			if(fileName.isEmpty()){
				fileName = "C:/Hydra/" + subject + ".DB4O";
			}
        	file = new File(fileName);
        	if (file.exists()){
        		done = true;
			}else{
	        	//Datei existiert nicht
        		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING
        				|SWT.RETRY |SWT.CANCEL);
        		mb.setMessage("Ungültiger Dateipfad");
        		if (mb.open() == SWT.CANCEL) done = true;
			}
	    }		
		
		return fileName;		
	}
	
}
