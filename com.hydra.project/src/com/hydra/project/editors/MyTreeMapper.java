/**
 * 
 */
package com.hydra.project.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hydra.project.model.MyTreeItem;
import com.hydra.project.nebula.INewMappingListener;
import com.hydra.project.nebula.ISemanticTreeMapperSupport;
import com.hydra.project.nebula.TreeMapperUIConfigProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
//import org.eclipse.nebula.widgets.treemapper.INewMappingListener;
//import org.eclipse.nebula.widgets.treemapper.ISemanticTreeMapperSupport;
//import org.eclipse.nebula.widgets.treemapper.internal.*;
//import org.eclipse.nebula.widgets.treemapper.TreeMapperUIConfigProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TreeDragSourceEffect;
import org.eclipse.swt.dnd.TreeDropTargetEffect;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.hydra.project.editors.MyLinkFigure;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.parts.LogfileView;

/**
 * A TreeMapper is a composite viewer the creates 2 {@link TreeViewer} (left and right)
 * and an area to display mappings between tree nodes.
 * It relies on a {@link ISemanticTreeMapperSupport} to create your business mapping objects,
 * and to resolve the bounds of a mapping object to object that are provided in the trees.
 * 
 * @author Mickael Istria (EBM WebSourcing (PetalsLink))
 * @since 0.1.0
 * @noextend This class is not intended to be subclassed by clients.
 *
 * @param <M> The type of the business <b>M<b>apping object
 * @param <L> The type of the left bound of the mapping (as provided by <b>L</b>eft {@link ITreeContentProvider})
 * @param <R> The type of the left bound of the mapping (as provided by <b>R</>ight {@link ITreeContentProvider})
 * @author Poehler
 *
 */
public class MyTreeMapper <M, L, R> implements ISelectionProvider {
	private SashForm control;
	private TreeMapperUIConfigProvider uiConfig;
	
	private static TreeViewer leftTreeViewer;
	private static TreeViewer rightTreeViewer;
	private TreeItem leftTopItem;
	private TreeItem rightTopItem;

	private Canvas linkCanvas;
	private LightweightSystem linkSystem;
	private Figure linkRootFigure;
	public static boolean canvasNeedRedraw;
	
	private List<M> mappings;
	private Map<MyLinkFigure, M> figuresToMappings;
	private Map<M, MyLinkFigure> mappingsToFigures;
	private MyLinkFigure selectedFigure;
	private M selectedMapping;
	private ISemanticTreeMapperSupport<M, L, R> semanticSupport;
	private IFigure warningFigure;
	protected static MyTreeItem leftListItem = null;
	protected static MyTreeItem rightListItem = null;

	
	public MyTreeMapper(Composite parent, ISemanticTreeMapperSupport<M, L, R> semanticTreeMapperSupport, TreeMapperUIConfigProvider uiConfig) {
		this.uiConfig = uiConfig;
		this.semanticSupport = semanticTreeMapperSupport;
		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		control = new SashForm(composite, SWT.HORIZONTAL);
		control.setLayout(new FillLayout());
		// left

		leftTreeViewer = new TreeViewer(control);
		//center
		linkCanvas = new Canvas(control, SWT.NONE);
		linkCanvas.setLayout(new FillLayout());
		linkCanvas.setBackground(ColorConstants.white);
		linkSystem = new LightweightSystem(linkCanvas);
		linkRootFigure = new Figure();
		linkRootFigure.setLayoutManager(new XYLayout());
		linkSystem.setContents(linkRootFigure);
		// right
		rightTreeViewer = new TreeViewer(control);
		
		figuresToMappings = new HashMap<MyLinkFigure, M>();
		mappingsToFigures = new HashMap<M, MyLinkFigure>();
		
		// Resize
		ControlListener resizeListener = new ControlListener() {
			public void controlResized(ControlEvent e) {
				canvasNeedRedraw = true;
			}
			public void controlMoved(ControlEvent e) {
				canvasNeedRedraw = true;
			}
		};
		
		leftTreeViewer.getTree().addControlListener(resizeListener);
		rightTreeViewer.getTree().addControlListener(resizeListener);
	

		leftTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				leftListItem = null;
				if (selection.size() == 1){
					leftListItem = (MyTreeItem) selection.getFirstElement();
					checkConnection();
				}			
			}
			
		});
		
		rightTreeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				rightListItem = null;
				if (selection.size() == 1){
					rightListItem = (MyTreeItem) selection.getFirstElement();
					checkConnection();
			    }			
			}
			
		});

