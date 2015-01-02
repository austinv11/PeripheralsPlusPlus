package javazoom.jl.player;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;

public class PlayerApplet extends Applet implements Runnable {

   public static final String AUDIO_PARAMETER = "audioURL";
   private Player player = null;
   private Thread playerThread = null;
   private String fileName = null;


   protected AudioDevice getAudioDevice() throws JavaLayerException {
      return FactoryRegistry.systemRegistry().createAudioDevice();
   }

   protected InputStream getAudioStream() {
      InputStream var1 = null;

      try {
         URL var2 = this.getAudioURL();
         if(var2 != null) {
            var1 = var2.openStream();
         }
      } catch (IOException var3) {
         System.err.println(var3);
      }

      return var1;
   }

   protected String getAudioFileName() {
      String var1 = this.fileName;
      if(var1 == null) {
         var1 = this.getParameter("audioURL");
      }

      return var1;
   }

   protected URL getAudioURL() {
      String var1 = this.getAudioFileName();
      URL var2 = null;
      if(var1 != null) {
         try {
            var2 = new URL(this.getDocumentBase(), var1);
         } catch (Exception var4) {
            System.err.println(var4);
         }
      }

      return var2;
   }

   public void setFileName(String var1) {
      this.fileName = var1;
   }

   public String getFileName() {
      return this.fileName;
   }

   protected void stopPlayer() throws JavaLayerException {
      if(this.player != null) {
         this.player.close();
         this.player = null;
         this.playerThread = null;
      }

   }

   protected void play(InputStream var1, AudioDevice var2) throws JavaLayerException {
      this.stopPlayer();
      if(var1 != null && var2 != null) {
         this.player = new Player(var1, var2);
         this.playerThread = this.createPlayerThread();
         this.playerThread.start();
      }

   }

   protected Thread createPlayerThread() {
      return new Thread(this, "Audio player thread");
   }

   public void init() {}

   public void start() {
      String var1 = this.getAudioFileName();

      try {
         InputStream var7 = this.getAudioStream();
         AudioDevice var8 = this.getAudioDevice();
         this.play(var7, var8);
      } catch (JavaLayerException var6) {
         JavaLayerException var2 = var6;
         PrintStream var3 = System.err;
         synchronized(System.err) {
            System.err.println("Unable to play " + var1);
            var2.printStackTrace(System.err);
         }
      }

   }

   public void stop() {
      try {
         this.stopPlayer();
      } catch (JavaLayerException var2) {
         System.err.println(var2);
      }

   }

   public void destroy() {}

   public void run() {
      if(this.player != null) {
         try {
            this.player.play();
         } catch (JavaLayerException var2) {
            System.err.println("Problem playing audio: " + var2);
         }
      }

   }
}
