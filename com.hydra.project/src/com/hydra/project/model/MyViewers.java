/**
 * 
 */
package com.hydra.project.model;

import com.hydra.project.parts.ProjectCalendarView;

/**
 * @author Poehler
 *	Bestimmt die benötigten Viewer
 */
public class MyViewers {

	private static Boolean baumViewer = true;
	private static Boolean editorenViewer = false;
	private static Boolean projectCalendarViewer = false;
	private static Boolean propertyViewer = true;
	private static Boolean sampleViewer = true;
	private static Boolean solverViewer = true;
	
	public MyViewers() {
		
	}

	
	/**
	 * @author Burkhard Pöhler
	 * öffnet die benötigten Viewer
	 * @param	myTreeItem der oberste Knoten eines neuen Projektes
	 */
	public static void setViewers(MyTreeItem myTreeItem){
		//suche nach Viewern
//		MyTreeItem myViewer = TreeTools.findMyTreeItemByID(myTreeItem, "P101.001.002");
//		
//		if(myViewer != null){
//			switch (myViewer.getVariablenWert()){
//			
//			case ("Projektkalender"):
//				projectCalendarViewer = true;
////				ProjectCalendarView.restartControls();
//				break;
//			case ("Editor"):
//				editorenViewer = true;
//				break;
//			};
//		}
	}
	
	
	
	/**
	 * @return the baumViewer
	 */
	public static Boolean getBaumViewer() {
		return baumViewer;
	}
	
	/**
	 * @param baumViewer the baumViewer to set
	 */
	public void setBaumViewer(Boolean baumViewer) {
		MyViewers.baumViewer = baumViewer;
	}
	
	/**
	 * @return the editorenViewer
	 */
	public static Boolean getEditorenViewer() {
		return editorenViewer;
	}
	
	/**
	 * @param editorenViewer the editorenViewer to set
	 */
	public void setEditorenViewer(Boolean editorenViewer) {
		MyViewers.editorenViewer = editorenViewer;
	}
	
	/**
	 * @return the projectCalendarViewer
	 */
	public Boolean getProjectCalendarViewer() {
		return projectCalendarViewer;
	}
	
	/**
	 * @param projectCalendarViewer the projectCalendarViewer to set
	 */
	public void setProjectCalendarViewer(Boolean projectCalendarViewer) {
		MyViewers.projectCalendarViewer = projectCalendarViewer;
	}
	
	/**
	 * @return the propertyViewer
	 */
	public static Boolean getPropertyViewer() {
		return propertyViewer;
	}
	
	/**
	 * @param propertyViewer the propertyViewer to set
	 */
	public void setPropertyViewer(Boolean propertyViewer) {
		MyViewers.propertyViewer = propertyViewer;
	}
	
	/**
	 * @return the sampleViewer
	 */
	public static Boolean getSampleViewer() {
		return sampleViewer;
	}
	
	/**
	 * @param sampleViewer the sampleViewer to set
	 */
	public void setSampleViewer(Boolean sampleViewer) {
		MyViewers.sampleViewer = sampleViewer;
	}
	
	/**
	 * @return the solverViewer
	 */
	public static Boolean getSolverViewer() {
		return solverViewer;
	}
	
	/**
	 * @param solverViewer the solverViewer to set
	 */
	public void setSolverViewer(Boolean solverViewer) {
		MyViewers.solverViewer = solverViewer;
	}
	
	

}
