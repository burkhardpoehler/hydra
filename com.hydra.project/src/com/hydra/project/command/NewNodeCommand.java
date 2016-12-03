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
public class NewNodeCommand {

	/**
	 * @author Burkhard Pöhler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		
		//nur der Vater des ausgewählten TreeItem bekommt das neue TreeItem!
		//hole Vater

		MyCheckTreeSelectionDialog myCheckTreeSelectionDialog = new MyCheckTreeSelectionDialog(shell, myTreeItem);
		int returnCode = myCheckTreeSelectionDialog.open();
		if (returnCode == IDialogConstants.CANCEL_ID){
			LogfileView.log(NewNodeCommand.class, "Taste: Cancel");
		}else if (returnCode == IDialogConstants.OK_ID){
			LogfileView.log(NewNodeCommand.class, "Taste: OK");
		
			MyTreeItem myCopiedTreeItem = MyCheckTreeSelectionDialog.getSelectedMyTreeItem();	//liefert einen geclonten Zweig
			MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myCopiedTreeItem);	
			if(myTreeItemStrukturknoten.isParameter()){
				myCopiedTreeItem = MyTreeItemSettings.copyItemOnly(myTreeItem, myCopiedTreeItem);	//entferne alle Kinder da Eigenschaften nicht in Projekte kopiert werden dürfen
			}else{
				myCopiedTreeItem = MyTreeItemSettings.copyFullItem(myTreeItem, myCopiedTreeItem);
			}
			
			myCopiedTreeItem.setHasPostChild(true);
			myCopiedTreeItem.setIndex(myTreeItem.getIndex()+1);  		//hinter dem ausgewählten Knoten einfügen	
			myTreeItem.getParent().setChild(myCopiedTreeItem);	
//			UpdateNodeCommand.update(myCopiedTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(myCopiedTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Neuer Knoten angelegt");
		}else if (returnCode == IDialogConstants.ABORT_ID){ 			// nur eine neue Stufe erzeugen
			LogfileView.log(NewNodeCommand.class, "Taste: Stufe");
			MyTreeItem myNewTreeItem = MyTreeItemSettings.dummy(myTreeItem);
			myNewTreeItem.setHasPostChild(true);
			myNewTreeItem.setIndex(myTreeItem.getIndex()+1);  		//hinter dem ausgewählten Knoten einfügen	
			myTreeItem.getParent().setChild(myNewTreeItem);	
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(myNewTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Neuen Dummyknoten angelegt");
		}else if (returnCode == IDialogConstants.DETAILS_ID){			// neuen Knoten einfügen
			LogfileView.log(NewNodeCommand.class, "Taste: Parameter oder Eigenschaft einfügen");
//			MyTreeItem myCopiedTreeItem = MyTreeItemSettings.copy(MyCheckTreeSelectionDialog.selectedMyTreeItem(), myTreeItem);
			MyTreeItem myCopiedTreeItem =MyTreeItemSettings.newNode(myTreeItem);
//			myCopiedTreeItem.setHasPostChild(true);
			myCopiedTreeItem.setIndex(myTreeItem.getIndex()+1);  		//hinter dem ausgewählten Knoten einfügen	
			myTreeItem.getParent().setChild(myCopiedTreeItem);	
			UpdateNodeCommand.update(myTreeItem);
			UpdateNodeCommand.update(myTreeItem.getParent());
			UpdateNodeCommand.setFocus(myCopiedTreeItem);
			LogfileView.log(NewNodeChildCommand.class, "Neuer Parameter oder Eigenschaft angelegt");
		}
	}
	
}
