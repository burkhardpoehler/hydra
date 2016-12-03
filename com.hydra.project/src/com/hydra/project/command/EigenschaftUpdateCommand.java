/**
 * 
 */
package com.hydra.project.command;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Burkhard P�hler
 *
 */
public class EigenschaftUpdateCommand {

	/**
	 * @author Burkhard P�hler
	 *
	 */
	public static void Action(Shell shell, MyTreeItem myTreeItem){
		
		//alle Eigenschaften mit der gleichen Paramter ID werden gesucht
		//und auf den neuen Stand gebracht.
		//Parameter Value wird nicht ver�ndert

		MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING| SWT.YES | SWT.NO);
		String string = "Diese Funktion aktualisiert in allen Parametern die ausgew�hlten Eigenschaft "
				+ myTreeItem.getParameter() + " > " + myTreeItem.getBezeichnung() + " < . Der eingegebene Wert bleibt dabei unver�ndert."
						+ " Eigenschaft updaten?";
		mb.setMessage(string);
		if (mb.open() == SWT.YES){
			TreeTools.updateEigenschaft(myTreeItem);
			
		}
	}
	
}
