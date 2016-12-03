package lifecycle;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.MyViewers;
import com.hydra.project.model.TreeTools;
import com.hydra.project.command.Viewer_BaumViewer_ON_Command;
import com.hydra.project.database.DBSettingsTools;

@SuppressWarnings("restriction")
public class LifeCycleManager {
//	@Inject EPartService partService;
//	@Inject MApplication mApplication;
//	@Inject EModelService eModelService;
	
	public static MyViewers myViewers;
	
	@PostContextCreate
	  public void postContextCreate()
	  {
	   
		System.out.println("Post Methoden starten");
		MyImageHandler.iconListeErzeugen();
//		if (MyViewers.getBaumViewer()) Viewer_BaumViewer_ON_Command.execute(mApplication, partService, eModelService);
//		myViewers = new MyViewers();
	  }
	
	@PreSave
	  public void preSave()
	  {
		System.out.println("PreSave Methoden starten");
	  }

	/**
	 * @return the myViewers
	 */
	public static MyViewers getMyViewers() {
		return myViewers;
	}

	/**
	 * @param myViewers the myViewers to set
	 */
	public static void setMyViewers(MyViewers myViewers) {
		LifeCycleManager.myViewers = myViewers;
	}
}
