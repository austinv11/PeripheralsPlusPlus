package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;

public abstract class AudioDeviceFactory {

   public abstract AudioDevice createAudioDevice() throws JavaLayerException;

   protected AudioDevice instantiate(ClassLoader var1, String var2) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      AudioDevice var3 = null;
      Class var4 = null;
      if(var1 == null) {
         var4 = Class.forName(var2);
      } else {
         var4 = var1.loadClass(var2);
      }

      Object var5 = var4.newInstance();
      var3 = (AudioDevice)var5;
      return var3;
   }
}
