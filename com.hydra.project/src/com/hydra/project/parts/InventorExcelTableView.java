 
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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import com.hydra.project.myplugin_nebula.xviewer.edit.DefaultXViewerControlFactory;
import com.hydra.project.myplugin_nebula.xviewer.edit.XViewerControlFactory;
import com.hydra.project.myplugin_nebula.xviewer.edit.XViewerConverter;
import com.hydra.project.myplugin_nebula.xviewer.edit.XViewerMultiEditAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.projects.XViewer.Main.images.MyImageCache;
import com.hydra.projects.XViewer.Main.model.MyTreeTask;
import com.hydra.projects.XViewer.Main.MyXViewer;
import com.hydra.projects.XViewer.Main.MyXViewerContentProvider;
import com.hydra.projects.XViewer.Main.MyXViewerConverter;
import com.hydra.projects.XViewer.Main.MyXViewerFactory;
import com.hydra.projects.XViewer.Main.MyXViewerLabelProvider;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask.RunDb;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask.TaskType;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * @author Poehler
 * <p>
 * Der Viewer dient zur Erzeugung von Tabellen
 * Zum Einsatz kommt das XViewer Framework von Eclipse Nebula
 */
public class InventorExcelTableView {

	private static MyXViewer myXViewer;

	// Variablen für die Viewerdarstellung
	static MyTreeItem mySelectedTreeItem = null;
	private static Composite myParent;
	private static final String VIEWERPARAMETERID ="P111.001"; //Parameter für alle Tabellenformate
	private static final String EINSPALTIGE_TABELLEN_ID ="P111.001.001"; //Parameter einspaltige Tabellen
	private static final String MEHRSPALTIGE_TABELLEN_ID ="P111.001.002"; //Parameter mehrspaltige Tabellen
	private static final String SPALTENQUELLE_ID ="P111.005.002"; //Parameter Spaltenquelle
	private static MyTreeItem myTopEntryTreeItem = null;
	private static boolean viewerFlag = true;
	private static MyTreeItem myViewerTreeItem;
	private static MyTreeItem mySeachTreeItem;
	private static MyTreeItem mySingleColumnTreeItem;
	private static MyTreeItem myMultiColumnTreeItem;
	public static MyTreeItem myTableTreeItem;
	
