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

import org.eclipse.jface.viewers.ILabelProviderListener;
import com.hydra.project.myplugin_nebula.xviewer.XViewerColumn;
import com.hydra.project.myplugin_nebula.xviewer.XViewerLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask;


/**
 * Example implementation for XViewerTest XViewer
 * 
 * @author Donald G. Dunne
 */
public class MyXViewerLabelProvider extends XViewerLabelProvider {
   private final MyXViewer xViewerTest;

   public MyXViewerLabelProvider(MyXViewer xViewerTest) {
      super(xViewerTest);
      this.xViewerTest = xViewerTest;
   }

   @Override
   public String getColumnText(Object element, XViewerColumn xCol, int columnIndex) {
      if (element instanceof String) {
         if (columnIndex == 1) {
            return (String) element;
         } else {
            return "";
         }
      }
      IMyTreeTask task = ((IMyTreeTask) element);
      if (task == null) {
         return "";
      }
 
      int spaltenanzahl = task.getMyList().size();
      
      for (int n = 0; n < spaltenanzahl; n++) {
    	     if (xCol.equals(MyXViewerFactory.xViewerColumnList.get(n))) {
    	          return task.getMyList().get(n).getVariablenWert();
    	     }
      }
      return "unhandled column";
   }

   @Override
   public void dispose() {
      // do nothing
   }

   @Override
   public boolean isLabelProperty(Object element, String property) {
      return false;
   }

   @Override
   public void addListener(ILabelProviderListener listener) {
      // do nothing
   }

   @Override
   public void removeListener(ILabelProviderListener listener) {
      // do nothing
   }

   @Override
   public Image getColumnImage(Object element, XViewerColumn xCol, int columnIndex) {
//      if (xCol.equals(MyXViewerFactory.Run_Col)) {
//         return xViewerTest.isRun((IMyTask) element) ? MyImageCache.getImage("chkbox_enabled.gif") : MyImageCache.getImage("chkbox_disabled.gif");
//      }
//      if (xCol.equals(MyXViewerFactory.Name_Col) && xViewerTest.isScheduled((IMyTask) element)) {
//         return MyImageCache.getImage("clock.gif");
//      }
      return null;
   }

   @Override
   public Color getBackground(Object element, int columnIndex) {
      return super.getBackground(element, columnIndex);
   }

   @Override
   public int getColumnGradient(Object element, XViewerColumn xCol, int columnIndex) throws Exception {
      if (!(element instanceof IMyTreeTask)) {
         return 0;
      }
      IMyTreeTask task = ((IMyTreeTask) element);
//      if (xCol.equals(MyXViewerFactory.Completed_Col)) {
//         return task.getPercentComplete();
//      }
      return 0;
   }

}
