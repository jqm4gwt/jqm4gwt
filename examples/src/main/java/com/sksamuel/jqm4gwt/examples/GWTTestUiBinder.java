package com.sksamuel.jqm4gwt.examples;

import com.sksamuel.jqm4gwt.examples.uibinder.TestView1;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase.
 * Using <code>"GwtTest*"</code> naming pattern exclude them from running with
 * surefire during the test phase.
 * <p/>
 * If you run the tests using the Maven command line, you will have to
 * navigate with your browser to a specific url given by Maven.
 * See http://mojo.codehaus.org/gwt-maven-plugin/user-guide/testing.html
 * for details.
 *
 * @author jraymond
 *         Date: 4/4/13
 *         Time: 9:09 AM
 */
public class GWTTestUiBinder /* extends GWTTestCase */ {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "com.sksamuel.jqm4gwt.jqm4gwtJUnit";
    }


    /**
     * This test simply brings up a UiBinder generated view.
     */
    public void onModuleLoad() {
        TestView1 view1 = new TestView1();
        view1.show();
    }

    public void testUiBinderView() {
//        assertTrue(true); // This proves successful compilation.
    }


}
