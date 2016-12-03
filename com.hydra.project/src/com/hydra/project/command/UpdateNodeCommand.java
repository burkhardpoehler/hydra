/**
 * 
 */
package com.hydra.project.command;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;

import com.hydra.project.database.DBTools;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.BaumView;

/**
 * @author Poehler
 *
 */
public class UpdateNodeCommand {
	
	@Inject
	static
	IEventBroker eventBroker;
	
	
	public UpdateNodeCommand(MyTreeItem myTreeItem){
		update(myTreeItem);
	}
	
	/**
	 * @author Poehler
	 * @param myTreeItem der Knoten zum Updaten
	 * 
	 * schickt einen Nachricht an alle Viewer zum updaten
	 * und speichert den Datensatz in der jeweiligen Datenbank
	 * 
	 */
	public static void update(MyTreeItem myTreeItem){
		//sendet die geänderten Informationen in den Datenbus
//		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		TreeTools.addParentUUID(myTreeItem);		//Reparaturroutine
		TreeTools.addParent(myTreeItem);			//Reparaturroutine
		TreeTools.updateValues(myTreeItem);			//aktualisiert Werte und ergänzt fehlende Werte
		
		DBTools.updateMyTreeItem(myTreeItem);	//speichert den Datensatz
		BaumView.RefreshNode(myTreeItem);
		BaumView.RefreshAllNodesAbove(myTreeItem);


//		BaumView.RevealNodes(myTreeItem);
		
//		if (wasDispatchedSuccessfully) System.out.println("UpdateNodeCommand gesendet"+ myTreeItem.getVariablenWert());
	}
	
	/**
	 * @author Poehler
	 * @param myTreeItem der Knoten zum Setzen des Focus
	 * 
	 */
	public static void setFocus(MyTreeItem myTreeItem){
		BaumView.RevealNodes(myTreeItem);
	}
	
}
