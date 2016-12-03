/**
 * 
 */
package com.hydra.project.database;

import java.io.File;
import java.util.List;
import com.hydra.project.myplugin_nebula.xviewer.customize.CustomizeData;
import com.hydra.projects.XViewer.Main.MyXViewer;
import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.constraints.UniqueFieldValueConstraintViolationException;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;
import com.hydra.project.model.MySettings;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.OpenProject;
import com.hydra.project.model.TreeTools;


/**
 * @author Burkhard Pöhler
 * enthält alle Methoden zur Datenbankabfrage
 */
public class DBTools extends Utilities {
	private static String thisClass= "DBTools";
    private static final EmbeddedObjectContainer EmbeddedObjectContainer = null;
    
    //Serverzugangsdaten
	private static final String PASSWORD = "db4o";
	private static final String USER = "db4o";
	private static final int PORT = 4488;
	private static String HOST = "localhost";
	
	private static int dateiIndex=0;
	public static String SETTINGSDATENBANK = "C:\\Hydra\\Settings.DB4O";
	public static String STUNDENDATENBANK = "C:\\Hydra\\Stunden.DB4O";
	private static MySettings mySettings= new MySettings();
	
	static ObjectServer[] server = new ObjectServer[50];
	static ObjectContainer[] client = new ObjectContainer[50];
	

    static EmbeddedObjectContainer[] db = new EmbeddedObjectContainer[50];
	static EmbeddedObjectContainer dbSettings =  EmbeddedObjectContainer;
	
	/**
	 * bekommt den Dummy dummyMyTreeItem
	 * und öffnet die darin gespeicherte Datei
	 * und holt den obersten Knoten
	 * und übergibt ihn an das treeItem
	 * @param myTreeItem  Der Dummy
	 * @return Oberste treeItem mit dem vollständigen Baum
	 */
	public static MyTreeItem dateiOeffnen(MyTreeItem myTreeItem){
		int index=myTreeItem.getDbIndex();
// 	   	Configuration conf = Db4o.newConfiguration();
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();
		conf.common().objectClass(MyXViewer.class);
		conf.common().objectClass(CustomizeData.class);
		conf.common().objectClass(MyTreeItem.class).cascadeOnUpdate(true);
		conf.common().objectClass(MyTreeItem.class).minimumActivationDepth(50);
		conf.common().objectClass(MyTreeItem.class).cascadeOnDelete(true);
		conf.common().objectClass(MyTreeItem.class).updateDepth(50);
//		conf.common().objectClass(MyTreeItem.class).objectField("name").indexed(true);
		conf.common().objectClass(MyTreeItem.class).objectField("uuid").indexed(true);
//	    conf.add(new UniqueFieldValueConstraint(MyTreeItem.class, "name"));
	
        try {
    		System.out.println("Datenbank öffnen: Index: " + myTreeItem.getDbIndex() + " Name: " + myTreeItem.getDatenbankName());
    		db[index] = Db4oEmbedded.openFile(conf, myTreeItem.getDatenbankName());

		} catch (Db4oIOException e) {
			e.printStackTrace();
		} catch (DatabaseFileLockedException e) {
			e.printStackTrace();
		} catch (IncompatibleFileFormatException e) {
			e.printStackTrace();
		} catch (OldFormatException e) {
			e.printStackTrace();
		} catch (DatabaseReadOnlyException e) {
			e.printStackTrace();
		} catch (UniqueFieldValueConstraintViolationException  e) {
			e.printStackTrace();
		}
		
		Query query=db[index].query();							//Abfrage definieren
		query.constrain(MyTreeItem.class);							//suche alle Datensätze
	    query.descend("isObersterKnoten").constrain(true);	    
	    
	    //nach obersten Knoten suchen
	    ObjectSet<MyTreeItem> results=query.execute();
//		Utilities.listResult(results);
	    MyTreeItem myNewTreeItem = null;
		if (results.size()>0){
	    	myNewTreeItem = results.get(0);
	    	myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
	    	myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
	    }
		return myNewTreeItem;
	}

