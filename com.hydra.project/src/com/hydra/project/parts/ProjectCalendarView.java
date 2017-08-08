package com.hydra.project.parts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import lifecycle.LifeCycleManager;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.nebula.widgets.datechooser.DateChooser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

import com.hydra.project.database.DBMitarbeiterTools;
import com.hydra.project.database.DBUserSettings;
import com.hydra.project.editors.ComboBoxTools;
import com.hydra.project.model.AktiveTasksFilter;
import com.hydra.project.model.MyMitarbeiter;
import com.hydra.project.model.MyUserSettings;
import com.hydra.project.model.MyTasksModel;
import com.hydra.project.model.MyTreeItem;
import com.hydra.project.model.MyViewers;
import com.hydra.project.model.MyVorgangTools;
import com.hydra.project.model.TasksTools;
import com.hydra.project.model.TreeTools;
import com.hydra.project.nebula.DateChooser;

public class ProjectCalendarView {


	private static String thisClass;
	private static Text text_2;
	private static Text text_3;
	private Table tableTask;
	private static TableViewer tableTasksViewer;
	private static Text text_Summe_Stunden;
	private Table table_1;
	private Text text_AufgelaufeneStunden;
	private Text text_KalkulierteStunden;
	private Boolean flagNurAktive = false;
	private FocusListener txtFocusListener;
	
	// die verwendeten Parameter
	private static final String ZEITERFASSUNG 			= "P091.001.004";
	private static final String PROJEKT		 			= "P071.002.001";
	private static final String PROJEKTBESCHREIBUNG		= "P071.002.002";
	private static final String AUFTRAGSNUMMER	 		= "P071.002.003";
	private static final String KOSTENTRÄGER		 	= "P071.002.004";
	private static final String AUFGABE					= "P071.002.005";
	private static final String PROJEKTMANAGER	 		= "P071.002.006";
	private static final String KOMMENTAR			 	= "P071.002.007";
	private static final String BEARBEITER			 	= "P071.002.008";
	
	private static final String KALKULIERTESTUNDEN		= "P061.003.002";
	private static final String AUFGELAUFENESTUNDEN		= "P061.003.003";
	private static final String GEPLANTESTUNDEN			= "P061.003.004";
	private static final String ARBEITSDAUERMONTAG		= "P061.003.005";
	private static final String ARBEITSDAUERDIENSTAG	= "P061.003.006";
	private static final String ARBEITSDAUERMITTWOCH	= "P061.003.007";
	private static final String ARBEITSDAUERDONNERSTAG	= "P061.003.008";
	private static final String ARBEITSDAUERFREITAG		= "P061.003.009";
	private static final String ARBEITSDAUERSAMSTAG		= "P061.003.010";
	private static final String ARBEITSDAUERSONNTAG		= "P061.003.011";
	
	public static final String DATUM					= "P061.002.001";
	private static final String STARTTERMIN				= "P061.002.002";
	private static final String ENDTERMIN				= "P061.002.003";
	
	private static final String VORGANG					= "P061.004.001";
	public static final String STUNDEN					= "P061.003.014";
		
	private static final String TASK					= "P061.004.002";
	private static final String ERLEDIGT				= "P061.004.003";
	private static final String FAHNE					= "P061.004.004";
	private static final String TYP						= "P061.004.005";
	private static final String AKTIVETASKS				= "P061.004.006";
	private static final String BESCHREIBUNG			= "P071.001.001";
	private static final String DAUER					= "P061.003.001";
	private static final String ARBEITSSCHRITTE			= "P081.002.001";
	private static final String ARBEITSSCHRITT			= "P081.002.002";
	
	private static final String NAME_FILTER				= "P071.003.001";
	private static final String VORGANG_FILTER			= "P071.003.002";
	private static final String PROJEKT_FILTER			= "P071.003.003";
	private static final String KOSTENTRÄGER_FILTER		= "P071.003.004";
	private static final String KOSTENSTELLE_FILTER		= "P071.003.005";
	
	private MDirtyable dirty;
	private static Combo comboProjekt;
	private static Text textProjektbeschreibung;
	private static Combo comboAuftragsnummer;
	private static Combo comboKostentraeger;
	private static Text txtAufgabe;
	private static Text textProjektmanager;
	private static StyledText styledTextKommentar;
	private static Combo comboBearbeiter;
	private static Text txtKalkulierteStunden;
	private static Text txtAufgelaufeneStunden;
	private static Text txtGeplanteStunden;
	private static Text textGeplanteStunden1;
	private static Text txtArbeitsdauerMontag;
	private static Text txtArbeitsdauerDienstag;
	private static Text txtArbeitsdauerMittwoch;
	private static Text txtArbeitsdauerDonnerstag;
	private static Text txtArbeitsdauerFreitag;
	private static Text txtArbeitsdauerSamstag;
	private static Text txtArbeitsdauerSonntag;
	private static String txtAktuelleDatum = "";
	
	private static Combo comboVorgang;
	private static Text txtStunden;
	
	private static Date dateDatum = new Date();
	private static DateChooser dateChooserStart;
	private static DateChooser dateChooserEnd;
	private static Button btnNurAktive;
	
	private static int projektDauerInMonaten = 1;

	private static Date dateStarttermin = new Date();
	private static Date dateEndtermin = new Date();
	
	private static ArrayList<MyTasksModel> tasks = new ArrayList<MyTasksModel>();
	public static ScrolledComposite scrolledCompositeKalender;
	public static Composite cmpKalenderTabelle;
	private static Text textDatum;
	private static Boolean AlleStundenAnzeigen = true;
	
	private static String nameFilter = "kein Filter";
	private static String vorgangFilter = "kein Filter";
	private static String projektFilter = "kein Filter";
	private static String kostenträgerFilter = "kein Filter";
	private static String kostenstelleFilter = "kein Filter";
	private static String[] vorgänge;
	
	// Variablen für die Viewerdarstellung
	static MyTreeItem mySelectedTreeItem = null;
	private static final String VIEWERPARAMETERID ="P101.001.002";
	private static MyTreeItem myTopEntryTreeItem = null;
	private static Boolean viewerFullActive = false;
	private static Boolean viewerEmptyActive = false;
	private static Composite firstParent;
	private static Composite parentFull;
	private static Composite parentEmpty;
	
	
//	private static List arrayListVorgang =new ArrayList<String>();
	private static ArrayList<String> arrayListVorgang =new ArrayList<String>();

	private static  MyTreeItem myZeiterfassungTreeItem = null;		//oberster Knoten der Zeiterfassung

	@Inject
	public ProjectCalendarView() {
		thisClass= this.getClass().toString();
	}

