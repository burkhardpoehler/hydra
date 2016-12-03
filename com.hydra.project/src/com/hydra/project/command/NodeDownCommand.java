/**
 * 
 */
package com.hydra.project.command;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;

/**
 * @author Burkhard P�hler
 *
 */
public class NodeDownCommand {

	/**
	 * @author Burkhard P�hler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		//1.Schritt Hauptbaum bearbeiten
		myTreeItem.shiftMyTreeItemDownwards();
		//2.Schritt TreeView aktualisieren
		UpdateNodeCommand.update(myTreeItem.getParent());
		UpdateNodeCommand.setFocus(myTreeItem);
		LogfileView.log(NodeDownCommand.class, "Knoten nach unten verschoben");
	}
	
}
