 
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
 * Der Viewer dient zur Erzeugung von RichtText
 * Zum Einsatz kommt das RichtText Framework von Eclipse Nebula
 */
public class RichtTextView {
	private static TableViewer tableViewer;
	
	// Variablen f�r die Viewerdarstellung
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
	public RichtTextView() {
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
	
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, true));

//		final RichTextEditor editor = new RichTextEditor(parent);
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(editor);
//
//		final RichTextViewer viewer = new RichTextViewer(parent, SWT.BORDER | SWT.WRAP);
//		GridDataFactory.fillDefaults().grab(true, true).span(1, 2).applyTo(viewer);
//
//		final Text htmlOutput = new Text(parent,
//				SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
//		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).hint(SWT.DEFAULT, 100).applyTo(htmlOutput);
//
//		Composite buttonPanel = new Composite(parent, SWT.NONE);
//		buttonPanel.setLayout(new RowLayout());
//		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonPanel);
//
//		Button getButton = new Button(buttonPanel, SWT.PUSH);
//		getButton.setText("Get text");
//		getButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				String htmlText = editor.getText();
//				viewer.setText(htmlText);
//				htmlOutput.setText(htmlText);
//			}
//		});
	}
	
	  /**
     * @author Poehler
     * Pr�ft, ob der aktuelle Knoten brauchbar ist f�r eine Anzeige
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
	    // empf�ngt ge�nderte Objekte mit dem Topic 'MyTreeItemEvent'
		clearViewer();
		showMessage("ParameterID: "+ myTreeItem.getParameter() + " Bezeichnung: " + myTreeItem.getBezeichnung());
		LogfileView.log(this.getClass(), "Nachricht in RichtTextViewer empfangen. Neuer Varibalenwert: "+ myTreeItem.getVariablenWert());
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
     * Meldet ge�nderte Knoten an alle Viewer weiter
     * @author Poehler
     * @param myTreeItem das Item
     */
	public static void sendEvent(MyTreeItem myTreeItem){
		myEventBroker(myTreeItem);
	}
    
    /**
     * Meldet ge�nderte Knoten an alle Viewer weiter
     * @author Poehler
     * @param myTreeItem das Item
     */
	private static void myEventBroker(MyTreeItem myTreeItem){
		//sendet die ge�nderten Informationen in den Datenbus
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully) LogfileView.log(RichtTextView.class, " Nachricht aus RichtTextViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
		
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}