	/**
	 * Schließt eine bestimmte Datenbank
	 * @param myTreeItem  enthält den Dateiindex der zu schließenden Datei
	 */
	public static  void dateiSchliessen(MyTreeItem myTreeItem){
		  dateiIndex=myTreeItem.getDbIndex();	
		  if (dateiIndex >0) {			//index 0 ist nicht erlaubt
			  db[dateiIndex].close();
		}
	}   
	
//	/**
//	 * Schließt alle Datenbanken
//	 */
//	public static  void alleDateienSchliessen(){
//		  for (int i = 0; i < dateiIndex; i++) {
//			db[i].close();
//		}
//	}   
	
	/**
	 * bekommt den Dummy dummyMyTreeItem
	 * und öffnet die darin gespeicherte Datei
	 * und holt den obersten Knoten
	 * und übergibt ihn an das treeItem
	 * @param myTreeItem  Der Dummy
	 * @return Oberste treeItem mit dem vollständigen Baum
	 */
	public static MyTreeItem serverDateiOeffnen(MyTreeItem myTreeItem){
		
		int index=myTreeItem.getDbIndex();
//		ClientConfiguration conf = Db4oClientServer.newClientConfiguration();	 
		ClientConfiguration conf = Db4oClientServer.newClientConfiguration();	
//		conf.common().objectClass(MyXViewer.class);
//		conf.common().objectClass(CustomizeData.class);
		conf.common().objectClass(MyTreeItem.class).cascadeOnUpdate(true);
		conf.common().objectClass(MyTreeItem.class).minimumActivationDepth(50);
		conf.common().objectClass(MyTreeItem.class).cascadeOnDelete(true);
		conf.common().objectClass(MyTreeItem.class).updateDepth(50);
		conf.common().objectClass(MyTreeItem.class).objectField("uuid").indexed(true);
		
		server[index] = Db4oClientServer.openServer(Db4oClientServer.newServerConfiguration(), myTreeItem.getDatenbankName(), PORT);
		server[index].grantAccess(USER, PASSWORD);
			try {
				System.out.println("Datenbankserver öffnen: Index: " + myTreeItem.getDbIndex() + " Name: " + myTreeItem.getDatenbankName());
				client[index] = Db4oClientServer.openClient(conf, "localhost", PORT, USER, PASSWORD);

				
			} finally {
//				server[index].close();
			}
		
			Query query=client[index].query();							//Abfrage definieren
			query.constrain(MyTreeItem.class);							//suche alle Datensätze
		    query.descend("isObersterKnoten").constrain(true);	    
		    
		    //nach obersten Knoten suchen
		    ObjectSet<MyTreeItem> results=query.execute();
			Utilities.listResult(results);
		    MyTreeItem myNewTreeItem = null;
			if (results.size()>0){
		    	myNewTreeItem = results.get(0);
		    	myNewTreeItem.setDbIndex(myTreeItem.getDbIndex());
		    	myNewTreeItem.setDatenbankName(myTreeItem.getDatenbankName());
		    }
		
		return myNewTreeItem;
		
	}
	

	/**
	 * Schließt eine bestimmte Serverdatenbank
	 * @param myTreeItem  enthält den Dateiindex der zu schließende Datei
	 */
	public static  void serverDateiSchliessen(MyTreeItem myTreeItem){
		  dateiIndex=myTreeItem.getDbIndex();	
			// Do something with this client, or open more clients
			client[dateiIndex].close();
			server[dateiIndex].close();
	}   
	
