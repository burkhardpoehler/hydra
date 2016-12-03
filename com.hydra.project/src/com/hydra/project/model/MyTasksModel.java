/**
 * 
 */
package com.hydra.project.model;

/**
 * Model enthält alle Tasks für die Arbeitsschritte eines Projektes
 * Gespeichert werden nur die Verweise auf die TreeItems
 * 
 * @author Poehler
 *
 */
public class MyTasksModel {
	
	private MyTreeItem Arbeitsschritt;
	private MyTreeItem Typ;
	private MyTreeItem Beschreibung;
	private MyTreeItem Dauer;
	private MyTreeItem Erledigt;
	private MyTreeItem Task;
	private MyTreeItem Fahne;
	
	
	public MyTasksModel(){
	}


	/**
	 * @return the arbeitsschritt
	 */
	public MyTreeItem getArbeitsschritt() {
		return Arbeitsschritt;
	}


	/**
	 * @param arbeitsschritt the arbeitsschritt to set
	 */
	public void setArbeitsschritt(MyTreeItem arbeitsschritt) {
		Arbeitsschritt = arbeitsschritt;
	}


	/**
	 * @return the typ
	 */
	public MyTreeItem getTyp() {
		return Typ;
	}


	/**
	 * @param typ the typ to set
	 */
	public void setTyp(MyTreeItem typ) {
		Typ = typ;
	}


	/**
	 * @return the beschreibung
	 */
	public MyTreeItem getBeschreibung() {
		return Beschreibung;
	}


	/**
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(MyTreeItem beschreibung) {
		Beschreibung = beschreibung;
	}


	/**
	 * @return the dauer
	 */
	public MyTreeItem getDauer() {
		return Dauer;
	}


	/**
	 * @param dauer the dauer to set
	 */
	public void setDauer(MyTreeItem dauer) {
		Dauer = dauer;
	}


	/**
	 * @return the erledigt
	 */
	public MyTreeItem getErledigt() {
		return Erledigt;
	}


	/**
	 * @param erledigt the erledigt to set
	 */
	public void setErledigt(MyTreeItem erledigt) {
		Erledigt = erledigt;
	}


	/**
	 * @return the task
	 */
	public MyTreeItem getTask() {
		return Task;
	}


	/**
	 * @param task the task to set
	 */
	public void setTask(MyTreeItem task) {
		Task = task;
	}


	/**
	 * @return the fahne
	 */
	public MyTreeItem getFahne() {
		return Fahne;
	}


	/**
	 * @param fahne the fahne to set
	 */
	public void setFahne(MyTreeItem fahne) {
		Fahne = fahne;
	}



	
}
