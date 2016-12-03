 
package com.hydra.project.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.hydra.project.parts.BaumView;
import com.hydra.project.parts.LogfileView;

public class RefreshHandler {
	
	private IStructuredSelection selection;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION)
	@Optional Object incoming) {
		BaumView.refreshAll();
		LogfileView.log("refresh aufgerufen");
	}
	
	
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)
	@Optional Object incoming) {
		
		return true;
	}
		
}