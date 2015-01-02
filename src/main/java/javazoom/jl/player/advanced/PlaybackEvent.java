package javazoom.jl.player.advanced;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class PlaybackEvent {

   public static int STOPPED = 1;
   public static int STARTED = 2;
   private AdvancedPlayer source;
   private int frame;
   private int id;


   public PlaybackEvent(AdvancedPlayer var1, int var2, int var3) {
      this.id = var2;
      this.source = var1;
      this.frame = var3;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int var1) {
      this.id = var1;
   }

   public int getFrame() {
      return this.frame;
   }

   public void setFrame(int var1) {
      this.frame = var1;
   }

   public AdvancedPlayer getSource() {
      return this.source;
   }

   public void setSource(AdvancedPlayer var1) {
      this.source = var1;
   }

}
