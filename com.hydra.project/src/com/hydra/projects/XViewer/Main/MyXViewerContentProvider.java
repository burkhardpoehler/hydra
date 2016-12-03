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

import java.util.Collection;
import java.util.HashSet;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.hydra.projects.XViewer.Main.model.IMyTreeTask;
import com.hydra.projects.XViewer.Main.model.MyTreeTask;

/**
 * Content provider for example XViewer implementation
 * 
 * @author Donald G. Dunne
 */
public class MyXViewerContentProvider implements ITreeContentProvider {

   protected Collection<IMyTreeTask> rootSet = new HashSet<IMyTreeTask>();
   private final static Object[] EMPTY_ARRAY = new Object[0];

   public MyXViewerContentProvider() {
      super();
   }

   @Override
   @SuppressWarnings("rawtypes")
   public Object[] getChildren(Object parentElement) {
	   //das Ausbleden verhindert die Baumstruktur in der ersten Spalte
      if (parentElement instanceof Object[]) {
         return (Object[]) parentElement;
      }
      if (parentElement instanceof Collection) {
         return ((Collection) parentElement).toArray();
      }
//      if (parentElement instanceof MyTreeTask) {
//         return ((MyTreeTask) parentElement).getMyList().toArray();
//      }
      return EMPTY_ARRAY;
   }

   @Override
   public Object getParent(Object element) {
      return null;
   }

   @Override
   public boolean hasChildren(Object element) {
      if (element instanceof MyTreeTask) {
         return ((MyTreeTask) element).getMyList().size() > 0;
      }
      return false;
   }

   @Override
   public Object[] getElements(Object inputElement) {
      if (inputElement instanceof String) {
         return new Object[] {inputElement};
      }
      return getChildren(inputElement);
   }

   @Override
   public void dispose() {
      // do nothing
   }

   @Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      // do nothing
   }

   public Collection<IMyTreeTask> getRootSet() {
      return rootSet;
   }

}
