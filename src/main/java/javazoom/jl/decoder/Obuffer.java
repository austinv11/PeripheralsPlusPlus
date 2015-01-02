package javazoom.jl.decoder;


public abstract class Obuffer {

   public static final int OBUFFERSIZE = 2304;
   public static final int MAXCHANNELS = 2;


   public abstract void append(int var1, short var2);

   public void appendSamples(int var1, float[] var2) {
      int var4 = 0;

      while(var4 < 32) {
         short var3 = this.clip(var2[var4++]);
         this.append(var1, var3);
      }

   }

   private final short clip(float var1) {
      return var1 > 32767.0F?32767:(var1 < -32768.0F?-32768:(short)((int)var1));
   }

   public abstract void write_buffer(int var1);

   public abstract void close();

   public abstract void clear_buffer();

   public abstract void set_stop_flag();
}
