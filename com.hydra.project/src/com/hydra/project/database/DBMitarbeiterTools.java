/**
 * 
 */
package com.hydra.project.database;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.SWT;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.query.Query;
import com.hydra.project.model.MyMitarbeiter;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.startProcedure.StartProcedure;
/**
 * Enthält alle Funktionen für den Zugriff auf die Stunden Datenbank
 * @author Poehler
 *
 */
public class DBMitarbeiterTools {

//	public static String DATENBANK = "C:\\Hydra\\Mitarbeiter.DB4O";
	private static String thisClass= "DBMitarbeiterTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	private static String settingPfad = StartProcedure.getCompanyDataDir();
	public static String DATENBANK = settingPfad + "\\Mitarbeiter.DB4O";
	private static boolean flag = false;
	
	/**
	 * Liest die Mitarbeiter Exceltabelle und füllt die Datenbank Mitarbeiter
	 */
	public static void DatenbankFüllen(String strFile) throws IOException {
		
		Workbook workbook; //<-Interface, accepts both HSSF and XSSF.
		File file = new File(strFile);
		if (strFile.endsWith(".xls")) {
		  workbook = new HSSFWorkbook(new FileInputStream(file));
		} else if (strFile.endsWith(".xlsx")) {
		  workbook = new XSSFWorkbook(new FileInputStream(file));
		} else {
		  throw new IllegalArgumentException("Received file does not have a standard excel extension.");
		}
		

		//Get the workbook instance for XLS file 
		Sheet sheet = workbook.getSheet("Tabelle1");
		
		//prüfen, ob richtige Tabelle 
		//prüft auf drei verschiedenen Überschriften
		Row rTestFile = sheet.getRow(0);
		flag = false;
		Integer counter = 0;	
		for (int j = 0; j < rTestFile.getLastCellNum(); j++) {
		
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Nr.")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Name")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Art")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Basiseinheitencode")) counter = counter +1;
			if (rTestFile.getCell(j).getRichStringCellValue().getString().equals("Produktbuchungsgruppe")) counter = counter +1;
		}
		if (counter == 5){
			flag = true;
		}else{
			LogfileView.log(thisClass,"Falsche Datei. Enthält keine Mitarbeiter oder es fehlen Spalten",SWT.ICON_ERROR);
		}
		
		ArrayList<MyMitarbeiter> arraylist = new ArrayList<MyMitarbeiter>();
		
		if (flag) {
			Row header = sheet.getRow(0);
			for (int row = 1; row < sheet.getLastRowNum(); row++) {
				MyMitarbeiter myMitarbeiter = new MyMitarbeiter();
				Row zeile = sheet.getRow(row);
				for (int col = 0; col < header.getLastCellNum(); col++) {
		
					Cell c = zeile.getCell(col);
					Cell spaltenueberschrift = header.getCell(col);
					
					switch(spaltenueberschrift.toString()){ 
					case "Nr.": 
						c.setCellType(HSSFCell.CELL_TYPE_STRING);
						myMitarbeiter.setNummer(c.getStringCellValue());
						break;
						
					case "Name": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myMitarbeiter.setName("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myMitarbeiter.setName(c.getStringCellValue());
						}
						break;
						
					case "Art": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myMitarbeiter.setArt("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myMitarbeiter.setArt(c.getStringCellValue());
						}
						break;
						
					case "Basiseinheitencode": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myMitarbeiter.setBasiseinheit("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myMitarbeiter.setBasiseinheit(c.getStringCellValue());
						}
						break;
						
					case "Produktbuchungsgruppe": 
						if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// This cell is empty
							myMitarbeiter.setProduktbuchungsgruppe("");
						}else{
							c.setCellType(HSSFCell.CELL_TYPE_STRING);
							myMitarbeiter.setProduktbuchungsgruppe(c.getStringCellValue());
						}
						break;
						
					default:
						
					}
				}
				//Felder ergänzen
				myMitarbeiter.setSatz(row);
				myMitarbeiter.setAbteilung("");
				myMitarbeiter.setFirma("");
				myMitarbeiter.setGeschlecht("");
				myMitarbeiter.setPosition("");
				myMitarbeiter.setTitel("");				
				myMitarbeiter.setGeändertAm(new Date());
				myMitarbeiter.setGeändertDurch("Datenimport");
	
				arraylist.add(myMitarbeiter);
				
				LogfileView.log(thisClass,"Datensatz: " 
						+ String.valueOf(myMitarbeiter.getSatz()) + "   "
						+ myMitarbeiter.getNummer() + "   "
						+ myMitarbeiter.getName(),SWT.ICON_INFORMATION);
				
			}
		}
		
		workbook.close();
    	writeMitarbeiterDB(arraylist);
    	serverStoppen();
	}
	
	
	
	/**
	 * Liest die Mitarbeiter Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return results Die Liste der Einträge
	 */
	public static List<MyMitarbeiter> readMitarbeiterDB(){
		serverStarten();
		ArrayList<MyMitarbeiter> list = new  ArrayList<MyMitarbeiter>(5);
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyMitarbeiter.class);	
//			ObjectSet<MyMitarbeiter> resultsNew = queryNew.execute();
			ObjectSet<MyMitarbeiter> resultsNew = queryNew.execute();			
			list.addAll(0,resultsNew);
			LogfileView.log(thisClass,"Mitarbeiterdatenbank Anzahl Datensätze: " + list.size() ,SWT.ICON_ERROR);
		} catch (DatabaseClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStoppen();
		return list;
	}
	
	/**
	 * Schreibt eine Liste von Mitarbeitern in die Mitarbeiter Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Doppelte Datensätze werden geprüft und nicht überschrieben
	 * @param arraylist Ein Liste mit Mitarbeiter Datensätzen
	 */
	public static MyMitarbeiter writeMitarbeiterDB(ArrayList<MyMitarbeiter> arraylist){
		serverStarten();

		try {
	        long good = 0;
	        long bad = 0;
	        for (int i = 0; i < arraylist.size(); i++) {
				//prüfen, ob identischer Datensatz existiert
	        	Query query=client.query();							//Abfrage definieren
	    		query.constrain(MyMitarbeiter.class);							//suche in allen Datensätze   	    
	    		String nummer = arraylist.get(i).getNummer();
	    		query.descend("nummer").constrain(nummer);	
				ObjectSet<MyMitarbeiter> results = query.execute();
				int zahl = results.size();			
				if (results.size() > 0) { //doppelten Datensatz gefunden
					
					//nichts tun
					LogfileView.log(thisClass,"Datensatz bereits vorhanden: " 
							+ String.valueOf(arraylist.get(i).getSatz()) + "   "
							+ arraylist.get(i).getNummer() + "   "
							+ arraylist.get(i).getName(),SWT.ICON_INFORMATION);
					bad++;
				} else {
					LogfileView.log(thisClass,"Datensatz speichern: " 
							+ String.valueOf(arraylist.get(i).getSatz()) + "   "
							+ arraylist.get(i).getNummer() + "   "
							+ arraylist.get(i).getName(),SWT.ICON_INFORMATION);
					client.store(arraylist.get(i));
					client.commit(); //speichern
					good++;
				}
			}
	  
			LogfileView.log(thisClass,"Datensätze neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
			LogfileView.log(thisClass,"Datensätze abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Speichert den geänderten Datensatz in der Datenbank ab
	 * @param myMitarbeiter
	 */
	public static void updateMyMitarbeiter(MyMitarbeiter myMitarbeiter) {
		serverStarten();
		ArrayList<MyMitarbeiter> list = new  ArrayList<MyMitarbeiter>(5);
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyMitarbeiter.class);	
			queryNew.descend("nummer").constrain(myMitarbeiter.getNummer()).equal();
			ObjectSet<MyMitarbeiter> resultsNew = queryNew.execute();			
			list.addAll(0,resultsNew);
			if (list.size() == 1){
				client.delete(resultsNew.get(0));
				client.store(myMitarbeiter);
				client.commit(); //speichern
				LogfileView.log(thisClass,"Mitarbeiterdatenbank Datensatz ersetzt: " + myMitarbeiter.getName() ,SWT.ICON_INFORMATION);
			}else{
				LogfileView.log(thisClass,"Mitarbeiterdatenbank keinen oder mehr als ein Datensatz gefunden: " + list.size() ,SWT.ICON_ERROR);
			}

		} catch (DatabaseClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStoppen();
	}
	
	/**
	 * Liefert eine Liste von Mitarbeiternamen
	 * @return 
	 * @return 
	 * @return list Eine Liste aller Mitarbeiter
	 */
	public static String[] getMitarbeiter(){
		List<MyMitarbeiter> myMitarbeiter = readMitarbeiterDB();

		List<String> liste = new ArrayList<String>();
		for (int i = 0; i < myMitarbeiter.size(); i++) {
			liste.add(myMitarbeiter.get(i).getName());
		}
		Collections.sort(liste);
		
		String[] list = new String[myMitarbeiter.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = liste.get(i);
		}
		return list;
	}
	
	
	
	/**
	 * Startet den Server
	 */
	public static void serverStarten(){
		if (serverOFF) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyMitarbeiter.class);
			
		    try {
				server = Db4oClientServer.openServer(configuration,DATENBANK, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "Server läuft bereits ",SWT.ICON_ERROR);
				e.printStackTrace();
			}
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
}
