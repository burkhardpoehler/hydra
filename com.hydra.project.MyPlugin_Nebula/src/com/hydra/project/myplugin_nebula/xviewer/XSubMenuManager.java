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
package com.hydra.project.myplugin_nebula.xviewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

/**
 * @author Andrew M. Finkbeiner
 */
public class XSubMenuManager extends MenuManager {

   private boolean visible = true;

   public XSubMenuManager(String string) {
      super(string);
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   @Override
   public boolean isVisible() {
      return visible;
   }

   @Override
   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void setEnabled(boolean enabled) {
      this.add(new Separator());
   }
}
