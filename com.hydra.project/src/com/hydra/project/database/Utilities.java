package com.hydra.project.database;

import java.io.File;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.hydra.project.model.MyTableCustomization;
import com.hydra.project.model.MyTreeItem;


public class Utilities {
    public final static String DB4OFILENAME="C:\\MeinBaum.DB4O";
    public final static String DBCUTANDPASTE="C:\\MeinCutAndPaste.DB4O";
    public final static String DBPARAMETER="C:\\Parameter.DB4O";
    public final static String DBPROPERTIES="C:\\Eigenschaften.DB4O";
    public final static String DBTEMPLATES="C:\\Vorlagen.DB4O";
  
    public static void listResult(ObjectSet result) {
    	for(int x = 0; x < result.size(); x++){
			MyTreeItem test=(MyTreeItem) result.get(x);
			String text1 = test.getUuid();
			int text3 = test.getIndex();
			String text4;
			try {
				text4 = String.valueOf(test.getChildren().size());
			} catch (Exception e) {
				text4="0";
			}
			String text5 = "Name "+ text1 + (" Index :" + text3 + " Kinder: " + text4  );
			System.out.println(text5);
		}
        System.out.println(result.size());
    }
    
    public static void listResultCusto(ObjectSet result) {
    	for(int x = 0; x < result.size(); x++){
    		MyTableCustomization test=(MyTableCustomization) result.get(x);
			String text1 = test.getDirectory();
			String text2 = test.getFilter();
			String text3 = test.getId();
			String text4 = test.getXml();
			String text5 = "Tabelleneinstellung "+ text1 + "  " + text2 + "  " + text3 + "  " + text4;
			System.out.println(text5);
		}
        System.out.println(result.size());
    }
    
    public static void listResult(java.util.List result){
        System.out.println("Ausgabe von listResult"); 
    	System.out.println(result.size());
		for(int x = 0; x < result.size(); x++){
			MyTreeItem test=(MyTreeItem) result.get(x);
			String text1 = test.getUuid();

			int text3 = test.getIndex();
			String text4;
			try {
				text4 = String.valueOf(test.getChildren().size());
			} catch (Exception e) {
				text4="0";
			}
			String text5 = "Name "+text1 + (" Index :" + text3 + " Kinder: " + text4 );
			System.out.println(text5);
		}
    }
    
 
    
    public static void listRefreshedResult(ObjectContainer container,ObjectSet result,int depth) {
        System.out.println(result.size());
        while(result.hasNext()) {
            Object obj = result.next();
            container.ext().refresh(obj, depth);

            System.out.println(obj);
        }
    }
    

    public static void retrieveAll(ObjectContainer db){
        ObjectSet result=db.queryByExample(new Object());
        listResult(result);
    }
    
    public static void deleteDBCUTANDPASTE(ObjectContainer db) {
    	new File(DBCUTANDPASTE).delete();
    	accessDB4O();
    	new File(DBCUTANDPASTE).delete();
    	
//    	db.store(new Object());				//Dummy speichern für den Fall das DB nicht leer
//        ObjectSet result=db.queryByExample(new Object());
//        while(result.hasNext()) {
//        	 db.delete(result.next());
//        }
       
    }

	private static void accessDB4O() {
		ObjectContainer db=Db4oEmbedded.openFile(DBCUTANDPASTE);
		try{
			//do something
			}
		finally {
			db.close();
		}
		
	}
}
