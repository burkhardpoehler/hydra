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
package com.hydra.project.myplugin_nebula.xviewer.column;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import com.hydra.project.myplugin_nebula.xviewer.Activator;
import com.hydra.project.myplugin_nebula.xviewer.IXViewerLabelProvider;
import com.hydra.project.myplugin_nebula.xviewer.XViewerColumn;
import com.hydra.project.myplugin_nebula.xviewer.XViewerComputedColumn;
import com.hydra.project.myplugin_nebula.xviewer.XViewerSorter;
import com.hydra.project.myplugin_nebula.xviewer.XViewerText;
import com.hydra.project.myplugin_nebula.xviewer.util.internal.XViewerLib;
import com.hydra.project.myplugin_nebula.xviewer.util.internal.XViewerLog;
import org.eclipse.swt.SWT;

/**
 * @author Donald G. Dunne
 */
public class XViewerDaysTillTodayColumn extends XViewerComputedColumn {

   private final static String ID = "ats.computed.daysTillToday"; //$NON-NLS-1$

   public XViewerDaysTillTodayColumn() {
      this(ID);
   }

   private XViewerDaysTillTodayColumn(String id) {
      super(id, XViewerText.get("column.daysTillToday.name"), 30, SWT.LEFT, false, SortDataType.Integer, false, //$NON-NLS-1$
         XViewerText.get("column.daysTillToday.description")); //$NON-NLS-1$
   }

   @Override
   public String getColumnText(Object element, XViewerColumn column, int columnIndex) {
      if (sourceXViewerColumn == null) {
         return String.format(XViewerText.get("error.no_source_column"), id); //$NON-NLS-1$
      }
      try {
         int sourceColumnNum = xViewer.getCustomizeMgr().getColumnNumFromXViewerColumn(sourceXViewerColumn);
         String dateStr = ((IXViewerLabelProvider) xViewer.getLabelProvider()).getColumnText(element, sourceColumnNum);
         if (dateStr == null || dateStr.equals("")) { //$NON-NLS-1$
            return ""; //$NON-NLS-1$
         }
         DateFormat format;
         if (dateStr.length() == 10) {
            format = XViewerSorter.format10;
         } else {
            format = new SimpleDateFormat();
         }
         Date date1Date = null;
         try {
            date1Date = format.parse(dateStr);
         } catch (ParseException ex) {
            try {
               date1Date = DateFormat.getInstance().parse(dateStr);
            } catch (ParseException ex2) {
               XViewerLog.log(Activator.class, Level.SEVERE, ex2);
            }
         }
         if (date1Date == null) {
            return "Can't parse date"; //$NON-NLS-1$
         }
         return String.valueOf(XViewerLib.daysTillToday(date1Date));
      } catch (Exception ex) {
         return ex.getLocalizedMessage();
      }
   }

   @Override
   public boolean isApplicableFor(XViewerColumn xViewerColumn) {
      return xViewerColumn.getSortDataType() == SortDataType.Date;
   }

   @Override
   public String getName() {
      if (sourceXViewerColumn == null) {
         return XViewerText.get("column.daysTillToday.name"); //$NON-NLS-1$
      }
      return XViewerText.get("column.daysTillToday.name2") + " " + sourceXViewerColumn.getName(); //$NON-NLS-1$//$NON-NLS-2$
   }

   @Override
   public XViewerDaysTillTodayColumn copy() {
      XViewerDaysTillTodayColumn col = new XViewerDaysTillTodayColumn();
      col.setXViewer(getXViewer());
      col.setSourceXViewerColumn(getSourceXViewerColumn());
      return col;
   }

   @Override
   public String getId() {
      if (sourceXViewerColumn == null) {
         return ID;
      }
      return ID + "(" + sourceXViewerColumn.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
   }

   @Override
   public boolean isApplicableFor(String storedId) {
      return storedId.startsWith(ID);
   }

   @Override
   public XViewerComputedColumn createFromStored(XViewerColumn storedColumn) {
      return new XViewerDaysTillTodayColumn(storedColumn.getId());
   }
}