	@Inject
	static
	IEventBroker eventBroker;
	

//	@Inject
//	private ESelectionService selectionService;
//	
	@Inject
//	Viewer viewer;

	
	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	private static  void createControls(Composite parent) {
		firstParent = parent;
		if (!viewerFullActive){
			createViewer(parent);
			viewerFullActive = true;
		}

	}
	

	private static void createViewer(Composite parent) {
		
//		parent.setLayout(new BorderLayout(0, 0));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setSize(822, 472);
		scrolledComposite.setLocation(0, 0);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		

		CTabFolder tabFolder = new CTabFolder(scrolledComposite, SWT.BORDER);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		

		CTabItem tbtmAuswertung = new CTabItem(tabFolder, SWT.NONE);
		tbtmAuswertung.setText("Auswertung");
		
		Composite cmpAuswertung = new Composite(tabFolder, SWT.NONE);
		tbtmAuswertung.setControl(cmpAuswertung);
		cmpAuswertung.setLayout(new BorderLayout(0, 0));
		

		Composite cmpAuswertungOben = new Composite(cmpAuswertung, SWT.NONE);
		cmpAuswertungOben.setLayoutData(BorderLayout.NORTH);
		cmpAuswertungOben.setLayout(null);
		
		List listAufgaben = new List(cmpAuswertungOben, SWT.BORDER);
		listAufgaben.setBounds(22, 26, 165, 93);
		
		List listMitarbeiter = new List(cmpAuswertungOben, SWT.BORDER);
		listMitarbeiter.setBounds(307, 26, 157, 93);
		
		Label lblAuswertungVorgang = new Label(cmpAuswertungOben, SWT.NONE);
		lblAuswertungVorgang.setBounds(22, 5, 110, 15);
		lblAuswertungVorgang.setText("Vorgang");
		
		Label lblMitarbeiter = new Label(cmpAuswertungOben, SWT.NONE);
		lblMitarbeiter.setBounds(307, 5, 87, 15);
		lblMitarbeiter.setText("Mitarbeiter");
		
		Label lblAufgelaufeneStunden = new Label(cmpAuswertungOben, SWT.NONE);
		lblAufgelaufeneStunden.setBounds(637, 61, 126, 15);
		lblAufgelaufeneStunden.setText("Aufgelaufene Stunden");
		
		txtAufgelaufeneStunden = new Text(cmpAuswertungOben, SWT.BORDER);
		txtAufgelaufeneStunden.setEnabled(false);
		txtAufgelaufeneStunden.setEditable(false);
		txtAufgelaufeneStunden.setBounds(768, 61, 76, 21);
		txtAufgelaufeneStunden.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"AufgelaufeneStunden wurde geändert in:" + t.getText());
				updateTreeItem(AUFGELAUFENESTUNDEN, t.getText());
			}		
		});
		
		Label lblGeplanteStunden_1 = new Label(cmpAuswertungOben, SWT.NONE);
		lblGeplanteStunden_1.setText("Geplante Stunden");
		lblGeplanteStunden_1.setBounds(637, 88, 126, 15);
		
		textGeplanteStunden1 = new Text(cmpAuswertungOben, SWT.BORDER);
		textGeplanteStunden1.setEnabled(false);
		textGeplanteStunden1.setEditable(false);
		textGeplanteStunden1.setBounds(768, 88, 76, 21);
		textGeplanteStunden1.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"GeplanteStunden wurde geändert in:" + t.getText());
				updateTreeItem(GEPLANTESTUNDEN, t.getText());
			}		
		});
		
		Label lblKalkulierteStunden = new Label(cmpAuswertungOben, SWT.NONE);
		lblKalkulierteStunden.setText("Kalkulierte Stunden");
		lblKalkulierteStunden.setBounds(637, 34, 126, 15);
		
		txtKalkulierteStunden = new Text(cmpAuswertungOben, SWT.BORDER);
		txtKalkulierteStunden.setEnabled(false);
		txtKalkulierteStunden.setEditable(false);
		txtKalkulierteStunden.setBounds(768, 34, 76, 21);
		txtKalkulierteStunden.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"KalkulierteStunden wurde geändert in:" + t.getText());
				updateTreeItem(KALKULIERTESTUNDEN, t.getText());
			}		
		});
		
		Composite cmpAuswertungUnten = new Composite(cmpAuswertung, SWT.NONE);
		cmpAuswertungUnten.setLayoutData(BorderLayout.SOUTH);
		cmpAuswertungUnten.setLayout(null);
		
		Button btnAktualisieren = new Button(cmpAuswertungUnten, SWT.NONE);
		btnAktualisieren.setBounds(825, 29, 75, 25);
		btnAktualisieren.setText("Aktualisieren");
		
		Button btnAusgabe = new Button(cmpAuswertungUnten, SWT.NONE);
		btnAusgabe.setBounds(735, 29, 75, 25);
		btnAusgabe.setText("Ausgabe");
		
		Composite cmpAuswertungMitte = new Composite(cmpAuswertung, SWT.NONE);
		cmpAuswertungMitte.setLayoutData(BorderLayout.CENTER);
		cmpAuswertungMitte.setLayout(new BorderLayout(0, 0));
		
		ScrolledComposite scmpAuswertungFahnen = new ScrolledComposite(cmpAuswertungMitte, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scmpAuswertungFahnen.setLayoutData(BorderLayout.NORTH);
		scmpAuswertungFahnen.setExpandHorizontal(true);
		scmpAuswertungFahnen.setExpandVertical(true);
		
		Canvas canvasFahnen = new Canvas(scmpAuswertungFahnen, SWT.NONE);
		scmpAuswertungFahnen.setContent(canvasFahnen);
		scmpAuswertungFahnen.setMinSize(canvasFahnen.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		ProgressBar progressBar = new ProgressBar(cmpAuswertungMitte, SWT.NONE);
		progressBar.setLayoutData(BorderLayout.SOUTH);
		
		ScrolledComposite scmpAuswertungBalken = new ScrolledComposite(cmpAuswertungMitte, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scmpAuswertungBalken.setLayoutData(BorderLayout.CENTER);
		scmpAuswertungBalken.setExpandHorizontal(true);
		scmpAuswertungBalken.setExpandVertical(true);
		
		Canvas canvasBalken = new Canvas(scmpAuswertungBalken, SWT.NONE);
		scmpAuswertungBalken.setContent(canvasBalken);
		scmpAuswertungBalken.setMinSize(canvasBalken.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		CTabItem tbtmTask = new CTabItem(tabFolder, SWT.NONE);
		tbtmTask.setText("Tasks");
		
		Composite cmpTasks = new Composite(tabFolder, SWT.NONE);
		tbtmTask.setControl(cmpTasks);
		cmpTasks.setLayout(new BorderLayout(0, 0));
		
		Composite cmpTasksOben = new Composite(cmpTasks, SWT.NONE);
		cmpTasksOben.setLayoutData(BorderLayout.NORTH);
		
	
		btnNurAktive = new Button(cmpTasksOben, SWT.CHECK);
		btnNurAktive.setBounds(10, 10, 182, 16);
		btnNurAktive.setText("Nur aktivierte Tasks zeigen");
		btnNurAktive.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				LogfileView.log(thisClass,"Nur Aktive wurde geändert in:" + btnNurAktive.getSelection());
				updateTreeItem(AKTIVETASKS, String.valueOf(btnNurAktive.getSelection()));
				AktiveTasksFilter.setFilterActive(btnNurAktive.getSelection());
				TasksTools.TabelleAktualisieren();
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});


		Composite cmpTasksUnten = new Composite(cmpTasks, SWT.NONE);
		cmpTasksUnten.setLayoutData(BorderLayout.SOUTH);
		
		Label lblNewLabel_4 = new Label(cmpTasksUnten, SWT.NONE);
		lblNewLabel_4.setBounds(31, 7, 107, 15);
		lblNewLabel_4.setText("Summe Stunden");
		
		text_Summe_Stunden = new Text(cmpTasksUnten, SWT.BORDER);
		text_Summe_Stunden.setEnabled(false);
		text_Summe_Stunden.setEditable(false);
		text_Summe_Stunden.setBounds(144, 4, 76, 21);
		
		Button btnUebernehmenTask = new Button(cmpTasksUnten, SWT.NONE);
		btnUebernehmenTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			}
		});
		btnUebernehmenTask.setBounds(691, 7, 75, 25);
		btnUebernehmenTask.setText("\u00DCbernehmen");
			
		TasksTools.TabelleErzeugen(cmpTasks);
		
		CTabItem tbtmKalender = new CTabItem(tabFolder, SWT.NONE);
		tbtmKalender.setText("Kalender");
		
		Composite cmpKalender = new Composite(tabFolder, SWT.NONE);
		tbtmKalender.setControl(cmpKalender);
		cmpKalender.setLayout(new BorderLayout(0, 0));
		
		Composite cmpKalenderOben = new Composite(cmpKalender, SWT.NONE);
		cmpKalenderOben.setLayoutData(BorderLayout.NORTH);
		cmpKalenderOben.setLayout(null);
		
		// Erstellt alle Elemente für das Filter
		MyVorgangTools.stundenFilterbereichErstellen(cmpKalenderOben);
