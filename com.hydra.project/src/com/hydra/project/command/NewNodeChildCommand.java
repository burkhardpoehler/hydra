/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.editors.MyCheckTreeSelectionDialog;
import com.hydra.project.functions.MyTreeItemSettings;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;


/**
 * @author Burkhard Pöhler
 *
 */
public class NewNodeChildCommand {

	/**
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Vater
	 * Erzeugt ein neues Kind. Eigenschaften des Vaters werden übernommen
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){

		//nur der Vater des ausgewählten TreeItem bekommt das neue TreeItem!
		//erzeuge leeren Knoten

		MyCheckTreeSelectionDialog myCheckTreeSelectionDialog = new MyCheckTreeSelectionDialog(shell, myTreeItem);
		int returnCode = myCheckTreeSelectionDialog.open();
		
		if (returnCode == IDialogConstants.CANCEL_ID){
			LogfileView.log(NewNodeChildCommand.class, "Taste: Cancel");
		}else if (returnCode == IDialogConstants.OK_ID){
			LogfileView.log(NewNodeChildCommand.class, "Taste: OK");
//			MyTreeItem myCopiedTreeItem = MyTreeItemSettings.copy(MyCheckTreeSelectionDialog.selectedMyTreeItem(), myTreeItem);
			MyTreeItem selectedMyTreeItem = MyCheckTreeSelectionDialog.getSelectedMyTreeItem(); //liefert einen geclonten Zweig
			MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(selectedMyTreeItem);
			if(myTreeItemStrukturknoten.isParameter()){
				selectedMyTreeItem = MyTreeItemSettings.copyItemOnly(myTreeItem, selectedMyTreeItem);	//entferne alle Kinder da Eigenschaften nicht in Projekte kopiert werden dürfen
			}else{
				selectedMyTreeItem = MyTreeItemSettings.copyFullItem(myTreeItem, selectedMyTreeItem);
			}
			
			myTreeItem.setChild(selectedMyTreeItem);	
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(selectedMyTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Neuer Kindknoten angelegt");
		}else if (returnCode == IDialogConstants.ABORT_ID){ 											// nur eine neue Stufe erzeugen
			LogfileView.log(NewNodeChildCommand.class, "Taste: Stufe");
			MyTreeItem myNewTreeItem = MyTreeItemSettings.dummy(myTreeItem);
			myTreeItem.setChild(myNewTreeItem);	
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(myNewTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Neuen Dummykindknoten angelegt");
		}else if (returnCode == IDialogConstants.DETAILS_ID){ 											// nur einen neuen Parameter oder Eigenschaft erzeugen
			LogfileView.log(NewNodeChildCommand.class, "Taste: Parameter oder Eigenschaft");
			MyTreeItem myNewTreeItem = MyTreeItemSettings.newNode(myTreeItem);
			myTreeItem.setChild(myNewTreeItem);	
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(myNewTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Parameter oder Eigenschaft angelegt");
		}
		
	}
}
