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
package com.hydra.projects.XViewer.Main.model;

import java.util.ArrayList;
import java.util.List;

import com.hydra.project.model.MyTreeItem;

/**
 * Interface for example objects used for example implementation of XViewer
 * 
 * @author Pöhler
 */
public interface IMyTreeTask {
   public enum RunDb {
      Production_Db,
      Test_Db
   };

   public enum TaskType {
      Regression,
      Db_Health,
      Data_Exchange,
      Backup,
      Refreshed
   }

   public TaskType getTaskType();

   public RunDb getRunDb();

   public  List<MyTreeItem> getMyList();
}
