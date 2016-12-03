 
package com.hydra.project.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.hydra.project.command.NodeDownCommand;
import com.hydra.project.model.MyTreeItem;

public class NodeDownHandler {
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {

		NodeDownCommand.Action(myTreeItem);

	}
	
	
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {

		if (!myTreeItem.isStrukturknoten()){
			if (myTreeItem.isHasPostChild()) {
				return true;
			}
		} 	
		return false;
	}
		
}