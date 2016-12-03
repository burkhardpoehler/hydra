 
package com.hydra.project.parts;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.visualization.widgets.datadefinition.IManualValueChangeListener;
import org.eclipse.nebula.visualization.widgets.figures.KnobFigure;
import org.eclipse.nebula.visualization.widgets.figures.MeterFigure;
import org.eclipse.nebula.visualization.widgets.figures.ProgressBarFigure;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.solver.Handler;

/**
 * @author Poehler
 * <p>
 * Der Viewer dient zur Erzeugung von Visualization Widgets
 * Zum Einsatz kommt das Visualization Framework von Eclipse Nebula
 */
public class VisualizationView {
	private static Viewer viewer;
	
	private static int counter = 0;
	private static boolean viewerFlag = true;
	private static MyTreeItem myViewerTreeItem;
	private static MyTreeItem mySeachTreeItem;
	public static MyTreeItem myTableTreeItem;
	
	// Variablen für die Viewerdarstellung
	static MyTreeItem mySelectedTreeItem = null;
	private static Composite myParent;
	private static final String VIEWERPARAMETERID ="P121.001";
	private static MyTreeItem myTopEntryTreeItem = null;
	private static final String SPALTENQUELLE_ID ="P121.001.001"; //Parameter
	

	@Inject
	static
	IEventBroker eventBroker;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ESelectionService selectionService;
	
