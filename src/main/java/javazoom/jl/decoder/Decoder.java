package javazoom.jl.decoder;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.DecoderErrors;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Equalizer;
import javazoom.jl.decoder.FrameDecoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.LayerIDecoder;
import javazoom.jl.decoder.LayerIIDecoder;
import javazoom.jl.decoder.LayerIIIDecoder;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.decoder.OutputChannels;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.decoder.SynthesisFilter;

public class Decoder implements DecoderErrors {

   private static final Decoder.Params DEFAULT_PARAMS = new Decoder.Params();
   private Obuffer output;
   private SynthesisFilter filter1;
   private SynthesisFilter filter2;
   private LayerIIIDecoder l3decoder;
   private LayerIIDecoder l2decoder;
   private LayerIDecoder l1decoder;
   private int outputFrequency;
   private int outputChannels;
   private Equalizer equalizer;
   private Decoder.Params params;
   private boolean initialized;


   public Decoder() {
      this((Decoder.Params)null);
   }

   public Decoder(Decoder.Params var1) {
      this.equalizer = new Equalizer();
      if(var1 == null) {
         var1 = DEFAULT_PARAMS;
      }

      this.params = var1;
      Equalizer var2 = this.params.getInitialEqualizerSettings();
      if(var2 != null) {
         this.equalizer.setFrom(var2);
      }

   }

   public static Decoder.Params getDefaultParams() {
      return (Decoder.Params)DEFAULT_PARAMS.clone();
   }

   public void setEqualizer(Equalizer var1) {
      if(var1 == null) {
         var1 = Equalizer.PASS_THRU_EQ;
      }

      this.equalizer.setFrom(var1);
      float[] var2 = this.equalizer.getBandFactors();
      if(this.filter1 != null) {
         this.filter1.setEQ(var2);
      }

      if(this.filter2 != null) {
         this.filter2.setEQ(var2);
      }

   }

   public Obuffer decodeFrame(Header var1, Bitstream var2) throws DecoderException {
      if(!this.initialized) {
         this.initialize(var1);
      }

      int var3 = var1.layer();
      this.output.clear_buffer();
      FrameDecoder var4 = this.retrieveDecoder(var1, var2, var3);
      var4.decodeFrame();
      this.output.write_buffer(1);
      return this.output;
   }

   public void setOutputBuffer(Obuffer var1) {
      this.output = var1;
   }

   public int getOutputFrequency() {
      return this.outputFrequency;
   }

   public int getOutputChannels() {
      return this.outputChannels;
   }

   public int getOutputBlockSize() {
      return 2304;
   }

   protected DecoderException newDecoderException(int var1) {
      return new DecoderException(var1, (Throwable)null);
   }

   protected DecoderException newDecoderException(int var1, Throwable var2) {
      return new DecoderException(var1, var2);
   }

   protected FrameDecoder retrieveDecoder(Header var1, Bitstream var2, int var3) throws DecoderException {
      Object var4 = null;
      switch(var3) {
      case 1:
         if(this.l1decoder == null) {
            this.l1decoder = new LayerIDecoder();
            this.l1decoder.create(var2, var1, this.filter1, this.filter2, this.output, 0);
         }

         var4 = this.l1decoder;
         break;
      case 2:
         if(this.l2decoder == null) {
            this.l2decoder = new LayerIIDecoder();
            this.l2decoder.create(var2, var1, this.filter1, this.filter2, this.output, 0);
         }

         var4 = this.l2decoder;
         break;
      case 3:
         if(this.l3decoder == null) {
            this.l3decoder = new LayerIIIDecoder(var2, var1, this.filter1, this.filter2, this.output, 0);
         }

         var4 = this.l3decoder;
      }

      if(var4 == null) {
         throw this.newDecoderException(513, (Throwable)null);
      } else {
         return (FrameDecoder)var4;
      }
   }

   private void initialize(Header var1) throws DecoderException {
      float var2 = 32700.0F;
      int var3 = var1.mode();
      int var4 = var1.layer();
      int var5 = var3 == 3?1:2;
      if(this.output == null) {
         this.output = new SampleBuffer(var1.frequency(), var5);
      }

      float[] var6 = this.equalizer.getBandFactors();
      this.filter1 = new SynthesisFilter(0, var2, var6);
      if(var5 == 2) {
         this.filter2 = new SynthesisFilter(1, var2, var6);
      }

      this.outputChannels = var5;
      this.outputFrequency = var1.frequency();
      this.initialized = true;
   }


   public static class Params implements Cloneable {

      private OutputChannels outputChannels;
      private Equalizer equalizer;


      public Params() {
         this.outputChannels = OutputChannels.BOTH;
         this.equalizer = new Equalizer();
      }

      public Object clone() {
         try {
            return super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new InternalError(this + ": " + var2);
         }
      }

      public void setOutputChannels(OutputChannels var1) {
         if(var1 == null) {
            throw new NullPointerException("out");
         } else {
            this.outputChannels = var1;
         }
      }

      public OutputChannels getOutputChannels() {
         return this.outputChannels;
      }

      public Equalizer getInitialEqualizerSettings() {
         return this.equalizer;
      }
   }
}
