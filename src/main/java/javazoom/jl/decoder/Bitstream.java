package javazoom.jl.decoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import javazoom.jl.decoder.BitstreamErrors;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Crc16;
import javazoom.jl.decoder.Header;

public final class Bitstream implements BitstreamErrors {

   static byte INITIAL_SYNC = 0;
   static byte STRICT_SYNC = 1;
   private static final int BUFFER_INT_SIZE = 433;
   private final int[] framebuffer = new int[433];
   private int framesize;
   private byte[] frame_bytes = new byte[1732];
   private int wordpointer;
   private int bitindex;
   private int syncword;
   private int header_pos = 0;
   private boolean single_ch_mode;
   private final int[] bitmask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, '\uffff', 131071};
   private final PushbackInputStream source;
   private final Header header = new Header();
   private final byte[] syncbuf = new byte[4];
   private Crc16[] crc = new Crc16[1];
   private byte[] rawid3v2 = null;
   private boolean firstframe = true;


   public Bitstream(InputStream var1) {
      if(var1 == null) {
         throw new NullPointerException("in");
      } else {
         BufferedInputStream var2 = new BufferedInputStream(var1);
         this.loadID3v2(var2);
         this.firstframe = true;
         this.source = new PushbackInputStream(var2, 1732);
         this.closeFrame();
      }
   }

   public int header_pos() {
      return this.header_pos;
   }

   private void loadID3v2(InputStream var1) {
      int var2 = -1;

      try {
         var1.mark(10);
         var2 = this.readID3v2Header(var1);
         this.header_pos = var2;
      } catch (IOException var14) {
         ;
      } finally {
         try {
            var1.reset();
         } catch (IOException var12) {
            ;
         }

      }

      try {
         if(var2 > 0) {
            this.rawid3v2 = new byte[var2];
            var1.read(this.rawid3v2, 0, this.rawid3v2.length);
         }
      } catch (IOException var13) {
         ;
      }

   }

   private int readID3v2Header(InputStream var1) throws IOException {
      byte[] var2 = new byte[4];
      int var3 = -10;
      var1.read(var2, 0, 3);
      if(var2[0] == 73 && var2[1] == 68 && var2[2] == 51) {
         var1.read(var2, 0, 3);
         byte var4 = var2[0];
         byte var5 = var2[1];
         var1.read(var2, 0, 4);
         var3 = (var2[0] << 21) + (var2[1] << 14) + (var2[2] << 7) + var2[3];
      }

      return var3 + 10;
   }

   public InputStream getRawID3v2() {
      if(this.rawid3v2 == null) {
         return null;
      } else {
         ByteArrayInputStream var1 = new ByteArrayInputStream(this.rawid3v2);
         return var1;
      }
   }

   public void close() throws BitstreamException {
      try {
         this.source.close();
      } catch (IOException var2) {
         throw this.newBitstreamException(258, var2);
      }
   }

   public Header readFrame() throws BitstreamException {
      Header var1 = null;

      try {
         var1 = this.readNextFrame();
         if(this.firstframe) {
            var1.parseVBR(this.frame_bytes);
            this.firstframe = false;
         }
      } catch (BitstreamException var5) {
         if(var5.getErrorCode() == 261) {
            try {
               this.closeFrame();
               var1 = this.readNextFrame();
            } catch (BitstreamException var4) {
               if(var4.getErrorCode() != 260) {
                  throw this.newBitstreamException(var4.getErrorCode(), var4);
               }
            }
         } else if(var5.getErrorCode() != 260) {
            throw this.newBitstreamException(var5.getErrorCode(), var5);
         }
      }

      return var1;
   }

   private Header readNextFrame() throws BitstreamException {
      if(this.framesize == -1) {
         this.nextFrame();
      }

      return this.header;
   }

   private void nextFrame() throws BitstreamException {
      this.header.read_header(this, this.crc);
   }

   public void unreadFrame() throws BitstreamException {
      if(this.wordpointer == -1 && this.bitindex == -1 && this.framesize > 0) {
         try {
            this.source.unread(this.frame_bytes, 0, this.framesize);
         } catch (IOException var2) {
            throw this.newBitstreamException(258);
         }
      }

   }

   public void closeFrame() {
      this.framesize = -1;
      this.wordpointer = -1;
      this.bitindex = -1;
   }

   public boolean isSyncCurrentPosition(int var1) throws BitstreamException {
      int var2 = this.readBytes(this.syncbuf, 0, 4);
      int var3 = this.syncbuf[0] << 24 & -16777216 | this.syncbuf[1] << 16 & 16711680 | this.syncbuf[2] << 8 & '\uff00' | this.syncbuf[3] << 0 & 255;

      try {
         this.source.unread(this.syncbuf, 0, var2);
      } catch (IOException var5) {
         ;
      }

      boolean var4 = false;
      switch(var2) {
      case 0:
         var4 = true;
         break;
      case 4:
         var4 = this.isSyncMark(var3, var1, this.syncword);
      }

      return var4;
   }

   public int readBits(int var1) {
      return this.get_bits(var1);
   }

   public int readCheckedBits(int var1) {
      return this.get_bits(var1);
   }

   protected BitstreamException newBitstreamException(int var1) {
      return new BitstreamException(var1, (Throwable)null);
   }

   protected BitstreamException newBitstreamException(int var1, Throwable var2) {
      return new BitstreamException(var1, var2);
   }

   int syncHeader(byte var1) throws BitstreamException {
      int var4 = this.readBytes(this.syncbuf, 0, 3);
      if(var4 != 3) {
         throw this.newBitstreamException(260, (Throwable)null);
      } else {
         int var3 = this.syncbuf[0] << 16 & 16711680 | this.syncbuf[1] << 8 & '\uff00' | this.syncbuf[2] << 0 & 255;

         boolean var2;
         do {
            var3 <<= 8;
            if(this.readBytes(this.syncbuf, 3, 1) != 1) {
               throw this.newBitstreamException(260, (Throwable)null);
            }

            var3 |= this.syncbuf[3] & 255;
            var2 = this.isSyncMark(var3, var1, this.syncword);
         } while(!var2);

         return var3;
      }
   }

   public boolean isSyncMark(int var1, int var2, int var3) {
      boolean var4 = false;
      if(var2 == INITIAL_SYNC) {
         var4 = (var1 & -2097152) == -2097152;
      } else {
         var4 = (var1 & -521216) == var3 && (var1 & 192) == 192 == this.single_ch_mode;
      }

      if(var4) {
         var4 = (var1 >>> 10 & 3) != 3;
      }

      if(var4) {
         var4 = (var1 >>> 17 & 3) != 0;
      }

      if(var4) {
         var4 = (var1 >>> 19 & 3) != 1;
      }

      return var4;
   }

   int read_frame_data(int var1) throws BitstreamException {
      boolean var2 = false;
      int var3 = this.readFully(this.frame_bytes, 0, var1);
      this.framesize = var1;
      this.wordpointer = -1;
      this.bitindex = -1;
      return var3;
   }

   void parse_frame() throws BitstreamException {
      int var1 = 0;
      byte[] var2 = this.frame_bytes;
      int var3 = this.framesize;

      for(int var4 = 0; var4 < var3; var4 += 4) {
         boolean var5 = false;
         boolean var6 = false;
         byte var7 = 0;
         byte var8 = 0;
         byte var9 = 0;
         byte var10 = var2[var4];
         if(var4 + 1 < var3) {
            var7 = var2[var4 + 1];
         }

         if(var4 + 2 < var3) {
            var8 = var2[var4 + 2];
         }

         if(var4 + 3 < var3) {
            var9 = var2[var4 + 3];
         }

         this.framebuffer[var1++] = var10 << 24 & -16777216 | var7 << 16 & 16711680 | var8 << 8 & '\uff00' | var9 & 255;
      }

      this.wordpointer = 0;
      this.bitindex = 0;
   }

   public int get_bits(int var1) {
      boolean var2 = false;
      int var3 = this.bitindex + var1;
      if(this.wordpointer < 0) {
         this.wordpointer = 0;
      }

      int var6;
      if(var3 <= 32) {
         var6 = this.framebuffer[this.wordpointer] >>> 32 - var3 & this.bitmask[var1];
         if((this.bitindex += var1) == 32) {
            this.bitindex = 0;
            ++this.wordpointer;
         }

         return var6;
      } else {
         int var4 = this.framebuffer[this.wordpointer] & '\uffff';
         ++this.wordpointer;
         int var5 = this.framebuffer[this.wordpointer] & -65536;
         var6 = var4 << 16 & -65536 | var5 >>> 16 & '\uffff';
         var6 >>>= 48 - var3;
         var6 &= this.bitmask[var1];
         this.bitindex = var3 - 32;
         return var6;
      }
   }

   void set_syncword(int var1) {
      this.syncword = var1 & -193;
      this.single_ch_mode = (var1 & 192) == 192;
   }

   private int readFully(byte[] var1, int var2, int var3) throws BitstreamException {
      int var4 = 0;

      try {
         while(var3 > 0) {
            int var5 = this.source.read(var1, var2, var3);
            if(var5 == -1) {
               while(var3-- > 0) {
                  var1[var2++] = 0;
               }

               return var4;
            }

            var4 += var5;
            var2 += var5;
            var3 -= var5;
         }

         return var4;
      } catch (IOException var6) {
         throw this.newBitstreamException(258, var6);
      }
   }

   private int readBytes(byte[] var1, int var2, int var3) throws BitstreamException {
      int var4 = 0;

      try {
         while(var3 > 0) {
            int var5 = this.source.read(var1, var2, var3);
            if(var5 == -1) {
               break;
            }

            var4 += var5;
            var2 += var5;
            var3 -= var5;
         }

         return var4;
      } catch (IOException var6) {
         throw this.newBitstreamException(258, var6);
      }
   }

}
