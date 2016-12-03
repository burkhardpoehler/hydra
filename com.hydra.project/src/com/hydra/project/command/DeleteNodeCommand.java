/**
 * 
 */
package com.hydra.project.command;

import com.hydra.project.database.DBTools;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;

/**
 * @author Burkhard Pöhler
 *
 */
public class DeleteNodeCommand {

	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){
		//1.Schritt TreeView bearbeiten
		com.hydra.project.parts.BaumView.DeleteNode(myTreeItem);
		//2.Schritt Hauptbaum bearbeiten
		myTreeItem.getParent().removeChild(myTreeItem);
		//3.Schritt Datenbank bearbeiten
		DBTools.deleteMyTreeItem(myTreeItem);

		UpdateNodeCommand.update(myTreeItem.getParent());
		UpdateNodeCommand.setFocus(myTreeItem.getParent());
		
		LogfileView.log(DeleteNodeCommand.class, "Knoten gelöscht");
		
		
	}
	
}
