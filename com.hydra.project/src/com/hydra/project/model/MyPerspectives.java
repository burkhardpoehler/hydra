/**
 * 
 */
package com.hydra.project.model;

/**
 * @author Poehler
 * Bestimmt die benötigten Perspektiven
 */
public class MyPerspectives {

	private Boolean projectCalendarPersepective = false;
	private Boolean standardPersepective = true;
	
	
	
	/**
	 * @return the projectCalendarPersepective
	 */
	public Boolean getProjectCalendarPersepective() {
		return projectCalendarPersepective;
	}
	/**
	 * @param projectCalendarPersepective the projectCalendarPersepective to set
	 */
	public void setProjectCalendarPersepective(
			Boolean projectCalendarPersepective) {
		this.projectCalendarPersepective = projectCalendarPersepective;
	}
	/**
	 * @return the standardPersepective
	 */
	public Boolean getStandardPersepective() {
		return standardPersepective;
	}
	/**
	 * @param standardPersepective the standardPersepective to set
	 */
	public void setStandardPersepective(Boolean standardPersepective) {
		this.standardPersepective = standardPersepective;
	}
	
}
