/**
 * 
 */
package com.hydra.project.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.hydra.project.database.DBSettingsTools;
import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.hydra.project.command.Perspective_ProjectCalendarCommand;
import com.hydra.project.command.UpdateNodeCommand;
import com.hydra.project.database.DBTools;
import com.hydra.project.editors.MyUpdateNodeStructureEditor;
import com.hydra.project.exceptions.NoChildFoundException;
import com.hydra.project.functions.MyTreeItemSettings;
import com.hydra.project.parts.BaumView;
import com.hydra.project.parts.LogfileView;

import org.eclipse.nebula.widgets.treemapper.*;
import org.eclipse.swt.widgets.Shell;


/**
 * @author Burkhard Pöhler							
 */
public class TreeTools {
	public static String BASISDATENBANK = "C:\\Hydra\\Basisdaten.DB4O";
	public static String PROJEKTDATENBANK = "C:\\Hydra\\Projektdaten.DB4O";
	public static String SETTINGSDATENBANK = "C:\\Hydra\\Settings.DB4O";
	private static int dateiIndex=0;							//zählt die offenen Datenbanken
	private static boolean zwischenspeicher=false;				//zeigt an, ob ein eine Kopiervorlage in der tmp Datenbank liegt
	public static MySettings mySettings = new MySettings();
	
		
	@Inject
	public static MyTreeItem myTopTreeItem = new MyTreeItem();	//Obersten Knoten erzeugen
	public static MyTreeItem myBasisTreeItem = new MyTreeItem();	//Basis Knoten erzeugen
	private static MyTreeItem myTreeItemCopy=new MyTreeItem()	;		//dient zum kopieren von Teilbäumen
	private static MyTreeItem projektKnoten = new MyTreeItem();
	
	
	
	/**
	 * @author Burkhard Pöhler
	 * Diese Methode öffnet alle nötigen Datenbanken und läd die vorliegenden Bäume in einen Hauptknoten
	 * +Wurzel										ist der höchste Knoten im Baum
	 * 	+Basis										ist der höchste Knoten in der Basisdatenbank
	 * 		+Parameter								ist der oberste Knoten aller Parameter
	 * 		+Eingeschaften							ist der oberste Knoten aller Eigenschaften
	 * 	+Projektbaum								ist der oberste Knoten aller Projekte
	 * 		+Projekt 1								ist ein Projekt von vielen	
	 * 			+Vorlagen							sollte immer vorhanden sein
	 * 
	 */	
	public static MyTreeItem InitialisiereBaum() {
		//Benutzer ermitteln
		mySettings = DBSettingsTools.readSettingsDB();
		
		//Obersten Knoten für den tree definieren und setzen
		myTopTreeItem.setBezeichnung("Wurzel");
		myTopTreeItem.setBeschreibung("Oberste Wurzel");
		myTopTreeItem.setObersterKnoten(false);				//hat keine eigene Datenbank
		myTopTreeItem.setWurzel(true);
		myTopTreeItem.setDbIndex(0);
		myTopTreeItem.setDatenbankName("leer");
		dateiIndex=0;
		myTopTreeItem.setBezeichnung("Oberste Wurzel");
		myTopTreeItem.setParameter("Wurzel");
		myTopTreeItem.setVariablenWert("Oberste Wurzel");
		myTopTreeItem.setIconDateiname("folder-open.png");
		myTopTreeItem.setUuid("wurzel");
		myTopTreeItem.setStrukturknoten(true);
		myTopTreeItem.setParentUUID(UUID.randomUUID().toString()); //nur der Wurzel bekommt eine vorgegebene ID
		
		//prüfen, ob Basisdatenbank existiert. Wenn nicht, wird neue angelegt
		File fileBasisdatenbank = new File(BASISDATENBANK);
		if (!fileBasisdatenbank.exists())BasisdatenbankAnlegen();
		MyTreeItem dummyMyTreeItem2 = new MyTreeItem();
		dummyMyTreeItem2.setDbIndex(1);
		dummyMyTreeItem2.setDatenbankName(BASISDATENBANK);
		dummyMyTreeItem2.setObersterKnoten(true);
		dateiIndex=1;
		dummyMyTreeItem2.setIndex(0);
		dummyMyTreeItem2.setUuid("dummy2");
		//Obersten Knoten mit Basisdatenbank füllen
		myBasisTreeItem = DBTools.dateiOeffnen(dummyMyTreeItem2);
		myTopTreeItem.setChild(myBasisTreeItem);
//		myTopTreeItem.setChild(DBTools.serverDateiOeffnen(dummyMyTreeItem2));
		//Nutzerspezifische Daten laden
//		mySettings = SettingsdatenbankLesen(); //fehlerhafte Funktion
//		mySettings = new MySettings();
		mySettings.clearProjectList();
		// alle zuletzt geöffneten Projekte laden
		myTopTreeItem.addChild(addAllProjects());
		addNewEmptyFile("C:\\Hydra\\Neues Projekt.DB4O");
		addParentUUID(myTopTreeItem);			//repariert eventuelle Fehler in der Baumstruktur
		addParent(myTopTreeItem);				//repariert eventuelle Fehler in der Baumstruktur
		updateValues(myTopTreeItem);			//aktualisiert Werte und ergänzt fehlende Werte
		return myTopTreeItem;
		
	}
	
	/**
	 * Schließt alle Datenbanken
	 */
	public static  void alleDateienSchliessen(){
		  for (int i = 1; i < dateiIndex+1; i++) {
			  MyTreeItem myTreeItem = new MyTreeItem();
			  myTreeItem.setDbIndex(i);
			  DBTools.dateiSchliessen(myTreeItem);
		}
	}   
	
