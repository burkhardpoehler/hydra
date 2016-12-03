 
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
import org.eclipse.swt.widgets.Composite;

import com.hydra.project.model.MyTreeItem;
import com.hydra.solver.Handler;

/**
 * @author Poehler
 * <p>
 * Der Viewer dient nur als Brücke um Änderungen an Knoten erfassen zu können
 * und geänderte Knoten an andere Viewer weiterzumelden
 */
public class SolverView {
	private static TableViewer tableViewer;
// projectsicherung
	@Inject
	static
	IEventBroker eventBroker;

	@Inject
	private MDirtyable dirty;

	
	@Inject
	public SolverView() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		createViewer(parent);
	}
	
	private void createViewer(Composite parent){
		tableViewer = new TableViewer(parent, SWT.READ_ONLY);
		tableViewer.add("Solver");
	}

	private void clearViewer(){
		tableViewer.getTable().clearAll();
		tableViewer.refresh();
	}

	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		clearViewer();
		showMessage("ParameterID: "+ myTreeItem.getParameter() + " Bezeichnung: " + myTreeItem.getBezeichnung());
		LogfileView.log(this.getClass(), "Nachricht in SolverViewer empfangen. Neuer Varibalenwert: "+ myTreeItem.getVariablenWert());
		callSolver(myTreeItem);
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
		if (wasDispatchedSuccessfully) LogfileView.log(SolverView.class, " Nachricht aus SolverViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
		
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}