package com.gtranslate;

import java.util.HashMap;

public class Language {

   public static final String ENGLISH = "en_US";
   public static final String ENGLISH_US = "en_US";
   public static final String ENGLISH_GB = "en_GB";
   public static final String FRENCH = "fr";
   public static final String GERMAN = "de";
   public static final String ITALIAN = "it";
   public static final String RUSSIAN = "ru";
   public static final String SWEDISH = "sv";
   public static final String TELUGU = "te";
   public static final String TURKISH = "tr";
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
      this.hashLanguage.put("en_US", "ENGLISH");
	   this.hashLanguage.put("en_GB", "ENGLISH_GB");
	   this.hashLanguage.put("fr", "FRENCH");
	   this.hashLanguage.put("de", "GERMAN");
	   this.hashLanguage.put("it", "ITALIAN");
	   this.hashLanguage.put("ru", "RUSSIAN");
	   this.hashLanguage.put("sv", "SWEDISH");
	   this.hashLanguage.put("te", "TELUGU");
	   this.hashLanguage.put("tr", "TURKISH");
   }

   public String getNameLanguage(String prefixLanguage) {
      return (String)this.hashLanguage.get(prefixLanguage);
   }

   public String getNameLanguage(String prefixLanguage, String outputLanguage) {
      Translator translate = Translator.getInstance();
      return translate.translate((String)this.hashLanguage.get(prefixLanguage), prefixLanguage, outputLanguage);
   }
}
