/**
 * 
 */
package com.hydra.project.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import com.hydra.project.command.Perspective_ProjectCalendarCommand;
import com.hydra.project.model.MyTreeItem;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

/**
 * @author Poehler
 * Schaltet um auf eine neue Ansicht
 *
 */
public class Perspective_ProjectCalendarHandler {
	@Inject EPartService partService;
	@Inject MApplication mApplication;
	@Inject EModelService eModelService;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {

		Perspective_ProjectCalendarCommand.execute(mApplication, partService, eModelService);
	}
	
	
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION)@Optional MyTreeItem myTreeItem) {
		//true = Befehl kann ausgeführt werden
		//false = Befehl ist ausgegraut
		return true;
	}
	

}
