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

import java.util.List;
import com.hydra.project.myplugin_nebula.xviewer.util.XViewerException;

/**
 * Methods to implement if this XViewer allows the user to save local/shared customizations
 * 
 * @author Donald G. Dunne
 */
public interface IXViewerCustomizations {

   /**
    * Called to have customization saved
    */
   void saveCustomization(CustomizeData custData) throws Exception;

   /**
    * Load and return saved customizations
    */
   List<CustomizeData> getSavedCustDatas() throws Exception;

   /**
    * Return customization saved as default
    */
   CustomizeData getUserDefaultCustData() throws XViewerException;

   /**
    * Return true if given customization is the default
    * 
    * @return true if default
    */
   boolean isCustomizationUserDefault(CustomizeData custData) throws XViewerException;

   /**
    * Set given customization as the user default
    */
   void setUserDefaultCustData(CustomizeData newCustData, boolean set) throws Exception;

   /**
    * Delete customization
    */
   void deleteCustomization(CustomizeData custData) throws Exception;

   /**
    * Return true if this XViewer is allowed to save local/shared customizations
    */
   boolean isCustomizationPersistAvailable();

}