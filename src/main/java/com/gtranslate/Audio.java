package com.gtranslate;

import rehost.javazoom.jl.decoder.JavaLayerException;
import rehost.javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class Audio {

   private static Audio audio;


   public static synchronized Audio getInstance() {
      if(audio == null) {
         audio = new Audio();
      }

      return audio;
   }

   public InputStream getAudio(String text, String languageOutput) throws IOException {
      //See http://stackoverflow.com/questions/32053442/google-translate-tts-api-blocked
      URL url = new URL("http://translate.google.com/translate_tts?q=" + text.replace(" ", "%20") + "&tl=" + languageOutput + "&client=tw-ob");
      URLConnection urlConn = url.openConnection();
      urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
      InputStream audioSrc = urlConn.getInputStream();
      return new BufferedInputStream(audioSrc);
   }

   public void play(InputStream sound) throws JavaLayerException {
      (new Player(sound)).play();
   }
}
