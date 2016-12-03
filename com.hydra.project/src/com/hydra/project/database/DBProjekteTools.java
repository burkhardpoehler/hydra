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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import com.db4o.query.Constraint;
import com.db4o.query.Query;
import com.hydra.project.model.MyHours;
import com.hydra.project.model.MyProjekte;
import com.hydra.project.parts.LogfileView;
/**
 * Enth�lt alle Funktionen f�r den Zugriff auf die Projekte Datenbank
 * @author Poehler
 *
 */
public class DBProjekteTools {

	public static String DATENBANK = "C:\\Hydra\\Projekte.DB4O";
	private static String thisClass= "DBProjekteTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits l�uft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	
	/**
	 * Liest die Mitarbeiter Exceltabelle und f�llt die Datenbank Mitarbeiter
	 */
	public static void DatenbankF�llen(String strFile) throws IOException {
		
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
		
		//pr�fen, ob richtige Tabelle 
		//oberste linke Zelle pr�fen
		Row rTest = sheet.getRow(0);
		
		String string = rTest.getCell(0).getRichStringCellValue().getString();
//		LogfileView.log(thisClass,"Inhalt der Zelle: "+string,SWT.ICON_ERROR);
		
		ArrayList<MyProjekte> arraylist = new ArrayList<MyProjekte>();
		
	
		if (rTest.getCell(0).getRichStringCellValue().getString().equals("Auftragsnummer")) {
			
		    // Loop over column and lines
			for (int j = 1; j < sheet.getLastRowNum(); j++) {
				MyProjekte myProjekte = new MyProjekte();
				Row r = sheet.getRow(j);

	    		//0.Spalte Auftragsnummer
				Cell c = r.getCell(0);
				c.setCellType(HSSFCell.CELL_TYPE_STRING);
				myProjekte.setAuftragsnummer(c.getStringCellValue());
				
				//1.Spalte Kostenstelle	
				c = r.getCell(1);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myProjekte.setKostenstelle("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myProjekte.setKostenstelle(c.getStringCellValue());
				}
				
				//2.Spalte Kostentr�ger	
				c = r.getCell(2);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myProjekte.setKostentr�ger("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myProjekte.setKostentr�ger(c.getStringCellValue());
				}
				
				//3.Spalte Bezeichnung 1	
				c = r.getCell(3);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myProjekte.setProjektbezeichnung1("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myProjekte.setProjektbezeichnung1(c.getStringCellValue());
				}
				
				//4.Spalte Bezeichnung 2	
				c = r.getCell(4);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myProjekte.setProjektbezeichnung2("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myProjekte.setProjektbezeichnung2(c.getStringCellValue());
				}
				
				//5.Spalte Beschreibung	
				c = r.getCell(5);
				if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK) {
					// This cell is empty
					myProjekte.setBeschreibung("");
				}else{
					c.setCellType(HSSFCell.CELL_TYPE_STRING);
					myProjekte.setBeschreibung(c.getStringCellValue());
				}

				//Felder erg�nzen
				myProjekte.setSatz(j);
				myProjekte.setFirma("");
				myProjekte.setGe�ndertAm(new Date());
				myProjekte.setBuchungsdatum(new Date());
				myProjekte.setGe�ndertDurch("Datenimport");
	
				arraylist.add(myProjekte);
				
				LogfileView.log(thisClass,"Datensatz: " 
						+ String.valueOf(myProjekte.getSatz()) + "   "
						+ myProjekte.getAuftragsnummer() + "   "
						+ myProjekte.getProjektbezeichnung1() + "   "
						+ myProjekte.getKostentr�ger()
						,SWT.ICON_INFORMATION);
				
			}
		}else {
			LogfileView.log(thisClass,"Falsche Datei. Enth�lt keine Mitarbeiter",SWT.ICON_ERROR);
		}
		