	/**
	 * Schließt alle Serverdatenbanken
	 */
	public static  void alleServerDateienSchliessen(){
		  for (int i = 0; i < dateiIndex; i++) {
			  client[i].close();
			  server[i].close();
		}
	}   
	

	
	public static List readLastOpenProjects(){
		//Prüfen, ob Datenbank existiert  
		File file = new File(SETTINGSDATENBANK);	
		if(!file.exists()){
			ObjectContainer neu = Db4oEmbedded.openFile(SETTINGSDATENBANK);
			OpenProject openproject = new OpenProject("leer");
			neu.store(openproject);
			neu.commit();
			neu.close();
		}

        ObjectContainer dbSettings = Db4oEmbedded.openFile(SETTINGSDATENBANK);
		Query query=dbSettings.query();							//Abfrage definieren
		query.constrain(OpenProject.class);							//suche alle Datensätze
		ObjectSet<List> results=query.execute();
		List item = results.get(0);
		dbSettings.close();
		return item;
	}
	

	
	/**
	 * Der gespeichrte Knoten wird komplett ausgegeben
	 * @author Burkhard Pöhler
	 * @param	myTreeItem ausgewählter Knoten
	 * @return der komplette gespeicherte Knoten
	 */
	public static MyTreeItem picMyStoredTreeItem() {

		ObjectContainer dbtemp = null;
		
		EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();	
		conf.common().objectClass(MyTreeItem.class).cascadeOnUpdate(true);
		conf.common().objectClass(MyTreeItem.class).minimumActivationDepth(50);
		conf.common().objectClass(MyTreeItem.class).cascadeOnDelete(true);
		conf.common().objectClass(MyTreeItem.class).updateDepth(50);
		conf.common().objectClass(MyTreeItem.class).objectField("name").indexed(true);
		
        try {
 
    		dbtemp = Db4oEmbedded.openFile(conf, "temp");

		} catch (Db4oIOException e) {
			e.printStackTrace();
		} catch (DatabaseFileLockedException e) {
			e.printStackTrace();
		} catch (IncompatibleFileFormatException e) {
			e.printStackTrace();
		} catch (OldFormatException e) {
			e.printStackTrace();
		} catch (DatabaseReadOnlyException e) {
			e.printStackTrace();
		} catch (UniqueFieldValueConstraintViolationException  e) {
			e.printStackTrace();
		}
		
		Query query=dbtemp.query();							//Abfrage definieren
		query.constrain(MyTreeItem.class);							//suche alle Datensätze
//		query.descend("uuid").constrain(myTreeItem.getUuid());		//nach Knoten suchen
		ObjectSet<MyTreeItem> results=query.execute();
   		System.out.println("In temporärer Datenbank gefunden:");
//		Utilities.listResult(results);
	    MyTreeItem myNewTreeItem=(MyTreeItem) results.get(0);
		dbtemp.close();
		myNewTreeItem.setUuid();
		return myNewTreeItem;
		
	}    
	
	public static void updateMyTreeItem(MyTreeItem myTreeItem) {
		if (myTreeItem != null ) {
			if (myTreeItem.isDirty()) myTreeItem.updateValues();		//vor dem speichern, Datensatz auf neuesten Stand bringen
			if (!myTreeItem.getDatenbankName().equals("leer")) {
				
//				  dateiIndex = myTreeItem.getDbIndex();	 Fehlerhaft wenn Knoten aus anderer Datenbank kopiert wurde  
				  MyTreeItem obersterKnotenMyTreeItem = TreeTools.searchForObersterknoten(myTreeItem);
				  dateiIndex = obersterKnotenMyTreeItem.getDbIndex();
				  MyTreeItem myParentTreeItem = myTreeItem.getParent();		//Vaterknoten zwischenspeichern
				  myTreeItem.setParent(null);								//Den Verweis auf die übergeordneten Knoten entfernen
				  
				Query query = db[dateiIndex].query(); //Abfrage definieren
				query.constrain(MyTreeItem.class); //suche alle Datensätze
				query.descend("uuid").constrain(myTreeItem.getUuid()); //nach Knoten suchen
				ObjectSet<MyTreeItem> results = query.execute();
				listResult(results);
				MyTreeItem myStoredTreeItem = results.get(0);
				System.out.println("speichere in Datenbank: "
						+ myTreeItem.getBeschreibung() + "   Dateiindex: "
						+ myTreeItem.getDbIndex() + "   Dateiname: "
						+ myTreeItem.getDatenbankName());
				//		  System.out.println("Datensatz  zu speicheren : " + myStoredTreeItem.getAnzeigeText());
				if (results.size() > 0) {
					db[dateiIndex].store(myStoredTreeItem);
					db[dateiIndex].commit(); //speichern
					System.out.println("Datensatz gespeichert");

				}
				myTreeItem.setParent(myParentTreeItem); 			//den übergeordneten Knoten wieder setzen
			}
		}
		
	}    
	
