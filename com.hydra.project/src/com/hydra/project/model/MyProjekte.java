package com.hydra.project.model;

import java.util.Date;

/**
 * @author Burkhard P�hler
 *
 */
public class MyProjekte {

	private long satz;
	private String auftragsnummer;
	private String projektbezeichnung1;
	private String projektbezeichnung2;
	private String beschreibung;
	private String kostenstelle;
	private String kostentr�ger;
	private String firma;
	private Date ge�ndertAm;
	private String ge�ndertDurch;
	private Date buchungsdatum;
	
	/**
	 * @param mitarbeiternummer
	 */
	public MyProjekte(String mitarbeiternummer) {
//		super();
		initialize();
		auftragsnummer = mitarbeiternummer;
	}

	/**
	 */
	public MyProjekte() {
//		super();
		initialize();
	}
	
	
	private void initialize(){
		this.satz = 0;
		this.auftragsnummer ="";
		this.projektbezeichnung1 = "";
		this.projektbezeichnung2 = "";
		this.beschreibung = "";
		this.kostenstelle = "";
		this.kostentr�ger = "";
		this.firma = "";
		this.ge�ndertDurch ="";
		this.ge�ndertAm = new Date();
		this.buchungsdatum = new Date();
		
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
	 * @return the auftragsnummer
	 */
	public String getAuftragsnummer() {
		return auftragsnummer;
	}

	/**
	 * @param auftragsnummer the auftragsnummer to set
	 */
	public void setAuftragsnummer(String auftragsnummer) {
		this.auftragsnummer = auftragsnummer;
	}

	/**
	 * @return the projektbezeichnung1
	 */
	public String getProjektbezeichnung1() {
		return projektbezeichnung1;
	}

	/**
	 * @param projektbezeichnung1 the projektbezeichnung1 to set
	 */
	public void setProjektbezeichnung1(String projektbezeichnung1) {
		this.projektbezeichnung1 = projektbezeichnung1;
	}

	/**
	 * @return the projektbezeichnung2
	 */
	public String getProjektbezeichnung2() {
		return projektbezeichnung2;
	}

	/**
	 * @param projektbezeichnung2 the projektbezeichnung2 to set
	 */
	public void setProjektbezeichnung2(String projektbezeichnung2) {
		this.projektbezeichnung2 = projektbezeichnung2;
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
	 * @return the kostentr�ger
	 */
	public String getKostentr�ger() {
		return kostentr�ger;
	}

	/**
	 * @param kostentr�ger the kostentr�ger to set
	 */
	public void setKostentr�ger(String kostentr�ger) {
		this.kostentr�ger = kostentr�ger;
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
	
	
	
	
}
