package javazoom.jl.decoder;

import javazoom.jl.decoder.BitReserve;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.FrameDecoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;
import javazoom.jl.decoder.SynthesisFilter;
import javazoom.jl.decoder.huffcodetab;

final class LayerIIIDecoder implements FrameDecoder {

   final double d43 = 1.3333333333333333D;
   public int[] scalefac_buffer;
   private int CheckSumHuff = 0;
   private int[] is_1d;
   private float[][][] ro;
   private float[][][] lr;
   private float[] out_1d;
   private float[][] prevblck;
   private float[][] k;
   private int[] nonzero;
   private Bitstream stream;
   private Header header;
   private SynthesisFilter filter1;
   private SynthesisFilter filter2;
   private Obuffer buffer;
   private int which_channels;
   private BitReserve br;
   private LayerIIIDecoder.III_side_info_t si;
   private LayerIIIDecoder.temporaire2[] III_scalefac_t;
   private LayerIIIDecoder.temporaire2[] scalefac;
   private int max_gr;
   private int frame_start;
   private int part2_start;
   private int channels;
   private int first_channel;
   private int last_channel;
   private int sfreq;
   private float[] samples1 = new float[32];
   private float[] samples2 = new float[32];
   private final int[] new_slen = new int[4];
   int[] x = new int[]{0};
   int[] y = new int[]{0};
   int[] v = new int[]{0};
   int[] w = new int[]{0};
   int[] is_pos = new int[576];
   float[] is_ratio = new float[576];
   float[] tsOutCopy = new float[18];
   float[] rawout = new float[36];
   private int counter = 0;
   private static final int SSLIMIT = 18;
   private static final int SBLIMIT = 32;
   private static final int[][] slen = new int[][]{{0, 0, 0, 0, 3, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4}, {0, 1, 2, 3, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 2, 3}};
   public static final int[] pretab = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 3, 3, 3, 2, 0};
   private LayerIIIDecoder.SBI[] sfBandIndex;
   public static final float[] two_to_negative_half_pow = new float[]{1.0F, 0.70710677F, 0.5F, 0.35355338F, 0.25F, 0.17677669F, 0.125F, 0.088388346F, 0.0625F, 0.044194173F, 0.03125F, 0.022097087F, 0.015625F, 0.011048543F, 0.0078125F, 0.0055242716F, 0.00390625F, 0.0027621358F, 0.001953125F, 0.0013810679F, 9.765625E-4F, 6.9053395E-4F, 4.8828125E-4F, 3.4526698E-4F, 2.4414062E-4F, 1.7263349E-4F, 1.2207031E-4F, 8.6316744E-5F, 6.1035156E-5F, 4.3158372E-5F, 3.0517578E-5F, 2.1579186E-5F, 1.5258789E-5F, 1.0789593E-5F, 7.6293945E-6F, 5.3947965E-6F, 3.8146973E-6F, 2.6973983E-6F, 1.9073486E-6F, 1.3486991E-6F, 9.536743E-7F, 6.7434956E-7F, 4.7683716E-7F, 3.3717478E-7F, 2.3841858E-7F, 1.6858739E-7F, 1.1920929E-7F, 8.4293696E-8F, 5.9604645E-8F, 4.2146848E-8F, 2.9802322E-8F, 2.1073424E-8F, 1.4901161E-8F, 1.0536712E-8F, 7.4505806E-9F, 5.268356E-9F, 3.7252903E-9F, 2.634178E-9F, 1.8626451E-9F, 1.317089E-9F, 9.313226E-10F, 6.585445E-10F, 4.656613E-10F, 3.2927225E-10F};
   public static final float[] t_43 = create_t_43();
   public static final float[][] io = new float[][]{{1.0F, 0.8408964F, 0.70710677F, 0.59460354F, 0.5F, 0.4204482F, 0.35355338F, 0.29730177F, 0.25F, 0.2102241F, 0.17677669F, 0.14865088F, 0.125F, 0.10511205F, 0.088388346F, 0.07432544F, 0.0625F, 0.052556027F, 0.044194173F, 0.03716272F, 0.03125F, 0.026278013F, 0.022097087F, 0.01858136F, 0.015625F, 0.013139007F, 0.011048543F, 0.00929068F, 0.0078125F, 0.0065695033F, 0.0055242716F, 0.00464534F}, {1.0F, 0.70710677F, 0.5F, 0.35355338F, 0.25F, 0.17677669F, 0.125F, 0.088388346F, 0.0625F, 0.044194173F, 0.03125F, 0.022097087F, 0.015625F, 0.011048543F, 0.0078125F, 0.0055242716F, 0.00390625F, 0.0027621358F, 0.001953125F, 0.0013810679F, 9.765625E-4F, 6.9053395E-4F, 4.8828125E-4F, 3.4526698E-4F, 2.4414062E-4F, 1.7263349E-4F, 1.2207031E-4F, 8.6316744E-5F, 6.1035156E-5F, 4.3158372E-5F, 3.0517578E-5F, 2.1579186E-5F}};
   public static final float[] TAN12 = new float[]{0.0F, 0.2679492F, 0.57735026F, 1.0F, 1.7320508F, 3.732051F, 9.9999998E10F, -3.732051F, -1.7320508F, -1.0F, -0.57735026F, -0.2679492F, 0.0F, 0.2679492F, 0.57735026F, 1.0F};
   private static int[][] reorder_table;
   private static final float[] cs = new float[]{0.8574929F, 0.881742F, 0.94962865F, 0.9833146F, 0.9955178F, 0.9991606F, 0.9998992F, 0.99999315F};
   private static final float[] ca = new float[]{-0.51449573F, -0.47173196F, -0.31337744F, -0.1819132F, -0.09457419F, -0.040965583F, -0.014198569F, -0.0036999746F};
   public static final float[][] win = new float[][]{{-0.016141215F, -0.05360318F, -0.100707136F, -0.16280818F, -0.5F, -0.38388735F, -0.6206114F, -1.1659756F, -3.8720753F, -4.225629F, -1.519529F, -0.97416484F, -0.73744076F, -1.2071068F, -0.5163616F, -0.45426053F, -0.40715656F, -0.3696946F, -0.3387627F, -0.31242222F, -0.28939587F, -0.26880082F, -0.5F, -0.23251417F, -0.21596715F, -0.20004979F, -0.18449493F, -0.16905846F, -0.15350361F, -0.13758625F, -0.12103922F, -0.20710678F, -0.084752575F, -0.06415752F, -0.041131172F, -0.014790705F}, {-0.016141215F, -0.05360318F, -0.100707136F, -0.16280818F, -0.5F, -0.38388735F, -0.6206114F, -1.1659756F, -3.8720753F, -4.225629F, -1.519529F, -0.97416484F, -0.73744076F, -1.2071068F, -0.5163616F, -0.45426053F, -0.40715656F, -0.3696946F, -0.33908543F, -0.3151181F, -0.29642227F, -0.28184548F, -0.5411961F, -0.2621323F, -0.25387916F, -0.2329629F, -0.19852729F, -0.15233535F, -0.0964964F, -0.03342383F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F}, {-0.0483008F, -0.15715657F, -0.28325045F, -0.42953748F, -1.2071068F, -0.8242648F, -1.1451749F, -1.769529F, -4.5470223F, -3.489053F, -0.7329629F, -0.15076515F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F}, {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.15076514F, -0.7329629F, -3.489053F, -4.5470223F, -1.769529F, -1.1451749F, -0.8313774F, -1.306563F, -0.54142016F, -0.46528974F, -0.4106699F, -0.3700468F, -0.3387627F, -0.31242222F, -0.28939587F, -0.26880082F, -0.5F, -0.23251417F, -0.21596715F, -0.20004979F, -0.18449493F, -0.16905846F, -0.15350361F, -0.13758625F, -0.12103922F, -0.20710678F, -0.084752575F, -0.06415752F, -0.041131172F, -0.014790705F}};
   public LayerIIIDecoder.Sftable sftable;
   public static final int[][][] nr_of_sfb_block = new int[][][]{{{6, 5, 5, 5}, {9, 9, 9, 9}, {6, 9, 9, 9}}, {{6, 5, 7, 3}, {9, 9, 12, 6}, {6, 9, 12, 6}}, {{11, 10, 0, 0}, {18, 18, 0, 0}, {15, 18, 0, 0}}, {{7, 7, 7, 0}, {12, 12, 12, 0}, {6, 15, 12, 0}}, {{6, 6, 6, 3}, {12, 9, 9, 6}, {6, 12, 9, 6}}, {{8, 8, 5, 0}, {15, 12, 9, 0}, {6, 18, 9, 0}}};


   public LayerIIIDecoder(Bitstream var1, Header var2, SynthesisFilter var3, SynthesisFilter var4, Obuffer var5, int var6) {
      huffcodetab.inithuff();
      this.is_1d = new int[580];
      this.ro = new float[2][32][18];
      this.lr = new float[2][32][18];
      this.out_1d = new float[576];
      this.prevblck = new float[2][576];
      this.k = new float[2][576];
      this.nonzero = new int[2];
      this.III_scalefac_t = new LayerIIIDecoder.temporaire2[2];
      this.III_scalefac_t[0] = new LayerIIIDecoder.temporaire2();
      this.III_scalefac_t[1] = new LayerIIIDecoder.temporaire2();
      this.scalefac = this.III_scalefac_t;
      this.sfBandIndex = new LayerIIIDecoder.SBI[9];
      int[] var7 = new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576};
      int[] var8 = new int[]{0, 4, 8, 12, 18, 24, 32, 42, 56, 74, 100, 132, 174, 192};
      int[] var9 = new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 114, 136, 162, 194, 232, 278, 330, 394, 464, 540, 576};
      int[] var10 = new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 136, 180, 192};
      int[] var11 = new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576};
      int[] var12 = new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192};
      int[] var13 = new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 52, 62, 74, 90, 110, 134, 162, 196, 238, 288, 342, 418, 576};
      int[] var14 = new int[]{0, 4, 8, 12, 16, 22, 30, 40, 52, 66, 84, 106, 136, 192};
      int[] var15 = new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 42, 50, 60, 72, 88, 106, 128, 156, 190, 230, 276, 330, 384, 576};
      int[] var16 = new int[]{0, 4, 8, 12, 16, 22, 28, 38, 50, 64, 80, 100, 126, 192};
      int[] var17 = new int[]{0, 4, 8, 12, 16, 20, 24, 30, 36, 44, 54, 66, 82, 102, 126, 156, 194, 240, 296, 364, 448, 550, 576};
      int[] var18 = new int[]{0, 4, 8, 12, 16, 22, 30, 42, 58, 78, 104, 138, 180, 192};
      int[] var19 = new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576};
      int[] var20 = new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192};
      int[] var21 = new int[]{0, 6, 12, 18, 24, 30, 36, 44, 54, 66, 80, 96, 116, 140, 168, 200, 238, 284, 336, 396, 464, 522, 576};
      int[] var22 = new int[]{0, 4, 8, 12, 18, 26, 36, 48, 62, 80, 104, 134, 174, 192};
      int[] var23 = new int[]{0, 12, 24, 36, 48, 60, 72, 88, 108, 132, 160, 192, 232, 280, 336, 400, 476, 566, 568, 570, 572, 574, 576};
      int[] var24 = new int[]{0, 8, 16, 24, 36, 52, 72, 96, 124, 160, 162, 164, 166, 192};
      this.sfBandIndex[0] = new LayerIIIDecoder.SBI(var7, var8);
      this.sfBandIndex[1] = new LayerIIIDecoder.SBI(var9, var10);
      this.sfBandIndex[2] = new LayerIIIDecoder.SBI(var11, var12);
      this.sfBandIndex[3] = new LayerIIIDecoder.SBI(var13, var14);
      this.sfBandIndex[4] = new LayerIIIDecoder.SBI(var15, var16);
      this.sfBandIndex[5] = new LayerIIIDecoder.SBI(var17, var18);
      this.sfBandIndex[6] = new LayerIIIDecoder.SBI(var19, var20);
      this.sfBandIndex[7] = new LayerIIIDecoder.SBI(var21, var22);
      this.sfBandIndex[8] = new LayerIIIDecoder.SBI(var23, var24);
      if(reorder_table == null) {
         reorder_table = new int[9][];

         for(int var25 = 0; var25 < 9; ++var25) {
            reorder_table[var25] = reorder(this.sfBandIndex[var25].s);
         }
      }

      int[] var29 = new int[]{0, 6, 11, 16, 21};
      int[] var26 = new int[]{0, 6, 12};
      this.sftable = new LayerIIIDecoder.Sftable(var29, var26);
      this.scalefac_buffer = new int[54];
      this.stream = var1;
      this.header = var2;
      this.filter1 = var3;
      this.filter2 = var4;
      this.buffer = var5;
      this.which_channels = var6;
      this.frame_start = 0;
      this.channels = this.header.mode() == 3?1:2;
      this.max_gr = this.header.version() == 1?2:1;
      this.sfreq = this.header.sample_frequency() + (this.header.version() == 1?3:(this.header.version() == 2?6:0));
      if(this.channels == 2) {
         switch(this.which_channels) {
         case 0:
         default:
            this.first_channel = 0;
            this.last_channel = 1;
            break;
         case 1:
         case 3:
            this.first_channel = this.last_channel = 0;
            break;
         case 2:
            this.first_channel = this.last_channel = 1;
         }
      } else {
         this.first_channel = this.last_channel = 0;
      }

      for(int var27 = 0; var27 < 2; ++var27) {
         for(int var28 = 0; var28 < 576; ++var28) {
            this.prevblck[var27][var28] = 0.0F;
         }
      }

      this.nonzero[0] = this.nonzero[1] = 576;
      this.br = new BitReserve();
      this.si = new LayerIIIDecoder.III_side_info_t();
   }

   public void seek_notify() {
      this.frame_start = 0;

      for(int var1 = 0; var1 < 2; ++var1) {
         for(int var2 = 0; var2 < 576; ++var2) {
            this.prevblck[var1][var2] = 0.0F;
         }
      }

      this.br = new BitReserve();
   }

   public void decodeFrame() {
      this.decode();
   }

   public void decode() {
      int var1 = this.header.slots();
      this.get_side_info();

      for(int var10 = 0; var10 < var1; ++var10) {
         this.br.hputbuf(this.stream.get_bits(8));
      }

      int var8 = this.br.hsstell() >>> 3;
      int var2;
      if((var2 = this.br.hsstell() & 7) != 0) {
         this.br.hgetbits(8 - var2);
         ++var8;
      }

      int var9 = this.frame_start - var8 - this.si.main_data_begin;
      this.frame_start += var1;
      if(var9 >= 0) {
         if(var8 > 4096) {
            this.frame_start -= 4096;
            this.br.rewindNbytes(4096);
         }

         while(var9 > 0) {
            this.br.hgetbits(8);
            --var9;
         }

         for(int var3 = 0; var3 < this.max_gr; ++var3) {
            int var4;
            for(var4 = 0; var4 < this.channels; ++var4) {
               this.part2_start = this.br.hsstell();
               if(this.header.version() == 1) {
                  this.get_scale_factors(var4, var3);
               } else {
                  this.get_LSF_scale_factors(var4, var3);
               }

               this.huffman_decode(var4, var3);
               this.dequantize_sample(this.ro[var4], var4, var3);
            }

            this.stereo(var3);
            if(this.which_channels == 3 && this.channels > 1) {
               this.do_downmix();
            }

            for(var4 = this.first_channel; var4 <= this.last_channel; ++var4) {
               this.reorder(this.lr[var4], var4, var3);
               this.antialias(var4, var3);
               this.hybrid(var4, var3);

               int var5;
               int var7;
               for(var7 = 18; var7 < 576; var7 += 36) {
                  for(var5 = 1; var5 < 18; var5 += 2) {
                     this.out_1d[var7 + var5] = -this.out_1d[var7 + var5];
                  }
               }

               int var6;
               if(var4 != 0 && this.which_channels != 2) {
                  for(var5 = 0; var5 < 18; ++var5) {
                     var6 = 0;

                     for(var7 = 0; var7 < 576; var7 += 18) {
                        this.samples2[var6] = this.out_1d[var7 + var5];
                        ++var6;
                     }

                     this.filter2.input_samples(this.samples2);
                     this.filter2.calculate_pcm_samples(this.buffer);
                  }
               } else {
                  for(var5 = 0; var5 < 18; ++var5) {
                     var6 = 0;

                     for(var7 = 0; var7 < 576; var7 += 18) {
                        this.samples1[var6] = this.out_1d[var7 + var5];
                        ++var6;
                     }

                     this.filter1.input_samples(this.samples1);
                     this.filter1.calculate_pcm_samples(this.buffer);
                  }
               }
            }
         }

         ++this.counter;
         this.buffer.write_buffer(1);
      }
   }

   private boolean get_side_info() {
      int var1;
      if(this.header.version() == 1) {
         this.si.main_data_begin = this.stream.get_bits(9);
         if(this.channels == 1) {
            this.si.private_bits = this.stream.get_bits(5);
         } else {
            this.si.private_bits = this.stream.get_bits(3);
         }

         for(var1 = 0; var1 < this.channels; ++var1) {
            this.si.ch[var1].scfsi[0] = this.stream.get_bits(1);
            this.si.ch[var1].scfsi[1] = this.stream.get_bits(1);
            this.si.ch[var1].scfsi[2] = this.stream.get_bits(1);
            this.si.ch[var1].scfsi[3] = this.stream.get_bits(1);
         }

         for(int var2 = 0; var2 < 2; ++var2) {
            for(var1 = 0; var1 < this.channels; ++var1) {
               this.si.ch[var1].gr[var2].part2_3_length = this.stream.get_bits(12);
               this.si.ch[var1].gr[var2].big_values = this.stream.get_bits(9);
               this.si.ch[var1].gr[var2].global_gain = this.stream.get_bits(8);
               this.si.ch[var1].gr[var2].scalefac_compress = this.stream.get_bits(4);
               this.si.ch[var1].gr[var2].window_switching_flag = this.stream.get_bits(1);
               if(this.si.ch[var1].gr[var2].window_switching_flag != 0) {
                  this.si.ch[var1].gr[var2].block_type = this.stream.get_bits(2);
                  this.si.ch[var1].gr[var2].mixed_block_flag = this.stream.get_bits(1);
                  this.si.ch[var1].gr[var2].table_select[0] = this.stream.get_bits(5);
                  this.si.ch[var1].gr[var2].table_select[1] = this.stream.get_bits(5);
                  this.si.ch[var1].gr[var2].subblock_gain[0] = this.stream.get_bits(3);
                  this.si.ch[var1].gr[var2].subblock_gain[1] = this.stream.get_bits(3);
                  this.si.ch[var1].gr[var2].subblock_gain[2] = this.stream.get_bits(3);
                  if(this.si.ch[var1].gr[var2].block_type == 0) {
                     return false;
                  }

                  if(this.si.ch[var1].gr[var2].block_type == 2 && this.si.ch[var1].gr[var2].mixed_block_flag == 0) {
                     this.si.ch[var1].gr[var2].region0_count = 8;
                  } else {
                     this.si.ch[var1].gr[var2].region0_count = 7;
                  }

                  this.si.ch[var1].gr[var2].region1_count = 20 - this.si.ch[var1].gr[var2].region0_count;
               } else {
                  this.si.ch[var1].gr[var2].table_select[0] = this.stream.get_bits(5);
                  this.si.ch[var1].gr[var2].table_select[1] = this.stream.get_bits(5);
                  this.si.ch[var1].gr[var2].table_select[2] = this.stream.get_bits(5);
                  this.si.ch[var1].gr[var2].region0_count = this.stream.get_bits(4);
                  this.si.ch[var1].gr[var2].region1_count = this.stream.get_bits(3);
                  this.si.ch[var1].gr[var2].block_type = 0;
               }

               this.si.ch[var1].gr[var2].preflag = this.stream.get_bits(1);
               this.si.ch[var1].gr[var2].scalefac_scale = this.stream.get_bits(1);
               this.si.ch[var1].gr[var2].count1table_select = this.stream.get_bits(1);
            }
         }
      } else {
         this.si.main_data_begin = this.stream.get_bits(8);
         if(this.channels == 1) {
            this.si.private_bits = this.stream.get_bits(1);
         } else {
            this.si.private_bits = this.stream.get_bits(2);
         }

         for(var1 = 0; var1 < this.channels; ++var1) {
            this.si.ch[var1].gr[0].part2_3_length = this.stream.get_bits(12);
            this.si.ch[var1].gr[0].big_values = this.stream.get_bits(9);
            this.si.ch[var1].gr[0].global_gain = this.stream.get_bits(8);
            this.si.ch[var1].gr[0].scalefac_compress = this.stream.get_bits(9);
            this.si.ch[var1].gr[0].window_switching_flag = this.stream.get_bits(1);
            if(this.si.ch[var1].gr[0].window_switching_flag != 0) {
               this.si.ch[var1].gr[0].block_type = this.stream.get_bits(2);
               this.si.ch[var1].gr[0].mixed_block_flag = this.stream.get_bits(1);
               this.si.ch[var1].gr[0].table_select[0] = this.stream.get_bits(5);
               this.si.ch[var1].gr[0].table_select[1] = this.stream.get_bits(5);
               this.si.ch[var1].gr[0].subblock_gain[0] = this.stream.get_bits(3);
               this.si.ch[var1].gr[0].subblock_gain[1] = this.stream.get_bits(3);
               this.si.ch[var1].gr[0].subblock_gain[2] = this.stream.get_bits(3);
               if(this.si.ch[var1].gr[0].block_type == 0) {
                  return false;
               }

               if(this.si.ch[var1].gr[0].block_type == 2 && this.si.ch[var1].gr[0].mixed_block_flag == 0) {
                  this.si.ch[var1].gr[0].region0_count = 8;
               } else {
                  this.si.ch[var1].gr[0].region0_count = 7;
                  this.si.ch[var1].gr[0].region1_count = 20 - this.si.ch[var1].gr[0].region0_count;
               }
            } else {
               this.si.ch[var1].gr[0].table_select[0] = this.stream.get_bits(5);
               this.si.ch[var1].gr[0].table_select[1] = this.stream.get_bits(5);
               this.si.ch[var1].gr[0].table_select[2] = this.stream.get_bits(5);
               this.si.ch[var1].gr[0].region0_count = this.stream.get_bits(4);
               this.si.ch[var1].gr[0].region1_count = this.stream.get_bits(3);
               this.si.ch[var1].gr[0].block_type = 0;
            }

            this.si.ch[var1].gr[0].scalefac_scale = this.stream.get_bits(1);
            this.si.ch[var1].gr[0].count1table_select = this.stream.get_bits(1);
         }
      }

      return true;
   }

   private void get_scale_factors(int var1, int var2) {
      LayerIIIDecoder.gr_info_s var5 = this.si.ch[var1].gr[var2];
      int var6 = var5.scalefac_compress;
      int var7 = slen[0][var6];
      int var8 = slen[1][var6];
      if(var5.window_switching_flag != 0 && var5.block_type == 2) {
         if(var5.mixed_block_flag != 0) {
            int var3;
            for(var3 = 0; var3 < 8; ++var3) {
               this.scalefac[var1].l[var3] = this.br.hgetbits(slen[0][var5.scalefac_compress]);
            }

            int var4;
            for(var3 = 3; var3 < 6; ++var3) {
               for(var4 = 0; var4 < 3; ++var4) {
                  this.scalefac[var1].s[var4][var3] = this.br.hgetbits(slen[0][var5.scalefac_compress]);
               }
            }

            for(var3 = 6; var3 < 12; ++var3) {
               for(var4 = 0; var4 < 3; ++var4) {
                  this.scalefac[var1].s[var4][var3] = this.br.hgetbits(slen[1][var5.scalefac_compress]);
               }
            }

            byte var9 = 12;

            for(var4 = 0; var4 < 3; ++var4) {
               this.scalefac[var1].s[var4][var9] = 0;
            }
         } else {
            this.scalefac[var1].s[0][0] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][0] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][0] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][1] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][1] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][1] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][2] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][2] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][2] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][3] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][3] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][3] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][4] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][4] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][4] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][5] = this.br.hgetbits(var7);
            this.scalefac[var1].s[1][5] = this.br.hgetbits(var7);
            this.scalefac[var1].s[2][5] = this.br.hgetbits(var7);
            this.scalefac[var1].s[0][6] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][6] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][6] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][7] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][7] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][7] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][8] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][8] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][8] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][9] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][9] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][9] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][10] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][10] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][10] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][11] = this.br.hgetbits(var8);
            this.scalefac[var1].s[1][11] = this.br.hgetbits(var8);
            this.scalefac[var1].s[2][11] = this.br.hgetbits(var8);
            this.scalefac[var1].s[0][12] = 0;
            this.scalefac[var1].s[1][12] = 0;
            this.scalefac[var1].s[2][12] = 0;
         }
      } else {
         if(this.si.ch[var1].scfsi[0] == 0 || var2 == 0) {
            this.scalefac[var1].l[0] = this.br.hgetbits(var7);
            this.scalefac[var1].l[1] = this.br.hgetbits(var7);
            this.scalefac[var1].l[2] = this.br.hgetbits(var7);
            this.scalefac[var1].l[3] = this.br.hgetbits(var7);
            this.scalefac[var1].l[4] = this.br.hgetbits(var7);
            this.scalefac[var1].l[5] = this.br.hgetbits(var7);
         }

         if(this.si.ch[var1].scfsi[1] == 0 || var2 == 0) {
            this.scalefac[var1].l[6] = this.br.hgetbits(var7);
            this.scalefac[var1].l[7] = this.br.hgetbits(var7);
            this.scalefac[var1].l[8] = this.br.hgetbits(var7);
            this.scalefac[var1].l[9] = this.br.hgetbits(var7);
            this.scalefac[var1].l[10] = this.br.hgetbits(var7);
         }

         if(this.si.ch[var1].scfsi[2] == 0 || var2 == 0) {
            this.scalefac[var1].l[11] = this.br.hgetbits(var8);
            this.scalefac[var1].l[12] = this.br.hgetbits(var8);
            this.scalefac[var1].l[13] = this.br.hgetbits(var8);
            this.scalefac[var1].l[14] = this.br.hgetbits(var8);
            this.scalefac[var1].l[15] = this.br.hgetbits(var8);
         }

         if(this.si.ch[var1].scfsi[3] == 0 || var2 == 0) {
            this.scalefac[var1].l[16] = this.br.hgetbits(var8);
            this.scalefac[var1].l[17] = this.br.hgetbits(var8);
            this.scalefac[var1].l[18] = this.br.hgetbits(var8);
            this.scalefac[var1].l[19] = this.br.hgetbits(var8);
            this.scalefac[var1].l[20] = this.br.hgetbits(var8);
         }

         this.scalefac[var1].l[21] = 0;
         this.scalefac[var1].l[22] = 0;
      }

   }

   private void get_LSF_scale_data(int var1, int var2) {
      int var5 = this.header.mode_extension();
      byte var8 = 0;
      LayerIIIDecoder.gr_info_s var9 = this.si.ch[var1].gr[var2];
      int var3 = var9.scalefac_compress;
      byte var7;
      if(var9.block_type == 2) {
         if(var9.mixed_block_flag == 0) {
            var7 = 1;
         } else if(var9.mixed_block_flag == 1) {
            var7 = 2;
         } else {
            var7 = 0;
         }
      } else {
         var7 = 0;
      }

      if(var5 != 1 && var5 != 3 || var1 != 1) {
         if(var3 < 400) {
            this.new_slen[0] = (var3 >>> 4) / 5;
            this.new_slen[1] = (var3 >>> 4) % 5;
            this.new_slen[2] = (var3 & 15) >>> 2;
            this.new_slen[3] = var3 & 3;
            this.si.ch[var1].gr[var2].preflag = 0;
            var8 = 0;
         } else if(var3 < 500) {
            this.new_slen[0] = (var3 - 400 >>> 2) / 5;
            this.new_slen[1] = (var3 - 400 >>> 2) % 5;
            this.new_slen[2] = var3 - 400 & 3;
            this.new_slen[3] = 0;
            this.si.ch[var1].gr[var2].preflag = 0;
            var8 = 1;
         } else if(var3 < 512) {
            this.new_slen[0] = (var3 - 500) / 3;
            this.new_slen[1] = (var3 - 500) % 3;
            this.new_slen[2] = 0;
            this.new_slen[3] = 0;
            this.si.ch[var1].gr[var2].preflag = 1;
            var8 = 2;
         }
      }

      if((var5 == 1 || var5 == 3) && var1 == 1) {
         int var4 = var3 >>> 1;
         if(var4 < 180) {
            this.new_slen[0] = var4 / 36;
            this.new_slen[1] = var4 % 36 / 6;
            this.new_slen[2] = var4 % 36 % 6;
            this.new_slen[3] = 0;
            this.si.ch[var1].gr[var2].preflag = 0;
            var8 = 3;
         } else if(var4 < 244) {
            this.new_slen[0] = (var4 - 180 & 63) >>> 4;
            this.new_slen[1] = (var4 - 180 & 15) >>> 2;
            this.new_slen[2] = var4 - 180 & 3;
            this.new_slen[3] = 0;
            this.si.ch[var1].gr[var2].preflag = 0;
            var8 = 4;
         } else if(var4 < 255) {
            this.new_slen[0] = (var4 - 244) / 3;
            this.new_slen[1] = (var4 - 244) % 3;
            this.new_slen[2] = 0;
            this.new_slen[3] = 0;
            this.si.ch[var1].gr[var2].preflag = 0;
            var8 = 5;
         }
      }

      int var10;
      for(var10 = 0; var10 < 45; ++var10) {
         this.scalefac_buffer[var10] = 0;
      }

      int var6 = 0;

      for(var10 = 0; var10 < 4; ++var10) {
         for(int var11 = 0; var11 < nr_of_sfb_block[var8][var7][var10]; ++var11) {
            this.scalefac_buffer[var6] = this.new_slen[var10] == 0?0:this.br.hgetbits(this.new_slen[var10]);
            ++var6;
         }
      }

   }

   private void get_LSF_scale_factors(int var1, int var2) {
      int var3 = 0;
      LayerIIIDecoder.gr_info_s var6 = this.si.ch[var1].gr[var2];
      this.get_LSF_scale_data(var1, var2);
      int var4;
      if(var6.window_switching_flag != 0 && var6.block_type == 2) {
         int var5;
         if(var6.mixed_block_flag != 0) {
            for(var4 = 0; var4 < 8; ++var4) {
               this.scalefac[var1].l[var4] = this.scalefac_buffer[var3];
               ++var3;
            }

            for(var4 = 3; var4 < 12; ++var4) {
               for(var5 = 0; var5 < 3; ++var5) {
                  this.scalefac[var1].s[var5][var4] = this.scalefac_buffer[var3];
                  ++var3;
               }
            }

            for(var5 = 0; var5 < 3; ++var5) {
               this.scalefac[var1].s[var5][12] = 0;
            }
         } else {
            for(var4 = 0; var4 < 12; ++var4) {
               for(var5 = 0; var5 < 3; ++var5) {
                  this.scalefac[var1].s[var5][var4] = this.scalefac_buffer[var3];
                  ++var3;
               }
            }

            for(var5 = 0; var5 < 3; ++var5) {
               this.scalefac[var1].s[var5][12] = 0;
            }
         }
      } else {
         for(var4 = 0; var4 < 21; ++var4) {
            this.scalefac[var1].l[var4] = this.scalefac_buffer[var3];
            ++var3;
         }

         this.scalefac[var1].l[21] = 0;
         this.scalefac[var1].l[22] = 0;
      }

   }

   private void huffman_decode(int var1, int var2) {
      this.x[0] = 0;
      this.y[0] = 0;
      this.v[0] = 0;
      this.w[0] = 0;
      int var3 = this.part2_start + this.si.ch[var1].gr[var2].part2_3_length;
      int var5;
      int var6;
      if(this.si.ch[var1].gr[var2].window_switching_flag != 0 && this.si.ch[var1].gr[var2].block_type == 2) {
         var5 = this.sfreq == 8?72:36;
         var6 = 576;
      } else {
         int var8 = this.si.ch[var1].gr[var2].region0_count + 1;
         int var9 = var8 + this.si.ch[var1].gr[var2].region1_count + 1;
         if(var9 > this.sfBandIndex[this.sfreq].l.length - 1) {
            var9 = this.sfBandIndex[this.sfreq].l.length - 1;
         }

         var5 = this.sfBandIndex[this.sfreq].l[var8];
         var6 = this.sfBandIndex[this.sfreq].l[var9];
      }

      int var7 = 0;

      huffcodetab var10;
      for(int var11 = 0; var11 < this.si.ch[var1].gr[var2].big_values << 1; var11 += 2) {
         if(var11 < var5) {
            var10 = huffcodetab.ht[this.si.ch[var1].gr[var2].table_select[0]];
         } else if(var11 < var6) {
            var10 = huffcodetab.ht[this.si.ch[var1].gr[var2].table_select[1]];
         } else {
            var10 = huffcodetab.ht[this.si.ch[var1].gr[var2].table_select[2]];
         }

         huffcodetab.huffman_decoder(var10, this.x, this.y, this.v, this.w, this.br);
         this.is_1d[var7++] = this.x[0];
         this.is_1d[var7++] = this.y[0];
         this.CheckSumHuff = this.CheckSumHuff + this.x[0] + this.y[0];
      }

      var10 = huffcodetab.ht[this.si.ch[var1].gr[var2].count1table_select + 32];

      int var4;
      for(var4 = this.br.hsstell(); var4 < var3 && var7 < 576; var4 = this.br.hsstell()) {
         huffcodetab.huffman_decoder(var10, this.x, this.y, this.v, this.w, this.br);
         this.is_1d[var7++] = this.v[0];
         this.is_1d[var7++] = this.w[0];
         this.is_1d[var7++] = this.x[0];
         this.is_1d[var7++] = this.y[0];
         this.CheckSumHuff = this.CheckSumHuff + this.v[0] + this.w[0] + this.x[0] + this.y[0];
      }

      if(var4 > var3) {
         this.br.rewindNbits(var4 - var3);
         var7 -= 4;
      }

      var4 = this.br.hsstell();
      if(var4 < var3) {
         this.br.hgetbits(var3 - var4);
      }

      if(var7 < 576) {
         this.nonzero[var1] = var7;
      } else {
         this.nonzero[var1] = 576;
      }

      if(var7 < 0) {
         var7 = 0;
      }

      while(var7 < 576) {
         this.is_1d[var7] = 0;
         ++var7;
      }

   }

   private void i_stereo_k_values(int var1, int var2, int var3) {
      if(var1 == 0) {
         this.k[0][var3] = 1.0F;
         this.k[1][var3] = 1.0F;
      } else if((var1 & 1) != 0) {
         this.k[0][var3] = io[var2][var1 + 1 >>> 1];
         this.k[1][var3] = 1.0F;
      } else {
         this.k[0][var3] = 1.0F;
         this.k[1][var3] = io[var2][var1 >>> 1];
      }

   }

   private void dequantize_sample(float[][] var1, int var2, int var3) {
      LayerIIIDecoder.gr_info_s var4 = this.si.ch[var2].gr[var3];
      int var5 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;
      float[][] var13 = var1;
      int var6;
      if(var4.window_switching_flag != 0 && var4.block_type == 2) {
         if(var4.mixed_block_flag != 0) {
            var6 = this.sfBandIndex[this.sfreq].l[1];
         } else {
            var8 = this.sfBandIndex[this.sfreq].s[1];
            var6 = (var8 << 2) - var8;
            var7 = 0;
         }
      } else {
         var6 = this.sfBandIndex[this.sfreq].l[1];
      }

      float var12 = (float)Math.pow(2.0D, 0.25D * ((double)var4.global_gain - 210.0D));

      int var11;
      int var14;
      int var15;
      int var16;
      for(var11 = 0; var11 < this.nonzero[var2]; ++var11) {
         var14 = var11 % 18;
         var15 = (var11 - var14) / 18;
         if(this.is_1d[var11] == 0) {
            var13[var15][var14] = 0.0F;
         } else {
            var16 = this.is_1d[var11];
            if(var16 < t_43.length) {
               if(this.is_1d[var11] > 0) {
                  var13[var15][var14] = var12 * t_43[var16];
               } else if(-var16 < t_43.length) {
                  var13[var15][var14] = -var12 * t_43[-var16];
               } else {
                  var13[var15][var14] = -var12 * (float)Math.pow((double)(-var16), 1.3333333333333333D);
               }
            } else if(this.is_1d[var11] > 0) {
               var13[var15][var14] = var12 * (float)Math.pow((double)var16, 1.3333333333333333D);
            } else {
               var13[var15][var14] = -var12 * (float)Math.pow((double)(-var16), 1.3333333333333333D);
            }
         }
      }

      for(var11 = 0; var11 < this.nonzero[var2]; ++var11) {
         var14 = var11 % 18;
         var15 = (var11 - var14) / 18;
         if(var9 == var6) {
            if(var4.window_switching_flag != 0 && var4.block_type == 2) {
               if(var4.mixed_block_flag != 0) {
                  if(var9 == this.sfBandIndex[this.sfreq].l[8]) {
                     var6 = this.sfBandIndex[this.sfreq].s[4];
                     var6 = (var6 << 2) - var6;
                     var5 = 3;
                     var8 = this.sfBandIndex[this.sfreq].s[4] - this.sfBandIndex[this.sfreq].s[3];
                     var7 = this.sfBandIndex[this.sfreq].s[3];
                     var7 = (var7 << 2) - var7;
                  } else if(var9 < this.sfBandIndex[this.sfreq].l[8]) {
                     ++var5;
                     var6 = this.sfBandIndex[this.sfreq].l[var5 + 1];
                  } else {
                     ++var5;
                     var6 = this.sfBandIndex[this.sfreq].s[var5 + 1];
                     var6 = (var6 << 2) - var6;
                     var7 = this.sfBandIndex[this.sfreq].s[var5];
                     var8 = this.sfBandIndex[this.sfreq].s[var5 + 1] - var7;
                     var7 = (var7 << 2) - var7;
                  }
               } else {
                  ++var5;
                  var6 = this.sfBandIndex[this.sfreq].s[var5 + 1];
                  var6 = (var6 << 2) - var6;
                  var7 = this.sfBandIndex[this.sfreq].s[var5];
                  var8 = this.sfBandIndex[this.sfreq].s[var5 + 1] - var7;
                  var7 = (var7 << 2) - var7;
               }
            } else {
               ++var5;
               var6 = this.sfBandIndex[this.sfreq].l[var5 + 1];
            }
         }

         if(var4.window_switching_flag != 0 && (var4.block_type == 2 && var4.mixed_block_flag == 0 || var4.block_type == 2 && var4.mixed_block_flag != 0 && var11 >= 36)) {
            int var10 = (var9 - var7) / var8;
            var16 = this.scalefac[var2].s[var10][var5] << var4.scalefac_scale;
            var16 += var4.subblock_gain[var10] << 2;
            var13[var15][var14] *= two_to_negative_half_pow[var16];
         } else {
            var16 = this.scalefac[var2].l[var5];
            if(var4.preflag != 0) {
               var16 += pretab[var5];
            }

            var16 <<= var4.scalefac_scale;
            var13[var15][var14] *= two_to_negative_half_pow[var16];
         }

         ++var9;
      }

      for(var11 = this.nonzero[var2]; var11 < 576; ++var11) {
         var14 = var11 % 18;
         var15 = (var11 - var14) / 18;
         if(var14 < 0) {
            var14 = 0;
         }

         if(var15 < 0) {
            var15 = 0;
         }

         var13[var15][var14] = 0.0F;
      }

   }

   private void reorder(float[][] var1, int var2, int var3) {
      LayerIIIDecoder.gr_info_s var4 = this.si.ch[var2].gr[var3];
      float[][] var13 = var1;
      int var7;
      int var14;
      int var15;
      if(var4.window_switching_flag != 0 && var4.block_type == 2) {
         for(var7 = 0; var7 < 576; ++var7) {
            this.out_1d[var7] = 0.0F;
         }

         int var16;
         if(var4.mixed_block_flag != 0) {
            for(var7 = 0; var7 < 36; ++var7) {
               var14 = var7 % 18;
               var15 = (var7 - var14) / 18;
               this.out_1d[var7] = var13[var15][var14];
            }

            for(int var8 = 3; var8 < 13; ++var8) {
               int var9 = this.sfBandIndex[this.sfreq].s[var8];
               int var10 = this.sfBandIndex[this.sfreq].s[var8 + 1] - var9;
               var14 = (var9 << 2) - var9;
               int var5 = 0;

               for(int var6 = 0; var5 < var10; var6 += 3) {
                  int var11 = var14 + var5;
                  int var12 = var14 + var6;
                  var15 = var11 % 18;
                  var16 = (var11 - var15) / 18;
                  this.out_1d[var12] = var13[var16][var15];
                  var11 += var10;
                  ++var12;
                  var15 = var11 % 18;
                  var16 = (var11 - var15) / 18;
                  this.out_1d[var12] = var13[var16][var15];
                  var11 += var10;
                  ++var12;
                  var15 = var11 % 18;
                  var16 = (var11 - var15) / 18;
                  this.out_1d[var12] = var13[var16][var15];
                  ++var5;
               }
            }
         } else {
            for(var7 = 0; var7 < 576; ++var7) {
               var14 = reorder_table[this.sfreq][var7];
               var15 = var14 % 18;
               var16 = (var14 - var15) / 18;
               this.out_1d[var7] = var13[var16][var15];
            }
         }
      } else {
         for(var7 = 0; var7 < 576; ++var7) {
            var14 = var7 % 18;
            var15 = (var7 - var14) / 18;
            this.out_1d[var7] = var13[var15][var14];
         }
      }

   }

   private void stereo(int var1) {
      int var2;
      int var3;
      if(this.channels == 1) {
         for(var2 = 0; var2 < 32; ++var2) {
            for(var3 = 0; var3 < 18; var3 += 3) {
               this.lr[0][var2][var3] = this.ro[0][var2][var3];
               this.lr[0][var2][var3 + 1] = this.ro[0][var2][var3 + 1];
               this.lr[0][var2][var3 + 2] = this.ro[0][var2][var3 + 2];
            }
         }
      } else {
         LayerIIIDecoder.gr_info_s var4 = this.si.ch[0].gr[var1];
         int var5 = this.header.mode_extension();
         boolean var11 = this.header.mode() == 1 && (var5 & 2) != 0;
         boolean var12 = this.header.mode() == 1 && (var5 & 1) != 0;
         boolean var13 = this.header.version() == 0 || this.header.version() == 2;
         int var14 = var4.scalefac_compress & 1;

         int var7;
         for(var7 = 0; var7 < 576; ++var7) {
            this.is_pos[var7] = 7;
            this.is_ratio[var7] = 0.0F;
         }

         if(var12) {
            int var6;
            if(var4.window_switching_flag != 0 && var4.block_type == 2) {
               int var8;
               int var9;
               int var15;
               int var16;
               if(var4.mixed_block_flag != 0) {
                  var15 = 0;

                  for(var16 = 0; var16 < 3; ++var16) {
                     int var17 = 2;

                     for(var6 = 12; var6 >= 3; --var6) {
                        var7 = this.sfBandIndex[this.sfreq].s[var6];
                        var8 = this.sfBandIndex[this.sfreq].s[var6 + 1] - var7;

                        for(var7 = (var7 << 2) - var7 + (var16 + 1) * var8 - 1; var8 > 0; --var7) {
                           if(this.ro[1][var7 / 18][var7 % 18] != 0.0F) {
                              var17 = var6;
                              var6 = -10;
                              var8 = -10;
                           }

                           --var8;
                        }
                     }

                     var6 = var17 + 1;
                     if(var6 > var15) {
                        var15 = var6;
                     }

                     while(var6 < 12) {
                        var9 = this.sfBandIndex[this.sfreq].s[var6];
                        var2 = this.sfBandIndex[this.sfreq].s[var6 + 1] - var9;

                        for(var7 = (var9 << 2) - var9 + var16 * var2; var2 > 0; --var2) {
                           this.is_pos[var7] = this.scalefac[1].s[var16][var6];
                           if(this.is_pos[var7] != 7) {
                              if(var13) {
                                 this.i_stereo_k_values(this.is_pos[var7], var14, var7);
                              } else {
                                 this.is_ratio[var7] = TAN12[this.is_pos[var7]];
                              }
                           }

                           ++var7;
                        }

                        ++var6;
                     }

                     var6 = this.sfBandIndex[this.sfreq].s[10];
                     var2 = this.sfBandIndex[this.sfreq].s[11] - var6;
                     var6 = (var6 << 2) - var6 + var16 * var2;
                     var9 = this.sfBandIndex[this.sfreq].s[11];
                     var2 = this.sfBandIndex[this.sfreq].s[12] - var9;

                     for(var7 = (var9 << 2) - var9 + var16 * var2; var2 > 0; --var2) {
                        this.is_pos[var7] = this.is_pos[var6];
                        if(var13) {
                           this.k[0][var7] = this.k[0][var6];
                           this.k[1][var7] = this.k[1][var6];
                        } else {
                           this.is_ratio[var7] = this.is_ratio[var6];
                        }

                        ++var7;
                     }
                  }

                  if(var15 <= 3) {
                     var7 = 2;
                     var3 = 17;
                     var2 = -1;

                     while(var7 >= 0) {
                        if(this.ro[1][var7][var3] != 0.0F) {
                           var2 = (var7 << 4) + (var7 << 1) + var3;
                           var7 = -1;
                        } else {
                           --var3;
                           if(var3 < 0) {
                              --var7;
                              var3 = 17;
                           }
                        }
                     }

                     for(var7 = 0; this.sfBandIndex[this.sfreq].l[var7] <= var2; ++var7) {
                        ;
                     }

                     var6 = var7;

                     for(var7 = this.sfBandIndex[this.sfreq].l[var7]; var6 < 8; ++var6) {
                        for(var2 = this.sfBandIndex[this.sfreq].l[var6 + 1] - this.sfBandIndex[this.sfreq].l[var6]; var2 > 0; --var2) {
                           this.is_pos[var7] = this.scalefac[1].l[var6];
                           if(this.is_pos[var7] != 7) {
                              if(var13) {
                                 this.i_stereo_k_values(this.is_pos[var7], var14, var7);
                              } else {
                                 this.is_ratio[var7] = TAN12[this.is_pos[var7]];
                              }
                           }

                           ++var7;
                        }
                     }
                  }
               } else {
                  for(var15 = 0; var15 < 3; ++var15) {
                     var16 = -1;

                     for(var6 = 12; var6 >= 0; --var6) {
                        var9 = this.sfBandIndex[this.sfreq].s[var6];
                        var8 = this.sfBandIndex[this.sfreq].s[var6 + 1] - var9;

                        for(var7 = (var9 << 2) - var9 + (var15 + 1) * var8 - 1; var8 > 0; --var7) {
                           if(this.ro[1][var7 / 18][var7 % 18] != 0.0F) {
                              var16 = var6;
                              var6 = -10;
                              var8 = -10;
                           }

                           --var8;
                        }
                     }

                     for(var6 = var16 + 1; var6 < 12; ++var6) {
                        var9 = this.sfBandIndex[this.sfreq].s[var6];
                        var2 = this.sfBandIndex[this.sfreq].s[var6 + 1] - var9;

                        for(var7 = (var9 << 2) - var9 + var15 * var2; var2 > 0; --var2) {
                           this.is_pos[var7] = this.scalefac[1].s[var15][var6];
                           if(this.is_pos[var7] != 7) {
                              if(var13) {
                                 this.i_stereo_k_values(this.is_pos[var7], var14, var7);
                              } else {
                                 this.is_ratio[var7] = TAN12[this.is_pos[var7]];
                              }
                           }

                           ++var7;
                        }
                     }

                     var9 = this.sfBandIndex[this.sfreq].s[10];
                     int var10 = this.sfBandIndex[this.sfreq].s[11];
                     var2 = var10 - var9;
                     var6 = (var9 << 2) - var9 + var15 * var2;
                     var2 = this.sfBandIndex[this.sfreq].s[12] - var10;

                     for(var7 = (var10 << 2) - var10 + var15 * var2; var2 > 0; --var2) {
                        this.is_pos[var7] = this.is_pos[var6];
                        if(var13) {
                           this.k[0][var7] = this.k[0][var6];
                           this.k[1][var7] = this.k[1][var6];
                        } else {
                           this.is_ratio[var7] = this.is_ratio[var6];
                        }

                        ++var7;
                     }
                  }
               }
            } else {
               var7 = 31;
               var3 = 17;
               var2 = 0;

               while(var7 >= 0) {
                  if(this.ro[1][var7][var3] != 0.0F) {
                     var2 = (var7 << 4) + (var7 << 1) + var3;
                     var7 = -1;
                  } else {
                     --var3;
                     if(var3 < 0) {
                        --var7;
                        var3 = 17;
                     }
                  }
               }

               for(var7 = 0; this.sfBandIndex[this.sfreq].l[var7] <= var2; ++var7) {
                  ;
               }

               var6 = var7;

               for(var7 = this.sfBandIndex[this.sfreq].l[var7]; var6 < 21; ++var6) {
                  for(var2 = this.sfBandIndex[this.sfreq].l[var6 + 1] - this.sfBandIndex[this.sfreq].l[var6]; var2 > 0; --var2) {
                     this.is_pos[var7] = this.scalefac[1].l[var6];
                     if(this.is_pos[var7] != 7) {
                        if(var13) {
                           this.i_stereo_k_values(this.is_pos[var7], var14, var7);
                        } else {
                           this.is_ratio[var7] = TAN12[this.is_pos[var7]];
                        }
                     }

                     ++var7;
                  }
               }

               var6 = this.sfBandIndex[this.sfreq].l[20];

               for(var2 = 576 - this.sfBandIndex[this.sfreq].l[21]; var2 > 0 && var7 < 576; --var2) {
                  this.is_pos[var7] = this.is_pos[var6];
                  if(var13) {
                     this.k[0][var7] = this.k[0][var6];
                     this.k[1][var7] = this.k[1][var6];
                  } else {
                     this.is_ratio[var7] = this.is_ratio[var6];
                  }

                  ++var7;
               }
            }
         }

         var7 = 0;

         for(var2 = 0; var2 < 32; ++var2) {
            for(var3 = 0; var3 < 18; ++var3) {
               if(this.is_pos[var7] == 7) {
                  if(var11) {
                     this.lr[0][var2][var3] = (this.ro[0][var2][var3] + this.ro[1][var2][var3]) * 0.70710677F;
                     this.lr[1][var2][var3] = (this.ro[0][var2][var3] - this.ro[1][var2][var3]) * 0.70710677F;
                  } else {
                     this.lr[0][var2][var3] = this.ro[0][var2][var3];
                     this.lr[1][var2][var3] = this.ro[1][var2][var3];
                  }
               } else if(var12) {
                  if(var13) {
                     this.lr[0][var2][var3] = this.ro[0][var2][var3] * this.k[0][var7];
                     this.lr[1][var2][var3] = this.ro[0][var2][var3] * this.k[1][var7];
                  } else {
                     this.lr[1][var2][var3] = this.ro[0][var2][var3] / (1.0F + this.is_ratio[var7]);
                     this.lr[0][var2][var3] = this.lr[1][var2][var3] * this.is_ratio[var7];
                  }
               }

               ++var7;
            }
         }
      }

   }

   private void antialias(int var1, int var2) {
      LayerIIIDecoder.gr_info_s var6 = this.si.ch[var1].gr[var2];
      if(var6.window_switching_flag == 0 || var6.block_type != 2 || var6.mixed_block_flag != 0) {
         short var5;
         if(var6.window_switching_flag != 0 && var6.mixed_block_flag != 0 && var6.block_type == 2) {
            var5 = 18;
         } else {
            var5 = 558;
         }

         for(int var3 = 0; var3 < var5; var3 += 18) {
            for(int var4 = 0; var4 < 8; ++var4) {
               int var7 = var3 + 17 - var4;
               int var8 = var3 + 18 + var4;
               float var9 = this.out_1d[var7];
               float var10 = this.out_1d[var8];
               this.out_1d[var7] = var9 * cs[var4] - var10 * ca[var4];
               this.out_1d[var8] = var10 * cs[var4] + var9 * ca[var4];
            }
         }

      }
   }

   private void hybrid(int var1, int var2) {
      LayerIIIDecoder.gr_info_s var5 = this.si.ch[var1].gr[var2];

      for(int var4 = 0; var4 < 576; var4 += 18) {
         int var3 = var5.window_switching_flag != 0 && var5.mixed_block_flag != 0 && var4 < 36?0:var5.block_type;
         float[] var6 = this.out_1d;

         int var8;
         for(var8 = 0; var8 < 18; ++var8) {
            this.tsOutCopy[var8] = var6[var8 + var4];
         }

         this.inv_mdct(this.tsOutCopy, this.rawout, var3);

         for(var8 = 0; var8 < 18; ++var8) {
            var6[var8 + var4] = this.tsOutCopy[var8];
         }

         float[][] var7 = this.prevblck;
         var6[0 + var4] = this.rawout[0] + var7[var1][var4 + 0];
         var7[var1][var4 + 0] = this.rawout[18];
         var6[1 + var4] = this.rawout[1] + var7[var1][var4 + 1];
         var7[var1][var4 + 1] = this.rawout[19];
         var6[2 + var4] = this.rawout[2] + var7[var1][var4 + 2];
         var7[var1][var4 + 2] = this.rawout[20];
         var6[3 + var4] = this.rawout[3] + var7[var1][var4 + 3];
         var7[var1][var4 + 3] = this.rawout[21];
         var6[4 + var4] = this.rawout[4] + var7[var1][var4 + 4];
         var7[var1][var4 + 4] = this.rawout[22];
         var6[5 + var4] = this.rawout[5] + var7[var1][var4 + 5];
         var7[var1][var4 + 5] = this.rawout[23];
         var6[6 + var4] = this.rawout[6] + var7[var1][var4 + 6];
         var7[var1][var4 + 6] = this.rawout[24];
         var6[7 + var4] = this.rawout[7] + var7[var1][var4 + 7];
         var7[var1][var4 + 7] = this.rawout[25];
         var6[8 + var4] = this.rawout[8] + var7[var1][var4 + 8];
         var7[var1][var4 + 8] = this.rawout[26];
         var6[9 + var4] = this.rawout[9] + var7[var1][var4 + 9];
         var7[var1][var4 + 9] = this.rawout[27];
         var6[10 + var4] = this.rawout[10] + var7[var1][var4 + 10];
         var7[var1][var4 + 10] = this.rawout[28];
         var6[11 + var4] = this.rawout[11] + var7[var1][var4 + 11];
         var7[var1][var4 + 11] = this.rawout[29];
         var6[12 + var4] = this.rawout[12] + var7[var1][var4 + 12];
         var7[var1][var4 + 12] = this.rawout[30];
         var6[13 + var4] = this.rawout[13] + var7[var1][var4 + 13];
         var7[var1][var4 + 13] = this.rawout[31];
         var6[14 + var4] = this.rawout[14] + var7[var1][var4 + 14];
         var7[var1][var4 + 14] = this.rawout[32];
         var6[15 + var4] = this.rawout[15] + var7[var1][var4 + 15];
         var7[var1][var4 + 15] = this.rawout[33];
         var6[16 + var4] = this.rawout[16] + var7[var1][var4 + 16];
         var7[var1][var4 + 16] = this.rawout[34];
         var6[17 + var4] = this.rawout[17] + var7[var1][var4 + 17];
         var7[var1][var4 + 17] = this.rawout[35];
      }

   }

   private void do_downmix() {
      for(int var1 = 0; var1 < 18; ++var1) {
         for(int var2 = 0; var2 < 18; var2 += 3) {
            this.lr[0][var1][var2] = (this.lr[0][var1][var2] + this.lr[1][var1][var2]) * 0.5F;
            this.lr[0][var1][var2 + 1] = (this.lr[0][var1][var2 + 1] + this.lr[1][var1][var2 + 1]) * 0.5F;
            this.lr[0][var1][var2 + 2] = (this.lr[0][var1][var2 + 2] + this.lr[1][var1][var2 + 2]) * 0.5F;
         }
      }

   }

   public void inv_mdct(float[] var1, float[] var2, int var3) {
      float var23 = 0.0F;
      float var22 = 0.0F;
      float var21 = 0.0F;
      float var20 = 0.0F;
      float var19 = 0.0F;
      float var18 = 0.0F;
      float var17 = 0.0F;
      float var16 = 0.0F;
      float var15 = 0.0F;
      float var14 = 0.0F;
      float var13 = 0.0F;
      float var12 = 0.0F;
      float var11 = 0.0F;
      float var10 = 0.0F;
      float var9 = 0.0F;
      float var8 = 0.0F;
      float var7 = 0.0F;
      float var6 = 0.0F;
      float var25;
      float var27;
      float var26;
      float var28;
      if(var3 == 2) {
         var2[0] = 0.0F;
         var2[1] = 0.0F;
         var2[2] = 0.0F;
         var2[3] = 0.0F;
         var2[4] = 0.0F;
         var2[5] = 0.0F;
         var2[6] = 0.0F;
         var2[7] = 0.0F;
         var2[8] = 0.0F;
         var2[9] = 0.0F;
         var2[10] = 0.0F;
         var2[11] = 0.0F;
         var2[12] = 0.0F;
         var2[13] = 0.0F;
         var2[14] = 0.0F;
         var2[15] = 0.0F;
         var2[16] = 0.0F;
         var2[17] = 0.0F;
         var2[18] = 0.0F;
         var2[19] = 0.0F;
         var2[20] = 0.0F;
         var2[21] = 0.0F;
         var2[22] = 0.0F;
         var2[23] = 0.0F;
         var2[24] = 0.0F;
         var2[25] = 0.0F;
         var2[26] = 0.0F;
         var2[27] = 0.0F;
         var2[28] = 0.0F;
         var2[29] = 0.0F;
         var2[30] = 0.0F;
         var2[31] = 0.0F;
         var2[32] = 0.0F;
         var2[33] = 0.0F;
         var2[34] = 0.0F;
         var2[35] = 0.0F;
         int var24 = 0;

         for(int var5 = 0; var5 < 3; ++var5) {
            var1[15 + var5] += var1[12 + var5];
            var1[12 + var5] += var1[9 + var5];
            var1[9 + var5] += var1[6 + var5];
            var1[6 + var5] += var1[3 + var5];
            var1[3 + var5] += var1[0 + var5];
            var1[15 + var5] += var1[9 + var5];
            var1[9 + var5] += var1[3 + var5];
            var26 = var1[12 + var5] * 0.5F;
            var25 = var1[6 + var5] * 0.8660254F;
            var27 = var1[0 + var5] + var26;
            var7 = var1[0 + var5] - var1[12 + var5];
            var6 = var27 + var25;
            var8 = var27 - var25;
            var26 = var1[15 + var5] * 0.5F;
            var25 = var1[9 + var5] * 0.8660254F;
            var27 = var1[3 + var5] + var26;
            var10 = var1[3 + var5] - var1[15 + var5];
            var11 = var27 + var25;
            var9 = var27 - var25;
            var9 *= 1.9318516F;
            var10 *= 0.70710677F;
            var11 *= 0.5176381F;
            var28 = var6;
            var6 += var11;
            var11 = var28 - var11;
            var28 = var7;
            var7 += var10;
            var10 = var28 - var10;
            var28 = var8;
            var8 += var9;
            var9 = var28 - var9;
            var6 *= 0.5043145F;
            var7 *= 0.5411961F;
            var8 *= 0.6302362F;
            var9 *= 0.8213398F;
            var10 *= 1.306563F;
            var11 *= 3.830649F;
            var14 = -var6 * 0.7933533F;
            var15 = -var6 * 0.6087614F;
            var13 = -var7 * 0.9238795F;
            var16 = -var7 * 0.38268343F;
            var12 = -var8 * 0.9914449F;
            var17 = -var8 * 0.13052619F;
            var6 = var9;
            var7 = var10 * 0.38268343F;
            var8 = var11 * 0.6087614F;
            var9 = -var11 * 0.7933533F;
            var10 = -var10 * 0.9238795F;
            var11 = -var6 * 0.9914449F;
            var6 *= 0.13052619F;
            var2[var24 + 6] += var6;
            var2[var24 + 7] += var7;
            var2[var24 + 8] += var8;
            var2[var24 + 9] += var9;
            var2[var24 + 10] += var10;
            var2[var24 + 11] += var11;
            var2[var24 + 12] += var12;
            var2[var24 + 13] += var13;
            var2[var24 + 14] += var14;
            var2[var24 + 15] += var15;
            var2[var24 + 16] += var16;
            var2[var24 + 17] += var17;
            var24 += 6;
         }
      } else {
         var1[17] += var1[16];
         var1[16] += var1[15];
         var1[15] += var1[14];
         var1[14] += var1[13];
         var1[13] += var1[12];
         var1[12] += var1[11];
         var1[11] += var1[10];
         var1[10] += var1[9];
         var1[9] += var1[8];
         var1[8] += var1[7];
         var1[7] += var1[6];
         var1[6] += var1[5];
         var1[5] += var1[4];
         var1[4] += var1[3];
         var1[3] += var1[2];
         var1[2] += var1[1];
         var1[1] += var1[0];
         var1[17] += var1[15];
         var1[15] += var1[13];
         var1[13] += var1[11];
         var1[11] += var1[9];
         var1[9] += var1[7];
         var1[7] += var1[5];
         var1[5] += var1[3];
         var1[3] += var1[1];
         float var42 = var1[0] + var1[0];
         float var43 = var42 + var1[12];
         float var50 = var43 + var1[4] * 1.8793852F + var1[8] * 1.5320889F + var1[16] * 0.34729636F;
         var25 = var42 + var1[4] - var1[8] - var1[12] - var1[12] - var1[16];
         var26 = var43 - var1[4] * 0.34729636F - var1[8] * 1.8793852F + var1[16] * 1.5320889F;
         var27 = var43 - var1[4] * 1.5320889F + var1[8] * 0.34729636F - var1[16] * 1.8793852F;
         var28 = var1[0] - var1[4] + var1[8] - var1[12] + var1[16];
         float var44 = var1[6] * 1.7320508F;
         float var29 = var1[2] * 1.9696155F + var44 + var1[10] * 1.2855753F + var1[14] * 0.6840403F;
         float var30 = (var1[2] - var1[10] - var1[14]) * 1.7320508F;
         float var31 = var1[2] * 1.2855753F - var44 - var1[10] * 0.6840403F + var1[14] * 1.9696155F;
         float var32 = var1[2] * 0.6840403F - var44 + var1[10] * 1.9696155F - var1[14] * 1.2855753F;
         float var45 = var1[1] + var1[1];
         float var46 = var45 + var1[13];
         float var33 = var46 + var1[5] * 1.8793852F + var1[9] * 1.5320889F + var1[17] * 0.34729636F;
         float var34 = var45 + var1[5] - var1[9] - var1[13] - var1[13] - var1[17];
         float var35 = var46 - var1[5] * 0.34729636F - var1[9] * 1.8793852F + var1[17] * 1.5320889F;
         float var36 = var46 - var1[5] * 1.5320889F + var1[9] * 0.34729636F - var1[17] * 1.8793852F;
         float var37 = (var1[1] - var1[5] + var1[9] - var1[13] + var1[17]) * 0.70710677F;
         float var47 = var1[7] * 1.7320508F;
         float var38 = var1[3] * 1.9696155F + var47 + var1[11] * 1.2855753F + var1[15] * 0.6840403F;
         float var39 = (var1[3] - var1[11] - var1[15]) * 1.7320508F;
         float var40 = var1[3] * 1.2855753F - var47 - var1[11] * 0.6840403F + var1[15] * 1.9696155F;
         float var41 = var1[3] * 0.6840403F - var47 + var1[11] * 1.9696155F - var1[15] * 1.2855753F;
         float var48 = var50 + var29;
         float var49 = (var33 + var38) * 0.5019099F;
         var6 = var48 + var49;
         var23 = var48 - var49;
         var48 = var25 + var30;
         var49 = (var34 + var39) * 0.5176381F;
         var7 = var48 + var49;
         var22 = var48 - var49;
         var48 = var26 + var31;
         var49 = (var35 + var40) * 0.55168897F;
         var8 = var48 + var49;
         var21 = var48 - var49;
         var48 = var27 + var32;
         var49 = (var36 + var41) * 0.61038727F;
         var9 = var48 + var49;
         var20 = var48 - var49;
         var10 = var28 + var37;
         var19 = var28 - var37;
         var48 = var27 - var32;
         var49 = (var36 - var41) * 0.8717234F;
         var11 = var48 + var49;
         var18 = var48 - var49;
         var48 = var26 - var31;
         var49 = (var35 - var40) * 1.1831008F;
         var12 = var48 + var49;
         var17 = var48 - var49;
         var48 = var25 - var30;
         var49 = (var34 - var39) * 1.9318516F;
         var13 = var48 + var49;
         var16 = var48 - var49;
         var48 = var50 - var29;
         var49 = (var33 - var38) * 5.7368565F;
         var14 = var48 + var49;
         var15 = var48 - var49;
         float[] var4 = win[var3];
         var2[0] = -var15 * var4[0];
         var2[1] = -var16 * var4[1];
         var2[2] = -var17 * var4[2];
         var2[3] = -var18 * var4[3];
         var2[4] = -var19 * var4[4];
         var2[5] = -var20 * var4[5];
         var2[6] = -var21 * var4[6];
         var2[7] = -var22 * var4[7];
         var2[8] = -var23 * var4[8];
         var2[9] = var23 * var4[9];
         var2[10] = var22 * var4[10];
         var2[11] = var21 * var4[11];
         var2[12] = var20 * var4[12];
         var2[13] = var19 * var4[13];
         var2[14] = var18 * var4[14];
         var2[15] = var17 * var4[15];
         var2[16] = var16 * var4[16];
         var2[17] = var15 * var4[17];
         var2[18] = var14 * var4[18];
         var2[19] = var13 * var4[19];
         var2[20] = var12 * var4[20];
         var2[21] = var11 * var4[21];
         var2[22] = var10 * var4[22];
         var2[23] = var9 * var4[23];
         var2[24] = var8 * var4[24];
         var2[25] = var7 * var4[25];
         var2[26] = var6 * var4[26];
         var2[27] = var6 * var4[27];
         var2[28] = var7 * var4[28];
         var2[29] = var8 * var4[29];
         var2[30] = var9 * var4[30];
         var2[31] = var10 * var4[31];
         var2[32] = var11 * var4[32];
         var2[33] = var12 * var4[33];
         var2[34] = var13 * var4[34];
         var2[35] = var14 * var4[35];
      }

   }

   private static float[] create_t_43() {
      float[] var0 = new float[8192];

      for(int var3 = 0; var3 < 8192; ++var3) {
         var0[var3] = (float)Math.pow((double)var3, 1.3333333333333333D);
      }

      return var0;
   }

   static int[] reorder(int[] var0) {
      int var1 = 0;
      int[] var2 = new int[576];

      for(int var3 = 0; var3 < 13; ++var3) {
         int var4 = var0[var3];
         int var5 = var0[var3 + 1];

         for(int var6 = 0; var6 < 3; ++var6) {
            for(int var7 = var4; var7 < var5; ++var7) {
               var2[3 * var7 + var6] = var1++;
            }
         }
      }

      return var2;
   }


   static class III_side_info_t {

      public int main_data_begin = 0;
      public int private_bits = 0;
      public LayerIIIDecoder.temporaire[] ch = new LayerIIIDecoder.temporaire[2];


      public III_side_info_t() {
         this.ch[0] = new LayerIIIDecoder.temporaire();
         this.ch[1] = new LayerIIIDecoder.temporaire();
      }
   }

   class Sftable {

      public int[] l;
      public int[] s;


      public Sftable() {
         this.l = new int[5];
         this.s = new int[3];
      }

      public Sftable(int[] var2, int[] var3) {
         this.l = var2;
         this.s = var3;
      }
   }

   static class temporaire {

      public int[] scfsi = new int[4];
      public LayerIIIDecoder.gr_info_s[] gr = new LayerIIIDecoder.gr_info_s[2];


      public temporaire() {
         this.gr[0] = new LayerIIIDecoder.gr_info_s();
         this.gr[1] = new LayerIIIDecoder.gr_info_s();
      }
   }

   static class temporaire2 {

      public int[] l = new int[23];
      public int[][] s = new int[3][13];


   }

   static class gr_info_s {

      public int part2_3_length = 0;
      public int big_values = 0;
      public int global_gain = 0;
      public int scalefac_compress = 0;
      public int window_switching_flag = 0;
      public int block_type = 0;
      public int mixed_block_flag = 0;
      public int[] table_select = new int[3];
      public int[] subblock_gain = new int[3];
      public int region0_count = 0;
      public int region1_count = 0;
      public int preflag = 0;
      public int scalefac_scale = 0;
      public int count1table_select = 0;


   }

   static class SBI {

      public int[] l;
      public int[] s;


      public SBI() {
         this.l = new int[23];
         this.s = new int[14];
      }

      public SBI(int[] var1, int[] var2) {
         this.l = var1;
         this.s = var2;
      }
   }
}
