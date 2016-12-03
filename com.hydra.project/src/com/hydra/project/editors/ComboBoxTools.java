/**
 * 
 */
package com.hydra.project.editors;

import java.util.ArrayList;

import org.eclipse.swt.SWT;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;

/**
 * Enth�lt Funktionen f�r das F�llen von Listenelementen
 * @author Poehler
 *
 */
public class ComboBoxTools {
//	private static ArrayList<String> arrayList = new ArrayList<String>();
	
	/**
	 * ListComboBoxCellEditor erlaubt nur die vorgegebenen Werte auszuw�hlen
	 * Die Werte m�ssen als Eigenschaft in Form von Listenwerten vorliegen
	 * R�ckgabe der ComboBox nur als Zahlen von 0 bis n
	 * 
	 * @param value das ausgew�hlte Element in der Liste (der Index)
	 * @param myTreeItem das ausgew�hlte Element
	 * 
	 * @return result der Index (0...n) des Wertes
	 */
    public static Object getComboBoxCellEditorValue(ArrayList<String> arrayList, String value, MyTreeItem myTreeItem){
    	Object result = 0;
    	//1. Schritt: suche im Eigenschaftenbaum nach der Eigenschaft
    	sucheListeneigenschaften(myTreeItem);
    	//2. Liste durchsuchen und Wert zuweisen
    	
		for (int i = 0; i < arrayList.size(); i++) { 
			if (arrayList.get(i).equals(value)) {
				result = i;
				break;
			}
		}
		return result;
    }
    
	/**
	 * ListComboBoxCellEditor erlaubt nur die vorgegebenen Werte auszuw�hlen
	 * Liefert als String das ausgew�hlte Element
	 * Die Werte m�ssen als Eigenschaft in Form von Listenwerten vorliegen
	 * 
	 * @param value (Zahl) das ausgew�hlte Element in der Liste
	 * @param myTreeItem das ausgew�hlte Element
	 * 
	 * @return result der Wert
	 */
    public static String setComboBoxCellEditorValues(ArrayList<String> arrayList, Object value, MyTreeItem myTreeItem){
    	if ((Integer)value == -1)value = 0;
    	
    	String result = "";
    	//1. Schritt: suche im Eigenschaftenbaum nach der Eigenschaft
    	sucheListeneigenschaften(myTreeItem);
    	//2. Wert zuweisen
		result = arrayList.get((Integer)value).toString();
//		if (result == "") result = arrayList.get(0).toString(); //keine Auswahl m�glich, setze kleinsten Wert
		return result;
    }
    
	/**
	* Pr�ft den Knoten auf Listeneigenschaften
	* wenn mindestens eine g�ltige Listeneigenschaft vorhanden ist, wird diese in eine Liste eingef�gt
    * @author Poehler
    * 
    * @param myTreeItem der Knoten mit den Listeneigenschaften
	* @return arrayList die Liste mit den Eintr�gen
    */
   public static ArrayList<String> sucheListeneigenschaften(MyTreeItem myTreeItem) {
	   ArrayList<String> arrayList = new ArrayList<String>();
//	   ArrayList<String> list =new ArrayList<String>();
//	   arrayList = new ArrayList();
		if(myTreeItem.isParameter()){			//Nur bei Parametern sinnvoll
			if (!myTreeItem.isDummy()) {		//Dummies haben keine Eigenschaften				
					//Alle Kinder des Parameters durchsuchen
					//Der Knoten hat selber keine Eigenschaften und m�ssen daher vom Original geholt werden
					MyTreeItem sampleMyTreeItem = TreeTools.findMyParameterTreeItem(TreeTools.getMyBasisTreeItem(), myTreeItem.getParameter());
					if (sampleMyTreeItem.getChildren() != null) {
						for (int i = 0; i < sampleMyTreeItem.getChildren().size(); i++) { //alle Kinder suchen
							// Listeneigenschaften beginnen mit E101.001.xxx
							
							String string = sampleMyTreeItem.getChildren().get(i).getParameter();
							
							if (string.startsWith("E101.")) {	//nur der erste Treffer wird genommen
								MyTreeItem listMyTreeItem = sampleMyTreeItem.getChildren().get(i);
								for (int n = 0; n < listMyTreeItem.getChildren().size(); n++) { //alle Kinder suchen
									arrayList.add(listMyTreeItem.getChildren().get(n).getVariablenWert());
								}
								break; //nach keinen weiteren Listen suchen
								
							}else {LogfileView.log(ComboBoxTools.class, "Kind ist kein Listeneigenschaft", SWT.ICON_INFORMATION);}
						}
						LogfileView.log(ComboBoxTools.class, "Alle Kinder durchlaufen", SWT.ICON_INFORMATION);
					}else {LogfileView.log(ComboBoxTools.class, "Hat keine Kinder", SWT.ICON_WARNING);}

			}else {LogfileView.log(ComboBoxTools.class, "Ist Dummy (Stufe)", SWT.ICON_INFORMATION);}
		}else {LogfileView.log(ComboBoxTools.class, "Kein Parameter", SWT.ICON_INFORMATION);}
		return arrayList;
//		return (String[]) arrayList.toArray();	
   }
    
}
