/**
 * 
 */
package com.hydra.project.command;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;

/**
 * @author Burkhard P�hler
 *
 */
public class LevelUpCommand {

	/**
	 * @author Burkhard P�hler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		//1.Schritt Hauptbaum bearbeiten
		TreeTools.CopyMyTreeItem(myTreeItem);
		DeleteNodeCommand.Action(myTreeItem);
		MyTreeItem tmp = TreeTools.PasteMyTreeItem(myTreeItem);
//		tmp.setIndex(myTreeItem.getParent().getParent().getChildren().size());	//letzte Position vom Gro�vater anw�hlen
		myTreeItem.getParent().getParent().addChild(tmp);						//an letzter Position des Gro�vaters anh�ngen
		
		//2.Schritt TreeView bearbeiten
		UpdateNodeCommand.update(myTreeItem.getParent());
		UpdateNodeCommand.update(myTreeItem.getParent().getParent());
		UpdateNodeCommand.setFocus(myTreeItem);
		LogfileView.log(NodeDownCommand.class, "Knoten Level nach oben verschoben");
	}
	
}
