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
public class CutNodeCommand {
	
	//Constructor
	public CutNodeCommand(){
		
	}

	/**
	 * Der Cut Befehl besteht aus zwei anderen Befehlen:
	 * 1) 	Copy Command
	 * 2)	Delete Command
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		TreeTools.CopyMyTreeItem(myTreeItem);
		DeleteNodeCommand.Action(myTreeItem);
		TreeTools.setZwischenspeicher(true);
		
		LogfileView.log(CutNodeCommand.class, "Knoten ausgeschnitten");
	}
	
}