//		rightTreeViewer.addTreeListener(myTreeViewerListener);
		
		linkCanvas.addControlListener(resizeListener);
		// Scroll
		leftTreeViewer.getTree().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (canvasNeedRedraw || leftTreeViewer.getTree().getTopItem() != leftTopItem) {
					leftTopItem = leftTreeViewer.getTree().getTopItem();
					redrawMappings();
				}
			}
		});
		rightTreeViewer.getTree().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (canvasNeedRedraw || rightTreeViewer.getTree().getTopItem() != rightTopItem) {
					rightTopItem = rightTreeViewer.getTree().getTopItem();
					redrawMappings();
					canvasNeedRedraw = false;
				}
			}
		});
		// Expand
		TreeListener treeListener = new TreeListener() {
			public void treeExpanded(TreeEvent e) {
				canvasNeedRedraw = true;
			}
			public void treeCollapsed(TreeEvent e) {
				canvasNeedRedraw = true;	
			}
		};
		leftTreeViewer.getTree().addTreeListener(treeListener);
		rightTreeViewer.getTree().addTreeListener(treeListener);
		
		leftTreeViewer.expandAll();
		rightTreeViewer.expandAll();
		
		control.setWeights(new int[] { 2, 1, 2} );
		
//		bindTreeForDND(leftTreeViewer, rightTreeViewer, SWT.LEFT_TO_RIGHT);
//		bindTreeForDND(rightTreeViewer, leftTreeViewer, SWT.RIGHT_TO_LEFT);
		
