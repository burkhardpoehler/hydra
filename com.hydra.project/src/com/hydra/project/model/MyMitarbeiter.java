package com.hydra.project.model;

import java.util.Date;

/**
 * @author Burkhard P�hler
 *
 */
public class MyMitarbeiter {

	private long satz;
	private String nummer;
	private String name;
	private String titel;
	private String position;
	private String firma;
	private String abteilung;
	private String geschlecht;
	private Date ge�ndertAm;
	private String ge�ndertDurch;
	private Date buchungsdatum;
	
	/**
	 * @param mitarbeiternummer
	 */
	public MyMitarbeiter(String mitarbeiternummer) {
//		super();
		initialize();
		nummer = mitarbeiternummer;
	}

	/**
	 */
	public MyMitarbeiter() {
//		super();
		initialize();
	}
	
	
	private void initialize(){
		this.satz = 0;
		this.nummer ="";
		this.name = "";
		this.titel = "";
		this.position = "";
		this.firma = "";
		this.abteilung = "";
		this.geschlecht = "";
		this.ge�ndertDurch ="";
		this.ge�ndertAm = new Date();
		this.buchungsdatum = new Date();
		
	}
	
	
	/**
	 * @return the Mitarbeiternummer
	 */
	public String getNummer() {
		return nummer;
	}


	/**
	 * @param nummer the Mitarbeiternummer to set
	 */
	public void setNummer(String nummer) {
		this.nummer = nummer;
	}


	/**
	 * @return the Mitarbeitername
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the Mitarbeitername to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the Mitarbeitertitel
	 */
	public String getTitel() {
		return titel;
	}


	/**
	 * @param titel the Mitarbeitertitel to set
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
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
	 * @return the abteilung
	 */
	public String getAbteilung() {
		return abteilung;
	}

	/**
	 * @param abteilung the abteilung to set
	 */
	public void setAbteilung(String abteilung) {
		this.abteilung = abteilung;
	}

	/**
	 * @return the geschlecht
	 */
	public String getGeschlecht() {
		return geschlecht;
	}

	/**
	 * @param geschlecht the geschlecht to set
	 */
	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	/**
	 * @return the ge�ndertAm
	 */
	public Date getGe�ndertAm() {
		return ge�ndertAm;
	}

	/**
	 * @param ge�ndertAm the ge�ndertAm to set
	 */
	public void setGe�ndertAm(Date ge�ndertAm) {
		this.ge�ndertAm = ge�ndertAm;
	}

	/**
	 * @return the ge�ndertDurch
	 */
	public String getGe�ndertDurch() {
		return ge�ndertDurch;
	}

	/**
	 * @param ge�ndertDurch the ge�ndertDurch to set
	 */
	public void setGe�ndertDurch(String ge�ndertDurch) {
		this.ge�ndertDurch = ge�ndertDurch;
	}

	/**
	 * @return the buchungsdatum
	 */
	public Date getBuchungsdatum() {
		return buchungsdatum;
	}

	/**
	 * @param buchungsdatum the buchungsdatum to set
	 */
	public void setBuchungsdatum(Date buchungsdatum) {
		this.buchungsdatum = buchungsdatum;
	}

	/**
	 * @return the satz
	 */
	public long getSatz() {
		return satz;
	}

	/**
	 * @param satz the satz to set
	 */
	public void setSatz(long satz) {
		this.satz = satz;
	}
	
	
	
}
