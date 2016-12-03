/**
 * 
 */
package com.hydra.project.model;

import lifecycle.MyImageHandler;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.hydra.project.parts.ProjectCalendarView;

/**
 * Unterstützt die Erstellung und Funktion des Tasktabelle im KalenderView
 * @author Poehler
 *
 */
public class TasksTools {

	private static TableViewer tableTasksViewer;
	private static Image CHECKED ;
	private static Image UNCHECKED ;
	private static Image FLAG ;
	private static Image TICK ;
	private static AktiveTasksFilter taskFilter;

	/**
	 * erzeugt die Tabelle für die Tasks
	 * @author Poehler
	 * @param composite das übergeordnete Fenster
	 */
	public static void TabelleErzeugen(Composite composite){
		CHECKED = MyImageHandler.getImage("Check_YES.gif");
		UNCHECKED = MyImageHandler.getImage("selection.png");
		FLAG = MyImageHandler.getImage("flag-checker.png");
		TICK = MyImageHandler.getImage("tick-white.png");
		
		tableTasksViewer = new TableViewer(composite, SWT.H_SCROLL| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		SpaltenErzeugen(tableTasksViewer);
		tableTasksViewer.getTable().setHeaderVisible(true);
		tableTasksViewer.getTable().setLinesVisible(true);
		tableTasksViewer.setContentProvider(ArrayContentProvider.getInstance());
		taskFilter = new AktiveTasksFilter();
		tableTasksViewer.addFilter(taskFilter);
		
		tableTasksViewer.setInput(ProjectCalendarView.getTasks());
	}
	
	public static void TabelleAktualisieren(){
		tableTasksViewer.setInput(ProjectCalendarView.getTasks());
	}
	
	public static void SpaltenErzeugen(final TableViewer viewer){
		
		// create a column
		TableViewerColumn col1 = new TableViewerColumn(viewer, SWT.NONE);
		col1.getColumn().setWidth(50);
		col1.getColumn().setText("Typ");
		col1.setLabelProvider(new ColumnLabelProvider() {
		  @Override
		  public String getText(Object element) {
		    MyTasksModel m = (MyTasksModel) element;
		    return m.getTyp().getVariablenWert();
		  }
		});
		
		// create a column (picture only)
		TableViewerColumn col2 = new TableViewerColumn(viewer,  SWT.CENTER |  SWT.NONE);
		col2.getColumn().setWidth(50);
		col2.getColumn().setText("Task");
		col2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
			  return null;  // no string representation, we only want to display the image
			}

			@Override
			public Image getImage(Object element) {
				MyTasksModel m = (MyTasksModel) element;
				String string = m.getTask().getVariablenWert();
				if (string.equals("true")) {
					return CHECKED;
				} 
			  return UNCHECKED;
			}
		});
       // Spalte editierbar machen:
		col2.setEditingSupport(new EditingSupport(viewer) {
            protected boolean canEdit(Object element) {
                return true;
            }
            protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(viewer.getTable());
            }
            protected Object getValue(Object element) {
            	MyTasksModel m = (MyTasksModel) element;
            	String value = m.getTask().getVariablenWert();
            	Boolean flag = Boolean.valueOf(value);
                return flag;
            }
            protected void setValue(Object element, Object value) {
            	MyTasksModel m = (MyTasksModel) element;
            	MyTreeItem myTreeItem = m.getTask();
            	myTreeItem.setVariablenWert(String.valueOf(value));
            	ProjectCalendarView.updateMyTreeItem(myTreeItem);
            }
        });       
			
		// create a column (picture only)
		TableViewerColumn col3 = new TableViewerColumn(viewer,  SWT.CENTER | SWT.NONE);
		col3.getColumn().setWidth(50);
		col3.getColumn().setText("Erledigt");
		col3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
			  return null;  // no string representation, we only want to display the image
			}

			public Image getImage(Object element) {
				MyTasksModel m = (MyTasksModel) element;
				String string = m.getErledigt().getVariablenWert();
				if (string.equals("true")) {
					return TICK;
				} 
			  return UNCHECKED;
			}
		});
       // Spalte editierbar machen:
		col3.setEditingSupport(new EditingSupport(viewer) {
            protected boolean canEdit(Object element) {
                return true;
            }
            protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(viewer.getTable());
//						return new TextCellEditor(viewer.getTable());
            }
            protected Object getValue(Object element) {
            	MyTasksModel m = (MyTasksModel) element;
            	String value = m.getErledigt().getVariablenWert();
            	Boolean flag = Boolean.valueOf(value);
                return flag;
            }
            protected void setValue(Object element, Object value) {
            	MyTasksModel m = (MyTasksModel) element;
            	MyTreeItem myTreeItem = m.getErledigt();
            	myTreeItem.setVariablenWert(String.valueOf(value));
            	ProjectCalendarView.updateMyTreeItem(myTreeItem);
            }
        });       
		
		// create a column (picture only)
		TableViewerColumn col4 = new TableViewerColumn(viewer, SWT.CENTER | SWT.NONE);
		col4.getColumn().setWidth(50);
		col4.getColumn().setText("Fahne");
		col4.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
			  return null;  // no string representation, we only want to display the image
			}

			public Image getImage(Object element) {
				MyTasksModel m = (MyTasksModel) element;
				String string = m.getFahne().getVariablenWert();
				if (string.equals("true")) {
					return FLAG;
				} 
			  return UNCHECKED;
			}
		});
		col4.setEditingSupport(new EditingSupport(viewer) {
            protected boolean canEdit(Object element) {
                return true;
            }
            protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(viewer.getTable());
//						return new TextCellEditor(viewer.getTable());
            }
            protected Object getValue(Object element) {
            	MyTasksModel m = (MyTasksModel) element;
            	String value = m.getFahne().getVariablenWert();
            	Boolean flag = Boolean.valueOf(value);
                return flag;
            }
            protected void setValue(Object element, Object value) {
            	MyTasksModel m = (MyTasksModel) element;
            	MyTreeItem myTreeItem = m.getFahne();
            	myTreeItem.setVariablenWert(String.valueOf(value));
            	ProjectCalendarView.updateMyTreeItem(myTreeItem);
            }
        });       
		

		// create a column
		TableViewerColumn col5 = new TableViewerColumn(viewer, SWT.NONE);
		col5.getColumn().setWidth(50);
		col5.getColumn().setText("Dauer");
		col5.setLabelProvider(new ColumnLabelProvider() {
		  @Override
		  public String getText(Object element) {
		    MyTasksModel m = (MyTasksModel) element;
		    return m.getDauer().getVariablenWert();
		  }
		});
		col5.setEditingSupport(new EditingSupport(viewer) {
            protected boolean canEdit(Object element) {
                return true;
            }
            protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(viewer.getTable());
            }
            protected Object getValue(Object element) {
            	MyTasksModel m = (MyTasksModel) element;
            	String value = m.getDauer().getVariablenWert();
                return value;
            }
            protected void setValue(Object element, Object value) {
            	MyTasksModel m = (MyTasksModel) element;
            	MyTreeItem myTreeItem = m.getDauer();
            	myTreeItem.setVariablenWert((String) value);
            	ProjectCalendarView.updateMyTreeItem(myTreeItem);
            }
        });       
		
		
		// create a column
		TableViewerColumn col6 = new TableViewerColumn(viewer, SWT.NONE);
		col6.getColumn().setWidth(300);
		col6.getColumn().setText("Beschreibung");
		col6.setLabelProvider(new ColumnLabelProvider() {
		  @Override
		  public String getText(Object element) {
		    MyTasksModel m = (MyTasksModel) element;
		    return m.getBeschreibung().getVariablenWert();
		  }
		});
	}
	

}
