package com.hydra.project.model;


/**
 * Enthält alle Einstellungen der Firma
 * @author Burkhard Pöhler
 *
 */
public class MyCompanySettings {
	

	private String firma ="Lödige Industries";					
	private String firmensparte ="Lödige Systems";				
	private String strasse ="Wilhelm Lödige Strasse 1";			
	private String postleitzahl ="123xxx";							
	private String ort ="Warburg Scherfede";		
	private String land ="Deutschland";		
	private String nutzer ="jedermann";		
	
	public MyCompanySettings(){
		nutzer = System.getProperty("user.name");
	}

	/**
	 * @return the firma
	 */
	public String getFirma() {
		return firma;
	}


	/**
	 * @param firma the firma to set
	 */
	public void setFirma(String firma) {
		this.firma = firma;
	}


	/**
	 * @return the firmensparte
	 */
	public String getFirmensparte() {
		return firmensparte;
	}


	/**
	 * @param firmensparte the firmensparte to set
	 */
	public void setFirmensparte(String firmensparte) {
		this.firmensparte = firmensparte;
	}


	/**
	 * @return the strasse
	 */
	public String getStrasse() {
		return strasse;
	}


	/**
	 * @param strasse the strasse to set
	 */
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}


	/**
	 * @return the postfach
	 */
	public String getPostleitzahl() {
		return postleitzahl;
	}


	/**
	 * @param postfach the postfach to set
	 */
	public void setPostleitzahl(String postleitzahl) {
		this.postleitzahl = postleitzahl;
	}


	/**
	 * @return the ort
	 */
	public String getOrt() {
		return ort;
	}


	/**
	 * @param ort the ort to set
	 */
	public void setOrt(String ort) {
		this.ort = ort;
	}


	/**
	 * @return the land
	 */
	public String getLand() {
		return land;
	}


	/**
	 * @param land the land to set
	 */
	public void setLand(String land) {
		this.land = land;
	}


	/**
	 * @return the nutzer
	 */
	public String getNutzer() {
		return System.getProperty("user.name");
	}


	/**
	 * @param nutzer the nutzer to set
	 */
	public void setNutzer(String nutzer) {
		this.nutzer = nutzer;
	}


}



