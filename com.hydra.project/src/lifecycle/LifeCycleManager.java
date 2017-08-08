package lifecycle;

import java.awt.Rectangle;
import java.io.File;

import javax.inject.Inject;


import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.MyViewers;
import com.hydra.project.model.TreeTools;
import com.hydra.project.startProcedure.StartProcedure;
import com.hydra.project.command.Viewer_BaumViewer_ON_Command;
import com.hydra.project.database.DBUserSettings;

@SuppressWarnings("restriction")
public class LifeCycleManager {
//	@Inject EPartService partService;
//	@Inject MApplication mApplication;
//	@Inject EModelService eModelService;
	
	public static MyViewers myViewers;
	
	@PostContextCreate
	  public void postContextCreate(IApplicationContext appContext, Display display)
	  {
        final Shell shell = new Shell(SWT.SHELL_TRIM);
        
		System.out.println("Startroutinen:");
		StartProcedure.start();
		
		//prüfen, ob Start erfolgreich, wenn nicht Abbruch
        if (StartProcedure.getFlag()) {
        
    		System.out.println("Post Methoden starten:");
    		MyImageHandler.iconListeErzeugen();
//    		if (MyViewers.getBaumViewer()) Viewer_BaumViewer_ON_Command.execute(mApplication, partService, eModelService);
//    		myViewers = new MyViewers();
    		
    		
		}else{
			// close the static splash screen
//			appContext.applicationRunning();
			// position the shell
			setLocation(display, shell);

			MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
			mb.setText("Hydra");
			mb.setMessage("Startroutine wurde abgebrochen. Basisdaten fehlen im Ordner C:/ProgramData/Hydrax_x_x/Basisdaten.db4o!");

    		if (mb.open() == SWT.CANCEL) {
    			System.out.println("Startroutine wurde abgebrochen. Basisdaten fehlen");
    			System.exit(-1);
    		}else{
    			File basisdatenFile = StartProcedure.selectBasisdatenFile(shell);
    			
    			if (basisdatenFile == null){
    				System.out.println("Startroutine wurde abgebrochen. Basisdaten fehlen");
    	    		System.exit(-1);
    			};
    		}
		}
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
	
	/**
	 * Sucht den Hauptmonitor und platziert mittig ein Fenster
	 */
    private void setLocation(Display display, Shell shell) {
        Monitor monitor = display.getPrimaryMonitor();
        org.eclipse.swt.graphics.Rectangle monitorRect = monitor.getBounds();
        org.eclipse.swt.graphics.Rectangle shellRect = shell.getBounds();
        int x = monitorRect.x + (monitorRect.width - shellRect.width) / 2;
        int y = monitorRect.y + (monitorRect.height - shellRect.height) / 2;
        shell.setLocation(x, y);
    }
    
    

}