	/**
	 * @author Burkhard Pöhler
	 * erstellt ein Standardprojekt und
	 * erzeugt einen neuen Eintrag in der aktiven Projektliste
	 * @param	project der komplette Dateipfad
	 */
	public static void addNewEmptyFile(String project){
		MyTreeItem myTreeItem = NeuesProjektAnlegen(project);	//Musterprojekt anlegen
		myTreeItem.setDatenbankName(project);
		dateiIndex = dateiIndex + 1;
		myTreeItem.setDbIndex(dateiIndex);
		mySettings.addProject(project);							// der Liste anfügen
//		SettingdatenbankSchreiben();							//sichern  !!!fehlerhafte Funktion
		addProjectToProjecttree(myTreeItem);
	}
	

	
	/**
	 * @author Burkhard Pöhler
	 * erzeugt einen neuen Eintrag in der aktiven Projektliste
	 * @param	myTreeItem das einzufügende Projekt
	 */
	public static void addProjectToProjecttree(MyTreeItem myTreeItem){
		// Projektbaum suchen und neuen Knoten einfügen
		for (int i = 0; i <myTopTreeItem.children.size(); i++) {
	   		if(myTopTreeItem.children.get(i).isProjektBaum()){
	   			MyTreeItem projektbaum = myTopTreeItem.children.get(i);
	   			MyTreeItem neuesKind = DBTools.dateiOeffnen(myTreeItem);
//	   			MyTreeItem neuesKind = DBTools.serverDateiOeffnen(myTreeItem);
	   			projektbaum.addChild(neuesKind);
            	UpdateNodeCommand.update(projektbaum);
        		UpdateNodeCommand.setFocus(neuesKind);
        		MyViewers.setViewers(neuesKind);						//passenden Viewer aufrufen
    		};	    		
		}
	}
	

	
	/**
	 * @author Burkhard Pöhler
	 * öffnet ein vorhandenes Projekt und
	 * erzeugt einen neuen Eintrag in der aktiven Projektliste
	 * @param	project der komplette Dateipfad
	 */
	public static void addNewFile(String project){
		MyTreeItem myTreeItem = new MyTreeItem();	
		myTreeItem.setDatenbankName(project);
		dateiIndex = dateiIndex + 1;
		myTreeItem.setDbIndex(dateiIndex);
		mySettings.addProject(project);							// der Liste anfügen
		DBSettingsTools.writeSettingsDB(mySettings);			//Daten sichern
		addProjectToProjecttree(myTreeItem);
//		MyViewers.setViewers(myTreeItem);						//passenden Viewer aufrufen
	}
	
	/**
	 * @author Burkhard Pöhler
	 * schliesst ein vorhandenes Projekt und
	 * aktualisiert die aktive Projektliste
	 * @param	myTreeItem das zu entfernende Kind
	 */
	public static void removeFile(MyTreeItem myTreeItem){
		myTreeItem.getParent().removeChild(myTreeItem);			//Kind entfernen
		mySettings.removeProject(myTreeItem.getVariablenWert());// aus der Liste entfernen
//		SettingdatenbankSchreiben();							//sichern  !!!fehlerhafte Funktion
      	DBTools.dateiSchliessen(myTreeItem);
		UpdateNodeCommand.update(myTreeItem.getParent());
    	UpdateNodeCommand.setFocus(myTreeItem.getParent());
	}
	
	
	/**
	 * @author Burkhard Pöhler
	 * erzeugt alle ursprünglich geöffneten Projekte
	 * @return	projektKnoten der Projektknoten mit allen Unterprojekten
	 */
	public static MyTreeItem addAllProjects(){
		// erster Knoten für Projekte

		projektKnoten.setBeschreibung("Projekte");
		projektKnoten.setDatenbankName("leer");
		projektKnoten.setDbIndex(0);			//Projektdatenbank hat immer Index 0
//		projektKnoten.setParent(null);
		projektKnoten.setObersterKnoten(true);
		projektKnoten.setIsProjektBaum(true);
		projektKnoten.setBezeichnung("Projekte");
		projektKnoten.setParameter("Projekte");
		projektKnoten.setIconDateiname("001_001_001.gif");
		projektKnoten.setIsParameter(false);
		projektKnoten.setVariablenWert("variablenWert");
		projektKnoten.setHasPreChild(false);
		projektKnoten.setHasPostChild(false);
		projektKnoten.setHasChildren(false);
		projektKnoten.setUuid("projekte");
		projektKnoten.setStrukturknoten(true);
		
		// Projekte ergänzen
		for (int i = 0; i < mySettings.projects.size(); i++) { 
			dateiIndex = dateiIndex +1;
			MyTreeItem dummyMyTreeItem1 = new MyTreeItem();
			dummyMyTreeItem1.setDatenbankName(mySettings.projects.get(i).toString());
			dummyMyTreeItem1.setBezeichnung("Projekt" + i);
			dummyMyTreeItem1.setObersterKnoten(true);
			dummyMyTreeItem1.setDbIndex(dateiIndex);
			dummyMyTreeItem1.setIconDateiname("folders-stack.png");
			projektKnoten.addChild(DBTools.dateiOeffnen(dummyMyTreeItem1));
//			projektKnoten.addChild(DBTools.serverDateiOeffnen(dummyMyTreeItem1));
		}
		return projektKnoten;
	}
	