//#######################################################//test		
		
		Label lblName = new Label(cmpKalenderOben, SWT.NONE);
		lblName.setBounds(20, 9, 32, 15);
		lblName.setText("Name");
		
		Combo comboName = new Combo(cmpKalenderOben, SWT.NONE);
		comboName.setBounds(78, 6, 136, 23);
		
		Label lblVorgang = new Label(cmpKalenderOben, SWT.NONE);
		lblVorgang.setBounds(20, 40, 45, 15);
		lblVorgang.setText("Vorgang");
		
		comboVorgang = new Combo(cmpKalenderOben, SWT.NONE);
		comboVorgang.setBounds(78, 37, 136, 23);
		comboVorgang.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Vorgang wurde geändert in:" + t.getText());
				updateTreeItem(VORGANG, t.getText());
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Label lblStunden = new Label(cmpKalenderOben, SWT.NONE);
		lblStunden.setBounds(514, 9, 44, 15);
		lblStunden.setText("Stunden");

		txtStunden = new Text(cmpKalenderOben, SWT.BORDER);
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
				updateTreeItem(STUNDEN, t.getText());
			}		
		});
		
		Button btnVorgangUebernehmen = new Button(cmpKalenderOben, SWT.NONE);
		btnVorgangUebernehmen.setBounds(710, 4, 128, 25);
		btnVorgangUebernehmen.setText("Vorgang \u00FCbernehmen");

		Button btnVorgangLoeschen = new Button(cmpKalenderOben, SWT.NONE);
		btnVorgangLoeschen.setBounds(710, 35, 127, 25);
		btnVorgangLoeschen.setText("Vorgang l\u00F6schen");

		final Button btnAlleAnzeigen = new Button(cmpKalenderOben, SWT.TOGGLE);
		btnAlleAnzeigen.setBounds(479, 68, 94, 20);
		btnAlleAnzeigen.setText("Alle anzeigen");
		btnAlleAnzeigen.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
					if (btnAlleAnzeigen.getSelection()) {
						setAlleStundenAnzeigen(false);
						btnAlleAnzeigen.setText("Alle anzeigen");
						MyVorgangTools.updateViewer();
					}else{
						setAlleStundenAnzeigen(true);
						btnAlleAnzeigen.setText("Tagesfilter aktiv");
						MyVorgangTools.updateViewer();
					}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});

		
		
		Label lblDatum = new Label(cmpKalenderOben, SWT.NONE);
		lblDatum.setBounds(253, 73, 36, 15);
		lblDatum.setText("Datum");
		textDatum = new Text(cmpKalenderOben, SWT.BORDER);
		textDatum.setBounds(340, 67, 128, 21);
		textDatum.setEnabled(false);
		textDatum.setEditable(false);
		textDatum.setText(txtAktuelleDatum);
		
		Label lblProjekt_1 = new Label(cmpKalenderOben, SWT.NONE);
		lblProjekt_1.setBounds(20, 73, 55, 15);
		lblProjekt_1.setText("Projekt");
		
		Combo comboProjekt_1 = new Combo(cmpKalenderOben, SWT.NONE);
		comboProjekt_1.setBounds(78, 70, 136, 23);
		
		Label lblKostentraeger1 = new Label(cmpKalenderOben, SWT.NONE);
		lblKostentraeger1.setBounds(253, 9, 81, 15);
		lblKostentraeger1.setText("Kostentr\u00E4ger");
		
		Label lblKostenstelle = new Label(cmpKalenderOben, SWT.NONE);
		lblKostenstelle.setBounds(251, 40, 70, 15);
		lblKostenstelle.setText("Kostenstelle");
		
		Combo comboKostenträger = new Combo(cmpKalenderOben, SWT.NONE);
		comboKostenträger.setBounds(340, 6, 128, 23);
		
		Combo comboKostenstelle = new Combo(cmpKalenderOben, SWT.NONE);
		comboKostenstelle.setBounds(340, 37, 128, 23);
		
