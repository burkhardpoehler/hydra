/**
 * 
 */
package com.hydra.project.database;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.query.Query;
import com.hydra.project.model.MyCompany;
import com.hydra.project.model.MyTableCustomization;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.myplugin_nebula.xviewer.customize.CustomizeData;
/**
 * Enthält alle Funktionen für den Zugriff auf die Company Datenbank
 * @author Poehler
 *
 */
public class DBCompanyTools {

	public static String DATENBANK = "C:\\Hydra\\Company.DB4O";
	private static String thisClass= "DBCompanyTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	private CustomizeData customizeData;

	/**
	 * Liest die Company Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return myCompany Die Einstellungen der Firma. Null wenn keiner gefunden.
	 */
	public static MyCompany readDB(){
		String nutzer = System.getProperty("user.name");
		MyCompany myCompany = new MyCompany();
		serverStarten(myCompany);
//		MyCompany myCompany = null;
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyCompany.class);	
			queryNew.descend("nutzer").constrain(nutzer);	//suche nach angemeldetem Nutzer
			ObjectSet<MyCompany> resultsNew = queryNew.execute();			
			if (resultsNew != null) {
				if (resultsNew.size() > 1) {
					//zuviele Treffer
					myCompany = resultsNew.get(0); //nimm den ersten
					LogfileView.log(thisClass,"Mehr als ein Datensatz gefunden", SWT.ICON_ERROR);
				}
				if (resultsNew.size() == 0) {
					//kein Treffer
					LogfileView.log(thisClass,"Kein passender Datensatz gefunden. Lege neuen an", SWT.ICON_INFORMATION);
					myCompany = new MyCompany();
					myCompany.setNutzer(nutzer);
					client.store(myCompany);
					client.commit(); //speichern
				}	
				if (resultsNew.size() == 1) {	
					myCompany = resultsNew.get(0);
					LogfileView.log(thisClass, "Nutzersettings geladen für: "
							+ resultsNew.get(0).getNutzer(),
							SWT.ICON_INFORMATION);
				}
			}else{		//der Nutzer exsistiert noch nicht, daher neu anlegen
				myCompany = new MyCompany();
				myCompany.setNutzer(nutzer);
				client.store(myCompany);
				client.commit(); //speichern
			}
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
		serverStoppen();
		return myCompany;
	}
	
	/**
	 * Liest die Company Datenbank.
	 * legt eine neue Datenbank an, wenn keine gefunden wird.
	 * @param customizeData Die zu lesende Klasse.
	 * @return customizeData Die Liste mit den gespeicherte Tabelleneinstellung.
	 */
	public static List<CustomizeData> readDB(CustomizeData customizeData){
		List<CustomizeData> custDatas = new ArrayList<CustomizeData>();
		serverStarten(customizeData);
//		MyCompany myCompany = null;
		try {
			
			Query query=client.query();							//Abfrage definieren
			query.constrain(CustomizeData.class);				//suche alle Datensätze		
//			query.descend("Guid").constrain(customizeData.getGuid()); //filter nach der gewünschten Tabelle
			query.descend("tableSpace").constrain(customizeData.getTableSpace());	
			ObjectSet<CustomizeData> results=query.execute();
//			Utilities.listResult(results);
			if (results.size()>0){
				custDatas = results;
				LogfileView.log(thisClass, "Tabelleneinstellungen geladen",SWT.ICON_INFORMATION);
		    }
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
			
		}
		serverStoppen();
		return custDatas;
	}
	
	/**
	 * Löscht aus der Company Datenbank eine Tabelleneinstellungen.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param directory Der virtuelle Verzeichnispfad.
	 * @return flag True= löschen erfolgreich.
	 */
	public static boolean deleteTableConfigInDB(String directory, String filename){
		boolean flag = false;
		serverStartenTableConfig();
		try {
			Query query=client.query();							//Abfrage definieren
			query.constrain(MyTableCustomization.class);				//suche alle Datensätze		
			Query filenameQuery=query.descend("filter");
			directory.replace("\\\\","\\");
			query.descend("directory").constrain(directory).and(filenameQuery.constrain(filename));
			ObjectSet<MyTableCustomization> results=query.execute();
			Utilities.listResultCusto(results);
			if (results.size()== 1){
				client.delete(results.get(0));
				client.commit();
				flag = true;
		    }
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
//		serverStoppen();
		
		if (flag){
			LogfileView.log(thisClass, "Tabelleneinstellung gelöscht",SWT.ICON_INFORMATION);
		}else{
			LogfileView.log(thisClass, "Tabelleneinstellung konnte nicht gelöscht werden",SWT.ICON_ERROR);
		}
		return flag;
	}
	
	/**
	 * Liest aus der Company Datenbank die Tabelleneinstellungen.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param directory Der virtuelle Verzeichnispfad.
	 * @param filter Der Dateifilter
	 * @return list Die Liste mit den gespeicherte Tabelleneinstellung.
	 */
	public static List<MyTableCustomization> readDB(String directory, String filter){
		List<MyTableCustomization> list = new ArrayList<MyTableCustomization>();
		serverStartenTableConfig();
		try {
			
			Query query=client.query();							//Abfrage definieren
			query.constrain(MyTableCustomization.class);				//suche alle Datensätze		
			query.descend("directory").constrain(directory);
//			query.descend("filter").constrain(filter);			
			ObjectSet<MyTableCustomization> results=query.execute();
			Utilities.listResultCusto(results);
			if (results.size()>1){
				for (int n = 0; n < results.size(); n++) {
					list.add(results.get(n));
				}
				LogfileView.log(thisClass, "Tabelleneinstellungen geladen",SWT.ICON_INFORMATION);
		    }
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
//		serverStoppen();
		return list;
	}
	
	/**
	 * Liest aus der Company Datenbank die Standardtabelleneinstellung.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param filename Der virtuelle Verzeichnispfad.
	 * @return defaultGuid Die ID der Standardtabelleneinstellung.
	 */
	public static String readDefaultFileFromDB(String directory , String filename){
		directory.replace("\\\\", "\\");
		String defaultGuid = null;
		serverStartenTableConfig();
		try {
			
			Query query=client.query();							//Abfrage definieren
			query.constrain(MyTableCustomization.class);				//suche alle Datensätze		
			Query filenameQuery=query.descend("filter");
			query.descend("directory").constrain(directory).and(filenameQuery.constrain(filename));
			ObjectSet<MyTableCustomization> results=query.execute();
			Utilities.listResultCusto(results);
			if (results.size()>0){
				defaultGuid = results.get(0).getId();
		    }
			if (results.size()>1){	//überzählige Standardeinstellungen löschen
				for (int n = 1; n < results.size(); n++) {
					client.delete(results.get(n));
					client.commit();
				}
			}
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
//		serverStoppen();
		return defaultGuid;
	}
	
	/**
	 * Schreibt in die Company Datenbank eine Tabelleneinstellung.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param custo Der Datensatz
	 */
	public static void writeFileToDB(MyTableCustomization custo){
		serverStarten(MyTableCustomization.class);
		try {
			client.store(custo);
			client.commit();
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
		serverStoppen();

	}
	
	/**
	 * Schreibt ein Firmendaten in die Company Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze werden überschrieben
	 * @param myCompany Ein aktueller Datensatz
	 */
	public static void writeDB(MyCompany myCompany){
		serverStarten(myCompany);
		try {  
				//prüfen, ob identischer Datensatz existiert
        	Query query=client.query();							//Abfrage definieren
    		query.constrain(MyCompany.class);					//suche in allen Datensätze   	    
    		String nutzer = myCompany.getNutzer();				//nach Nutzernamen
    		query.descend("nutzer").constrain(nutzer);	
    		
			ObjectSet<MyCompany> results = query.execute();
			if (results.size() > 0) { //doppelten Datensatz gefunden					
				LogfileView.log(thisClass,"Mehr als ein Datensatz vorhanden: " 
						+ myCompany.getNutzer(),SWT.ICON_ERROR);
				client.store(results.get(0));
				client.commit(); //speichern
			} else {
				LogfileView.log(thisClass,"Settings speichern: " 
						+ myCompany.getNutzer(),SWT.ICON_INFORMATION);
				client.store(results.get(0));
				client.commit(); //speichern
			}
			
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schreibt die TAbelleneinstellungen in die Company Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze des Nutzers werden überschrieben
	 * @param custDatas Die aktuellen Datensätze
	 */
	public static boolean writeDB(List<CustomizeData> custDatas){
		boolean flag = false;
		CustomizeData customizeData = new CustomizeData();
		serverStarten(customizeData);
		try {  
				//prüfen, ob identischer Datensatz existiert		
			Query query=client.query();							//Abfrage definieren
			query.constrain(CustomizeData.class);				//suche alle Datensätze		
//			query.descend("Guid").constrain(custDatas.get(0).getGuid()); //filter nach der gewünschten Tabelle
			query.descend("tableSpace").constrain(custDatas.get(0).getTableSpace());	
			ObjectSet<CustomizeData> results=query.execute();
			Utilities.listResult(results);
			
			if (results.size()>0){
				for (int n = 0; n < results.size(); n++) {
					client.delete(results.get(n));
				}
			}
			for (int n = 0; n < custDatas.size(); n++) {
				client.store(custDatas.get(n));
				client.commit();
				LogfileView.log(thisClass, "Tabelleneinstellungen gespeichert" + custDatas.get(n).getName() ,SWT.ICON_INFORMATION);
				flag = true;		
			}

		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"SpeicherungTabelleneinstellung in die Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * Startet den Server
	 */
	public static void serverStarten(Object o){
		if (serverOFF) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyTableCustomization.class);
			
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
	 * Startet den Server
	 */
	public static void serverStartenTableConfig(){
		if (serverOFF) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyTableCustomization.class);
			
		    try {
				server = Db4oClientServer.openServer(configuration,DATENBANK, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "Server läuft bereits ",SWT.ICON_ERROR);
				e.printStackTrace();
			}
			client = server.openClient();
			LogfileView.log(thisClass, "Server starten  für Tabellenkonfiguration",SWT.ICON_INFORMATION);
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