	@Inject
	public VisualizationView() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		myParent = parent;
		createViewer(parent);
	}
	
	private void createViewer(Composite parent){
//		tableViewer = new TableViewer(parent, SWT.READ_ONLY);
//		tableViewer.add("Solver");
		Canvas canvas = new Canvas(parent, 0);
		showViewer(parent);
	}

	private void clearViewer(){
//		tableViewer.getTable().clearAll();
//		tableViewer.refresh();
	}
	
	private void showViewer(Composite parent){
	      FillLayout fillLayout = new FillLayout();
	      
	      GridLayout layout = new GridLayout();
		    layout.numColumns=4;
		    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		    parent.setLayout(layout);
		    
		    //####################################################################################################
		    Canvas meterFigureCanvas = new Canvas(parent, SWT.NONE);
		    meterFigureCanvas.setLayoutData(gd);
		    //use LightweightSystem to create the bridge between SWT and draw2D
			final LightweightSystem lws1 = new LightweightSystem(meterFigureCanvas);		
			
			//Create Gauge
			final MeterFigure meterFigure = new MeterFigure();
			
			//Init gauge
			meterFigure.setBackgroundColor(XYGraphMediaFactory.getInstance().getColor(255, 255, 255));		
			meterFigure.setBorder(new SchemeBorder(SchemeBorder.SCHEMES.ETCHED));
			meterFigure.setRange(-100, 100);
			meterFigure.setLoLevel(-50);
			meterFigure.setLoloLevel(-80);
			meterFigure.setHiLevel(60);
			meterFigure.setHihiLevel(80);
			meterFigure.setMajorTickMarkStepHint(50);
			lws1.setContents(meterFigure);		
			
			//Update the gauge in another thread.
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(new Runnable() {
				
				@Override
				public void run() {
					meterFigureCanvas.getDisplay().asyncExec(new Runnable() {					
						@Override
						public void run() {
							meterFigure.setValue(Math.sin(counter++/10.0)*100);						
						}
					});
				}
			}, 100, 100, TimeUnit.MILLISECONDS);
			
		    //####################################################################################################
			Canvas knobCanvas = new Canvas(parent, SWT.NONE);
			knobCanvas.setLayoutData(gd);
			//use LightweightSystem to create the bridge between SWT and draw2D
			final LightweightSystem lws2 = new LightweightSystem(knobCanvas);		
			
			//Create Knob
			final KnobFigure knobFigure = new KnobFigure();
			
			//Init Knob
			knobFigure.setRange(-100, 100);
			knobFigure.setLoLevel(-50);
			knobFigure.setLoloLevel(-80);
			knobFigure.setHiLevel(60);
			knobFigure.setHihiLevel(80);
			knobFigure.setMajorTickMarkStepHint(50);
			knobFigure.setThumbColor(ColorConstants.gray);
			knobFigure.addManualValueChangeListener(new IManualValueChangeListener() {			
				public void manualValueChanged(double newValue) {
					System.out.println("You set value to: " + newValue);
				}
			});
			
			lws2.setContents(knobFigure);	
			
			
		    //####################################################################################################
			Canvas progressBarCanvas = new Canvas(parent, SWT.NONE);
			progressBarCanvas.setLayoutData(gd);
			//use LightweightSystem to create the bridge between SWT and draw2D
			final LightweightSystem lws3 = new LightweightSystem(progressBarCanvas);		
			
			//Create Gauge
			final ProgressBarFigure progressBarFigure = new ProgressBarFigure();
			
			//Init gauge
			progressBarFigure.setFillColor(XYGraphMediaFactory.getInstance().getColor(0, 255, 0));
					
			progressBarFigure.setRange(-100, 100);
			progressBarFigure.setLoLevel(-50);
			progressBarFigure.setLoloLevel(-80);
			progressBarFigure.setHiLevel(60);
			progressBarFigure.setHihiLevel(80);
			progressBarFigure.setMajorTickMarkStepHint(50);
			progressBarFigure.setHorizontal(true);
			progressBarFigure.setOriginIgnored(true);
			
			lws3.setContents(progressBarFigure);		
			
			//Update the gauge in another thread.
			ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
			ScheduledFuture<?> future1 = scheduler1.scheduleAtFixedRate(new Runnable() {
				
				public void run() {
					progressBarCanvas.getDisplay().asyncExec(new Runnable() {					
						public void run() {
							progressBarFigure.setValue(Math.sin(counter++/10.0)*100);						
						}
					});
				}
			}, 100, 100, TimeUnit.MILLISECONDS);		

	}
	
	   
		  /**
	     * @author Poehler
	     * Prüft, ob der aktuelle Knoten brauchbar ist für eine Anzeige
	     */
	    public void selectViewer(Composite parent) {
	    	if (mySelectedTreeItem != null) {
	    		// ist das Projekt als oberster Knoten
	    		myTopEntryTreeItem = TreeTools.searchForProjekt(mySelectedTreeItem); 
	    		// prüft, ob der Viewer überhaupt benötigt wird	    			
	    		
	    		if (myViewerTreeItem != null) {
	    			mySeachTreeItem = TreeTools.findMyTreeItemByID(myViewerTreeItem, SPALTENQUELLE_ID);
	    			LogfileView.log(this.getClass(), "VisualisationViewer erzeuge Ansicht");	    			
	    			createViewer(parent);
				}
			}
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

    private static void refreshViewer(MyTreeItem myTreeItem){
		viewer.refresh();
	}
    
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		LogfileView.log(this.getClass(), "Nachricht in VisualizationViewer empfangen. Neuer Variablenwert: "+ myTreeItem.getVariablenWert());
		if (myTreeItem != null) {
			mySelectedTreeItem = myTreeItem;
			if (myParent != null) {
				selectViewer(myParent);
			}	
		}
	}	
	
	@ Inject
	public void setSelection (@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional MyTreeItem myTreeItem) {
		//wird in einem anderen Editor eine neue Selektion vorgenommen, wird diese Funktion automatisch aufgerufen
		LogfileView.log(this.getClass(), "Neue Selektion in VisualizationViewer empfangen.");
		if (myTreeItem != null) {
			mySelectedTreeItem = myTreeItem;
			if (myParent != null) {
				selectViewer(myParent);
			}	
		}
	}
	
	  /**
     * @author Poehler
     * @param string die Meldung
     *
     */
    public static void showMessage(String string) {
//		if (tableViewer != null) {  // keine Meldung wenn Fenster nicht existiert		
//			tableViewer.add(string);
//		}
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
		refreshViewer(myTreeItem);
	}
    
    /**
     * Meldet geänderte Knoten an alle Viewer weiter
     * @author Poehler
     * @param myTreeItem das Item
     */
	private static void myEventBroker(MyTreeItem myTreeItem){
		//sendet die geänderten Informationen in den Datenbus
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully) LogfileView.log(VisualizationView.class, " Nachricht aus VisualizationViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
		LogfileView.log(VisualizationView.class, "Focus on VisualizationViewer");
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}