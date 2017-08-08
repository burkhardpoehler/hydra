package com.hydra.project.editors;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.hydra.project.command.NewNodeChildCommand;
import com.hydra.project.database.DBCompanySettings;
import com.hydra.project.database.DBComputerSettings;
import com.hydra.project.database.DBUserSettings;
import com.hydra.project.model.AktiveTasksFilter;
import com.hydra.project.model.MyCompanySettings;
import com.hydra.project.model.MyComputerSettings;
import com.hydra.project.model.MyUserSettings;
import com.hydra.project.model.TasksTools;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.settings.pathControl;
import com.hydra.project.startProcedure.StartProcedure;

import swing2swt.layout.BorderLayout;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class MySettings {

	protected static Shell shlEinstellungen;
	private Text textPath1;
	private Text textPath2;
	private Text textPath3;
	private Text textPath4;
	private Text textPath5;
	private Text textPath6;
	private Text textPath7;
	private Text textPath8;
	private Text textPath9;
	private Text textPath10;
	private Text text_F1;
	private Text text_F2;
	private Text text_F3;
	private Text text_F4;
	private Text text_F5;
	private Text text_F6;
	private Text text_F7;
	private Text text_F8;
	private Text text_F9;
	private Text text_F10;
	Boolean dirtyFlag = false;
	private Text txtFirmaFirma;
	private Text txtLFirmaSparte;
	private Text txtFirmaStrasse;
	private Text txtFirmaPostleitzahl;
	private Text txtFirmaOrt;
	private Text txtFirmaLand;
	private MyCompanySettings myCompanySettings;
	private MyComputerSettings myComputerSettings;
	private MyUserSettings myUserSettings;
	private static String thisClass= "MySettings";
	private Text txtComputerjavahome;
	private Text txtComputerjavaversion;
	private Text txtComputerarchitektur;
	private Text txtComputerbetriebssystemName;
	private Text txtComputerbetriebssystemVersion;
	private Text txtComputerbesitzer;
	private Text txtUser;
	private Text txtUserdir;
	private Text txtUserhome;
	private Text txtUseremail;
	private Text txtUsershort;
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MySettings window = new MySettings();
		
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * Open the window.
//	 */
//	public static void open(Shell shell) {
//		
//		Display display = shell.getDisplay();
//		createContents();
//		shlEinstellungen = shell;
//		shlEinstellungen.setSize(905, 547);
//		shlEinstellungen.setText("Einstellungen");
//		shlEinstellungen.setLayout(new BorderLayout(0, 0));
//
//	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlEinstellungen.open();
		shlEinstellungen.layout();

		while (!shlEinstellungen.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		//hole die Firmendaten
		myCompanySettings = new MyCompanySettings();
		myCompanySettings = DBCompanySettings.readDB();
		
		myComputerSettings = new MyComputerSettings();
		myComputerSettings = DBComputerSettings.readDB();
		
		myUserSettings = new MyUserSettings();
		myUserSettings = DBUserSettings.readSettingsDB();
		
		shlEinstellungen = new Shell();
		shlEinstellungen.setSize(905, 547);
		shlEinstellungen.setText("Einstellungen");
		shlEinstellungen.setLayout(new BorderLayout(0, 0));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlEinstellungen, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		
		Composite composite_main = new Composite(scrolledComposite, SWT.NONE);
		composite_main.setLayout(new BorderLayout(0, 0));
	
		
		Composite composite_oben = new Composite(composite_main, SWT.NONE);
		composite_oben.setLayoutData(BorderLayout.CENTER);
		FillLayout fl_composite_oben = new FillLayout(SWT.HORIZONTAL);
		composite_oben.setLayout(fl_composite_oben);
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(composite_oben, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		Composite composite_buttons = new Composite(composite_main, SWT.NONE);
		composite_buttons.setLayoutData(BorderLayout.SOUTH);
		composite_buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		Button btnClose = new Button(composite_buttons, SWT.NONE);
		btnClose.setText("Schließen");
		btnClose.setEnabled(true);
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlEinstellungen.dispose();
			}
		});
		
		
		TabFolder tabFolder = new TabFolder(scrolledComposite_1, SWT.NONE);
		
		TabItem tbtmPath = new TabItem(tabFolder, SWT.NONE);
		tbtmPath.setText("Pfade");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmPath.setControl(composite);
		composite.setLayout(null);
		
		Button btnNewPath1 = new Button(composite, SWT.NONE);
		btnNewPath1.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath1.setBounds(0, 30, 37, 25);
		btnNewPath1.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				LogfileView.log(this.getClass(), "Button gedrückt");
				String textPath1_old = textPath1.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath1.getText(), "Basisdaten", "C:/Hydra/Daten/", false);
				textPath1.setText(path);
				if (!textPath1_old.contentEquals(textPath1.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath2 = new Button(composite, SWT.NONE);
		btnNewPath2.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath2.setBounds(0, 61, 37, 25);
		btnNewPath2.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textPath2_old = textPath2.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath2.getText(), "Company", "C:/Hydra/Daten/", false);
				textPath2.setText(path);
				if (!textPath2_old.contentEquals(textPath2.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath3 = new Button(composite, SWT.NONE);
		btnNewPath3.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath3.setBounds(0, 92, 37, 25);
		btnNewPath3.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textPath3_old = textPath3.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath3.getText(), "Computer", "C:/Hydra/Daten/", false);
				
				textPath3.setText(path);
				if (!textPath3_old.contentEquals(textPath3.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath4 = new Button(composite, SWT.NONE);
		btnNewPath4.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath4.setBounds(0, 123, 37, 25);
		btnNewPath4.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textPath4_old = textPath4.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath4.getText(), "Mitarbeiter", "C:/Hydra/Daten/", false);	
				textPath4.setText(path);
				if (!textPath4_old.contentEquals(textPath4.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath5 = new Button(composite, SWT.NONE);
		btnNewPath5.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath5.setBounds(0, 154, 37, 25);
		btnNewPath5.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textPath5_old = textPath5.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath5.getText(), "Stunden", "C:/Hydra/Daten/", false);	
				textPath5.setText(path);
				if (!textPath5_old.contentEquals(textPath5.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath6 = new Button(composite, SWT.NONE);
		btnNewPath6.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath6.setBounds(0, 185, 37, 25);
		btnNewPath6.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String textPath6_old = textPath6.getText();
				String path = pathControl.selectPath(shlEinstellungen, textPath6.getText(), "Projekte", "C:/Hydra/Daten/", false);	
				textPath6.setText(path);
				if (!textPath6_old.contentEquals(textPath6.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		Button btnNewPath7 = new Button(composite, SWT.NONE);
		btnNewPath7.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath7.setBounds(0, 216, 37, 25);
		
		Button btnNewPath8 = new Button(composite, SWT.NONE);
		btnNewPath8.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath8.setBounds(0, 247, 37, 25);
		
		Button btnNewPath9 = new Button(composite, SWT.NONE);
		btnNewPath9.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath9.setBounds(0, 278, 37, 25);
		
		Button btnNewPath10 = new Button(composite, SWT.NONE);
		btnNewPath10.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath10.setBounds(0, 309, 37, 25);
		
		Label lblPathLabel1 = new Label(composite, SWT.NONE);
		lblPathLabel1.setText("Basisdaten");
		lblPathLabel1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel1.setBounds(42, 30, 96, 25);
		
		Label lblPathLabel2 = new Label(composite, SWT.NONE);
		lblPathLabel2.setText("Firma");
		lblPathLabel2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel2.setBounds(43, 61, 96, 25);
		
		Label lblPathLabel3 = new Label(composite, SWT.NONE);
		lblPathLabel3.setText("Computer");
		lblPathLabel3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel3.setBounds(42, 92, 96, 25);
		
		Label lblPathLabel4 = new Label(composite, SWT.NONE);
		lblPathLabel4.setText("Mitarbeiter");
		lblPathLabel4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel4.setBounds(43, 123, 96, 25);
		
		Label lblPathLabel5 = new Label(composite, SWT.NONE);
		lblPathLabel5.setText("Stunden");
		lblPathLabel5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel5.setBounds(42, 154, 96, 25);
		
		Label lblPathLabel6 = new Label(composite, SWT.NONE);
		lblPathLabel6.setText("Projekte");
		lblPathLabel6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel6.setBounds(42, 185, 96, 25);
		
		Label lblPathLabel7 = new Label(composite, SWT.NONE);
		lblPathLabel7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel7.setBounds(42, 216, 96, 25);
		
		Label lblPathLabel8 = new Label(composite, SWT.NONE);
		lblPathLabel8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel8.setBounds(43, 247, 96, 25);
		
		Label lblPathLabel9 = new Label(composite, SWT.NONE);
		lblPathLabel9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel9.setBounds(43, 278, 96, 25);
		
		Label lblPathLabel10 = new Label(composite, SWT.NONE);
		lblPathLabel10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel10.setBounds(43, 309, 96, 25);
		
		//Basisdaten
		textPath1 = new Text(composite, SWT.BORDER);
		textPath1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath1.setBounds(144, 30, 626, 25);
		textPath1.setEnabled(false);
		textPath1.setText(StartProcedure.getBasisdatenDir());
		
		//Firma
		textPath2 = new Text(composite, SWT.BORDER);
		textPath2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath2.setBounds(144, 61, 626, 25);
		textPath2.setEnabled(false);
		textPath2.setText(StartProcedure.getCompanyDir());
		
		//Computer
		textPath3 = new Text(composite, SWT.BORDER);
		textPath3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath3.setBounds(144, 92, 626, 25);
		textPath3.setEnabled(false);
		textPath3.setText(StartProcedure.getComputerDir());
		
		//Mitarbeiter
		textPath4 = new Text(composite, SWT.BORDER);
		textPath4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath4.setBounds(145, 123, 626, 25);
		textPath4.setEnabled(false);
		textPath4.setText(StartProcedure.getCompanyDataDir());
		
		//Stunden
		textPath5 = new Text(composite, SWT.BORDER);
		textPath5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath5.setBounds(144, 154, 626, 25);
		textPath5.setEnabled(false);
		textPath5.setText(StartProcedure.getCompanyDataDir());
		
		//Projekte
		textPath6 = new Text(composite, SWT.BORDER);
		textPath6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath6.setBounds(144, 185, 626, 25);
		textPath6.setEnabled(false);
		textPath6.setText(StartProcedure.getProjekteDir());
		
		textPath7 = new Text(composite, SWT.BORDER);
		textPath7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath7.setBounds(144, 216, 626, 25);
		textPath7.setEnabled(false);
		
		textPath8 = new Text(composite, SWT.BORDER);
		textPath8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath8.setBounds(145, 247, 626, 25);
		textPath8.setEnabled(false);
		
		textPath9 = new Text(composite, SWT.BORDER);
		textPath9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath9.setBounds(144, 278, 626, 25);
		textPath9.setEnabled(false);
		
		textPath10 = new Text(composite, SWT.BORDER);
		textPath10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath10.setBounds(145, 309, 626, 25);
		textPath10.setEnabled(false);

		//###########################################################################################
		TabItem tbtmCompany = new TabItem(tabFolder, SWT.NONE);
		tbtmCompany.setText("Firma");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmCompany.setControl(composite_1);
		

		Label lblFirma = new Label(composite_1, SWT.NONE);
		lblFirma.setText("Firma");
		lblFirma.setBounds(10, 24, 110, 21);
		
		Label lblFirmensparte = new Label(composite_1, SWT.NONE);
		lblFirmensparte.setText("Firmensparte");
		lblFirmensparte.setBounds(10, 51, 110, 21);
		
		Label lblStrasse = new Label(composite_1, SWT.NONE);
		lblStrasse.setText("Strasse");
		lblStrasse.setBounds(10, 78, 110, 21);
		
		Label lblPostleitzahl = new Label(composite_1, SWT.NONE);
		lblPostleitzahl.setText("Postleitzahl");
		lblPostleitzahl.setBounds(10, 105, 110, 21);
		
		Label lblOrt = new Label(composite_1, SWT.NONE);
		lblOrt.setText("Ort");
		lblOrt.setBounds(10, 132, 110, 21);
		
		Label lblLand = new Label(composite_1, SWT.NONE);
		lblLand.setText("Land");
		lblLand.setBounds(10, 159, 110, 21);
		

		
		txtFirmaFirma = new Text(composite_1, SWT.BORDER);
		txtFirmaFirma.setBounds(126, 24, 321, 21);
		if (myCompanySettings == null){
			txtFirmaFirma.setText("L\u00F6dige Industries");
		}else{
			txtFirmaFirma.setText(myCompanySettings.getFirma());
		}
		txtFirmaFirma.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {			
				boolean condition = myCompanySettings.getFirma().contains(txtFirmaFirma.getText());
				if (!condition) {
					myCompanySettings.setFirma(txtFirmaFirma.getText());
					LogfileView.log(thisClass, "Feld Firma geändert in: " + txtFirmaFirma.getText(),
							SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}

		});
		
		txtLFirmaSparte = new Text(composite_1, SWT.BORDER);
		txtLFirmaSparte.setBounds(126, 51, 321, 21);
		if (myCompanySettings == null){
			txtLFirmaSparte.setText("L\u00F6dige Systems");
		}else{
			txtLFirmaSparte.setText(myCompanySettings.getFirmensparte());
		}
		txtLFirmaSparte.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myCompanySettings.getFirmensparte().contains(txtLFirmaSparte.getText());
				if (!condition) {
					myCompanySettings.setFirmensparte(txtLFirmaSparte.getText());
					LogfileView.log(thisClass, "Feld Firmensparte geändert in: " + txtLFirmaSparte.getText(),
							SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}
		});
		
		txtFirmaStrasse = new Text(composite_1, SWT.BORDER);
		txtFirmaStrasse.setBounds(126, 78, 321, 21);
		if (myCompanySettings == null){
			txtFirmaStrasse.setText("Wilhelm L\u00F6dige Strasse 1");
		}else{
			txtFirmaStrasse.setText(myCompanySettings.getStrasse());
		}
		txtFirmaStrasse.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myCompanySettings.getStrasse().contains(txtFirmaStrasse.getText());
				if (!condition) {
					myCompanySettings.setStrasse(txtFirmaStrasse.getText());
					LogfileView.log(thisClass, "Feld Straße geändert in: " + txtFirmaStrasse.getText(),
							SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}
		});
		
		txtFirmaPostleitzahl = new Text(composite_1, SWT.BORDER);
		txtFirmaPostleitzahl.setBounds(126, 105, 321, 21);
		if (myCompanySettings == null){
			txtFirmaPostleitzahl.setText("34414");
		}else{
			txtFirmaPostleitzahl.setText(myCompanySettings.getPostleitzahl());
		}
		txtFirmaPostleitzahl.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myCompanySettings.getPostleitzahl().contains(txtFirmaPostleitzahl.getText());
				if (!condition) {
					myCompanySettings.setPostleitzahl(txtFirmaPostleitzahl.getText());
					LogfileView.log(thisClass, "Feld Postleitzahl geändert in: " + txtFirmaPostleitzahl.getText(),
							SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}
		});
		
		txtFirmaOrt = new Text(composite_1, SWT.BORDER);
		txtFirmaOrt.setBounds(126, 132, 321, 21);
		if (myCompanySettings == null){
			txtFirmaOrt.setText("Warburg Scherfede");
		}else{
			txtFirmaOrt.setText(myCompanySettings.getOrt());
		}
		txtFirmaOrt.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myCompanySettings.getOrt().contains(txtFirmaOrt.getText());
				if (!condition) {
					myCompanySettings.setOrt(txtFirmaOrt.getText());
					LogfileView.log(thisClass, "Feld Ort geändert in: " + txtFirmaOrt.getText(), SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}
		});
		
		txtFirmaLand = new Text(composite_1, SWT.BORDER);
		txtFirmaLand.setBounds(126, 159, 321, 21);
		if (myCompanySettings == null){
			txtFirmaLand.setText("Deutschland");
		}else{
			txtFirmaLand.setText(myCompanySettings.getLand());
		}
		txtFirmaLand.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myCompanySettings.getLand().contains(txtFirmaLand.getText());
				if (!condition) {
					myCompanySettings.setLand(txtFirmaLand.getText());
					LogfileView.log(thisClass, "Feld Land geändert in: " + txtFirmaLand.getText(),
							SWT.ICON_INFORMATION);
					updateFirmendaten();
				}
			}
		});
		

		//##########################################################################################
		TabItem tbtmComputer = new TabItem(tabFolder, SWT.NONE);
		tbtmComputer.setText("Computer");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmComputer.setControl(composite_2);
		
		Label lblJavaHome = new Label(composite_2, SWT.NONE);
		lblJavaHome.setText("Java Home");
		lblJavaHome.setBounds(10, 30, 135, 21);
		
		Label lblJavaVersion = new Label(composite_2, SWT.NONE);
		lblJavaVersion.setText("Java Version");
		lblJavaVersion.setBounds(10, 57, 135, 21);
		
		Label lblArchitektur = new Label(composite_2, SWT.NONE);
		lblArchitektur.setText("Architektur");
		lblArchitektur.setBounds(10, 84, 135, 21);
		
		Label lblNameBetriebssystem = new Label(composite_2, SWT.NONE);
		lblNameBetriebssystem.setText("Name Betriebssystem");
		lblNameBetriebssystem.setBounds(10, 111, 135, 21);
		
		Label lblVersionBetriebssystem = new Label(composite_2, SWT.NONE);
		lblVersionBetriebssystem.setText("Version Betriebssystem");
		lblVersionBetriebssystem.setBounds(10, 138, 135, 21);
		
		Label lblBesitzer = new Label(composite_2, SWT.NONE);
		lblBesitzer.setText("Besitzer");
		lblBesitzer.setBounds(10, 165, 135, 21);
		
		txtComputerjavahome = new Text(composite_2, SWT.BORDER);
		txtComputerjavahome.setEnabled(false);
		txtComputerjavahome.setBounds(174, 30, 321, 21);
		if (myComputerSettings == null){
			txtComputerjavahome.setText(System.getProperty("java.home"));
		}else{
			txtComputerjavahome.setText(myComputerSettings.getJavaHome());
		}
		
		txtComputerjavaversion = new Text(composite_2, SWT.BORDER);
		txtComputerjavaversion.setEnabled(false);
		txtComputerjavaversion.setBounds(174, 57, 321, 21);
		if (myComputerSettings == null){
			txtComputerjavaversion.setText(System.getProperty("java.version"));
		}else{
			txtComputerjavaversion.setText(myComputerSettings.getJavaVersion());
		}
		
		txtComputerarchitektur = new Text(composite_2, SWT.BORDER);
		txtComputerarchitektur.setEnabled(false);
		txtComputerarchitektur.setBounds(174, 84, 321, 21);
		if (myComputerSettings == null){
			txtComputerarchitektur.setText(System.getProperty("java.arch"));
		}else{
			txtComputerarchitektur.setText(myComputerSettings.getOsArch());
		}
		
		txtComputerbetriebssystemName = new Text(composite_2, SWT.BORDER);
		txtComputerbetriebssystemName.setEnabled(false);
		txtComputerbetriebssystemName.setBounds(174, 111, 321, 21);
		if (myComputerSettings == null){
			txtComputerbetriebssystemName.setText(System.getProperty("java.name"));
		}else{
			txtComputerbetriebssystemName.setText(myComputerSettings.getOsName());
		}
		
		txtComputerbetriebssystemVersion = new Text(composite_2, SWT.BORDER);
		txtComputerbetriebssystemVersion.setEnabled(false);
		txtComputerbetriebssystemVersion.setBounds(174, 138, 321, 21);
		if (myComputerSettings == null){
			txtComputerbetriebssystemVersion.setText(System.getProperty("java.version"));
		}else{
			txtComputerbetriebssystemVersion.setText(myComputerSettings.getOsVersion());
		}
		
		txtComputerbesitzer = new Text(composite_2, SWT.BORDER);
		txtComputerbesitzer.setText("ComputerBesitzer");
		txtComputerbesitzer.setBounds(174, 165, 321, 21);
		if (myComputerSettings == null){
			txtComputerbesitzer.setText("");
		}else{
			txtComputerbesitzer.setText(myComputerSettings.getBesitzer());
		}
		txtComputerbesitzer.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myComputerSettings.getBesitzer().contains(txtComputerbesitzer.getText());
				if (!condition) {
					myComputerSettings.setBesitzer(txtComputerbesitzer.getText());
					LogfileView.log(thisClass, "Feld Besitzer geändert in: " + txtComputerbesitzer.getText(),
							SWT.ICON_INFORMATION);
					updateComputerdaten();
				}
			}
		});
		
		//##########################################################################################
		
		TabItem tbtmUser = new TabItem(tabFolder, SWT.NONE);
		tbtmUser.setText("Nutzer");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmUser.setControl(composite_3);
		
		Label lblUser = new Label(composite_3, SWT.NONE);
		lblUser.setText("User");
		lblUser.setBounds(10, 37, 135, 21);
		
		Label lblUserdir = new Label(composite_3, SWT.NONE);
		lblUserdir.setText("UserDir");
		lblUserdir.setBounds(10, 64, 135, 21);
		
		Label lblUserhome = new Label(composite_3, SWT.NONE);
		lblUserhome.setText("UserHome");
		lblUserhome.setBounds(10, 91, 135, 21);
		
		Label lblUseremail = new Label(composite_3, SWT.NONE);
		lblUseremail.setText("UserEmail");
		lblUseremail.setBounds(10, 118, 135, 21);
		
		Label lblUsershort = new Label(composite_3, SWT.NONE);
		lblUsershort.setText("UserShort");
		lblUsershort.setBounds(10, 145, 135, 21);
		
		txtUser = new Text(composite_3, SWT.BORDER);
		txtUser.setEnabled(false);
		txtUser.setBounds(152, 37, 321, 21);
		if (myUserSettings == null){
			txtUser.setText(System.getProperty("user.name"));
		}else{
			txtUser.setText(myUserSettings.getUser());
		}
		
		txtUserdir = new Text(composite_3, SWT.BORDER);
		txtUserdir.setEnabled(false);
		txtUserdir.setBounds(151, 64, 321, 21);
		if (myUserSettings == null){
			txtUserdir.setText(System.getProperty("user.dir"));
		}else{
			txtUserdir.setText(myUserSettings.getUserDir());
		}
		
		txtUserhome = new Text(composite_3, SWT.BORDER);
		txtUserhome.setEnabled(false);
		txtUserhome.setBounds(151, 91, 321, 21);
		if (myUserSettings == null){
			txtUserhome.setText(System.getProperty("user.home"));
		}else{
			txtUserhome.setText(myUserSettings.getUserHome());
		}
		
		txtUseremail = new Text(composite_3, SWT.BORDER);
		txtUseremail.setEnabled(true);
		txtUseremail.setBounds(152, 118, 321, 21);
		if (myUserSettings == null){
			txtUseremail.setText("");
		}else{
			txtUseremail.setText(myUserSettings.getUserEmail());
		}
		txtUseremail.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myUserSettings.getUserEmail().contains(txtUseremail.getText());
				if (!condition) {
					myUserSettings.setUserEmail(txtUseremail.getText());
					LogfileView.log(thisClass, "Feld UserEMail geändert in: " + txtUseremail.getText(),
							SWT.ICON_INFORMATION);
					updateUserdaten();
				}
			}
		});
		
		txtUsershort = new Text(composite_3, SWT.BORDER);
		txtUsershort.setEnabled(true);
		txtUsershort.setBounds(152, 145, 321, 21);
		if (myUserSettings == null){
			txtUsershort.setText("");
		}else{
			txtUsershort.setText(myUserSettings.getUserShort());
		}
		txtUsershort.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				boolean condition = myUserSettings.getUserShort().contains(txtUsershort.getText());
				if (!condition) {
					myUserSettings.setUserShort(txtUsershort.getText());
					LogfileView.log(thisClass, "Feld UserShort geändert in: " + txtUsershort.getText(),
							SWT.ICON_INFORMATION);
					updateUserdaten();
				}
			}
		});
		
		//##########################################################################################
		
		TabItem tbtmSample1 = new TabItem(tabFolder, SWT.NONE);
		tbtmSample1.setText("Diverses");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmSample1.setControl(composite_4);
		
		TabItem tbtmFavoriten = new TabItem(tabFolder, SWT.NONE);
		tbtmFavoriten.setText("Favoriten");
		
		Composite composite_6 = new Composite(tabFolder, SWT.NONE);
		tbtmFavoriten.setControl(composite_6);
		
		Button button_F1 = new Button(composite_6, SWT.NONE);
		button_F1.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F1.setBounds(0, 30, 37, 25);
		button_F1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F1_old =text_F1.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F1.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F1.setText(path);
				if (!text_F1_old.contentEquals(text_F1.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F2 = new Button(composite_6, SWT.NONE);
		button_F2.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F2.setBounds(0, 61, 37, 25);
		button_F2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F2_old =text_F2.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F2.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F2.setText(path);
				if (!text_F2_old.contentEquals(text_F2.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F3 = new Button(composite_6, SWT.NONE);
		button_F3.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F3.setBounds(0, 92, 37, 25);
		button_F3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F3_old =text_F3.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F3.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F3.setText(path);
				if (!text_F3_old.contentEquals(text_F3.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F4 = new Button(composite_6, SWT.NONE);
		button_F4.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F4.setBounds(0, 123, 37, 25);
		button_F4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F4_old =text_F4.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F4.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F4.setText(path);
				if (!text_F4_old.contentEquals(text_F4.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F5 = new Button(composite_6, SWT.NONE);
		button_F5.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F5.setBounds(0, 154, 37, 25);
		button_F5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F5_old =text_F5.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F5.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F5.setText(path);
				if (!text_F5_old.contentEquals(text_F5.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F6 = new Button(composite_6, SWT.NONE);
		button_F6.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F6.setBounds(0, 185, 37, 25);
		button_F6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F6_old =text_F6.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F6.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F6.setText(path);
				if (!text_F6_old.contentEquals(text_F6.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F7 = new Button(composite_6, SWT.NONE);
		button_F7.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F7.setBounds(0, 216, 37, 25);
		button_F7.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F7_old =text_F7.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F7.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F7.setText(path);
				if (!text_F7_old.contentEquals(text_F7.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F8 = new Button(composite_6, SWT.NONE);
		button_F8.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F8.setBounds(0, 247, 37, 25);
		button_F8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F8_old =text_F8.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F8.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F8.setText(path);
				if (!text_F8_old.contentEquals(text_F8.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F9 = new Button(composite_6, SWT.NONE);
		button_F9.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F9.setBounds(0, 278, 37, 25);
		button_F9.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F9_old =text_F9.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F9.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F9.setText(path);
				if (!text_F9_old.contentEquals(text_F9.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Button button_F10 = new Button(composite_6, SWT.NONE);
		button_F10.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F10.setBounds(0, 309, 37, 25);
		button_F10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				String text_F10_old =text_F10.getText();
				String path = pathControl.selectPath(shlEinstellungen, text_F10.getText(), "*", "C:/Hydra/Projekte/", true);	
				text_F10.setText(path);
				if (!text_F10_old.contentEquals(text_F10.getText())){
					dirtyFlag=true;
					btnClose.setEnabled(true);
				}
			}
		});
		
		Label lblFavorit = new Label(composite_6, SWT.NONE);
		lblFavorit.setText("Favorit 1");
		lblFavorit.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit.setBounds(42, 30, 84, 25);
		
		Label lblFavorit_1 = new Label(composite_6, SWT.NONE);
		lblFavorit_1.setText("Favorit 2");
		lblFavorit_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_1.setBounds(43, 61, 83, 25);
		
		Label lblFavorit_2 = new Label(composite_6, SWT.NONE);
		lblFavorit_2.setText("Favorit 3");
		lblFavorit_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_2.setBounds(42, 92, 84, 25);
		
		Label lblFavorit_3 = new Label(composite_6, SWT.NONE);
		lblFavorit_3.setText("Favorit 4");
		lblFavorit_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_3.setBounds(43, 123, 83, 25);
		
		Label lblFavorit_4 = new Label(composite_6, SWT.NONE);
		lblFavorit_4.setText("Favorit 5");
		lblFavorit_4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_4.setBounds(42, 154, 84, 25);
		
		Label lblFavorit_5 = new Label(composite_6, SWT.NONE);
		lblFavorit_5.setText("Favorit 6");
		lblFavorit_5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_5.setBounds(42, 185, 84, 25);
		
		Label lblFavorit_6 = new Label(composite_6, SWT.NONE);
		lblFavorit_6.setText("Favorit 7");
		lblFavorit_6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_6.setBounds(42, 216, 84, 25);
		
		Label lblFavorit_7 = new Label(composite_6, SWT.NONE);
		lblFavorit_7.setText("Favorit 8");
		lblFavorit_7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_7.setBounds(43, 247, 83, 25);
		
		Label lblFavorit_8 = new Label(composite_6, SWT.NONE);
		lblFavorit_8.setText("Favorit 9");
		lblFavorit_8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_8.setBounds(43, 278, 83, 25);
		
		Label lblFavorit_9 = new Label(composite_6, SWT.NONE);
		lblFavorit_9.setText("Favorit 10");
		lblFavorit_9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFavorit_9.setBounds(43, 309, 83, 25);
		
		text_F1 = new Text(composite_6, SWT.BORDER);
		text_F1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F1.setBounds(132, 30, 509, 25);
		text_F1.setEnabled(false);
		
		text_F2 = new Text(composite_6, SWT.BORDER);
		text_F2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F2.setBounds(132, 61, 509, 25);
		text_F2.setEnabled(false);
		
		text_F3 = new Text(composite_6, SWT.BORDER);
		text_F3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F3.setBounds(132, 92, 509, 25);
		text_F3.setEnabled(false);
		
		text_F4 = new Text(composite_6, SWT.BORDER);
		text_F4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F4.setBounds(132, 123, 509, 25);
		text_F4.setEnabled(false);
		
		text_F5 = new Text(composite_6, SWT.BORDER);
		text_F5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F5.setBounds(132, 154, 509, 25);
		text_F5.setEnabled(false);
		
		text_F6 = new Text(composite_6, SWT.BORDER);
		text_F6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F6.setBounds(132, 185, 509, 25);
		text_F6.setEnabled(false);
		
		text_F7 = new Text(composite_6, SWT.BORDER);
		text_F7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F7.setBounds(132, 216, 509, 25);
		text_F7.setEnabled(false);
		
		text_F8 = new Text(composite_6, SWT.BORDER);
		text_F8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F8.setBounds(132, 247, 509, 25);
		text_F8.setEnabled(false);
		
		text_F9 = new Text(composite_6, SWT.BORDER);
		text_F9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F9.setBounds(132, 278, 509, 25);
		text_F9.setEnabled(false);
		
		text_F10 = new Text(composite_6, SWT.BORDER);
		text_F10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F10.setBounds(132, 309, 509, 25);
		text_F10.setEnabled(false);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("New Item");
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("New Item");
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_2.setText("New Item");
		
		scrolledComposite_1.setContent(tabFolder);
		scrolledComposite_1.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		scrolledComposite.setContent(composite_main);
		scrolledComposite.setMinSize(composite_main.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.pack();
		
//		createContents();
		


	}
	
	
	private void updateFirmendaten(){
		 DBCompanySettings.writeDB(myCompanySettings);
	}
	
	private void updateComputerdaten(){
		 DBComputerSettings.writeDB(myComputerSettings);
	}
	
	private void updateUserdaten(){
		 DBUserSettings.writeSettingsDB(myUserSettings);
	}

}
