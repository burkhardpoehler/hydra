 
package com.hydra.project.parts;

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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hydra.project.editors.CDateTimeCellEditor;
//import com.hydra.project.editors.MyDateTimeDialogCellEditor;
import com.hydra.project.editors.MyImageDialogCellEditor;
import com.hydra.project.model.MyPropertyItem;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.provider.PropertyTableLabelProvider;
import com.hydra.project.provider.TableModelProvider;

public class PropertyView {
	

//	private static final String MyTreeItemEvent = null;

	private TableViewer viewer;
	
	private static String[]  AuswahlComboBoxVariablentyp = {	
			"String", 
			"BigInteger",
			"Float",
			"Integer",
			"Link",
			"Grafic",
			"Path",
			"Boolean"};
	
    private static String[]  AuswahlComboBoxEditoren = {	
    		"CellEditor", 
			"CheckboxCellEditor",
			"ImageDialogCellEditor",
			"VariablentypComboBoxCellEditor",
			"ColorCellEditor",
			"EditorComboBoxCellEditor",
			"ListComboBoxCellEditor",
			"DateTimeDialogCellEditor"
			};
	
	@Inject
	IEventBroker eventBroker;
	
//	@Inject
//	private ESelectionService selectionService;
//	
	@Inject
	private MDirtyable dirty;

	
	@Inject
	public PropertyView() {
		//TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
//	    GridLayout layout = new GridLayout(6, false);
	    createViewer(parent);
	}
	
    private void createViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns(parent, viewer);
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        viewer.setLabelProvider(new PropertyTableLabelProvider());
        viewer.setContentProvider(new ArrayContentProvider());
        MyTreeItem myTreeItem = null;

        TableModelProvider.generateTable(myTreeItem);
        viewer.setInput(TableModelProvider.getItem());

