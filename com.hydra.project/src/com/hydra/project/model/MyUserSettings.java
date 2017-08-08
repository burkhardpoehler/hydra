package com.hydra.project.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.hydra.project.parts.LogfileView;




/**
 * Enthält alle Einstellungen des Nutzers
 * @author Burkhard Pöhler
 *
 */
public class MyUserSettings {
	private String user = "";						// Name des Nutzers
	private String userDir = "";					// Das Arbeitsverzeichnis des Nutzers
	private String userHome = "";					// Das Heimverzeichnis des Nutzers
	private String userEmail = "";					// Die Email Adresse des Nutzers
	private String userShort = "";					// Das Kurzzeichen des Nutzers
	private ArrayList <String> projects;				//Liste zuletzt geöffneten Projekte
//	public ArrayList <String> projects;				//Liste zuletzt geöffneten Projekte

	public MyUserSettings(){
		projects = new ArrayList<String>(5);
		user = System.getProperty("user.name");
		userDir = System.getProperty("user.dir");
		userHome = System.getProperty("user.home");
		userEmail = "";
		userShort = "";
		projects = new  ArrayList<String>(5);
	}
	
	/**
	 * Fügt das Projekt an letzter Position an
	 * @param project das aktuell Projekt
	 */
	public void addProject(String project) {
		if (projects == null){
			projects = new  ArrayList<String>(5);
		}
		Boolean flag = true;
		
		for (int i=0; i<projects.size();i++) {
			if (projects.get(i).equals(project)) {
				LogfileView.log(this, "Projekt ist bereits in Liste: " + project, SWT.ICON_INFORMATION);
				flag = false;						// Projekt existiert schon
				break;
			}
		}
		if (flag) projects.add(project); 			// hängt hinten an				
	}
	
	/**
	 * Entfernt das Projekt aus der Liste
	 * @param project das aktuell Projekt
	 */
	public void removeProject(String project) {
		
		for (int i=0; i<projects.size();i++) {
			if (projects.get(i).equals(project)) {
				projects.set(i, "empty");
				LogfileView.log(this, "Projekt aus Liste entfernt: " + project, SWT.ICON_INFORMATION);
			}
		}
		clearProjectList();
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MySettings [nutzer=" + user + "]";
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the userDir
	 */
	public String getUserDir() {
		return userDir;
	}

	/**
	 * @param userDir the userDir to set
	 */
	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}

	/**
	 * @return the userHome
	 */
	public String getUserHome() {
		return userHome;
	}

	/**
	 * @param userHome the userHome to set
	 */
	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}

	/**
	 * @return the projects
	 */
	public ArrayList<String> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(ArrayList<String> projects) {
		this.projects = projects;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userShort
	 */
	public String getUserShort() {
		return userShort;
	}

	/**
	 * @param userShort the userShort to set
	 */
	public void setUserShort(String userShort) {
		this.userShort = userShort;
	}


}



