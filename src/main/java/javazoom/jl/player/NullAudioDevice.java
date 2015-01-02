package javazoom.jl.player;

import javazoom.jl.player.AudioDeviceBase;

public class NullAudioDevice extends AudioDeviceBase {

   public int getPosition() {
      return 0;
   }
}
