 
package com.hydra.project.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.solver.Handler;

/**
 * @author Poehler
 * <p>
 * Der Viewer dient zur Erzeugung von Gallery Widgets
 * Zum Einsatz kommt das Gallery Framework von Eclipse Nebula
 */
public class GalleryView {
	private static TableViewer tableViewer;
	
	// Variablen für die Viewerdarstellung
	static MyTreeItem mySelectedTreeItem = null;
	private static Composite myParent;
	private static final String VIEWERPARAMETERID ="P1xx.001.001";
	private static MyTreeItem myTopEntryTreeItem = null;
	
// projectsicherung
	@Inject
	static
	IEventBroker eventBroker;

	@Inject
	private MDirtyable dirty;

	
	@Inject
	public GalleryView() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		myParent = parent;
		if (checkInput(parent)){
			createViewer(parent);
		}
	}
	
	private void createViewer(Composite parent){
		tableViewer = new TableViewer(parent, SWT.READ_ONLY);
		tableViewer.add("Solver");
	}

	
	
	private void clearViewer(){
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
	}

	  /**
     * @author Poehler
     * Prüft, ob der aktuelle Knoten brauchbar ist für eine Anzeige
     */
    public static Boolean checkInput(Composite parent) {
    	Boolean flag = false;
    	if (mySelectedTreeItem != null) {
    		MyTreeItem mySeachTreeItem = TreeTools.searchUpwardsForTreeItemParameter(mySelectedTreeItem, VIEWERPARAMETERID);
    		
    		if (mySeachTreeItem != null) {
    			myTopEntryTreeItem = mySeachTreeItem;
    			flag = true;
			}
		}else{
			parent.setLayout(new GridLayout());
			parent.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
			Label label = new Label (parent, SWT.NONE);
			label.setText("Keine Daten zur Anzeige vorhanden.");
		}
		return flag;	
    }
	
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		clearViewer();
		showMessage("ParameterID: "+ myTreeItem.getParameter() + " Bezeichnung: " + myTreeItem.getBezeichnung());
		LogfileView.log(this.getClass(), "Nachricht in GalleryViewer empfangen. Neuer Varibalenwert: "+ myTreeItem.getVariablenWert());
		if (myParent != null) {
			checkInput(myParent);
		}	
	}	

	  /**
     * @author Poehler
     * @param string die Meldung
     *
     */
    public static void showMessage(String string) {
		if (tableViewer != null) {  // keine Meldung wenn Fenster nicht existiert		
			tableViewer.add(string);
		}
    }
    
    /**
     * Sendet das Item an den Solver
     * @param myTreeItem das Item
     * @author Poehler
     */
    private static void callSolver(MyTreeItem myTreeItem){	
		Handler.Solver(myTreeItem);   	
    }

    /**
     * Meldet geänderte Knoten an alle Viewer weiter
     * @author Poehler
     * @param myTreeItem das Item
     */
	public static void sendEvent(MyTreeItem myTreeItem){
		myEventBroker(myTreeItem);
	}
    
    /**
     * Meldet geänderte Knoten an alle Viewer weiter
     * @author Poehler
     * @param myTreeItem das Item
     */
	private static void myEventBroker(MyTreeItem myTreeItem){
		//sendet die geänderten Informationen in den Datenbus
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully) LogfileView.log(GalleryView.class, " Nachricht aus GalleryViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
		
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}