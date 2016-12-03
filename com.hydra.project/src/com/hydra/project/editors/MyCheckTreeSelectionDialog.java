package com.hydra.project.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lifecycle.MyImageHandler;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.TreeTools;

public class MyCheckTreeSelectionDialog extends Dialog {
	  private Combo combobox;
	  private String txtRow1 = "Baum";
//	  private static CheckboxTreeViewer treeViewer;
	  private static TreeViewer treeViewer;
	  private MyTreeItem myTreeItem;
	  private MyTreeItem myFilterTreeItem = null;
	  private static MyTreeItem mySelectedTreeItem = null;
	  protected List <MyTreeItem> list;
	  private Button OKButton;
	  private Boolean noSelectionInTreeviewer=false;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public MyCheckTreeSelectionDialog(Shell parentShell, MyTreeItem selectedMyTreeItem) {
		super(parentShell);
		myTreeItem=selectedMyTreeItem;
		
	}

	  @Override
	  public void create() {
	    super.create();
	  }
	
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createFilterArea(container);
		parent.getShell().setText("Auswahldialog");

		GridData dataTreeArea = new GridData();
		dataTreeArea.grabExcessHorizontalSpace = true;
		dataTreeArea.grabExcessVerticalSpace = true;
		dataTreeArea.horizontalAlignment = GridData.FILL;
		dataTreeArea.verticalAlignment = GridData.FILL;

		final Tree MeinBaum = new Tree(container,SWT.FILL | SWT.MULTI | SWT.H_SCROLL
	            | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);			
//		treeViewer = new CheckboxTreeViewer(MeinBaum);
		treeViewer = new TreeViewer(MeinBaum);
		MeinBaum.setHeaderVisible(true);
		MeinBaum.setLayoutData(dataTreeArea);
		SpaltenErzeugen(MeinBaum);	
		
		treeViewer.setContentProvider(new KnotenContentProvider());	
		treeViewer.setLabelProvider(new KnotenTableLabelProvider()); 
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			  @Override
			  public void doubleClick(DoubleClickEvent event) {
			    TreeViewer viewer = (TreeViewer) event.getViewer();
			    IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection(); 
		        //schließt den Editor 
			    setReturnCode(OK);
		        close();
//			    Object selectedNode = thisSelection.getFirstElement(); 
//			    viewer.setExpandedState(selectedNode,
//			        !viewer.getExpandedState(selectedNode));
			  }
			}); 
		
		
		
		// react to the selection change of the viewer
		// note that the viewer returns the actual object 
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
		  public void selectionChanged(SelectionChangedEvent event) {
		    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		    if (selection.size() > 0){
		    	mySelectedTreeItem = (MyTreeItem) selection.getFirstElement();
		    	String path= "";
		    	path = getPath(mySelectedTreeItem, path);
		    	parent.getShell().setText("Auswahldialog: >" + path);
		    	if (!noSelectionInTreeviewer) {
					OKButton.setEnabled(true);
				}
		    }
		  }
		}); 
		

//		// Retrieve the current selection
//		treeViewer.setSelection((ISelection) mySelectedTreeItem , true);
//		ISelection selection = treeViewer.getSelection();
//		// Ensure there is a selection
//		if (selection != null) {
//			if ((selection instanceof IStructuredSelection) == true) {
//				
//			}
//		}
		
//		treeViewer.setCheckStateProvider(new KnotenCheckProvider());
//		  // When user checks a checkbox in the tree, check all its children
//		treeViewer.addCheckStateListener(new ICheckStateListener() {
//	      public void checkStateChanged(CheckStateChangedEvent event) {
//	        if (event.getChecked()) {
//	        	treeViewer.setSubtreeChecked(event.getElement(), true);
//	        	System.out.println("SelectionChecked in treeViewer");
//	        }else {
//	        	treeViewer.setSubtreeChecked(event.getElement(), false);
//	        	System.out.println("SelectionChecked in treeViewer");
//	        }
//	        	
//	      }
//	    });
		
		combobox.deselectAll();
	    combobox.select(0);
		list = new  ArrayList<MyTreeItem>(5);
		list.add(0,myFilterTreeItem);
    	if (!noSelectionInTreeviewer) {			//keine Anzeige für den Treeviewer erlaubt
    		treeViewer.setInput(list);
    		treeViewer.expandAll();
		}
		

		return container;
	}
	
	public static MyTreeItem getSelectedMyTreeItem(){
		return mySelectedTreeItem;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myTreeItem);

//		Button buttonCancel = new Button(parent, SWT.PUSH);
//	    buttonCancel.setText("Cancel");
		String Buttontext = "Parameter neu";
		
		if (myTreeItemStrukturknoten.isEigenschaft()) Buttontext="Eigenschaft neu";
		if (myTreeItemStrukturknoten.isParameter()) Buttontext="Parameter neu";
		
		createButton(parent, IDialogConstants.DETAILS_ID, Buttontext,false);
		createButton(parent, IDialogConstants.ABORT_ID, "Stufe neu",false);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,	true);
		createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
		

