/**
 * 
 */
package com.hydra.project.settings;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.database.DBUserSettings;
import com.hydra.project.model.MyUserSettings;
import com.hydra.project.parts.LogfileView;

/**
 * @author Poehler
 * Behandelt die Pfadeinstellungen
 *
 */
public class pathControl {
	private static String thisClass= "pathControl";
	
	  /**
	 * Überprüft, ob die Dateien in der MyUserSettings Dateiliste vorhanden ist.
	 * Nicht vorhandene Datenbanken werden entfernt.
	 * @author Poehler
	 */
	public static void checkFiles(){
		MyUserSettings mySettings = DBUserSettings.readSettingsDB();
		Boolean flag = false;
		for (int i = 0; i < mySettings.getProjects().size(); i++) {
			File file = null;
			if (!mySettings.getProjects().get(i).isEmpty()){
				file = new File(mySettings.getProjects().get(i));
				if (!file.exists()){
					LogfileView.log(thisClass,"Datenbank nicht mehr vorhanden: " + file.getAbsolutePath() ,SWT.ICON_ERROR);
					mySettings.getProjects().set(i, "empty");
					flag = true;
				}else{
					LogfileView.log(thisClass,"Lade Datenbank: " + file.getAbsolutePath() ,SWT.ICON_INFORMATION);
				}
			}
		}
		if (flag){
			mySettings.clearProjectList();
			DBUserSettings.writeSettingsDB(mySettings);				//sichern
		}
		
	}
	
	  /**
	 * Überprüft, ob die Datei vorhanden ist.
     * @author Poehler
     * @param path der aktuelle Pfad
     * @param subject der Dateiname ohne Endung
     * @return flag der ausgewählte Pfad
     */
	public static Boolean checkFile(String path, String subject){
		File file = null;
		Boolean flag = false;
		if (!path.isEmpty()){
			file = new File(path);
			if (file.exists()){
				if (file.isFile()){
					if (file.getName() == subject + ".DB4O"){
						flag = true;
					}
				}
			}
		}
		return flag;	
	}
	
	  /**
	   * Erzeugt einen Abfragedialog für Dateien
     * @author Poehler
     * @param shell die aktuelle Shell
     * @param path der aktuelle Pfad
     * @param subject der Dateiname ohne Endung
     * @param defaultPath der Standardpfad
     * @param quittAllowed Rückgabe ohne Pfad
     * @return fileName der ausgewählte Pfad
     *
     */
	public static String selectPath(Shell shell, String path, String subject, String defaultPath, Boolean quittAllowed){
		File file = null;

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
		String fileName = defaultPath + subject + ".DB4O";

		boolean done = false;
		while (!done) {
	    // Open the File Dialog

			fileName = fileDialog.open();
			
			if (fileName == null){
				if (quittAllowed == true){
					fileName = "";
					if (!path.isEmpty()){
						file = new File(path);
						if (file.exists()){
							fileName = path;
						}
					}				
					done = true;
				}else{
					fileName = defaultPath + subject + ".DB4O";
					done = true;
				}

			}
			if (fileName.isEmpty()){
				if (quittAllowed == true){
					fileName = "";
					if (!path.isEmpty()){
						file = new File(path);
						if (file.exists()){
							fileName = path;
						}
					}				
					done = true;
				}else{
					fileName = defaultPath + subject + ".DB4O";
					done = true;
				}

			}
//			if(fileName.isEmpty()){
//				fileName = defaultPath + subject + ".DB4O";
//			}
        	file = new File(fileName);
        	if (file.exists()){
        		done = true;
			}
        	
//	        	//Datei existiert nicht
//        		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING|SWT.RETRY |SWT.CANCEL);
//        		mb.setMessage("Ungültiger Dateipfad");
//        		if (mb.open() == SWT.CANCEL) {
//        			fileName = "";
//        			done = true;
//        		}else{
//        			done = true;
//        		}
//			}
	    }		
		
		return fileName;		
	}
	
}