//##################################################		

		
		Composite cmpKalenderMitte = new Composite(cmpKalender, SWT.NONE);
		cmpKalenderMitte.setLayoutData(BorderLayout.CENTER);
		cmpKalenderMitte.setLayout(new BorderLayout(0, 0));
		
		Composite cmpKalenderKalender = new Composite(cmpKalenderMitte, SWT.NONE);
		cmpKalenderKalender.setLayoutData(BorderLayout.NORTH);
		cmpKalenderKalender.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		scrolledCompositeKalender = new ScrolledComposite(cmpKalenderKalender, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledCompositeKalender.setExpandHorizontal(true);
		scrolledCompositeKalender.setExpandVertical(true);

		cmpKalenderTabelle = new Composite(cmpKalenderMitte, SWT.NONE);
		cmpKalenderTabelle.setLayoutData(BorderLayout.CENTER);
		cmpKalenderTabelle.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		MyVorgangTools.stundenTabelleErstellen();
		
		CTabItem tbtmEinstellung = new CTabItem(tabFolder, SWT.NONE);
		tbtmEinstellung.setText("Einstellungen");
		
		Composite cmpEinstellung = new Composite(tabFolder, SWT.NONE);
		tbtmEinstellung.setControl(cmpEinstellung);
		cmpEinstellung.setLayout(null);
		
		Label lblStartdatum = new Label(cmpEinstellung, SWT.NONE);
		lblStartdatum.setBounds(28, 25, 59, 15);
		lblStartdatum.setText("Startdatum");
		
		Label lblEnddatum = new Label(cmpEinstellung, SWT.NONE);
		lblEnddatum.setBounds(193, 25, 55, 15);
		lblEnddatum.setText("Enddatum");
		
		dateChooserStart = new DateChooser(cmpEinstellung, SWT.NONE);
		dateChooserStart.setBounds(27, 45, 160, 157);
		dateChooserStart.setWeeksVisible(true);
		dateChooserStart.setSelectedDate(new Date());
		dateChooserStart.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				DateChooser t = (DateChooser) e.getSource();
				LogfileView.log(thisClass,"Starttermin wurde geändert in:" + t.getSelectedDate());			
				updateTreeItemDate(STARTTERMIN, t.getSelectedDate());
				dateChooserEnd.setSelectedDate(checkDate());
				MyVorgangTools.kalenderErstellen();
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		dateChooserEnd = new DateChooser(cmpEinstellung, SWT.NONE);
		dateChooserEnd.setBounds(193, 46, 164, 157);
		dateChooserEnd.setWeeksVisible(true);
		dateChooserEnd.setSelectedDate(new Date());
		dateChooserEnd.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				DateChooser t = (DateChooser) e.getSource();
				
//				t.setFocusOnDate(checkDate());
				t.setSelectedDate(checkDate());
				LogfileView.log(thisClass,"Endtermin wurde geändert in:" + t.getSelectedDate());			
				updateTreeItemDate(ENDTERMIN, t.getSelectedDate());
				MyVorgangTools.kalenderErstellen();
				
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Label lblTglicheArbeitsdauer = new Label(cmpEinstellung, SWT.NONE);
		lblTglicheArbeitsdauer.setBounds(28, 246, 115, 15);
		lblTglicheArbeitsdauer.setText("T\u00E4gliche Arbeitsdauer");
		
		Label lblMontag = new Label(cmpEinstellung, SWT.NONE);
		lblMontag.setBounds(28, 267, 42, 15);
		lblMontag.setText("Montag");
		
		Label lblDienstag = new Label(cmpEinstellung, SWT.NONE);
		lblDienstag.setBounds(28, 295, 55, 15);
		lblDienstag.setText("Dienstag");
		
		Label lblMittwoch = new Label(cmpEinstellung, SWT.NONE);
		lblMittwoch.setBounds(28, 323, 55, 15);
		lblMittwoch.setText("Mittwoch");
		
		Label lblDonnerstag = new Label(cmpEinstellung, SWT.NONE);
		lblDonnerstag.setBounds(28, 351, 80, 15);
		lblDonnerstag.setText("Donnerstag");
		
		Label lblFreitag = new Label(cmpEinstellung, SWT.NONE);
		lblFreitag.setBounds(28, 379, 55, 15);
		lblFreitag.setText("Freitag");
		
		Label lblSamstag = new Label(cmpEinstellung, SWT.NONE);
		lblSamstag.setBounds(28, 407, 55, 15);
		lblSamstag.setText("Samstag");
		
		Label lblSonntag = new Label(cmpEinstellung, SWT.NONE);
		lblSonntag.setBounds(28, 436, 55, 15);
		lblSonntag.setText("Sonntag");
		
		Label lblProjektdaten = new Label(cmpEinstellung, SWT.NONE);
		lblProjektdaten.setBounds(363, 25, 154, 15);
		lblProjektdaten.setText("Projektdaten");
		
		Label lblProjekt = new Label(cmpEinstellung, SWT.NONE);
		lblProjekt.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblProjekt.setBounds(363, 45, 125, 15);
		lblProjekt.setText("Projekt");
		
		Label lblAufgabe = new Label(cmpEinstellung, SWT.NONE);
		lblAufgabe.setBounds(363, 118, 125, 15);
		lblAufgabe.setText("Aufgabe");
		
		Label lblAuftragsNummer = new Label(cmpEinstellung, SWT.NONE);
		lblAuftragsNummer.setBounds(363, 145, 125, 15);
		lblAuftragsNummer.setText("Auftragnummer");
		
		Label lblKostentraeger = new Label(cmpEinstellung, SWT.NONE);
		lblKostentraeger.setBounds(363, 174, 125, 15);
		lblKostentraeger.setText("Kostentr\u00E4ger");
		
		Label lblGeplanteStunden = new Label(cmpEinstellung, SWT.NONE);
		lblGeplanteStunden.setBounds(363, 203, 125, 15);
		lblGeplanteStunden.setText("Geplante Stunden");
		
		Label lblBearbeiter = new Label(cmpEinstellung, SWT.NONE);
		lblBearbeiter.setBounds(363, 231, 125, 15);
		lblBearbeiter.setText("Bearbeiter");
		
		Label lblProjektbeschreibung = new Label(cmpEinstellung, SWT.NONE);
		lblProjektbeschreibung.setBounds(363, 74, 125, 15);
		lblProjektbeschreibung.setText("Projektbeschreibung");
		
		Label lblProjektmanager = new Label(cmpEinstellung, SWT.NONE);
		lblProjektmanager.setBounds(363, 290, 125, 15);
		lblProjektmanager.setText("New Label");
		
		Label lblNewLabel_2 = new Label(cmpEinstellung, SWT.NONE);
		lblNewLabel_2.setBounds(363, 318, 125, 15);
		lblNewLabel_2.setText("New Label");
		
		Label lblNewLabel_3 = new Label(cmpEinstellung, SWT.NONE);
		lblNewLabel_3.setBounds(363, 345, 125, 15);
		lblNewLabel_3.setText("New Label");
		
		comboProjekt = new Combo(cmpEinstellung, SWT.NONE);
		comboProjekt.setBounds(518, 45, 156, 23);
		comboProjekt.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Combo t = (Combo) e.getSource();
//				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Projekt wurde geändert in:" + t.getText());
				updateTreeItem(PROJEKT, t.getText());
			}		
		});
		
		txtAufgabe = new Text(cmpEinstellung, SWT.BORDER);
		txtAufgabe.setText("Aufgabe");
		txtAufgabe.setBounds(518, 118, 156, 21);
		txtAufgabe.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Aufgabe wurde geändert in:" + t.getText());
				updateTreeItem(AUFGABE, t.getText());
			}		
		});
		
		comboAuftragsnummer = new Combo(cmpEinstellung, SWT.NONE);
		comboAuftragsnummer.setBounds(518, 145, 156, 23);
		comboAuftragsnummer.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Auftragsnummer wurde geändert in:" + t.getText());
				updateTreeItem(AUFTRAGSNUMMER, t.getText());
			}		
		});
		
		comboKostentraeger = new Combo(cmpEinstellung, SWT.NONE);
		comboKostentraeger.setBounds(518, 174, 156, 23);
		comboKostentraeger.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Kostentraeger wurde geändert in:" + t.getText());
				updateTreeItem(KOSTENTRÄGER, t.getText());
			}		
		});
		
		
		comboBearbeiter = new Combo(cmpEinstellung, SWT.NONE);
		comboBearbeiter.setBounds(518, 231, 156, 23);
		comboBearbeiter.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Combo t = (Combo) e.getSource();
				LogfileView.log(thisClass,"Bearbeiter wurde geändert in:" + t.getText());
				updateTreeItem(BEARBEITER, t.getText());
			}		
		});
		
		textProjektbeschreibung = new Text(cmpEinstellung, SWT.BORDER);
		textProjektbeschreibung.setText("");
		textProjektbeschreibung.setBounds(518, 74, 156, 21);
		textProjektbeschreibung.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Projektbeschreibung wurde geändert in:" + t.getText());
				updateTreeItem(PROJEKTBESCHREIBUNG, t.getText());
			}		
		});
		
		textProjektmanager = new Text(cmpEinstellung, SWT.BORDER);
		textProjektmanager.setText("");
		textProjektmanager.setBounds(518, 290, 156, 21);
		textProjektmanager.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Projektmanager wurde geändert in:" + t.getText());
				updateTreeItem(PROJEKTMANAGER, t.getText());
			}		
		});
		
		text_2 = new Text(cmpEinstellung, SWT.BORDER);
		text_2.setBounds(518, 318, 156, 21);
		
		text_3 = new Text(cmpEinstellung, SWT.BORDER);
		text_3.setBounds(518, 345, 156, 21);
		
		Button btnUebernehmen = new Button(cmpEinstellung, SWT.NONE);
		btnUebernehmen.setBounds(603, 426, 75, 25);
		btnUebernehmen.setText("\u00DCbernehmen");
		
		txtGeplanteStunden = new Text(cmpEinstellung, SWT.BORDER);
		txtGeplanteStunden.setBounds(518, 202, 156, 21);
		txtGeplanteStunden.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"GeplanteStunden wurde geändert in:" + t.getText());
				updateTreeItem(GEPLANTESTUNDEN, t.getText());
			}		
		});
		
		txtArbeitsdauerMontag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerMontag.setBounds(114, 268, 47, 21);
		txtArbeitsdauerMontag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerMontag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERMONTAG, t.getText());
			}		
		});
		
		txtArbeitsdauerDienstag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerDienstag.setBounds(114, 296, 47, 21);
		txtArbeitsdauerDienstag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerDienstag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERDIENSTAG, t.getText());
			}		
		});
		
		txtArbeitsdauerMittwoch = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerMittwoch.setBounds(114, 324, 47, 21);
		txtArbeitsdauerMittwoch.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerMittwoch wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERMITTWOCH, t.getText());
			}		
		});
		
		txtArbeitsdauerDonnerstag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerDonnerstag.setBounds(114, 352, 47, 21);
		txtArbeitsdauerDonnerstag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerDonnerstag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERDONNERSTAG, t.getText());
			}		
		});
		
		txtArbeitsdauerFreitag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerFreitag.setBounds(114, 380, 47, 21);
		txtArbeitsdauerFreitag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerFreitag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERFREITAG, t.getText());
			}		
		});
		
		txtArbeitsdauerSamstag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerSamstag.setBounds(114, 409, 47, 21);
		txtArbeitsdauerSamstag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerSamstag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERSAMSTAG, t.getText());
			}		
		});
		
		txtArbeitsdauerSonntag = new Text(cmpEinstellung, SWT.BORDER);
		txtArbeitsdauerSonntag.setBounds(114, 436, 47, 21);
		txtArbeitsdauerSonntag.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"ArbeitsdauerSontag wurde geändert in:" + t.getText());
				updateTreeItem(ARBEITSDAUERSONNTAG, t.getText());
			}		
		});
		
		CTabItem tbtmKommentar = new CTabItem(tabFolder, SWT.NONE);
		tbtmKommentar.setText("Kommentar");
		
		ScrolledComposite scmpKommentar = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tbtmKommentar.setControl(scmpKommentar);
		scmpKommentar.setExpandHorizontal(true);
		scmpKommentar.setExpandVertical(true);
		
		styledTextKommentar = new StyledText(scmpKommentar, SWT.BORDER);
		scmpKommentar.setContent(styledTextKommentar);
		scmpKommentar.setMinSize(styledTextKommentar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		styledTextKommentar.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				Text t = (Text) e.getSource();
				LogfileView.log(thisClass,"Kommentar wurde geändert in:" + t.getText());
				updateTreeItem(KOMMENTAR, t.getText());
			}		
		});
		
		
