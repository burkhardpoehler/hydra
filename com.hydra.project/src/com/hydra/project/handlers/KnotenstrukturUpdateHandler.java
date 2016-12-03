 
package com.hydra.project.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.command.KnotenstrukturUpdateCommand;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

public class KnotenstrukturUpdateHandler {

	@Execute
	public void execute(IWorkbench workbench, @Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {

		KnotenstrukturUpdateCommand.Action(shell, myTreeItem);
	
	}
	
	
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {

		if (myTreeItem.isParameter()) {
			MyTreeItem find = TreeTools.searchForProjekt(myTreeItem);	
			if (find.isProjekt()) {
				return true;
			}
		}
		return false;
	}	
}