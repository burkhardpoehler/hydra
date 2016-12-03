/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.widgets.Shell;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Burkhard Pöhler
 *
 */
public class CloseFileCommand {

	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		TreeTools.removeFile(myTreeItem);
		
	}
	
}
