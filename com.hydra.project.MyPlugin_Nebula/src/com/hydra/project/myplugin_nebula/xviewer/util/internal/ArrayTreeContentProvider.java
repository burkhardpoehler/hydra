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
package com.hydra.project.myplugin_nebula.xviewer.util.internal;

import java.util.Collection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Donald G. Dunne
 */
public class ArrayTreeContentProvider implements ITreeContentProvider {

   public ArrayTreeContentProvider() {
      super();
   }

   @Override
   @SuppressWarnings("rawtypes")
   public Object[] getChildren(Object parentElement) {
      if (parentElement instanceof Collection) {
         return ((Collection) parentElement).toArray();
      }
      return new Object[] {};
   }

   @Override
   public Object getParent(Object element) {
      return null;
   }

   @Override
   public boolean hasChildren(Object element) {
      return false;
   }

   @Override
   public Object[] getElements(Object inputElement) {
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

}
