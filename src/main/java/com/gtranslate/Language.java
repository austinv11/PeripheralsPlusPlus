package com.gtranslate;

import com.gtranslate.Translator;
import java.util.HashMap;

public class Language {

   public static final String AFRIKAANS = "af";
   public static final String ALBANIAN = "sq";
   public static final String ARABIC = "ar";
   public static final String ARMENIAN = "hy";
   public static final String AZERBAIJANI = "az";
   public static final String BASQUE = "eu";
   public static final String BELARUSIAN = "be";
   public static final String BENGALI = "bn";
   public static final String BULGARIAN = "bg";
   public static final String CATALAN = "ca";
   public static final String CHINESE = "zh-CN";
   public static final String CROATIAN = "hr";
   public static final String CZECH = "cs";
   public static final String DANISH = "da";
   public static final String DUTCH = "nl";
   public static final String ENGLISH = "en";
   public static final String ESTONIAN = "et";
   public static final String FILIPINO = "tl";
   public static final String FINNISH = "fi";
   public static final String FRENCH = "fr";
   public static final String GALICIAN = "gl";
   public static final String GEORGIAN = "ka";
   public static final String GERMAN = "de";
   public static final String GREEK = "el";
   public static final String GUJARATI = "gu";
   public static final String HAITIAN_CREOLE = "ht";
   public static final String HEBREW = "iw";
   public static final String HINDI = "hi";
   public static final String HUNGARIAN = "hu";
   public static final String ICELANDIC = "is";
   public static final String INDONESIAN = "id";
   public static final String IRISH = "ga";
   public static final String ITALIAN = "it";
   public static final String JAPANESE = "ja";
   public static final String KANNADA = "kn";
   public static final String KOREAN = "ko";
   public static final String LATIN = "la";
   public static final String LATVIAN = "lv";
   public static final String LITHUANIAN = "lt";
   public static final String MACEDONIAN = "mk";
   public static final String MALAY = "ms";
   public static final String MALTESE = "mt";
   public static final String NORWEGIAN = "no";
   public static final String PERSIAN = "fa";
   public static final String POLISH = "pl";
   public static final String PORTUGUESE = "pt";
   public static final String ROMANIAN = "ro";
   public static final String RUSSIAN = "ru";
   public static final String SERBIAN = "sr";
   public static final String SLOVAK = "sk";
   public static final String SLOVENIAN = "sl";
   public static final String SPANISH = "es";
   public static final String SWAHILI = "sw";
   public static final String SWEDISH = "sv";
   public static final String TAMIL = "ta";
   public static final String TELUGU = "te";
   public static final String THAI = "th";
   public static final String TURKISH = "tr";
   public static final String UKRAINIAN = "uk";
   public static final String URDU = "ur";
   public static final String VIETNAMESE = "vi";
   public static final String WELSH = "cy";
   public static final String YIDDISH = "yi";
   public static final String CHINESE_SIMPLIFIED = "zh-CN";
   public static final String CHINESE_TRADITIONAL = "zh-TW";
   private static Language language;
   private HashMap hashLanguage = new HashMap();


   private Language() {
      this.init();
   }

   public static synchronized Language getInstance() {
      if(language == null) {
         language = new Language();
      }

      return language;
   }

