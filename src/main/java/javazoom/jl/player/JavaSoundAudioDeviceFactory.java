package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.AudioDeviceFactory;
import javazoom.jl.player.JavaSoundAudioDevice;

public class JavaSoundAudioDeviceFactory extends AudioDeviceFactory {

   private boolean tested = false;
   private static final String DEVICE_CLASS_NAME = "javazoom.jl.player.JavaSoundAudioDevice";


   public synchronized AudioDevice createAudioDevice() throws JavaLayerException {
      if(!this.tested) {
         this.testAudioDevice();
         this.tested = true;
      }

      try {
         return this.createAudioDeviceImpl();
      } catch (Exception var2) {
         throw new JavaLayerException("unable to create JavaSound device: " + var2);
      } catch (LinkageError var3) {
         throw new JavaLayerException("unable to create JavaSound device: " + var3);
      }
   }

   protected JavaSoundAudioDevice createAudioDeviceImpl() throws JavaLayerException {
      ClassLoader var1 = this.getClass().getClassLoader();

      try {
         JavaSoundAudioDevice var2 = (JavaSoundAudioDevice)this.instantiate(var1, "javazoom.jl.player.JavaSoundAudioDevice");
         return var2;
      } catch (Exception var3) {
         throw new JavaLayerException("Cannot create JavaSound device", var3);
      } catch (LinkageError var4) {
         throw new JavaLayerException("Cannot create JavaSound device", var4);
      }
   }

   public void testAudioDevice() throws JavaLayerException {
      JavaSoundAudioDevice var1 = this.createAudioDeviceImpl();
      var1.test();
   }
}
