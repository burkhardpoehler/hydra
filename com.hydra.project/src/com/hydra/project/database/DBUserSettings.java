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
import com.hydra.project.model.MySettings;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;
/**
 * Enthält alle Funktionen für den Zugriff auf die Settings Datenbank
 * @author Poehler
 *
 */
public class DBSettingsTools {

	public static String DATENBANK = "C:\\Hydra\\Settings.DB4O";
	private static String thisClass= "DBSettingsTools";
	private static Boolean serverOFF = true;		//Flag gibt an, ob der Server bereits läuft
	static ObjectServer server = null;
	static ObjectContainer client = null;
	

	/**
	 * Liest die Settings Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * @return mySettings Das Setting des gefundenen Nutzers. Null wenn keiner gefunden.
	 */
	public static MySettings readSettingsDB(){
		String nutzer = System.getProperty("user.name");
		serverStarten();
		MySettings mySettings = null;
		try {
			Query queryNew = client.query();
			queryNew.constrain(MySettings.class);	
			queryNew.descend("nutzer").constrain(nutzer);	//suche nach angemeldetem Nutzer
			ObjectSet<MySettings> resultsNew = queryNew.execute();			
			if (resultsNew != null) {
				if (resultsNew.size() > 1) {
					//zuviele Treffer
					mySettings = resultsNew.get(0); //nimm den ersten
					LogfileView.log(thisClass,"Mehr als ein Datensatz gefunden", SWT.ICON_ERROR);
				}
				if (resultsNew.size() == 0) {
					//kein Treffer
					LogfileView.log(thisClass,"Kein passender Datensatz gefunden. Lege neuen an", SWT.ICON_INFORMATION);
					mySettings = new MySettings();
					mySettings.setNutzer(nutzer);
					client.store(mySettings);
					client.commit(); //speichern
				}	
				if (resultsNew.size() == 1) {	
					mySettings = resultsNew.get(0);
					LogfileView.log(thisClass, "Nutzersettings geladen für: "
							+ resultsNew.get(0).getNutzer(),
							SWT.ICON_INFORMATION);
				}
			}else{		//der Nutzer exsistiert noch nicht, daher neu anlegen
				mySettings = new MySettings();
				mySettings.setNutzer(nutzer);
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
	 * Schreibt ein Settings in die Settings Datenbank
	 * legt eine neue Datenbank an, wenn keine gefunden wird
	 * Vorhandene Datensätze werden überschrieben
	 * @param mySettings Ein aktueller Datensatz
	 */
	public static void writeSettingsDB(MySettings mySettings){
		serverStarten();
		try {  
				//prüfen, ob identischer Datensatz existiert
        	Query query=client.query();							//Abfrage definieren
    		query.constrain(MySettings.class);					//suche in allen Datensätze   	    
    		String nutzer = mySettings.getNutzer();				//nach Nutzernamen
    		query.descend("nutzer").constrain(nutzer);	
			ObjectSet<MySettings> results = query.execute();
			if (results.size() > 0) { //doppelten Datensatz gefunden					
				LogfileView.log(thisClass,"Mehr als ein Datensatz vorhanden: " 
						+ mySettings.getNutzer(),SWT.ICON_ERROR);
				client.store(results.get(0));
				client.commit(); //speichern
			} else {
				LogfileView.log(thisClass,"Settings speichern: " 
						+ mySettings.getNutzer(),SWT.ICON_INFORMATION);
				client.store(results.get(0));
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
			configuration.common().activationDepth(1);
			configuration.common().objectClass(MySettings.class);
			
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
