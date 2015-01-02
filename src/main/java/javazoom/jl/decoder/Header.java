package javazoom.jl.decoder;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Crc16;

public final class Header {

   public static final int[][] frequencies = new int[][]{{22050, 24000, 16000, 1}, {'\uac44', '\ubb80', 32000, 1}, {11025, 12000, 8000, 1}};
   public static final int MPEG2_LSF = 0;
   public static final int MPEG25_LSF = 2;
   public static final int MPEG1 = 1;
   public static final int STEREO = 0;
   public static final int JOINT_STEREO = 1;
   public static final int DUAL_CHANNEL = 2;
   public static final int SINGLE_CHANNEL = 3;
   public static final int FOURTYFOUR_POINT_ONE = 0;
   public static final int FOURTYEIGHT = 1;
   public static final int THIRTYTWO = 2;
   private int h_layer;
   private int h_protection_bit;
   private int h_bitrate_index;
   private int h_padding_bit;
   private int h_mode_extension;
   private int h_version;
   private int h_mode;
   private int h_sample_frequency;
   private int h_number_of_subbands;
   private int h_intensity_stereo_bound;
   private boolean h_copyright;
   private boolean h_original;
   private double[] h_vbr_time_per_frame = new double[]{-1.0D, 384.0D, 1152.0D, 1152.0D};
   private boolean h_vbr;
   private int h_vbr_frames;
   private int h_vbr_scale;
   private int h_vbr_bytes;
   private byte[] h_vbr_toc;
   private byte syncmode;
   private Crc16 crc;
   public short checksum;
   public int framesize;
   public int nSlots;
   private int _headerstring;
   public static final int[][][] bitrates = new int[][][]{{{0, 32000, '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, '\u9c40', '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, '\u9c40', '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 0}}, {{0, 32000, '\ufa00', 96000, 128000, 160000, 192000, 224000, 256000, 288000, 320000, 352000, 384000, 416000, 448000, 0}, {0, 32000, '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 384000, 0}, {0, 32000, '\u9c40', '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 160000, 192000, 224000, 256000, 320000, 0}}, {{0, 32000, '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 176000, 192000, 224000, 256000, 0}, {0, 8000, 16000, 24000, 32000, '\u9c40', '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 0}, {0, 8000, 16000, 24000, 32000, '\u9c40', '\ubb80', '\udac0', '\ufa00', 80000, 96000, 112000, 128000, 144000, 160000, 0}}};
   public static final String[][][] bitrate_str = new String[][][]{{{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "64 kbit/s", "96 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "288 kbit/s", "320 kbit/s", "352 kbit/s", "384 kbit/s", "416 kbit/s", "448 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "384 kbit/s", "forbidden"}, {"free format", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "160 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "320 kbit/s", "forbidden"}}, {{"free format", "32 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "176 kbit/s", "192 kbit/s", "224 kbit/s", "256 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}, {"free format", "8 kbit/s", "16 kbit/s", "24 kbit/s", "32 kbit/s", "40 kbit/s", "48 kbit/s", "56 kbit/s", "64 kbit/s", "80 kbit/s", "96 kbit/s", "112 kbit/s", "128 kbit/s", "144 kbit/s", "160 kbit/s", "forbidden"}}};


   Header() {
      this.syncmode = Bitstream.INITIAL_SYNC;
      this._headerstring = -1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(200);
      var1.append("Layer ");
      var1.append(this.layer_string());
      var1.append(" frame ");
      var1.append(this.mode_string());
      var1.append(' ');
      var1.append(this.version_string());
      if(!this.checksums()) {
         var1.append(" no");
      }

      var1.append(" checksums");
      var1.append(' ');
      var1.append(this.sample_frequency_string());
      var1.append(',');
      var1.append(' ');
      var1.append(this.bitrate_string());
      String var2 = var1.toString();
      return var2;
   }

   void read_header(Bitstream var1, Crc16[] var2) throws BitstreamException {
      boolean var5 = false;

      int var3;
      do {
         var3 = var1.syncHeader(this.syncmode);
         this._headerstring = var3;
         if(this.syncmode == Bitstream.INITIAL_SYNC) {
            this.h_version = var3 >>> 19 & 1;
            if((var3 >>> 20 & 1) == 0) {
               if(this.h_version != 0) {
                  throw var1.newBitstreamException(256);
               }

               this.h_version = 2;
            }

            if((this.h_sample_frequency = var3 >>> 10 & 3) == 3) {
               throw var1.newBitstreamException(256);
            }
         }

         this.h_layer = 4 - (var3 >>> 17) & 3;
         this.h_protection_bit = var3 >>> 16 & 1;
         this.h_bitrate_index = var3 >>> 12 & 15;
         this.h_padding_bit = var3 >>> 9 & 1;
         this.h_mode = var3 >>> 6 & 3;
         this.h_mode_extension = var3 >>> 4 & 3;
         if(this.h_mode == 1) {
            this.h_intensity_stereo_bound = (this.h_mode_extension << 2) + 4;
         } else {
            this.h_intensity_stereo_bound = 0;
         }

         if((var3 >>> 3 & 1) == 1) {
            this.h_copyright = true;
         }

         if((var3 >>> 2 & 1) == 1) {
            this.h_original = true;
         }

         if(this.h_layer == 1) {
            this.h_number_of_subbands = 32;
         } else {
            int var4 = this.h_bitrate_index;
            if(this.h_mode != 3) {
               if(var4 == 4) {
                  var4 = 1;
               } else {
                  var4 -= 4;
               }
            }

            if(var4 != 1 && var4 != 2) {
               if(this.h_sample_frequency != 1 && (var4 < 3 || var4 > 5)) {
                  this.h_number_of_subbands = 30;
               } else {
                  this.h_number_of_subbands = 27;
               }
            } else if(this.h_sample_frequency == 2) {
               this.h_number_of_subbands = 12;
            } else {
               this.h_number_of_subbands = 8;
            }
         }

         if(this.h_intensity_stereo_bound > this.h_number_of_subbands) {
            this.h_intensity_stereo_bound = this.h_number_of_subbands;
         }

         this.calculate_framesize();
         int var6 = var1.read_frame_data(this.framesize);
         if(this.framesize >= 0 && var6 != this.framesize) {
            throw var1.newBitstreamException(261);
         }

         if(var1.isSyncCurrentPosition(this.syncmode)) {
            if(this.syncmode == Bitstream.INITIAL_SYNC) {
               this.syncmode = Bitstream.STRICT_SYNC;
               var1.set_syncword(var3 & -521024);
            }

            var5 = true;
         } else {
            var1.unreadFrame();
         }
      } while(!var5);

      var1.parse_frame();
      if(this.h_protection_bit == 0) {
         this.checksum = (short)var1.get_bits(16);
         if(this.crc == null) {
            this.crc = new Crc16();
         }

         this.crc.add_bits(var3, 16);
         var2[0] = this.crc;
      } else {
         var2[0] = null;
      }

      if(this.h_sample_frequency == 0) {
         ;
      }

   }

   void parseVBR(byte[] var1) throws BitstreamException {
      String var2 = "Xing";
      byte[] var3 = new byte[4];
      boolean var4 = false;
      byte var9;
      if(this.h_version == 1) {
         if(this.h_mode == 3) {
            var9 = 17;
         } else {
            var9 = 32;
         }
      } else if(this.h_mode == 3) {
         var9 = 9;
      } else {
         var9 = 17;
      }

      try {
         System.arraycopy(var1, var9, var3, 0, 4);
         if(var2.equals(new String(var3))) {
            this.h_vbr = true;
            this.h_vbr_frames = -1;
            this.h_vbr_bytes = -1;
            this.h_vbr_scale = -1;
            this.h_vbr_toc = new byte[100];
            byte var5 = 4;
            byte[] var6 = new byte[4];
            System.arraycopy(var1, var9 + var5, var6, 0, var6.length);
            int var10 = var5 + var6.length;
            if((var6[3] & 1) != 0) {
               System.arraycopy(var1, var9 + var10, var3, 0, var3.length);
               this.h_vbr_frames = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
               var10 += 4;
            }

            if((var6[3] & 2) != 0) {
               System.arraycopy(var1, var9 + var10, var3, 0, var3.length);
               this.h_vbr_bytes = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
               var10 += 4;
            }

            if((var6[3] & 4) != 0) {
               System.arraycopy(var1, var9 + var10, this.h_vbr_toc, 0, this.h_vbr_toc.length);
               var10 += this.h_vbr_toc.length;
            }

            if((var6[3] & 8) != 0) {
               System.arraycopy(var1, var9 + var10, var3, 0, var3.length);
               this.h_vbr_scale = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
               var10 += 4;
            }
         }
      } catch (ArrayIndexOutOfBoundsException var8) {
         throw new BitstreamException("XingVBRHeader Corrupted", var8);
      }

      String var13 = "VBRI";
      var9 = 32;

      try {
         System.arraycopy(var1, var9, var3, 0, 4);
         if(var13.equals(new String(var3))) {
            this.h_vbr = true;
            this.h_vbr_frames = -1;
            this.h_vbr_bytes = -1;
            this.h_vbr_scale = -1;
            this.h_vbr_toc = new byte[100];
            byte var12 = 10;
            System.arraycopy(var1, var9 + var12, var3, 0, var3.length);
            this.h_vbr_bytes = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
            int var11 = var12 + 4;
            System.arraycopy(var1, var9 + var11, var3, 0, var3.length);
            this.h_vbr_frames = var3[0] << 24 & -16777216 | var3[1] << 16 & 16711680 | var3[2] << 8 & '\uff00' | var3[3] & 255;
            var11 += 4;
         }

      } catch (ArrayIndexOutOfBoundsException var7) {
         throw new BitstreamException("VBRIVBRHeader Corrupted", var7);
      }
   }

   public int version() {
      return this.h_version;
   }

   public int layer() {
      return this.h_layer;
   }

   public int bitrate_index() {
      return this.h_bitrate_index;
   }

   public int sample_frequency() {
      return this.h_sample_frequency;
   }

   public int frequency() {
      return frequencies[this.h_version][this.h_sample_frequency];
   }

   public int mode() {
      return this.h_mode;
   }

   public boolean checksums() {
      return this.h_protection_bit == 0;
   }

   public boolean copyright() {
      return this.h_copyright;
   }

   public boolean original() {
      return this.h_original;
   }

   public boolean vbr() {
      return this.h_vbr;
   }

   public int vbr_scale() {
      return this.h_vbr_scale;
   }

   public byte[] vbr_toc() {
      return this.h_vbr_toc;
   }

   public boolean checksum_ok() {
      return this.checksum == this.crc.checksum();
   }

   public boolean padding() {
      return this.h_padding_bit != 0;
   }

   public int slots() {
      return this.nSlots;
   }

   public int mode_extension() {
      return this.h_mode_extension;
   }

   public int calculate_framesize() {
      if(this.h_layer == 1) {
         this.framesize = 12 * bitrates[this.h_version][0][this.h_bitrate_index] / frequencies[this.h_version][this.h_sample_frequency];
         if(this.h_padding_bit != 0) {
            ++this.framesize;
         }

         this.framesize <<= 2;
         this.nSlots = 0;
      } else {
         this.framesize = 144 * bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index] / frequencies[this.h_version][this.h_sample_frequency];
         if(this.h_version == 0 || this.h_version == 2) {
            this.framesize >>= 1;
         }

         if(this.h_padding_bit != 0) {
            ++this.framesize;
         }

         if(this.h_layer == 3) {
            if(this.h_version == 1) {
               this.nSlots = this.framesize - (this.h_mode == 3?17:32) - (this.h_protection_bit != 0?0:2) - 4;
            } else {
               this.nSlots = this.framesize - (this.h_mode == 3?9:17) - (this.h_protection_bit != 0?0:2) - 4;
            }
         } else {
            this.nSlots = 0;
         }
      }

      this.framesize -= 4;
      return this.framesize;
   }

   public int max_number_of_frames(int var1) {
      return this.h_vbr?this.h_vbr_frames:(this.framesize + 4 - this.h_padding_bit == 0?0:var1 / (this.framesize + 4 - this.h_padding_bit));
   }

   public int min_number_of_frames(int var1) {
      return this.h_vbr?this.h_vbr_frames:(this.framesize + 5 - this.h_padding_bit == 0?0:var1 / (this.framesize + 5 - this.h_padding_bit));
   }

   public float ms_per_frame() {
      if(!this.h_vbr) {
         float[][] var3 = new float[][]{{8.707483F, 8.0F, 12.0F}, {26.12245F, 24.0F, 36.0F}, {26.12245F, 24.0F, 36.0F}};
         return var3[this.h_layer - 1][this.h_sample_frequency];
      } else {
         double var1 = this.h_vbr_time_per_frame[this.layer()] / (double)this.frequency();
         if(this.h_version == 0 || this.h_version == 2) {
            var1 /= 2.0D;
         }

         return (float)(var1 * 1000.0D);
      }
   }

   public float total_ms(int var1) {
      return (float)this.max_number_of_frames(var1) * this.ms_per_frame();
   }

   public int getSyncHeader() {
      return this._headerstring;
   }

   public String layer_string() {
      switch(this.h_layer) {
      case 1:
         return "I";
      case 2:
         return "II";
      case 3:
         return "III";
      default:
         return null;
      }
   }

   public String bitrate_string() {
      return this.h_vbr?Integer.toString(this.bitrate() / 1000) + " kb/s":bitrate_str[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public int bitrate() {
      return this.h_vbr?(int)((float)(this.h_vbr_bytes * 8) / (this.ms_per_frame() * (float)this.h_vbr_frames)) * 1000:bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public int bitrate_instant() {
      return bitrates[this.h_version][this.h_layer - 1][this.h_bitrate_index];
   }

   public String sample_frequency_string() {
      switch(this.h_sample_frequency) {
      case 0:
         if(this.h_version == 1) {
            return "44.1 kHz";
         } else {
            if(this.h_version == 0) {
               return "22.05 kHz";
            }

            return "11.025 kHz";
         }
      case 1:
         if(this.h_version == 1) {
            return "48 kHz";
         } else {
            if(this.h_version == 0) {
               return "24 kHz";
            }

            return "12 kHz";
         }
      case 2:
         if(this.h_version == 1) {
            return "32 kHz";
         } else {
            if(this.h_version == 0) {
               return "16 kHz";
            }

            return "8 kHz";
         }
      default:
         return null;
      }
   }

   public String mode_string() {
      switch(this.h_mode) {
      case 0:
         return "Stereo";
      case 1:
         return "Joint stereo";
      case 2:
         return "Dual channel";
      case 3:
         return "Single channel";
      default:
         return null;
      }
   }

   public String version_string() {
      switch(this.h_version) {
      case 0:
         return "MPEG-2 LSF";
      case 1:
         return "MPEG-1";
      case 2:
         return "MPEG-2.5 LSF";
      default:
         return null;
      }
   }

   public int number_of_subbands() {
      return this.h_number_of_subbands;
   }

   public int intensity_stereo_bound() {
      return this.h_intensity_stereo_bound;
   }

}
