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
package com.hydra.project.myplugin_nebula.xviewer.customize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hydra.project.myplugin_nebula.xviewer.Activator;
import com.hydra.project.myplugin_nebula.xviewer.XViewerColumn;
import com.hydra.project.myplugin_nebula.xviewer.util.internal.XViewerLib;
import com.hydra.project.myplugin_nebula.xviewer.util.internal.XViewerLog;
import com.hydra.project.myplugin_nebula.xviewer.util.internal.XmlUtil;

/**
 * Provides object for storage of sorting data
 * 
 * @author Donald G. Dunne
 */
public class SortingData {

   private final static String XTREESORTER_TAG = "xSorter"; //$NON-NLS-1$
   private final static String COL_NAME_TAG = "id"; //$NON-NLS-1$
   private final static String OLD_COL_NAME_TAG = "name"; //$NON-NLS-1$
   private final List<String> sortingIds = new ArrayList<String>();
//   protected List<String> sortingIds = new ArrayList<String>();
   private final CustomizeData custData;

   public SortingData(CustomizeData custData) {
      this.custData = custData;
   }

   public SortingData(String xml) {
      this.custData = null;
      setFromXml(xml);
   }

   public void clearSorter() {
      sortingIds.clear();
   }

   public boolean isSorting() {
      return sortingIds.size() > 0;
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      for (String str : sortingIds) {
         sb.append(str);
         sb.append(", "); //$NON-NLS-1$
      }
      return sb.toString().replaceFirst(", $", ""); //$NON-NLS-1$ //$NON-NLS-2$
   }

   public List<XViewerColumn> getSortXCols(Map<String, XViewerColumn> oldNameToColumnId) {
      List<XViewerColumn> cols = new ArrayList<XViewerColumn>();
      for (String id : getSortingIds()) {
         XViewerColumn xCol = custData.getColumnData().getXColumn(id);
         // For backward compatibility, try to resolve column name
         if (xCol == null) {
            XViewerColumn resolvedCol = oldNameToColumnId.get(id);
            if (resolvedCol != null) {
               xCol = custData.getColumnData().getXColumn(resolvedCol.getId());
            }
         }
         if (xCol != null) {
            cols.add(xCol);
         } else {
            // Ignore known removed columns
            if (!CustomizeManager.REMOVED_COLUMNS_TO_IGNORE.contains(id)) {
//               XViewerLog.log(
//                  Activator.class,
//                  Level.WARNING,
//                  "XViewer Conversion for saved Customization \"" + custData.getName() + "\" dropped unresolved SORTING column Name/Id: \"" + id + "\".  Delete customization and re-save to resolve.");
            }
         }
      }
      return cols;
   }

   public void setSortXCols(List<XViewerColumn> sortXCols) {
      sortingIds.clear();
      for (XViewerColumn xCol : sortXCols) {
         sortingIds.add(XViewerLib.intern(xCol.getId()));
      }
   }

   public String getXml() {
      StringBuffer sb = new StringBuffer("<" + XTREESORTER_TAG + ">"); //$NON-NLS-1$ //$NON-NLS-2$
      // NOTE: Sorting direction is stored as part of the column data
      for (String item : sortingIds) {
         sb.append(XmlUtil.addTagData(COL_NAME_TAG, item));
      }
      sb.append("</" + XTREESORTER_TAG + ">"); //$NON-NLS-1$ //$NON-NLS-2$
      return sb.toString();
   }

   private static Pattern pattern1 = Pattern.compile("<" + COL_NAME_TAG + ">(.*?)</" + COL_NAME_TAG + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   private static Pattern pattern2 = Pattern.compile("<" + OLD_COL_NAME_TAG + ">(.*?)</" + OLD_COL_NAME_TAG + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

   public void setFromXml(String xml) {
      // NOTE: Sorting direction is stored as part of the column data
      sortingIds.clear();
      String xmlSortStr = XmlUtil.getTagData(xml, XTREESORTER_TAG);
      Matcher m = pattern1.matcher(xmlSortStr);
      while (m.find()) {
         sortingIds.add(m.group(1));
      }
      Matcher mOld = pattern2.matcher(xmlSortStr);
      while (mOld.find()) {
         sortingIds.add(mOld.group(1));
      }
   }

   public List<String> getSortingIds() {
      return sortingIds;
   }

   public void removeSortingName(String name) {
      this.sortingIds.remove(name);
   }

   public void addSortingName(String name) {
      if (!this.sortingIds.contains(name)) {
         this.sortingIds.add(XViewerLib.intern(name));
      }
   }

   public void setSortingNames(String... xViewerColumnId) {
      this.sortingIds.clear();
      for (String id : xViewerColumnId) {
         this.sortingIds.add(XViewerLib.intern(id));
      }
   }

}
