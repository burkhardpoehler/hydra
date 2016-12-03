package com.hydra.project.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Enthält alle Einstellungen des Nutzers
 * @author Burkhard Pöhler
 *
 */
public class MySettings {
	

	private String nutzer;							// Name des Nutzers
	protected List <String> projects;				//Liste zuletzt geöffneten Projekte

	public MySettings(){
		projects = new ArrayList<String>(5);
		nutzer = "";
	}
	
	/**
	 * Fügt das Projekt an letzter Position an
	 * @param project das aktuell Projekt
	 */
	public void addProject(String project) {
		if (projects == null){
			projects = new  ArrayList<String>(5);
		}
		projects.add(project); 						// hängt hinten an
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
	 * Räumt die Projektliste auf
	 * Entfernt alle mit NULL oder "empty" markierten Einträge
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



