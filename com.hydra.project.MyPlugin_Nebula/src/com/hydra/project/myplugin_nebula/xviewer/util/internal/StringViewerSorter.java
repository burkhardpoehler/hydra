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

import java.text.Collator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * @author Donald G. Dunne
 */
@SuppressWarnings("deprecation")
public class StringViewerSorter extends ViewerSorter {

   public StringViewerSorter() {
      // do nothing
   }

   public StringViewerSorter(Collator collator) {
      super(collator);
   }

   @SuppressWarnings("unchecked")
   @Override
   public int compare(Viewer viewer, Object e1, Object e2) {
	   String s1 = (String) e1;
	   String s2 = (String) e2;
      return getComparator().compare(s1, s2);
   }
}
