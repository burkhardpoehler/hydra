/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.widgets.Shell;
import com.hydra.project.editors.MyUpdateNodeStructureEditor;
import com.hydra.project.model.MyTreeItem;

/**
 * @author Burkhard Pöhler
 *
 */
public class KnotenstrukturUpdateCommand {

	/**
	 * @author Burkhard Pöhler
	 * @param shell die Shell
	 * @param myTreeItem das ausgewählte TreeItem
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		Shell myShell = new Shell();
		myShell.setParent(shell);

		
	    MyUpdateNodeStructureEditor.OpenEditor(myShell, myTreeItem);

	}
	
}
