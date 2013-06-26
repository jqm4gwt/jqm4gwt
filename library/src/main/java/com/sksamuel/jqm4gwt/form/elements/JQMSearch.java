package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 18 May 2011 04:17:45
 * 
 *         Text element stylised as a search box.
 * 
 * @link 
 *       http://jquerymobile.com/demos/1.0b1/#/demos/1.0b1/docs/forms/forms-search
 *       .html
 * 
 */
public class JQMSearch extends JQMText {

	/**
	 * Create a new {@link JQMSearch} with no label text
	 * 
	 */
	public JQMSearch() {
		this(null);
	}

	/**
	 * Create a new {@link JQMSearch} with the given label text
	 * 
	 * @param text
	 *              the text to use as the label
	 */
	public JQMSearch(String text) {
		super(text);
		setType("search");
	}

}
