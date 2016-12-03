/*******************************************************************************
 * Copyright (c) 2004, 2007 Boeing.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boeing - initial API and implementation
 *     Wim jongman <wim.jongman@remainsoftwarte.com> - bug 368889
 *******************************************************************************/
package com.hydra.projects.XViewer.Main;

import java.util.HashSet;
import java.util.Set;
import com.hydra.project.myplugin_nebula.xviewer.XViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import com.hydra.project.model.MyTreeItem;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask;

/**
 * Example extension of XViewer.
 * 
 * @author Donald G. Dunne
 */
public class MyXViewer extends XViewer {
   private final Set<IMyTreeTask> runList = new HashSet<IMyTreeTask>();

   public MyXViewer(Tree tree, MyTreeItem myTableTreeItem, MyTreeItem mySeachTreeItem) {
      super(tree, new MyXViewerFactory(myTableTreeItem, mySeachTreeItem));
   }

   public MyXViewer(Shell shell_1, int i, MyTreeItem myTableTreeItem, MyTreeItem mySeachTreeItem) {
      super(shell_1, i, new MyXViewerFactory(myTableTreeItem, mySeachTreeItem));
   }

   public MyXViewer(Composite parent, int i, MyTreeItem myTableTreeItem, MyTreeItem mySeachTreeItem) {
      super(parent, i, new MyXViewerFactory(myTableTreeItem, mySeachTreeItem));
   }

   public boolean isScheduled(IMyTreeTask autoRunTask) {
      return true;
   }

   public boolean isRun(IMyTreeTask autoRunTask) {
      return runList.contains(autoRunTask);
   }

   public void setRun(IMyTreeTask autoRunTask, boolean run) {
      if (run) {
         runList.add(autoRunTask);
      } else {
         runList.remove(autoRunTask);
      }
   }

   @Override
   public boolean handleLeftClickInIconArea(TreeColumn treeColumn, TreeItem treeItem) {
	return false;
//      if (treeColumn.getData().equals(MyXViewerFactory.Run_Col)) {
//         setRun((ISomeTask) treeItem.getData(), !isRun((ISomeTask) treeItem.getData()));
//         update(treeItem.getData(), null);
//         return true;
//      } else {
//         return super.handleLeftClickInIconArea(treeColumn, treeItem);
//      }

   }

   @Override
   public boolean handleAltLeftClick(TreeColumn treeColumn, TreeItem treeItem) {
	return false;
//      if (treeColumn.getData().equals(MyXViewerFactory.Last_Run_Date)) {
//         Date promptChangeDate = XPromptChange.promptChangeDate(MyXViewerFactory.Last_Run_Date.getName(), new Date());
//         System.out.println("promptChangeDate " + promptChangeDate);
//         SomeTask task = (SomeTask) treeItem.getData();
//         task.setLastRunDate(promptChangeDate);
//         refresh();
//      }
//      return super.handleAltLeftClick(treeColumn, treeItem);
   }

}
