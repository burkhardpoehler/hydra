/**
 * 
 */
package com.hydra.project.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hydra.project.database.DBProjekteTools;
import com.hydra.project.database.DBStundenTools;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.parts.ProjectCalendarView;
import com.hydra.project.provider.HoursTableLabelProvider;
import com.hydra.project.model.AktiveHourFilter;
import com.hydra.project.database.DBMitarbeiterTools;
import com.hydra.project.editors.ComboBoxTools;
/**
 * 
 * Unterstützt die Erstellung und Funktion des Vorgangstabelle im KalenderView
 * 
 * @author Poehler
 *
 */
public class MyVorgangTools {

	private static String thisClass ="MyVorgangTools";
	private static TableViewer tableVorgangViewer;
	private static ScrolledComposite scrolledComposite;
	private static Composite tabelleComposite;
	private static int anzahlMonate =1;
	private static TableViewer tableViewer;
	private static AktiveHourFilter hourFilter;
	private static Composite cmpFilter;
	private static String nameFilter = "kein Filter";
	private static String vorgangFilter = "kein Filter";
	private static String auftragsnummerFilter = "kein Filter";
	private static String kostenträgerFilter = "kein Filter";
	private static String kostenstelleFilter = "kein Filter";
	private static Boolean alleStundenAnzeigen = true;
	
	
	public MyVorgangTools() {
		thisClass= this.getClass().toString();
	}