//		 if (myTreeItemStrukturknoten.isEigenschaft()){
//			this.getButton(OK).setText("Neu"); //Button ausblenden 
//		 }
//		
		OKButton = getButton(IDialogConstants.OK_ID);
		OKButton.setEnabled(false);


		 Button buttonABORT = getButton(IDialogConstants.ABORT_ID);
		 buttonABORT.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			     // Set return code
		        setReturnCode(IDialogConstants.ABORT_ID);
				close();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(IDialogConstants.ABORT_ID);
				close();	
			}
			    });
		 
		// Optionbutton nur sichtbar, wenn neuer Parameter oder Eigenschaft neu erzeugt werden darf
		 Button buttonDETAILS = getButton(IDialogConstants.DETAILS_ID);
		if (myTreeItemStrukturknoten.isParameter() |myTreeItemStrukturknoten.isEigenschaft() ){
			buttonDETAILS.setVisible(true);
		}else {
			buttonDETAILS.setVisible(false);
		}
		 
		 buttonDETAILS.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			     // Set return code
		        setReturnCode(IDialogConstants.DETAILS_ID);
				close();
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(IDialogConstants.DETAILS_ID);
				close();	
			}
			    });
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(1000, 600);
	}

	@Override
	protected boolean isResizable() {
	   return true;
	}
	
	 private void createFilterArea(Composite container) {
//		 Label lbHeadline = new Label(container, SWT.NONE);
//		 lbHeadline.setText("Filter auswählen und die gewünschten Werte durch Setzen des Kontrollkästchen auswählen");
		    
		 Label lbtFilterName = new Label(container, SWT.NONE);
		 lbtFilterName.setText("Filter");

		 GridData dataComboBox = new GridData();
		 dataComboBox.grabExcessHorizontalSpace = true;
		 dataComboBox.horizontalAlignment = GridData.FILL;

	    combobox = new Combo(container, SWT.BORDER | SWT.READ_ONLY);	    
	    combobox.setLayoutData(dataComboBox);
	    defineFilterForCombobox(combobox, myTreeItem);
	    combobox.select(0);

	    combobox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combobox.getText().equals("Projekte")) {
					System.out.println("Projekte" );
					myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Projekte");
				}else if (combobox.getText().equals("Parameter")) {
					System.out.println("Parameter" );
					myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Parameter");
				}else if (combobox.getText().equals("Vorlagen")) {
					System.out.println("Vorlagen" );
					myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.searchForStrukturknoten(myTreeItem).getParent(), "Vorlagen");
