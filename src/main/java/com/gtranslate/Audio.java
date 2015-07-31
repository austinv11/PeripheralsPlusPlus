package com.gtranslate;

import rehost.javazoom.jl.decoder.JavaLayerException;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
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
      URL url = new URL("http://mary.dfki.de:59125/process?INPUT_TEXT=" + text.replace(" ", "+").replace("%20", "+") + "&INPUT_TYPE=TEXT&OUTPUT_TYPE=AUDIO&LOCALE="  + languageOutput + "&AUDIO=WAVE_FILE");
      URLConnection urlConn = url.openConnection();
      urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
      InputStream audioSrc = urlConn.getInputStream();
      return new BufferedInputStream(audioSrc);
   }

   public void play(InputStream sound) throws JavaLayerException {
//      (new Player(sound)).play();
      playSound(sound);
   }
   
   private final int BUFFER_SIZE = 128000;
   private File soundFile;
   private AudioInputStream audioStream;
   private AudioFormat audioFormat;
   private SourceDataLine sourceLine;
   
   /**
	* Modified from http://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
    * @param filename the name of the file that is going to be played
    */
   public void playSound(InputStream sound){
      
//      String strFilename = filename;
//      
//      try {
//         soundFile = new File(strFilename);
//      } catch (Exception e) {
//         e.printStackTrace();
//         System.exit(1);
//      }
      
      try {
         audioStream = AudioSystem.getAudioInputStream(sound);
      } catch (Exception e){
         e.printStackTrace();
//         System.exit(1);
      }
      
      audioFormat = audioStream.getFormat();
      
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      try {
         sourceLine = (SourceDataLine) AudioSystem.getLine(info);
         sourceLine.open(audioFormat);
      } catch (LineUnavailableException e) {
         e.printStackTrace();
//         System.exit(1);
      } catch (Exception e) {
         e.printStackTrace();
//         System.exit(1);
      }
      
      sourceLine.start();
      
      int nBytesRead = 0;
      byte[] abData = new byte[BUFFER_SIZE];
      while (nBytesRead != -1) {
         try {
            nBytesRead = audioStream.read(abData, 0, abData.length);
         } catch (IOException e) {
            e.printStackTrace();
         }
         if (nBytesRead >= 0) {
            @SuppressWarnings("unused")
            int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
         }
      }
      
      sourceLine.drain();
      sourceLine.close();
   }
}
