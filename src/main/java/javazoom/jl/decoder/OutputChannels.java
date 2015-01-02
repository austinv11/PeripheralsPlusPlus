package javazoom.jl.decoder;


public class OutputChannels {

   public static final int BOTH_CHANNELS = 0;
   public static final int LEFT_CHANNEL = 1;
   public static final int RIGHT_CHANNEL = 2;
   public static final int DOWNMIX_CHANNELS = 3;
   public static final OutputChannels LEFT = new OutputChannels(1);
   public static final OutputChannels RIGHT = new OutputChannels(2);
   public static final OutputChannels BOTH = new OutputChannels(0);
   public static final OutputChannels DOWNMIX = new OutputChannels(3);
   private int outputChannels;


   public static OutputChannels fromInt(int var0) {
      switch(var0) {
      case 0:
         return BOTH;
      case 1:
         return LEFT;
      case 2:
         return RIGHT;
      case 3:
         return DOWNMIX;
      default:
         throw new IllegalArgumentException("Invalid channel code: " + var0);
      }
   }

   private OutputChannels(int var1) {
      this.outputChannels = var1;
      if(var1 < 0 || var1 > 3) {
         throw new IllegalArgumentException("channels");
      }
   }

   public int getChannelsOutputCode() {
      return this.outputChannels;
   }

   public int getChannelCount() {
      int var1 = this.outputChannels == 0?2:1;
      return var1;
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if(var1 instanceof OutputChannels) {
         OutputChannels var3 = (OutputChannels)var1;
         var2 = var3.outputChannels == this.outputChannels;
      }

      return var2;
   }

   public int hashCode() {
      return this.outputChannels;
   }

}