//				}else if (combobox.getText().equals("Eigenschaften")) {
//					System.out.println("Eigenschaften" );
//					myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Eigenschaften");
				}else{
					myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Parameter");
				}
				List <MyTreeItem> list = new  ArrayList<MyTreeItem>(5);
				list.add(0,myFilterTreeItem);
				treeViewer.setInput(list);
				treeViewer.expandAll();				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
	      });
	 }   

	/**
	 * Setzt die erlaubten Knoten für die Combobox
	 * @param myTreeItem der Startknoten
	 * @param combo die Combobox
	 */ 
	 private void defineFilterForCombobox(Combo combo, MyTreeItem myTreeItem) {
		  //Combobox vorbereiten. Combobx darf nicht leer sein und auch das Filter muss gesetzt sein
		 // 1. Schritt Stuktruknoten ermitteln
		 MyTreeItem myTreeItemStrukturknoten = TreeTools.searchForStrukturknoten(myTreeItem);
		 if (myTreeItemStrukturknoten.isParameter()){
			 if (myTreeItem.isDummy()) {
					//keine Anzeige
				 combobox.add("Keine Auswahl möglich");
				 noSelectionInTreeviewer=true;
				 combobox.select(0);
				 myFilterTreeItem = new MyTreeItem();
			}else {
				//Anzeige nur von Eigenschaften
				combobox.add("Eigenschaften");
				combobox.select(0);
				myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Eigenschaften");
			}
				
		 } else if(myTreeItemStrukturknoten.isEigenschaft()){
			 //keine Anzeige
			 combobox.add("Keine Auswahl möglich");
			 noSelectionInTreeviewer=true;
			 combobox.select(0);
			 myFilterTreeItem = new MyTreeItem();
			 
		 }	else if(myTreeItemStrukturknoten.isVorlage()){
			//Anzeige nur von Parametern
			 combobox.add("Parameter");
			 combobox.add("Vorlagen");
			 combobox.select(0);
			 myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Parameter");
		 } else{
			 combobox.add("Parameter");
			 combobox.add("Vorlagen");
			 combobox.select(0);
			 myFilterTreeItem = TreeTools.findMyTreeItem(TreeTools.getMyTopTreeItem(), "Parameter");
		 }
	 }
	 
		public void SpaltenErzeugen(Tree MeinBaum){

	        TreeViewerColumn column1 = new TreeViewerColumn(treeViewer, SWT.NONE);
	        column1.getColumn().setAlignment(SWT.LEFT);
	        column1.getColumn().setText(txtRow1);
	        column1.getColumn().setWidth(300);
	        column1.setLabelProvider(new ColumnLabelProvider());

	        TreeColumn column2 = new TreeColumn(MeinBaum,SWT.LEFT);
	        column2.setAlignment(SWT.LEFT);
	        column2.setText("ParameterID");
	        column2.setWidth(100);
	        
	        TreeColumn column3 = new TreeColumn(MeinBaum,SWT.LEFT);
	        column3.setAlignment(SWT.LEFT);
	        column3.setText("Bezeichnung");
	        column3.setWidth(200);
	        
	        TreeColumn column4 = new TreeColumn(MeinBaum,SWT.LEFT);
	        column4.setAlignment(SWT.LEFT);
	        column4.setText("Beschreibung");
	        column4.setWidth(300);
		}
		
	 
	
	 /**
	  * @author Poehler
	  *
	  */
	 private class KnotenCheckProvider implements ICheckStateProvider{

	 	@Override
	 	public boolean isChecked(Object element) {
	 		boolean schwitch = false; 
	 		if (element instanceof MyTreeItem){
	 			MyTreeItem myTreeItem =(MyTreeItem) element;
	 			schwitch= myTreeItem.isChecked();
	 		}
	 		return schwitch;
	 	}

	 	@Override
	 	public boolean isGrayed(Object element) {
	 		boolean schwitch = false; 
	 		if (element instanceof MyTreeItem){
	 			MyTreeItem myTreeItem =(MyTreeItem) element;
	 			schwitch= myTreeItem.isGrayed();
	 		}
	 		return schwitch;
	 	}

	 }
	 

	private class KnotenContentProvider implements ITreeContentProvider{
	
		protected TreeViewer viewer;
	
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MyTreeItem){
					MyTreeItem myTreeItem = (MyTreeItem)parentElement;
					
					Object[] o = null;
					if (myTreeItem.getChildren() != null) {
						if (!myTreeItem.isWurzel()) {	
						}
						o = myTreeItem.getChildren().toArray();
					}
					
					return o;
				}
				return new Object[0];
			}
			
			@Override
			public Object getParent(Object element) {
				if (element instanceof MyTreeItem){
				 	return ((MyTreeItem)element).getParent();
				}
				return null;
			}
	
			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof MyTreeItem){
					MyTreeItem myTreeItem =(MyTreeItem) element;
					if (myTreeItem.isEigenschaft()){			//Alle Eigenschaften werden angezeigt
						return myTreeItem.isHasChildren();
					}else if(myTreeItem.isParameter()){			//Nur die Parameter dürfen sichtbar sein
						if (myTreeItem.isHasChildren()) {
							for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
								if(myTreeItem.getChildren().get(i).isEigenschaft()){	//Kinder sind Eigenschaften und werden nicht dargestellt
									return false;
								}
							}
						}
					}
					
					return myTreeItem.isHasChildren();
				}
				return false;
			}
			
			@Override
			public Object[] getElements(Object Liste) {
				return ((List)Liste).toArray();
			}
	
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				System.out.println("inputChanged" );
				this.viewer = (TreeViewer)viewer;
				if (oldInput != null) {
					for (Iterator iterator = ((List) oldInput).iterator(); iterator
							.hasNext();) {
						MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
					}
				}
				if (newInput != null) {
					for (Iterator iterator = ((List) newInput).iterator(); iterator
							.hasNext();) {					
						MyTreeItem myTreeItem = (MyTreeItem) iterator.next();
					}
				}
			}
		}
	
	
	private class KnotenTableLabelProvider implements ITableLabelProvider {
		
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof MyTreeItem){
					return getImage(element,  columnIndex);
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			
			MyTreeItem myTreeItem =(MyTreeItem) element;
			switch (columnIndex){
				case 0: return myTreeItem.getAnzeigeText();
				case 1: return myTreeItem.getParameter();
				case 2: return myTreeItem.getBezeichnung();
				case 3: return myTreeItem.getBeschreibung();
//				case 4: return String.valueOf(ds.getIndex());
//				case 5: return ds.getText();
//				case 6: return String.valueOf(ds.getFirstPos());
//				case 7: return String.valueOf(ds.getLastPos());
//				case 8: return ds.getVariablentyp();
				
			}
			return null;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}
		
		public  Image getImage(Object element, int columnIndex) {
			String dateiname = null;	// hole den Namen des Bildes
			if (element instanceof MyTreeItem){
				MyTreeItem myTreeItem =(MyTreeItem) element;

				switch (columnIndex){
				case 0: dateiname = myTreeItem.getIconDateiname();break;
//				case 1: dateiname = myTreeItem.getIconDateiname();break;
//				case 2:	dateiname = "001_001_001.gif";break;
//				case 3: dateiname = myTreeItem.getVariablentyp().toString()+".gif";break;
				}
			}		
			return MyImageHandler.getImage(dateiname);		
		}

	}
	
	
	 private static String getPath(MyTreeItem myTreeItem, String path){
		  
		  if(myTreeItem.getParent() != null){
			  String path1 = getPath(myTreeItem.getParent(), "/" + myTreeItem.getParent().getBezeichnung());
			  path = path1 + path;
		  }
		  return path;
	  }
		 
}




