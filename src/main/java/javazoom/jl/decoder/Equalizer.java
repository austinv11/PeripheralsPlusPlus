package javazoom.jl.decoder;


public final class Equalizer {

   public static final float BAND_NOT_PRESENT = Float.NEGATIVE_INFINITY;
   public static final Equalizer PASS_THRU_EQ = new Equalizer();
   private static final int BANDS = 32;
   private final float[] settings = new float[32];


   public Equalizer() {}

   public Equalizer(float[] var1) {
      this.setFrom(var1);
   }

   public Equalizer(Equalizer.EQFunction var1) {
      this.setFrom(var1);
   }

   public void setFrom(float[] var1) {
      this.reset();
      int var2 = var1.length > 32?32:var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.settings[var3] = this.limit(var1[var3]);
      }

   }

   public void setFrom(Equalizer.EQFunction var1) {
      this.reset();
      byte var2 = 32;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.settings[var3] = this.limit(var1.getBand(var3));
      }

   }

   public void setFrom(Equalizer var1) {
      if(var1 != this) {
         this.setFrom(var1.settings);
      }

   }

   public void reset() {
      for(int var1 = 0; var1 < 32; ++var1) {
         this.settings[var1] = 0.0F;
      }

   }

   public int getBandCount() {
      return this.settings.length;
   }

   public float setBand(int var1, float var2) {
      float var3 = 0.0F;
      if(var1 >= 0 && var1 < 32) {
         var3 = this.settings[var1];
         this.settings[var1] = this.limit(var2);
      }

      return var3;
   }

   public float getBand(int var1) {
      float var2 = 0.0F;
      if(var1 >= 0 && var1 < 32) {
         var2 = this.settings[var1];
      }

      return var2;
   }

   private float limit(float var1) {
      return var1 == Float.NEGATIVE_INFINITY?var1:(var1 > 1.0F?1.0F:(var1 < -1.0F?-1.0F:var1));
   }

   float[] getBandFactors() {
      float[] var1 = new float[32];
      int var2 = 0;

      for(byte var3 = 32; var2 < var3; ++var2) {
         var1[var2] = this.getBandFactor(this.settings[var2]);
      }

      return var1;
   }

   float getBandFactor(float var1) {
      if(var1 == Float.NEGATIVE_INFINITY) {
         return 0.0F;
      } else {
         float var2 = (float)Math.pow(2.0D, (double)var1);
         return var2;
      }
   }


   public abstract static class EQFunction {

      public float getBand(int var1) {
         return 0.0F;
      }
   }
}
