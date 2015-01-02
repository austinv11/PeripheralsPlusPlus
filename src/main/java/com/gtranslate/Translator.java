package com.gtranslate;

import com.gtranslate.parsing.ParseTextDetect;
import com.gtranslate.parsing.ParseTextTranslate;
import com.gtranslate.text.Text;
import com.gtranslate.text.TextTranslate;

public class Translator {

   private static Translator translator;


   public static synchronized Translator getInstance() {
      if(translator == null) {
         translator = new Translator();
      }

      return translator;
   }

   public void translate(TextTranslate textTranslate) {
      ParseTextTranslate parse = new ParseTextTranslate(textTranslate);
      parse.parse();
   }

   public String translate(String text, String languageInput, String languageOutput) {
      Text input = new Text(text, languageInput);
      TextTranslate textTranslate = new TextTranslate(input, languageOutput);
      ParseTextTranslate parse = new ParseTextTranslate(textTranslate);
      parse.parse();
      return textTranslate.getOutput().getText();
   }

   public String detect(String text) {
      Text input = new Text(text);
      ParseTextDetect parse = new ParseTextDetect(input);
      parse.parse();
      return input.getLanguage();
   }
}
