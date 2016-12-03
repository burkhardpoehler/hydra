 
package com.hydra.project.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
//import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * @author Poehler
 * <p>
 * Der Viewer dient zur Erzeugung von Reporten mit Birt
 */
public class ReportView {
	private static org.eclipse.birt.report.viewer.utilities.WebViewer viewer;
	
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
	
	protected static final String REPORT_DIRECTORY = "/reports/";
	protected static final String BUNDLE_DIRECTORY = "com.hydra.project";
	
	private static Label label;

	
	@Inject
	static
	IEventBroker eventBroker;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ESelectionService selectionService;
	
	@Inject
	public ReportView() {
		super();
		System.out.println("ReportView initialisiert ");
	}

	public void createPartControl(Composite parent) {
		myParent = parent;
		createViewer(parent);
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		myParent = parent;
		createViewer(parent);
	}
	
//	@Inject
//	void Contructor(Composite parent){
//		createViewer(parent);
//	}
	
	@Inject
	public static void setInput(@Optional @Named(IServiceConstants.ACTIVE_SELECTION)Object input){
		if(input==null){
			 return;
		}
//		label.setText(input.toString());
	}
	
	private void createViewer(Composite parent){
		setMyTreeItems();
//		   String path = "";
//		      String reportName = "items.rptdesign";
//		    try {
//		      URL urlneu = Platform.getBundle(BUNDLE_DIRECTORY).getEntry(REPORT_DIRECTORY + "items.rptdesign");
//		      reportName = FileLocator.toFileURL(urlneu).getPath();
//		    } catch (MalformedURLException me) {
//		      System.out.println("Fehler bei URL " + me.getStackTrace());
//		    } catch (IOException e) {
//		      e.printStackTrace();
//		    }
//
//		    Browser browser = new Browser(parent, SWT.NONE);
//		    
////		    // use the filename of your report
////		    WebViewer.display(path, WebViewer.HTML, browser);
//		
//		    System.setProperty( WebViewer.REPORT_DEBUT_MODE, "true");
//		    HashMap myparms = new HashMap();
//		    HashMap emitmap = new HashMap();
//
//		    myparms.put("SERVLET_NAME_KEY", "frameset");
//		    myparms.put("FORMAT_KEY", "html");
////		    myparms.put("RESOURCE_FOLDER_KEY", "c:/hydra");
//		    myparms.put("ALLOW_PAGE", "false");
//		    myparms.put("SHOW_PARAMETER_PAGE", "false");
//		    //myparms.put(WebViewer.MAX_ROWS_KEY, "5");
//
//		    try {
//				emitmap.put("addtorul", URLEncoder.encode("addtourl", "UTF-8"));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    myparms.put("EMITTER_OPTIONS_KEY", emitmap);
//		    
//			//myparms.put("MAX_ROWS_KEY", "500");
//		    WebViewer.display(reportName , browser, myparms);
//		    
		    
		    //WebViewer.display(reportName, myparms);
		    
		    
	}

	 /**
     * Bereitet die Daten für den Report vor
     * 
     * @author Poehler
     */
	public void setMyTreeItems(){
		List<MyTreeItem> items = new ArrayList<MyTreeItem>();
		items.add(mySelectedTreeItem);
		com.hydra.project.reportdata.ReportDataMyTreeItems.setItems(items);
	}
	
	private void showViewer(Composite parent){
	      FillLayout fillLayout = new FillLayout();
	      
	      GridLayout layout = new GridLayout();
		    layout.numColumns=4;
		    GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		    parent.setLayout(layout);
	}
	
	   
		  /**
	     * @author Poehler
	     * Prüft, ob der aktuelle Knoten brauchbar ist für eine Anzeige
	     */
	    public void selectViewer(Composite parent) {
	    	if (mySelectedTreeItem != null) {
	    		createViewer(parent);
	    		
	    		// ist das Projekt als oberster Knoten
//	    		myTopEntryTreeItem = TreeTools.searchForProjekt(mySelectedTreeItem); 
	    		// prüft, ob der Viewer überhaupt benötigt wird	    			
//	    		mySeachTreeItem = TreeTools.findMyTreeItemByID(myViewerTreeItem, SPALTENQUELLE_ID);
//	    		if (myViewerTreeItem != null) {
	    			LogfileView.log(this.getClass(), "ReportViewer erzeuge Ansicht");	    			
//	    			createViewer(parent);
//				}
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
//		viewer.refresh();
	}
    
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		LogfileView.log(this.getClass(), "Nachricht in ReportViewer empfangen. Neuer Variablenwert: "+ myTreeItem.getVariablenWert());
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
		LogfileView.log(this.getClass(), "Neue Selektion in ReportViewer empfangen.");
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
		if (wasDispatchedSuccessfully) LogfileView.log(ReportView.class, " Nachricht aus ReportViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
		LogfileView.log(ReportView.class, "Focus on ReportViewer");
		
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}