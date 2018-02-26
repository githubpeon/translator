package org.translator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class T18N {

	private final static Map<Package, ResourceBundle> resourceBundles = new HashMap<Package, ResourceBundle>();

	static {
		for (Package pack : Package.getPackages()) {
			try {
				ResourceBundle resourceBundle = ResourceBundle.getBundle(pack.getName() + "." + "t18n");
				resourceBundles.put(pack, resourceBundle);
			} catch (MissingResourceException e) {
			}
		}
	}

	public static String findKey(String text) {
		return findKey(text, Locale.getDefault());
	}

	public static String findKey(String text, Locale locale) {
		return findKey(null, text, locale);
	}

	public static String findKey(Package pack, String text) {
		return findKey(pack, text, Locale.getDefault());
	}

	public static String findKey(Package pack, String text, Locale locale) {
		if (pack != null) {
			ResourceBundle resourceBundle = T18N.resourceBundles.get(pack);
			if (resourceBundle.containsKey(text)) {
				return text;
			} else if (resourceBundle.containsKey(text + "." + locale)) {
				return text + "." + locale;
			}
		} else {

		}
		return null;
	}
}