		workbook.close();
    	writeProjekteDB(arraylist);
    	serverStoppen();
	}
	
	
	
	/**
	 * Liest die Projekte Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return results Die Liste der Eintr�ge
	 */
	public static List<MyProjekte> readProjekteDB(){
		serverStarten();
		ArrayList<MyProjekte> list = new  ArrayList<MyProjekte>(5);
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyProjekte.class);	
//			ObjectSet<MyMitarbeiter> resultsNew = queryNew.execute();
			ObjectSet<MyProjekte> resultsNew = queryNew.execute();			
			list.addAll(0,resultsNew);
			LogfileView.log(thisClass,"Projektedatenbank Anzahl Datens�tze: " + list.size() ,SWT.ICON_ERROR);
		} catch (DatabaseClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverStoppen();
		return list;
	}
	
	
	/**
	 * Erh�lt eine Liste von Stundens�tzen aus der Stunden Datenbank
	 * �bernimmt die Kostentr�ger und Kostenstellen
	 * Doppelte Datens�tze werden nicht �bernommen
	 * @param arraylist Eine Liste mit Stunden Datens�tzen
	 */
	public static void addProjekteDB(ArrayList<MyHours> arraylist){
		serverStarten();
		//1.Schritt zwei neue Listen erzeugen und mit den Daten der arrayliste f�llen
		ArrayList<String> listeKostentr�ger = new ArrayList<String>();
		ArrayList<String> listeKostenstellen = new ArrayList<String>();
		for (int n = 0; n < arraylist.size(); n++) { 		//durchlaufe alle Stundens�tze
			if (!arraylist.get(n).getKostentraeger().equals("")){		//nicht leer
				listeKostentr�ger.add(arraylist.get(n).getKostentraeger());
			}
			if (!arraylist.get(n).getKostenstelle().equals("")){		//nicht leer
				listeKostenstellen.add(arraylist.get(n).getKostenstelle());
			}
		}
		
		//2. Schritt beseitigen der Doppelg�nger
		Set<String> set1 = new LinkedHashSet<String>(listeKostentr�ger);
		listeKostentr�ger = new ArrayList<String>(set1);
		
		Set<String> set2 = new LinkedHashSet<String>(listeKostenstellen);
		listeKostenstellen = new ArrayList<String>(set2);
		
		//3. Schritt Kostentr�ger in DB suchen, Wenn nicht zu finden -> anlegen
		List<MyProjekte> projekte =  readProjekteDB();
		for (int n = 0; n < listeKostentr�ger.size(); n++) { 		//durchlaufe alle Stundens�tze
			try {
		        long good = 0;
		        long bad = 0;
		        for (int i = 0; i < projekte.size(); i++) {
					//pr�fen, ob identischer Datensatz existiert
		        	Query query=client.query();							//Abfrage definieren
		    		query.constrain(MyProjekte.class);							//suche in allen Datens�tze   	    
		    		String kostentr�ger = projekte.get(i).getKostentr�ger();
		    		Constraint constrain1 = query.descend("kostentr�ger").constrain(kostentr�ger);
		    		query.constrain(constrain1);

					ObjectSet<MyProjekte> results = query.execute();
					if (results.size() > 0) { //doppelten Datensatz gefunden
						//nichts tun
						LogfileView.log(thisClass,"Kostentr�ger bereits vorhanden: " 
								+ String.valueOf(projekte.get(i).getSatz()) + "   "
								+ projekte.get(i).getKostentr�ger()
								,SWT.ICON_INFORMATION);
						bad++;
					} else {
						LogfileView.log(thisClass,"Kostentr�ger neu und speichern: " 
								+ String.valueOf(projekte.get(i).getSatz()) + "   "
								+ projekte.get(i).getKostentr�ger()
								,SWT.ICON_INFORMATION);
						MyProjekte myProjekte = new MyProjekte();
						myProjekte.setKostentr�ger(kostentr�ger);
						client.store(myProjekte);
						client.commit(); //speichern
						good++;
					}
				}
		  
				LogfileView.log(thisClass,"Kostentr�ger neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
				LogfileView.log(thisClass,"Kostentr�ger abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
			} catch (DatabaseClosedException e) {
				e.printStackTrace();
			}
		}

		//4. Schritt Kostenstellen in DB suchen, Wenn nicht zu finden -> anlegen
		for (int n = 0; n < listeKostenstellen.size(); n++) { 		//durchlaufe alle Stundens�tze
			try {
		        long good = 0;
		        long bad = 0;
		        for (int i = 0; i < projekte.size(); i++) {
					//pr�fen, ob identischer Datensatz existiert
		        	Query query=client.query();							//Abfrage definieren
		    		query.constrain(MyProjekte.class);							//suche in allen Datens�tze   	    
		    		String kostenstelle = projekte.get(i).getKostenstelle();
		    		Constraint constrain1 = query.descend("kostenstellen").constrain(kostenstelle);
		    		query.constrain(constrain1);

					ObjectSet<MyProjekte> results = query.execute();
					if (results.size() > 0) { //doppelten Datensatz gefunden
						//nichts tun
						LogfileView.log(thisClass,"Kostenstelle bereits vorhanden: " 
								+ String.valueOf(projekte.get(i).getSatz()) + "   "
								+ projekte.get(i).getKostenstelle()
								,SWT.ICON_INFORMATION);
						bad++;
					} else {
						LogfileView.log(thisClass,"Kostenstelle neu und speichern: " 
								+ String.valueOf(projekte.get(i).getSatz()) + "   "
								+ projekte.get(i).getKostenstelle()
								,SWT.ICON_INFORMATION);
						MyProjekte myProjekte = new MyProjekte();
						myProjekte.setKostenstelle(kostenstelle);
						client.store(myProjekte);
						client.commit(); //speichern
						good++;
					}
				}
		  
				LogfileView.log(thisClass,"Kostenstelle neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
				LogfileView.log(thisClass,"Kostenstelle abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
			} catch (DatabaseClosedException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Schreibt eine Liste von Projekten in die Projekte Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Doppelte Datens�tze werden gepr�ft und nicht �berschrieben
	 * @param arraylist Eine Liste mit Projekte Datens�tzen
	 */
	public static void writeProjekteDB(ArrayList<MyProjekte> arraylist){
		serverStarten();

		try {
	        long good = 0;
	        long bad = 0;
	        for (int i = 0; i < arraylist.size(); i++) {
				//pr�fen, ob identischer Datensatz existiert
	        	Query query=client.query();							//Abfrage definieren
	    		query.constrain(MyProjekte.class);							//suche in allen Datens�tze   	    
	    		String auftragsnummer = arraylist.get(i).getAuftragsnummer();
	    		String kostenstelle = arraylist.get(i).getKostenstelle();
	    		String kostentr�ger = arraylist.get(i).getKostentr�ger();
	    		
	    		//Datens�tze sind gleich, wenn die drei Felder identisch sind
	    		
	    		Constraint constrain1 = query.descend("auftragsnummer").constrain(auftragsnummer);
	    		Constraint constrain2 = query.descend("kostenstelle").constrain(kostenstelle);
	    		Constraint constrain3=  query.descend("kostentr�ger").constrain(kostentr�ger);
	    		query.constrain(constrain1).and(constrain2).and(constrain3);

				ObjectSet<MyProjekte> results = query.execute();
				if (results.size() > 0) { //doppelten Datensatz gefunden
					//nichts tun
					LogfileView.log(thisClass,"Datensatz bereits vorhanden: " 
							+ String.valueOf(arraylist.get(i).getSatz()) + "   "
							+ arraylist.get(i).getAuftragsnummer() + "   "
							+ arraylist.get(i).getProjektbezeichnung1() + "   "
							+ arraylist.get(i).getKostentr�ger()
							,SWT.ICON_INFORMATION);
					bad++;
				} else {
					LogfileView.log(thisClass,"Datensatz speichern: " 
							+ String.valueOf(arraylist.get(i).getSatz()) + "   "
							+ arraylist.get(i).getAuftragsnummer() + "   "
							+ arraylist.get(i).getProjektbezeichnung1() + "   "
							+ arraylist.get(i).getKostentr�ger()
							,SWT.ICON_INFORMATION);
					client.store(arraylist.get(i));
					client.commit(); //speichern
					good++;
				}
			}
	  
			LogfileView.log(thisClass,"Datens�tze neu geschrieben: " + String.valueOf(good),SWT.ICON_INFORMATION);
			LogfileView.log(thisClass,"Datens�tze abgelehnt: " + String.valueOf(bad),SWT.ICON_INFORMATION);
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Liefert eine Liste von Auftragsnummern
	 * @return list Eine Liste aller Auftragsnummern
	 */
	public static String[] getAuftragsnummern(){
		List<MyProjekte> myProjekte = readProjekteDB();

		List<String> liste = new ArrayList<String>();
		for (int i = 0; i < myProjekte.size(); i++) {
			liste.add(myProjekte.get(i).getAuftragsnummer());
		}
		Collections.sort(liste);
		
		String[] list = new String[myProjekte.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = liste.get(i);
		}
		return list;
	}
	
	/**
	 * Liefert eine Liste von Kostenstellen
	 * @return list Eine Liste aller Kostenstellen
	 */
	public static String[] getKostenstellen(){
		List<MyProjekte> myProjekte = readProjekteDB();
		
		List<String> liste = new ArrayList<String>();
		for (int i = 0; i < myProjekte.size(); i++) {
			liste.add(myProjekte.get(i).getKostenstelle());
		}
		Collections.sort(liste);
		
		String[] list = new String[myProjekte.size()];
		for (int i = 0; i < list.length; i++) {
			list[i] = liste.get(i);
		}
		return list;
	}
	
	/**
	 * Liefert eine Liste von Kostentr�ger
	 * @return list Eine Liste aller Kostentr�ger
	 */
	public static String[] getKostentr�ger(){
		List<MyProjekte> myProjekte = readProjekteDB();

		List<String> liste = new ArrayList<String>();
		for (int i = 0; i < myProjekte.size(); i++) {
			liste.add(myProjekte.get(i).getKostentr�ger());
		}
		Collections.sort(liste);
		
		String[] list = new String[myProjekte.size()];
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
			configuration.common().objectClass(MyProjekte.class);
			
		    try {
				server = Db4oClientServer.openServer(configuration,DATENBANK, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "Server l�uft bereits ",SWT.ICON_ERROR);
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
