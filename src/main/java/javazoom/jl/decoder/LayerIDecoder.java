package javazoom.jl.decoder;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Crc16;
import javazoom.jl.decoder.FrameDecoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.decoder.SynthesisFilter;

class LayerIDecoder implements FrameDecoder {

   protected Bitstream stream;
   protected Header header;
   protected SynthesisFilter filter1;
   protected SynthesisFilter filter2;
   protected Obuffer buffer;
   protected int which_channels;
   protected int mode;
   protected int num_subbands;
   protected LayerIDecoder.Subband[] subbands;
   protected Crc16 crc = null;


   public LayerIDecoder() {
      this.crc = new Crc16();
   }

   public void create(Bitstream var1, Header var2, SynthesisFilter var3, SynthesisFilter var4, Obuffer var5, int var6) {
      this.stream = var1;
      this.header = var2;
      this.filter1 = var3;
      this.filter2 = var4;
      this.buffer = var5;
      this.which_channels = var6;
   }

   public void decodeFrame() {
      this.num_subbands = this.header.number_of_subbands();
      this.subbands = new LayerIDecoder.Subband[32];
      this.mode = this.header.mode();
      this.createSubbands();
      this.readAllocation();
      this.readScaleFactorSelection();
      if(this.crc != null || this.header.checksum_ok()) {
         this.readScaleFactors();
         this.readSampleData();
      }

   }

   protected void createSubbands() {
      int var1;
      if(this.mode == 3) {
         for(var1 = 0; var1 < this.num_subbands; ++var1) {
            this.subbands[var1] = new LayerIDecoder.SubbandLayer1(var1);
         }
      } else if(this.mode == 1) {
         for(var1 = 0; var1 < this.header.intensity_stereo_bound(); ++var1) {
            this.subbands[var1] = new LayerIDecoder.SubbandLayer1Stereo(var1);
         }

         while(var1 < this.num_subbands) {
            this.subbands[var1] = new LayerIDecoder.SubbandLayer1IntensityStereo(var1);
            ++var1;
         }
      } else {
         for(var1 = 0; var1 < this.num_subbands; ++var1) {
            this.subbands[var1] = new LayerIDecoder.SubbandLayer1Stereo(var1);
         }
      }

   }

   protected void readAllocation() {
      for(int var1 = 0; var1 < this.num_subbands; ++var1) {
         this.subbands[var1].read_allocation(this.stream, this.header, this.crc);
      }

   }

   protected void readScaleFactorSelection() {}

   protected void readScaleFactors() {
      for(int var1 = 0; var1 < this.num_subbands; ++var1) {
         this.subbands[var1].read_scalefactor(this.stream, this.header);
      }

   }

   protected void readSampleData() {
      boolean var1 = false;
      boolean var2 = false;
      int var3 = this.header.mode();

      do {
         int var4;
         for(var4 = 0; var4 < this.num_subbands; ++var4) {
            var1 = this.subbands[var4].read_sampledata(this.stream);
         }

         do {
            for(var4 = 0; var4 < this.num_subbands; ++var4) {
               var2 = this.subbands[var4].put_next_sample(this.which_channels, this.filter1, this.filter2);
            }

            this.filter1.calculate_pcm_samples(this.buffer);
            if(this.which_channels == 0 && var3 != 3) {
               this.filter2.calculate_pcm_samples(this.buffer);
            }
         } while(!var2);
      } while(!var1);

   }

   abstract static class Subband {

      public static final float[] scalefactors = new float[]{2.0F, 1.587401F, 1.2599211F, 1.0F, 0.7937005F, 0.62996054F, 0.5F, 0.39685026F, 0.31498027F, 0.25F, 0.19842513F, 0.15749013F, 0.125F, 0.099212565F, 0.07874507F, 0.0625F, 0.049606282F, 0.039372534F, 0.03125F, 0.024803141F, 0.019686267F, 0.015625F, 0.012401571F, 0.009843133F, 0.0078125F, 0.0062007853F, 0.0049215667F, 0.00390625F, 0.0031003926F, 0.0024607833F, 0.001953125F, 0.0015501963F, 0.0012303917F, 9.765625E-4F, 7.7509816E-4F, 6.1519584E-4F, 4.8828125E-4F, 3.8754908E-4F, 3.0759792E-4F, 2.4414062E-4F, 1.9377454E-4F, 1.5379896E-4F, 1.2207031E-4F, 9.688727E-5F, 7.689948E-5F, 6.1035156E-5F, 4.8443635E-5F, 3.844974E-5F, 3.0517578E-5F, 2.4221818E-5F, 1.922487E-5F, 1.5258789E-5F, 1.2110909E-5F, 9.612435E-6F, 7.6293945E-6F, 6.0554544E-6F, 4.8062175E-6F, 3.8146973E-6F, 3.0277272E-6F, 2.4031087E-6F, 1.9073486E-6F, 1.5138636E-6F, 1.2015544E-6F, 0.0F};


      public abstract void read_allocation(Bitstream var1, Header var2, Crc16 var3);

      public abstract void read_scalefactor(Bitstream var1, Header var2);

      public abstract boolean read_sampledata(Bitstream var1);

      public abstract boolean put_next_sample(int var1, SynthesisFilter var2, SynthesisFilter var3);

   }

   static class SubbandLayer1Stereo extends LayerIDecoder.SubbandLayer1 {

      protected int channel2_allocation;
      protected float channel2_scalefactor;
      protected int channel2_samplelength;
      protected float channel2_sample;
      protected float channel2_factor;
      protected float channel2_offset;


      public SubbandLayer1Stereo(int var1) {
         super(var1);
      }