	/**
	 * erzeugt die Elemente für den Filterbereich
	 * @param cmp das Composite
	 * @author Poehler
	 */
	public static void stundenFilterbereichErstellen(Composite cmp){
		cmpFilter = cmp;
		
		
		Label lblName = new Label(cmpFilter, SWT.NONE);
		lblName.setBounds(20, 9, 32, 15);
		lblName.setText("Name");
		
		Label lblVorgang = new Label(cmpFilter, SWT.NONE);
		lblVorgang.setBounds(20, 40, 45, 15);
		lblVorgang.setText("Vorgang");
		
		Label lblProjekt_1 = new Label(cmpFilter, SWT.NONE);
		lblProjekt_1.setBounds(20, 73, 55, 15);
		lblProjekt_1.setText("Projekt");
		
		Label lblKostentraeger1 = new Label(cmpFilter, SWT.NONE);
		lblKostentraeger1.setBounds(250, 9, 81, 15);
		lblKostentraeger1.setText("Kostentr\u00E4ger");
		
		Label lblKostenstelle = new Label(cmpFilter, SWT.NONE);
		lblKostenstelle.setBounds(250, 40, 70, 15);
		lblKostenstelle.setText("Kostenstelle");
		
		Label lblDatum = new Label(cmpFilter, SWT.NONE);
		lblDatum.setBounds(250, 73, 36, 15);
		lblDatum.setText("Datum");
		
		Label lblStunden = new Label(cmpFilter, SWT.NONE);
		lblStunden.setBounds(514, 9, 44, 15);
		lblStunden.setText("Stunden");
		
		final String[] mitarbeiter =  DBMitarbeiterTools.getMitarbeiter();
		Combo comboName = new Combo(cmpFilter, SWT.NONE);
		comboName.setBounds(78, 6, 136, 23);
		comboName.add(nameFilter);			//Standardwert immer an oberster Stelle
		comboName.setItems(mitarbeiter);	//lade alle Mitarbeiter
		comboName.select(getIndex(mitarbeiter, ProjectCalendarView.getNameFilter()));	//selektiere ausgewählten Mitarbeiter
		comboName.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Filter 'Name' wurde geändert in:" + t.getText());
				ProjectCalendarView.setNameFilter(mitarbeiter[t.getSelectionIndex()]);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
				
		
		final String[] vorgang =  ProjectCalendarView.getVorgänge();
		Combo comboVorgang = new Combo(cmpFilter, SWT.NONE);
		comboVorgang.setBounds(78, 37, 136, 23);
		comboVorgang.add(vorgangFilter);			//Standardwert immer an oberster Stelle
		if (vorgang != null) comboVorgang.setItems(vorgang);	//lade alle Vorgänge
		comboVorgang.select(getIndex(vorgang, ProjectCalendarView.getNameFilter()));	//selektiere ausgewählten Vorgang
		comboVorgang.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Filter 'Vorgang' wurde geändert in:" + t.getText());
				ProjectCalendarView.setVorgangFilter(vorgang[t.getSelectionIndex()]);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		final String[] auftragsnummern =  DBProjekteTools.getAuftragsnummern();
		Combo comboProjekt = new Combo(cmpFilter, SWT.NONE);
		comboProjekt.setBounds(78, 70, 136, 23);
		comboProjekt.add(auftragsnummerFilter);			//Standardwert immer an oberster Stelle
		comboProjekt.setItems(auftragsnummern);	//lade alle Auftragsnummern
		comboProjekt.select(getIndex(auftragsnummern, ProjectCalendarView.getProjektFilter()));	//selektiere ausgewählte Auftragsnummer
		comboProjekt.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Filter 'Auftragsnummer' wurde geändert in:" + t.getText());
				ProjectCalendarView.setNameFilter(auftragsnummern[t.getSelectionIndex()]);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		final String[] kostenträger =  DBProjekteTools.getKostenträger();
		Combo comboKostenträger = new Combo(cmpFilter, SWT.NONE);
		comboKostenträger.setBounds(340, 6, 128, 23);
		comboKostenträger.add(kostenträgerFilter);			//Standardwert immer an oberster Stelle
		comboKostenträger.setItems(kostenträger);	//lade alle Kostenträger
		comboKostenträger.select(getIndex(kostenträger, ProjectCalendarView.getKostenträgerFilter()));	//selektiere ausgewählten Kostenträger
		comboKostenträger.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Filter 'Kostenträger' wurde geändert in:" + t.getText());
				ProjectCalendarView.setNameFilter(kostenträger[t.getSelectionIndex()]);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		final String[] kostenstelle =  DBProjekteTools.getKostenstellen();
		Combo comboKostenstelle = new Combo(cmpFilter, SWT.NONE);
		comboKostenstelle.setBounds(340, 37, 128, 23);
		comboKostenstelle.add(kostenstelleFilter);			//Standardwert immer an oberster Stelle
		comboKostenstelle.setItems(kostenstelle);	//lade alle Kostenstelle
		comboKostenstelle.select(getIndex(kostenstelle, ProjectCalendarView.getKostenstelleFilter()));	//selektiere ausgewählten Kostenstelle
		comboKostenstelle.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Filter 'Kostenstelle' wurde geändert in:" + t.getText());
				ProjectCalendarView.setNameFilter(kostenstelle[t.getSelectionIndex()]);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		//Datumsausgabe formatieren
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		String datum = formatter.format(ProjectCalendarView.getDateDatum());
		Text textDatum = new Text(cmpFilter, SWT.BORDER);
		textDatum.setBounds(340, 67, 128, 21);
		textDatum.setEnabled(false);
		textDatum.setEditable(false);
		textDatum.setText(datum);


		Text txtStunden = new Text(cmpFilter, SWT.BORDER);
		txtStunden.setBounds(768, 7, 76, 21);
		txtStunden.setBounds(564, 6, 45, 21);
		txtStunden.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Stunden wurde geändert in:" + t.getText());
				ProjectCalendarView.updateTreeItem(ProjectCalendarView.STUNDEN, t.getText());
			}		
		});
		
		Button btnVorgangUebernehmen = new Button(cmpFilter, SWT.NONE);
		btnVorgangUebernehmen.setBounds(710, 4, 128, 25);
		btnVorgangUebernehmen.setText("Vorgang \u00FCbernehmen");
		btnVorgangUebernehmen.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		Button btnVorgangLoeschen = new Button(cmpFilter, SWT.NONE);
		btnVorgangLoeschen.setBounds(710, 35, 127, 25);
		btnVorgangLoeschen.setText("Vorgang l\u00F6schen");
		btnVorgangLoeschen.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		final Button btnAlleAnzeigen = new Button(cmpFilter, SWT.TOGGLE);
		btnAlleAnzeigen.setBounds(479, 68, 94, 20);
		btnAlleAnzeigen.setText("Alle anzeigen");
		btnAlleAnzeigen.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
					if (btnAlleAnzeigen.getSelection()) {
						alleStundenAnzeigen =false;
						btnAlleAnzeigen.setText("Alle anzeigen");
						MyVorgangTools.updateViewer();
					}else{
						alleStundenAnzeigen =true;
						btnAlleAnzeigen.setText("Tagesfilter aktiv");
						MyVorgangTools.updateViewer();
					}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});

	}
	
	/**
	 * Unterstützt Combobox
	 * liefert für einen Wert den Index aus einer Liste
	 * @param value der gesuchte Wert
	 * @param liste eine Stringarray
	 * @return index der gefundene Index. 0 wenn nicht gefunden.
	 * @author Poehler
	 */
	public static int getIndex(String[] liste, String value){
		int index = 0;
		for (int i = 0; i < liste.length; i++) {
			if (liste[i].equals(value)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	
	
	/**
	 * erzeugt die Tabelle für die Stundenübersicht
	 * @author Poehler
	 */
	public static void stundenTabelleErstellen(){
		tabelleComposite = ProjectCalendarView.cmpKalenderTabelle;
		tableViewer = new TableViewer(tabelleComposite, SWT.H_SCROLL| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		TableViewerColumn colum[] = new TableViewerColumn[15];
		int i=0;
		
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(80);
		colum[i].getColumn().setText("Datum");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(40);
		colum[i].getColumn().setText("Status");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(150);
		colum[i].getColumn().setText("Name");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(60);
		colum[i].getColumn().setText("Nummer");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(50);
		colum[i].getColumn().setText("Menge");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(100);
		colum[i].getColumn().setText("Aufgabe");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(100);
		colum[i].getColumn().setText("Projekt");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(100);
		colum[i].getColumn().setText("Beschreibung");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(80);
		colum[i].getColumn().setText("Kostenstelle");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(80);
		colum[i].getColumn().setText("Kostenträger");
		i++;
		
		colum[i] = new TableViewerColumn(tableViewer, SWT.NONE);
		colum[i].getColumn().setWidth(100);
		colum[i].getColumn().setText("Bearbeiter");
		i++;
		
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new HoursTableLabelProvider());
		tableViewer.addFilter(new AktiveHourFilter());
		
		
		if (DBStundenTools.readStundenDB() != null) {
			tableViewer.setInput(DBStundenTools.readStundenDB());
		}

	}
	
	
	/**
	 * aktualisiert die Stunden Tabelle
	 * @author Poehler
	 */
	public static void updateViewer(){
		tableViewer.refresh();
	}
	
	/**
	 * erzeugt die Kalenderzeile
	 * @author Poehler
	 */
	public static void kalenderErstellen(){
		anzahlMonate = ProjectCalendarView.getProjektDauerInMonaten();
		scrolledComposite = ProjectCalendarView.scrolledCompositeKalender;
		
		Date start = new Date();
		Date end = new Date();
		if (ProjectCalendarView.getDateChooserStart()!=null) {
			start = ProjectCalendarView.getDateChooserStart().getSelectedDate();
			end = ProjectCalendarView.getDateChooserEnd().getSelectedDate();
		}
		Date run = start;
		Calendar calendarStart = new GregorianCalendar();
		calendarStart.setTime(start);
		Calendar calendarEnd = new GregorianCalendar();
		calendarEnd.setTime(end);

		scrolledComposite.setExpandHorizontal(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		LogfileView.log("MyVorgangTools","Kalender erstellen mit " + anzahlMonate + " Monaten");	
		final DateChooser dateChooser[] = new DateChooser[anzahlMonate+1];

		for (int i = 0; i < anzahlMonate + 1 ; i++) {
			dateChooser[i] = new DateChooser(composite, SWT.NONE);
			dateChooser[i].setCurrentMonth(run);
			dateChooser[i].setWeeksVisible(true);
			dateChooser[i].setNavigationEnabled(false);
			dateChooser[i].setAutoChangeOnAdjacent(false);
			calendarStart.add(Calendar.MONTH, 1); // nächsten Monat generieren
			run = calendarStart.getTime();
			

			dateChooser[i].addSelectionListener(new SelectionListener(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					DateChooser t = (DateChooser) e.getSource();
					LogfileView.log(thisClass,"Auswahltermin wurde geändert in:" + t.getSelectedDate());			
					handleSelection(dateChooser, t.getSelectedDate());
					ProjectCalendarView.updateTreeItemDate(ProjectCalendarView.DATUM, t.getSelectedDate());
					MyVorgangTools.updateViewer();
					}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					}		
			});

		}
		//setze das aktuelle gespeicherte Datum
		setDate(dateChooser, ProjectCalendarView.getDateDatum());
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	/**
	 * in den angezeigten Kalendern darf nur ein Feld selektiert sein
	 * Diese Routine sorgt dafür.
	 * @author Poehler
	 * @param dateChooser Array auf alle Kalender
	 * @param date das ausgewählte Datum
	 */
	private static void handleSelection(DateChooser[] dateChooser, Date date){

		if (date != null) {
			for (int i = 0; i < dateChooser.length; i++) {

				if (dateChooser[i].isDateSelected(date)) {

				} else {
					dateChooser[i].clearSelection();
				}

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				Calendar calendarSelected = new GregorianCalendar();
				calendarSelected.setTime(dateChooser[i].getCurrentMonth());
				int month = calendar.get(Calendar.MONTH);
				int monthSelected = calendarSelected.get(Calendar.MONTH);

				if (month == monthSelected) { //selektierter Monat stimmt mit dem gepsiecherten Monat überein
					dateChooser[i].setSelectedDate(date);
				}

			}
		}	
	}
	
	/**
	 * setzt das aktuell gespeicherte Datum
	 * @author Poehler
	 * @param dateChooser Array auf alle Kalender
	 * @param date das ausgewählte Datum
	 */
	private static void setDate(DateChooser[] dateChooser, Date date){

		if (date != null) {
			if (dateChooser != null) {
				for (int i = 0; i < dateChooser.length; i++) {

					if (dateChooser[i].getCurrentMonth() != null) {
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(date);
						Calendar calendarSelected = new GregorianCalendar();
						calendarSelected.setTime(dateChooser[i]
								.getCurrentMonth());
						int month = calendar.get(Calendar.MONTH);
						int monthSelected = calendarSelected
								.get(Calendar.MONTH);
						if (month == monthSelected) { //selektierter Monat stimmt mit dem gepsiecherten Monat überein
							dateChooser[i].setSelectedDate(date);
						}
					}

				}
			}
		}	
	}
	
	public static void updateVorgangstabelle(){
		
	}
	
}
