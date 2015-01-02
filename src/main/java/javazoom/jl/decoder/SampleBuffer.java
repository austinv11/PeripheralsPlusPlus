package javazoom.jl.decoder;

import javazoom.jl.decoder.Obuffer;

public class SampleBuffer extends Obuffer {

   private short[] buffer = new short[2304];
   private int[] bufferp = new int[2];
   private int channels;
   private int frequency;


   public SampleBuffer(int var1, int var2) {
      this.channels = var2;
      this.frequency = var1;

      for(int var3 = 0; var3 < var2; ++var3) {
         this.bufferp[var3] = (short)var3;
      }

   }

   public int getChannelCount() {
      return this.channels;
   }

   public int getSampleFrequency() {
      return this.frequency;
   }

   public short[] getBuffer() {
      return this.buffer;
   }

   public int getBufferLength() {
      return this.bufferp[0];
   }

   public void append(int var1, short var2) {
      this.buffer[this.bufferp[var1]] = var2;
      this.bufferp[var1] += this.channels;
   }

   public void appendSamples(int var1, float[] var2) {
      int var3 = this.bufferp[var1];

      for(int var6 = 0; var6 < 32; var3 += this.channels) {
         float var5 = var2[var6++];
         var5 = var5 > 32767.0F?32767.0F:(var5 < -32767.0F?-32767.0F:var5);
         short var4 = (short)((int)var5);
         this.buffer[var3] = var4;
      }

      this.bufferp[var1] = var3;
   }

   public void write_buffer(int var1) {}

   public void close() {}

   public void clear_buffer() {
      for(int var1 = 0; var1 < this.channels; ++var1) {
         this.bufferp[var1] = (short)var1;
      }

   }

   public void set_stop_flag() {}
}
