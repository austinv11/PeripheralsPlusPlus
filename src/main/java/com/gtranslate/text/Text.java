package com.gtranslate.text;


public class Text {

   private String text;
   private String language;


   public Text(String text, String language) {
      this.text = text;
      this.language = language;
   }

   public Text(String language) {
      this.language = language;
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getLanguage() {
      return this.language;
   }

   public void setLanguage(String language) {
      this.language = language;
   }
}
