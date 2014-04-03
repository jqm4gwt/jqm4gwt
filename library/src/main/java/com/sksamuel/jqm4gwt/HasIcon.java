package com.sksamuel.jqm4gwt;

/**
 * @author Stephen K Samuel samspade79@gmail.com 13 Jul 2011 23:08:58
 *
 *       * <h3>Use in UiBinder Templates</h3>
 *
 * Using components implementing HasIcon can define which Icon to use in
 * {@link com.google.gwt.uibinder.client.UiBinder UiBinder} templates in defining it in the 'icon attribute.
 * For example:
 * <pre>
 * &lt;jqm:button.JQMButton icon="RIGHT"/>
 * </pre>
 *
 * Valid values for the icon attribute are defined in @see DataIcon
 *
 */
public interface HasIcon<T> extends HasIconPos<T> {

	/**
	 * Removes any icon previously set on the implementing class, i.e. reverts back to default icon (if any).
	 * <p/> If no icon has been previously set then this has no effect.
	 * <p/> To hide icon use {@link HasIcon#setBuiltInIcon(DataIcon)} with DataIcon.NONE parameter.
	 */
	T removeIcon();

    /**
   	 * Sets the data icon to use, overriding any previously built-in or URL-defined icons specified.
   	 *
   	 * @param icon  of the standard built in icon types
   	 */
   	void setBuiltInIcon(DataIcon icon);

   	/**
   	 * Sets the icon to be a custom URL, overriding any previously built-in or URL-defined icons specified.
   	 *
   	 * @param src
   	 *              the src of the custom icon
   	 */
   	void setIconURL(String src);

	/**
	 * Sets the data icon to use, overriding any previously built-in or URL-defined icons specified.
	 *
	 * @param icon
	 *              of the standard built in icon types
	 */
	T withBuiltInIcon(DataIcon icon);

	/**
	 * Sets the icon to be a custom URL, overriding any previously built-in or URL-defined icons specified.
	 *
	 * @param src
	 *              the src of the custom icon
	 */
	T withIconURL(String src);

}
