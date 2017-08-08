/**
 * 
 */
package com.hydra.project.database;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
//import org.apache.poi.*;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.s

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.query.Query;
import com.hydra.project.database.calc.SummeAufträge;
import com.hydra.project.database.calc.SummeKostenträger;
import com.hydra.project.dialogs.MyProgressBarJob;
import com.hydra.project.model.MyHours;
import com.hydra.project.model.MyMitarbeiter;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.startProcedure.StartProcedure; 

/**
 * Enthält alle Funktionen für den Zugriff auf die Stunden Datenbank
 * @author Poehler
 *
 */
public class DBStundenTools {
	@Inject
	static
	IEventBroker eventBroker;

	private static final String STATUSBAR ="statusbar";

//	public static String DATENBANK = "C:\\Hydra\\Stunden.DB4O";
	private static String thisClass= "DBStundenTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	private static String settingPfad = StartProcedure.getCompanyDataDir();
	public static String DATENBANK = settingPfad + "\\Stunden.DB4O";
	private static boolean flag = false;

 
	/**
	 * Liest die Stunden Exceltabelle und füllt die Datenbank Stunden
	 */
	public static String DatenbankFüllen(String strFile) throws IOException {
		String status = "OK";
		Workbook workbook; //<-Interface, accepts both HSSF and XSSF.
		File file = new File(strFile);
		if (strFile.endsWith(".xls")) {
		  workbook = new HSSFWorkbook(new FileInputStream(file));
		} else if (strFile.endsWith(".xlsx")) {
			XSSFWorkbook newWorkBook = new XSSFWorkbook();
			Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };
		  workbook = new XSSFWorkbook(new FileInputStream(file));
		} else {
		  throw new IllegalArgumentException("Received file does not have a standard excel extension.");
		}

        
		//Get the workbook instance for XLS file 
		Sheet sheet = workbook.getSheet("Tabelle1");
		if (sheet == null){
			workbook.close();
			status = "In Exceltabelle fehlt -Tabelle1- oder -export-";
			LogfileView.log(thisClass, status, SWT.ICON_ERROR);
			return status;
		}

		MyProgressBarJob myProgressBarJob = new MyProgressBarJob("Daten einlesen", 0, sheet.getLastRowNum());
		myEventBroker(myProgressBarJob);

