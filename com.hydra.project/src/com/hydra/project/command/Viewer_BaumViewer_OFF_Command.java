package com.hydra.project.command;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import com.hydra.project.parts.LogfileView;

/**
 * @author Poehler
 * Schlieﬂt den Viewer BaumViewer
 *
 */
public class Viewer_BaumViewer_OFF_Command {

	 @Execute
	  public static void execute(MApplication app, EPartService partService, EModelService modelService) {
		 MPart element = (MPart) modelService.find("com.hydra.project.parts.BaumView", app);
	    // now switch perspective com.hydra.project.parts.
		 partService.hidePart(element);
	    LogfileView.log(Perspective_ProjectCalendarCommand.class, "BaumViewer schlieﬂen");
	  }
	
	
}
