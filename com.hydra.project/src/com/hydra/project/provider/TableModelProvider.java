/**
 * 
 */
package com.hydra.project.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import lifecycle.MyImageHandler;

import org.eclipse.swt.graphics.Image;

import com.hydra.project.model.MyPropertyItem;
import com.hydra.project.model.MyTreeItem;

/**
 * @author Poehler
 *
 */
public class  TableModelProvider {
//	public enum  TableModelProvider {
//	INSTANCE;

//	public TableModelProvider(){
//		generateTable();
//	}
	
	private static Calendar calendar = Calendar.getInstance();
	private static SimpleDateFormat outputFormat = new SimpleDateFormat();
	
	
	protected static List <MyPropertyItem> items;
//	  private MyImageHandler i = new MyImageHandler();
	  private static Image image = MyImageHandler.getImage("action2.gif");
	
	  private static MyTreeItem myTreeItemOld;
	  @ Inject
	  private static MyTreeItem myTreeItemNew;
	  
	  public static void generateTable(MyTreeItem myTreeItem) {
		  boolean editable =  true;  //checkEditable(myTreeItem);
		  myTreeItemNew = myTreeItem;
		  // Liste mit Werten füllen
		  items = new ArrayList <MyPropertyItem> (5);
		  if (myTreeItem != null){
			  myTreeItemOld = myTreeItem;			//Wert zwischenspeichern
			  fillList(myTreeItem, editable);
		  } else {
			  if (myTreeItemOld == null){
				  MyTreeItem myTreeItemDummy = new MyTreeItem();
				  items.add(new MyPropertyItem("leer",true,false,"","CellEditor",MyImageHandler.getImage("action1.gif")));
			  }else{
				  myTreeItem = myTreeItemOld;
				  fillList(myTreeItem, editable);
			  }

		  }

	  }
	  
	  	/**
	  	 * Prüft, ob bestimmte Parameter bearbeitet werden dürfen
	  	 * Nur erlaubt im Bereich "Basis"
		 * @return true = Bearbeitung möglich
		 */
	  private static boolean checkEditable(MyTreeItem myTreeItem){
		boolean toggle = false;

			if (myTreeItem != null) {
				if (myTreeItem.isObersterKnoten()) {
					if (myTreeItem.isBasis()) {
						toggle = true;
					} else {
						toggle = checkEditable(myTreeItem.getParent());
					}
				} else{
					toggle = checkEditable(myTreeItem.getParent());
				}
			}
		return toggle;
	  }
	  
