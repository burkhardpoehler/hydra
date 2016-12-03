/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import com.hydra.project.parts.LogfileView;

/**
 * @author Poehler
 * Öffnet die Perspektive Projektkalender
 *
 */
public class Perspective_ProjectCalendarCommand {

	 @Execute
	  public static void execute(MApplication app, EPartService partService, EModelService modelService) {
	    MPerspective element = (MPerspective) modelService.find("com.hydra.project.perspective.ProjectCalendar", app);
	    // now switch perspective com.hydra.project.parts.
	    partService.switchPerspective(element);
	    LogfileView.log(Perspective_ProjectCalendarCommand.class, "Perspektive für Projektkalender öffnen");
	  }
}	
