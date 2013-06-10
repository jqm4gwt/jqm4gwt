package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 9 Jul 2011 14:23:25
 * 
 *         This enum represents the default iconset available in jquery mobile.
 * 
 *         To see what the icon set looks like, visit the following site:
 * 
 * @link http://jquerymobile.com/test/docs/buttons/buttons-icons.html
 * 
 */
public enum DataIcon {

	GEAR("gear"), LEFT("arrow-l"), RIGHT("arrow-r"), UP("arrow-u"), DOWN("arrow-d"), DELETE("delete"), PLUS("plus"), MINUS(
			"minus"), CHECK("check"), REFRESH("refresh"), FORWARD("forward"), BACK("back"), GRID("grid"), STAR(
			"star"), ALERT("alert"), INFO("info"), HOME("home"), SEARCH("search"), BARS("bars"), EDIT("edit"), ;

	private final String	jqmValue;

	private DataIcon(String jqmValue) {
		this.jqmValue = jqmValue;
	}

	/**
	 * Returns the string value that JQM expects
	 */
	public String getJqmValue() {
		return jqmValue;
	}

}