      public void read_allocation(Bitstream var1, Header var2, Crc16 var3) {
         this.allocation = var1.get_bits(4);
         this.channel2_allocation = var1.get_bits(4);
         if(var3 != null) {
            var3.add_bits(this.allocation, 4);
            var3.add_bits(this.channel2_allocation, 4);
         }

         if(this.allocation != 0) {
            this.samplelength = this.allocation + 1;
            this.factor = table_factor[this.allocation];
            this.offset = table_offset[this.allocation];
         }

         if(this.channel2_allocation != 0) {
            this.channel2_samplelength = this.channel2_allocation + 1;
            this.channel2_factor = table_factor[this.channel2_allocation];
            this.channel2_offset = table_offset[this.channel2_allocation];
         }

      }

      public void read_scalefactor(Bitstream var1, Header var2) {
         if(this.allocation != 0) {
            this.scalefactor = scalefactors[var1.get_bits(6)];
         }

         if(this.channel2_allocation != 0) {
            this.channel2_scalefactor = scalefactors[var1.get_bits(6)];
         }

      }

      public boolean read_sampledata(Bitstream var1) {
         boolean var2 = super.read_sampledata(var1);
         if(this.channel2_allocation != 0) {
            this.channel2_sample = (float)var1.get_bits(this.channel2_samplelength);
         }

         return var2;
      }

      public boolean put_next_sample(int var1, SynthesisFilter var2, SynthesisFilter var3) {
         super.put_next_sample(var1, var2, var3);
         if(this.channel2_allocation != 0 && var1 != 1) {
            float var4 = (this.channel2_sample * this.channel2_factor + this.channel2_offset) * this.channel2_scalefactor;
            if(var1 == 0) {
               var3.input_sample(var4, this.subbandnumber);
            } else {
               var2.input_sample(var4, this.subbandnumber);
            }
         }

         return true;
      }
   }

   static class SubbandLayer1IntensityStereo extends LayerIDecoder.SubbandLayer1 {

      protected float channel2_scalefactor;


      public SubbandLayer1IntensityStereo(int var1) {
         super(var1);
      }

      public void read_allocation(Bitstream var1, Header var2, Crc16 var3) {
         super.read_allocation(var1, var2, var3);
      }

      public void read_scalefactor(Bitstream var1, Header var2) {
         if(this.allocation != 0) {
            this.scalefactor = scalefactors[var1.get_bits(6)];
            this.channel2_scalefactor = scalefactors[var1.get_bits(6)];
         }

      }

      public boolean read_sampledata(Bitstream var1) {
         return super.read_sampledata(var1);
      }

      public boolean put_next_sample(int var1, SynthesisFilter var2, SynthesisFilter var3) {
         if(this.allocation != 0) {
            this.sample = this.sample * this.factor + this.offset;
            float var4;
            if(var1 == 0) {
               var4 = this.sample * this.scalefactor;
               float var5 = this.sample * this.channel2_scalefactor;
               var2.input_sample(var4, this.subbandnumber);
               var3.input_sample(var5, this.subbandnumber);
            } else if(var1 == 1) {
               var4 = this.sample * this.scalefactor;
               var2.input_sample(var4, this.subbandnumber);
            } else {
               var4 = this.sample * this.channel2_scalefactor;
               var2.input_sample(var4, this.subbandnumber);
            }
         }

         return true;
      }
   }

   static class SubbandLayer1 extends LayerIDecoder.Subband {

      public static final float[] table_factor = new float[]{0.0F, 0.6666667F, 0.2857143F, 0.13333334F, 0.06451613F, 0.031746034F, 0.015748031F, 0.007843138F, 0.0039138943F, 0.0019550342F, 9.770396E-4F, 4.884005E-4F, 2.4417043E-4F, 1.2207776E-4F, 6.103702E-5F};
      public static final float[] table_offset = new float[]{0.0F, -0.6666667F, -0.8571429F, -0.9333334F, -0.9677419F, -0.98412704F, -0.992126F, -0.9960785F, -0.99804306F, -0.9990225F, -0.9995115F, -0.99975586F, -0.9998779F, -0.99993896F, -0.9999695F};
      protected int subbandnumber;
      protected int samplenumber;
      protected int allocation;
      protected float scalefactor;
      protected int samplelength;
      protected float sample;
      protected float factor;
      protected float offset;


      public SubbandLayer1(int var1) {
         this.subbandnumber = var1;
         this.samplenumber = 0;
      }

      public void read_allocation(Bitstream var1, Header var2, Crc16 var3) {
         if((this.allocation = var1.get_bits(4)) == 15) {
            ;
         }

         if(var3 != null) {
            var3.add_bits(this.allocation, 4);
         }

         if(this.allocation != 0) {
            this.samplelength = this.allocation + 1;
            this.factor = table_factor[this.allocation];
            this.offset = table_offset[this.allocation];
         }

      }

      public void read_scalefactor(Bitstream var1, Header var2) {
         if(this.allocation != 0) {
            this.scalefactor = scalefactors[var1.get_bits(6)];
         }

      }

      public boolean read_sampledata(Bitstream var1) {
         if(this.allocation != 0) {
            this.sample = (float)var1.get_bits(this.samplelength);
         }

         if(++this.samplenumber == 12) {
            this.samplenumber = 0;
            return true;
         } else {
            return false;
         }
      }

      public boolean put_next_sample(int var1, SynthesisFilter var2, SynthesisFilter var3) {
         if(this.allocation != 0 && var1 != 2) {
            float var4 = (this.sample * this.factor + this.offset) * this.scalefactor;
            var2.input_sample(var4, this.subbandnumber);
         }

         return true;
      }

   }
}
