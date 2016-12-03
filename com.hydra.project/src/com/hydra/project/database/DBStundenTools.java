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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.swt.SWT;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraintViolationException;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;
import com.hydra.project.model.MyHours;
import com.hydra.project.model.MyMitarbeiter;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;

/**
 * Enthält alle Funktionen für den Zugriff auf die Stunden Datenbank
 * @author Poehler
 *
 */
public class DBStundenTools {

	public static String DATENBANK = "C:\\Hydra\\Stunden.DB4O";
	private static String thisClass= "DBStundenTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
 
	/**
	 * Liest die Stunden Exceltabelle und füllt die Datenbank Stunden
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
		
//		FileInputStream file = new FileInputStream(new File(strFile));
        
		//Get the workbook instance for XLS file 
		Sheet sheet = workbook.getSheet("Tabelle1");
		
		//prüfen, ob richtige Tabelle 
		//oberste linke Zelle prüfen
		Row rTest = sheet.getRow(0);
		
		String string = rTest.getCell(0).getRichStringCellValue().getString();
		LogfileView.log(thisClass,"Inhalt der Zelle: "+string,SWT.ICON_ERROR);
		
		ArrayList<MyHours> arraylist = new ArrayList<MyHours>();
		
	
		if (rTest.getCell(0).getRichStringCellValue().getString().equals("Buchungsdatum")) {
			
		    // Loop over column and lines
			for (int j = 1; j < sheet.getLastRowNum(); j++) {
				MyHours myNewHours = new MyHours();
				Row r = sheet.getRow(j);

	    		//0.Spalte Buchungsdatum
				Cell c = r.getCell(0);
				myNewHours.setBuchungsdatum(c.getDateCellValue());
				
				//1.Spalte Postenart	
				c = r.getCell(1);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setAufgabe("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setAufgabe(c.getStringCellValue());
				}
				
				//2.Spalte Ressourcennr
				c = r.getCell(2);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setNummer("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setNummer(c.getStringCellValue());
				}
				
				//3.Spalte Menge
				c = r.getCell(3);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setMenge(0);
				}else{
//					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setMenge(c.getNumericCellValue());
				}
				
				
				//4.Spalte Kostenstelle Code
				c = r.getCell(4);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setKostenstelle("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setKostenstelle(c.getStringCellValue());
				}
					
				//5.Spalte Kostenträger Code
				c = r.getCell(5);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setKostentraeger("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setKostentraeger(c.getStringCellValue());
				}

				//6.Spalte Beschreibung
				c = r.getCell(6);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myNewHours.setBeschreibung("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myNewHours.setBeschreibung(c.getStringCellValue());
				}
				
				//Felder ergänzen
				myNewHours.setSatz(j);
				myNewHours.setStatus("Ist");
				myNewHours.setGeändertAm(new Date());
				myNewHours.setGeändertDurch("Datenimport");
				myNewHours.setName("");
				myNewHours.setProjekt("");
				//Kostenträger und Kostenstelle dürfen nicht gleichzeitig gefüllt sein
				//Kostenträger hat Vorrang
				if (myNewHours.getKostentraeger().length() > 0) {
					myNewHours.setKostenstelle("");
				}
				arraylist.add(myNewHours);
				
				LogfileView.log(thisClass,"Datensatz: " 
						+ String.valueOf(myNewHours.getSatz()) + "   "
						+ myNewHours.getBeschreibung() + "   "
						+ myNewHours.getMenge() + "   "
						+ myNewHours.getBuchungsdatum(),SWT.ICON_INFORMATION);
				
				
//			       for (int i = 0; i < arraylist.size(); i++) {
//						LogfileView.log(thisClass,"Datensatz auslesen: " 
//								+ String.valueOf(arraylist.get(i).getSatz()) + "   "
//								+ arraylist.get(i).getBeschreibung() + "   "
//								+ arraylist.get(i).getMenge() + "   "
//								+ arraylist.get(i).getBuchungsdatum(),SWT.ICON_INFORMATION);
//			        }
			}
		}else {
			LogfileView.log(thisClass,"Falsche Datei. Enthält keine Stunden",SWT.ICON_ERROR);
		}
		
		workbook.close();
    	writeStundenDB(namenErgaenzen(arraylist));
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
	 * Schreibt eine Liste von Stundensätzen in die Stunden Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Doppelte Datensätze werden geprüft und überschrieben
	 * @param arraylist Ein Liste mit Stunden Datensätzen
	 */
	public static void writeStundenDB(ArrayList<MyHours> arraylist){
		serverStarten();
			
        long good = 0;
        long bad = 0;
        
        for (int i = 0; i < arraylist.size(); i++) {
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
		LogfileView.log(thisClass,"Datensätze neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
		LogfileView.log(thisClass,"Datensätze abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
	}
	
	/**
	 * Namen auffüllen
	 * Datensätze kommen teilweise ohne den Klarnamen
	 * Diese Funktion ergänzt die Namen
	 */
	public static ArrayList<MyHours> namenErgaenzen(ArrayList<MyHours> arraylist){
		List<MyMitarbeiter> mitarbeiter = DBMitarbeiterTools.readMitarbeiterDB();
		for (int i = 0; i < arraylist.size(); i++) {
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
		}

		LogfileView.log(thisClass,"Mitarbeiternamen ergänzt " + arraylist.size() ,SWT.ICON_ERROR);
		return arraylist;
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
}
