/**
 * 
 */
package com.hydra.project.editors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.hydra.project.model.MyPropertyItem;

/**
 * @author Poehler
 *
 */
public class MyTextCellEditor extends EditingSupport{
	 private final TableViewer viewer;
	  private final CellEditor editor;

	  public MyTextCellEditor(TableViewer viewer) {
	    super(viewer);
	    this.viewer = viewer;
	    this.editor = new TextCellEditor(viewer.getTable());
	  }

	  @Override
	  protected CellEditor getCellEditor(Object element) {
	    return editor;
	  }

	  @Override
	  protected boolean canEdit(Object element) {
	    return true;
	  }

	  @Override
	  protected Object getValue(Object element) {
	    return ((MyPropertyItem) element).getValue();
	  }

	  @Override
	  protected void setValue(Object element, Object userInputValue) {
	    ((MyPropertyItem) element).setValue(String.valueOf(userInputValue));
	    viewer.update(element, null);
	  }
	
	
	
	
	
}
