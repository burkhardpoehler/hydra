package com.hydra.project.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Enth�lt alle Einstellungen des Nutzers
 * @author Burkhard P�hler
 *
 */
public class MySettings {
	

	private String nutzer;							// Name des Nutzers
	protected List <String> projects;				//Liste zuletzt ge�ffneten Projekte

	public MySettings(){
		projects = new ArrayList<String>(5);
		nutzer = "";
	}
	
	/**
	 * F�gt das Projekt an letzter Position an
	 * @param project das aktuell Projekt
	 */
	public void addProject(String project) {
		if (projects == null){
			projects = new  ArrayList<String>(5);
		}
		projects.add(project); 						// h�ngt hinten an
	}
	
	/**
	 * Entfernt das Projekt aus der Liste
	 * @param project das aktuell Projekt
	 */
	public void removeProject(String project) {
		
		for (int i=0; i<projects.size();i++) {
			if (projects.get(i).equals(project)) {
				projects.set(i, "empty");
			}
		}
	}
	
	/**
	 * R�umt die Projektliste auf
	 * Entfernt alle mit NULL oder "empty" markierten Eintr�ge
	 */
	public void clearProjectList() {

		for (int i=0; i<projects.size();i++){
			if (projects.get(i)== null){
				projects.remove(i);
			}
			if (projects.get(i)== "empty"){
				projects.remove(i);
			}
		}
	}
	
	/**
	 * @return the nutzer
	 */
	public String getNutzer() {
		return nutzer;
	}

	/**
	 * @param nutzer the nutzer to set
	 */
	public void setNutzer(String nutzer) {
		this.nutzer = nutzer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MySettings [nutzer=" + nutzer + "]";
	}


}



