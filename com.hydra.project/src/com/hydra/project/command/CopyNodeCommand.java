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
public class CopyNodeCommand {

	
	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		TreeTools.CopyMyTreeItem(myTreeItem);
		TreeTools.setZwischenspeicher(true);
		LogfileView.log(CopyNodeCommand.class, "Knoten kopiert");
	}
	
}