	/**
	 * 
	 * Rekursive suche nach der UUID
	 *<p>
	 * @param uUID die zu suchende UUID
	 * @return myTreeItem der Startknoten
	 * @author Burkhard Pöhler
	 */
	public static MyTreeItem searchForUUID(MyTreeItem myTreeItem, String uUID){
		MyTreeItem myFoundTreeItem = null;
		if (uUID != null) {
			if (!myTreeItem.getUuid().equals(uUID)) {
				if (myTreeItem.isHasChildren()) { //wenn Kinder vorhanden dann durchsuchen
					for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
						if (myTreeItem.getChildren().get(i).getUuid()
								.equals(uUID)) {
							myFoundTreeItem = myTreeItem.getChildren().get(i);
							break;
						} else {
							myFoundTreeItem = searchForUUID(myTreeItem
									.getChildren().get(i), uUID);
						}
					}
				}
			}
		}
		return myFoundTreeItem;
	}
	
	/**
	 * Durchsucht den Baum abwärts nach einer ParameterID
	 * @param myTreeItem der Startknoten
	 * @param parameterID entweder "Parameter" oder "Eigenschaft"
	 * @return the myTreeItem oder Null wenn nicht gefunden
	 * (möglicherweise fehlerhaft, durchläuft nicht den gesamten Baum)
	 */
	public static MyTreeItem findMyTreeItem(MyTreeItem myTreeItem, String parameterID) {
		if(myTreeItem.isHasChildren()){			//wenn Kinder vorhanden dann durchsuchen
			for (int i=0; i<myTreeItem.getChildren().size();i++){				
				MyTreeItem myNewTreeItem = myTreeItem.getChildren().get(i);
				if (myNewTreeItem.isStrukturknoten()){					
					if (myNewTreeItem.getParameter().equals(parameterID)) {
						return myNewTreeItem;
					}else{
						myNewTreeItem = findMyTreeItem(myNewTreeItem, parameterID);			//rekursiv durchlaufen
						if (myNewTreeItem.getParameter().equals(parameterID)) {
							myTreeItem = myNewTreeItem;
							return myNewTreeItem;
						}
					}
				}
			}
		}
		return myTreeItem;
	}
	
	/**
	 * Durchsucht den Baum abwärts nach einer ParameterID.
	 * Bricht die Suche ab, wenn erfolgreich
	 * @param myTreeItem der Startknoten
	 * @param parameterID entweder "Parameter" oder "Eigenschaft"
	 * @return the myTreeItem oder Null wenn nicht gefunden
	 */
	public static MyTreeItem findMyTreeItemByID(MyTreeItem myTreeItem, String parameterID) {
		MyTreeItem foundTreeItem = null;
		if (parameterID != null) {
			if (myTreeItem != null) {
				if (!myTreeItem.getParameter().equals(parameterID)) {
					if (myTreeItem.isHasChildren()) { //wenn Kinder vorhanden dann durchsuchen
						for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
							foundTreeItem = findMyTreeItemByID(myTreeItem.getChildren().get(i), parameterID);
							if (foundTreeItem != null)
								break;
						}
					}
				} else {
					foundTreeItem = myTreeItem;
				}
			} 
		}
		return foundTreeItem;
	}
	
	/**
	 * Durchsucht den Baum abwärts nach einer ParameterID.
	 * Erzeugt eine Liste mit den gefundenen Knoten
	 * @param foundTreeItem Liste der gefundenen Knoten
	 * @param myTreeItem der Startknoten
	 * @param parameterID entweder "Parameter" oder "Eigenschaft"
	 * @return the myTreeItem oder Null wenn nicht gefunden
	 */
	public static List<MyTreeItem> findAllMyTreeItemByID(List<MyTreeItem> foundTreeItem, MyTreeItem myTreeItem, String parameterID) {
		
		if (myTreeItem.getParameter().equals(parameterID)){
			LogfileView.log(TreeTools.class, "Parameter gefunden: " + myTreeItem.getParameter());
			foundTreeItem.add(myTreeItem);
		}
		
		if(myTreeItem.isHasChildren()){			//wenn Kinder vorhanden dann durchsuchen
			for (int i=0; i<myTreeItem.getChildren().size();i++){	//Kind gefunden
				//weitersuchen in jedem Kind
				findAllMyTreeItemByID(foundTreeItem, myTreeItem.getChildren().get(i),parameterID );
			}
		}
		return foundTreeItem;
	}
	
	
	/**
	 * Durchsucht den Projektbaum abwärts nach einer ParameterID.
	 * Bringt die Eigenschaften der gefundenen Parameter auf den aktuellen Stand
	 * @param myTreeItem Der geänderte Parameter
	 */
	public static void updateParameter(MyTreeItem myTreeItem){
		for (int i = 0; i <myTopTreeItem.children.size(); i++) {
	   		if(myTopTreeItem.children.get(i).isProjektBaum()){
	   			List<MyTreeItem> foundTreeItem = new ArrayList<MyTreeItem>();
	   			foundTreeItem = findAllMyTreeItemByID(foundTreeItem, myTopTreeItem.children.get(i), myTreeItem.getParameter());
	   			if (foundTreeItem != null) {
					for (int n = 0; n < foundTreeItem.size(); n++) {
						MyTreeItem a = foundTreeItem.get(n);
						//Parameterwerte anpassen
						a.setAnzeigeText(myTreeItem.getAnzeigeText());
						a.setBeschreibung(myTreeItem.getBeschreibung());
						a.setBezeichnung(myTreeItem.getBezeichnung());
						a.setBildDateiname(myTreeItem.getBildDateiname());
						a.setBirtReport(myTreeItem.getBirtReport());
						a.setEditor(myTreeItem.getEditor());
						a.setToolTipText(myTreeItem.getToolTipText());
						a.setVariablentyp(myTreeItem.getVariablentyp());
						a.setIconDateiname(myTreeItem.getIconDateiname());
						UpdateNodeCommand.update(a.getParent());
					}
				}
	   			LogfileView.log(TreeTools.class, "Anzahl geänderter Parameter: " + foundTreeItem.size());
				break;
    		};	    		
		}
	}
	
	
	/**
	 * Durchsucht den Parameterbaum abwärts nach einer ParameterID.
	 * Bringt die Eigenschaften der gefundenen Parameter auf den aktuellen Stand
	 * @param myTreeItem Die geänderte Eigenschaft
	 */
	public static void updateEigenschaft(MyTreeItem myTreeItem){
		for (int i = 0; i <myTopTreeItem.children.size(); i++) {
	   		if(myTopTreeItem.children.get(i).isBasis()){
	   			List<MyTreeItem> foundTreeItem = new ArrayList<MyTreeItem>();
	   			foundTreeItem = findAllMyTreeItemByID(foundTreeItem, myTopTreeItem.children.get(i), myTreeItem.getParameter());
	   			if (foundTreeItem != null) {
					for (int n = 0; n < foundTreeItem.size(); n++) {
						MyTreeItem a = foundTreeItem.get(n);
						//Parameterwerte anpassen
						a.setAnzeigeText(myTreeItem.getAnzeigeText());
						a.setBeschreibung(myTreeItem.getBeschreibung());
						a.setBezeichnung(myTreeItem.getBezeichnung());
						a.setBildDateiname(myTreeItem.getBildDateiname());
						a.setBirtReport(myTreeItem.getBirtReport());
						a.setEditor(myTreeItem.getEditor());
						a.setToolTipText(myTreeItem.getToolTipText());
						a.setVariablentyp(myTreeItem.getVariablentyp());
						a.setIconDateiname(myTreeItem.getIconDateiname());
						UpdateNodeCommand.update(a.getParent());
					}
				}
	   			LogfileView.log(TreeTools.class, "Anzahl geänderter Eigenschaften: " + foundTreeItem.size());
				break;
    		};	    		
		}
	}
	
