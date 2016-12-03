/**
 * 
 */
package com.hydra.project.command;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;

/**
 * @author Burkhard Pöhler
 *
 */
public class PasteNodeCommand {

	/**
	 * Der neue Knoten wird unter dem ausgewählten Knoten auf gleicher Ebene  eingefügt 
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		if (myTreeItem.isStrukturknoten()) {			//wenn STrukturknoten, dann unterhalb als letztes anhängen
			MyTreeItem tmp = TreeTools.PasteMyTreeItem(myTreeItem);
			tmp.setIndex(myTreeItem.getChildren().size());	//letzte Position anwählen
			myTreeItem.setChild(tmp);
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.setFocus(myTreeItem);
		}else {
			LogfileView.log(PasteNodeCommand.class, "Aktiver Knoten Index" + myTreeItem.getIndex());
			MyTreeItem tmp = TreeTools.PasteMyTreeItem(myTreeItem);  	//holt den gespeicherten Knoten aus der temporären Datenbank
			tmp.setIndex(myTreeItem.getIndex()+1);
			myTreeItem.setChild(tmp);
//			myTreeItem.getParent().setChild(tmp);
//			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.setFocus(myTreeItem);
		}
		LogfileView.log(PasteNodeCommand.class, "Knoten eingefügt");
	}
	
}
