/*******************************************************************************
 * Copyright (c) 2004, 2007 Boeing.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boeing - initial API and implementation
 *******************************************************************************/
package com.hydra.projects.XViewer.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.hydra.project.database.DBCompanyTools;
import com.hydra.project.model.MyTableCustomization;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.myplugin_nebula.xviewer.Activator;
import com.hydra.project.myplugin_nebula.xviewer.customize.CustomizeData;
import com.hydra.project.myplugin_nebula.xviewer.customize.XViewerCustomizations;
import com.hydra.project.myplugin_nebula.xviewer.util.XViewerException;
import com.hydra.project.parts.TableView;
import com.hydra.projects.XViewer.Main.util.FileUtil;
import com.hydra.projects.XViewer.Main.util.MatchFilter;
import com.hydra.projects.XViewer.Main.util.MyLib;
import com.hydra.projects.XViewer.Main.util.MyLog;

/**
 * Implementation for example XViewer implementation. Saves customizations as files at C:/UserData
 * 
 * @author Donald G. Dunne
 */
public class MyXViewerCustomizations extends XViewerCustomizations {
	
	private String user_home = System.getProperty("user.home");
	private String USERDEFAULT = "CustDataUserDefault";
	
	/**
	 * Erzeugt einen eindeutigen Namen für die Tabelle
	 * 
	 * @author B.Pöhler
	 */
	private void getTableItem(){
		user_home = System.getProperty("user.home") + "_" + TableView.getMyTableTreeItem().getVariablenWert();
	}

   @Override
   public void deleteCustomization(CustomizeData custData) throws Exception {
	   getTableItem();
	   boolean flag = false;
	   flag = DBCompanyTools.deleteTableConfigInDB(user_home, "XViewer_CustData_" + custData.getGuid() + ".xml");
	   if (!flag) {
		   throw new XViewerException("Delete Customization Failed");
	   }
//	   File file = new File(getFilename(custData));
//      if (file.exists()) {
//         boolean success = file.delete();
//         if (!success) {
//            throw new XViewerException("Delete Customization Failed");
//         }
//      }
   }

   @Override
   public List<CustomizeData> getSavedCustDatas() throws XViewerException {
	   getTableItem();
      List<CustomizeData> custDatas = new ArrayList<CustomizeData>();
//      custDatas.add(MyDefaultCustomizations.getCompletionCustomization());
//      custDatas.add(MyDefaultCustomizations.getDescriptionCustomization());
//      String userHome = System.getProperty("user.home");
      List<MyTableCustomization> myTableCustomization = new ArrayList<MyTableCustomization>();
      myTableCustomization = DBCompanyTools.readDB(user_home, "");
   
      for (int n = 0; n < myTableCustomization.size(); n++) {
//    	  String dateipfad = myTableCustomization.get(n).getDirectory() + myTableCustomization.get(n).getFilter();
//    	  String match = userHome + "/" + filename;
//    	  if (dateipfad == match){
    	  
    	  //die Standardeinstellung muss rausgefiltert werden
    	  if (!myTableCustomization.get(n).getFilter().contains(USERDEFAULT)){
    		  if (myTableCustomization.get(n).getXml()!=null){
    			  custDatas.add(new CustomizeData(myTableCustomization.get(n).getXml()));
    		  }
    	  }

//    	  }	  
      }
      
      
//      for (String filename : MyLib.readListFromDir(new File(userHome), new MatchFilter("XViewer_CustData_.*\\.xml"),true)) {
//         custDatas.add(new CustomizeData(FileUtil.readFile(userHome + "/" + filename)));
//      }
      return custDatas;
   }

   @Override
   public CustomizeData getUserDefaultCustData() throws XViewerException {
	   getTableItem();
//      File file = new File(getDefaultFilename());
//      if (!file.exists()) {
//         return null;
//      }
//      
      String defaultGuid = DBCompanyTools.readDefaultFileFromDB(user_home,USERDEFAULT);
      
//      String defaultGuid = FileUtil.readFile(file).replaceAll("\\s", "");
      if (defaultGuid != null) {
         for (CustomizeData custData : getSavedCustDatas()) {
            if (custData.getGuid().equals(defaultGuid)) {
               return custData;
            }
         }
      }
      return null;
   }

   @Override
   public boolean isCustomizationPersistAvailable() {
      return true;
   }

   @Override
   public boolean isCustomizationUserDefault(CustomizeData custData) throws XViewerException {
	   getTableItem();
//      File file = new File(getDefaultFilename());
//      if (!file.exists()) {
//         return false;
//      }
//      
      String defaultGuid = DBCompanyTools.readDefaultFileFromDB(user_home,USERDEFAULT);
      
//      String defaultGuid = FileUtil.readFile(getDefaultFilename()).replaceAll("\\s", "");
      if (defaultGuid == null){
    	  return false;
      }
      return custData.getGuid().equals(defaultGuid);
   }

   @Override
   public void saveCustomization(CustomizeData custData) throws Exception {
	   getTableItem();
	   MyTableCustomization myTableCustomization = 
			   new MyTableCustomization(user_home,"XViewer_CustData_" + custData.getGuid() + ".xml",custData.getXml(true),custData.getGuid());   
	   DBCompanyTools.writeFileToDB(myTableCustomization);
	   
//      MyLib.writeStringToFile(custData.getXml(true), new File(getFilename(custData)));
//      Thread.sleep(2000);
   }

//   private String getFilename(CustomizeData custData) {
//      String userHome = System.getProperty("user.home");
//      return userHome + "/" + "XViewer_CustData_" + custData.getGuid() + ".xml";
//   }
//
//   private String getDefaultFilename() {
//      String userHome = System.getProperty("user.home");
//      return userHome + "/" + "XViewer_CustDataUserDefault.txt";
//   }

   @Override
   public void setUserDefaultCustData(CustomizeData newCustData, boolean set) throws XViewerException {
	   getTableItem();
      if (set) {
//         try {
//    	  String userHome = System.getProperty("user.home");
//    	  String filename = "XViewer_CustDataUserDefault.txt";
    	  DBCompanyTools.deleteTableConfigInDB(user_home,USERDEFAULT);		//alte Einstellung löschen
    	  MyTableCustomization myTableCustomization = new MyTableCustomization(user_home,USERDEFAULT,"",newCustData.getGuid());   
      	  DBCompanyTools.writeFileToDB(myTableCustomization);
//            MyLib.writeStringToFile(newCustData.getGuid(), new File(getDefaultFilename()));
//         } catch (IOException ex) {
//            MyLog.logAndPopup(Activator.class, Level.SEVERE, ex);
//         }
      } else {
    	  	boolean flag = false;
   	   		flag = DBCompanyTools.deleteTableConfigInDB(user_home, USERDEFAULT);
   	   		if (!flag) {
   	   			throw new XViewerException("Delete Customization Failed");
   	   		}
    	  
//         File file = new File(getDefaultFilename());
//         if (file.exists()) {
//            boolean success = file.delete();
//            if (!success) {
//               throw new XViewerException("Delete Customization Failed");
//            }
//         }
      }
   }

}
