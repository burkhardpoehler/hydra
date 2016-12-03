package com.hydra.project.model;


/**
 * Enth�lt ein zuletzt ge�ffnetes Projekt
 * @author Burkhard P�hler
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
