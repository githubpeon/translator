package org.translator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.translator.T18N.L;
import static org.translator.T18N.P;
import static org.translator.T18N.localize;

import java.util.Locale;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.translator.annotation.enUS;
import org.translator.annotation.svSE;

@RunWith(JukitoRunner.class)
public class T18NTest {

	private String[] helloWorld = { "en_US:Hello World!", "sv_SE:Hej Värld!" };
	private String[] errors = { "en_US:The form contains #{1=error,?=errors} and #{1=warning,?=warnings}.", "sv_SE:Formularet innehaller #{?=fel} och #{1=varning,?=varningar}." };

	@enUS("Hello World!")
	@svSE("Hej Värld!")
	private String helloWorldAnnotation;

	@enUS("The form contains #{1=error,?=errors} and #{1=warning,?=warnings}.")
	@svSE("Formularet innehaller #{?=fel} och #{1=varning,?=varningar}.")
	private String errorsAnnotation;

	public static class T18NTestModule extends JukitoModule {
		@Override
		protected void configureTest() {
		}
	}

	@Test
	public void localizeTest() {
		assertEquals("Hello World!", L("Hello World!"));
		assertEquals("Hello World!", L("Hello World!", Locale.getDefault()));
		assertEquals("Hej Varld!", L("Hello World!", new Locale("sv", "SE")));

		assertEquals("Hello World!", L("org.translator.1"));
		assertEquals("Hello World!", L("org.translator.1", Locale.getDefault()));
		assertEquals("Hej Varld!", L("org.translator.1", new Locale("sv", "SE")));

		assertEquals("Hello World!", L("org.translator.1.en_US"));
		assertEquals("Hej Varld!", L("org.translator.1.sv_SE", new Locale("sv", "SE")));

		assertEquals("Hello World!", L(this, "Hello World!"));
		assertEquals("Hello World!", L(this, "Hello World!", Locale.getDefault()));
		assertEquals("Hej Varld!", L(this, "Hello World!", new Locale("sv", "SE")));

		assertEquals("The form contains 1 error and 2 warnings.", L("The form contains #{errors} and #{warnings}.", 1, 2));
		assertEquals("The form contains 2 errors and 1 warning.", L("The form contains #{errors} and #{warnings}.", Locale.getDefault(), 2, 1));
		assertEquals("Formularet innehaller 1 fel och 2 varningar.", L("The form contains #{errors} and #{warnings}.", new Locale("sv", "SE"), 1, 2));

		assertEquals("The form contains 1 error and 2 warnings.", L(this, "The form contains #{errors} and #{warnings}.", 1, 2));
		assertEquals("The form contains 2 errors and 1 warning.", L(this, "The form contains #{errors} and #{warnings}.", Locale.getDefault(), 2, 1));
		assertEquals("Formularet innehaller 1 fel och 2 varningar.", L(this, "The form contains #{errors} and #{warnings}.", new Locale("sv", "SE"), 1, 2));

		assertEquals("Hello World!", L(helloWorld));
		assertEquals("Hello World!", L(helloWorld, Locale.getDefault()));
		assertEquals("Hej Värld!", L(helloWorld, new Locale("sv", "SE")));

		assertEquals("The form contains 1 error and 2 warnings.", L(errors, 1, 2));
		assertEquals("The form contains 2 errors and 1 warning.", L(errors, Locale.getDefault(), 2, 1));
		assertEquals("Formularet innehaller 1 fel och 2 varningar.", L(errors, new Locale("sv", "SE"), 1, 2));

		assertNull(helloWorldAnnotation);

		localize(this);
		assertEquals("Hello World!", helloWorldAnnotation);
		assertEquals("The form contains 1 error and 2 warnings.", P(errorsAnnotation, 1, 2));

		localize(Locale.getDefault(), this);
		assertEquals("Hello World!", helloWorldAnnotation);
		assertEquals("The form contains 2 errors and 1 warning.", P(errorsAnnotation, 2, 1));

		localize(new Locale("sv", "SE"), this);
		assertEquals("Hej Värld!", helloWorldAnnotation);
		assertEquals("Formularet innehaller 1 fel och 2 varningar.", P(errorsAnnotation, 1, 2));

	}
}
