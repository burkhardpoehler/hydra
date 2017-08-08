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
import com.hydra.project.model.MyCompanySettings;
import com.hydra.project.model.MyTableCustomization;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.startProcedure.StartProcedure;
import com.hydra.project.myplugin_nebula.xviewer.customize.CustomizeData;
/**
 * Enthält alle Funktionen für den Zugriff auf die Company Settings Datenbanken
 * @author Poehler
 *
 */
public class DBCompanySettings {

//	public static String DATENBANK = "C:\\Hydra\\Company.DB4O";
	private static String thisClass= "DBCompanySettings";
	private static Boolean serverOFFTS = true;		//Flag gibt an, ob der Server für Tablesettings bereits läuft
	private static Boolean serverOFFCS = true;		//Flag gibt an, ob der Server für Companysettings bereits läuft
	static ObjectServer serverTS = null;
	static ObjectServer serverCS = null;
	static ObjectContainer clientTS = null;
	static ObjectContainer clientCS = null;
	private static String settingPfad = StartProcedure.getCompanyDir();
	public static String DATENBANK_TS = settingPfad + "\\TableSettings.DB4O";
	public static String DATENBANK_CS = settingPfad + "\\CompanySettings.DB4O";

	/**
	 * Liest die Company Settings Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return myCompany Die Einstellungen der Firma. Null wenn keiner gefunden.
	 */
	public static MyCompanySettings readDB(){
		MyCompanySettings myCompany = new MyCompanySettings();
		serverStartenCS();
//		MyCompany myCompany = null;
		try {
			Query queryNew = clientCS.query();
			queryNew.constrain(MyCompanySettings.class);	
			String nutzer = myCompany.getNutzer();				//nach Nutzernamen
			queryNew.descend("nutzer").constrain(nutzer);		//suche nach angemeldetem Nutzer
			ObjectSet<MyCompanySettings> resultsNew = queryNew.execute();			
			if (resultsNew != null) {
				if (resultsNew.size() > 1) {
					//zuviele Treffer
					myCompany = resultsNew.get(0); //nimm den ersten
					LogfileView.log(thisClass,"Mehr als ein Datensatz gefunden", SWT.ICON_ERROR);
				}
				if (resultsNew.size() == 0) {
					//kein Treffer
					LogfileView.log(thisClass,"Kein passender Datensatz gefunden. Lege neuen an", SWT.ICON_INFORMATION);
					myCompany = new MyCompanySettings();
					myCompany.setNutzer(nutzer);
					clientCS.store(myCompany);
					clientCS.commit(); //speichern
				}	
				if (resultsNew.size() == 1) {	
					myCompany = resultsNew.get(0);
					LogfileView.log(thisClass, "Companysettings geladen für: "+ resultsNew.get(0).getNutzer(), SWT.ICON_INFORMATION);
				}
			}else{		//der Nutzer exsistiert noch nicht, daher neu anlegen
				myCompany = new MyCompanySettings();
				myCompany.setNutzer(nutzer);
				clientCS.store(myCompany);
				clientCS.commit(); //speichern
			}
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
		serverStoppenCS();
		return myCompany;
	}
	
	/**
	 * Schreibt die Firmendaten in die Company Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze werden überschrieben
	 * @param myCompany Ein aktueller Datensatz
	 */
	public static void writeDB(MyCompanySettings myCompany){
		serverStartenCS();
		try {  
				//prüfen, ob identischer Datensatz existiert
        	Query query=clientCS.query();							//Abfrage definieren
    		query.constrain(MyCompanySettings.class);					//suche in allen Datensätze   	    
    		String nutzer = myCompany.getNutzer();				//nach Nutzernamen
    		query.descend("nutzer").constrain(nutzer);	
    		
			ObjectSet<MyCompanySettings> results = query.execute();
			if (results.size() > 0) { //doppelten Datensatz gefunden					
				LogfileView.log(thisClass,"Aktualisiere Company Datensatz: " + myCompany.getNutzer(),SWT.ICON_ERROR);
				clientCS.delete(results.get(0));
				clientCS.store(myCompany);
				clientCS.commit(); //speichern
			} else {
				LogfileView.log(thisClass,"Neuen Company Datensatz speichern: "+ myCompany.getNutzer(),SWT.ICON_INFORMATION);
				clientCS.store(myCompany);
				clientCS.commit(); //speichern
			}
			
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * Liest die Company Datenbank.
//	 * legt eine neue Datenbank an, wenn keine gefunden wird.
//	 * @param customizeData Die zu lesende Klasse.
//	 * @return customizeData Die Liste mit den gespeicherte Tabelleneinstellung.
//	 */
//	public static List<CustomizeData> readDB(CustomizeData customizeData){
//		List<CustomizeData> custDatas = new ArrayList<CustomizeData>();
//		serverStarten(customizeData);
////		MyCompany myCompany = null;
//		try {
//			
//			Query query=client.query();							//Abfrage definieren
//			query.constrain(CustomizeData.class);				//suche alle Datensätze		
////			query.descend("Guid").constrain(customizeData.getGuid()); //filter nach der gewünschten Tabelle
//			query.descend("tableSpace").constrain(customizeData.getTableSpace());	
//			ObjectSet<CustomizeData> results=query.execute();
////			Utilities.listResult(results);
//			if (results.size()>0){
//				custDatas = results;
//				LogfileView.log(thisClass, "Tabelleneinstellungen geladen",SWT.ICON_INFORMATION);
//		    }
//		} catch (DatabaseClosedException e) {
//			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
//			e.printStackTrace();
//			
//		}
//		serverStoppen();
//		return custDatas;
//	}
	
	/**
	 * Löscht aus der Table Datenbank eine Tabelleneinstellungen.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param directory Der virtuelle Verzeichnispfad.
	 * @return flag True= löschen erfolgreich.
	 */
	public static boolean deleteTableConfigInDB(String directory, String filename){
		boolean flag = false;
		serverStartenTS();
		try {
			Query query=clientTS.query();							//Abfrage definieren
			query.constrain(MyTableCustomization.class);				//suche alle Datensätze		
			Query filenameQuery=query.descend("filter");
			directory.replace("\\\\","\\");
			query.descend("directory").constrain(directory).and(filenameQuery.constrain(filename));
			ObjectSet<MyTableCustomization> results=query.execute();
			Utilities.listResultCusto(results);
			if (results.size()== 1){
				clientTS.delete(results.get(0));
				clientTS.commit();
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
	 * Liest aus der Table Datenbank die Tabelleneinstellungen.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param directory Der virtuelle Verzeichnispfad.
	 * @param filter Der Dateifilter
	 * @return list Die Liste mit den gespeicherte Tabelleneinstellung.
	 */
	public static List<MyTableCustomization> readTableDB(String directory, String filter){
		List<MyTableCustomization> list = new ArrayList<MyTableCustomization>();
		serverStartenTS();
		try {
			
			Query query=clientTS.query();							//Abfrage definieren
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
	 * Liest aus der Table Datenbank die Standardtabelleneinstellung.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param filename Der virtuelle Verzeichnispfad.
	 * @return defaultGuid Die ID der Standardtabelleneinstellung.
	 */
	public static String readDefaultFileFromDB(String directory , String filename){
		directory.replace("\\\\", "\\");
		String defaultGuid = null;
		serverStartenTS();
		try {
			
			Query query=clientTS.query();							//Abfrage definieren
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
					clientTS.delete(results.get(n));
					clientTS.commit();
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
	 * Schreibt in die Table Datenbank eine Tabelleneinstellung.
	 * Legt eine neue Datenbank an wenn keine gefunden wird.
	 * @param custo Der Datensatz
	 */
	public static void writeFileToDB(MyTableCustomization custo){
		serverStartenTS();
		try {
			clientTS.store(custo);
			clientTS.commit();
		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"Lesen der Tabelleneinstellung aus der Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
		serverStoppenTS();

	}
	

	
	/**
	 * Schreibt die Tabelleneinstellungen in die Company Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze des Nutzers werden überschrieben
	 * @param custDatas Die aktuellen Datensätze
	 */
	public static boolean writeDB(List<CustomizeData> custDatas){
		boolean flag = false;
		serverStartenTS();
		try {  
				//prüfen, ob identischer Datensatz existiert		
			Query query=clientTS.query();							//Abfrage definieren
			query.constrain(CustomizeData.class);				//suche alle Datensätze		
//			query.descend("Guid").constrain(custDatas.get(0).getGuid()); //filter nach der gewünschten Tabelle
			query.descend("tableSpace").constrain(custDatas.get(0).getTableSpace());	
			ObjectSet<CustomizeData> results=query.execute();
			Utilities.listResult(results);
			
			if (results.size()>0){
				for (int n = 0; n < results.size(); n++) {
					clientTS.delete(results.get(n));
				}
			}
			for (int n = 0; n < custDatas.size(); n++) {
				clientTS.store(custDatas.get(n));
				clientTS.commit();
				LogfileView.log(thisClass, "Tabelleneinstellungen gespeichert" + custDatas.get(n).getName() ,SWT.ICON_INFORMATION);
				flag = true;		
			}

		} catch (DatabaseClosedException e) {
			LogfileView.log(thisClass,"SpeicherungTabelleneinstellung in die Company Datenbank misslungen", SWT.ICON_ERROR);
			e.printStackTrace();
		}
		return flag;
	}
	
//	/**
//	 * Startet den Server für Tablesettings
//	 */
//	public static void serverStartenTS(Object o){
//		if (serverOFFTS) {
//			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
//			configuration.common().activationDepth(1);
//			configuration.common().objectClass(MyTableCustomization.class);
//			
//		    try {
//				serverTS = Db4oClientServer.openServer(configuration,DATENBANK_TS, 0);
//			} catch ( DatabaseFileLockedException e) {
//				LogfileView.log(thisClass, "Server läuft bereits ",SWT.ICON_ERROR);
//				e.printStackTrace();
//			}
//			clientTS = serverTS.openClient();
//			LogfileView.log(thisClass, " serverStarten ",SWT.ICON_INFORMATION);
//			serverOFFTS = false;
//		}
//	}
	
	
	/**
	 * Startet den Server für Tablesettings
	 */
	public static void serverStartenTS(){
		if (serverOFFTS) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyTableCustomization.class);
			
		    try {
				serverTS = Db4oClientServer.openServer(configuration,DATENBANK_TS, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "Server läuft bereits ",SWT.ICON_ERROR);
				e.printStackTrace();
			}
			clientTS = serverTS.openClient();
			LogfileView.log(thisClass, "Server starten  für Tabellenkonfiguration",SWT.ICON_INFORMATION);
			serverOFFTS = false;
		}
	}
	
	/**
	 * Startet den Server für Companysettings
	 */
	public static void serverStartenCS(){
		if (serverOFFCS) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MyCompanySettings.class);
			
		    try {
				serverCS = Db4oClientServer.openServer(configuration,DATENBANK_CS, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "Server läuft bereits ",SWT.ICON_ERROR);
				e.printStackTrace();
			}
			clientCS = serverCS.openClient();
			LogfileView.log(thisClass, "Server starten für Companysettings",SWT.ICON_INFORMATION);
			serverOFFCS = false;
		}
	}
	
	
	/**
	 * Stoppt den Server für Tablesettings
	 */
	public static void serverStoppenTS(){
		clientTS.close();
		serverTS.close();
		LogfileView.log(thisClass, "Server schließen für Tabellenkonfiguration",SWT.ICON_INFORMATION);
		serverOFFTS = true;
	}
	
	/**
	 * Stoppt den Server für Companysettings
	 */
	public static void serverStoppenCS(){
		clientCS.close();
		serverCS.close();
		LogfileView.log(thisClass, "Server schließen für Companysettings",SWT.ICON_INFORMATION);
		serverOFFCS = true;
	}
	
}
