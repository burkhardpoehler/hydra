package com.hydra.projects.XViewer.Main;

import java.util.List;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.myplugin_nebula.xviewer.edit.CellEditDescriptor;
import com.hydra.project.myplugin_nebula.xviewer.edit.XViewerConverter;
import com.hydra.project.parts.TableView;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import com.hydra.projects.XViewer.Main.model.MyTreeTask;


public class MyXViewerConverter implements XViewerConverter {

   @Override
   public void setInput(Control c, CellEditDescriptor ced, Object selObject) {
	   // Text Editor
      if (c instanceof Text) {
         Text text = (Text) c;
         if (selObject instanceof MyTreeTask) {
        	 //myTreeTask repräsentiert die Zeile einer Tabelle
        	 MyTreeTask myTreeTask = (MyTreeTask) selObject;
        	 List<MyTreeItem> list = myTreeTask.getMyList();
        	 //durchsuche die einzelnen Zeilen nach dem passenden TreeItem
        	 for (int n = 0; n < list.size(); n++) {
        		 if (ced.getInputField().equals("SpaltenId." + n)) {
        			 text.setText(list.get(n).getVariablenWert());
        			 break;
        		 }
            }
         }
      }

   }

   @Override
   public Object getInput(Control c, CellEditDescriptor ced, Object selObject) {
	   List<MyTreeTask> lsite = (List<MyTreeTask>) selObject;
	   List<MyTreeItem> list = lsite.get(0).getMyList();
	   MyTreeItem myFoundTreeItem = null;
	   for (int n = 0; n < list.size(); n++) {
	 		if (ced.getInputField().equals("SpaltenId." + n)) {
	 			myFoundTreeItem = list.get(n);
	 			break;
	 		}
	   }    	 	
      	 	if (myFoundTreeItem != null){
      	 		// Text Editor
      	 		if (c instanceof Text) {
      	 			Text text = (Text) c;
      	 			myFoundTreeItem.setVariablenWert(text.getText());
      	 			//geändertes Item sichern
      	 			TableView.sendEvent((MyTreeItem) myFoundTreeItem);
      	 			return myFoundTreeItem;      	 			
      	 		}
      	 		// andere Editoren
      	 		
      	 	}else{
      	 		return null;
      	 	}
	    return null;	    
   }

   @Override
   public boolean isValid(CellEditDescriptor ced, Object selObject) {
      return true;
   }


}
