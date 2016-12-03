/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.widgets.Shell;

import com.hydra.project.editors.MySettings;
import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 * Öffnet die Einstellungen
 *
 */
public class OpenSettingsCommand {

	public static void Action(Shell shell, MyTreeItem myTreeItem){

		MySettings.main(null);
		
	}
}	
