package com.hydra.project.provider;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hydra.project.model.MyHours;


public class HoursTableLabelProvider implements ITableLabelProvider{
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof MyHours){
				return getImage(element,  columnIndex);
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		
		if(element instanceof MyHours) {
			MyHours myHours =(MyHours) element;
			
			//Datumsausgabe formatieren
			DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			String buchungsdatum = formatter.format( myHours.getBuchungsdatum());
			
			switch (columnIndex){
			case 0: return (myHours.getBuchungsdatum() == null) ? "" : buchungsdatum;
			case 1: return (myHours.getName() == null) ? "" : myHours.getStatus();
			case 2: return (myHours.getName() == null) ? "" : myHours.getName();
			case 3: return (myHours.getNummer() == null) ? "" : myHours.getNummer();
			case 4: return (myHours.getMenge() == 0) ? "0.0" : String.valueOf(myHours.getMenge());
			case 5: return (myHours.getAufgabe() == null) ? "" : myHours.getAufgabe();
			case 6: return (myHours.getProjekt() == null) ? "" : myHours.getProjekt();
			case 7: return (myHours.getBeschreibung() == null) ? "" : myHours.getBeschreibung();
			case 8: return (myHours.getKostenstelle() == null) ? "" : myHours.getKostenstelle();
			case 9: return (myHours.getKostentraeger() == null) ? "" : myHours.getKostentraeger();
			case 10: return (myHours.getGeändertDurch() == null) ? "" : myHours.getGeändertDurch();
			}	
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
		Image image = null;	// hole den Namen des Bildes
//		if (element instanceof MyHours){
//			MyHours myHours =(MyHours) element;
//
//			switch (columnIndex){
//			case 0: image = myHours.getImage();break;
//			}
//		}		
		return image;		
	}

}