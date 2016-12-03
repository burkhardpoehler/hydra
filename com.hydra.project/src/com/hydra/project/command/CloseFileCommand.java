/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.widgets.Shell;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Burkhard P�hler
 *
 */
public class CloseFileCommand {

	/**
	 * @author Burkhard P�hler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		TreeTools.removeFile(myTreeItem);
		
	}
	
}