	public static void updateMyTreeItemOnly(MyTreeItem myTreeItem) {
		if (myTreeItem != null ) {
			if (myTreeItem.isDirty()) myTreeItem.updateValues();		//vor dem speichern, Datensatz auf neuesten Stand bringen			
			  dateiIndex = TreeTools.searchForObersterknoten(myTreeItem).getDbIndex();
			  try {
				  ObjectSet<MyTreeItem> results = db[dateiIndex].queryByExample(myTreeItem);
				  listResult(results);
				  db[dateiIndex].store(myTreeItem);
				  db[dateiIndex].commit(); //speichern
			  } catch (DatabaseClosedException | DatabaseReadOnlyException | Db4oIOException e) {
				// TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  	System.out.println("Datensatz gespeichert");		
		}
	}    
	  
	public static void deleteMyTreeItem(MyTreeItem myTreeItem) {
		System.out.println("DeleteMyTreeItem" + myTreeItem.getAnzeigeText());
//		  dateiIndex = myTreeItem.getDbIndex();	 Fehlerhaft wenn Knoten aus anderer Datenbank kopiert wurde  
		  MyTreeItem obersterKnotenMyTreeItem = TreeTools.searchForObersterknoten(myTreeItem);
		  dateiIndex = obersterKnotenMyTreeItem.getDbIndex();
		  
		  Query query=db[dateiIndex].query();							//Abfrage definieren
		  query.constrain(MyTreeItem.class);							//suche alle Datensätze
		  query.descend("uuid").constrain(myTreeItem.getUuid());		//nach Knoten suchen
		  ObjectSet<MyTreeItem> results=query.execute();

		  db[dateiIndex].delete(results);	//löschen
		  db[dateiIndex].commit();			//speichern
		
	}
	
	
	/**
	 * Gesucht wird mit der UUID
	 * @author Burkhard Pöhler
	 * @param myTreeItem ausgewählter Knoten
	 * @return item der gefundene Knoten
	 */
	public static MyTreeItem seachMyTreeItem(MyTreeItem myTreeItem) {
		System.out.println("SeachMyTreeItem" + myTreeItem.getAnzeigeText());
//		  dateiIndex = myTreeItem.getDbIndex();	 Fehlerhaft wenn Knoten aus anderer Datenbank kopiert wurde  
		  MyTreeItem obersterKnotenMyTreeItem = TreeTools.searchForObersterknoten(myTreeItem);
		  dateiIndex = obersterKnotenMyTreeItem.getDbIndex();
		  
		  Query query=db[dateiIndex].query();							//Abfrage definieren
		  query.constrain(MyTreeItem.class);							//suche alle Datensätze
		  query.descend("uuid").constrain(myTreeItem.getUuid());		//nach Knoten suchen
		  ObjectSet<MyTreeItem> results=query.execute();
		  MyTreeItem item=(MyTreeItem) results.get(0);
		  listResult(results);
		  return item;
	}
	
	/**
	 * Der ausgewählte Knoten wird mit Hilfe der UUID gesucht und dann komplett in einer temporären Datenbank abgelegt
	 * @author Burkhard Pöhler
	 * @param	myTreeItem ausgewählter Knoten
	 */
	public static void copyMyTreeItem(MyTreeItem myTreeItem) {

//		  dateiIndex = myTreeItem.getDbIndex();	 Fehlerhaft wenn Knoten aus anderer Datenbank kopiert wurde  
		  MyTreeItem obersterKnotenMyTreeItem = TreeTools.searchForObersterknoten(myTreeItem);
		  dateiIndex = obersterKnotenMyTreeItem.getDbIndex();
		  
		  MyTreeItem myParentTreeItem = myTreeItem.getParent();		//Vaterknoten zwischenspeichern
		  myTreeItem.setParent(null);								//Den Verweis auf die übergeordneten Knoten entfernen

		  
//		Query query=db[dateiIndex].query();							//Abfrage definieren
//		query.constrain(MyTreeItem.class);							//suche alle Datensätze
//		query.descend("uuid").constrain(myTreeItem.getUuid());		//nach Knoten suchen
//		ObjectSet<MyTreeItem> results=query.execute();
//		MyTreeItem item=(MyTreeItem) results.get(0);
//		System.out.println("copyMyTreeItem Temporär gespeichert :");
//		listResult(results);
			
		  File file = new File("temp");
		  if (file.exists()) file.delete();
			
			ObjectContainer dbtemp = null;
			
			EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();	
			conf.common().objectClass(MyTreeItem.class).cascadeOnUpdate(true);
			conf.common().objectClass(MyTreeItem.class).minimumActivationDepth(50);
			conf.common().objectClass(MyTreeItem.class).cascadeOnDelete(true);
			conf.common().objectClass(MyTreeItem.class).updateDepth(50);
			conf.common().objectClass(MyTreeItem.class).objectField("name").indexed(true);
			
	        try {
	    		System.out.println("Temp Datenbank öffnen");
	    		dbtemp = Db4oEmbedded.openFile(conf, "temp");

			} catch (Db4oIOException e) {
				e.printStackTrace();
			} catch (DatabaseFileLockedException e) {
				e.printStackTrace();
			} catch (IncompatibleFileFormatException e) {
				e.printStackTrace();
			} catch (OldFormatException e) {
				e.printStackTrace();
			} catch (DatabaseReadOnlyException e) {
				e.printStackTrace();
			} catch (UniqueFieldValueConstraintViolationException  e) {
				e.printStackTrace();
			}
	        
	
	        myTreeItem.setUuid();
	        dbtemp.store(myTreeItem);
	        dbtemp.commit(); //speichern
	        dbtemp.close();
	        
			myTreeItem.setParent(myParentTreeItem); 			//den übergeordneten Knoten wieder setzen
		
	}

	/**
	 * Der ausgewählte Knoten wird komplett in einer temporären Datenbank abgelegt
	 * @author Burkhard Pöhler
	 * @param	myTreeItem ausgewählter Knoten
	 */
	public static void storeMyTreeItem(MyTreeItem myTreeItem) {
		
		  File file = new File("temp");
		  if (file.exists()) file.delete();
			
			ObjectContainer dbtemp = null;
			
			EmbeddedConfiguration conf = Db4oEmbedded.newConfiguration();	
			conf.common().objectClass(MyTreeItem.class).cascadeOnUpdate(true);
			conf.common().objectClass(MyTreeItem.class).minimumActivationDepth(50);
			conf.common().objectClass(MyTreeItem.class).cascadeOnDelete(true);
			conf.common().objectClass(MyTreeItem.class).updateDepth(50);
			conf.common().objectClass(MyTreeItem.class).objectField("name").indexed(true);
			
	        try {
	    		System.out.println("Temp Datenbank öffnen");
	    		dbtemp = Db4oEmbedded.openFile(conf, "temp");

			} catch (Db4oIOException e) {
				e.printStackTrace();
			} catch (DatabaseFileLockedException e) {
				e.printStackTrace();
			} catch (IncompatibleFileFormatException e) {
				e.printStackTrace();
			} catch (OldFormatException e) {
				e.printStackTrace();
			} catch (DatabaseReadOnlyException e) {
				e.printStackTrace();
			} catch (UniqueFieldValueConstraintViolationException  e) {
				e.printStackTrace();
			}

	        myTreeItem.setUuid();
	        dbtemp.store(myTreeItem);
	        dbtemp.commit(); //speichern

	        
			Query query=dbtemp.query();							//Abfrage definieren
			query.constrain(MyTreeItem.class);							//suche alle Datensätze
//			query.descend("uuid").constrain(myTreeItem.getUuid());		//nach Knoten suchen
			ObjectSet<MyTreeItem> results=query.execute();
	   		System.out.println("In temporärer Datenbank gefunden:");
	        dbtemp.close();
		
	}
}
