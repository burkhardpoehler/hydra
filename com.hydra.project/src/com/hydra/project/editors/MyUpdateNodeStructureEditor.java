/**
 * 
 */
package com.hydra.project.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;

import java.util.Iterator;

import lifecycle.MyImageHandler;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.treemapper.TreeMapperUIConfigProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;

import swing2swt.layout.BorderLayout;

import com.hydra.project.command.UpdateNodeCommand;
import com.hydra.project.editors.MySemanticTreeMapperSupport;


/**
 * @author Poehler
 *
 */
public class MyUpdateNodeStructureEditor {
	  private static SashForm control;
	  private static SashForm control2;
	  private static final Composite composite = null;
	  private static Combo combobox;
	  private static MyTreeItem myLeftTreeItem;
	  private static MyTreeItem myRightTreeItem;
	  private static MyTreeItem myFilterTreeItem = null;
	  private static MyTreeItem mySelectedTreeItem = null;
	  protected static List <MyTreeItem> leftList;
	  protected static List <MyTreeItem> rightList;
	  protected static List <MyTreeItem> leftListItem;
	  protected static List <MyTreeItem> rightListItem;
	  private static Button OKButton;
	  private static Boolean noSelectionInTreeviewer=false;
	  private static List<MyTreeItemBean> mappings = new ArrayList<MyTreeItemBean>();
	  private static Composite myDisplay; 
	  private static Shell shell;
	  private static int selection;
	  private static int anzahlKnoten = 0;
	  private static List<MyTreeItem> foundTreeItems = new ArrayList<MyTreeItem>();
	  private static List<MyTreeItem> foundIdenticalTreeItems = new ArrayList<MyTreeItem>();
	  public static boolean deleteLink = false;
	  public static boolean showLink = false;
	  public static Button buttonOK;
	  public static Button buttonDeleteLink;
	  public static Button buttonShowLink;
	  private static MyTreeItemBean myActiveTreeItemBean;
	  private static Composite middleComposite;
	  private static Label lbtMessage;
	  
	 static MyTreeMapper<MyTreeItemBean, MyTreeItem, MyTreeItem> mapper = 
				new MyTreeMapper<MyTreeItemBean, MyTreeItem, MyTreeItem>();
	 private static MySemanticTreeMapperSupport semanticTreeMapperSupport;

		/**
		 * Create the shell.
		 * @param display
		 * @param sample the selected TreeItem
		 */
	  public MyUpdateNodeStructureEditor(Shell myShell, MyTreeItem sample) {
		  super();
//		myShell = parent;
//		myLeftTreeItem = sample;
//		myRightTreeItem = sample;
//		produceLayout(display);
	}