//	/**
//	 * Durchsucht das Projekt abwärts nach einer ParameterID.
//	 * Bringt die Struktur der gefundenen Parameter auf den aktuellen Stand
//	 * @param sample Der geänderte Knoten
//	 * @param shell Die Shell
//	 */
//	public static void updateNodeStructure(Shell shell, MyTreeItem sample){
//		
//		// 1. Stufe: rekursives Durchlaufen der Knotenstruktur
//		MyTreeItem project = TreeTools.searchForProjekt(sample);
//		List<MyTreeItem> foundTreeItem = new ArrayList<MyTreeItem>();
//		foundTreeItem = findAllMyTreeItemByID(foundTreeItem, project, sample.getParameter());
//		//Liste enthält auch den sample!
//		if (foundTreeItem != null) {
//			for (int n = 0; n < foundTreeItem.size(); n++) {
//				MyTreeItem original = foundTreeItem.get(n);
//				if (original != sample) {	//gefundener Knoten ist nicht identisch mit sample	
//					// 2. Stufe: sample kopieren
//					CopyMyTreeItem(sample);
//					MyTreeItem copy = PasteMyTreeItem(original);
//					
//					MyUpdateNodeStructureDialog myUpdateNodeStructureDialog = new MyUpdateNodeStructureDialog(shell, sample, copy);
//					int returnCode = myUpdateNodeStructureDialog.open();
//					
//					
//					
//					
//				}
//			}
//		}
		
