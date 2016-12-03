 
package com.hydra.project.parts;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.TreeViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import com.hydra.project.database.DBTools;
import com.hydra.project.editors.MyCellEditors;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;
import com.hydra.project.provider.KnotenCheckProvider;
import com.hydra.project.provider.KnotenContentProvider;
import com.hydra.project.provider.KnotenTableLabelProvider;

public class BaumView {
	@Inject
	IEventBroker eventBroker;
	
	@Inject
	private MDirtyable dirty;
	
	public final static String DB4OFILENAME="C:/MeinBaum";

	private static final String MyTreeItemEvent = null;
	
	private static CheckboxTreeViewer treeViewer;
	private static Tree MeinBaum;
	protected List <MyTreeItem> list;
	
	@Inject
	private ESelectionService selectionService;
	
	@Inject
	public BaumView() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		createViewer(parent);
	}
	
	@Focus
	public void setFocus() {
		treeViewer.getControl().setFocus();
		LogfileView.log(this + " setFocus is called");
	}
	
	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	public static void addNodes(MyTreeItem myTreeItem){
		if (myTreeItem.getParent()==null){
			treeViewer.add(treeViewer.getTree().getItem(0),myTreeItem);
		}else{
			treeViewer.add(myTreeItem.getParent(),myTreeItem);
		}	
	}
	
	 private void createViewer(Composite parent) {
		final Tree MeinBaum = new Tree(parent,SWT.NONE | SWT.CHECK | SWT.MULTI | SWT.H_SCROLL
	            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);	
		treeViewer = new CheckboxTreeViewer(MeinBaum);		
		MeinBaum.setHeaderVisible(true);
		SpaltenErzeugen(MeinBaum);		

		treeViewer.setContentProvider(new KnotenContentProvider());	
		treeViewer.setLabelProvider(new KnotenTableLabelProvider()); 
		treeViewer.setCheckStateProvider(new KnotenCheckProvider());
		

		list = new  ArrayList<MyTreeItem>(5);
	    list.add(0,TreeTools.InitialisiereBaum());
	    
	  
	    treeViewer.setInput(list);
	    
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
//					if (selection.size() == 0){
					MyTreeItem myTreeItem = (MyTreeItem) selection.getFirstElement();
					selectionService.setSelection(myTreeItem);
//					System.out.println("SelectionChanged in treeViewer" + myTreeItem.getClass().hashCode());
//					}
			}
		});
	    
		  // When user checks a checkbox in the tree, check all its children
		treeViewer.addCheckStateListener(new ICheckStateListener() {
	      public void checkStateChanged(CheckStateChangedEvent event) {
	        if (event.getChecked()) {
	        	treeViewer.setSubtreeChecked(event.getElement(), true);
	        	System.out.println("SelectionChecked in treeViewer");
	        }else {
	        	treeViewer.setSubtreeChecked(event.getElement(), false);
	        	System.out.println("SelectionChecked in treeViewer");
	        }
	        	
	      }
	    });
	 }
	
	
	private void myEventBroker(MyTreeItem myTreeItem){
		if (myTreeItem==null) LogfileView.log(this.getClass(),"Nachricht aus BaumViewer:treeitem ist null ");
		//sendet die geänderten Informationen in den Datenbus
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully) LogfileView.log(this.getClass(),"Nachricht aus BaumViewer gesendet "+ myTreeItem.getVariablenWert());
		
		eventBroker.post("MyTreeItemEventUpdate", myTreeItem);
		
	}

	
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		System.out.println("Nachricht in BaumViewer empfangen " + myTreeItem.getVariablenWert());
		if (treeViewer != null && myTreeItem != null) {
			treeViewer.refresh(myTreeItem);
			DBTools.updateMyTreeItem(myTreeItem);
		}	
	}
	
	
//	@ Inject
//	public void setSelection (@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional MyTreeItem myTreeItem) {
//	//Process Selection
//		System.out.println("SelectionChanged");
//		if (treeViewer!=null) {
//			treeViewer.refresh(myTreeItem);
//		}
//
//	}
	
	
	public static void refreshView(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.remove(myTreeItem);
			treeViewer.setInput(myTreeItem.getChildren());
//			treeViewer.setInput(ToolsDB4O.GetRootNodes());
		}else {
			treeViewer.remove(myTreeItem.getParent());
			treeViewer.refresh(myTreeItem.getParent());
		}
		setMySelection(myTreeItem);
	}
	
	public static void refreshAll(){
//		treeViewer.remove(ToolsDB4O.GetRootNodes());
//		treeViewer.refresh(ToolsDB4O.GetRootNodes());
		treeViewer.refresh();
	}
	
	public static void setMySelection(MyTreeItem myTreeItem) {
        TreeItem item = findTreeItem(treeViewer.getTree().getItems(), myTreeItem);
        if (item == null) return;
        treeViewer.getTree().setSelection(item);
        treeViewer.setSelection(treeViewer.getSelection(), true);
    }
	
