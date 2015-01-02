package javazoom.jl.converter;

import javazoom.jl.converter.WaveFile;
import javazoom.jl.decoder.Obuffer;

public class WaveFileObuffer extends Obuffer {

   private short[] buffer;
   private short[] bufferp;
   private int channels;
   private WaveFile outWave;
   short[] myBuffer = new short[2];


   public WaveFileObuffer(int var1, int var2, String var3) {
      if(var3 == null) {
         throw new NullPointerException("FileName");
      } else {
         this.buffer = new short[2304];
         this.bufferp = new short[2];
         this.channels = var1;

         for(int var4 = 0; var4 < var1; ++var4) {
            this.bufferp[var4] = (short)var4;
         }

         this.outWave = new WaveFile();
         this.outWave.OpenForWrite(var3, var2, (short)16, (short)this.channels);
      }
   }

   public void append(int var1, short var2) {
      this.buffer[this.bufferp[var1]] = var2;
      this.bufferp[var1] = (short)(this.bufferp[var1] + this.channels);
   }

   public void write_buffer(int var1) {
      boolean var2 = false;
      boolean var3 = false;
      int var5 = this.outWave.WriteData(this.buffer, this.bufferp[0]);

      for(int var4 = 0; var4 < this.channels; ++var4) {
         this.bufferp[var4] = (short)var4;
      }

   }

   public void close() {
      this.outWave.Close();
   }

   public void clear_buffer() {}

   public void set_stop_flag() {}
}
