 
package com.hydra.project.handlers;

import javax.inject.Named;

import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.hydra.project.command.DeleteNodeCommand;
import com.hydra.project.model.MyTreeItem;
//import model.MyTreeItem;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;


public class DeleteNodeHandler {
	
	private IStructuredSelection selection;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {
		DeleteNodeCommand.Action(myTreeItem);
	}
	

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {	
	
		if (!myTreeItem.isStrukturknoten()) {
			return true;
		}
		return false;
	}
}