//	protected void showItem(Item item) {
//		treeViewer.getTree().showItem((TreeItem)item);
//	}

//	private static MyTreeItem findMyTreeItemParent(MyTreeItem myTreeItem) {
//		TreeItem[] items = treeViewer.getTree().getItems();
//		
//		for (TreeItem item : items) {
//        	MyTreeItem n = (MyTreeItem)item.getData();
//            if (n == null) continue;
//            if (n.getUuid().equals(myTreeItem.getUuid())) return (MyTreeItem)item.getData();
//            TreeItem result = findMyTreeItemParent(item.getItems(), myTreeItem);
//            if (result != null) return result;
//        }
//        return null;
//	}
	
	
	private static TreeItem findTreeItem(TreeItem[] items, MyTreeItem myTreeItem) {
	        for (TreeItem item : items) {
	        	MyTreeItem n = (MyTreeItem)item.getData();
	            if (n == null) continue;
	            if (n.getUuid().equals(myTreeItem.getUuid())) return item;
	            TreeItem result = findTreeItem(item.getItems(), myTreeItem);
	            if (result != null) return result;
	        }
	        return null;
	}
	private static TreeItem findTreeItem(TreeItem[] items, String string) {
       for (TreeItem item : items) {
       	MyTreeItem myTreeItem = (MyTreeItem)item.getData();
           if (myTreeItem == null) continue;
           String value = myTreeItem.getVariablenWert();
           if (value.endsWith(string)) return item;
           TreeItem result = findTreeItem(item.getItems(), string);
           if (result != null) return result;
       }
       return null;
	}
	
	public static MyTreeItem findNode(String string) {
		TreeItem item = findTreeItem(treeViewer.getTree().getItems(), string);
		MyTreeItem myTreeItem = (MyTreeItem)item.getData();
		if (item == null) return myTreeItem;
       treeViewer.getTree().setSelection(item);
       treeViewer.setSelection(treeViewer.getSelection(), true);
       return myTreeItem;
	}
	
	
	
	public static void LevelUpOperation(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.setInput(myTreeItem.getChildren());
		}else {
			treeViewer.refresh(myTreeItem.getParent(),false);
		}
		treeViewer.expandToLevel(myTreeItem,3);
	}
	
	

	/**
	 * @param myTreeItem Vater wird aktualisiert
	 */
	public static void LevelDownOperation(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.setInput(myTreeItem.getChildren());
		}else {
			treeViewer.refresh(myTreeItem.getParent(),false);
		}
		treeViewer.expandToLevel(myTreeItem,3);
	}
	
	public static void NodeDownOperation(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.setInput(myTreeItem.getChildren());
		}else {
			treeViewer.refresh(myTreeItem.getParent(),false);
		}
	}
	public static void NodeUpOperation(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.setInput(myTreeItem.getChildren());
		}else {
			treeViewer.refresh(myTreeItem.getParent(),false);
		}
	}
	public static void NodePasteOperation(MyTreeItem myTreeItem){
		if (myTreeItem.getParent().getUuid().equals("Root")){
			treeViewer.setInput(myTreeItem.getChildren());
		}else {
			treeViewer.refresh(myTreeItem.getParent(),false);
		}
	}
	
	public static void UpdateOperation(MyTreeItem myTreeItem){

		treeViewer.add(myTreeItem.getParent(),myTreeItem);

		treeViewer.update(myTreeItem, null);
	}		
	
	public static void NewTreeOperation(){
		 treeViewer.remove(TreeTools.myTopTreeItem);
		 treeViewer.setInput(TreeTools.myTopTreeItem);
	}
	
	public static void RevealNodes(MyTreeItem myTreeItem){
		treeViewer.reveal(myTreeItem);
		treeViewer.expandToLevel(myTreeItem, 1);
		treeViewer.setSelection((ISelection) new StructuredSelection(myTreeItem),true);
		treeViewer.getControl().setFocus();
	}		
	
	public static void DeleteNode(MyTreeItem myTreeItem){
		try {
			treeViewer.remove(myTreeItem);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param myTreeItem the myTreeItem to set
	 * rekursiv noch oben laufen und alle Vaterknoten aktualisieren
	 */
	public static void RefreshAllNodesAbove(MyTreeItem myTreeItem){
		if (myTreeItem != null) {
			try {
				treeViewer.refresh(myTreeItem);
				LogfileView.log("Aktualisiere Knoten:"
						+ myTreeItem.getBezeichnung());
				if (myTreeItem.getParent() != null) {
					if (!myTreeItem.isWurzel()) {
						RefreshAllNodesAbove(myTreeItem.getParent());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void RefreshNode(MyTreeItem myTreeItem){
		try {
			treeViewer.refresh(myTreeItem);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void UpdateNode(MyTreeItem myTreeItem){
		try {
			treeViewer.update(myTreeItem, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void NewNode(MyTreeItem myTreeItem){
		try {		
			treeViewer.refresh(myTreeItem.getParent(),false);
//			treeViewer.update(parentElement.getParent(), null);
//			treeViewer.add(parentElement,ds);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	

	
	public void SpaltenErzeugen(Tree MeinBaum){
		MyCellEditors.setTreeViewer(treeViewer);
		
        TreeViewerColumn column1 = new TreeViewerColumn(treeViewer, SWT.NONE);
        column1.getColumn().setAlignment(SWT.LEFT);
        column1.getColumn().setText("Baum");
        column1.getColumn().setWidth(300);
        column1.setLabelProvider(new ColumnLabelProvider());
  
        TreeViewerColumn column2 = new TreeViewerColumn(treeViewer,SWT.LEFT);
        column2.getColumn().setAlignment(SWT.LEFT);
        column2.getColumn().setText("Wert");
        column2.getColumn().setWidth(200);
        column2.setLabelProvider(new ColumnLabelProvider());
        // Spalte editierbar machen:
        column2.setEditingSupport(new EditingSupport(treeViewer) {
            protected boolean canEdit(Object element) {
                return MyCellEditors.canEdit(element);
            }
 
            protected CellEditor getCellEditor(Object element) {
				return MyCellEditors.getCellEditor(element);
            }
 
            protected Object getValue(Object element) {
                return MyCellEditors.getValue(element);
            }
 
            protected void setValue(Object element, Object value) {
            	MyTreeItem myTreeItem = MyCellEditors.setValue(element, value);
            	myEventBroker(myTreeItem);
                treeViewer.update(element, null);
            }
        });       
        
        TreeViewerColumn column3 = new TreeViewerColumn(treeViewer, SWT.NONE);
        column3.getColumn().setAlignment(SWT.LEFT);
        column3.getColumn().setText("Parameter");
        column3.getColumn().setWidth(100);
        column3.setLabelProvider(new ColumnLabelProvider());
        
  
        TreeViewerFocusCellManager focusCellManager = new TreeViewerFocusCellManager(
                treeViewer, new FocusCellOwnerDrawHighlighter(treeViewer));
 
        ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
                treeViewer) {
            protected boolean isEditorActivationEvent(
                    ColumnViewerEditorActivationEvent event) {
                return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
                        || event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2
                        || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
                        //|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
                        || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION;
            }
        };
 
        TreeViewerEditor.create(treeViewer, focusCellManager, actSupport,
                ColumnViewerEditor.TABBING_HORIZONTAL
                        | ColumnViewerEditor.TABBING_VERTICAL
                        | ColumnViewerEditor.KEYBOARD_ACTIVATION);
	}
	

	/**
	 * @author Burkhard Pöhler
	 * Überprüft beim Copy/Cut and Paste ob versucht wird, im eigenen Zweig zu kopieren 
	 * @param item Startpunkt im Baum
	 * @param uuid gesuchter Datensatzname
	 * @param return false = nicht gefunden
	 * 
	 */		
	public boolean suchenAufwärts(TreeItem item , String uuid){
		MyTreeItem myTreeItem = (MyTreeItem) item.getData();
		if((myTreeItem.getUuid().equals(uuid))){
			return true;
		}else{
			if (item.getParentItem()==null)return false;
			TreeItem parentItem = (TreeItem) item.getParentItem();
			boolean flag = suchenAufwärts(parentItem,uuid); 
			return flag;
		}
	}
	
//	private void addInPlaceTitleEditor(){
//	final TreeEditor editor = new TreeEditor(treeViewer.getTree());
//	
////	final ColumnViewerEditor columnViewerEditor = new ColumnViewerEditor(treeViewer.getTree());
////	treeViewer.setColumnViewerEditor(columnViewerEditor );
//	
//	treeViewer.getTree().addKeyListener(new KeyListener(){
//		
//		@Override
//		public void keyPressed(KeyEvent e) {
//			 if (e.keyCode == SWT.F2) {
//				 IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
//				 if (selection.size() == 1){
//					 if(selection.getFirstElement() instanceof Nodes){
//							System.out.println("start addInPlaceTitleEditor by F2 Key");
//							Control oldEditor = editor.getEditor();
//							if (oldEditor != null)oldEditor.dispose();
//							Nodes nodes = (Nodes)selection.getFirstElement();
//							TreeItem treeItem = (TreeItem) selection.getFirstElement();
//							Text newEditor = new Text(treeViewer.getTree(), SWT.BORDER);
//							newEditor.setText(nodes.getDatensatz().getText());
//							
//							newEditor.addKeyListener(new KeyAdapter(){
//								public void keyPressed(KeyEvent e){
//									if (e.character ==27){
//										editor.getEditor().dispose();
//									}else if (e.character ==13){
//										saveEditorContent(editor);
//									}
//								}
//							});
//							
//							newEditor.selectAll();
//							newEditor.setFocus();
//							editor.setEditor(newEditor, treeItem); 
//					 }
//				 }
//			 }
//			
//		}
//		private void saveEditorContent(final TreeEditor editor){
//			
//		}
//
//		@Override
//		public void keyReleased(KeyEvent e) {
//			// TODO Auto-generated method stub
//		}
//		
//	});
//}

}