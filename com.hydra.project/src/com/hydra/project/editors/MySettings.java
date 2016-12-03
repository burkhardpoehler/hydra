package com.hydra.project.editors;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import com.hydra.project.model.AktiveTasksFilter;
import com.hydra.project.model.TasksTools;
import com.hydra.project.parts.LogfileView;
import com.hydra.project.settings.pathControl;

public class MySettings {

	protected Shell shlEinstellungen;
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
		shlEinstellungen = new Shell();
		shlEinstellungen.setSize(679, 547);
		shlEinstellungen.setText("Einstellungen");
		shlEinstellungen.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlEinstellungen, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		TabFolder tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
		
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
				String path = pathControl.checkPath(shlEinstellungen, "test", "Basisdaten");
				
				textPath1.setText(path);
				}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				}		
		});
		
		
		
		Button btnNewPath2 = new Button(composite, SWT.NONE);
		btnNewPath2.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath2.setBounds(0, 61, 37, 25);
		
		Button btnNewPath3 = new Button(composite, SWT.NONE);
		btnNewPath3.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath3.setBounds(0, 92, 37, 25);
		
		Button btnNewPath4 = new Button(composite, SWT.NONE);
		btnNewPath4.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath4.setBounds(0, 123, 37, 25);
		
		Button btnNewPath5 = new Button(composite, SWT.NONE);
		btnNewPath5.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath5.setBounds(0, 154, 37, 25);
		
		Button btnNewPath6 = new Button(composite, SWT.NONE);
		btnNewPath6.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		btnNewPath6.setBounds(0, 185, 37, 25);
		
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
		lblPathLabel1.setBounds(42, 30, 167, 25);
		
		Label lblPathLabel2 = new Label(composite, SWT.NONE);
		lblPathLabel2.setText("Company");
		lblPathLabel2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel2.setBounds(43, 61, 167, 25);
		
		Label lblPathLabel3 = new Label(composite, SWT.NONE);
		lblPathLabel3.setText("Settings");
		lblPathLabel3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel3.setBounds(42, 92, 167, 25);
		
		Label lblPathLabel4 = new Label(composite, SWT.NONE);
		lblPathLabel4.setText("Mitarbeiter");
		lblPathLabel4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel4.setBounds(43, 123, 167, 25);
		
		Label lblPathLabel5 = new Label(composite, SWT.NONE);
		lblPathLabel5.setText("Stunden");
		lblPathLabel5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel5.setBounds(42, 154, 167, 25);
		
		Label lblPathLabel6 = new Label(composite, SWT.NONE);
		lblPathLabel6.setText("Projekte");
		lblPathLabel6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel6.setBounds(42, 185, 167, 25);
		
		Label lblPathLabel7 = new Label(composite, SWT.NONE);
		lblPathLabel7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel7.setBounds(42, 216, 167, 25);
		
		Label lblPathLabel8 = new Label(composite, SWT.NONE);
		lblPathLabel8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel8.setBounds(43, 247, 167, 25);
		
		Label lblPathLabel9 = new Label(composite, SWT.NONE);
		lblPathLabel9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel9.setBounds(43, 278, 167, 25);
		
		Label lblPathLabel10 = new Label(composite, SWT.NONE);
		lblPathLabel10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblPathLabel10.setBounds(43, 309, 167, 25);
		
		textPath1 = new Text(composite, SWT.BORDER);
		textPath1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath1.setBounds(215, 30, 426, 25);
		
		textPath2 = new Text(composite, SWT.BORDER);
		textPath2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath2.setBounds(215, 61, 426, 25);
		
		textPath3 = new Text(composite, SWT.BORDER);
		textPath3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath3.setBounds(215, 92, 426, 25);
		
		textPath4 = new Text(composite, SWT.BORDER);
		textPath4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath4.setBounds(216, 123, 426, 25);
		
		textPath5 = new Text(composite, SWT.BORDER);
		textPath5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath5.setBounds(215, 154, 426, 25);
		
		textPath6 = new Text(composite, SWT.BORDER);
		textPath6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath6.setBounds(215, 185, 426, 25);
		
		textPath7 = new Text(composite, SWT.BORDER);
		textPath7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath7.setBounds(215, 216, 426, 25);
		
		textPath8 = new Text(composite, SWT.BORDER);
		textPath8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath8.setBounds(216, 247, 426, 25);
		
		textPath9 = new Text(composite, SWT.BORDER);
		textPath9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath9.setBounds(215, 278, 426, 25);
		
		textPath10 = new Text(composite, SWT.BORDER);
		textPath10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		textPath10.setBounds(216, 309, 426, 25);
		
		TabItem tbtmCompany = new TabItem(tabFolder, SWT.NONE);
		tbtmCompany.setText("Firma");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmCompany.setControl(composite_1);
		
		TabItem tbtmComputer = new TabItem(tabFolder, SWT.NONE);
		tbtmComputer.setText("Computer");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmComputer.setControl(composite_2);
		
		TabItem tbtmUser = new TabItem(tabFolder, SWT.NONE);
		tbtmUser.setText("Nutzer");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmUser.setControl(composite_3);
		
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
		
		Button button_F2 = new Button(composite_6, SWT.NONE);
		button_F2.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F2.setBounds(0, 61, 37, 25);
		
		Button button_F3 = new Button(composite_6, SWT.NONE);
		button_F3.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F3.setBounds(0, 92, 37, 25);
		
		Button button_F4 = new Button(composite_6, SWT.NONE);
		button_F4.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F4.setBounds(0, 123, 37, 25);
		
		Button button_F5 = new Button(composite_6, SWT.NONE);
		button_F5.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F5.setBounds(0, 154, 37, 25);
		
		Button button_F6 = new Button(composite_6, SWT.NONE);
		button_F6.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F6.setBounds(0, 185, 37, 25);
		
		Button button_F7 = new Button(composite_6, SWT.NONE);
		button_F7.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F7.setBounds(0, 216, 37, 25);
		
		Button button_F8 = new Button(composite_6, SWT.NONE);
		button_F8.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F8.setBounds(0, 247, 37, 25);
		
		Button button_F9 = new Button(composite_6, SWT.NONE);
		button_F9.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F9.setBounds(0, 278, 37, 25);
		
		Button button_F10 = new Button(composite_6, SWT.NONE);
		button_F10.setImage(SWTResourceManager.getImage(MySettings.class, "/com/hydra/projects/XViewer/Main/images/refresh.gif"));
		button_F10.setBounds(0, 309, 37, 25);
		
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
		text_F1.setBounds(124, 30, 517, 25);
		
		text_F2 = new Text(composite_6, SWT.BORDER);
		text_F2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F2.setBounds(124, 61, 517, 25);
		
		text_F3 = new Text(composite_6, SWT.BORDER);
		text_F3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F3.setBounds(124, 92, 517, 25);
		
		text_F4 = new Text(composite_6, SWT.BORDER);
		text_F4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F4.setBounds(125, 123, 516, 25);
		
		text_F5 = new Text(composite_6, SWT.BORDER);
		text_F5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F5.setBounds(124, 154, 517, 25);
		
		text_F6 = new Text(composite_6, SWT.BORDER);
		text_F6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F6.setBounds(124, 185, 517, 25);
		
		text_F7 = new Text(composite_6, SWT.BORDER);
		text_F7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F7.setBounds(124, 216, 517, 25);
		
		text_F8 = new Text(composite_6, SWT.BORDER);
		text_F8.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F8.setBounds(125, 247, 516, 25);
		
		text_F9 = new Text(composite_6, SWT.BORDER);
		text_F9.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F9.setBounds(124, 278, 517, 25);
		
		text_F10 = new Text(composite_6, SWT.BORDER);
		text_F10.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_F10.setBounds(125, 309, 516, 25);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("New Item");
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("New Item");
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_2.setText("New Item");
		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
}
