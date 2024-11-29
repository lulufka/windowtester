package abbot.i18n;

import abbot.Log;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Provides i18n support.
 */
public class Strings {

  private static final String BUNDLE = "abbot.i18n.StringsBundle";

  private static final Set<ResourceBundle> bundles = new HashSet<>();
  private static final Map<String, MessageFormat> formats = new HashMap<>();

  static {
    String language = System.getProperty("abbot.locale.language");
    if (language != null) {
      String country = System.getProperty("abbot.locale.country", language.toUpperCase());
      String variant = System.getProperty("abbot.locale.variant", "");
      Locale locale = new Locale(language, country, variant);
      Locale.setDefault(locale);
      System.out.println("Using locale " + locale);
    }
    addBundle(BUNDLE);
  }

  private Strings() {
  }

  public static void addBundle(String bundle) {
    Locale locale = Locale.getDefault();
    try {
      bundles.add(ResourceBundle.getBundle(bundle, locale));
    } catch (MissingResourceException mre) {
      String msg = "No resource bundle found in " + bundle;
      if (System.getProperty("java.class.path").contains("eclipse")) {
        Log.warn(msg + ": copy one into your project output dir or run the ant build");
      } else {
        throw new Error(msg);
      }
    }
  }

  public static String get(String key) {
    return get(key, false);
  }

  public static String get(String key, boolean optional) {
    String defaultValue = "#" + key + "#";
    String value = null;

    for (ResourceBundle local : bundles) {
      try {
        value = local.getString(key);
      } catch (MissingResourceException mre) {
        // do nothing
      }
    }

    if (value == null
        && !optional) {
      Log.log("Missing resource '" + key + "'");
      value = defaultValue;
    }
    return value;
  }

  public static String get(String key, Object[] args) {
    MessageFormat fmt = formats.computeIfAbsent(key, k -> new MessageFormat(get(k)));
    return fmt.format(args);
  }
}
