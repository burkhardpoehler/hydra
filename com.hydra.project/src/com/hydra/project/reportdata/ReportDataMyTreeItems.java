/**
 * 
 */
package com.hydra.project.reportdata;

import java.util.ArrayList;
import java.util.List;

import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 *
 */
public class ReportDataMyTreeItems {
	private static List<MyTreeItem> items = new ArrayList<MyTreeItem>();
	
	 /**
     * Bereitet die Daten für den Report vor
     * @param myItems Die Liste der TreeItems
     * @author Poehler
     */
	public static void setItems(List<MyTreeItem> myItems){
		items.clear();
		
		for (int n = 0; n < myItems.size(); n++) {
			items.addAll(myItems);
		}
	}
	
	/**
     * Liefert eine Liste mit TreeItems
     * @return items Die Liste der TreeItems
     * 
     * @author Poehler
     */
	public static List<MyTreeItem> getItems(){
		return items;
	}
	
}
