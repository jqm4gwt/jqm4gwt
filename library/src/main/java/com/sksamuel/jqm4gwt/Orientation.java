package com.sksamuel.jqm4gwt;

public enum Orientation { 
    HORIZONTAL("horizontal"), VERTICAL("vertical");

    private final String jqmValue;

	private Orientation(String value) {
		this.jqmValue = value;
	}

	/**
	 * Returns the string value that JQM expects
	 */
	public String getJqmValue() {
		return jqmValue;
	}     
}