		//prüfen, ob richtige Tabelle 
		//prüft auf drei verschiedenen Überschriften
		Row rTestFile = sheet.getRow(0);
		flag = false;
		Integer counter = 0;	
		for (int j = 0; j < rTestFile.getLastCellNum(); j++) {

			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Buchungsdatum")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Postenart")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Beschreibung")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Kostenstelle Code")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Kostenträger Code")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Ressourcennr.")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Menge")) counter = counter +1;
		}
		if (counter == 7){
			flag = true;
		}else{
			LogfileView.log(thisClass,"Falsche Datei. Enthält keine Stunden oder es fehlen Spalten",SWT.ICON_ERROR);
			 workbook.close();
			 status = "Falsche Datei. Enthält keine Stunden oder es fehlen Spalten";
			 return status;
		}
		
		ArrayList<MyHours> arraylist = new ArrayList<MyHours>();
		if (flag) {
			Row header = sheet.getRow(0);
			for (int row = 1; row < sheet.getLastRowNum(); row++) {
				myProgressBarJob.setWorked(row);
				myEventBroker(myProgressBarJob);
				
				MyHours myNewHours = new MyHours();
				Row zeile = sheet.getRow(row);
				for (int col = 0; col < header.getLastCellNum(); col++) {
		
					Cell c = zeile.getCell(col);
					Cell spaltenueberschrift = header.getCell(col);
					
					switch(spaltenueberschrift.toString()){ 
					case "Buchungsdatum": 
						myNewHours.setBuchungsdatum(c.getDateCellValue());
						break;
						
					case "Postenart": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setAufgabe("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setAufgabe(c.getStringCellValue());
						}
						break;
						
					case "Ressourcennr.": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setNummer("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setNummer(c.getStringCellValue());
						}
						break;
						
					case "Menge": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setMenge(0);
						}else{
//							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setMenge(c.getNumericCellValue());
						}
						break;
						
					case "Kostenstelle Code": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setKostenstelle("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setKostenstelle(c.getStringCellValue());
						}
						break;
						
					case "Kostenträger Code": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setKostentraeger("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setKostentraeger(c.getStringCellValue());
						}
						break;
						
					case "Beschreibung": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setBeschreibung("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setBeschreibung(c.getStringCellValue());
						}
						break;
						
					case "Lfd. Nr.": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setLfdnr(0);
						}else{
							Double d = c.getNumericCellValue();
							Long l = d.longValue();
							myNewHours.setLfdnr(l);
						}
						break;
						
					case "Arbeitstypencode": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myNewHours.setArbeitstypencode("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myNewHours.setArbeitstypencode(c.getStringCellValue());
						}
						break;
					default:
						
					}
				}
				//Felder ergänzen
				myNewHours.setSatz(row);
				myNewHours.setStatus("Ist");
				myNewHours.setGeändertAm(new Date());
				myNewHours.setGeändertDurch("Datenimport");
				myNewHours.setName("");
				myNewHours.setProjekt("");
				
				arraylist.add(myNewHours);
				
				LogfileView.log(thisClass,"Datensatz: " 
						+ String.valueOf(myNewHours.getSatz()) + "   "
						+ myNewHours.getBeschreibung() + "   "
						+ myNewHours.getMenge() + "   "
						+ myNewHours.getBuchungsdatum(),SWT.ICON_INFORMATION);
				
			} 
		}
		myProgressBarJob.setWorked(-1);
		myEventBroker(myProgressBarJob);
		workbook.close();
		
    	writeStundenDB(namenErgaenzen(postenartBereinigen(arraylist)));
    	SummeKostenträger.summeKostenträgerErmittlen();
    	SummeAufträge.summeAufträgeErmittlen();
    	return status;
	}
	
	
	
	/**
	 * Liest die Stunden Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return results Die Liste der Einträge
	 */
	public static List<MyHours> readStundenDB(){
		serverStarten();

		ArrayList<MyHours> list = new  ArrayList<MyHours>(5);
		Query query=client.query();							//Abfrage definieren
		query.constrain(MyHours.class);	
		ObjectSet<MyHours> results = query.execute();
		list.addAll(0,results);
		LogfileView.log(thisClass,"Stundendatenbank Anzahl Datensätze: " + list.size() ,SWT.ICON_ERROR);
		serverStoppen();
		return list;
	}
	
	/**
	 * Liest aus der Stunden Datenbank die passenden Kostenträger
	 * @param kostenträger der gesuchte Kostenträger
	 * @return results Die Liste mit dem gesuchten Kostenträger
	 */
	public static List<MyHours> readKostenträgerAusDB(String kostenträger){
		serverStarten();

		ArrayList<MyHours> list = new  ArrayList<MyHours>(5);
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyHours.class);	
			queryNew.descend("kostentraeger").constrain(kostenträger).equal();
			ObjectSet<MyHours> resultsNew = queryNew.execute();			
			list.addAll(0,resultsNew);
		} catch (DatabaseClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStoppen();
		return list;
	}
	
	/**
	 * Schreibt eine Liste von Stundensätzen in die Stunden Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Doppelte Datensätze werden geprüft und überschrieben
	 * @param arraylist Ein Liste mit Stunden Datensätzen
	 */
	public static void writeStundenDB(ArrayList<MyHours> arraylist){
		MyProgressBarJob myProgressBarJob = new MyProgressBarJob("Stunden speichern",0,arraylist.size());
		myEventBroker(myProgressBarJob);
		
		serverStarten();
			
        long good = 0;
        long bad = 0;
        
        for (int i = 0; i < arraylist.size(); i++) {
			myProgressBarJob.setWorked(i);
			myEventBroker(myProgressBarJob);
			
			//prüfen, ob identischer Datensatz existiert
        	MyHours myHoursSample = new MyHours();
        	myHoursSample.setNummer(arraylist.get(i).getNummer());
        	myHoursSample.setKostenstelle(arraylist.get(i).getKostenstelle());
        	myHoursSample.setKostentraeger(arraylist.get(i).getKostentraeger());
        	myHoursSample.setBuchungsdatum(arraylist.get(i).getBuchungsdatum());
        	myHoursSample.setStatus(arraylist.get(i).getStatus());
        	myHoursSample.setMenge(arraylist.get(i).getMenge());
        	
			ObjectSet<MyHours> results = client.queryByExample(myHoursSample);
			
			if (results.size() > 0) { //doppelten Datensatz gefunden
				//nichts tun
				LogfileView.log(thisClass,"Datensatz bereits vorhanden: " 
						+ String.valueOf(arraylist.get(i).getSatz()) + "   "
						+ arraylist.get(i).getBeschreibung() + "   "
						+ arraylist.get(i).getMenge() + "   "
						+ arraylist.get(i).getBuchungsdatum(),SWT.ICON_INFORMATION);
				bad++;
			} else {
				LogfileView.log(thisClass,"Datensatz speichern: " 
						+ String.valueOf(arraylist.get(i).getSatz()) + "   "
						+ arraylist.get(i).getBeschreibung() + "   "
						+ arraylist.get(i).getMenge() + "   "
						+ arraylist.get(i).getBuchungsdatum(),SWT.ICON_INFORMATION);
				client.store(arraylist.get(i));
				client.commit(); //speichern
				good++;
			}
		}
        
        // wenn neu Datensätze vorhanden, dann die Kostenträger und -stellen in ProjektDB ergänzen
        if (good >0) {
			DBProjekteTools.addProjekteDB(arraylist);
		}
		myProgressBarJob.setWorked(-1);
		myEventBroker(myProgressBarJob);
		LogfileView.log(thisClass,"Datensätze neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
		LogfileView.log(thisClass,"Datensätze abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
	}
	
	/**
	 * Namen auffüllen
	 * Datensätze kommen teilweise ohne den Klarnamen
	 * Diese Funktion ergänzt die Namen
	 */
	public static ArrayList<MyHours> namenErgaenzen(ArrayList<MyHours> arraylist){
		MyProgressBarJob myProgressBarJob = new MyProgressBarJob("Mitarbeitername ergänzen",0,arraylist.size());
		myEventBroker(myProgressBarJob);
		
		List<MyMitarbeiter> mitarbeiter = DBMitarbeiterTools.readMitarbeiterDB();
		for (int i = 0; i < arraylist.size(); i++) {
			myProgressBarJob.setWorked(i);
			myEventBroker(myProgressBarJob);
			String name = arraylist.get(i).getName();
			if (name == "") {							//nur Namen ergänzen, wenn Feld leer ist
				String stundenNummer = arraylist.get(i).getNummer();
				Boolean found = false;
				for (int j = 0; j < mitarbeiter.size(); j++) {		//Mitarbeiterliste durchsuchen
					String mitarbeiterNummer = mitarbeiter.get(j).getNummer();
					
					if (stundenNummer.equals(mitarbeiterNummer)) {
						arraylist.get(i).setName(mitarbeiter.get(j).getName());	//Namen zuweisen
						LogfileView.log(thisClass, i + " Mitarbeiternamen ergänzt "
								+ mitarbeiter.get(j).getName(),
								SWT.ICON_INFORMATION);
						found = true;
						break;
					} 				
				}
				if (!found) {
					LogfileView.log(thisClass,
							 i + " Mitarbeiternamen nicht gefunden für Nummer:"
									+ arraylist.get(i).getNummer(),
							SWT.ICON_ERROR);
				}
			}
			//Standardmäßig wird von Dynamics der Mitarbeitername in die Beschreibung eingetragen
			//Dieser wird hier gelöscht, wenn nichts anderes eingetragen wurde
			if (arraylist.get(i).getBeschreibung().equals(arraylist.get(i).getName())){
				arraylist.get(i).setBeschreibung("");
			}
		}

		LogfileView.log(thisClass,"Mitarbeiternamen ergänzt " + arraylist.size() ,SWT.ICON_ERROR);
		myProgressBarJob.setWorked(-1);
		myEventBroker(myProgressBarJob);
		return arraylist;
	}
	
	/**
	 * Entfernt aus der Spalte Postenart alle Datensätze, die nicht mit "Verbrauch" gekennzeichnet sind
	 */
	public static ArrayList<MyHours> postenartBereinigen(ArrayList<MyHours> arraylist){
		ArrayList<MyHours> newList = new ArrayList<MyHours>();
		for (MyHours satz: arraylist){
			if (satz.getAufgabe().equals("Verbrauch")){
				newList.add(satz);
			}else{
				LogfileView.log(thisClass,"Stundendatenbank Datensatz entfernt: " + satz.getName() ,SWT.ICON_INFORMATION);
			}
		}
		LogfileView.log(thisClass,"Postenart bereinigt " + newList.size() ,SWT.ICON_ERROR);
		return newList;
	}
	/**
	 * Speichert den geänderten Datensatz in der Datenbank ab
	 * @param myHours
	 */
	public static void updateMyHours(MyHours myHours) {
		serverStarten();
		ArrayList<MyHours> list = new  ArrayList<MyHours>(5);
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyHours.class);	
			queryNew.descend("lfdnr").constrain(myHours.getLfdnr()).equal();
			ObjectSet<MyHours> resultsNew = queryNew.execute();			
			list.addAll(0,resultsNew);
			if (list.size() == 1){
				client.delete(resultsNew.get(0));
				client.store(myHours);
				client.commit(); //speichern
				LogfileView.log(thisClass,"Stundendatenbank Datensatz ersetzt: " + myHours.getName() ,SWT.ICON_INFORMATION);
			}else{
				LogfileView.log(thisClass,"Stundendatenbank keinen oder mehr als ein Datensatz gefunden: " + list.size() ,SWT.ICON_ERROR);
			}

		} catch (DatabaseClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStoppen();
	}
	
	/**
	 * Startet den Server
	 */
	private static void serverStarten(){
		if (serverOFF) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyHours.class);
			configuration.common().objectClass(MyHours.class).objectField("name").indexed(true);
		    server = Db4oClientServer.openServer(configuration,DATENBANK, 0);
			client = server.openClient();
			LogfileView.log(thisClass, " serverStarten ",SWT.ICON_INFORMATION);
			serverOFF = false;
		}
	}
	
	/**
	 * Stoppt den Server
	 */
	public static void serverStoppen(){
		client.close();
		server.close();
		serverOFF = true;
	}
	
	private static void myEventBroker(MyProgressBarJob myProgressBarJob){
		//sendet die geänderten Informationen in den Datenbus
		eventBroker.send("MyProgressbarEvent", myProgressBarJob);
	}
}
