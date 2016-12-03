/**
 * 
 */
package com.hydra.project.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import com.hydra.project.exceptions.NoChildFoundException;
import com.hydra.project.myplugin_nebula.xviewer.customize.CustomizeData;
import com.hydra.project.parts.LogfileView;


/**
 * @author Burkhard Pöhler
 *
 */
public class MyTreeItem implements Cloneable {

	//Formate:
	private Font font;				//Schrifttype
	private RGB cellForeground;		//Farbe Zelle Vordergrund
	private RGB cellBackground;		//Farbe Zelle Hintergrund
	private RGB foreground;			//Farbe Vordergrund
	private RGB background;			//Farbe Hintergrund
	private String iconDateiname= "001_001_001.gif";	//Name des zugehörigen Icons
	private String toolTipText="ToolTip";		//Tooltip

	//Funktionen
	private boolean expanded=false;				//Knoten ist voll dargestellt
	private boolean checked=false;				//angeklickt im TreeView
	private boolean grayed=false;					//grau geschaltet	
	private boolean hasChildren=false;			//wenn Kinder --> dan ja
	private boolean hasPreChild=false;			//true wenn vorhergehendes Kind existiert
	private boolean hasPostChild=false;			//true wenn nachfolgendes Kind existiert
	private boolean dirty=false;					//Dirty Flag wird gesetzt wenn Daten geändert wurden
//	private boolean dateiItem;				//true wenn zweitoberster Knoten
	private boolean ueberschrift=false;			//true wenn Überschrift angezeigt werden soll
	private boolean isWurzel=false;				//true wenn Wurzel
	private boolean isBasis=false;				//true wenn Basis
	private boolean isParameter=false;			//true wenn Parameter
	private boolean isEigenschaft=false;			//true wenn Eigenschaft
	private boolean isVorlage=false;				//true wenn Vorlage
	private boolean isProjektBaum=false;			//true wenn oberster Projektbaumknoten
	private boolean isProjekt=false;				//true wenn oberster Projektknoten
	private boolean isObersterKnoten=false;		//true wenn oberster Knoten
	private boolean isStrukturknoten=false;		//true wenn zum festen Baum gehörend
	private boolean isDummy=true;				//true wenn nur eine neuer Zweig benötigt wird
	
	//Werte aktuell
	private String uuid;					//eindeutiger Name UUID
	private String parameter="";				//Parameter
	private String variablenWert="";			//eigentlicher Datenwert/Inhalt der Variablen
	private String variablenEinheit="";		//Größeneinheit
	private String variablentyp="";			//Auswahl Variabelntyp: Text,Zahl,Zeit usw.
	private String beschreibung="";			//Beschreibt den Parameter
	private String bezeichnung="";				//bezeichnet den Parameter
	private String datenbankName="leer";			//Kennzeichnet die Datenbank, nur im obersten Knoten gefüllt
	private int dbIndex=0;						//Datenbankindex
	private String bildDateiname="leer";			//Name des zugehörigen Bildes
	private String birtReport="leer";				//Name des zugehörigen Birt Reports
	private String anzeigeText="";				//Name des zugehörigen Textes am Knoten
	private Object object;					//Enhält das Objekt einer Auswertezeile (Überschrift)
	private String erstelldatum;				//Datum der Erstellung
	private String ersteller="leer";				//Name des Erstellers
	private String bearbeiter="leer";				//Name des letzen Bearbeiters
	private String aenderungsdatum;			//Datum der letzen Änderung
	private String Link="";					//eindeutiger Verweis auf einen anderen Parameter	
	private String editor="TextCellEditor";					//der zu verwendende Editor	
	
	//Werte alt
	private String uuidAlt;					//eindeutiger Name UUID
	private String variablenWertAlt="";		//eigentlicher Datenwert/Inhalt der Variablen alt
	private String variablenEinheitAlt="";		//Größeneinheit alt
	private String variablentypAlt="leer";			//Auswahl Variabelntyp: Text,Zahl,Zeit usw. alt
	private String beschreibungAlt="leer";			//Beschreibt den alten Parameter
	private String bezeichnungAlt="leer";			//bezeichnet den alten Parameter
	private String bildDateinameAlt="leer";		//Name des zugehörigen alten Bildes
	private String anzeigeTextAlt="leer";			//Name des zugehörigen alten Anzeigetextes
	private Object objectAlt;				//Enhält das alte Objekt einer Auswertezeile (Überschrift)
	private String datenbankNameAlt="leer";		//Kennzeichnet die alte Datenbank, nur im obersten Knoten gefüllt
	private int totalNoOfChildrenAlt=0;			//Gesamtanzahl der Kinder unterhalb des Knotens	

