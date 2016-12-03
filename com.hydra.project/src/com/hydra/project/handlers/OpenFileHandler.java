 
package com.hydra.project.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.command.OpenFileCommand;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;

public class OpenFileHandler {
	
	@Execute
	public void execute(IWorkbench workbench, @Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell ) {

			LogfileView.log(this + "OpenFileHandler execute läuft");
			OpenFileCommand.Action(shell,myTreeItem);
	}
	
	
	@CanExecute
	public boolean canExecute() {
		//Öffnen eines Projektes ist immer möglich
		return true;
	}
		
}