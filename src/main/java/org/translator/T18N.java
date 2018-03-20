package org.translator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.translator.annotation.enUS;
import org.translator.annotation.esES;
import org.translator.annotation.etEE;
import org.translator.annotation.fiFI;
import org.translator.annotation.frFR;
import org.translator.annotation.itIT;
import org.translator.annotation.jaJP;
import org.translator.annotation.koKR;
import org.translator.annotation.ruRU;
import org.translator.annotation.svFI;
import org.translator.annotation.svSE;
import org.translator.annotation.zhCN;

public class T18N {

	private final static Map<Package, ResourceBundle> resourceBundles = new HashMap<Package, ResourceBundle>();
	private final static Pattern pluralPattern = Pattern.compile("\\#\\{(.*?)\\}");

	static {
		for (Package pack : Package.getPackages()) {
			try {
				ResourceBundle resourceBundle = ResourceBundle.getBundle(pack.getName() + "." + "t18n");
				resourceBundles.put(pack, resourceBundle);
			} catch (MissingResourceException e) {
			}
		}
	}

	public static String localize(String text) {
		return localize(text, Locale.getDefault());
	}

	public static String localize(String text, Locale locale) {
		String key = findKey(text, locale);
		if (key != null) {
			key = key.substring(0, key.lastIndexOf(".") + 1) + locale;
			for (ResourceBundle resourceBundle : T18N.resourceBundles.values()) {
				try {
					return resourceBundle.getString(key);
				} catch (MissingResourceException e) {
				}
			}
		}
		return text;
	}

	public static String localize(Object view, String text) {
		return localize(view, text, Locale.getDefault());
	}

	public static String localize(Object view, String text, Locale locale) {
		String key = findKey(view.getClass().getPackage(), text, locale);
		if (key != null) {
			key = key.substring(0, key.lastIndexOf(".") + 1) + locale;
			ResourceBundle resourceBundle = T18N.resourceBundles.get(view.getClass().getPackage());
			if (resourceBundle.getString(key) != null) {
				return resourceBundle.getString(key);
			}
		}
		return text;
	}

	public static String localize(String text, int... counts) {
		return localize(text, Locale.getDefault(), counts);
	}

	public static String localize(String text, Locale locale, int... counts) {
		text = localize(text, locale);
		text = pluralize(text, counts);
		return text;
	}

	public static String localize(Object view, String text, int... counts) {
		return localize(view, text, Locale.getDefault(), counts);
	}

	public static String localize(Object view, String text, Locale locale, int... counts) {
		text = localize(view, text, locale);
		text = pluralize(text, counts);
		return text;
	}

	public static String L(String text) {
		return localize(text);
	}

	public static String L(String text, Locale locale) {
		return localize(text, locale);
	}

	public static String L(Object view, String text) {
		return localize(view, text);
	}

	public static String L(Object view, String text, Locale locale) {
		return localize(view, text, locale);
	}

	public static String L(String text, int... counts) {
		return localize(text, counts);
	}

	public static String L(String text, Locale locale, int... counts) {
		return localize(text, locale, counts);
	}

	public static String L(Object view, String text, int... counts) {
		return localize(view, text, counts);
	}

	public static String L(Object view, String text, Locale locale, int... counts) {
		return localize(view, text, locale, counts);
	}

	public static String L(String[] texts) {
		return L(texts, Locale.getDefault());
	}

	/**
	 * We expect the strings to have the format localeIso:text. Ex: "en_US:Hello World!"
	 * 
	 * @param texts Array of localeIso:text pairs
	 * @param locale The locale we want to find a text for.
	 * @return The text for the given locale or null if none was found.
	 */
	public static String L(String[] texts, Locale locale) {
		for (String text : texts) {
			if (text.startsWith(locale.toString())) {
				return text.substring(6);
			}
		}
		return null;
	}

	public static String L(String[] texts, int... counts) {
		return L(texts, Locale.getDefault(), counts);
	}

	public static String L(String[] texts, Locale locale, int... counts) {
		String text = L(texts, locale);
		text = pluralize(text, counts);
		return text;
	}

	public static void localize(Object object) {
		localize(Locale.getDefault(), object);
	}

	public static void localize(Locale locale, Object object) {
		for (Field field : getAllFields(object)) {
			String value = localize(locale, field);
			if (value != null) {
				try {
					field.setAccessible(true);
					field.set(object, value);
				} catch (IllegalAccessException e) {
				}
			}
		}
	}

	private static List<Field> getAllFields(Object object) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		return fields;
	}

	public static String pluralize(String text, int... counts) {
		Matcher matcher = T18N.pluralPattern.matcher(text);
		for (int i = 0; matcher.find(); ++i) {
			Map<Integer, String> countMap = new HashMap<Integer, String>();
			for (String countString : matcher.group(1).split(",")) {
				String[] keyValue = countString.split("=");
				Integer count = null;
				try {
					count = Integer.parseInt(keyValue[0]);
				} catch (NumberFormatException e) {
				}
				countMap.put(count, keyValue[1]);
			}
			String pluralized = countMap.get(counts[i]);
			if (pluralized == null) {
				pluralized = countMap.get(null);
			}
			text = text.replace("#{" + matcher.group(1) + "}", counts[i] + " " + pluralized);
		}
		return text;
	}

	public static String P(String text, int... counts) {
		return pluralize(text, counts);
	}

	private static String localize(Locale locale, Field field) {
		try {
			switch (locale.toString()) {
				case "en_US":
					return field.getAnnotation(enUS.class).value();
				case "es_ES":
					return field.getAnnotation(esES.class).value();
				case "fr_FR":
					return field.getAnnotation(frFR.class).value();
				case "it_IT":
					return field.getAnnotation(itIT.class).value();
				case "ja_JP":
					return field.getAnnotation(jaJP.class).value();
				case "ko_KR":
					return field.getAnnotation(koKR.class).value();
				case "ru_RU":
					return field.getAnnotation(ruRU.class).value();
				case "zh_CN":
					return field.getAnnotation(zhCN.class).value();
				case "fi_FI":
					return field.getAnnotation(fiFI.class).value();
				case "sv_FI":
					return field.getAnnotation(svFI.class).value();
				case "et_EE":
					return field.getAnnotation(etEE.class).value();
				case "sv_SE":
					return field.getAnnotation(svSE.class).value();
				default:
					return null;
			}
		} catch (NullPointerException e) {
			return null;
		}
	}

	private static String findKey(String text, Locale locale) {
		return findKey((Package) null, text, locale);
	}

	private static String findKey(Package pack, String text, Locale locale) {
		if (pack != null) {
			ResourceBundle resourceBundle = T18N.resourceBundles.get(pack);
			if (resourceBundle != null) {
				return findKey(resourceBundle, text, locale);
			}
		} else {
			for (ResourceBundle resourceBundle : T18N.resourceBundles.values()) {
				String key = findKey(resourceBundle, text, locale);
				if (key != null) {
					return key;
				}
			}
		}
		return null;
	}

	private static String findKey(ResourceBundle resourceBundle, String text, Locale locale) {
		if (resourceBundle.containsKey(text)) {
			return text;
		} else if (resourceBundle.containsKey(text + "." + locale)) {
			return text + "." + locale;
		} else {
			for (String key : resourceBundle.keySet()) {
				String value = resourceBundle.getString(key);
				if (text.replaceAll(pluralPattern.pattern(), "").equals(value.replaceAll(pluralPattern.pattern(), ""))) {
					return key;
				}
			}
		}
		return null;
	}

}
