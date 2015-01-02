package com.gtranslate.parsing;

import com.gtranslate.parsing.Parse;
import com.gtranslate.text.Text;
import com.gtranslate.text.TextTranslate;
import com.gtranslate.utils.WebUtils;

public class ParseTextTranslate implements Parse {

   private TextTranslate textTranslate;
   private StringBuilder url;


   public ParseTextTranslate(TextTranslate textTranslate) {
      this.textTranslate = textTranslate;
   }

   public void parse() {
      this.appendURL();
      String result = WebUtils.source(this.url.toString());
      String[] split = result.replace("[", "").replace("]", "").replace("\"", "").split(",");
      Text output = this.textTranslate.getOutput();
      output.setText(split[0]);
   }

   public TextTranslate getTextTranslate() {
      return this.textTranslate;
   }

   public void appendURL() {
      Text input = this.textTranslate.getInput();
      Text output = this.textTranslate.getOutput();
      this.url = new StringBuilder("http://translate.google.com.br/translate_a/t?");
      this.url.append("client=t&text=" + input.getText().replace(" ", "%20"));
      this.url.append("&hl=" + input.getLanguage());
      this.url.append("&sl=" + input.getLanguage());
      this.url.append("&tl=" + output.getLanguage());
      this.url.append("&multires=1&prev=btn&ssel=0&tsel=0&sc=1");
   }
}