        // define layout for the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        
//        menuService.registerContextMenu(viewer.getControl(),"com.hydra.project.popupmenu.propertyview");
        
      }
	

    
	private void createColumns(Composite parent, final TableViewer viewer2) {
	    // first column is for the first name
	    TableViewerColumn col0 = createTableViewerColumn("Parameter", 150, 0);
	    
	    col0.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public String getText(Object element) {
	    	  MyPropertyItem p = (MyPropertyItem) element;
	        return p.getProperty();
	      }
	    });
	    

	    // second column is for the last name
	    TableViewerColumn col1 = createTableViewerColumn("Wert", 400, 1);
	    col1.setLabelProvider(new ColumnLabelProvider() {	
	      @Override
	      public String getText(Object element) {
	    	  MyPropertyItem p = (MyPropertyItem) element;
	        return p.getValue();
	      }
	    });
	    

	    col1.setEditingSupport(new EditingSupport(viewer2) {
            protected boolean canEdit(Object element) {
            	if (element instanceof MyPropertyItem) {
                    return ((MyPropertyItem) element).isEditable();
                }
                return false;
            }
 
            protected CellEditor getCellEditor(Object element) {
            	CellEditor cellEditor = null;
            	if (element instanceof MyPropertyItem) {
                	MyPropertyItem p = (MyPropertyItem) element;
                	
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
                		case "ColorCellEditor":
                			cellEditor = colorCellEditor;
                			break;
                		case "EditorComboBoxCellEditor":
                			cellEditor = editorComboBoxCellEditor;
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
 
            protected Object getValue(Object element) {
            	Object result = null;
            	if (element instanceof MyPropertyItem) {
                    String value = ((MyPropertyItem) element).getValue();
                   	String editor= ((MyPropertyItem) element).getEditor();
                    
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
            		case "VariablentypComboBoxCellEditor":		//Eingabe für ComboBox nur als Zahlen von 0 bis n
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
               		case "EditorComboBoxCellEditor":		//Eingabe für ComboBox nur als Zahlen von 0 bis n
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
            		default:
            			result = value;
                   	}                 
                }
                return result;
            }
 
            protected void setValue(Object element, Object value) {
            	String result = null;
            	if (element instanceof MyPropertyItem) {
                   	String editor= ((MyPropertyItem) element).getEditor();
                    
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
            		default:
            			result = value.toString();
                   	}                 
                }
            	((MyPropertyItem) element).setValue(result);
            	setValueMyTreeItem(element,result);           
                viewer2.update(element, null);
            }
            final TextCellEditor textCellEditor = new TextCellEditor(viewer2.getTable());
            final CheckboxCellEditor checkboxCellEditor = new CheckboxCellEditor(viewer2.getTable());
           
            
            final ComboBoxCellEditor variablentypComboBoxCellEditor = new ComboBoxCellEditor(viewer2.getTable(), AuswahlComboBoxVariablentyp,SWT.READ_ONLY);
            
 
            final ComboBoxCellEditor editorComboBoxCellEditor = new ComboBoxCellEditor(viewer2.getTable(), AuswahlComboBoxEditoren,SWT.READ_ONLY);
            final ComboBoxCellEditor listComboBoxCellEditor = new ComboBoxCellEditor(viewer2.getTable(), AuswahlComboBoxEditoren,SWT.READ_ONLY);
            
            final MyImageDialogCellEditor myImageDialogCellEditor = new MyImageDialogCellEditor(viewer2.getTable());
            final ColorCellEditor colorCellEditor = new ColorCellEditor(viewer2.getTable());
            final CDateTimeCellEditor cDateTimeCellEditor = new CDateTimeCellEditor(viewer2.getTable());
//            final MyDateTimeDialogCellEditor myDateTimeDialogCellEditor = new MyDateTimeDialogCellEditor(viewer2.getTable());
            
        });       
	}

	
	
	private void setValueMyTreeItem(Object element, String result){
        // der neue Wert muss dem ausgewählten treeItem übergeben werden
      	
    	if (element instanceof MyPropertyItem) {
    		MyTreeItem myTreeItem = TableModelProvider.getMyTreeItemNew();
    		String property= ((MyPropertyItem) element).getProperty();
          	
    		switch (property) {

    		case "Wert":
    			myTreeItem.setVariablenWert(result);
    			break;
     		case "variablenEinheit":
    			myTreeItem.setVariablenEinheit(result);
    			break;
     		case "variablentyp":
    			myTreeItem.setVariablentyp(result);
    			break;
       		case "bezeichnung":
    			myTreeItem.setBezeichnung(result);
    			break;
    		case "Beschreibung":
    			myTreeItem.setBeschreibung(result);
    			break;
       		case "Parameter":
    			myTreeItem.setParameter(result);
    			break;
       		case "UUID":
    			myTreeItem.setUuid(result);
    			break;
       		case "ParentUUID":
    			myTreeItem.setParentUUID(result);
    			break;
       		case "anzeigeText":
    			myTreeItem.setAnzeigeText(result);
    			break;
     		case "iconDateiname":
    			myTreeItem.setIconDateiname(result);
    			break;
    		case "toolTipText":
    			myTreeItem.setToolTipText(result);
    			break;   
    		case "Editor":
    			myTreeItem.setEditor(result);
    			if (result.equals("CheckboxCellEditor")){
    				myTreeItem.setVariablentyp("Boolean");
    				myTreeItem.setVariablenWert("false");
    			}
    			if (result.equals("CellEditor")){
    				myTreeItem.setVariablentyp("String");
    			}
    			break;   			
    		case "Hat Kinder":
    			myTreeItem.setHasChildren(Boolean.parseBoolean(result));
    			break;
    		case "expanded":
    			myTreeItem.setExpanded(Boolean.parseBoolean(result));
    			break;
    		case "checked":
    			myTreeItem.setChecked(Boolean.parseBoolean(result));
    			break;
       		case "grayed":
    			myTreeItem.setGrayed(Boolean.parseBoolean(result));
    			break;
      		case "hasPreChild":
    			myTreeItem.setHasPreChild(Boolean.parseBoolean(result));
    			break;
     		case "hasPostChild":
    			myTreeItem.setHasPostChild(Boolean.parseBoolean(result));
    			break;
     		case "ueberschrift":
    			myTreeItem.setUeberschrift(Boolean.parseBoolean(result));
    			break;
    		case "isWurzel":
    			myTreeItem.setWurzel(Boolean.parseBoolean(result));
    			break;
      		case "isBasis":
    			myTreeItem.setIsBasis(Boolean.parseBoolean(result));
    			break;
     		case "isParameter":
    			myTreeItem.setIsParameter(Boolean.parseBoolean(result));
    			break;
     		case "isEigenschaft":
    			myTreeItem.setIsEigenschaft(Boolean.parseBoolean(result));
    			break;   			
    		case "isVorlage":
    			myTreeItem.setIsVorlage(Boolean.parseBoolean(result));
    			break;   		
    		case "isProjektBaum":
    			myTreeItem.setIsProjektBaum(Boolean.parseBoolean(result));
    			break;   
    		case "isProjekt":
    			myTreeItem.setIsProjekt(Boolean.parseBoolean(result));
    			break;   
     		case "isObersterKnoten":
    			myTreeItem.setObersterKnoten(Boolean.parseBoolean(result));
    			break;      			
     		case "isStrukturknoten":
    			myTreeItem.setStrukturknoten(Boolean.parseBoolean(result));
    			break;      
     		case "isDummy":
    			myTreeItem.setDummy(Boolean.parseBoolean(result));
    			break;      
    		case "Index":
    			myTreeItem.setIndex(Integer.parseInt(result));
    			break;
    			
    		case "Datenbankindex":
    			myTreeItem.setDbIndex(Integer.parseInt(result));
    			break;
      		case "Datenbankname":
    			myTreeItem.setDatenbankName(result);
    			break;
    			
           	}   
    		myEventBroker(myTreeItem);
    		
    	}		
	}
	
	private void myEventBroker(MyTreeItem myTreeItem){
		//sendet die geänderten Informationen in den Datenbus
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully) System.out.println("Nachricht aus PropertyViewer gesendet "+ myTreeItem.getVariablenWert());
	}
	
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		LogfileView.log("Nachricht in PropertyViewer empfangen "+ myTreeItem.getVariablenWert());
		if (viewer != null && myTreeItem != null) {
	        TableModelProvider.generateTable(myTreeItem);
	        viewer.setInput(TableModelProvider.getItem());
		}	
	}
	
	 private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
		        SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(true);
		    return viewerColumn;
		  }
	
	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
		LogfileView.log(this + " setFocus is called");
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	
	@ Inject
	public void setSelection (@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional MyTreeItem myTreeItem) {
	//Process Selection
//		System.out.println("SelectionChanged in Tree");
		LogfileView.log(this.getClass(), "Neue Selektion in PropertyViewer empfangen.");
		TableModelProvider.generateTable(myTreeItem);
		if (viewer != null & myTreeItem != null) {
			viewer.setInput(TableModelProvider.getItem());
		}

	}

	/**
	 * @return the auswahlComboBoxVariablentyp
	 */
	public static String[] getAuswahlComboBoxVariablentyp() {
		return AuswahlComboBoxVariablentyp;
	}

	/**
	 * @return the auswahlComboBoxEditoren
	 */
	public static String[] getAuswahlComboBoxEditoren() {
		return AuswahlComboBoxEditoren;
	}
}