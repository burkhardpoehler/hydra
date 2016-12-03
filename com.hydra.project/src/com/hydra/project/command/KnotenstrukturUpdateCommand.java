/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.widgets.Shell;
import com.hydra.project.editors.MyUpdateNodeStructureEditor;
import com.hydra.project.model.MyTreeItem;

/**
 * @author Burkhard P�hler
 *
 */
public class KnotenstrukturUpdateCommand {

	/**
	 * @author Burkhard P�hler
	 * @param shell die Shell
	 * @param myTreeItem das ausgew�hlte TreeItem
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		Shell myShell = new Shell();
		myShell.setParent(shell);

		
	    MyUpdateNodeStructureEditor.OpenEditor(myShell, myTreeItem);

	}
	
}
