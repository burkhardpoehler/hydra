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
 * Example object for use of example XViewer implementation
 * 
 * @author Pöhler
 */
public class MyTreeTask implements IMyTreeTask {

   private final RunDb runDb;
   private TaskType taskType;

   private  List<MyTreeItem> myList = new ArrayList<MyTreeItem>();

 
   public MyTreeTask(RunDb runDb, TaskType taskType, List<MyTreeItem> myList) {
      this.runDb = runDb;
      this.taskType = taskType;
      this.myList = myList;
   }


   @Override
   public RunDb getRunDb() {
      return runDb;
   }

   @Override
   public TaskType getTaskType() {
      return taskType;
   }

 
   public void setTaskType(TaskType taskType) {
      this.taskType = taskType;
   }

   @Override
   public List<MyTreeItem> getMyList() {
	   return myList;
   }
	
	public void setMyList(List<MyTreeItem> myList) {
		this.myList = myList;
	}

}
