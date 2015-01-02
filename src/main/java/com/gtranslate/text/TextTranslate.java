package com.gtranslate.text;

import com.gtranslate.text.Text;

public class TextTranslate {

   private Text input;
   private Text output;


   public TextTranslate(Text input, String loutput) {
      this.input = input;
      this.output = new Text(loutput);
   }

   public TextTranslate(Text input, Text output) {
      this.input = input;
      this.output = output;
   }

   public Text getInput() {
      return this.input;
   }

   public Text getOutput() {
      return this.output;
   }
}