	//Beziehungen
	private MyTreeItem parent;
	private String parentUUID;
	private MyTreeItem preChild;			//vorhergehende Kind
	private MyTreeItem postChild;			//nachfolgende Kind
	private int noOfChildren=0;				//Anzahl der Kinder eines Knotens
	private int totalNoOfChildren=0;			//Gesamtanzahl der Kinder unterhalb des Knotens	
	protected List <MyTreeItem> children;	//Liste der Kinder eines Knoten
//	protected List <String> treePath;		//Liste der Väter eines Knoten
	private int index=0;						//Sortierreihenfolge

	private Calendar calendar = Calendar.getInstance();
	SimpleDateFormat sdfmt = new SimpleDateFormat();


	
//	@Inject
	public MyTreeItem(){					
		setup();
	}

	private void setup(){
		parent=null;
		this.setHasChildren(false);
		this.setNoOfChildren(0);
		children = new ArrayList<MyTreeItem>();
//		treePath = new ArrayList<String>();
		this.uuid=UUID.randomUUID().toString();
		uuidAlt= uuid;
		this.dirty = true;
		this.iconDateiname = "001_001_001.gif";
		this.dbIndex=0;
		if(this.isDummy()) this.setIconDateiname("arrow-branch-270-left.png");
		this.variablentyp = "String";
		this.setErsteller(System.getProperty("user.name")); 
		this.setBearbeiter(System.getProperty("user.name"));
		sdfmt.applyPattern( "EEEE', 'dd. MMMM yyyy hh:mm" );
		
		erstelldatum = calendar.getTime().toString();
		aenderungsdatum = calendar.getTime().toString();
	}
	
		
	@Override
	public MyTreeItem clone(){
	    try
		    {
	    		MyTreeItem myTreeItem = (MyTreeItem) super.clone();
	    		myTreeItem.setUuid(); //alle neuen Knoten brauchen eine neue UUID
	    		return myTreeItem;
		    }
	    catch ( CloneNotSupportedException e ) {
	      // Kann eigentlich nicht passieren, da Cloneable
	      throw new InternalError();
	    }
	}

	/**
	 * Berechnet neue Werte nach Änderung. Muß separat angestoßen werden.
	 * Setzt den Schalter "dirty" zurück
	 */
	public void updateValues (){
		this.setBearbeiter(System.getProperty("user.name"));
		LogfileView.log("Update von: " + this.getBezeichnung());
		Calendar calendar = Calendar.getInstance();
		if (aenderungsdatum == null) aenderungsdatum = calendar.getTime().toString();
		if (erstelldatum == null) erstelldatum = calendar.getTime().toString();
		if (ersteller == null) ersteller = System.getProperty("user.name");
		if (bearbeiter == null) bearbeiter = System.getProperty("user.name");
		if (dirty) aenderungsdatum = calendar.getTime().toString();
		if (dirty) bearbeiter = System.getProperty("user.name");
		if (editor == null) editor = "textCellEditor";
		this.dirty = false;
	}
	
	
	/**
	 * Benennt die Namen aller treeItems um für eine Copy Aktion
	 */
	public void Rename (MyTreeItem myTreeItem){
		this.dirty = true;
		
		if (myTreeItem.isHasChildren()) {
			for (int i = 0; i < children.size(); i++) { 
				children.get(i).uuid = UUID.randomUUID().toString();
				Rename(children.get(i));			//rekursiv durch alle Kinder laufen
			}
		}
	}
	
	/**
	 * Fügt das Kind an letzter Position an
	 */
	public void addChild(MyTreeItem myTreeItem) {
		this.dirty = true;

		int anzahl=0;
		if (children == null){
			children = new  ArrayList<MyTreeItem>(5);
			myTreeItem.setIndex(0);		//erste Kind muss immer 0 sein
		}
		myTreeItem.setParentUUID(uuid);
		children.add(myTreeItem); 						// hängt hinten an

		for (int i=0; i<children.size();i++){	//eventuelle Lücken entfernen
			if (children.get(i)== null){
				children.remove(i);
			}
		}

		anzahl = setPreAndPostChilds();
		this.setHasChildren(true);
		this.setNoOfChildren(children.size());
		this.totalNoOfChildren = children.size() + anzahl;
		this.setTotalNoOfChildren();
	}
	
	/**
	 * Fügt das Kind an der gewünschten Stelle ein
	 */
	public void setChild(MyTreeItem myTreeItem) {
		this.dirty = true;

		int anzahl=0;
		if (children == null){
			children = new  ArrayList<MyTreeItem>(5);
			myTreeItem.setIndex(0);		//erste Kind muss immer 0 sein
		}
		if (myTreeItem.getIndex()> children.size()) myTreeItem.setIndex(children.size());
		myTreeItem.setParentUUID(uuid);
		
		if (myTreeItem.hasPostChild) {
			children.add(myTreeItem.getIndex(), myTreeItem); //alle anderen nachfolgenden Kinder werden verschoben
		}else {
			children.add(myTreeItem); 						// hängt hinten an
		}
		for (int i=0; i<children.size();i++){	//eventuelle Lücken entfernen
			if (children.get(i)== null){
				children.remove(i);
			}
		}

		anzahl = setPreAndPostChilds();
		this.setHasChildren(true);
		this.setNoOfChildren(children.size());
		this.totalNoOfChildren = children.size() + anzahl;
		this.setTotalNoOfChildren();
		
	}
	
