 
package com.hydra.project.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lifecycle.MyImageHandler;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import com.hydra.project.model.MyMessageModel;



public class LogfileView {
	private static TableViewer tableViewer;
	private static int index = 0;
	private static List<MyMessageModel> messages = new ArrayList<MyMessageModel>();
	private static boolean shutdown = false;
	
	@Inject
	private MDirtyable dirty;
	
	public LogfileView() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		MyMessageModel myModel = new MyMessageModel(0,"kein Eintrag", MyImageHandler.getImage("information.png"));
		messages.add(myModel);

		tableViewer = new TableViewer(parent, SWT.H_SCROLL| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, tableViewer);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setLinesVisible(true);
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setInput(messages);
	}
	
	
	/**
	 * Prints a message to the Logfile Viewer
	 * @param myMessage the massage
	 */
	public static void log(String myMessage){
		if (tableViewer != null & !shutdown) {  // keine Meldung wenn Fenster nicht existiert
			index++;
			MyMessageModel myModel = new MyMessageModel(index,myMessage, MyImageHandler.getImage("information.png"));
			messages.add(myModel);
			checkLengthOfList();
			tableViewer.setInput(messages);
			tableViewer.reveal(myModel);
		}
		System.out.println(myMessage);
	}
	
	/**
	 * Prints a message to the Logfile Viewer
	 * @param object the source of the message
	 * @param myMessage the massage
	 */
	public static void log(Object object, String myMessage){
		if (tableViewer != null & !shutdown) {  // keine Meldung wenn Fenster nicht existiert
			index++;
			index++;
			MyMessageModel myModel = new MyMessageModel(index,object.toString() + ": " + myMessage, MyImageHandler.getImage("information.png"));
			messages.add(myModel);
			checkLengthOfList();
			tableViewer.setInput(messages);
			tableViewer.reveal(myModel);
		}
		System.out.println(object.toString() + ": " + myMessage);
	}
	
	/**
	 * Prints a message to the Logfile Viewer
	 * @param object the source of the message
	 * @param myMessage the massage
	 * @param imagetype the Icon to show
	 * </p>
	 * Image Parameter SWT.ICON_INFORMATION, SWT.ICON_ERROR, SWT.ICON_WARNING
	 */
	public static void log(Object object, String myMessage, int imagetype){
		
		if (tableViewer != null & !shutdown) {  // keine Meldung wenn Fenster nicht existiert
			Image image;
			switch (imagetype) {
    		case SWT.ICON_INFORMATION:
    			image = MyImageHandler.getImage("information.png");
    			break;
    		case SWT.ICON_ERROR:
    			image = MyImageHandler.getImage("exclamation-red.png");
    			break;
    		case SWT.ICON_WARNING:
    			image = MyImageHandler.getImage("question-white.png");
    			break;
    		default:
    			image = MyImageHandler.getImage("information.png");
			}
			index++;
			MyMessageModel myModel = new MyMessageModel(index,object.toString() + ": " + myMessage, image);
			messages.add(myModel);
			checkLengthOfList();
			tableViewer.setInput(messages);
			tableViewer.reveal(myModel);
		}
		System.out.println(object.toString() + ": " + myMessage);
	}

	private static void checkLengthOfList(){
		if (messages.size() > 1000){
			for (int i = 0; i < 100; i++) {
				messages.remove(i);
			}
		}
	}
//	private void list(){
//		for (int i = 0; i < messages.size(); i++) { 
//			System.out.println(messages.get(i).getMessage());
//		}
//	}
	
	 // create the columns for the table
	  private void createColumns(final Composite parent, final TableViewer viewer) {
	    String[] titles = { "Nr.", "Message"};
	    int[] bounds = { 50, 800};

	    // first column is for the position
	    TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    	  MyMessageModel p = (MyMessageModel) element;
	        return p.getPos();
	      }
	    });

	    // second column is for the icon and message
	    col = createTableViewerColumn(titles[1], bounds[1], 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	      @Override
	      public Image getImage(Object element) {
	    	  MyMessageModel p = (MyMessageModel) element;
	        return p.getImage();
	      }
	      
	    	@Override
	    	public String getText(Object element) {
	    	  MyMessageModel p = (MyMessageModel) element;
	        return p.getMessage();
	      }
	    });

	  }
	  private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		    final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		    final TableColumn column = viewerColumn.getColumn();
		    column.setText(title);
		    column.setWidth(bound);
		    column.setResizable(true);
		    column.setMoveable(true);
		    return viewerColumn;
	  }

	  public void setFocus() {
		  tableViewer.getControl().setFocus();
	  }
	  
	  private class MyArrayContentProvider implements  IStructuredContentProvider {

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return null;
		}
		  
	  }

	/**
	 * @param shutdown the shutdown to set
	 */
	public static void setShutdown(boolean shutdown) {
		LogfileView.shutdown = shutdown;
	}
}