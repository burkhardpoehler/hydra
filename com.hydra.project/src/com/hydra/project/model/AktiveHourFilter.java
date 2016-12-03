package com.hydra.project.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.hydra.project.parts.ProjectCalendarView;



/**
 * Filterklasse für die Tasks
 * @param tasks the tasks to set
 */
public class AktiveHourFilter extends ViewerFilter{

	private static Boolean filterActive = true;
	
	/**
	 * Blendet alle abgewählten Stunden aus
	 * @param viewer die Tabelle
	 * @param parentElement die Tabellenelemente
	 * @param element
	 * @return wenn true dann wird der Datensatz dargestellt
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		MyHours myHours =(MyHours) element;
		filterActive= ProjectCalendarView.getAlleStundenAnzeigen();
		
	    if (filterActive) {		//wenn Filter eingeschaltet dann prüfen
	    	//hole angeklicktes Datum und entferne Zeiten
	    	Calendar calSelection = new GregorianCalendar();
	    	calSelection.setTime(ProjectCalendarView.getDateDatum());
	    	calSelection.set(Calendar.HOUR_OF_DAY, 0);
	    	calSelection.set(Calendar.MINUTE, 0);
	    	calSelection.set(Calendar.SECOND, 0);
	    	calSelection.set(Calendar.MILLISECOND, 0);
	    	String a = calSelection.toString();
	    	
	    	//hole Datensatz und entferne Zeiten
	    	Calendar calElement = new GregorianCalendar();
	    	calElement.setTime(myHours.getBuchungsdatum());
	    	calElement.set(Calendar.HOUR_OF_DAY, 0);
	    	calElement.set(Calendar.MINUTE, 0);
	    	calElement.set(Calendar.SECOND, 0);
	    	calElement.set(Calendar.MILLISECOND, 0);
	    	String b = calElement.toString();
	    	
	    	if (a.equals(b)) {	//gültiges Datum
				
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