	public boolean removeChild(MyTreeItem myTreeItem) {
		this.dirty = true;
		int anzahl=0;
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) == null) {
					children.remove(i);
				}
				if (children.get(i).getUuid().equals(myTreeItem.getUuid())) {
					children.remove(i);
				}
			}
			anzahl = setPreAndPostChilds();
			this.setNoOfChildren(children.size());
			this.totalNoOfChildren = children.size() + anzahl;
			this.setTotalNoOfChildren();
			if (children.isEmpty()) {
				children = null;
				this.setHasChildren(false);
			}
		}else {
			LogfileView.log("+++ Fehler in MyTreeItem.class removeChild: Kindknoten konnte nicht gelöscht werden");
		}
		return true;	
	}
	
	private int setPreAndPostChilds(){
		this.dirty = true;
		int anzahl=0;
		for (int i=0; i<children.size();i++){
			children.get(i).setIndex(i);
			if (i==0){												//1. Kind
				children.get(0).setHasPreChild(false);
				if(children.size()>1){
					children.get(0).setHasPostChild(true);
				}else{
					children.get(0).setHasPostChild(false);
				}
			}else if (i>0 & i < children.size()){					//alle weiteren Kinder
				children.get(i).setHasPreChild(true);
				children.get(i).setHasPostChild(true);
			}
			children.get(children.size()-1).setHasPostChild(false);	//letztes Kind
			anzahl=anzahl + children.get(i).getTotalNoOfChildren();
			
			children.get(i).setParent(this);
		}
		return anzahl;
	}
	
	public void checkChildren() {
		this.dirty = true;
		if (children == null){
			children = new  ArrayList<MyTreeItem>(5);
		}
		for (int i=0; i<children.size();i++){	//eventuelle Lücken entfernen
			if (children.get(i)== null){
				children.remove(i);
			}
		}
		this.setNoOfChildren(children.size());
		for (int i=0; i<children.size();i++){
			children.get(i).setIndex(i);
		}
	}
	
	public List<MyTreeItem> getChildren() {
		 try {
		} catch (NullPointerException e) {
			System.out.println("NullPointerException");
		} 
		return children;
	}
	
	public void shiftMyTreeItemDownwards() {
		this.dirty = true;
		//tauscht zwei Kinder gegeneinander aus
			MyTreeItem child1=this.getParent().children.get(getIndex());
			MyTreeItem child2=this.getParent().children.get(getIndex()+1);
			child1.setIndex(child1.getIndex()+1);
			child2.setIndex(child2.getIndex()-1);
			this.getParent().children.set(child1.getIndex(), child1);
			this.getParent().children.set(child2.getIndex(), child2);
			this.getParent().setPreAndPostChilds();
	}
	
	public void shiftMyTreeItemUpwards() {
		this.dirty = true;
		//tauscht zwei Kinder gegeneinander aus
			MyTreeItem child1=this.getParent().children.get(this.getIndex());
			MyTreeItem child2=this.getParent().children.get(this.getIndex()-1);
			child1.setIndex(child1.getIndex()-1);
			child2.setIndex(child2.getIndex()+1);
			this.getParent().children.set(child1.getIndex(), child1);
			this.getParent().children.set(child2.getIndex(), child2);
			this.getParent().setPreAndPostChilds();
	}
	
	
	public void clearChildren(){
		this.dirty = true;
		if(!(children==null)){
			children.clear();
			this.setHasChildren(false);
			this.setNoOfChildren(0);
		}
	}
	

	/**
	 * @return the preChild
	 */
	public MyTreeItem getPreChild() {
		MyTreeItem parent = this.getParent();
		if(this.getIndex()==0){
			return null;
		}else {
			if (!(parent.getChildren()==null)){
				MyTreeItem preChild = parent.children.get(this.getIndex()-1);
				return preChild;
			}
		}
		return null;
	}
	
	/**
	 * @return the postChild
	 */
	public MyTreeItem getPostChild() {
		MyTreeItem parent = this.getParent();
		if(!(parent.getChildren()==null)){
			if (parent.getChildren().size()-1 > this.getIndex()){
				MyTreeItem postChild = parent.getChildren().get(this.getIndex()+1);
				return postChild;
			}
		}
		return null;
	}
	
	/**
	 * Schrifttype
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}


	/**
	 * Schrifttype
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.dirty = true;
		this.font = font;
	}


	/**
	 * Farbe Zelle Vordergrund
	 * @return the cellForeground
	 */
	public RGB getCellForeground() {
		return cellForeground;
	}


	/**
	 * Farbe Zelle Vordergrund
	 * @param cellForeground the cellForeground to set
	 */
	public void setCellForeground(RGB cellForeground) {
		this.dirty = true;
		this.cellForeground = cellForeground;
	}


	/**
	 * Farbe Zelle Hintergrund
	 * @return the cellBackground
	 */
	public RGB getCellBackground() {
		return cellBackground;
	}


	/**
	 * Farbe Zelle Hintergrund
	 * @param cellBackground the cellBackground to set
	 */
	public void setCellBackground(RGB cellBackground) {
		this.dirty = true;
		this.cellBackground = cellBackground;
	}


	/**
	 * Farbe Vordergrund
	 * @return the foreground
	 */
	public RGB getForeground() {
		return foreground;
	}


	/**Farbe Vordergrund
	 * @param foreground the foreground to set
	 */
	public void setForeground(RGB foreground) {
		this.dirty = true;
		this.foreground = foreground;
	}


	/**
	 * Farbe Hintergrund
	 * @return the background
	 */
	public RGB getBackground() {
		return background;
	}


	/**
	 * Farbe Hintergrund
	 * @param background the background to set
	 */
	public void setBackground(RGB background) {
		this.dirty = true;
		this.background = background;
	}


	/**
	 * Name des zugehörigen Icons
	 * @return the iconDateiname
	 */
	public String getIconDateiname() {
		return iconDateiname;
	}


	/**
	 * Name des zugehörigen Icons
	 * @param iconDateiname the iconDateiname to set
	 */
	public void setIconDateiname(String iconDateiname) {
		this.dirty = true;
		this.iconDateiname = iconDateiname;
	}


	/**
	 * @return the toolTipText
	 */
	public String getToolTipText() {
		return toolTipText;
	}


	/**
	 * @param toolTipText the toolTipText to set
	 */
	public void setToolTipText(String toolTipText) {
		this.dirty = true;
		this.toolTipText = toolTipText;
	}


	/**
	 * Knoten ist voll dargestellt
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}


	/**
	 * Knoten ist voll dargestellt
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}


	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.dirty = true;
		this.checked = checked;
	}


	/**
	 * @return the grayed
	 */
	public boolean isGrayed() {
		return grayed;
	}


	/**
	 * @param grayed the grayed to set
	 */
	public void setGrayed(boolean grayed) {
		this.dirty = true;
		this.grayed = grayed;
	}


	/**
	 * @return the hasChildren
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}


	/**
	 * @param hasChildren the hasChildren to set
	 */
	public void setHasChildren(boolean hasChildren) {
		this.dirty = true;
		this.hasChildren = hasChildren;
	}


	/**
	 * @return the hasPreChild
	 */
	public boolean isHasPreChild() {
		return hasPreChild;
	}


	/**
	 * @param hasPreChild the hasPreChild to set
	 */
	public void setHasPreChild(boolean hasPreChild) {
		this.dirty = true;
		this.hasPreChild = hasPreChild;
	}


	/**
	 * @return the hasPostChild
	 */
	public boolean isHasPostChild() {
		return hasPostChild;
	}


	/**
	 * @param hasPostChild the hasPostChild to set
	 */
	public void setHasPostChild(boolean hasPostChild) {
		this.dirty = true;
		this.hasPostChild = hasPostChild;
	}


	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}


	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @return the ueberschrift
	 */
	public boolean isUeberschrift() {
		return ueberschrift;
	}


	/**
	 * @param ueberschrift the ueberschrift to set
	 */
	public void setUeberschrift(boolean ueberschrift) {
		this.dirty = true;
		this.ueberschrift = ueberschrift;
	}


	/**
	 * @return the isParameter
	 */
	public boolean isParameter() {
		return isParameter;
	}


	/**
	 * @param isParameter the isParameter to set
	 */
	public void setIsParameter(boolean isParameter) {
		this.dirty = true;
		this.isParameter = isParameter;
	}


	/**
	 * @return the isEigenschaft
	 */
	public boolean isEigenschaft() {
		return isEigenschaft;
	}


	/**
	 * @param isEigenschaft the isEigenschaft to set
	 */
	public void setIsEigenschaft(boolean isEigenschaft) {
		this.dirty = true;
		this.isEigenschaft = isEigenschaft;
	}


	/**
	 * eindeutiger Name UUID. Wird automatisch vergeben
	 * @return the name
	 */
	public String getUuid() {
		return uuid;
	}


	/**
	 * Parameter in der Form x123.456.789
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}


	/**
	 * Parameter in der Form x123.456.789
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.dirty = true;
		this.parameter = parameter;
	}


	/**
	 * eigentlicher Datenwert/Inhalt der Variablen
	 * @return the variablenWert
	 */
	public String getVariablenWert() {
		return variablenWert;
	}


	/**
	 * eigentlicher Datenwert/Inhalt der Variablen
	 * @param variablenWert the variablenWert to set
	 */
	public void setVariablenWert(String variablenWert) {
		this.dirty = true;
		this.setVariablenWertAlt(this.variablenWert);
		this.variablenWert = variablenWert;
	}


	/**
	 * Größeneinheit
	 * @return the variablenEinheit
	 */
	public String getVariablenEinheit() {
		return variablenEinheit;
	}


	/**
	 * Größeneinheit
	 * @param variablenEinheit the variablenEinheit to set
	 */
	public void setVariablenEinheit(String variablenEinheit) {
		this.dirty = true;
		this.setVariablenEinheitAlt(this.variablenEinheit);
		this.variablenEinheit = variablenEinheit;
	}


	/**
	 * Auswahl Variabelntyp: Text,Zahl,Zeit usw.
	 * @return the variablentyp
	 */
	public String getVariablentyp() {
		return variablentyp;
	}


	/**
	 * Auswahl Variabelntyp: Text,Zahl,Zeit usw.
	 * @param variablentyp the variablentyp to set
	 */
	public void setVariablentyp(String variablentyp) {
		this.dirty = true;
		this.setVariablentypAlt(this.variablentyp);
		this.variablentyp = variablentyp;
	}


	/**
	 * Beschreibt den Parameter
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}


	/**
	 * Beschreibt den Parameter
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(String beschreibung) {
		this.dirty = true;
		this.setBeschreibungAlt(this.beschreibung);
		this.beschreibung = beschreibung;
	}


	/**
	 * bezeichnet den Parameter
	 * @return the bezeichnung
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}


	/**
	 * bezeichnet den Parameter
	 * @param bezeichnung the bezeichnung to set
	 */
	public void setBezeichnung(String bezeichnung) {
		this.dirty = true;
		this.setBezeichnungAlt(this.bezeichnung);
		this.bezeichnung = bezeichnung;
	}


	/**
	 * Kennzeichnet die Datenbank
	 * @return the datenbankName
	 */
	public String getDatenbankName() {
		return datenbankName;
	}

	/**
	 * Ersetzt ein vorhandenes Child durch ein anderes
	 * @author Burkhard Pöhler
	 * @param myOldTreeItem Zu ersetzendes Kind
	 * @param myNewTreeItem Das neue Kind
	 */
	public void replaceMyTreeItem(MyTreeItem myOldTreeItem, MyTreeItem myNewTreeItem) {
		if (children.contains(myOldTreeItem)){
			children.set(children.lastIndexOf(myOldTreeItem), myNewTreeItem);
		}
	}
	
	
	/**
	 * Liefert den Index eines Kindes
	 * @param myTreeItem Das zu suchende Kind
	 * @return Index des Kindes. -1 wenn nicht gefunden
	 */
	public Integer findMyTreeItemChild(MyTreeItem myTreeItem) {
		Integer index = -1;
		if(this.isHasChildren()){			//wenn Kinder vorhanden dann durchsuchen
			for (int i=0; i<children.size();i++){
				if (children.get(i).equals(myTreeItem)){
					index = i;
					break;
				}
			}
		}		
		return index;
	}
	
	
	/**
	 * Durchsucht den Baum abwärts nach einem Namen
	 * @return the myTreeItem oder Null wenn nicht gefunden
	 */
	public MyTreeItem findMyTreeItem(String uuid) {
		MyTreeItem myTreeItem = new MyTreeItem();
		myTreeItem=null;
		if(this.isHasChildren()){			//wenn Kinder vorhanden dann durchsuchen
			for (int i=0; i<children.size();i++){
				if (children.get(i).getUuid().equals(uuid)){
					myTreeItem=children.get(i);
					break;
				}else {
					findMyTreeItem(uuid);			//rekursiv durchlaufen
				}
			}
		}
		return myTreeItem;
	}
	

	/**
	 * Kennzeichnet die Datenbank
	 * Rekursiv durch alle Kinder
	 * @param datenbankName the datenbankName to set
	 */
	public void setDatenbankName(String datenbankName) {
		this.datenbankName = datenbankName;
		if ( children != null) {
			for (int i = 0; i < children.size(); i++) { //alle Kinder suchen
				children.get(i).setDatenbankName(datenbankName);
			}
		}
	}


	/**
	 * Name des zugehörigen Bildes
	 * @return the bildDateiname
	 */
	public String getBildDateiname() {
		return bildDateiname;
	}


	/**
	 * Name des zugehörigen Bildes
	 * @param bildDateiname the bildDateiname to set
	 */
	public void setBildDateiname(String bildDateiname) {
		this.dirty = true;
		this.setBildDateinameAlt(this.bildDateiname);
		this.bildDateiname = bildDateiname;
	}


	/**
	 * @return the birtReport
	 */
	public String getBirtReport() {
		return birtReport;
	}


	/**
	 * @param birtReport the birtReport to set
	 */
	public void setBirtReport(String birtReport) {
		this.dirty = true;
		this.birtReport = birtReport;
	}


	/**
	 * Name des zugehörigen Textes am Knoten
	 * @return the anzeigeText
	 */
	public String getAnzeigeText() {
		this.setAnzeigeText(	
				this.bezeichnung+
				" "+
//				this.variablenWert+
//				" "+
//				this.variablenEinheit+
//				" "+
				this.noOfChildren+
				"/"+
				this.totalNoOfChildren);
		
		return anzeigeText;
	}


	/**
	 * Name des zugehörigen Textes am Knoten
	 * @param anzeigeText the anzeigeText to set
	 */
	public void setAnzeigeText(String anzeigeText) {
		this.setAnzeigeTextAlt(this.anzeigeText);
		this.anzeigeText = anzeigeText;
	}


	/**
	 * Enhält das Objekt einer Auswertezeile (Überschrift)
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}


	/**
	 * Enhält das Objekt einer Auswertezeile (Überschrift)
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.dirty = true;
		this.setObjectAlt(this.object);
		this.object = object;
	}


	/**
	 * Datum der Erstellung
	 * @return the erstelldatum
	 */
	public String getErstelldatum() {
		return erstelldatum;
	}


	/**
	 * Name des Erstellers
	 * @return the ersteller
	 */
	public String getErsteller() {
		return ersteller;
	}


	/**
	 * Name des Erstellers
	 * @param ersteller the ersteller to set
	 */
	public void setErsteller(String ersteller) {
		this.dirty = true;
		this.ersteller = ersteller;
	}


	/**
	 * Name des letzen Bearbeiters
	 * @return the bearbeiter
	 */
	public String getBearbeiter() {
		return bearbeiter;
	}


	/**
	 * Name des letzen Bearbeiters
	 * @param bearbeiter the bearbeiter to set
	 */
	public void setBearbeiter(String bearbeiter) {
		this.dirty = true;
		this.bearbeiter = bearbeiter;
	}


	/**
	 * Datum der letzen Änderung
	 * @return the aenderungsdatum
	 */
	public String getAenderungsdatum() {
		return aenderungsdatum;
	}

	/**
	 * eigentlicher Datenwert/Inhalt der Variablen alt
	 * @return the variablenWertAlt
	 */
	public String getVariablenWertAlt() {
		return variablenWertAlt;
	}


	/**
	 * eigentlicher Datenwert/Inhalt der Variablen alt
	 * @param variablenWertAlt the variablenWertAlt to set
	 */
	private void setVariablenWertAlt(String variablenWertAlt) {
		this.variablenWertAlt = variablenWertAlt;
	}


	/**
	 * Größeneinheit alt
	 * @return the variablenEinheitAlt
	 */
	public String getVariablenEinheitAlt() {
		return variablenEinheitAlt;
	}


	/**
	 * Größeneinheit alt
	 * @param variablenEinheitAlt the variablenEinheitAlt to set
	 */
	private void setVariablenEinheitAlt(String variablenEinheitAlt) {
		this.variablenEinheitAlt = variablenEinheitAlt;
	}


	/**
	 * Auswahl Variabelntyp: Text,Zahl,Zeit usw. alt
	 * @return the variablentypAlt
	 */
	public String getVariablentypAlt() {
		return variablentypAlt;
	}


	/**
	 * Auswahl Variabelntyp: Text,Zahl,Zeit usw. alt
	 * @param variablentypAlt the variablentypAlt to set
	 */
	private void setVariablentypAlt(String variablentypAlt) {
		this.variablentypAlt = variablentypAlt;
	}


	/**
	 * Beschreibt den alten Parameter
	 * @return the beschreibungAlt
	 */
	public String getBeschreibungAlt() {
		return beschreibungAlt;
	}


	/**
	 * Beschreibt den alten Parameter
	 * @param beschreibungAlt the beschreibungAlt to set
	 */
	private void setBeschreibungAlt(String beschreibungAlt) {
		this.beschreibungAlt = beschreibungAlt;
	}


	/**
	 * bezeichnet den alten Parameter
	 * @return the bezeichnungAlt
	 */
	public String getBezeichnungAlt() {
		return bezeichnungAlt;
	}


	/**
	 * bezeichnet den alten Parameter
	 * @param bezeichnungAlt the bezeichnungAlt to set
	 */
	private void setBezeichnungAlt(String bezeichnungAlt) {
		this.bezeichnungAlt = bezeichnungAlt;
	}


	/**
	 * Name des zugehörigen alten Bildes
	 * @return the bildDateinameAlt
	 */
	public String getBildDateinameAlt() {
		return bildDateinameAlt;
	}


	/**
	 * Name des zugehörigen alten Bildes
	 * @param bildDateinameAlt the bildDateinameAlt to set
	 */
	private void setBildDateinameAlt(String bildDateinameAlt) {
		this.bildDateinameAlt = bildDateinameAlt;
	}


	/**
	 * Name des zugehörigen alten Anzeigetextes
	 * @return the anzeigeTextAlt
	 */
	public String getAnzeigeTextAlt() {
		this.dirty = true;
		return anzeigeTextAlt;
	}


	/**
	 * Name des zugehörigen alten Anzeigetextes
	 * @param anzeigeTextAlt the anzeigeTextAlt to set
	 */
	private void setAnzeigeTextAlt(String anzeigeTextAlt) {
		this.anzeigeTextAlt = anzeigeTextAlt;
	}


	/**
	 * Enhält das alte Objekt einer Auswertezeile (Überschrift)
	 * @return the objectAlt
	 */
	public Object getObjectAlt() {
		this.dirty = true;
		return objectAlt;
	}


	/**
	 * Enhält das alte Objekt einer Auswertezeile (Überschrift)
	 * @param objectAlt the objectAlt to set
	 */
	private void setObjectAlt(Object objectAlt) {
		this.objectAlt = objectAlt;
	}


	/**
	 * der Vater
	 * @return the parent
	 * @throws NoChildFoundException 
	 */
	public MyTreeItem getParent() {
		MyTreeItem myParentTreeItem = new MyTreeItem();
//		myTreeItem.setUuid(parentUUID);
//		myTreeItem.setDbIndex(dbIndex);
//		MyTreeItem myParentTreeItem = DBTools.seachMyTreeItem(myTreeItem);
		
//		if (!this.isWurzel()) {
//			myParentTreeItem = TreeTools.searchForUUID(
//					TreeTools.getMyTopTreeItem(), this.getParentUUID());
//			LogfileView.log("Suche nach Parent.UUID für Kind: "
//					+ this.getBezeichnung() + "  " + this.getUuid());
//			if (myParentTreeItem != null) {
//				LogfileView.log("Suche nach Parent.UUID erfolgreich: "
//						+ myParentTreeItem.getBezeichnung() + "  "
//						+ myParentTreeItem.getUuid());
//			} else {
//				LogfileView
//						.log("Suche nach Parent.UUID war nicht erfolgreich: ");
//			}
//		}else{
//			myParentTreeItem = null;
//		}
		return this.parent;
	}

	/**
	 * Anzahl der Kinder eines Knotens
	 * @return the noOfChildren
	 */
	public int getNoOfChildren() {
		return noOfChildren;
	}


	/**
	 * Anzahl der Kinder eines Knotens
	 * @param noOfChildren the noOfChildren to set
	 * 
	 */
	public void setNoOfChildren(int noOfChildren) {
		
		this.noOfChildren = noOfChildren;
	}


	/**
	 * Gesamtanzahl der Kinder unterhalb des Knotens	
	 * @return the totalNoOfChildren
	 */
	public int getTotalNoOfChildren() {

		return totalNoOfChildren;
	}

	/**
	 * Sortierreihenfolge
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * Sortierreihenfolge
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.dirty = true;
		this.index = index;
	}

	/**
	 * vorhergehende Kind
	 * @param preChild the preChild to set
	 */
	public void setPreChild(MyTreeItem preChild) {
		this.dirty = true;
		this.preChild = preChild;
	}


	/**
	 * nachfolgendes Kind
	 * @param postChild the postChild to set
	 */
	public void setPostChild(MyTreeItem postChild) {
		this.dirty = true;
		this.postChild = postChild;
	}


	/**
	 * Anzahl der Kinder
	 * @param children the children to set
	 */
	public void setChildren(List<MyTreeItem> children) {
		this.dirty = true;
		this.children = children;
	}
	
	/**
	 * @return ist True wenn ObersterKnoten
	 * <p>
	 * <b>IMPORTANT:</b>  Oberster Knoten ist immer der Anfang in einer Datei bzw. Datenbank
	 * <p>
	 */
	public boolean isObersterKnoten() {
		return isObersterKnoten;
	}

	/**
	 * @param isObersterKnoten auf True setzen wenn ObersterKnoten
	 * <p>
	 * <b>IMPORTANT:</b>  Oberster Knoten ist immer der Anfang in einer Datei bzw. Datenbank
	 * <p>
	 */
	public void setObersterKnoten(boolean isObersterKnoten) {
		this.dirty = true;
		this.isObersterKnoten = isObersterKnoten;
	}

	/**
	 * @return the dbIndex
	 * Enhält die Indexnummer der geöffneten Datenbankdatei
	 */
	public int getDbIndex() {
		return dbIndex;
	}

	/**
	 * Setzt die Indexnummer der geöffneten Datenbankdatei
	 * rekursiv durch alle Kinder
	 * 
	 * @param dbIndex the dbIndex to set
	 */
	public void setDbIndex(int dbIndex) { 	
		this.dbIndex = dbIndex;
		
		if (children != null) {
			for (int i = 0; i < children.size(); i++) { //alle Kinder suchen
				children.get(i).setDbIndex(dbIndex);
			}
		}
	}

	/**
	 * @return the isProjektBaum
	 * True wenn Knoten der oberste Projektbaum ist
	 * Enhält alle aktuelle geöffneten Projekte
	 * Darf nur einmal vorkommen!
	 */
	public boolean isProjektBaum() {
		return isProjektBaum;
	}

	/**
	 * @param isProjektBaum the isProjektBaum to set
	 * True wenn Knoten der oberste Projektbaum ist
	 * Enhält alle aktuelle geöffneten Projekte
	 * Darf nur einmal vorkommen!
	 */
	public void setIsProjektBaum(boolean isProjektBaum) {
		this.dirty = true;
		this.isProjektBaum = isProjektBaum;
		if (isProjektBaum)this.isProjekt = false;
	}

	/**
	 * @return the isProjekt
	 * True wenn Knoten der oberste Dateiknoten ist
	 * Darf nur einmal vorkommen pro Datei!
	 */
	public boolean isProjekt() {
		return isProjekt;
	}

	/**
	 * @param isProjekt the isProjekt to set
	 * True wenn Knoten der oberste Dateiknoten ist
	 * Darf nur einmal vorkommen pro Datei!
	 */
	public void setIsProjekt(boolean isProjekt) {
		this.dirty = true;
		this.isProjekt = isProjekt;
	}

	/**
	 * @return the isWurzel
	 */
	public boolean isWurzel() {
		return isWurzel;
	}

	/**
	 * @param isWurzel the isWurzel to set
	 */
	public void setWurzel(boolean isWurzel) {
		this.isWurzel = isWurzel;
	}

	/**
	 * @return the datenbankNameAlt
	 */
	public String getDatenbankNameAlt() {
		return datenbankNameAlt;
	}

	/**
	 * @param datenbankNameAlt the datenbankNameAlt to set
	 */
	private void setDatenbankNameAlt(String datenbankNameAlt) {
		this.datenbankNameAlt = datenbankNameAlt;
	}

	/**
	 * @return the isBasis
	 */
	public boolean isBasis() {
		return isBasis;
	}

	/**
	 * @param isBasis the isBasis to set
	 */
	public void setIsBasis(boolean isBasis) {
		this.dirty = true;
		this.isBasis = isBasis;
	}

	/**
	 * @return the isVorlage
	 */
	public boolean isVorlage() {
		return isVorlage;
	}

	/**
	 * @param isVorlage the isVorlage to set
	 */
	public void setIsVorlage(boolean isVorlage) {
		this.dirty = true;
		this.isVorlage = isVorlage;
	}

	/**
	 * wenn sich die Anzahl verändert hat, rekursiv noch oben laufen
	 * 
	 * @param totalNoOfChildren the totalNoOfChildren to set
	 * @return the totalNoOfChildren
	 */
	public void setTotalNoOfChildren() {
		countTotalNoOfChildren();
		LogfileView.log("Aktualisiere totalNoOfChildren: " + this.totalNoOfChildren);
			if(this.getParent() != null){
				if (!this.isWurzel()) {
					this.getParent().setTotalNoOfChildren();
				}
			}
			totalNoOfChildrenAlt = totalNoOfChildren;
	}
	
	/**
	 * zählt alle Kinder und Kindeskinder
	 */
	private void countTotalNoOfChildren() {
		int count = 0;
		for (int i=0; i<children.size();i++){
			count = count + children.get(i).getTotalNoOfChildren();
		}
		totalNoOfChildren = count + this.children.size();
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Generiert eine neue UUID
	 * Rekursiv durch alle Kinder
	 */
	public void setUuid() {
		if (children != null) {
			for (int i = 0; i < children.size(); i++) { //alle Kinder suchen
				children.get(i).setUuid();
			}
		}		
		this.uuid=UUID.randomUUID().toString();
	}
	
	/**
	 * @param parent the parent to set
	 */
	public void setParent(MyTreeItem parent) {
		
		if (this.parent != null) {
			if (parent != null) {
				this.parentUUID = parent.getUuid();
			}
		}
		this.parent = parent;
	}

	/**
	 * @return the isStrukturknoten
	 * Ein Strukturknoten gehört zur festen Baumstruktur und darf weder kopiert, verschoben oder gelöscht werden
	 */
	public boolean isStrukturknoten() {
		return isStrukturknoten;
	}

	/**
	 * @param isStrukturknoten the isStrukturknoten to set
	 * Ein Strukturknoten gehört zur festen Baumstruktur und darf weder kopiert, verschoben oder gelöscht werden
	 */
	public void setStrukturknoten(boolean isStrukturknoten) {
		this.isStrukturknoten = isStrukturknoten;
	}

	/**
	 * @return the isDummy
	 * erlaubt das Einfügen von einem neuen Zweig mit Kindern
	 */
	public boolean isDummy() {
		return isDummy;
	}

	/**
	 * @param isDummy the isDummy to set
	 * erlaubt das Einfügen von einem neuen Zweig mit Kindern
	 * hat keinen Parameter oder Eigenschaft
	 */
	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return Link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		Link = link;
	}

	/**
	 * @return the parentUUID
	 */
	public String getParentUUID() {
		return parentUUID;
	}

	/**
	 * @param parentUUID the parentUUID to set
	 */
	public void setParentUUID(String parentUUID) {
		this.parentUUID = parentUUID;
	}

	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
}