	  private static void fillList(MyTreeItem myTreeItem,boolean editable){ 
		

		  
		  items.add(new MyPropertyItem("Parameter",
				  true,
				  editable,
				  myTreeItem.getParameter(),
				  "CellEditor",MyImageHandler.getImage("edit-indent-rtl.png")));
		  
		  items.add(new MyPropertyItem("bezeichnung",
				  true,
				  editable,
				  myTreeItem.getBezeichnung(),
				  "CellEditor",MyImageHandler.getImage("Eigenschaften.GIF")));
		  
		  items.add(new MyPropertyItem("Beschreibung",
				  true,
				  editable,
				  myTreeItem.getBeschreibung(),
				  "CellEditor",MyImageHandler.getImage("document-hf-select.png")));		  

		  items.add(new MyPropertyItem("Wert",
				  true,
				  true,
				  myTreeItem.getVariablenWert(),
				  "CellEditor",MyImageHandler.getImage("action3.gif")));
		  
		  items.add(new MyPropertyItem("variablenEinheit",
				  true,
				  editable,
				  myTreeItem.getVariablenEinheit(),
				  "CellEditor",MyImageHandler.getImage("Format.gif")));
	
		  items.add(new MyPropertyItem("variablentyp",
				  true,
				  editable,
				  myTreeItem.getVariablentyp(),
				  "VariablentypComboBoxCellEditor",MyImageHandler.getImage("emblem-system.png")));
		  


		  items.add(new MyPropertyItem("anzeigeText",
				  true,
				  true,
				  myTreeItem.getAnzeigeText(),
				  "CellEditor",MyImageHandler.getImage("document-smiley.png")));
		  
		  items.add(new MyPropertyItem("iconDateiname",
				  true,
				  editable,
				  myTreeItem.getIconDateiname(),
				  "ImageDialogCellEditor",MyImageHandler.getImage( myTreeItem.getIconDateiname())));
		  
		  items.add(new MyPropertyItem("toolTipText",
				  true,
				  editable,
				  myTreeItem.getToolTipText(),
				  "CellEditor",MyImageHandler.getImage("flag-blue.png")));
		  
		  items.add(new MyPropertyItem("Editor",
				  true,
				  editable,
				  myTreeItem.getEditor(),
				  "EditorComboBoxCellEditor",MyImageHandler.getImage("edit-diff.png")));
		 
		  
		  String parent = "kein Parent";
		  if (myTreeItem.getParent() != null){
			  parent=myTreeItem.getParent().getBezeichnung();
		  }
		  items.add(new MyPropertyItem("parent",
				  true,
				  false,
				  parent,
				  "CellEditor",
				  MyImageHandler.getImage("web.gif")));	
		  
		  items.add(new MyPropertyItem("UUID",
				  true,
				  true,
				  myTreeItem.getUuid(),
				  "CellEditor",
				  MyImageHandler.getImage("web.gif")));		
		  
		  String wert = "";
		  if (myTreeItem.getParent() != null){
			  wert=myTreeItem.getParent().getUuid();
		  }
		  items.add(new MyPropertyItem("ParentUUID",
				  true,
				  true,
				  myTreeItem.getParentUUID(),
				  "CellEditor",
				  MyImageHandler.getImage("web.gif")));		
	
		  String path = "";
		  path = getPath(myTreeItem,path);

		  items.add(new MyPropertyItem("Path",
				  true,
				  false,
				  path,
				  "CellEditor",
				  MyImageHandler.getImage("web.gif")));		
		  
		  
		  items.add(new MyPropertyItem("Link",
				  true,
				  false,
				  myTreeItem.getLink(),
				  "CellEditor",
				  MyImageHandler.getImage("plug.png")));	
		  
		  items.add(new MyPropertyItem("Ersteller",
				  true,
				  false,
				  myTreeItem.getErsteller(),
				  "CellEditor",
				  MyImageHandler.getImage("plug.png")));
		  
//		  Date date = new Date(myTreeItem.getErstelldatum());
//		  calendar.setTime(date);
//		  outputFormat.applyPattern( "EEEE', 'dd. MMMM yyyy hh:mm" );
//		  String timestamp = outputFormat.format(date);
//		  try {
//			outputFormat.parse(myTreeItem.getErstelldatum());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		  
		  items.add(new MyPropertyItem("Erstelldatum",
				  true,
				  false,
				  myTreeItem.getErstelldatum(),
				  "CellEditor",
				  MyImageHandler.getImage("plug.png")));	
		  
		  items.add(new MyPropertyItem("Bearbeiter",
				  true,
				  false,
				  myTreeItem.getBearbeiter(),
				  "CellEditor",
				  MyImageHandler.getImage("plug.png")));	
		  
//		  Date date1 = new Date(myTreeItem.getErstelldatum());
//		  calendar.setTime(date1);
//		  outputFormat.applyPattern( "EEEE', 'dd. MMMM yyyy hh:mm" );
//		  String timestamp1 = outputFormat.format(date1);
		  
		  items.add(new MyPropertyItem("Änderungsdatum",
				  true,
				  false,
				  myTreeItem.getErstelldatum(),
				  "CellEditor",
				  MyImageHandler.getImage("plug.png")));	

		  items.add(new MyPropertyItem("Hat Kinder",
				  true,
				  false,
				  String.valueOf(myTreeItem.isHasChildren()),
				  "CheckboxCellEditor",
//				  MyImageHandler.getImage("hierarchical.gif")));
		  			MyImageHandler.getImage("help.gif")));
		  items.add(new MyPropertyItem("expanded",
				  true,
				  false,
				  String.valueOf(myTreeItem.isExpanded()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("sample.gif")));
		  
		  items.add(new MyPropertyItem("checked",
				  true,
				  false,
				  String.valueOf(myTreeItem.isChecked()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("Check_YES.gif")));
		  
		  items.add(new MyPropertyItem("grayed",
				  true,
				  false,
				  String.valueOf(myTreeItem.isGrayed()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("gradient.png")));
		  
		  items.add(new MyPropertyItem("hasPreChild",
				  true,
				  false,
				  String.valueOf(myTreeItem.isHasPreChild()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("node-select-previous.png")));
		  
		  items.add(new MyPropertyItem("hasPostChild",
				  true,
				  false,
				  String.valueOf(myTreeItem.isHasPostChild()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("node-select-next.png")));
		  
		  items.add(new MyPropertyItem("ueberschrift",
				  true,
				  true,
				  String.valueOf(myTreeItem.isUeberschrift()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("table-sum.png")));
		  
		  items.add(new MyPropertyItem("isWurzel",
				  true,
				  false,
				  String.valueOf(myTreeItem.isWurzel()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("folder-open.png")));
		  
		  items.add(new MyPropertyItem("isBasis",
				  true,
				  false,
				  String.valueOf(myTreeItem.isBasis()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("beans.png")));
		  
		  items.add(new MyPropertyItem("isEigenschaft",
				  true,
				  true,
				  String.valueOf(myTreeItem.isEigenschaft()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("bean-small.png")));
		  		  
		  items.add(new MyPropertyItem("isParameter",
				  true,
				  true,
				  String.valueOf(myTreeItem.isParameter()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("bean.png")));
		  

		  items.add(new MyPropertyItem("isVorlage",
				  true,
				  false,
				  String.valueOf(myTreeItem.isVorlage()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("folder-visiting.png")));
		  
		  items.add(new MyPropertyItem("isProjektBaum",
				  true,
				  false,
				  String.valueOf(myTreeItem.isProjektBaum()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("folders-stack.png")));
		  
		  items.add(new MyPropertyItem("isProjekt",
				  true,
				  false,
				  String.valueOf(myTreeItem.isProjekt()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("sample.gif")));
	
		  items.add(new MyPropertyItem("isObersterKnoten",
				  true,
				  false,
				  String.valueOf(myTreeItem.isObersterKnoten()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("TYPE_GROUP.gif")));
		  
		  items.add(new MyPropertyItem("isStrukturknoten",
				  true,
				  true,
				  String.valueOf(myTreeItem.isStrukturknoten()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("node-insert-next.png")));
	
		  items.add(new MyPropertyItem("isDummy",
				  true,
				  true,
				  String.valueOf(myTreeItem.isDummy()),
				  "CheckboxCellEditor",
				  MyImageHandler.getImage("arrow-branch-270-left.png")));
		  
		  items.add(new MyPropertyItem("Index",
				  true,
				  false,
				  String.valueOf(myTreeItem.getIndex()),
				  "CellEditor",
				  MyImageHandler.getImage("sort-number-column.png")));
		  
		  
		  items.add(new MyPropertyItem("Datenbankindex",
				  true,
				  false,
				  String.valueOf(myTreeItem.getDbIndex()),
				  "CellEditor",
				  MyImageHandler.getImage("key.png")));
		  
		  items.add(new MyPropertyItem("Datenbankname",
				  true,
				  false,
				  String.valueOf(myTreeItem.getDatenbankName()),
				  "CellEditor",
				  MyImageHandler.getImage("database--arrow.png")));
		  
	  }

	  public static List<MyPropertyItem> getItem() {
	    return items;
	  }

	/**
	 * @return the myTreeItemNew
	 */
	public static MyTreeItem getMyTreeItemNew() {
		return myTreeItemNew;
	}

	  private static String getPath(MyTreeItem myTreeItem, String path){
		  
		  if(myTreeItem.getParent() != null){
			  String path1 = getPath(myTreeItem.getParent(), "/" + myTreeItem.getParent().getBezeichnung());
			  path = path1 + path;
		  }
		  return path;
	  }


}
