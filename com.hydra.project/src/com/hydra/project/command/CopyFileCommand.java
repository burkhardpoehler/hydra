package com.hydra.project.command;

import java.io.File;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Burkhard Pöhler
 *
 */
public class CopyFileCommand {
	public static String fileNameOld;
	public static String fileNameNew;
	public static String pathNameOld;
	public static String pathNameNew;
	public static File file;
	
	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		fileNameOld = myTreeItem.getVariablenWert();
		pathNameOld = myTreeItem.getDatenbankName();
		
		fileNameNew = myTreeItem.getVariablenWert() + " neu";
		InputDialog dlg = new InputDialog(shell,"Kopiere Projekt: "+ fileNameOld, "Name des neuen Projektes:", fileNameNew,  new LengthValidator());
		if (dlg.open() == Window.OK) {
         // User clicked OK
			TreeTools.CopyMyTreeItemToFile(myTreeItem,file);
			TreeTools.addNewFile(file.toString());
		}
	}
	
}

/**
 * This class validates a String. It makes sure that the String is between 5 and 8
 * characters
 */
class LengthValidator implements IInputValidator {
	  /**
	   * Validates the String. Returns null for no error, or an error message
	   * 
	   * @param newText the String to validate
	   * @return String
	   */
	  public String isValid(String newText) {
		  File oldFile = new File(CopyFileCommand.pathNameOld);
		  String fileNameOld = oldFile.getName();
		  String fileNameNew = "";
		  
		  if (newText.endsWith(".DB4O")){
			  fileNameNew=newText;
		  }else{
			  fileNameNew=newText + ".DB4O";
		  }
			  
		  String newFile = CopyFileCommand.pathNameOld.replace(fileNameOld, fileNameNew);
		  
		  CopyFileCommand.file = new File(newFile);
		  if (CopyFileCommand.file.exists()) return "Datei existiert bereits";
		
		  int len = newText.length();
		  // Determine if input is too short or too long
		  if (len < 1) return "Dateiname zu kurz";
	
		  // Input must be OK
		  return null;
	  }
}
