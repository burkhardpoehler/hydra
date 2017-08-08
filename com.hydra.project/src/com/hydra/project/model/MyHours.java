package com.hydra.project.model;
import java.util.Date;

/**
 * 
 */

/**
 * @author Burkhard Pöhler
 *
 */
public class MyHours {
	
	private long satz;
	private String nummer;
	private String name;
	private String beschreibung;
	private String kostenstelle;
	private String kostentraeger;
	private double menge;
	private String status;
	private String projekt;
	private String aufgabe;
	private Date geändertAm;
	private String geändertDurch;
	private Date buchungsdatum;
	private long lfdnr;
	private String arbeitstypencode;

//	/**
//	 * 
//	 */
//	public MyHours(long satznummer) {
//		super();
//		satz = satznummer;
//	}

	/**
	 * Konstruktor
	 */
	public MyHours() {
//		super();
	}


	/**
	 * @return the string
	 */
	public String toString() {
		String string;
		string= this.getSatz()+","+this.getBuchungsdatum()+","+this.getNummer()+","+this.getMenge()
				+","+this.getKostenstelle()+","+this.getKostentraeger()+","+this.getName();
		return string;
	}


	/**
	 * @return the nummer
	 */
	public String getNummer() {
		return nummer;
	}



	/**
	 * @param nummer the nummer to set
	 */
	public void setNummer(String nummer) {
		this.nummer = nummer;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the kostenstelle
	 */
	public String getKostenstelle() {
		return kostenstelle;
	}



	/**
	 * @param kostenstelle the kostenstelle to set
	 */
	public void setKostenstelle(String kostenstelle) {
		this.kostenstelle = kostenstelle;
	}



	/**
	 * @return the kostentraeger
	 */
	public String getKostentraeger() {
		return kostentraeger;
	}



	/**
	 * @param kostentraeger the kostentraeger to set
	 */
	public void setKostentraeger(String kostentraeger) {
		this.kostentraeger = kostentraeger;
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
	 * @return the menge
	 */
	public double getMenge() {
		return menge;
	}



	/**
	 * @param menge the menge to set
	 */
	public void setMenge(double menge) {
		this.menge = menge;
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



	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 * <p>
	 * darf nur die folgenden Werte annehmen:
	 *<p>
	 * "Ist"	für alle Daten, die von Dynamics überneommen werden
	 * <p>
	 * "Soll"	für alle Planungen
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return the projekt
	 */
	public String getProjekt() {
		return projekt;
	}



	/**
	 * @param projekt the projekt to set
	 */
	public void setProjekt(String projekt) {
		this.projekt = projekt;
	}



	/**
	 * @return the aufgabe
	 */
	public String getAufgabe() {
		return aufgabe;
	}



	/**
	 * @param aufgabe the aufgabe to set
	 */
	public void setAufgabe(String aufgabe) {
		this.aufgabe = aufgabe;
	}



	/**
	 * @return the geändertAm
	 */
	public Date getGeändertAm() {
		return geändertAm;
	}



	/**
	 * @param geändertAm the geändertAm to set
	 */
	public void setGeändertAm(Date geändertAm) {
		this.geändertAm = geändertAm;
	}



	/**
	 * @return the geändertDurch
	 */
	public String getGeändertDurch() {
		return geändertDurch;
	}



	/**
	 * @param geändertDurch the geändertDurch to set
	 */
	public void setGeändertDurch(String geändertDurch) {
		this.geändertDurch = geändertDurch;
	}



	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}



	/**
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}


	/**
	 * @return the lfdnr
	 */
	public long getLfdnr() {
		return lfdnr;
	}


	/**
	 * @param lfdnr the lfdnr to set
	 */
	public void setLfdnr(long lfdnr) {
		this.lfdnr = lfdnr;
	}


	/**
	 * @return the arbeitstypen
	 */
	public String getArbeitstypencode() {
		return arbeitstypencode;
	}


	/**
	 * @param arbeitstypen the arbeitstypen to set
	 */
	public void setArbeitstypencode(String arbeitstypencode) {
		this.arbeitstypencode = arbeitstypencode;
	}
}