	/**
	 * Create the shell.
	 * @param display
	 * @param sample the selected TreeItem
	 */
	public static void OpenEditor(Shell myShell, MyTreeItem sample){
		
		shell = myShell;
		myLeftTreeItem = sample;	
		shell.setText("Parameter neu zuordnen");
		
		if (searchForIdenticalNodes()) {
			//linker Baum enthält die Vorlage
			//rechter Baum enthält einen zu ändernden Knoten
			myRightTreeItem = foundTreeItems.get(0);	
			leftList = new  ArrayList<MyTreeItem>(1);
			rightList = new  ArrayList<MyTreeItem>(1);
			TreeTools.prepareNodeList(myLeftTreeItem, leftList );
			TreeTools.prepareNodeList(myRightTreeItem, rightList );
			
			prepareMapping(myLeftTreeItem, myRightTreeItem);	//bereitet die Mapping Tabelle vor
			
			LogfileView.log(MyUpdateNodeStructureEditor.class," öffne Editor");
			Composite composite = new Composite(shell, 0);
			
			produceLayoutNew(composite);

			shell.open();
			shell.pack();
		}else{
			MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.NO);
			String string = "Keinen identischen Knoten gefunden!";
			mb.setMessage(string);
		}

	}
	
	private static void produceLayoutNew(final Composite parent) {
		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		
		//top
		Composite topComposite = new Composite(composite, SWT.NONE);
		topComposite.setLayoutData(BorderLayout.NORTH);
		topComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		createFilterArea(topComposite);
		
		//bottom
		Composite bottomComposite = new Composite(composite, SWT.NONE);
		bottomComposite.setLayoutData(BorderLayout.SOUTH);
		bottomComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		createButtonsForButtonBar(bottomComposite);

		//middle
		middleComposite = new Composite(composite, SWT.NONE);
		middleComposite.setLayoutData(BorderLayout.CENTER);
		middleComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		createTreeArea(middleComposite);
		lbtMessage.setText("Anzahl gefundener Knoten: " + foundTreeItems.size());
		
		parent.pack();
		
	}
	
	
	

	private static void createTreeArea(Composite parent) {
		Color defaultColor = new Color(parent.getShell().getDisplay(), new RGB(247, 206, 206));
		Color selectedColor = new Color(parent.getShell().getDisplay(), new RGB(147, 86, 111));
		TreeMapperUIConfigProvider uiConfig = new TreeMapperUIConfigProvider(defaultColor, 3, selectedColor, 3);
		
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		semanticTreeMapperSupport = new MySemanticTreeMapperSupport(parent, myLeftTreeItem, myRightTreeItem);
		
		mapper = new MyTreeMapper<MyTreeItemBean, MyTreeItem, MyTreeItem>(parent,  semanticTreeMapperSupport, uiConfig);
		mapper.setContentProviders(new KnotenContentProvider(), new KnotenContentProvider());
		mapper.setLabelProviders(new KnotenLabelProvider(), new KnotenLabelProvider());
		
		leftListItem = new  ArrayList<MyTreeItem>(5);
		leftListItem.add(0,myLeftTreeItem);
		rightListItem = new  ArrayList<MyTreeItem>(5);
		rightListItem.add(0,myRightTreeItem);
		mapper.setInput(leftListItem, rightListItem, mappings);
	}
	
	
	
	/**
	 * Bearbeitet alle identischen Knoten
	 */
	private static void updateNodeStructure(){
		// foundTreeItems enthält alle gefundenen Knoten ohne das Original
		
		
		if (foundTreeItems != null) {
			searchForIdenticalStructure(myRightTreeItem);
			TreeTools.CopyMyTreeItem(myLeftTreeItem);
			
			LogfileView.log(MyUpdateNodeStructureEditor.class," Anzahl der gefundenen Knoten: " + foundTreeItems.size());
			LogfileView.log(MyUpdateNodeStructureEditor.class," Anzahl der identischen Strukturen: " + foundIdenticalTreeItems.size());
			if (foundIdenticalTreeItems.size() != 0) {
				for (int n = 0; n < foundIdenticalTreeItems.size(); n++) {	
					//ergänzt die Indixes
					leftList = new  ArrayList<MyTreeItem>(5);
					leftList.clear();
					rightList = new  ArrayList<MyTreeItem>(5);
					rightList.clear();
					TreeTools.prepareNodeList(myLeftTreeItem, leftList );
					TreeTools.prepareNodeList(myRightTreeItem, rightList );
					updateMapping();	
					//erzeugt eine Kopie der Vorlage
					MyTreeItem myNewTreeItem = TreeTools.PasteMyTreeItem(myLeftTreeItem);
					//überträgt die Variablenwerte
					transferParameters(myNewTreeItem, foundIdenticalTreeItems.get(n));
					// tauscht das alte Kind gegen das neue Kind aus
					foundIdenticalTreeItems.get(n).getParent().replaceMyTreeItem(foundIdenticalTreeItems.get(n), myNewTreeItem);
					//Daten speichern 
					UpdateNodeCommand.update(foundIdenticalTreeItems.get(n).getParent());

					//wenn noch ein Item übrig, neue mapping erstellen
					lbtMessage.setText("Bearbeite Knoten: " + n);
					if (n == foundIdenticalTreeItems.size()-1){
						lbtMessage.setText("Hole nächsten identischen Knoten: ");
						switchMapping(foundIdenticalTreeItems.get(n));
						
					}
					
				}
			}
			
			reduceFoundTreeItems();
			if (foundTreeItems.size() == 0){
				lbtMessage.setText("Alle Knoten bearbeitet.");
				buttonOK.setEnabled(false);
			}else{
				lbtMessage.setText("Noch vorhandene Knoten mit anderer Struktur: " + foundTreeItems.size());
				rightListItem = new  ArrayList<MyTreeItem>(5);
				myRightTreeItem = foundTreeItems.get(0);
				rightListItem.add(0,myRightTreeItem);
				rightList = new  ArrayList<MyTreeItem>(5);
				rightList.clear();
				TreeTools.prepareNodeList(myRightTreeItem, rightList );
				prepareMapping(myLeftTreeItem, myRightTreeItem);	//bereitet die Mapping Tabelle vor
				mapper.setInput(leftListItem, rightListItem, mappings);
			}
		}
	}
	
	/**
	 * Bearbeitet die foundTreeItems Liste und entfernt die bearbeiteten Knoten
	 * Tauscht den myRightTreeItem gegen das neue Item
	 * Setzt die mappings Liste zurück
	 */
	private static void reduceFoundTreeItems(){
		if (foundTreeItems != null) {
			if (foundIdenticalTreeItems != null) {
				for (int n = 0; n < foundIdenticalTreeItems.size(); n++) {
					foundTreeItems.remove(foundIdenticalTreeItems.get(n));
				}
			}
		}
		LogfileView.log(MyUpdateNodeStructureEditor.class," Entfernte bearbeitete Knoten: " + foundIdenticalTreeItems.size());
		if (foundTreeItems.size() > 0) {
			myRightTreeItem = foundTreeItems.get(0);
			mappings.clear();
		}
	}
	
	/**
	 * Bearbeitet die mappings Liste und tauscht die alten Items gegen die neuen Items
	 * Bearbeitet die rightList Liste und tauscht die alten Items gegen die neuen Items
	 * Tauscht den myRightTreeItem gegen das neue Item
	 * @param myTreeItem Das neue Item
	 */
	private static void switchMapping(MyTreeItem myTreeItem){
		myRightTreeItem = myTreeItem;
		rightList.clear();
		rightList = TreeTools.prepareNodeList(myTreeItem, rightList);
		List<MyTreeItemBean> tempMappings = new ArrayList<MyTreeItemBean>();
		showMapping();
		if (!mappings.isEmpty()) {
			for (int n = 0; n < mappings.size(); n++) {
				//durchlaufe die Liste der Zuweisungen. Die linke Seite bleibt unverändert, die rechte Seite wird aktualisiert
				MyTreeItemBean myTreeItemBean = new MyTreeItemBean(mappings.get(n).left, rightList.get(mappings.get(n).posRight) );
				myTreeItemBean.posLeft = mappings.get(n).posLeft;
				myTreeItemBean.posRight = mappings.get(n).posRight;
				tempMappings.add(myTreeItemBean);
	
			}
			showMapping();
			mappings = tempMappings;
		}
		
	}
	
	/**
	 * Listet die rightList und leftList auf
	 */
	private static void showList(){
		LogfileView.log(MyUpdateNodeStructureEditor.class," Listen: ");
		String string1;
		for (int n = 0; n < leftList.size(); n++) {		
			string1 = leftList.get(n).getParameter() + " " +
					leftList.get(n).getBezeichnung() + " " +
					leftList.get(n).getVariablenWert();
			LogfileView.log(MyUpdateNodeStructureEditor.class,"linke Liste: " + n + " " + string1);		
		}
		for (int n = 0; n < rightList.size(); n++) {		
			string1 = rightList.get(n).getParameter() + " " +
					rightList.get(n).getBezeichnung() + " " +
					rightList.get(n).getVariablenWert();
			LogfileView.log(MyUpdateNodeStructureEditor.class,"rechte Liste: " + n + " " + string1);		
		}
	}
	
	/**
	 * Listet die mappings auf
	 */
	private static void showMapping(){
		LogfileView.log(MyUpdateNodeStructureEditor.class," Mapping Liste: ");
		String string1, string2;
		for (int n = 0; n < mappings.size(); n++) {		
			string1 = mappings.get(n).posLeft + " " +
					mappings.get(n).left.getParameter() + " " +
					mappings.get(n).left.getBezeichnung() + " " +
					mappings.get(n).left.getVariablenWert();
			
			string2 = mappings.get(n).posRight + " " +
					mappings.get(n).right.getParameter() + " " +
					mappings.get(n).right.getBezeichnung() + " " +
					mappings.get(n).right.getVariablenWert();
			LogfileView.log(MyUpdateNodeStructureEditor.class,"Eintrag: " + n + " " + string1 + " " +string2);
			
		}
	}
	/**
	 * Bearbeitet die mappings Liste und ergänzt die Positionen (Index) der zugewiesenen Knoten
	 */
	private static void updateMapping(){
		showMapping();
		if (!mappings.isEmpty()) {
			for (int n = 0; n < mappings.size(); n++) {						//durchlaufe die Liste der Zuweisungen
				if(leftList.contains(mappings.get(n).left)){				//linker Knoten vorhanden
					if(rightList.contains(mappings.get(n).right)){			//rechter Knoten vorhanden
																			//Indexes ergänzen
						mappings.get(n).posLeft = leftList.indexOf(mappings.get(n).left);
						mappings.get(n).posRight = rightList.indexOf(mappings.get(n).right);
					}
				}
			}
		}
		showMapping();
	}
	
	/**
	 * Vergleicht zwei Knotenlisten miteinander und versucht die Knoten über eine Mappingliste zu verknüpfen.
	 * @param leftList Die Vorlage
	 * @param rightList Die anzupassende Liste
	 */
	private static void prepareMapping(MyTreeItem myLeftTreeItem, MyTreeItem myRightTreeItem){
		mappings.clear();
		
		//erzeuge linke Liste
		ArrayList<MyTreeItem> left_List = new  ArrayList<MyTreeItem>(5);
		LogfileView.log(MyUpdateNodeStructureEditor.class," Linke Liste erzeugen ");
		left_List = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(myLeftTreeItem, left_List );
		
		//erzeuge rechte Liste
		ArrayList<MyTreeItem> right_List = new  ArrayList<MyTreeItem>(5);
		LogfileView.log(MyUpdateNodeStructureEditor.class," Rechte Liste erzeugen ");
		right_List = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(myRightTreeItem, right_List );
		
		// 1. Stufe : nur eineindeutige Knoten werden zugewiesen
		compareTreeSingle(left_List, right_List, mappings);

	
	}
	

	
	/**
	 * Vergleich zweier Bäume.
	 * Vergleicht die Knoten zweier Listen miteinander.
	 * Zuordnung 1:1.
	 * 
	 * Liste A enthält nur einen Parameter gleicher Id
	 * Liste B enthält nur einen Parameter gleicher Id
	 * Liste M enthält paarweise die gefundenen Zuordnungen
	 * @param listA Der neue Baum
	 * @param listB Der vorhandene Baum
	 * @param listM Die Mappingliste
	 * @return listM Die Mappingliste
	 */
	private static List<MyTreeItemBean>  compareTreeSingle(List<MyTreeItem> listA, List<MyTreeItem> listB, List<MyTreeItemBean> listM){
		LogfileView.log(MyUpdateNodeStructureEditor.class," compareTreeSingle ");
		int countA = 0;
		int countB = 0;
		if (listA != null && listB != null) {	//beide Listen nicht leer
			for (int i = 0; i < listA.size(); i++) {			
				countA = countList(listA.get(i), listA);
				countB = countList(listA.get(i), listB);
				if (countA == 1 && countB == 1){		//beide Listen enthalten nur einmal den gleichen Parameter
					int index = TreeTools.findParameter(listA.get(i), listB);
					if (index > -1){
						MyTreeItemBean myTreeItemBean = new MyTreeItemBean(listA.get(i), listB.get(index));
						if(!listM.contains(myTreeItemBean)){	//Bean schon vorhanden?
							LogfileView.log(MyUpdateNodeStructureEditor.class," Mappingliste erweitert " 
									+ myTreeItemBean.left.getParameter() +"  "
									+ myTreeItemBean.right.getParameter());
							listM.add(myTreeItemBean);			//Bean in Liste eintragen
						}	
					}
				}
			}
		}
		showMapping();
		return listM;
	}
	

	

	
	/**
	 * Zählt die Anzahl der gleichen Parameter Ids in einer Liste
	 * @param sample Der Vergleichsknoten
	 * @param list Die Vorlageliste
	 * @return n Die Anzahl gleicher Knoten
	 */
	private static int  countList(MyTreeItem sample,List<MyTreeItem> list){
		int n = 0;
		
		if (list!= null) {
			for (int j = 0; j < list.size(); j++) {
				if(list.get(j).getParameter().equals(sample.getParameter())) n++;
			}
		}
		LogfileView.log(MyUpdateNodeStructureEditor.class," Anzahl gleicher Parameter: " + n);
		return n;
		
	}
	

	
	/**
	 * Durchsucht die foundTreeItems Liste nach identischen Strukturen
	 * und updated die foundIdenticalTreeItems Liste
	 * @param myTreeItem gibt die Struktur vor
	 */
	private static void searchForIdenticalStructure(MyTreeItem myTreeItem){
		ArrayList<MyTreeItem> list = new  ArrayList<MyTreeItem>(5);
		ArrayList<MyTreeItem> newList = new  ArrayList<MyTreeItem>(5);
		list = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(myTreeItem, list );
		foundIdenticalTreeItems.clear();
		boolean flag = true;
		
		if (!foundTreeItems.isEmpty()) {
			for (int n = 0; n < foundTreeItems.size(); n++) {
				newList.clear();
				newList = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(foundTreeItems.get(n), newList );
				flag = TreeTools.compareList(list, newList);
				
				if (flag){
					foundIdenticalTreeItems.add(foundTreeItems.get(n));
				}
			}
		}
		LogfileView.log(MyUpdateNodeStructureEditor.class," Anzahl gleicher Strukturen: " + foundIdenticalTreeItems.size());
	}
	
	
	/**
	 * Durchsucht das Projekt abwärts nach anderen identischen ParameterIDs.
	 * Erzeugt eine Liste der gefundenen Parameter.
	 * @return flag = false : keine Knoten gefunden
	 */
	private static boolean searchForIdenticalNodes(){
		boolean flag = false;
		// 1. Stufe: rekursives Durchlaufen der Knotenstruktur
		MyTreeItem project = TreeTools.searchForProjekt(myLeftTreeItem);
		foundTreeItems.clear();
		foundTreeItems = TreeTools.findAllMyTreeItemByID(foundTreeItems, project, myLeftTreeItem.getParameter());
		if (foundTreeItems != null) {
			foundTreeItems.remove(myLeftTreeItem); 		//entferne eigene Knoten
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Überträgt die Parameter an den neuen Knoten
	 * @author Burkhard Pöhler
	 * @param myRightTreeItem der zu kopierende ursprüngliche Konten
	 * @param myLeftTreeItem der neue Knoten
	 */
	private static void transferParameters( MyTreeItem myLeftTreeItem , MyTreeItem myRightTreeItem){
		
		//erzeuge linke Liste
		ArrayList<MyTreeItem> left_List = new  ArrayList<MyTreeItem>(5);
		LogfileView.log(MyUpdateNodeStructureEditor.class," Linke Liste erzeugen ");
		left_List = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(myLeftTreeItem, left_List );
		
		//erzeuge rechte Liste
		ArrayList<MyTreeItem> right_List = new  ArrayList<MyTreeItem>(5);
		LogfileView.log(MyUpdateNodeStructureEditor.class," Rechte Liste erzeugen ");
		right_List = (ArrayList<MyTreeItem>) TreeTools.prepareNodeList(myRightTreeItem, right_List );
		
		if (!left_List.isEmpty()){
			if (!right_List.isEmpty()){
				if (!mappings.isEmpty()){
					for (int n = 0; n < mappings.size(); n++) {
						left_List.get(mappings.get(n).posLeft).setVariablenWert(right_List.get(mappings.get(n).posRight).getVariablenWert());
						left_List.get(mappings.get(n).posLeft).setVariablenEinheit(right_List.get(mappings.get(n).posRight).getVariablenEinheit());
						left_List.get(mappings.get(n).posLeft).setVariablentyp(right_List.get(mappings.get(n).posRight).getVariablentyp());
						String string = "Übertrage Parameter: " 
								+ right_List.get(mappings.get(n).posRight).getVariablenWert() + " "
								+ right_List.get(mappings.get(n).posRight).getVariablenEinheit() + " "
								+ right_List.get(mappings.get(n).posRight).getVariablentyp() + " "
								+ " nach: "
								+ left_List.get(mappings.get(n).posLeft).getVariablenWert() + " "
								+ left_List.get(mappings.get(n).posLeft).getVariablenEinheit() + " "
								+ left_List.get(mappings.get(n).posLeft).getVariablentyp() + " "
								+ " ";
								
								LogfileView.log(MyUpdateNodeStructureEditor.class,string);
					}
				}
			}
		}
	}
	

	private static void createFilterArea(Composite container) {
			container.setLayout(new FillLayout(SWT.V_SCROLL));
			control = new SashForm(container, SWT.VERTICAL);
			control.setLayout(new FillLayout());
			
			///test
			
			
//			Label lbtFilterName = new Label(control, SWT.NONE);
//			lbtFilterName.setText("Filter");
//
//		    combobox = new Combo(control, SWT.BORDER | SWT.READ_ONLY);	    
//		    combobox.add("Alle Verbindungen");
//		    combobox.add("Nur erkannte Verbindungen");
//		    combobox.add("Fehlende Verbindungen");
//		    combobox.add("Neue Parameter");
//		    combobox.select(0);
//		    selection = 0;
//		    
//		    combobox.addSelectionListener(new SelectionListener() {
//		
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					if (combobox.getText().equals("Alle Verbindungen")) {
//						System.out.println("Alle Verbindungen" );
//						selection = 0;
//					}else if (combobox.getText().equals("Nur erkannte Verbindungen")) {
//						System.out.println("Nur erkannte Verbindungen" );
//						selection = 1;
//					}else if (combobox.getText().equals("Fehlende Verbindungen")) {
//						System.out.println("Fehlende Verbindungen" );
//						selection = 2;
//					}else{
//						System.out.println("Neue Parameter" );
//						selection = 3;
//					}	
//					
//					selectionChanged(selection);
//				}
//		
//				private void selectionChanged(int selection) {
//					// TODO Auto-generated method stub
//					
//				}
//		
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//					// TODO Auto-generated method stub
//					
//				}
//		      });
		    
			 lbtMessage = new Label(control, SWT.NONE);
			 lbtMessage.setSize(20, 200);
			 lbtMessage.setText("Anzahl gefundener Knoten: " + foundTreeItems.size());
			 control2 = new SashForm(container, SWT.HORIZONTAL);
			 control2.setLayout(new FillLayout());

			 
			 Label lbtText1 = new Label(control2, SWT.NONE);
			 lbtText1.setText("Neue Struktur");
			 Label lbtText2 = new Label(control2, SWT.NONE);
			 lbtText2.setText("Verlinkungen");
			 Label lbtText3 = new Label(control2, SWT.NONE);
			 lbtText3.setText("Alte Struktur");
		 }

	private static void createButtonsForButtonBar(Composite parent) {

		buttonOK = new Button(parent, SWT.PUSH);
		buttonOK.setText("Übernehmen");
		
		buttonDeleteLink = new Button(parent, SWT.PUSH);
		buttonDeleteLink.setText("Verbindung löschen");
		buttonDeleteLink.setEnabled(deleteLink);
		
		buttonShowLink = new Button(parent, SWT.PUSH);
		buttonShowLink.setText("Verbindung herstellen");
		buttonShowLink.setEnabled(showLink);
		
		Button buttonCancel = new Button(parent, SWT.PUSH);
		buttonCancel.setText("Beenden");
	
		buttonOK.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateNodeStructure();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateNodeStructure();
			}
		});
		
		buttonCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			     // Set return code
				shell.close();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	
		buttonDeleteLink.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				LogfileView.log(MyTreeMapper.class," Verbindung löschen: " 
						+ myActiveTreeItemBean.left.getBezeichnung() + " <> "
						+ myActiveTreeItemBean.right.getBezeichnung());
				deleteMappingItem(myActiveTreeItemBean);
				leftListItem.clear();
				leftListItem.add(leftList.get(0));
				rightListItem.clear();
				rightListItem.add(rightList.get(0));
				mapper.setInput(leftListItem, rightListItem, mappings);
				buttonDeleteLink.setEnabled(false);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				LogfileView.log(MyTreeMapper.class," Verbindung löschen: " 
						+ myActiveTreeItemBean.left.getBezeichnung() + " <> "
						+ myActiveTreeItemBean.right.getBezeichnung());
				deleteMappingItem(myActiveTreeItemBean);
				leftListItem.clear();
				leftListItem.add(leftList.get(0));
				rightListItem.clear();
				rightListItem.add(rightList.get(0));
				mapper.setInput(leftListItem, rightListItem, mappings);
				buttonDeleteLink.setEnabled(false);
			}
		});
		
		
		buttonShowLink.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				LogfileView.log(MyTreeMapper.class," Verbindung herstellen: " 
						+ myActiveTreeItemBean.left.getBezeichnung() + " <> "
						+ myActiveTreeItemBean.right.getBezeichnung());

				mappings.add(myActiveTreeItemBean);				
				mapper.setInput(leftListItem, rightListItem, mappings);
				buttonShowLink.setEnabled(false);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				LogfileView.log(MyTreeMapper.class," Verbindung herstellen: " 
						+ myActiveTreeItemBean.left.getBezeichnung() + " <> "
						+ myActiveTreeItemBean.right.getBezeichnung());
				mappings.add(myActiveTreeItemBean);			
				mapper.setInput(leftListItem, rightListItem, mappings);
				buttonShowLink.setEnabled(false);
			}
		});
		
		
		
	}
	
	/**
	 * Sucht in der mapping Liste eine Verbindung und löscht diese.
	 * @param myTreeItem Das zu löschende Item
	 */
	private static void deleteMappingItem(MyTreeItemBean myTreeItemBean){
		LogfileView.log(MyTreeMapper.class," Mapping vor dem löschen");
		showMapping();
		for (int n = 0; n < mappings.size(); n++) {
			if (mappings.get(n).left.equals(myTreeItemBean.left)){
				LogfileView.log(MyTreeMapper.class," lösche Bean" +  myTreeItemBean.left.getParameter() + "  " + myTreeItemBean.right.getParameter());
				mappings.remove(n);
				break;
			}
		}
		LogfileView.log(MyTreeMapper.class," Mapping nach dem löschen");
		showMapping();
	}
	
	
	public static void updateDeleteButton(boolean flag, MyTreeItem myLeftTreeItem, MyTreeItem myRightTreeItem){
		buttonDeleteLink.setEnabled(flag);
		myActiveTreeItemBean = new MyTreeItemBean(myLeftTreeItem, myRightTreeItem); 
	}
	
	public static void updateShowButton(boolean flag, MyTreeItem myLeftTreeItem, MyTreeItem myRightTreeItem){
		buttonShowLink.setEnabled(flag);
		myActiveTreeItemBean = new MyTreeItemBean(myLeftTreeItem, myRightTreeItem); 
	}
	
	private static class KnotenLabelProvider implements ILabelProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image getImage(Object element) {
			String dateiname = null;	// hole den Namen des Bildes
			Image image = null;
			if (element instanceof MyTreeItem){
				MyTreeItem myTreeItem = (MyTreeItem) element;
				image = MyImageHandler.getImage(myTreeItem.getIconDateiname());
			}else {
				image = MyImageHandler.getImage(dateiname);
			}
			return image;	
		}

		@Override
		public String getText(Object element) {
			MyTreeItem myTreeItem =(MyTreeItem) element;
			String string = myTreeItem.getBezeichnung() + " :" +
					myTreeItem.getVariablenWert() + myTreeItem.getVariablenEinheit();
			return string;
		}
		
		
	}
	 private static class KnotenTableLabelProvider implements ITableLabelProvider {
			public Image getColumnImage(Object element, int columnIndex) {
				if (element instanceof MyTreeItem){
						return getImage(element,  columnIndex);
				}
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				MyTreeItem myTreeItem =(MyTreeItem) element;
				String string = myTreeItem.getBezeichnung() + " :" +
						myTreeItem.getVariablenWert() + myTreeItem.getVariablenEinheit();
				return string;
			}
	
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
			}
	
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
	
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			public  Image getImage(Object element, int columnIndex) {
				String dateiname = null;	// hole den Namen des Bildes
				if (element instanceof MyTreeItem){
					MyTreeItem myTreeItem = (MyTreeItem) element;
					Image image =  MyImageHandler.getImage(myTreeItem.getIconDateiname());
				}		
				return MyImageHandler.getImage(dateiname);		
			}
		}

	private static class KnotenContentProvider implements ITreeContentProvider{
	
			private TreeViewer viewer;
	
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MyTreeItem){
					MyTreeItem myTreeItem = (MyTreeItem)parentElement;
					
					Object[] o = null;
					if (myTreeItem.getChildren() != null) {
						if (!myTreeItem.isWurzel()) {	
						}
						o = myTreeItem.getChildren().toArray();
					}
					
					return o;
				}
				return new Object[0];
			}
			
			@Override
			public Object getParent(Object element) {
				if (element instanceof MyTreeItem){
				 	return ((MyTreeItem)element).getParent();
				}
				return null;
			}
	
			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof MyTreeItem){
					MyTreeItem myTreeItem =(MyTreeItem) element;
					if (myTreeItem.isEigenschaft()){			//Alle Eigenschaften werden angezeigt
						return myTreeItem.isHasChildren();
					}else if(myTreeItem.isParameter()){			//Nur die Parameter dürfen sichtbar sein
						if (myTreeItem.isHasChildren()) {
							for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
								if(myTreeItem.getChildren().get(i).isEigenschaft()){	//Kinder sind Eigenschaften und werden nicht dargestellt
									return false;
								}
							}
						}
					}
					
					return myTreeItem.isHasChildren();
				}
				return false;
			}
			
			@Override
			public Object[] getElements(Object Liste) {
				return ((List)Liste).toArray();
			}
	
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//				System.out.println("inputChanged" );
	//			this.viewer = (TreeViewer)viewer;
				if (oldInput != null) {
					for (Iterator iterator = ((List) oldInput).iterator(); iterator
							.hasNext();) {
						MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
					}
				}
				if (newInput != null) {
					for (Iterator iterator = ((List) newInput).iterator(); iterator
							.hasNext();) {					
						MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
					}
				}
			}
		}   
}
