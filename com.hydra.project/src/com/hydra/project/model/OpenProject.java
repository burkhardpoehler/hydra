package com.hydra.project.model;


/**
 * Enthält ein zuletzt geöffnetes Projekt
 * @author Burkhard Pöhler
 *
 */
public class OpenProject {
	private String project;
	
	public OpenProject(String newProject){		
		setProject(newProject);
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
}
