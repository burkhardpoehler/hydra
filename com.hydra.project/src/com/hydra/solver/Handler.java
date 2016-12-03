/**
 * 
 */
package com.hydra.solver;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.SolverView;


/**
 * @author Poehler
 * Der Handler wird benachrichtigt, wenn sich ein TreeItem verändert hat
 * Anschließend prüft der Handler, ob für dieses Item ein ahndlungsbedarf besteht.
 * Wenn JA werden die entsprechende Methode aufgerufen und abgearbeitet.
 * Zum Schluß wird eine Nachricht an alle Viewer zum Update gesendet.
 *
 */
public class Handler {


	public static void Solver(MyTreeItem myTreeItem)  {

		checkEigenschaften(myTreeItem);
		triggerCalculation(myTreeItem);
	}


	/**
	* Prüft den Knoten auf Berechnungseigenschaften
	* wenn mindestens eine gültige Eigenschaft vorhanden ist, wird diese an den Solver für einen Eventtrigger weitergeleitet
    * @author Poehler
    * 
    * @param myTreeItem der Knoten
    */
   public static void checkEigenschaften(MyTreeItem myTreeItem) {
		if (!myTreeItem.getVariablenWert().equals(myTreeItem.getVariablenWertAlt())){  		//Variablenwert wurde geändert
			if(myTreeItem.isParameter()){			//Nur bei Parametern sinnvoll
				if (!myTreeItem.isDummy()) {		//Dummies haben keine Eigenschaften				
					MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myTreeItem);	
					if (!myTreeItemStrukturknoten.isParameter()) { 	//nicht im Parameterbereich sinnvoll
						
						//Alle Kinder des Parameters durchsuchen
						//Der Knoten hat selber keine Eigenschaften und müssen daher vom Original geholt werden
						MyTreeItem sampleMyTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), myTreeItem.getParameter());
						if (sampleMyTreeItem.getChildren() != null) {
							for (int i = 0; i < sampleMyTreeItem.getChildren().size(); i++) { //alle Kinder suchen
								// Berechnungseigenschaften beginnen mit E011.xxx.xxx
								String string = sampleMyTreeItem.getChildren().get(i).getParameter();
								if (string.startsWith("E011.")) {
									SolverView.showMessage("Berechnungsparameter wird gesucht: " + sampleMyTreeItem.getBezeichnung());	
									searchForBerechnungsknoten(myTreeItem, sampleMyTreeItem.getChildren().get(i).getParameter());
								}else {SolverView.showMessage("Kind fordert keine Berechnung");}
							}
							SolverView.showMessage("Alle Kinder durchlaufen");
						}else {SolverView.showMessage("Hat keine Kinder");}
						
						
					}else {SolverView.showMessage("Befindet sich im Parameterbereich");}
				}else {SolverView.showMessage("Ist Dummy (Stufe)");}
			}else {SolverView.showMessage("Keine Parameter");}
		}else {SolverView.showMessage("Variablenwert wurde nicht geändert");}
   }
	
 
   
	/**
	 * Sucht rekursiv aufwärts bis zum nächsten Berechnungsparameter
	 * Gesucht wird rekursiv oberhalb des Knotens
	 * Alle Kinder des Vaterknotens werden auf den Parameter untersucht
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @param parameter der zu suchende Parameter
	 */
	public static void searchForBerechnungsknoten(MyTreeItem myTreeItem, String parameter){
		boolean flag = false;
		MyTreeItem myNewTreeItem = myTreeItem.getParent();
		if (myNewTreeItem.getChildren() != null) {
			for (int i = 0; i < myTreeItem.getChildren().size(); i++) { //alle Kinder suchen
				if(myNewTreeItem.getChildren().get(i).getParameter().equals(parameter)){
					SolverView.showMessage("Berechnungsparameter gefunden. Sende Aktualisierung");
					//Aktualisierung senden
					SolverView.sendEvent(myNewTreeItem.getChildren().get(i));
					flag = true;
				}
			}
		}
		if(!flag){
			SolverView.showMessage("Noch keinen Berechnungsparameter gefunden. Suche weiter aufwärts");
			searchForBerechnungsknoten(myNewTreeItem, parameter);
		}
	}
   
	
	  /**
	    * Prüft, ob ein Berechnungsparameter vorliegt und stößt eine Aktualisierung an.
	    * Für jede Berechnung muß eine separate Methode aufgerufen werden.
	    * @author Poehler
	    * @param myTreeItem das Item
	    */
	   private static void triggerCalculation(MyTreeItem myTreeItem) {
		   String paramterID = myTreeItem.getParameter();

		   if (paramterID.startsWith("E011.")) {
				switch (paramterID) {
				case ("P011.001.001"):
					SolverView
							.showMessage("Berechnungsparameter P011.001.001 in Bearbeitung");
					p011_001_001(myTreeItem);
					break;
				default:
					SolverView.showMessage("Keine Methode gefunden!"
							+ myTreeItem.getBezeichnung() + ": "
							+ myTreeItem.getParameter());
				}
		   }

	   }
	   
		/**
	    * Berechnung für Parameter P011.001.001
	    * 
	    * @author Poehler
	    * @param myTreeItem das Item
	    */
	   private static void p011_001_001(MyTreeItem myTreeItem) {
		   SolverView.showMessage("Berechnung abgeschlossen");
	   }

}
