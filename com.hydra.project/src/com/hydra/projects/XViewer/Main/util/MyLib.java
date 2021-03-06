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
package com.hydra.projects.XViewer.Main.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.eclipse.jface.dialogs.MessageDialog;

import com.hydra.project.database.DBCompanySettings;
import com.hydra.project.model.MyTableCustomization;
import com.hydra.project.myplugin_nebula.xviewer.Activator;
import com.hydra.project.parts.LogfileView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.PlatformUI;

/**
 * @author Donald G. Dunne
 */
public class MyLib {
	private static String thisClass= "MyLib";
   static Random random = new Random();
   private static final Date today = new Date();
   public final static int MILLISECS_PER_DAY = (1000 * 60 * 60 * 24);

   public static long daysTillToday(Date date) {
      return (date.getTime() - today.getTime()) / MILLISECS_PER_DAY;
   }

   public static String generateGuidStr() {
      long rand = (random.nextLong() & 0x7FFFFFFFFFFFFFFFL) | 0x4000000000000000L;
      return Long.toString(rand, 32) + Long.toString(System.currentTimeMillis() & 0xFFFFFFFFFFFFFL, 32);
   }

   public static void writeStringToFile(String str, File outFile) throws IOException {
      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
      char[] chars = str.toCharArray();
      out.write(chars, 0, chars.length);
      out.close();
   }

   public static void popup(final String title, final String message) {
//      if (!PlatformUI.isWorkbenchRunning()) {
//         MyLog.log(Activator.class, Level.SEVERE, message);
//      } else {
//         ensureInDisplayThread(new Runnable() {
//            @Override
//            public void run() {
//               MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title,
//                  message);
//            }
//         });
//      }
   }

   public static GridLayout getZeroMarginLayout(int numColumns, boolean equalColumnWidth) {
      GridLayout layout = new GridLayout();
      layout.numColumns = numColumns;
      layout.makeColumnsEqualWidth = equalColumnWidth;
      layout.verticalSpacing = 0;
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      return layout;
   }

   public static GridLayout getZeroMarginLayout() {
      return getZeroMarginLayout(1, false);
   }

   public static List<String> readListFromDir(File directory, FilenameFilter filter, boolean keepExtension) {
      List<String> list = new ArrayList<String>(400);

      if (directory == null) {
         MyLog.log(Activator.class, Level.SEVERE, "Invalid directory path");
         return list;
      }

      File[] files = directory.listFiles(filter);
      if (files == null) {
         MyLog.log(Activator.class, Level.SEVERE, "Invalid path: " + directory);
         return list;
      }
      if (files.length > 0) {
         Arrays.sort(files);
      }

      if (keepExtension) {
         for (int i = 0; i < files.length; i++) {
            list.add(files[i].getName());
         }
      } else {
         for (int i = 0; i < files.length; i++) {
            list.add(removeExtension(files[i].getName()));
         }
      }

      return list;
   }

   /**
	 * Liest aus der Company Datenbank eine virtuelle Dateipfadliste.
	 * @param directory Der virtuelle Verzeichnispfad.
	 * @param filter Der Dateifilter
	 * @param keepExtension Legt fest, ob die Dateiextension beibehalten werden soll
	 * @return list Die Liste mit den virtuellen Dateipfaden.
	 */
   public static List<MyTableCustomization> readListFromDB(String directory, String filter, boolean keepExtension) {
	   List<MyTableCustomization> myTableCustomization = new ArrayList<MyTableCustomization>();
	   LogfileView.log(thisClass,"Tabelleneinstellung: Dateipfad: " + directory.toString(),SWT.ICON_INFORMATION);
	   LogfileView.log(thisClass,"Tabelleneinstellung: Filter: " + filter,SWT.ICON_INFORMATION);
	   LogfileView.log(thisClass,"Tabelleneinstellung: Endung: " + keepExtension,SWT.ICON_INFORMATION);
	   myTableCustomization = DBCompanySettings.readTableDB(directory,  filter);
	   if (myTableCustomization != null) {
		     if (keepExtension) {
		    	 tabelleAnzeigen(myTableCustomization);
		    	 return myTableCustomization;
		      } else {
		         for (int i = 0; i < myTableCustomization.size(); i++) {
		        	 myTableCustomization.get(i).setFilter(removeExtension(myTableCustomization.get(i).getFilter()));
		         }
		      }
	   }
	   tabelleAnzeigen(myTableCustomization);
	   return myTableCustomization;
   }
   
   private static void tabelleAnzeigen(List<MyTableCustomization> myTableCustomization){
       for (int i = 0; i < myTableCustomization.size(); i++) {
      	 myTableCustomization.get(i);
      	 LogfileView.log(thisClass,"Nr: " + i +  myTableCustomization.get(i).getDirectory() + " " +
      			 myTableCustomization.get(i).getFilter() + " " +
      			 myTableCustomization.get(i).getId(),SWT.ICON_INFORMATION);
       }
   }
   
   public static List<String> readListFromDir(String directory, FilenameFilter filter) {
      return readListFromDir(new File(directory), filter, false);
   }

   
//   /**
// 	 * Liest aus der Company Datenbank eine virtuelle Dateipfadliste.
// 	 * @param directory Der virtuelle Verzeichnispfad.
// 	 * @param filter Der Dateifilter
// 	 * @return list Die Liste mit den virtuellen Dateipfaden.
// 	 */
//   public static List<MyTableCustomization> readListFromDB(String directory, String filter) {
//	   List<MyTableCustomization> myTableCustomization = new ArrayList<MyTableCustomization>();
//	   myTableCustomization = DBCompanySettings.readDB(directory,  filter);
//	   return myTableCustomization;
//   }
   
   /**
    * Remove the file extension from the file path
    */
   public static String removeExtension(String filepath) {
      String ext = getExtension(filepath);
      if (ext != null && ext.length() > 0) {
         filepath = filepath.substring(0, filepath.length() - (ext.length() + 1));
      }
      return filepath;
   }

   /**
    * Determine is OS is windows
    */
   public static boolean isWindows() {
      return System.getProperty("os.name").indexOf("indows") != -1;
   }

   /**
    * Get file extension from the file path
    */
   public static String getExtension(String filepath) {
      filepath = filepath.trim();
      String separatorRegEx = File.separator;
      if (isWindows()) {
         separatorRegEx = "\\\\";
      }
      String[] pathsArray = filepath.split(separatorRegEx);

      String fileName = pathsArray[0];
      if (pathsArray.length > 0) {
         fileName = pathsArray[pathsArray.length - 1];
      }

      int index = fileName.lastIndexOf('.');
      if (index >= 0 && index + 1 < fileName.length()) {
         return fileName.substring(index + 1);
      } else {
         return "";
      }
   }

   public static void ensureInDisplayThread(Runnable runnable) {
      ensureInDisplayThread(runnable, false);
   }

   public static void ensureInDisplayThread(Runnable runnable, boolean forcePend) {
      if (isDisplayThread()) {
         // No need to check for force since this will always pend
         runnable.run();
      } else {
         if (forcePend) {
            Display.getDefault().syncExec(runnable);
         } else {
            Display.getDefault().asyncExec(runnable);
         }
      }
   }

   public static boolean isDisplayThread() {
      if (Display.getCurrent() == null) {
         return false;
      }

      return Display.getCurrent().getThread() == Thread.currentThread();
   }

   public static String doubleToI18nString(double d) {
      return doubleToI18nString(d, false);
   }

   public static String doubleToI18nString(double d, boolean blankIfZero) {
      if (blankIfZero && d == 0) {
         return "";
      } else {
         return String.format("%4.2f", d);
      }
   }
}
