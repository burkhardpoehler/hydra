package com.hydra.project.command;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Burkhard Pöhler
 *
 */
public class DeleteFileCommand {

	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){

		String fileName = myTreeItem.getDatenbankName();
		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING
				| SWT.YES | SWT.NO);
		mb.setMessage("Datei " + fileName + " wirklich löschen?");
		if (mb.open() == SWT.YES){
			TreeTools.removeFile(myTreeItem);
			File file = new File(myTreeItem.getDatenbankName());
			if (file.exists()){
				file.delete();
				System.out.println("lösche Datei: " + myTreeItem.getDatenbankName());
			}
			
		}
	}
}
