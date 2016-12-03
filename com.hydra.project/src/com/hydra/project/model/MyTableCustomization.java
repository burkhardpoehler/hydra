/**
 * 
 */
package com.hydra.project.model;

/**
 * @author Poehler
 *
 */
public class MyTableCustomization {

	private String directory;
	private String filter;
	private String xml;
	private String id;
	
	/**
	 * @author Poehler
	 * @param dir the directorypath
	 * @param fltr the filefilter
	 * @param xml the xml value
	 * @param id the id value
	 */
	public MyTableCustomization(String _dir, String _fltr, String _xml, String _id) {
	    this.directory = _dir;
	    this.filter = _fltr;
	    this.xml = _xml;
	    this.id = _id;
	    
	}
	
	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
	/**
	 * @return the xml
	 */
	public String getXml() {
		return xml;
	}
	/**
	 * @param xml the xml to set
	 */
	public void setXml(String xml) {
		this.xml = xml;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
