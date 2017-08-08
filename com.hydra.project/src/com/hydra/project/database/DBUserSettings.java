/**
 * 
 */
package com.hydra.project.database;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.query.Query;
import com.hydra.project.model.MyUserSettings;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.startProcedure.StartProcedure;
/**
 * Enthält alle Funktionen für den Zugriff auf die User Settings Datenbank
 * @author Poehler
 *
 */
public class DBUserSettings {

//	public static String DATENBANK = "C:\\Hydra\\Settings.DB4O";
	private static String thisClass= "DBUserSettings";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	private static String settingPfad = StartProcedure.getUserDir();
	public static String DATENBANK = settingPfad + "\\UserSettings.DB4O";

	/**
	 * Liest die User Settings Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return mySettings Das Setting des gefundenen Nutzers. Null wenn keiner gefunden.
	 */
	public static MyUserSettings readSettingsDB(){
		String user = System.getProperty("user.name");
		serverStarten();
		MyUserSettings mySettings = null;
		try {
			Query queryNew = client.query();
			queryNew.constrain(MyUserSettings.class);	
			queryNew.descend("user").constrain(user);	//suche nach angemeldetem Nutzer
			ObjectSet<MyUserSettings> resultsNew = queryNew.execute();			
			if (resultsNew != null) {
				if (resultsNew.size() > 1) {
					//zuviele Treffer
					mySettings = resultsNew.get(0); //nimm den ersten
					LogfileView.log(thisClass,"Mehr als ein Datensatz gefunden", SWT.ICON_ERROR);
				}
				if (resultsNew.size() == 0) {
					//kein Treffer
					LogfileView.log(thisClass,"Kein passender Datensatz gefunden. Lege neuen an", SWT.ICON_INFORMATION);
					mySettings = new MyUserSettings();
					mySettings.setUser(user);
					client.store(mySettings);
					client.commit(); //speichern
				}	
				if (resultsNew.size() == 1) {	
					mySettings = resultsNew.get(0);
					LogfileView.log(thisClass, "Nutzersettings geladen für: "+ resultsNew.get(0).getUser(),SWT.ICON_INFORMATION);
				}
			}else{		//der Nutzer exsistiert noch nicht, daher neu anlegen
				mySettings = new MyUserSettings();
				mySettings.setUser(user);
				client.store(mySettings);
				client.commit(); //speichern
			}
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
		serverStoppen();
		return mySettings;
	}
	
	/**
	 * Schreibt ein User Settings in die Settings Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze werden überschrieben
	 * @param mySettings Ein aktueller Datensatz
	 */
	public static void writeSettingsDB(MyUserSettings mySettings){
		serverStarten();
		try {  
				//prüfen, ob identischer Datensatz existiert
        	Query query=client.query();							//Abfrage definieren
    		query.constrain(MyUserSettings.class);					//suche in allen Datensätze   	    
    		String user = mySettings.getUser();				//nach Nutzernamen
    		query.descend("user").constrain(user);	
			ObjectSet<MyUserSettings> results = query.execute();
			if (results.size() > 0) { //Datensatz gefunden	
				for (int i = 0; i < results.size(); i++) {
					client.delete(results.get(i));					
					LogfileView.log(thisClass,"Lösche Datensatz: " + mySettings.getUser(),SWT.ICON_INFORMATION);				
				}
				LogfileView.log(thisClass,"Settings speichern: " + mySettings.getUser(),SWT.ICON_INFORMATION);
				client.store(mySettings);
				client.commit(); //speichern
			} else {
				LogfileView.log(thisClass,"Settings speichern: " + mySettings.getUser(),SWT.ICON_INFORMATION);
				client.store(mySettings);
				client.commit(); //speichern
			}
			
		} catch (DatabaseClosedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Startet den Server
	 */
	public static void serverStarten(){
		if (serverOFF) {
			ServerConfiguration configuration = Db4oClientServer.newServerConfiguration();
			configuration.common().activationDepth(5);
			configuration.common().objectClass(MyUserSettings.class);
			
		    try {
				server = Db4oClientServer.openServer(configuration,DATENBANK, 0);
			} catch ( DatabaseFileLockedException e) {
				LogfileView.log(thisClass, "User Server läuft bereits ",SWT.ICON_ERROR);
				e.printStackTrace();
			}
			client = server.openClient();
			LogfileView.log(thisClass, " User Server starten ",SWT.ICON_INFORMATION);
			serverOFF = false;
		}
	}
	
	/**
	 * Stoppt den Server
	 */
	public static void serverStoppen(){
		client.close();
		server.close();
		LogfileView.log(thisClass, " User Server stoppen ",SWT.ICON_INFORMATION);
		serverOFF = true;
	}
}
