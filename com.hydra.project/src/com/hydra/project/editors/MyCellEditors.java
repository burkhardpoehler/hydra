/**
 * 
 */
package com.hydra.project.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.swt.SWT;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.parts.PropertyView;

/**
 * MyCellEditors produces the correct editor for the cell
 * @author Poehler
 *
 */
public class MyCellEditors {

	private static TreeViewer treeViewer = null;
	private static TextCellEditor myCellEditor = null;
 	static TextCellEditor textCellEditor = null;
	static CheckboxCellEditor checkboxCellEditor = null;
	static ComboBoxCellEditor variablentypComboBoxCellEditor = null;
	static MyImageDialogCellEditor myImageDialogCellEditor = null;
//	static MyDateTimeDialogCellEditor myDateTimeDialogCellEditor = null;
	static ComboBoxCellEditor editorComboBoxCellEditor = null;
	static ComboBoxCellEditor listComboBoxCellEditor = null;
	static ColorCellEditor colorCellEditor = null;
	static CDateTimeCellEditor cDateTimeCellEditor = null;
	static Date date = null;
	protected static CDateTime cDateTime;
	static DateChooser cal;
	
	
	private static ArrayList<String> arrayList = new ArrayList<String>();
	
	/**
	 * Prüft das ausgewählte Element auf Editierbarkeit
	 * @param element das ausgewählte Element
	 * @return flag True = Freigabe zum Editieren
	 */
    public static boolean canEdit(Object element) {
        boolean flag = true;		//Standardmäßig is editieren erlaubt
        
    	if (element instanceof MyTreeItem) {
        	MyTreeItem myTreeItem = (MyTreeItem) element;
        	if (myTreeItem.isParameter()){
    			if (myTreeItem.getChildren() != null) {
					for (int i = 0; i < myTreeItem.getChildren().size(); i++) {  //suche die Sperreigenschaft
						if (myTreeItem.getChildren().get(i).getParameter().equals("E001.004.001")) {
							flag = false;
						}
					}
				}
        	}     	
        }
    	
    	if (flag) setList(element); //Liste für ListComboBox erzeugen
        return flag;
        

        
    }

	/**
	 * Ermittelt den passenden Editor
	 * @param element das ausgewählte Element
	 * @return myCellEditor der Editor
	 */
    public static CellEditor getCellEditor(Object element) {
     	CellEditor cellEditor = null;
    	if (element instanceof MyTreeItem) {
    		MyTreeItem p = (MyTreeItem) element;
        	
        	switch (p.getEditor()) {
        		case "CellEditor":
        			cellEditor = textCellEditor;
        			break;
        		case "CheckboxCellEditor":
        			cellEditor = checkboxCellEditor;
        			break;
        		case "ImageDialogCellEditor":
        			cellEditor = myImageDialogCellEditor;
        			break;
        		case "VariablentypComboBoxCellEditor":
        			cellEditor = variablentypComboBoxCellEditor;
        			break;
        		case "EditorComboBoxCellEditor":
        			cellEditor = editorComboBoxCellEditor;
        			break;
        		case "ColorCellEditor":
        			cellEditor = colorCellEditor;
        			break;
        		case "ListComboBoxCellEditor":
        			cellEditor = listComboBoxCellEditor;
        			break;
        		case "DateTimeDialogCellEditor":
        			cellEditor = cDateTimeCellEditor;
        			break;
        		default:
        			cellEditor = textCellEditor;
        	}
        }      
    	System.out.println("CellEditor :" + cellEditor.toString());
		return cellEditor;
    }