//		// 1. Stufe: rekursives Durchlaufen der Knotenstruktur
//		MyTreeItem project = TreeTools.searchForProjekt(sample);
//		List<MyTreeItem> foundTreeItem = new ArrayList<MyTreeItem>();
//		foundTreeItem = findAllMyTreeItemByID(foundTreeItem, project, sample.getParameter());
//		//Liste enthält auch den sample!
//		if (foundTreeItem != null) {
//			for (int n = 0; n < foundTreeItem.size(); n++) {
//				MyTreeItem original = foundTreeItem.get(n);
//				if (original != sample) {	//gefundener Knoten ist nicht identisch mit sample	
//					// 2. Stufe: sample kopieren
//					CopyMyTreeItem(sample);
//					MyTreeItem copy = PasteMyTreeItem(original);
//					copy.setIndex(original.getIndex()); //hole die Position des Kindes
//					original.getParent().setChild(copy);//füge vor dem Kind ein. Damit existiert neu und alt
//					original.getParent().removeChild(original);//lösche altes Kind
//					// 3. Stufe: Parameterwerte übertragen
//					transferParameters(original, copy);
//					UpdateNodeCommand.update(original.getParent());
//				}
//			}
//		}
//	}
	
	/**
	 * Vergleicht zwei Knotenstrukturen rekursiv und überträgt die Werte der Parameter
	 * @author Burkhard Pöhler
	 * @param original der zu kopierende ursprüngliche Konten
	 * @param sample der neue Knoten
	 */
	public static void transferParameters(MyTreeItem original, MyTreeItem sample){
		if (original.getParameter().equals(sample.getParameter())) {	//oberster Knoten ist gesuchter Parameter
			sample.setVariablenWert(original.getVariablenWert());
			sample.setVariablenEinheit(original.getVariablenEinheit());
			String string = "Übertrage Parameter: " 
			+ sample.getParameter() + " "
			+ sample.getBezeichnung() + " "
			+ sample.getVariablenWert() + " "
			+ " nach: "
			+ sample.getParameter() + " "
			+ sample.getBezeichnung() + " "
			+ sample.getVariablenWert() + " "
			+ " ";
			
			LogfileView.log(TreeTools.class,string);
		}
		
		if (sample.isHasChildren()) {
			if (original.isHasChildren()){  //beide haben Kinder
				//alle Kinder im original  durchlaufen
				for (int n = 0; n < original.getNoOfChildren(); n++) {
					MyTreeItem originalChild = original.getChildren().get(n);
					MyTreeItem sampleChild = null;
					String o = originalChild.getParameter();
					//in Kindern des samples suchen
					sampleChild = findMyParameterTreeItem(sample, o);
					if (sampleChild != null){
						sampleChild.setVariablenWert(originalChild.getVariablenWert());
						sampleChild.setVariablenEinheit(originalChild.getVariablenEinheit());
						String string = "Übertrage Parameter: " 
						+ originalChild.getParameter() + " "
						+ originalChild.getBezeichnung() + " "
						+ originalChild.getVariablenWert() + " "
						+ " nach: "
						+ sampleChild.getParameter() + " "
						+ sampleChild.getBezeichnung() + " "
						+ sampleChild.getVariablenWert() + " "
						+ " ";
						
						LogfileView.log(TreeTools.class,string);
					}else{
						String string = "gelöschter Parameter: " 
								+ originalChild.getParameter() + " "
								+ originalChild.getBezeichnung() + " "
								+ originalChild.getVariablenWert() + " "
								+ " ";
								
						LogfileView.log(TreeTools.class,string);
					}
					transferParameters(originalChild,  sampleChild);	//rekursive suche
				}
			}	
			//nichts tun da sample mehr (neue) Kinder hat als original
		}
	}
	
	
	/**
	 * Durchsucht den Baum abwärts nach einer ParameterID
	 * im Parameterbaum
	 * @param myTreeItem der Basisknoten als Startknoten
	 * @param parameterID "Parameter"
	 * @return the myTreeItem oder Null wenn nicht gefunden
	 */
	public static MyTreeItem findMyParameterTreeItem(MyTreeItem myTreeItem, String parameterID) {
		MyTreeItem foundMyTreeItem = null;

		if (myTreeItem.isHasChildren()) { //wenn Kinder vorhanden dann durchsuchen
			for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
				MyTreeItem myNewTreeItem = myTreeItem.getChildren().get(i);
				if (myNewTreeItem.isParameter()) {
					if (myNewTreeItem.getParameter().equals(parameterID)) {
						return foundMyTreeItem = myNewTreeItem;

					} else {
						foundMyTreeItem = findMyParameterTreeItem(
								myNewTreeItem, parameterID);//rekursiv durchlaufen
						if (foundMyTreeItem != null) { //gefunden
							return foundMyTreeItem;
						}
						;
					}
				}
			}
		}

		return foundMyTreeItem;
	}
	

	/**
	 * Sucht rekursiv aufwärts bis zum Strukturknoten
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @return myTreeItem der gefundene Strukturknoten
	 */
	public static MyTreeItem searchForStrukturknoten(MyTreeItem myTreeItem){
		if(!myTreeItem.isStrukturknoten()){
			myTreeItem = myTreeItem.getParent();
			myTreeItem = searchForStrukturknoten(myTreeItem);
		}
		return myTreeItem;
	}
	
	/**
	 * Prüft, ob der Knoten ein Bestandteil des Projektes ist
	 * Knoten aus dem Bereich Basis, Vorlagen werden mit false markiert
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @return flag true = gehört zum Projekt
	 */
	public static boolean checkForProjektknoten(MyTreeItem myTreeItem){
		boolean flag = false;
		if(!myTreeItem.isStrukturknoten()){
			myTreeItem = myTreeItem.getParent();
			flag = checkForProjektknoten(myTreeItem);
		}else{
			if (myTreeItem.isBasis() || myTreeItem.isVorlage()){
				flag = false;
				return flag;
			}else{
				flag = true;
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * Sucht rekursiv aufwärts bis zum Oberstenknoten
	 * Dieser ist der erste Knoten in einer Datei
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @return myTreeItem der gefundene Obersterknoten
	 */
	public static MyTreeItem searchForObersterknoten(MyTreeItem myTreeItem){
		if(!myTreeItem.isObersterKnoten()){
			myTreeItem = myTreeItem.getParent();
			myTreeItem = searchForObersterknoten(myTreeItem);
		}
		return myTreeItem;
	}
	
	/**
	 * Sucht rekursiv aufwärts bis zum Projekt
	 * Dieser ist der erste Knoten in einem Projekt
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @return myTreeItem der gefundene Projektknoten
	 */
	public static MyTreeItem searchForProjekt(MyTreeItem myTreeItem){
		if(!myTreeItem.isProjekt()){
			myTreeItem = myTreeItem.getParent();
			myTreeItem = searchForProjekt(myTreeItem);
		}
		return myTreeItem;
	}
	
	/**
	 * Sucht rekursiv aufwärts bis zum Projektknoten.
	 * Der gefundene Knoten ist der erste auf dem Weg nach oben.
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @param parameterID Die gesuchte Id
	 * @return myTreeItem der gefundene Projektknoten, Null wenn nicht gefunden
	 */
	public static MyTreeItem searchUpwardsForTreeItemParameter(MyTreeItem myTreeItem, String parameterID){
		MyTreeItem myFoundTreeItem = null;
		
		if (myTreeItem != null) {
			if (!myTreeItem.getParameter().equals(parameterID)) {
				if (!myTreeItem.isProjekt() || !myTreeItem.isBasis()
						|| !myTreeItem.isObersterKnoten() || !myTreeItem.isProjektBaum()
						|| !myTreeItem.isStrukturknoten() || !myTreeItem.isVorlage() || !myTreeItem.isWurzel()) {

					myTreeItem = myTreeItem.getParent();
					myFoundTreeItem = searchUpwardsForTreeItemParameter(myTreeItem, parameterID);
				}
			} else {
				myFoundTreeItem = myTreeItem;
			} 
		}
		return myFoundTreeItem;
	}
	
	
	/**
	 * @author Burkhard Pöhler
	 * Suche nach dem Kinde
	 * @param kind das Kind (UUID)
	 * @param vater der Vater
	 * @return myTreeItem das gefundene Kind
	 * @throws NoChildFoundException 
	 */
	public static MyTreeItem searchForChild(MyTreeItem vater, String kind) throws NoChildFoundException{
		MyTreeItem myTreeItem = null;
		for (int i = 0; i < vater.getChildren().size(); i++) { 
			if(vater.getChildren().get(i).getUuid() == kind){
				myTreeItem = vater.getChildren().get(i);
				break;
			}
		}	
		if (myTreeItem == null){
            /* Hier wird unsere Exception geworfen */
            throw new NoChildFoundException();
		}	
		return myTreeItem;
	}

	/**
	 * @author Burkhard Pöhler
	 * @param	myTreeItem zu kopierender Knoten
	 */
	public static void CopyMyTreeItem(MyTreeItem myTreeItem){
		DBTools.copyMyTreeItem(myTreeItem);
	}
	
	/**
	 * @author Burkhard Pöhler
	 * @param	myTreeItem zu kopierender und auszuschneidender Zweig
	 */
	public static void CutMyTreeItem(MyTreeItem myTreeItem){
		myTreeItem.getParent().removeChild(myTreeItem);			//entfernt das Kind
		myTreeItemCopy = myTreeItem;							//speichert den Zweig
		myTreeItem.Rename(myTreeItemCopy);						//rekursives Umbenennen aller items	
		BaumView.DeleteNode(myTreeItem);				//löscht das item
	}	
	
	/**
	 * Der zwischengespeicherte Knoten wird rekursiv durchlaufen
	 * Anschließend werden alle Knoten mit neuen Eigenschaften (UUID etc.) versehen 
	 * 
	 * @author Burkhard Pöhler
	 * @param	myTreeItem ausgewählter Knoten
	 */
	public static MyTreeItem PasteMyTreeItem(MyTreeItem myTreeItem){
		
		MyTreeItem myNewTreeItem = DBTools.picMyStoredTreeItem();  //erzeuge eine tiefe Kopie
		myNewTreeItem = MyTreeItemSettings.copyFullItem(myTreeItem, myNewTreeItem);//stellt sicher, dass keine identischen Objekte entstehen
//		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
//		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
//		myNewTreeItem.setUuid();
		return myNewTreeItem;
	}	
	
	/**
	 * @author Burkhard Pöhler
	 * @param	myTreeItem zu kopierender Zweig
	 * @param	file neuer Dateipfad
	 */
	public static void CopyMyTreeItemToFile(MyTreeItem myTreeItem, File file){
		CopyMyTreeItem(myTreeItem);				//läd den kompletten Zweig in die Zwischendatei
		MyTreeItem myNewTreeItem = DBTools.picMyStoredTreeItem();  //erzeuge eine tiefe Kopie
		myNewTreeItem = MyTreeItemSettings.copyFullItem(myTreeItem, myNewTreeItem); //stellt sicher, dass keine identischen Objekte entstehen
		myNewTreeItem.setDatenbankName(file.toString());
		myNewTreeItem.setVariablenWert(file.getName().replace(".DB4O", ""));
		EmbeddedObjectContainer db;
		db=Db4oEmbedded.openFile(file.toString());					//erzeuge Datei
	    db.store(myNewTreeItem);									//speicher den Baum
	    db.close();	    											//neue Datei schließen
	}	
	
	/**
	 * @author Burkhard Pöhler
	 * @param	file Dateipfad des zu öffnenden Projektes
	 */
	
	public static void ProjektOeffnen(File file){
		//Neuen Knoten anlegen
		MyTreeItem neuerDateiKnoten = new MyTreeItem();
		neuerDateiKnoten.setBeschreibung("Datei");
		neuerDateiKnoten.setObersterKnoten(true);
		neuerDateiKnoten.setBezeichnung("Datei");
		neuerDateiKnoten.setParameter("Projekt");
		neuerDateiKnoten.setIsProjekt(true);
		neuerDateiKnoten.setVariablenWert(file.toString());
		
		//Neuen Knoten in Projektdatenbank ablegen
		if (file.exists()) {
			// Datei in die Liste der akutellen Projekte aufnehmen
			
			EmbeddedObjectContainer db;
			db=Db4oEmbedded.openFile(PROJEKTDATENBANK);
			Query query=db.query();							//Abfrage definieren
			query.constrain(MyTreeItem.class);				//suche alle Datensätze
		    query.descend("isObersterKnoten").constrain("true");		//nach obersten Knoten suchen
		    ObjectSet<MyTreeItem> results=query.execute();
		    if (results.size()==1){
		    	results.get(0).setChild(neuerDateiKnoten);
		    }
		    db.store(results);
		    db.close();	    
			
		}
		//Hauptbaum ergänzen
    	Iterator<MyTreeItem> it = myTopTreeItem.children.iterator();
    	while (it.hasNext()) {
    		if(it.next().isProjektBaum()){
    			it.next().setChild(neuerDateiKnoten);
    		};	    		
		}
		
		
	}
	
	/**
	 * @author Burkhard Pöhler
	 * @param	speichert und schließt alle offenen Projekte
	 */
	
//	public void AlleProjekteSchließen(){
//		
//    	Iterator<MyTreeItem> itProjektBaum = myTopTreeItem.children.iterator();
//    	//Projektbaum suchen und durchlaufen
//    	while (itProjektBaum.hasNext()) {		
//    		if(itProjektBaum.next().isProjektBaum()){
//    			Iterator<MyTreeItem> itProjekt = itProjektBaum.next().children.iterator();
//    			while (itProjekt.hasNext()) {
//    				database.DBTools.SchliesseDatei(itProjekt.next());
//    			}
//    		};	    		
//		}
//		
//	}
	
	/**
	 * @author Burkhard Pöhler
	 * @param	file Dateipfad des zu schließenden Projektes
	 * @return true wenn Datei erfolgreich geschlossen
	 */
	
//	public static boolean ProjektSchließen(MyTreeItem myTreeItem){
//		//Datei speichern und Schließen
//		database.DBTools.SchliesseDatei(myTreeItem);
//		//Datei aus Projektbaum entfernen
//		Iterator<MyTreeItem> itProjektBaum = myTopTreeItem.children.iterator();
//		//Projektbaum suchen und durchlaufen
//    	while (itProjektBaum.hasNext()) {		
//    		if(itProjektBaum.next().isProjektBaum()){
//    			itProjektBaum.next().removeChild(myTreeItem);
//    		};	    		
//		}
//    	//zu löschenden Knoten in Hauptbaum entfernen
//    	BaumView.DeleteNode(myTreeItem);
//    	myTreeItem.getParent().Update();
    	
//		// Datei in die Liste der akutellen Projekte s
//		
//		ObjectContainer db;
//		db=Db4o.openFile(projektdatenbank);
//		Query query=db.query();							//Abfrage definieren
//		query.constrain(MyTreeItem.class);				//suche alle Datensätze
//	    query.descend("isProjektBaum").constrain("true");		//nach obersten Knoten suchen
//	    ObjectSet<MyTreeItem> results=query.execute();
//	    if (results.size()==1){
//	    	results.get(0).removeListener(myTreeItem);
//	    	results.get(0).removeChild(myTreeItem);
//	    	results.get(0).fireRemove(myTreeItem.getParent());
//	    }	
//	    db.store(results);
//	    db.close();	 
	
//	    return true;
//		
//	}
	

	public void BasisdatenbankSchreiben(MyTreeItem myTreeItem){
		EmbeddedObjectContainer db;
		db=Db4oEmbedded.openFile(BASISDATENBANK);
		db.store(myTreeItem);
	    db.close();	    
	}
	
	public static MyTreeItem BasisdatenbankLesen(){
		MyTreeItem myTreeItem = null;
		EmbeddedObjectContainer db;
		db=Db4oEmbedded.openFile(BASISDATENBANK);
//		ObjectSet result2=db.queryByExample(MyTreeItem.class);			
//		listResult(result2);
		
		Query query=db.query();							//Abfrage definieren
		query.constrain(MyTreeItem.class);				//suche alle Datensätze
	    query.descend("isObersterKnoten").constrain("true");		//nach obersten Knoten suchen
	    ObjectSet<MyTreeItem> results=query.execute();
	    
	    if (results.size()==1){
	    	myTreeItem = results.get(0);
	    }
	    db.close();	    
		return myTreeItem;
	}
	
	private static void BasisdatenbankAnlegen() {
		//Datei wird erzeugt, wenn nicht vorhanden
		EmbeddedObjectContainer db=Db4oEmbedded.openFile(BASISDATENBANK);

		//füllen der leeren Datenbank
		
		// erster Knoten für Basis
		MyTreeItem basisKnoten = new MyTreeItem();
		basisKnoten.setDatenbankName(BASISDATENBANK);
		basisKnoten.setDbIndex(1);						//Basisdatenbank hat immer Index 1
		basisKnoten.setBeschreibung("Basis");
//		basisKnoten.setParent(null);
		basisKnoten.setObersterKnoten(true);
		basisKnoten.setBezeichnung("Basis");
		basisKnoten.setParameter("Basis");
		basisKnoten.setIconDateiname("beans.png");
		basisKnoten.setIsBasis(true);
		basisKnoten.setVariablenWert("variablenWert");
		basisKnoten.setHasPreChild(false);
		basisKnoten.setHasPostChild(false);
		basisKnoten.setHasChildren(true);
		basisKnoten.setUuid("basis");
		basisKnoten.setStrukturknoten(true);
		
	
		// erster Knoten für Parameter
		MyTreeItem parameterKnoten = new MyTreeItem();
		parameterKnoten.setBeschreibung("Parameter");
		parameterKnoten.setBezeichnung("Parameter");
		parameterKnoten.setParameter("Parameter");
		parameterKnoten.setIconDateiname("bean-green.png");
		parameterKnoten.setObersterKnoten(false);
		parameterKnoten.setIsParameter(true);
		parameterKnoten.setVariablenWert("variablenWert");
		parameterKnoten.setHasPreChild(false);
		parameterKnoten.setHasPostChild(true);
		parameterKnoten.setHasChildren(false);
		parameterKnoten.setUuid("parameter");
		parameterKnoten.setStrukturknoten(true);
		
		// zweiter Knoten für Eigenschaften
		MyTreeItem eigenschaftenKnoten = new MyTreeItem();
		eigenschaftenKnoten.setBeschreibung("Eigenschaften");
		eigenschaftenKnoten.setBezeichnung("Eigenschaften");
		eigenschaftenKnoten.setParameter("Eigenschaften");
		eigenschaftenKnoten.setIconDateiname("bean-small-green.png");
		eigenschaftenKnoten.setObersterKnoten(false);
		eigenschaftenKnoten.setIsEigenschaft(true);
		eigenschaftenKnoten.setVariablenWert("variablenWert");
		eigenschaftenKnoten.setHasPreChild(true);
		eigenschaftenKnoten.setHasPostChild(false);
		eigenschaftenKnoten.setHasChildren(false);
		eigenschaftenKnoten.setUuid("eigenschaft");
		eigenschaftenKnoten.setStrukturknoten(true);
		
		basisKnoten.setChild(parameterKnoten);
		basisKnoten.setChild(eigenschaftenKnoten);
		
		db.store(basisKnoten);							// speichern
		db.close();
	}
	
	/**
	 * Erzeugt mit dem Dateipfad eine neue Datenbank für ein Projekt
	 * @return myTreeItem der neue Projektknoten
	 */
	public static MyTreeItem NeuesProjektAnlegen(String projekt) {
		File file = new File(projekt);
		if (file.exists()) file.delete();
		
		EmbeddedObjectContainer db;
		db=Db4oEmbedded.openFile(projekt); 						//Datei wird erzeugt, wenn nicht vorhanden
		//füllen der leeren Datenbank
		
		// Knoten für das Projekt selbst
		MyTreeItem projektKnoten = new MyTreeItem();
		projektKnoten.setBeschreibung(projekt);
		projektKnoten.setObersterKnoten(true);
		projektKnoten.setIsProjekt(true);
		projektKnoten.setBezeichnung(projekt);
		projektKnoten.setParameter("Projekt");
		projektKnoten.setIsParameter(false);
		projektKnoten.setVariablenWert("");
		projektKnoten.setHasPreChild(false);
		projektKnoten.setHasPostChild(false);
		projektKnoten.setHasChildren(true);
		projektKnoten.setIconDateiname("folders-stack.png");
		projektKnoten.setStrukturknoten(true);
		projektKnoten.setDummy(false);
	
		// Knoten für die Projekteinstellungen
		MyTreeItem einstellungenKnoten = new MyTreeItem();
		einstellungenKnoten.setIndex(0);
		einstellungenKnoten.setBeschreibung("Projekteinstellungen");
		einstellungenKnoten.setBezeichnung("Einstellungen");
		einstellungenKnoten.setIsVorlage(false);
		einstellungenKnoten.setObersterKnoten(false);
		einstellungenKnoten.setParameter("Einstellungen");
		einstellungenKnoten.setIsParameter(false);
		einstellungenKnoten.setVariablenWert("");
		einstellungenKnoten.setHasPreChild(false);
		einstellungenKnoten.setHasPostChild(true);
		einstellungenKnoten.setHasChildren(false);
		einstellungenKnoten.setIconDateiname("key.png");
		einstellungenKnoten.setStrukturknoten(true);
		einstellungenKnoten.setDummy(false);
		
		// Knoten für die Basisdaten
		MyTreeItem basisdatenKnoten = new MyTreeItem();
		basisdatenKnoten.setIndex(10);
		basisdatenKnoten.setBeschreibung("Basisdaten");
		basisdatenKnoten.setBezeichnung("Basisdaten");
		basisdatenKnoten.setIsVorlage(false);
		basisdatenKnoten.setObersterKnoten(false);
		basisdatenKnoten.setParameter("Basisdaten");
		basisdatenKnoten.setIsParameter(false);
		basisdatenKnoten.setVariablenWert("");
		basisdatenKnoten.setHasPreChild(false);
		basisdatenKnoten.setHasPostChild(true);
		basisdatenKnoten.setHasChildren(false);
		basisdatenKnoten.setIconDateiname("application-home.png");
		basisdatenKnoten.setStrukturknoten(true);
		basisdatenKnoten.setDummy(false);
		
		// Knoten für die Vorlagen
		MyTreeItem vorlagenKnoten = new MyTreeItem();
		vorlagenKnoten.setIndex(10);
		vorlagenKnoten.setBeschreibung("Vorlagen");
		vorlagenKnoten.setBezeichnung("Vorlagen");
		vorlagenKnoten.setIsVorlage(true);
		vorlagenKnoten.setObersterKnoten(false);
		vorlagenKnoten.setParameter("Vorlagen");
		vorlagenKnoten.setIsParameter(false);
		vorlagenKnoten.setVariablenWert("");
		vorlagenKnoten.setHasPreChild(true);
		vorlagenKnoten.setHasPostChild(true);
		vorlagenKnoten.setHasChildren(false);
		vorlagenKnoten.setIconDateiname("applications-accessories.png");
		vorlagenKnoten.setStrukturknoten(true);
		vorlagenKnoten.setDummy(false);
		
		// Knoten für E-Technik
		MyTreeItem e_technikKnoten = new MyTreeItem();
		e_technikKnoten.setIndex(10);
		e_technikKnoten.setBeschreibung("E-Technik");
		e_technikKnoten.setBezeichnung("E-Technik");
		e_technikKnoten.setParameter("E-Technik");
		e_technikKnoten.setObersterKnoten(false);
		e_technikKnoten.setIsEigenschaft(false);
		e_technikKnoten.setVariablenWert("");
		e_technikKnoten.setHasPreChild(true);
		e_technikKnoten.setHasPostChild(false);
		e_technikKnoten.setHasChildren(false);
		e_technikKnoten.setIconDateiname("lightning.png");
		e_technikKnoten.setStrukturknoten(true);
		e_technikKnoten.setDummy(false);
		
		// Knoten für M-Technik
		MyTreeItem m_technikKnoten = new MyTreeItem();
		m_technikKnoten.setIndex(10);
		m_technikKnoten.setBeschreibung("M-Technik");
		m_technikKnoten.setBezeichnung("M-Technik");
		m_technikKnoten.setParameter("M-Technik");
		m_technikKnoten.setObersterKnoten(false);
		m_technikKnoten.setIsEigenschaft(false);
		m_technikKnoten.setVariablenWert("");
		m_technikKnoten.setHasPreChild(true);
		m_technikKnoten.setHasPostChild(false);
		m_technikKnoten.setHasChildren(false);
		m_technikKnoten.setIconDateiname("applications-system.png");
		m_technikKnoten.setStrukturknoten(true);
		m_technikKnoten.setDummy(false);
		
		projektKnoten.setChild(einstellungenKnoten);
		projektKnoten.setChild(basisdatenKnoten);
		projektKnoten.setChild(vorlagenKnoten);
		projektKnoten.setChild(e_technikKnoten);
		projektKnoten.setChild(m_technikKnoten);
		
		db.store(projektKnoten);							// speichern
		db.close();
		return projektKnoten;
	}
	
	public static Boolean isProjektVorhanden(String string){
		Boolean flag = false;
		//Projektbaum suchen und durchlaufen
		for (int i = 0; i <myTopTreeItem.children.size(); i++) {
	   		if(myTopTreeItem.children.get(i).isProjektBaum()){
	   			projektKnoten=myTopTreeItem.children.get(i);	
	   			for (int n=0; n<projektKnoten.getChildren().size();n++){
	   				String name = projektKnoten.getChildren().get(n).getDatenbankName();
	   				if(name.equals(string)){
	   					flag = true;		
	   				}
	   			}
    		};	    		
		}
		return flag;
		
	}

	/**
	 * @return the zwischenspeicher
	 */
	public static boolean isZwischenspeicher() {
		return zwischenspeicher;
	}

	/**
	 * @param zwischenspeicher the zwischenspeicher to set
	 */
	public static void setZwischenspeicher(boolean zwischenspeicher) {
		TreeTools.zwischenspeicher = zwischenspeicher;
	}

	/**
	 * @return the myTopTreeItem
	 */
	public static MyTreeItem getMyTopTreeItem() {
		return myTopTreeItem;
	}
	
	/**
	 * Läuft rekursiv durch alle Knoten und ergänzt die UUID des Vaters
	 * @param myTreeItem der Startknoten
	 */
	public static void addParentUUID(MyTreeItem myTreeItem) {
		if (myTreeItem != null) {
			if (myTreeItem.children != null) {
				for (int n = 0; n < myTreeItem.getChildren().size(); n++) {
					addParentUUID(myTreeItem.getChildren().get(n));
				}
			}
			if (myTreeItem.getParent() != null) {
				myTreeItem.setParentUUID(myTreeItem.getParent().getUuid());
				if (myTreeItem.getParent().getUuid().equals("")) {
					LogfileView.log("Fehler! Parent.UUID is null: "
							+ myTreeItem.getBezeichnung() + "  "
							+ myTreeItem.getUuid());
				}
			}
		}
	}
	
	/**
	 * Läuft rekursiv durch alle Knoten und ergänzt den Vater
	 * @param myTreeItem der Startknoten
	 */
	public static void addParent(MyTreeItem myTreeItem) {
		if (myTreeItem != null) {
			if (myTreeItem.children != null) {
				for (int n = 0; n < myTreeItem.getChildren().size(); n++) {
					myTreeItem.getChildren().get(n).setParent(myTreeItem);
					addParent(myTreeItem.getChildren().get(n));
				}
			}
		}
	}
	
	
	/**
	 * Läuft rekursiv durch alle Knoten und ergänzt die Werte
	 * @param myTreeItem der Startknoten
	 */
	public static void updateValues(MyTreeItem myTreeItem) {
		if (myTreeItem != null) {
			if (myTreeItem.children != null) {
				for (int n = 0; n < myTreeItem.getChildren().size(); n++) {
					myTreeItem.updateValues();
					updateValues(myTreeItem.getChildren().get(n));
				}
			}
		}
	}

	/**
	 * @return the myBasisTreeItem
	 */
	public static MyTreeItem getMyBasisTreeItem() {
		return myBasisTreeItem;
	}
	
	/**
	 * Sucht in einer Liste einen Parameter. 
	 * Index liefert die erste Position in der Liste. Wenn nicht gefunden liefert Index -1.
	 * 
	 * @param sample Der Knoten
	 * @param list Die zu durchsuchende Liste
	 * @return index
	 */
	public static int  findParameter(MyTreeItem sample, List<MyTreeItem> list){
		int index = -1;
		if (list != null) {
			if (sample != null){
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getParameter().equals(sample.getParameter())){
						index=i;
						break;
					}
				}
			}
		}
		return index;
	}
	
	/**
	 * Vergleicht die Knoten zweier Listen miteinander. Anzahl und Reihenfolge der Knoten und Parameter Id müssen gleich sein
	 * @param listA 
	 * @param listB 
	 * @return flag True=identisch
	 */
	public static boolean  compareList(List<MyTreeItem> listA,List<MyTreeItem> listB){
		boolean flag = true;
		if (listA != null && listB != null) {		//beide Listen nicht leer
			if (listA.size() == listB.size()){		//beide Listen gleich lang
				for (int i = 0; i < listA.size(); i++) {		
					if (!listA.get(i).getParameter().equals(listB.get(i).getParameter())){ //nicht gleich
						flag = false;
						break;
					}
				}
			}else flag = false;
		}else flag = false;
		return flag;
	}
	
	/**
	 * Durchsucht rekursiv den Knoten und listet alle Kinder in einer Liste auf.
	 * @param sample Startknoten
	 * @param list leere Liste
	 * @return list die Liste mit den Knoten
	 */
	public static List<MyTreeItem> prepareNodeList(MyTreeItem sample, List<MyTreeItem> list ){
		if (sample != null) {
			list.add(sample); 
//			LogfileView.log(TreeTools.class," Liste erweitert um " + sample.getParameter());
			if (sample.isHasChildren()){
				for (int n = 0; n < sample.getNoOfChildren(); n++) {
					prepareNodeList(sample.getChildren().get(n), list );
				}
			}
		}
		return list;
	}
	

}