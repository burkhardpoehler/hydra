/**
 * 
 */
package com.hydra.project.functions;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * manipulates the settings of the TreeItem
 * @author Poehler
 *
 */
public class MyTreeItemSettings {

	/**
	 * makes a copy of the Item with all subitems
	 * @author Poehler
	 * @param myTreeItem the target item to add a child
	 * @param mySelectedTreeItem the item to copy
	 * @return myTreeItem the copied item
	 */
	public static MyTreeItem copyFullItem(MyTreeItem myTreeItem, MyTreeItem mySelectedTreeItem){
		MyTreeItem myNewTreeItem = copyValues(mySelectedTreeItem);

//		rekursives daurchlaufen des Baumes
		myNewTreeItem = copyFullItemNew(mySelectedTreeItem);
		
		//Da das oberste Kind einem neuen Vater zugeordnet wird, muss der Vater benannt werden 
		if (myTreeItem.getParent()!= null){
			myNewTreeItem.setParent(myTreeItem.getParent());
			myNewTreeItem.setParentUUID(myTreeItem.getUuid());
		}
		
		//da ein Transfer von einer Datei auf eine andere Datei möglich ist, muss der neu Knoten angepasst werden
		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		return myNewTreeItem;		
	}
	
	/**
	 * recursive copy of all subitems
	 * @author Poehler
	 * @param myToCopyTreeItem the item to copy
	 * @return myTreeItem the copied item
	 */
	public static MyTreeItem copyFullItemNew(MyTreeItem myToCopyTreeItem){
		MyTreeItem myNewTreeItem = copyValues(myToCopyTreeItem);
		if (myToCopyTreeItem.getChildren() != null) {
			for (int i = 0; i < myToCopyTreeItem.getChildren().size(); i++) { //alle Kinder suchen
				myNewTreeItem.addChild(copyFullItemNew(myToCopyTreeItem.getChildren().get(i)));
			}
		}		
		return myNewTreeItem;
	}
	
	/**
	 * makes a copy of the item only.Children will be deleted
	 * @author Poehler
	 * @param myTreeItem the target item to add a child
	 * @param mySelectedTreeItem the item to copy
	 * @return myTreeItem the copied item without children
	 */
	public static MyTreeItem copyItemOnly(MyTreeItem myTreeItem, MyTreeItem mySelectedTreeItem){
		MyTreeItem myNewTreeItem = copyValues(mySelectedTreeItem);
		
		//Da das Kind einem neuen Vater zugeordnet wird, muss der Vater benannt werden 
		if (myTreeItem.getParent()!= null){
			myNewTreeItem.setParent(myTreeItem.getParent());
			myNewTreeItem.setParentUUID(myTreeItem.getUuid());
		}
		
		//da ein Transfer von einer Datei auf eine andere Datei möglich ist, muss der neu Knoten angepasst werden
		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		return myNewTreeItem;
	}
	
	/**
	 * makes a copy of the settings
	 * @author Poehler
	 * @param myTreeItem the item to copy
	 * @return myNewTreeItem the copied item
	 */
	private static MyTreeItem copyValues(MyTreeItem myTreeItem){
		MyTreeItem myNewTreeItem = new MyTreeItem();
		
		//Eigenschaften des Vaters erben:
		myNewTreeItem.setParameter(myTreeItem.getParameter());

		myNewTreeItem.setBezeichnung(myTreeItem.getBezeichnung());
		myNewTreeItem.setBeschreibung(myTreeItem.getBeschreibung());
		myNewTreeItem.setVariablenWert(myTreeItem.getVariablenWert());
		myNewTreeItem.setVariablenEinheit(myTreeItem.getVariablenEinheit());
		myNewTreeItem.setVariablentyp(myTreeItem.getVariablentyp());
		myNewTreeItem.setEditor(myTreeItem.getEditor());
		myNewTreeItem.setIconDateiname(myTreeItem.getIconDateiname());
		myNewTreeItem.setToolTipText(myTreeItem.getToolTipText());
		
		myNewTreeItem.setIsEigenschaft(myTreeItem.isEigenschaft());
		myNewTreeItem.setIsParameter(myTreeItem.isParameter());
		myNewTreeItem.setIsVorlage(myTreeItem.isVorlage());
		myNewTreeItem.setDummy(myTreeItem.isDummy());
		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		return myNewTreeItem;
	}
	
	/**
	 * makes a dummy
	 * @author Poehler
	 *
	 */
	public static MyTreeItem dummy(MyTreeItem myTreeItem){
		MyTreeItem myNewTreeItem = new MyTreeItem();
		//Eigenschaften des Vaters erben:
		myNewTreeItem.setParameter("");
	
		myNewTreeItem.setBezeichnung("Dummy");
		myNewTreeItem.setBeschreibung("");
		myNewTreeItem.setVariablenWert("");
		myNewTreeItem.setVariablenEinheit("");
		myNewTreeItem.setVariablentyp("");
		
		myNewTreeItem.setIconDateiname("arrow-branch-270-left.png");
		myNewTreeItem.setToolTipText("");

		myNewTreeItem.setIsEigenschaft(myTreeItem.isEigenschaft());
		myNewTreeItem.setIsParameter(myTreeItem.isParameter());
		myNewTreeItem.setIsVorlage(myTreeItem.isVorlage());
		myNewTreeItem.setDummy(true);		
		
		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		
		return myNewTreeItem;
	}
	
	/**
	 * makes a dummy
	 * @author Poehler
	 *
	 */
	public static MyTreeItem newNode(MyTreeItem myTreeItem){
		MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myTreeItem);
		MyTreeItem myNewTreeItem = new MyTreeItem();
		//Eigenschaften des Vaters erben:
		
		if(myTreeItemStrukturknoten.isParameter()){
			myNewTreeItem.setParameter("P000.000.000");
			myNewTreeItem.setBezeichnung("Neuer Parameter");
		}else if (myTreeItemStrukturknoten.isEigenschaft()){
			myNewTreeItem.setParameter("E000.000.000");
			myNewTreeItem.setBezeichnung("Neue Eigenschaft");
		}
		myNewTreeItem.setBeschreibung("");
		myNewTreeItem.setVariablenWert("");
		myNewTreeItem.setVariablenEinheit("");
		myNewTreeItem.setVariablentyp("");
		
		myNewTreeItem.setIconDateiname("empty.png");
		myNewTreeItem.setToolTipText("");

		myNewTreeItem.setIsEigenschaft(myTreeItem.isEigenschaft());
		myNewTreeItem.setIsParameter(myTreeItem.isParameter());
		myNewTreeItem.setIsVorlage(myTreeItem.isVorlage());
		myNewTreeItem.setDummy(false);		
		
		myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		
		return myNewTreeItem;
	}
	
}