   private void init() {
      this.hashLanguage.put("af", "AFRIKAANS");
      this.hashLanguage.put("sq", "ALBANIAN");
      this.hashLanguage.put("ar", "ARABIC");
      this.hashLanguage.put("hy", "ARMENIAN");
      this.hashLanguage.put("az", "AZERBAIJANI");
      this.hashLanguage.put("eu", "BASQUE");
      this.hashLanguage.put("be", "BELARUSIAN");
      this.hashLanguage.put("bn", "BENGALI");
      this.hashLanguage.put("bg", "BULGARIAN");
      this.hashLanguage.put("ca", "CATALAN");
      this.hashLanguage.put("zh-CN", "CHINESE");
      this.hashLanguage.put("hr", "CROATIAN");
      this.hashLanguage.put("cs", "CZECH");
      this.hashLanguage.put("da", "DANISH");
      this.hashLanguage.put("nl", "DUTCH");
      this.hashLanguage.put("en", "ENGLISH");
      this.hashLanguage.put("et", "ESTONIAN");
      this.hashLanguage.put("tl", "FILIPINO");
      this.hashLanguage.put("fi", "FINNISH");
      this.hashLanguage.put("fr", "FRENCH");
      this.hashLanguage.put("gl", "GALICIAN");
      this.hashLanguage.put("ka", "GEORGIAN");
      this.hashLanguage.put("de", "GERMAN");
      this.hashLanguage.put("el", "GREEK");
      this.hashLanguage.put("gu", "GUJARATI");
      this.hashLanguage.put("ht", "HAITIAN_CREOLE");
      this.hashLanguage.put("iw", "HEBREW");
      this.hashLanguage.put("hi", "HINDI");
      this.hashLanguage.put("hu", "HUNGARIAN");
      this.hashLanguage.put("is", "ICELANDIC");
      this.hashLanguage.put("id", "INDONESIAN");
      this.hashLanguage.put("ga", "IRISH");
      this.hashLanguage.put("it", "ITALIAN");
      this.hashLanguage.put("ja", "JAPANESE");
      this.hashLanguage.put("kn", "KANNADA");
      this.hashLanguage.put("ko", "KOREAN");
      this.hashLanguage.put("la", "LATIN");
      this.hashLanguage.put("lv", "LATVIAN");
      this.hashLanguage.put("lt", "LITHUANIAN");
      this.hashLanguage.put("mk", "MACEDONIAN");
      this.hashLanguage.put("ms", "MALAY");
      this.hashLanguage.put("mt", "MALTESE");
      this.hashLanguage.put("no", "NORWEGIAN");
      this.hashLanguage.put("fa", "PERSIAN");
      this.hashLanguage.put("pl", "POLISH");
      this.hashLanguage.put("pt", "PORTUGUESE");
      this.hashLanguage.put("ro", "ROMANIAN");
      this.hashLanguage.put("ru", "RUSSIAN");
      this.hashLanguage.put("sr", "SERBIAN");
      this.hashLanguage.put("sk", "SLOVAK");
      this.hashLanguage.put("sl", "SLOVENIAN");
      this.hashLanguage.put("es", "SPANISH");
      this.hashLanguage.put("sw", "SWAHILI");
      this.hashLanguage.put("sv", "SWEDISH");
      this.hashLanguage.put("ta", "TAMIL");
      this.hashLanguage.put("te", "TELUGU");
      this.hashLanguage.put("th", "THAI");
      this.hashLanguage.put("tr", "TURKISH");
      this.hashLanguage.put("uk", "UKRAINIAN");
      this.hashLanguage.put("ur", "URDU");
      this.hashLanguage.put("vi", "VIETNAMESE");
      this.hashLanguage.put("cy", "WELSH");
      this.hashLanguage.put("yi", "YIDDISH");
      this.hashLanguage.put("af", "AFRIKAANS");
      this.hashLanguage.put("sq", "ALBANIAN");
      this.hashLanguage.put("ar", "ARABIC");
      this.hashLanguage.put("hy", "ARMENIAN");
      this.hashLanguage.put("az", "AZERBAIJANI");
      this.hashLanguage.put("eu", "BASQUE");
      this.hashLanguage.put("be", "BELARUSIAN");
      this.hashLanguage.put("bn", "BENGALI");
      this.hashLanguage.put("bg", "BULGARIAN");
      this.hashLanguage.put("ca", "CATALAN");
      this.hashLanguage.put("zh-CN", "CHINESE_SIMPLIFIED");
      this.hashLanguage.put("zh-TW", "CHINESE_TRADITIONAL");
      this.hashLanguage.put("hr", "CROATIAN");
      this.hashLanguage.put("cs", "CZECH");
      this.hashLanguage.put("da", "DANISH");
      this.hashLanguage.put("nl", "DUTCH");
      this.hashLanguage.put("et", "ESTONIAN");
      this.hashLanguage.put("tl", "FILIPINO");
      this.hashLanguage.put("fi", "FINNISH");
      this.hashLanguage.put("fr", "FRENCH");
      this.hashLanguage.put("gl", "GALICIAN");
      this.hashLanguage.put("ka", "GEORGIAN");
      this.hashLanguage.put("de", "GERMAN");
      this.hashLanguage.put("el", "GREEK");
      this.hashLanguage.put("gu", "GUJARATI");
      this.hashLanguage.put("ht", "HAITIAN_CREOLE");
      this.hashLanguage.put("iw", "HEBREW");
      this.hashLanguage.put("hi", "HINDI");
      this.hashLanguage.put("hu", "HUNGARIAN");
      this.hashLanguage.put("is", "ICELANDIC");
      this.hashLanguage.put("id", "INDONESIAN");
      this.hashLanguage.put("ga", "IRISH");
      this.hashLanguage.put("it", "ITALIAN");
      this.hashLanguage.put("ja", "JAPANESE");
      this.hashLanguage.put("kn", "KANNADA");
      this.hashLanguage.put("ko", "KOREAN");
      this.hashLanguage.put("la", "LATIN");
      this.hashLanguage.put("lv", "LATVIAN");
      this.hashLanguage.put("lt", "LITHUANIAN");
      this.hashLanguage.put("mk", "MACEDONIAN");
      this.hashLanguage.put("ms", "MALAY");
      this.hashLanguage.put("mt", "MALTESE");
      this.hashLanguage.put("no", "NORWEGIAN");
      this.hashLanguage.put("fa", "PERSIAN");
      this.hashLanguage.put("pl", "POLISH");
      this.hashLanguage.put("pt", "PORTUGUESE");
      this.hashLanguage.put("ro", "ROMANIAN");
      this.hashLanguage.put("ru", "RUSSIAN");
      this.hashLanguage.put("sr", "SERBIAN");
      this.hashLanguage.put("sk", "SLOVAK");
      this.hashLanguage.put("sl", "SLOVENIAN");
      this.hashLanguage.put("es", "SPANISH");
      this.hashLanguage.put("sw", "SWAHILI");
      this.hashLanguage.put("sv", "SWEDISH");
      this.hashLanguage.put("ta", "TAMIL");
      this.hashLanguage.put("te", "TELUGU");
      this.hashLanguage.put("th", "THAI");
      this.hashLanguage.put("tr", "TURKISH");
      this.hashLanguage.put("uk", "UKRAINIAN");
      this.hashLanguage.put("ur", "URDU");
      this.hashLanguage.put("vi", "VIETNAMESE");
      this.hashLanguage.put("cy", "WELSH");
      this.hashLanguage.put("yi", "YIDDISH");
   }

   public String getNameLanguage(String prefixLanguage) {
      return (String)this.hashLanguage.get(prefixLanguage);
   }

   public String getNameLanguage(String prefixLanguage, String outputLanguage) {
      Translator translate = Translator.getInstance();
      return translate.translate((String)this.hashLanguage.get(prefixLanguage), prefixLanguage, outputLanguage);
   }
}