	/**
	 * holt den Wert
	 * @param element das ausgewählte Element
	 * @return element der Wert
	 */
    public static Object getValue(Object element) {
    	Object result = null;
    	if (element instanceof MyTreeItem) {
            String value = ((MyTreeItem) element).getVariablenWert();
           	String editor= ((MyTreeItem) element).getEditor();
            
           	switch (editor) {
    		case "CellEditor":
    			result = value;
    			break;
    		case "CheckboxCellEditor":
    			result = Boolean.valueOf(value);
    			break;
    		case "ImageDialogCellEditor":
    			result = value;
    			break;
    		case "VariablentypComboBoxCellEditor":
       			switch (value){
    			case "String":
    				result = 0;
    				break;
    			case "BigInteger":
    				result = 1;
    				break;
    			case "Float":
    				result = 2;
    				break;
    			case "Integer":
    				result = 3;
    				break;
    			case "Link":
    				result = 4;
    				break;
    			case "Grafic":
    				result = 5;
    				break;
    			case "Path":
    				result = 6;
    				break;
    			case "Boolean":
    				result = 7;
    				break;
    			default:
    				result = 0;
    			}
    			break;
       		case "EditorComboBoxCellEditor":
       			switch (value){
    			case "CellEditor":
    				result = 0;
    				break;
    			case "CheckboxCellEditor":
    				result = 1;
    				break;
    			case "ImageDialogCellEditor":
    				result = 2;
    				break;
    			case "VariablentypComboBoxCellEditor":
    				result = 3;
    				break;
    			case "ColorCellEditor":
    				result = 4;
    				break;
    			case "EditorComboBoxCellEditor":
    				result = 5;
    				break;
    			case "ListComboBoxCellEditor":
    				result = 6;
    				break;
    			case "DateTimeDialogCellEditor":
    				result = 7;
    				break;
    			default:
    				result = 0;
    			}
    			break;
    		case "ColorCellEditor":
    			result = value;
    			break;
       		case "ListComboBoxCellEditor":
    			result = getComboBoxCellEditorValue(value, (MyTreeItem) element);
    			break;
       		case "DateTimeDialogCellEditor":
       			
       			if (value == "") {
       				Date date = new Date();
//       				cDateTime.setSelection(time);
       				((MyTreeItem) element).setObject(date);
       				result = date;
       			}else{
       				result =((MyTreeItem) element).getObject();//da Zeitformat nicht umgewandelt werden kann aus String wird hier der Wert direkt geholt
       				if (result == null) {
         				Date date = new Date();
//         				cDateTime.setSelection(new Date());
           				((MyTreeItem) element).setObject(date);
           				result = date;
       				}
       			}
    			
    			break;
    		default:
    			result = value;
           	}                 
        }
//        return element.toString();
        return result;
    }
    
