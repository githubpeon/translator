package org.translator;

import java.util.Locale;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class T18NTest {

	public static class T18NTestModule extends JukitoModule {
		@Override
		protected void configureTest() {
		}
	}

	@Test
	public void localizeTest() {
		System.out.println(T18N.findKey(T18N.class.getPackage(), "org.translator.1.en_US", Locale.getDefault()));

		// assertEquals("Hello World!", L("org.translator.1.en_US"));
		// assertEquals("org.translator.1.en_US", L("Hello World!", Locale.getDefault()));
		// assertEquals("org.translator.1.sv_SE", L("Hello World!", new Locale("sv", "SE")));
		//
		// assertEquals("org.translator.1", L("Hello World!"));
		// assertEquals("org.translator.1", L("Hello World!", Locale.getDefault()));
		// assertEquals("org.translator.1", L("Hello World!", new Locale("sv", "SE")));
		//
		// assertEquals("Hello World!", L(this, "Hello World!"));
		// assertEquals("Hello World!", L(this, "Hello World!", Locale.getDefault()));
		// assertEquals("Hej Varld!", L(this, "Hello World!", new Locale("sv", "SE")));
		//
		// assertEquals("The form contains 1 error and 2 warnings.", L("The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", 1, 2));
		// assertEquals("The form contains 2 errors and 1 warning.", L("The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", Locale.getDefault(), 2, 1));
		// assertEquals("Formularet innehaller 1 fel och 2 varningar.", L("The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", new Locale("sv", "SE"), 1, 2));
		//
		// assertEquals("The form contains 1 error and 2 warnings.", L(this, "The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", 1, 2));
		// assertEquals("The form contains 2 errors and 1 warning.", L(this, "The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", Locale.getDefault(), 2, 1));
		// assertEquals("Formularet innehaller 1 fel och 2 varningar.", L(this, "The form contains {0} #{1=error,?=errors} and {1} #{1=warning,?=warnings}.", new Locale("sv", "SE"), 1, 2));
	}

}
