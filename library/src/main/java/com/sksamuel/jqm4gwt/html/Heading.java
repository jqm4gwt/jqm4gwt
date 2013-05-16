package com.sksamuel.jqm4gwt.html;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sksamuel.jqm4gwt.HasText;

/**
 * @author Stephen K Samuel samspade79@gmail.com 11 Jul 2011 13:38:38
 * 
 *         An implementation of a H{n} element.
 * 
 */
public class Heading extends Widget implements HasText<Heading> {

	public Heading(int n) {
		setElement(Document.get().createHElement(n));
	}

	public Heading(int i, String text) {
		this(i);
		setText(text);
	}

	@Override
	public String getText() {
		return getElement().getInnerText();
	}

	@Override
	public void setText(String text) {
		getElement().setInnerText(text);
	}

    @Override
    public Heading withText(String text) {
        setText(text);
        return this;
    }

    public static class H1 extends Heading {
        public H1() {
            super(1);
        }
    }

    public static class H2 extends Heading {
        public H2() {
            super(2);
        }
    }

    public static class H3 extends Heading {
        public H3() {
            super(3);
        }
    }

    public static class H4 extends Heading {
        public H4() {
            super(4);
        }
    }

    public static class H5 extends Heading {
        public H5() {
            super(5);
        }
    }

    public static class H6 extends Heading {
        public H6() {
            super(6);
        }
    }
}