//		MyVorgangTools.kalenderErstellen(scrolledCompositeKalender, projektDauerInMonaten);
		MyVorgangTools.kalenderErstellen();
		
		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
	
	
	
	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {

//		tbtmAuswertung.getControl().setFocus();
//		this.setFocus();
		System.out.println("Focus on ProjectCalendarView");
	}
	

	
	public static void updateMyTreeItem(MyTreeItem myTreeItem){
		myEventBroker(myTreeItem);
	}
	
	private static void myEventBroker(MyTreeItem myTreeItem){
		//sendet die geänderten Informationen in den Datenbus
		
		boolean wasDispatchedSuccessfully = eventBroker.send("MyTreeItemEvent", myTreeItem);
		if (wasDispatchedSuccessfully){
			LogfileView.log(thisClass,"Nachricht aus ProjectCalendarView gesendet "+ myTreeItem.getBezeichnung() + " : "  + myTreeItem.getVariablenWert());
		}
		
	}
	
	@Inject @Optional
	void myEventReceiver(@UIEventTopic("MyTreeItemEvent") MyTreeItem myTreeItem) {
	    // empfängt geänderte Objekte mit dem Topic 'MyTreeItemEvent'
		LogfileView.log(this.getClass(),"Nachricht in ProjectCalendarView empfangen "+ myTreeItem.getVariablenWert());
		mySelectedTreeItem = myTreeItem;
		reactOnSelectionOrEvent(myTreeItem);
	}
	
	@ Inject
	public void setSelection (@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional MyTreeItem myTreeItem) {
		LogfileView.log(this.getClass(), "Neue Selektion in ProjektCalenderViewer empfangen.");
		mySelectedTreeItem = myTreeItem;
		reactOnSelectionOrEvent(myTreeItem);
	}
	
	private void reactOnSelectionOrEvent(MyTreeItem myTreeItem){
		mySelectedTreeItem = myTreeItem;
		if (firstParent != null) {
//			if (checkInput(firstParent)) {
				if (myTreeItem != null) {
					MyTreeItem myNewSelectedTreeItem = searchProjectCalendarKnoten(myTreeItem);
					
					if(myNewSelectedTreeItem == null){	//kein Kalenderknoten
						//nichts tun
					}else {	//grundsätzlich alle Werte aktualisieren
						tasks.clear();			//Taskliste leeren
						updateViewer(myNewSelectedTreeItem);
						TasksTools.TabelleAktualisieren();
						checkDate();
						MyVorgangTools.kalenderErstellen();
					}
				}	
//			}
		}	
	}
	
	/**
	 * Sucht rekursiv aufwärts bis zum obersten Knoten des ProjectCalendar
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @return myTreeItem der gefundene Knoten oder NULL wenn nicht vorhanden
	 */
	private static  MyTreeItem searchProjectCalendarKnoten(MyTreeItem myTreeItem){
		MyTreeItem myNewTreeItem = null;
		//wenn ein Strukturknoten gefunden wird, gibt es keinen ProjectCalenderKnoten 
		if(myTreeItem.isStrukturknoten()){
			return myNewTreeItem;
		}else {
			if(myTreeItem.getParameter().equals(ZEITERFASSUNG)){
				return myTreeItem;
			}else {
				myTreeItem = searchProjectCalendarKnoten(myTreeItem.getParent());
				myZeiterfassungTreeItem = myTreeItem;
			}
		}
		return myTreeItem;
	}
	

	
	/**
	 * Liest  rekursiv den gesamten Kalenderbaum und weist die Werte zu
	 * @author Burkhard Pöhler
	 * @param myTreeItem der oberste Kalenderknoten
	 */
	private static void updateViewer(MyTreeItem myTreeItem){
		updateValue(myTreeItem);
		if (myTreeItem.getParameter().equals(ARBEITSSCHRITT)){
			ErzeugeEintragInTasksListe(myTreeItem);	//Taskliste füllen
		}
		if (myTreeItem.isHasChildren()){
			for (int i=0; i<myTreeItem.getChildren().size();i++){		
				updateViewer(myTreeItem.getChildren().get(i));
			}
		}
	}
	
	
	/**
	 * Sucht rekursiv den gesamten Kalenderbaum und weist die Werte zu
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Startknoten
	 * @param parameter der zu suchende Parameter
	 * @return myFoundTreeItem der gefundene Knoten
	 */
	private static MyTreeItem searchForMyTreeItem(MyTreeItem myTreeItem, String parameter){
		LogfileView.log(thisClass,"Prüfe Parameter " + myTreeItem.getParameter());
		MyTreeItem myFoundTreeItem = null;
		if (parameter != null) {
			if (!myTreeItem.getParameter().equals(parameter)) {		//nicht identisch
				if (myTreeItem.isHasChildren()) { //wenn Kinder vorhanden dann durchsuchen
					for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
						myFoundTreeItem = searchForMyTreeItem(myTreeItem.getChildren().get(i), parameter);
						if (myFoundTreeItem != null) break;
//						if (myTreeItem.getChildren().get(i).getParameter().equals(parameter)) {
//							LogfileView.log(thisClass,"Parameter gefunden " + myTreeItem.getParameter());
//							myFoundTreeItem = myTreeItem.getChildren().get(i);
//							break;
//						} else {
//							myFoundTreeItem = searchForMyTreeItem(myTreeItem.getChildren().get(i), parameter);
//						}
					}
				}
			}else{
				LogfileView.log(thisClass,"Parameter gefunden" + myTreeItem.getParameter());
				myFoundTreeItem = myTreeItem;			//gefunden
			}
		}
		return myFoundTreeItem;
	}
	
	
	/**
	 * Übergibt den Wert an das TreeItem und alarmiert alle anderen Viewer
	 * @author Burkhard Pöhler
	 * @param parameter der Parameter
	 * @param value der zu übergebende Wert
	 */
	public static void updateTreeItem(String parameter, String value){
		MyTreeItem myFoundTreeItem = null;
		myFoundTreeItem = searchForMyTreeItem(myZeiterfassungTreeItem, parameter);
		if(myFoundTreeItem != null){
			myFoundTreeItem.setVariablenWert(value);
			myEventBroker(myFoundTreeItem);			//alarmiere alle anderen Viewer über diese Änderung
		}

	}
	
	/**
	 * Übergibt den Wert an das TreeItem und alarmiert alle anderen Viewer
	 * Gilt nur für Datums und Zeitformate, da diese als Objekt gespeichert werden müssen
	 * @author Burkhard Pöhler
	 * @param parameter der Parameter
	 * @param value das zu übergebende Datum
	 */
	public static void updateTreeItemDate(String parameter, Date value){
		MyTreeItem myFoundTreeItem = null;
		myFoundTreeItem = searchForMyTreeItem(myZeiterfassungTreeItem, parameter);
		if(myFoundTreeItem != null){
			myFoundTreeItem.setObject(value);
			Date date = (Date) myFoundTreeItem.getObject();
			myFoundTreeItem.setVariablenWert(date.toString());
			myEventBroker(myFoundTreeItem);			//alarmiere alle anderen Viewer über diese Änderung
		}

	}
	
	
	/**
	 * Übergibt den Wert an das Widget
	 * @author Burkhard Pöhler
	 * @param myTreeItem der Parameter
	 */
	private static void updateValue(MyTreeItem myTreeItem){
		LogfileView.log(thisClass,"Update Parameter " + myTreeItem.getParameter() +" = "+ myTreeItem.getBezeichnung() +" : "+ myTreeItem.getVariablenWert());
		if (myTreeItem != null) {
			switch (myTreeItem.getParameter()) {

			//		case PROJEKT:
			//			comboProjekt.setText(myTreeItem.getVariablenWert());
			//			break;

			case NAME_FILTER:
				nameFilter = myTreeItem.getVariablenWert();
				break;
			case VORGANG_FILTER:
				vorgangFilter = myTreeItem.getVariablenWert();
				break;
			case PROJEKT_FILTER:
				projektFilter = myTreeItem.getVariablenWert();
				break;
			case KOSTENTRÄGER_FILTER:
				kostenträgerFilter = myTreeItem.getVariablenWert();
				break;
			case KOSTENSTELLE_FILTER:
				kostenstelleFilter = myTreeItem.getVariablenWert();
				break;
			case PROJEKTBESCHREIBUNG:
				textProjektbeschreibung.setText(myTreeItem.getVariablenWert());
				break;
			case AUFTRAGSNUMMER:
				comboAuftragsnummer.setText(myTreeItem.getVariablenWert());
				break;
			case KOSTENTRÄGER:
				comboKostentraeger.setText(myTreeItem.getVariablenWert());
				break;
			case AUFGABE:
				txtAufgabe.setText(myTreeItem.getVariablenWert());
				break;
			case PROJEKTMANAGER:
				textProjektmanager.setText(myTreeItem.getVariablenWert());
				break;
			case KOMMENTAR:
				styledTextKommentar.setText(myTreeItem.getVariablenWert());
				break;
			case BEARBEITER:
				comboBearbeiter.setText(myTreeItem.getVariablenWert());
				break;
			case KALKULIERTESTUNDEN:
				txtKalkulierteStunden.setText(myTreeItem.getVariablenWert());
				break;
			case AUFGELAUFENESTUNDEN:
				txtAufgelaufeneStunden.setText(myTreeItem.getVariablenWert());
				break;
			case GEPLANTESTUNDEN:
				txtGeplanteStunden.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERMONTAG:
				txtArbeitsdauerMontag.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERDIENSTAG:
				txtArbeitsdauerDienstag.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERMITTWOCH:
				txtArbeitsdauerMittwoch.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERDONNERSTAG:
				txtArbeitsdauerDonnerstag
						.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERFREITAG:
				txtArbeitsdauerFreitag.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERSAMSTAG:
				txtArbeitsdauerSamstag.setText(myTreeItem.getVariablenWert());
				break;
			case ARBEITSDAUERSONNTAG:
				txtArbeitsdauerSonntag.setText(myTreeItem.getVariablenWert());
				break;
			case AKTIVETASKS:
				btnNurAktive.setSelection(Boolean.valueOf(myTreeItem
						.getVariablenWert()));
				break;
			case VORGANG:
				arrayListVorgang = ComboBoxTools.sucheListeneigenschaften(myTreeItem);
				vorgänge = arrayListVorgang.toArray(new String[arrayListVorgang.size()]);
				break;
			case DATUM:
				if (myTreeItem.getObject() == null) { //kein Datum vorhanden
					Date date = new Date(); //nehme aktuelles Datum
					myTreeItem.setObject(date); //da Zeitformat nicht umgewandelt werden kann aus String wird hier der Wert direkt geholt
					dateDatum = (Date) myTreeItem.getObject();
					textDatum.setText(myTreeItem.getVariablenWert());
					txtAktuelleDatum = myTreeItem.getVariablenWert();
				} else {
					dateDatum = (Date) myTreeItem.getObject();
					textDatum.setText(myTreeItem.getVariablenWert());
					txtAktuelleDatum = myTreeItem.getVariablenWert();
				}
				break;
			case STARTTERMIN:
				if (myTreeItem.getObject() == null) { //kein Datum vorhanden
					Date date = new Date(); //nehme aktuelles Datum
					myTreeItem.setObject(date); //da Zeitformat nicht umgewandelt werden kann aus String wird hier der Wert direkt geholt
					dateChooserStart.setSelectedDate((Date) myTreeItem
							.getObject());
				} else {
					dateChooserStart.setSelectedDate((Date) myTreeItem
							.getObject());
				}
				break;
			case ENDTERMIN:
				if (myTreeItem.getObject() == null) { //kein Datum vorhanden
					Date date = new Date(); //nehme aktuelles Datum
					myTreeItem.setObject(date); //da Zeitformat nicht umgewandelt werden kann aus String wird hier der Wert direkt geholt
					dateChooserEnd.setSelectedDate((Date) myTreeItem
							.getObject());
				} else {
					dateChooserEnd.setSelectedDate((Date) myTreeItem
							.getObject());
				}
				break;

			}
		}                 
	}
	
	/**
	 * Liest den Baum und holt jeden Arbeitsschritt aus dem Baumknoten Arbeitsschritte
	 * im Modell werden nur Verweise auf die TreeItem gespeichert, nicht die Daten selber
	 * @param myTreeItem der oberste Knoten eines Arbeitsschrittes
	 * @author Burkhard Pöhler
	 */
	 private static void ErzeugeEintragInTasksListe(MyTreeItem myTreeItem){
		LogfileView.log(thisClass,"Lese Task :" + myTreeItem.getBezeichnung());
		MyTreeItem myFoundTreeItem = null;
		if (myTreeItem.isHasChildren()) { //wenn Kinder vorhanden dann durchsuchen
			MyTasksModel myTasksModell = new MyTasksModel();
			for (int i = 0; i < myTreeItem.getChildren().size(); i++) {
				myFoundTreeItem = myTreeItem.getChildren().get(i);
				
				switch (myFoundTreeItem.getParameter()) {
				
				case TASK:
					myTasksModell.setTask(myFoundTreeItem);
					break;
				case ERLEDIGT:
					myTasksModell.setErledigt(myFoundTreeItem);
					break;
				case FAHNE:
					myTasksModell.setFahne(myFoundTreeItem);
					break;
				case TYP:
					myTasksModell.setTyp(myFoundTreeItem);
					break;
				case BESCHREIBUNG:
					myTasksModell.setBeschreibung(myFoundTreeItem);
					break;
				case DAUER:
					myTasksModell.setDauer(myFoundTreeItem);
					break;
				case ARBEITSSCHRITT:
					myTasksModell.setArbeitsschritt(myFoundTreeItem);
					break;				
				}
			}
			tasks.add(myTasksModell);
		}
	
	 }

	/**
	 * @return the tasks
	 */
	public static ArrayList<MyTasksModel> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public static void setTasks(ArrayList<MyTasksModel> tasks) {
		ProjectCalendarView.tasks = tasks;
	}

	/**
	 * Prüft, dass das Enddatum immer nach dem Startdatum folgt
	 * @param enddatum das Enddatum
	 * @return das korrigierte Datum (Startdatum + 1 Tag)
	 */
	public static Date checkDate() {
		long Sekunden = 0;
		long Minuten =  0;
		long Stunden =  0;
		long Tage =  0;
		long Wochen =  0;
		long Monate =  0;
		int glatteMonate = 0;
		
		Date start = dateChooserStart.getSelectedDate();
		Date end = dateChooserEnd.getSelectedDate();
		int diff = start.compareTo(end);
		Calendar calendarStart = new GregorianCalendar();
		calendarStart.setTime(start);
		Calendar calendarEnd = new GregorianCalendar();
		calendarEnd.setTime(end);
		
		if (diff == 0){		// gleicher Tag
			calendarStart.add(Calendar.DAY_OF_MONTH, 1);
			end = calendarStart.getTime();
			updateTreeItemDate(ENDTERMIN,end); //alle anderen Viewer über Änderung informieren
			LogfileView.log(thisClass, "Endtermin ist am gleichen Tag wie Starttermin", SWT.ICON_ERROR);	
		}else if (diff < 0){	// liegt nach dem Startdatum
			
		}else { // liegt vor dem Startdatum
			LogfileView.log(thisClass, "Endtermin liegt vor dem Starttermin", SWT.ICON_ERROR);	
			calendarStart.add(Calendar.DAY_OF_MONTH, 1);
			end = calendarStart.getTime();
			updateTreeItemDate(ENDTERMIN,end); //alle anderen Viewer über Änderung informieren
		}
		// berechne die Anzahl der Monate zwischen den beiden Terminen
		
		long delta = calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis();
		Sekunden = delta / 1000;
		Minuten = Sekunden / 60;
		Stunden = Minuten / 60;
		Tage = Stunden / 24;
		Wochen = Tage / 7;
		Monate = Wochen / 4;
		glatteMonate = (int) Monate;
		
		projektDauerInMonaten = glatteMonate;
		LogfileView.log(thisClass, "Anzahl Projektmonate:" + projektDauerInMonaten , SWT.ICON_INFORMATION);
		return end;
	}

	/**
	 * @return the dateChooserStart
	 */
	public static DateChooser getDateChooserStart() {
		return dateChooserStart;
	}

	/**
	 * @param dateChooserStart the dateChooserStart to set
	 */
	public static void setDateChooserStart(DateChooser dateChooserStart) {
		ProjectCalendarView.dateChooserStart = dateChooserStart;
	}

	/**
	 * @return the dateChooserEnd
	 */
	public static DateChooser getDateChooserEnd() {
		return dateChooserEnd;
	}

	/**
	 * @param dateChooserEnd the dateChooserEnd to set
	 */
	public static void setDateChooserEnd(DateChooser dateChooserEnd) {
		ProjectCalendarView.dateChooserEnd = dateChooserEnd;
	}

	/**
	 * @return the scrolledCompositeKalender
	 */
	public static ScrolledComposite getScrolledCompositeKalender() {
		return scrolledCompositeKalender;
	}

	/**
	 * @return the projektDauerInMonaten
	 */
	public static int getProjektDauerInMonaten() {
		return projektDauerInMonaten;
	}

	/**
	 * @param projektDauerInMonaten the projektDauerInMonaten to set
	 */
	public static void setProjektDauerInMonaten(int projektDauerInMonaten) {
		ProjectCalendarView.projektDauerInMonaten = projektDauerInMonaten;
	}

	/**
	 * @return the dateDatum
	 */
	public static Date getDateDatum() {
		return dateDatum;
	}

	/**
	 * @param dateDatum the dateDatum to set
	 */
	public static void setDateDatum(Date dateDatum) {
		ProjectCalendarView.dateDatum = dateDatum;
	}

	/**
	 * @return the alleStundenAnzeigen
	 */
	public static Boolean getAlleStundenAnzeigen() {
		return AlleStundenAnzeigen;
	}

	/**
	 * @param alleStundenAnzeigen the alleStundenAnzeigen to set
	 */
	public static void setAlleStundenAnzeigen(Boolean alleStundenAnzeigen) {
		AlleStundenAnzeigen = alleStundenAnzeigen;
	}

	/**
	 * @return the nameFilter
	 */
	public static String getNameFilter() {
		return nameFilter;
	}

	/**
	 * @param nameFilter the nameFilter to set
	 */
	public static void setNameFilter(String nameFilter) {
		ProjectCalendarView.nameFilter = nameFilter;
		updateTreeItem(NAME_FILTER, nameFilter);
	}

	/**
	 * @return the vorgangFilter
	 */
	public static String getVorgangFilter() {
		return vorgangFilter;
	}

	/**
	 * @param vorgangFilter the vorgangFilter to set
	 */
	public static void setVorgangFilter(String vorgangFilter) {
		ProjectCalendarView.vorgangFilter = vorgangFilter;
		updateTreeItem(VORGANG_FILTER, vorgangFilter);
	}

	/**
	 * @return the projektFilter
	 */
	public static String getProjektFilter() {
		return projektFilter;
	}

	/**
	 * @param projektFilter the projektFilter to set
	 */
	public static void setProjektFilter(String projektFilter) {
		ProjectCalendarView.projektFilter = projektFilter;
		updateTreeItem(PROJEKT_FILTER, projektFilter);
	}

	/**
	 * @return the kostenträgerFilter
	 */
	public static String getKostenträgerFilter() {
		return kostenträgerFilter;
	}

	/**
	 * @param kostenträgerFilter the kostenträgerFilter to set
	 */
	public static void setKostenträgerFilter(String kostenträgerFilter) {
		ProjectCalendarView.kostenträgerFilter = kostenträgerFilter;
		updateTreeItem(KOSTENTRÄGER_FILTER, kostenträgerFilter);
	}

	/**
	 * @return the kostenstelleFilter
	 */
	public static String getKostenstelleFilter() {
		return kostenstelleFilter;
	}

	/**
	 * @param kostenstelleFilter the kostenstelleFilter to set
	 */
	public static void setKostenstelleFilter(String kostenstelleFilter) {
		ProjectCalendarView.kostenstelleFilter = kostenstelleFilter;
		updateTreeItem(KOSTENSTELLE_FILTER, kostenstelleFilter);
	}

	/**
	 * @return the arrayListVorgang
	 */
	public static String[] getArrayListVorgang() {
		String[] stringArray = arrayListVorgang.toArray(new String[arrayListVorgang.size()]);
		return stringArray;
	}

	/**
	 * @param arrayListVorgang the arrayListVorgang to set
	 */
	public static void setArrayListVorgang(ArrayList<String> arrayListVorgang) {
		ProjectCalendarView.arrayListVorgang = arrayListVorgang;
	}

	/**
	 * @return the vorgänge
	 */
	public static String[] getVorgänge() {
		return vorgänge;
	}

	/**
	 * @param vorgänge the vorgänge to set
	 */
	public static void setVorgänge(String[] vorgänge) {
		ProjectCalendarView.vorgänge = vorgänge;
	}
}
