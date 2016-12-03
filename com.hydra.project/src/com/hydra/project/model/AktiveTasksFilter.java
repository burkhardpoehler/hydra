package com.hydra.project.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;



/**
 * Filterklasse für die Tasks
 * @param tasks the tasks to set
 */
public class AktiveTasksFilter extends ViewerFilter{

	private static Boolean filterActive = false;
	
	/**
	 * Blendet alle abgewählten Task aus
	 * @param viewer die Tabelle
	 * @param parentElement die Tabellenelemente
	 * @param element
	 * @return wenn true dann wird der Datensatz dargestellt
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		MyTasksModel m = (MyTasksModel) element;
	    if (filterActive) {		//wenn Filter eingeschaltet dann prüfen
			if (Boolean.valueOf(m.getTask().getVariablenWert())) {
				return true;
			} else{
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the filterActive
	 */
	public Boolean getFilterActive() {
		return filterActive;
	}

	/**
	 * @param filterActive the filterActive to set
	 */
	public static void setFilterActive(Boolean flag) {
		filterActive = flag;
	}

}
