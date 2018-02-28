package org.translator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
                if (resourceBundle.getString(key) != null) {
                    return resourceBundle.getString(key);
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
        if(key != null) {
            key = key.substring(0, key.lastIndexOf(".") + 1) + locale;
            ResourceBundle resourceBundle = T18N.resourceBundles.get(view.getClass().getPackage());
            if (resourceBundle.getString(key) != null) {
                return resourceBundle.getString(key);
            }
        }
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

    // public static String L(String text, int... counts) {
    // return localize(text, counts);
    // }
    //
    // public static String L(String text, Locale locale, int... counts) {
    // return localize(text, locale, counts);
    // }
    //
    // public static String L(Object view, String text, int... counts) {
    // return localize(view, text, counts);
    // }
    //
    // public static String L(Object view, String text, Locale locale, int...
    // counts) {
    // return localize(view, text, locale, counts);
    // }

    private static String findKey(String text) {
        return findKey(text, Locale.getDefault());
    }

    private static String findKey(String text, Locale locale) {
        return findKey((Package) null, text, locale);
    }

    private static String findKey(Package pack, String text) {
        return findKey(pack, text, Locale.getDefault());
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

    private static String findKey(ResourceBundle resourceBundle, String text,
            Locale locale) {
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
