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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.Dialog;
import com.hydra.project.myplugin_nebula.xviewer.XViewer;
import com.hydra.project.myplugin_nebula.xviewer.XViewerColumn.SortDataType;
import com.hydra.project.myplugin_nebula.xviewer.XViewerFactory;
import com.hydra.project.myplugin_nebula.xviewer.customize.IXViewerCustomizations;
import com.hydra.project.myplugin_nebula.xviewer.customize.dialog.XViewerCustomizeDialog;
import com.hydra.project.myplugin_nebula.xviewer.edit.CellEditDescriptor;
import com.hydra.project.myplugin_nebula.xviewer.edit.ExtendedViewerColumn;
import com.hydra.projects.XViewer.Main.model.MyTreeTask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

/**
 * Columns for example XViewer
 *
 * @author Donald G. Dunne
 */
public class MyXViewerFactory extends XViewerFactory {

   public static String COLUMN_NAMESPACE = "XViewer";
//   public static XViewerColumn Spalte[];
   public static List<ExtendedViewerColumn> xViewerColumnList = new ArrayList<ExtendedViewerColumn>();
   private static MyTreeItem myTreeItem;
 
   /**
    * Columns for example XViewer
    * @param item Das erste gefundene Item
    * @param myTableTreeItem Das Tabellenitem (speichert die Konfiguration)
    * @author Pöhler
    */
   public MyXViewerFactory(MyTreeItem myTableTreeItem, MyTreeItem item) {
	      super(COLUMN_NAMESPACE);
//	      COLUMN_NAMESPACE=myTableTreeItem.getVariablenWert();
	      myTreeItem = item;
	      prepareColumns();
   }
   
   public void prepareColumns(){
	   xViewerColumnList.clear();
	      List<MyTreeItem> list = new  ArrayList<MyTreeItem>();
	      list = TreeTools.prepareNodeList(myTreeItem, list);
		   for (int n = 0; n < list.size(); n++) {
			   ExtendedViewerColumn xViewerColumn = new ExtendedViewerColumn("SpaltenId." + n, list.get(n).getBezeichnung(), 
					   200, SWT.LEFT, true, SortDataType.String, false, null );
			   
//			   XViewerColumn xViewerColumn = new XViewerColumn("SpaltenId." + n, list.get(n).getBezeichnung(), 
//					   200, SWT.LEFT, true, SortDataType.String, true, null );
			   
			   
			   xViewerColumn.addMapEntry(MyTreeTask.class, new CellEditDescriptor(Text.class, SWT.BORDER, "SpaltenId." + n, MyTreeItem.class));
			   xViewerColumnList.add(xViewerColumn);
		   }
	      registerColumns(xViewerColumnList);
   }
   
   @Override
   public IXViewerCustomizations getXViewerCustomizations() {
      return new MyXViewerCustomizations();
   }

   @Override
   public boolean isAdmin() {
      return true;
   }

   @Override
   public boolean isCellGradientOn() {
      return true;
   }

   @Override
   public Dialog getCustomizeDialog(XViewer xViewer) {
      return new XViewerCustomizeDialog(xViewer);
   }

   public static void setMyTreeItem(MyTreeItem item) {
	   myTreeItem = item;
   }
}