	@Inject
	static
	IEventBroker eventBroker;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ESelectionService selectionService;
	
	
	@Inject
	public InventorExcelTableView() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		myParent = parent;
		selectViewer(parent);
	}
	
	private static void createViewer(Composite parent){
		
	    if (viewerFlag) {
	    	viewerFlag = false;		//Layout nicht erneut aufbauen
			parent.setLayout(new GridLayout());
			parent.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
			// Button composite for state transitions, etc
			Composite toolBarComposite = new Composite(parent, SWT.NONE);
			// bComp.setBackground(mainSComp.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			toolBarComposite.setLayout(new GridLayout(2, false));
			toolBarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			myXViewer = new MyXViewer(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,myTableTreeItem, mySeachTreeItem);
			myXViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
			myXViewer.setContentProvider(new MyXViewerContentProvider());
			myXViewer.setLabelProvider(new MyXViewerLabelProvider(myXViewer));
			XViewerControlFactory cFactory = new DefaultXViewerControlFactory();
			XViewerConverter converter = new MyXViewerConverter();
			myXViewer.setXViewerEditAdapter(new XViewerMultiEditAdapter(cFactory, converter));
			createTaskActionBar(toolBarComposite);
			
		}
	    selectTabletype();
	}

	private static void selectTabletype(){
		List<Object> tasks = new ArrayList<Object>();
	    // nur für einspaltige Tabellen
	    if (mySingleColumnTreeItem != null) {
			if (myXViewer != null && mySelectedTreeItem != null) {
				tasks.clear();				
				tasks.addAll(getMyTasks(mySeachTreeItem));	
//				myXViewer.setInputXViewer(tasks);
			} 
		}	
	    
	    // nur für mehrspaltige Tabellen
	    if (myMultiColumnTreeItem != null) {
			if (myXViewer != null && mySelectedTreeItem != null) {
				tasks.clear();
				if (mySeachTreeItem != null) {
					tasks.addAll(getMyTasks(mySeachTreeItem));
				}
				MyXViewerFactory.setMyTreeItem(mySeachTreeItem); 		//Tabellespalten neu konfigurieren
//				MyXViewerFactory.setMyTableTreeItem(myTableTreeItem);	//Das Item zur Speicherung der Tabelleneinstellungen
//				MyNewXViewerCustomizations.setMyTableTreeItem(myTableTreeItem);
				myXViewer.setInputXViewer(tasks);						//Tabelle neu aufbauen
			} 
		}	
	}
	
	private static void refreshViewer(MyTreeItem myTreeItem){
		myXViewer.refresh();
	}
	
	private void createStandardViewer(Composite parent){
			LogfileView.log(this.getClass(), "TableViewer keine Ansicht möglich");
			parent.setLayout(new GridLayout());
			parent.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
			Label label = new Label (parent, SWT.NONE);
			label.setText("Keine Daten zur Anzeige vorhanden.");
	}

	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		
		LogfileView.log(this.getClass(), "Nachricht in TableViewer empfangen. Neuer Variablenwert: "+ myTreeItem.getVariablenWert());
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
		LogfileView.log(this.getClass(), "Neue Selektion in TableViewer empfangen.");
		if (myTreeItem != null) {
			mySelectedTreeItem = myTreeItem;
			if (myParent != null) {
				selectViewer(myParent);
			}	
		}

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
    		myViewerTreeItem = TreeTools.searchUpwardsForTreeItemParameter(mySelectedTreeItem, VIEWERPARAMETERID);
    		// püft die Tabellenart
    		mySingleColumnTreeItem = TreeTools.searchUpwardsForTreeItemParameter(mySelectedTreeItem, EINSPALTIGE_TABELLEN_ID);
    		myMultiColumnTreeItem = TreeTools.searchUpwardsForTreeItemParameter(mySelectedTreeItem, MEHRSPALTIGE_TABELLEN_ID);
    		
    		if (mySingleColumnTreeItem != null) myTableTreeItem = mySingleColumnTreeItem;
    		if (myMultiColumnTreeItem != null) myTableTreeItem = myMultiColumnTreeItem;
    		
    		// sucht die Quelle für Tabelle
    		if(myMultiColumnTreeItem != null){
    			
    			mySeachTreeItem = TreeTools.findMyTreeItemByID(myMultiColumnTreeItem, SPALTENQUELLE_ID);
    			if (mySeachTreeItem != null) {		//Quelle gefunden
					//suche unterhalb des Projektes den ersten Knoten mit passender Parameter ID
    				mySeachTreeItem = TreeTools.findMyTreeItemByID(myTopEntryTreeItem,
							mySeachTreeItem.getVariablenWert());
				}
    			
    		}
    		
    		if (myViewerTreeItem != null) {
    			LogfileView.log(this.getClass(), "TableViewer erzeuge Ansicht");
    			
    			createViewer(parent);
			}
		}
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
		if (wasDispatchedSuccessfully) LogfileView.log(InventorExcelTableView.class, " Nachricht aus TableViewer gesendet "+ myTreeItem.getBezeichnung());
	}
	@Focus
	public void setFocus() {
//		viewer.getControl().setFocus();
		LogfileView.log(InventorExcelTableView.class, "Focus on Table Viewer");
		System.out.println("Focus on Table Viewer");
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	
	  public static void createTaskActionBar(Composite parent) {

	      Composite actionComp = new Composite(parent, SWT.NONE);
	      actionComp.setLayout(new GridLayout());
	      actionComp.setLayoutData(new GridData(GridData.END));

	      ToolBar toolBar = new ToolBar(actionComp, SWT.FLAT | SWT.RIGHT);
	      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	      toolBar.setLayoutData(gd);

	      ToolItem refreshItem = new ToolItem(toolBar, SWT.PUSH);
	      refreshItem.setImage(MyImageCache.getImage("refresh.gif"));
	      refreshItem.setToolTipText("Refresh");
	      refreshItem.addSelectionListener(new SelectionAdapter() {
	         @Override
	         public void widgetSelected(SelectionEvent e) {
//	            List<Object> tasks = new ArrayList<Object>();
//	            for (int x = 0; x < 1; x++) {
//	               tasks.addAll(getMyTasks(mySelectedTreeItem));
//	            }
	            selectTabletype();
	         }
	      });

	      Action dropDownAction = myXViewer.getCustomizeAction();
	      new ActionContributionItem(dropDownAction).fill(toolBar, 0);

//	      ToolItem descriptionItem = new ToolItem(toolBar, SWT.PUSH);
//	      descriptionItem.setImage(MyImageCache.getImage("descriptionView.gif"));
//	      descriptionItem.setToolTipText("Show Description View");
//	      descriptionItem.addSelectionListener(new SelectionAdapter() {
//	         @Override
//	         public void widgetSelected(SelectionEvent e) {
//	        	 myXViewer.getCustomizeMgr().loadCustomization(MyDefaultCustomizations.getDescriptionCustomization());
//	        	 myXViewer.refresh();
//	         }
//	      });
//
//	      ToolItem completeItem = new ToolItem(toolBar, SWT.PUSH);
//	      completeItem.setImage(MyImageCache.getImage("completionView.gif"));
//	      completeItem.setToolTipText("Show Completion View");
//	      completeItem.addSelectionListener(new SelectionAdapter() {
//	         @Override
//	         public void widgetSelected(SelectionEvent e) {
//	        	 myXViewer.getCustomizeMgr().loadCustomization(MyDefaultCustomizations.getCompletionCustomization());
//	        	 myXViewer.refresh();
//	         }
//	      });

	   }

	   /**
		 * Erzeugt eine Liste mit allen Unterknoten eines Knotens
		 * Je Unterknoten wird eine Spalte vorgesehen
		 * Anschließend werden alle identischen Knoten gesucht und in die TaskListe eingetragen
		 * @param myTreeItem Startknoten
		 * @return taskList die Liste mit den Knoten
		 */
	   private static List<IMyTreeTask> getMyTasks(MyTreeItem myTreeItem) {
		   
		   
		   // 1. Schritt: erstelle eine Liste mit identischen Knotenstrukturen
		   List<MyTreeItem> foundIdenticalTreeItems = new  ArrayList<MyTreeItem>();
		   foundIdenticalTreeItems = searchForIdenticalNodes(myTreeItem);
		   
		   // 2. Schritt: erstelle eine Liste von Tasks (Zeilen) für die Tabelle
		   
		   List<IMyTreeTask> tasks = new ArrayList<IMyTreeTask>();


		   for (int n = 0; n < foundIdenticalTreeItems.size(); n++) {
			   List<MyTreeItem> rowList = new  ArrayList<MyTreeItem>();
			   rowList = TreeTools.prepareNodeList(foundIdenticalTreeItems.get(n), rowList );
			   MyTreeTask task = new MyTreeTask(RunDb.Test_Db, TaskType.Backup, rowList);
//			   MyTask task = new MyTask(RunDb.Test_Db, TaskType.Backup, foundIdenticalTreeItems.get(n));
			   
//			   MyTask task = new MyTask(RunDb.Test_Db, TaskType.Backup, foundIdenticalTreeItems.get(n));
			   tasks.add(task);
		   }
		   return tasks;
	   }

		/**
		 * Durchsucht das Projekt abwärts nach anderen identischen ParameterIDs.
		 * Erzeugt eine Liste der gefundenen identischen Parameter.
		 * @param myTreeItem Startknoten
		 * @return foundTreeItems = Liste alle identischer Knoten
		 */
		private static List<MyTreeItem> searchForIdenticalNodes(MyTreeItem myTreeItem){
			boolean flag = false;
			// 1. Stufe: rekursives Durchlaufen der Knotenstruktur
			MyTreeItem project = TreeTools.searchForProjekt(myTreeItem);
			List<MyTreeItem> foundTreeItems = new  ArrayList<MyTreeItem>();
			foundTreeItems = TreeTools.findAllMyTreeItemByID(foundTreeItems, project, myTreeItem.getParameter());
			
			List<MyTreeItem> listSample = new  ArrayList<MyTreeItem>();
			listSample = TreeTools.prepareNodeList(myTreeItem, listSample);
			List<MyTreeItem> foundIdenticalTreeItems = new  ArrayList<MyTreeItem>();
			for (int n = 0; n < foundTreeItems.size(); n++) {
				List<MyTreeItem> rowList = new  ArrayList<MyTreeItem>();
				rowList = TreeTools.prepareNodeList(foundTreeItems.get(n), rowList );
				
				if (TreeTools.compareList(listSample,rowList)){
					foundIdenticalTreeItems.add(foundTreeItems.get(n));
				}
			}
			
			//entferne alle Knoten, die im Strukturknoten Basis, Einstellungen und Vorlagen sind
			List<MyTreeItem> removeSample = new  ArrayList<MyTreeItem>();
			if (foundIdenticalTreeItems != null) {
				for (MyTreeItem o : foundIdenticalTreeItems) {
					if (!TreeTools.checkForProjektknoten(o)) {
						removeSample.add(o);
					}
				}
				if (removeSample != null) {
					for (MyTreeItem o : removeSample) {
						foundIdenticalTreeItems.remove(o);
					} 
				} 
			}
			return foundIdenticalTreeItems;
		}

		/**
		 * @return the myTableTreeItem
		 */
		public static MyTreeItem getMyTableTreeItem() {
			return myTableTreeItem;
		}
}