//		parent.pack();
	}
	

	public MyTreeMapper() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Set the content providers for both trees.
	 * Both tree provides MUST HAVE their {@link ITreeContentProvider#getParent(Object)} method implemeneted.
	 * @param leftContentProvider An {@link ITreeContentProvider} that node are instances of the <b>L<b> type parameter.
	 * @param rightTreeContentProvider An {@link ITreeContentProvider} that node are instances of the <b>R<b> type parameter.
	 */
	public void setContentProviders(ITreeContentProvider leftTreeContentProvider, ITreeContentProvider rightTreeContentProvider) {
		leftTreeViewer.setContentProvider(leftTreeContentProvider);
		rightTreeViewer.setContentProvider(rightTreeContentProvider);
	}
	
	public void setLabelProviders(IBaseLabelProvider leftLabelProvider, IBaseLabelProvider rightLabelProvider) {
		leftTreeViewer.setLabelProvider(leftLabelProvider);
		rightTreeViewer.setLabelProvider(rightLabelProvider);
	}
	
	/**
	 * Sets the input of the widget.
	 * @param leftTreeInput The input for left {@link TreeViewer}
	 * @param rightTreeInput The input for right {@link TreeViewer}
	 * @param mappings The list containing the mapping. It will be used as a working copy and
	 * then MODIFIED by the tree mapper. If you don't want to pass a modifiable list, then pass
	 * a copy of the default mapping list, and prefer using {@link TreeMapper}{@link #addNewMappingListener(INewMappingListener)}
	 * and {@link INewMappingListener} to track the creation of mapping.
	 */
	public void setInput(Object leftTreeInput, Object rightTreeInput, List<M> mappings) {
		clearFigures();
		if (leftTreeInput != null) {
			leftTreeViewer.setInput(leftTreeInput);
			leftTreeViewer.expandAll();
		}
		if (rightTreeInput != null) {
			rightTreeViewer.setInput(rightTreeInput);
			rightTreeViewer.expandAll();
		}
		if (mappings != null) {
			this.mappings = mappings;
			canvasNeedRedraw = true;
		} else {
			this.mappings = new ArrayList<M>();
		}
	}
	
	
	private void checkConnection(){
		MyUpdateNodeStructureEditor.updateShowButton(false, leftListItem, rightListItem);
		if (leftListItem != null){
			if (rightListItem != null){
				MyUpdateNodeStructureEditor.updateShowButton(false, leftListItem, rightListItem);				
				if (checkOperation(leftListItem)){
					if (checkOperation(rightListItem)){
						MyUpdateNodeStructureEditor.updateShowButton(true, leftListItem, rightListItem);
					}
				}
			}
		}
	}

	/**
	 * DO NOT USE IN CODE. Prefer setting "canvasNeedsRedraw" field to true to
	 * avoid useless operations.
	 * @param mappings
	 */
	private void redrawMappings() {
		if (this.mappings == null) {
			return;
		}
		
		boolean everythingOK = true;
		for (M mapping : this.mappings) {
			everythingOK &= drawMapping(mapping);
			if (mapping == selectedMapping) {
				MyLinkFigure newSelectedFigure = mappingsToFigures.get(mapping);
				applySelectedMappingFeedback(newSelectedFigure);
				selectedFigure = newSelectedFigure;
			}
		}
		if (everythingOK && warningFigure != null) {
			linkRootFigure.remove(warningFigure);
			warningFigure = null;
		} else if (!everythingOK && warningFigure == null) {
			warningFigure = createWarningFigure();
			linkRootFigure.add(warningFigure, new Rectangle(5, 5, SWT.DEFAULT, SWT.DEFAULT));
		}
	}

	/**
	 * @return a newly created figure to alert the end-user of an inconsistency in the widget
	 */
	private IFigure createWarningFigure() {
		Image image = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
		ImageFigure res = new ImageFigure(image);
		res.setPreferredSize(10, 10);
//		Label label = new Label(Messages.widgetInconsistency);
//		res.setToolTip(label);
		return res;
	}

//	/**
//	 * @param sourceTreeViewer
//	 * @param targetTreeViewer
//	 * @param direction
//	 */
//	private void bindTreeForDND(final TreeViewer sourceTreeViewer, final TreeViewer targetTreeViewer, final int direction) {
//		final LocalSelectionTransfer sourceTransfer = LocalSelectionTransfer.getTransfer();
//		final LocalSelectionTransfer targetTransfer = LocalSelectionTransfer.getTransfer();
//		sourceTreeViewer.addDragSupport(DND.DROP_LINK, new Transfer[] { sourceTransfer }, 
//				new TreeDragSourceEffect(sourceTreeViewer.getTree()) {
//			@Override
//			public void dragStart(DragSourceEvent event) {
//				if (!sourceTreeViewer.getSelection().isEmpty()){			
//					if (checkOperation( sourceTreeViewer.getSelection())){
//						if (checkOperation( targetTreeViewer.getSelection())) {
//							event.doit = true;
//						}else {
//							event.doit = false;
//						}
//					}					
//				}
//			}
//		});
//		targetTreeViewer.addDropSupport(DND.DROP_LINK, new Transfer[] { targetTransfer }, 
//				new TreeDropTargetEffect(targetTreeViewer.getTree()) {
//			@Override
//			public void dragEnter(DropTargetEvent event) {
//				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
//				event.detail = DND.DROP_LINK;
//				super.dragEnter(event);
//			}
//			
//			@Override
//			public void drop(DropTargetEvent event) {
//				if (checkOperation( targetTreeViewer.getSelection())){
//					if (checkOperation( sourceTreeViewer.getSelection())){
//						performMappingByDrop(sourceTreeViewer, sourceTreeViewer.getSelection(), 
//								targetTreeViewer, (TreeItem) getItem(event.x, event.y), direction);
//					}
//					
//				}
//
//			}
//		});
//	}

//	/**
//	 * @param targetTreeViewer 
//	 * @param data
//	 * @param widget
//	 */
//	protected void performMappingByDrop(TreeViewer sourceTreeViewer, ISelection sourceData, TreeViewer targetTreeViewer, TreeItem targetTreeItem, int direction) {
//		if (checkOperation( targetTreeViewer.getSelection())){
//			if (checkOperation( sourceTreeViewer.getSelection())){
//				Object resolvedTargetItem = resolveTreeViewerItem(targetTreeViewer, targetTreeItem);
//				for (Object sourceItem : ((IStructuredSelection)sourceData).toList()) {
////					createMapping((L)resolvedTargetItem, (R)sourceItem);
//					if (direction == SWT.RIGHT_TO_LEFT) {
//						createMapping((L)resolvedTargetItem, (R)sourceItem);
////						createMapping((L)sourceItem, (R)resolvedTargetItem);
//					} else if (direction == SWT.LEFT_TO_RIGHT) {
//						createMapping((L)resolvedTargetItem, (R)sourceItem);
//					}
//				}
//			}
//		}
//
//	}

	/**
	 * Prüft, ob ein Knoten bereits eine Verbindung hat
	 * @param item
	 * @return flag = false = keine weitere Verbindung erlaubt
	 */
	private boolean checkOperation(ISelection sourceData) {
		boolean flag = true;	
		if (sourceData instanceof IStructuredSelection) {
		    Object o=((IStructuredSelection)sourceData).getFirstElement();    
		    if (o instanceof MyTreeItem)     {
				if (!mappings.isEmpty()){

					for (int n = 0; n < mappings.size(); n++) {
						MyTreeItemBean myTreeItemBean = (MyTreeItemBean) mappings.get(n);
						
//						((MyTreeItem) o).getUuid();
//						if (myTreeItemBean.left.getUuid() == ((MyTreeItem) o).getUuid()){
						if (myTreeItemBean.left.equals(o)){
							flag = false;
							LogfileView.log(MyTreeMapper.class," Vergleiche " + myTreeItemBean.left +" <> " + sourceData);
							LogfileView.log(MyTreeMapper.class," Keine Verbindung erlaubt. Bereits in linker Liste vorhanden");
							break;
						}
//						if (myTreeItemBean.right.getUuid() == ((MyTreeItem) o).getUuid()){
						if (myTreeItemBean.right.equals(o)){
							flag = false;
							LogfileView.log(MyTreeMapper.class," Vergleiche " + myTreeItemBean.right +" <> " + sourceData);
							LogfileView.log(MyTreeMapper.class," Keine Verbindung erlaubt. Bereits in rechter Liste vorhanden");
							break;
						}
					}
				}

		    }
		  }
		if(flag == true) LogfileView.log(MyTreeMapper.class," Verbindung erlaubt.");
		showMapping();
		return flag;
	}
	
	/**
	 * Prüft, ob ein Knoten bereits eine Verbindung hat
	 * @param item
	 * @return flag = false = keine weitere Verbindung erlaubt
	 */
	private boolean checkOperation(MyTreeItem sourceData) {
		boolean flag = true;	
		if (!mappings.isEmpty()){

			for (int n = 0; n < mappings.size(); n++) {
				MyTreeItemBean myTreeItemBean = (MyTreeItemBean) mappings.get(n);
				if (myTreeItemBean.left.equals(sourceData)){
					flag = false;
					LogfileView.log(MyTreeMapper.class," Vergleiche " + myTreeItemBean.left +" <> " + sourceData);
					LogfileView.log(MyTreeMapper.class," Keine Verbindung erlaubt. Bereits in linker Liste vorhanden");
					break;
				}

				if (myTreeItemBean.right.equals(sourceData)){
					flag = false;
					LogfileView.log(MyTreeMapper.class," Vergleiche " + myTreeItemBean.right +" <> " + sourceData);
					LogfileView.log(MyTreeMapper.class," Keine Verbindung erlaubt. Bereits in rechter Liste vorhanden");
					break;
				}
			}
		  }
		if(flag == true) LogfileView.log(MyTreeMapper.class," Verbindung erlaubt.");
		showMapping();
		return flag;
	}
	
	/**
	 * Listet die mappings auf
	 */
	private void showMapping(){
		String string1, string2;
		LogfileView.log(MyUpdateNodeStructureEditor.class," Mapping Liste: ");
		for (int n = 0; n < mappings.size(); n++) {
			MyTreeItemBean myTreeItemBean = (MyTreeItemBean) mappings.get(n);
			string1 = myTreeItemBean.left.getParameter() + " " +
					 myTreeItemBean.left.getBezeichnung() + " " +
					 myTreeItemBean.left.getVariablenWert();
			
			string2 = myTreeItemBean.right.getParameter() + " " +
					 myTreeItemBean.right.getBezeichnung() + " " +
					 myTreeItemBean.right.getVariablenWert();
			LogfileView.log(MyUpdateNodeStructureEditor.class,"Eintrag: " + n + " " + string1 + " " +string2);
		}
	}
	
//	/**
//	 * @param leftItem
//	 * @param rightItem
//	 */
//	private void createMapping(L leftItem, R rightItem) {
//		M newMapping = semanticSupport.createSemanticMappingObject(leftItem, rightItem);
//		if (newMapping != null) {
//			mappings.add(newMapping);
//			refresh();
//			drawMapping(newMapping);
//			for (INewMappingListener<M> listener : creationListeners) {
//				listener.mappingCreated(newMapping);
//			}
//		}
//	}

	/**
	 * Draw a mapping and returns whether the operation is successful or not.
	 * If not, a message is logged to help in debugging.
	 * @param leftItem
	 * @param rightItem
	 * @return true is successful, false if an issue occured
	 */
	private boolean drawMapping(final M mapping) {
		MyLinkFigure previousFigure = mappingsToFigures.get(mapping);
		if (previousFigure != null) {
			previousFigure.deleteFromParent();
			mappingsToFigures.remove(mapping);
			figuresToMappings.remove(previousFigure);
		}
		
		final MyLinkFigure arrowFigure = new MyLinkFigure(linkRootFigure);
		
		{
			boolean leftItemVisible = true;
			TreeItem leftTreeItem = (TreeItem) leftTreeViewer.testFindItem(semanticSupport.resolveLeftItem(mapping));	
			if (leftTreeItem == null) {
//				LogfileView.log(MyTreeMapper.class," Could not find left entry of mapping  " 
//						+ mapping.toString() + " in left treeViewer.");
				return false;
			}
			TreeItem lastVisibleLeftTreeItem = leftTreeItem;
			while (leftTreeItem.getParentItem() != null) {
				if (!leftTreeItem.getParentItem().getExpanded()) {
					lastVisibleLeftTreeItem = leftTreeItem.getParentItem();
					leftItemVisible = false;
				}
				leftTreeItem = leftTreeItem.getParentItem();
			}
			arrowFigure.setLeftPoint(0, lastVisibleLeftTreeItem.getBounds().y + lastVisibleLeftTreeItem.getBounds().height / 2);
			arrowFigure.setLeftMappingVisible(leftItemVisible);
		}
		
		{
			boolean rightItemVisible = true;
			TreeItem rightTreeItem = (TreeItem) rightTreeViewer.testFindItem(semanticSupport.resolveRightItem(mapping));
			if (rightTreeItem == null) {
//				LogfileView.log(MyTreeMapper.class," Could not find right entry of mapping  " 
//						+ mapping.toString() + " in right treeViewer.");
				return false;
			}
			TreeItem lastVisibleRightTreeItem = rightTreeItem;
			while (rightTreeItem.getParentItem() != null) {
				if (!rightTreeItem.getParentItem().getExpanded()) {
					lastVisibleRightTreeItem = rightTreeItem.getParentItem();
					rightItemVisible = false;
				}
				rightTreeItem = rightTreeItem.getParentItem();
			}
			arrowFigure.setRightPoint(linkRootFigure.getBounds().width, lastVisibleRightTreeItem.getBounds().y + rightTreeItem.getBounds().height / 2);
			arrowFigure.setRightMappingVisible(rightItemVisible);
		}
		
		arrowFigure.setLineWidth(uiConfig.getDefaultArrowWidth());
		arrowFigure.seLineColor(uiConfig.getDefaultMappingColor());
		arrowFigure.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent me) {
				fireMappingSelection(mapping, arrowFigure);
				TreeItem leftTreeItem = (TreeItem) leftTreeViewer.testFindItem(semanticSupport.resolveLeftItem(mapping));
				TreeItem rightTreeItem = (TreeItem) rightTreeViewer.testFindItem(semanticSupport.resolveRightItem(mapping));
				fireMousePressed(mapping, arrowFigure, leftTreeItem,  rightTreeItem);
			}
			public void mouseReleased(MouseEvent me) {
			}

			public void mouseDoubleClicked(MouseEvent me) {
				//if (arrowFigure.)
			}
		});
		arrowFigure.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) {
				fireMouseEntered(mapping, arrowFigure);
			}

			public void mouseExited(MouseEvent me) {
				fireMouseExited(mapping, arrowFigure);
			}

			public void mouseHover(MouseEvent me) {
			}

			public void mouseMoved(MouseEvent me) {
			}
			
		});
		// store it
		figuresToMappings.put(arrowFigure, mapping);
		mappingsToFigures.put(mapping, arrowFigure);
	
		return true;
	}

