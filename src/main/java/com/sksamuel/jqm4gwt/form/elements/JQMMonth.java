package com.sksamuel.jqm4gwt.form.elements;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 16:42:27
 * 
 *         A convenience implementation of a select input that contains the 12
 *         months of the Gregorian calender.
 */
public class JQMMonth extends JQMSelect {

	/**
	 * Create a month select element using numbers for the month names.
	 * 
	 * @param text
	 *              the label text
	 */
	public JQMMonth(String text) {
		super(text);

		addOption("01");
		addOption("02");
		addOption("03");
		addOption("04");
		addOption("05");
		addOption("06");
		addOption("07");
		addOption("08");
		addOption("09");
		addOption("10");
		addOption("11");
		addOption("12");

	}

	/**
	 * Creates a new select widget populated with the 12 months with the
	 * values 01 to 12, and using the names for the months taken from the
	 * months array.
	 * 
	 * @param text
	 *              the label text
	 * 
	 * @param months
	 *              an array of month names
	 */
	public JQMMonth(String text, String[] months) {
		super(text);

		addOption("01", months[0]);
		addOption("02", months[1]);
		addOption("03", months[2]);
		addOption("04", months[3]);
		addOption("05", months[4]);
		addOption("06", months[5]);
		addOption("07", months[6]);
		addOption("08", months[7]);
		addOption("09", months[8]);
		addOption("10", months[9]);
		addOption("11", months[10]);
		addOption("12", months[11]);

	}
}
