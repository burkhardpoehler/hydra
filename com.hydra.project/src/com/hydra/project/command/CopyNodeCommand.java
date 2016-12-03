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
public class CopyNodeCommand {

	
	/**
	 * @author Burkhard P�hler
	 *
	 */
	public static void Action(MyTreeItem myTreeItem){

		TreeTools.CopyMyTreeItem(myTreeItem);
		TreeTools.setZwischenspeicher(true);
		LogfileView.log(CopyNodeCommand.class, "Knoten kopiert");
	}
	
}
