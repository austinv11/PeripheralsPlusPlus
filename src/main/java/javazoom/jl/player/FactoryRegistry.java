package javazoom.jl.player;

import java.util.Enumeration;
import java.util.Hashtable;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.AudioDeviceFactory;
import javazoom.jl.player.JavaSoundAudioDeviceFactory;

public class FactoryRegistry extends AudioDeviceFactory {

   private static FactoryRegistry instance = null;
   protected Hashtable factories = new Hashtable();


   public static synchronized FactoryRegistry systemRegistry() {
      if(instance == null) {
         instance = new FactoryRegistry();
         instance.registerDefaultFactories();
      }

      return instance;
   }

   public void addFactory(AudioDeviceFactory var1) {
      this.factories.put(var1.getClass(), var1);
   }

   public void removeFactoryType(Class var1) {
      this.factories.remove(var1);
   }

   public void removeFactory(AudioDeviceFactory var1) {
      this.factories.remove(var1.getClass());
   }

   public AudioDevice createAudioDevice() throws JavaLayerException {
      AudioDevice var1 = null;
      AudioDeviceFactory[] var2 = this.getFactoriesPriority();
      if(var2 == null) {
         throw new JavaLayerException(this + ": no factories registered");
      } else {
         JavaLayerException var3 = null;

         for(int var4 = 0; var1 == null && var4 < var2.length; ++var4) {
            try {
               var1 = var2[var4].createAudioDevice();
            } catch (JavaLayerException var6) {
               var3 = var6;
            }
         }

         if(var1 == null && var3 != null) {
            throw new JavaLayerException("Cannot create AudioDevice", var3);
         } else {
            return var1;
         }
      }
   }

   protected AudioDeviceFactory[] getFactoriesPriority() {
      AudioDeviceFactory[] var1 = null;
      Hashtable var2 = this.factories;
      synchronized(this.factories) {
         int var3 = this.factories.size();
         if(var3 != 0) {
            var1 = new AudioDeviceFactory[var3];
            int var4 = 0;

            AudioDeviceFactory var6;
            for(Enumeration var5 = this.factories.elements(); var5.hasMoreElements(); var1[var4++] = var6) {
               var6 = (AudioDeviceFactory)var5.nextElement();
            }
         }

         return var1;
      }
   }

   protected void registerDefaultFactories() {
      this.addFactory(new JavaSoundAudioDeviceFactory());
   }

}