	/**
	 * ListComboBoxCellEditor erlaubt nur die vorgegebenen Werte auszuwählen
	 * Die Werte müssen als Eigenschaft in Form von Listenwerten vorliegen
	 * Rückgabe der ComboBox nur als Zahlen von 0 bis n
	 * 
	 * @param value das ausgewählte Element in der Liste (der Index)
	 * @param myTreeItem das ausgewählte Element
	 * 
	 * @return result der Index (0...n) des Wertes
	 */
    public static Object getComboBoxCellEditorValue(String value, MyTreeItem myTreeItem){
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
	 * ListComboBoxCellEditor erlaubt nur die vorgegebenen Werte auszuwählen
	 * Die Werte müssen als Eigenschaft in Form von Listenwerten vorliegen
	 * 
	 * @param value das ausgewählte Element in der Liste
	 * @param myTreeItem das ausgewählte Element
	 * 
	 * @return result der Wert
	 */
    public static String setComboBoxCellEditorValues(Object value, MyTreeItem myTreeItem){
    	if ((Integer)value == -1)value = 0;
    	
    	String result = "";
    	//1. Schritt: suche im Eigenschaftenbaum nach der Eigenschaft
    	sucheListeneigenschaften(myTreeItem);
    	//2. Wert zuweisen
		result = arrayList.get((Integer)value).toString();
//		if (result == "") result = arrayList.get(0).toString(); //keine Auswahl möglich, setze kleinsten Wert
		return result;
    }
    
	/**
	* Prüft den Knoten auf Listeneigenschaften
	* wenn mindestens eine gültige Listeneigenschaft vorhanden ist, wird diese in eine Liste eingefügt
    * @author Poehler
    * 
    * @param myTreeItem der Knoten
	 * @return 
    */
   public static void sucheListeneigenschaften(MyTreeItem myTreeItem) {
	   arrayList.clear();
		if(myTreeItem.isParameter()){			//Nur bei Parametern sinnvoll
			if (!myTreeItem.isDummy()) {		//Dummies haben keine Eigenschaften				
//				MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myTreeItem);	
//				if (!myTreeItemStrukturknoten.isParameter()) { 	//nicht im Parameterbereich sinnvoll
					
					//Alle Kinder des Parameters durchsuchen
					//Der Knoten hat selber keine Eigenschaften und müssen daher vom Original geholt werden
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
								
							}else {LogfileView.log(MyCellEditors.class, "Kind ist kein Listeneigenschaft", SWT.ICON_INFORMATION);}
						}
						LogfileView.log(MyCellEditors.class, "Alle Kinder durchlaufen", SWT.ICON_INFORMATION);
					}else {LogfileView.log(MyCellEditors.class, "Hat keine Kinder", SWT.ICON_WARNING);}
//				}else {LogfileView.log(MyCellEditors.class, "Befindet sich im Parameterbereich", SWT.ICON_INFORMATION);}
			}else {LogfileView.log(MyCellEditors.class, "Ist Dummy (Stufe)", SWT.ICON_INFORMATION);}
		}else {LogfileView.log(MyCellEditors.class, "Kein Parameter", SWT.ICON_INFORMATION);}
		
   }
    
    
    /**
	 * setzt den Wert
	 * @param element das ausgewählte Element
	 * @param value der übergebene Wert
	 * @return myTreeItem das ausgewählte Element
	 */
    public static MyTreeItem setValue(Object element, Object value) {
       	String result = null;
    	if (element instanceof MyTreeItem) {
           	String editor= ((MyTreeItem) element).getEditor();
            
           	switch (editor) {
    		case "CellEditor":
    			result = value.toString();
    			break;
    		case "CheckboxCellEditor":
    			result = "false";
    			if ((boolean) value)  result ="true";
    			break;
    		case "ImageDialogCellEditor":
    			result = value.toString();
    			break;
    		case "VariablentypComboBoxCellEditor":
    			switch ((Integer)value) {
				case 0:
					result = "String";
					break;
				case 1:
					result = "BigInteger";
					break;
				case 2:
					result = "Float";
					break;
				case 3:
					result = "Integer";
					break;
				case 4:
					result = "Link";
					break;
				case 5:
					result = "Grafic";
					break;
				case 6:
					result = "Path";
					break;
   				case 7:
					result = "Boolean";
					break;
				default:
        			result = "Integer";
        			break;
    			}
    			break;
    		case "EditorComboBoxCellEditor":
     			switch ((Integer)value) {
    				case 0:
    					result = "CellEditor";
    					break;
    				case 1:
    					result = "CheckboxCellEditor";
    					break;
    				case 2:
    					result = "ImageDialogCellEditor";
    					break;
    				case 3:
    					result = "VariablentypComboBoxCellEditor";
    					break;
    				case 4:
    					result = "ColorCellEditor";
    					break;
    				case 5:
    					result = "EditorComboBoxCellEditor";
    					break;
      				case 6:
    					result = "ListComboBoxCellEditor";
    					break;
    				case 7:
    					result = "DateTimeDialogCellEditor";
    					break;
    				default:
            			result = "CellEditor";
            			break;
        			}
        			break;
    		case "ColorCellEditor":
    			result = value.toString();
    			break;
    		case "ListComboBoxCellEditor":
    			result = setComboBoxCellEditorValues(value, (MyTreeItem) element);
				break;
       		case "DateTimeDialogCellEditor":
       			if (value == null) value = date.getTime();
       			result = value.toString();
    			((MyTreeItem) element).setObject(value);//da Zeitformat nicht umgewandelt werden kann aus String wird hier der Wert direkt gespeichert
				break;
			default:
        		result = value.toString();
    		}
        }
    	
  		((MyTreeItem) element).setVariablenWert(result);
    	treeViewer.update(element, null);
		return ((MyTreeItem) element);
    
    
    }
    
    /**
	 * Definiert die verschiedenen Editoren
	 */
    private static void setEditor(){
      	textCellEditor = new TextCellEditor(treeViewer.getTree());
    	checkboxCellEditor = new CheckboxCellEditor(treeViewer.getTree());
 
    	
        variablentypComboBoxCellEditor = new ComboBoxCellEditor(treeViewer.getTree(), PropertyView.getAuswahlComboBoxVariablentyp(),SWT.READ_ONLY);
 
        editorComboBoxCellEditor = new ComboBoxCellEditor(treeViewer.getTree(), PropertyView.getAuswahlComboBoxEditoren(),SWT.READ_ONLY);
        

    	myImageDialogCellEditor = new MyImageDialogCellEditor(treeViewer.getTree());
    	colorCellEditor = new ColorCellEditor(treeViewer.getTree());
    	cDateTimeCellEditor = new CDateTimeCellEditor(treeViewer.getTree());
//    	myDateTimeDialogCellEditor = new MyDateTimeDialogCellEditor(treeViewer.getTree());
    	
    }
    
    
    /**
	 * Definiert die Elemente für den listComboBoxCellEditor
	 */
    private static void setList(Object element){
    	sucheListeneigenschaften((MyTreeItem) element); //initialisiert das arrray
        //First Step: convert ArrayList to an Object array.
        Object[] obj = arrayList.toArray();
       
        //Second Step: convert Object array to String array
        String[] AuswahlComboBoxVariablen = Arrays.copyOf(obj, obj.length, String[].class);
        
        listComboBoxCellEditor = new ComboBoxCellEditor(treeViewer.getTree(), AuswahlComboBoxVariablen,SWT.READ_ONLY);
    }
    
    

	/**
	 * @param treeViewer the treeViewer to set
	 */
	public static void setTreeViewer(TreeViewer treeViewer) {
		MyCellEditors.treeViewer = treeViewer;
		setEditor();
		myCellEditor = new TextCellEditor(treeViewer.getTree());
	}
	
	
	
}