//	/**
//	 * @param treeViewer
//	 * @param treeItem
//	 * @return
//	 */
//	private Object resolveTreeViewerItem(TreeViewer treeViewer, TreeItem treeItem) {
//		//return treeItem.getData();
//		ITreeContentProvider contentProvider = (ITreeContentProvider) treeViewer.getContentProvider();
//		List<Integer> locations = new ArrayList<Integer>();
//		TreeItem parentTreeItem = treeItem.getParentItem();
//		while (parentTreeItem != null) {
//			int index = Arrays.asList(parentTreeItem.getItems()).indexOf(treeItem);
//			locations.add(index);
//			treeItem = parentTreeItem;
//			parentTreeItem = treeItem.getParentItem();
//		}
//		// root
//		if (treeItem != null) {
//			int rootIndex = Arrays.asList(treeViewer.getTree().getItems()).indexOf(treeItem);
//			locations.add(rootIndex);
//		}
//		Collections.reverse(locations);
//		Object current = contentProvider.getElements(treeViewer.getInput())[locations.get(0)];
//		locations.remove(0);
//		for (int index : locations) {
//			current = contentProvider.getChildren(current)[index];
//		}
//		return current;
//	}

	/**
	 * @return
	 */
	public SashForm getControl() {
		return control;
	}
	
	
	//
	// Selection management
	//
	
	private List<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();
	private IStructuredSelection currentSelection = new StructuredSelection();
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionChangedListeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public IStructuredSelection getSelection() {
		return currentSelection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		IStructuredSelection strSelection = (IStructuredSelection)selection;
		if (strSelection.isEmpty()) {
			currentSelection = new StructuredSelection();
			fireMouseExited(selectedMapping, mappingsToFigures.get(selectedMapping));
		} else {
			M mapping = (M) strSelection.getFirstElement();
			fireMappingSelection(mapping, mappingsToFigures.get(mapping));
		}
	}
	
	/**
	 * @param mapping
	 * @param arrowFigure
	 */
	protected void fireMappingSelection(M mapping, MyLinkFigure arrowFigure) {
		if (selectedFigure != null) {
			applyDefaultMappingStyle(selectedFigure);
		}
		applySelectedMappingFeedback(arrowFigure);
		selectedFigure = arrowFigure;
		selectedMapping = mapping;
		currentSelection = new StructuredSelection(selectedMapping);
		for (ISelectionChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(new SelectionChangedEvent(this, currentSelection));
		}
	}
	
	/**
	 * Select no item
	 */
	private void unselect() {
		selectedMapping = null;
		selectedFigure = null;
		currentSelection = new StructuredSelection();
		for (ISelectionChangedListener listener : selectionChangedListeners) {
			listener.selectionChanged(new SelectionChangedEvent(this, currentSelection));
		}
	}

	
	//
	// Creation management
	//
	
	private List<INewMappingListener<M>> creationListeners = new ArrayList<INewMappingListener<M>>();
	
	/**
	 * @param iNewMappingListener
	 */
	public void addNewMappingListener(INewMappingListener<M> listener) {
		this.creationListeners.add(listener);
	}
	
	
	
	
	/**
	 * 
	 */
	private void applyDefaultMappingStyle(MyLinkFigure figure) {
		figure.seLineColor(uiConfig.getDefaultMappingColor());
		figure.setLineWidth(uiConfig.getDefaultArrowWidth());
	}

	/**
	 * @param arrowFigure
	 */
	private void applySelectedMappingFeedback(MyLinkFigure arrowFigure) {
		
		arrowFigure.seLineColor(uiConfig.getSelectedMappingColor());
		arrowFigure.setLineWidth(uiConfig.getHoverArrowWidth());
	}

	/**
	 * @param mapping
	 * @param arrowFigure
	 */
	protected void fireMouseExited(M mapping, MyLinkFigure arrowFigure) {
		if (arrowFigure != selectedFigure) {
			applyDefaultMappingStyle(arrowFigure);
		}
	}

	/**
	 * @param mapping
	 * @param arrowFigure
	 */
	protected void fireMouseEntered(M mapping, MyLinkFigure arrowFigure) {
		if (arrowFigure != selectedFigure) {
			arrowFigure.setLineWidth(uiConfig.getHoverArrowWidth());
		}
	}

	/**
	 * @param mapping
	 * @param arrowFigure
	 * @param leftTreeItem
	 * @param rightTreeItem
	 */
	protected void fireMousePressed(M mapping,  MyLinkFigure arrowFigure, TreeItem leftTreeItem,  TreeItem rightTreeItem) {
		LogfileView.log(MyTreeMapper.class," Mouse pressed");
		MyTreeItem myLeftTreeItem = (MyTreeItem) semanticSupport.resolveLeftItem(mapping);
		MyTreeItem myRightTreeItem = (MyTreeItem) semanticSupport.resolveRightItem(mapping);
		MyUpdateNodeStructureEditor.deleteLink = true;
		MyUpdateNodeStructureEditor.updateDeleteButton(true, myLeftTreeItem, myRightTreeItem);
	
	}

	
	/**
	 * @return
	 */
	public TreeViewer getLeftTreeViewer() {
		return leftTreeViewer;
	}
	
	/**
	 * @return
	 */
	public TreeViewer getRightTreeViewer() {
		return rightTreeViewer;
	}

	public  void redraw(){
		refresh();
	}
	
	/**
	 * Refresh the widget by resetting the setInput value
	 */
	public void refresh() {
		setInput(leftTreeViewer.getInput(), rightTreeViewer.getInput(), mappings);
		if (!mappings.contains(selectedMapping)) {
			unselect();
		}
		leftTreeViewer.expandAll();
		leftTreeViewer.refresh();
		rightTreeViewer.expandAll();
		rightTreeViewer.refresh();
		canvasNeedRedraw = true;
		control.layout(true);
	}

	/**
	 * 
	 */
	private void clearFigures() {
		for (Entry<M, MyLinkFigure> entry : mappingsToFigures.entrySet()) {
			entry.getValue().deleteFromParent();
		}
		mappingsToFigures.clear();
		figuresToMappings.clear();
	}

}
