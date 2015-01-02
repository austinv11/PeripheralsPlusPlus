package javazoom.jl.converter;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RiffFile {

   public static final int DDC_SUCCESS = 0;
   public static final int DDC_FAILURE = 1;
   public static final int DDC_OUT_OF_MEMORY = 2;
   public static final int DDC_FILE_ERROR = 3;
   public static final int DDC_INVALID_CALL = 4;
   public static final int DDC_USER_ABORT = 5;
   public static final int DDC_INVALID_FILE = 6;
   public static final int RFM_UNKNOWN = 0;
   public static final int RFM_WRITE = 1;
   public static final int RFM_READ = 2;
   private RiffFile.RiffChunkHeader riff_header = new RiffFile.RiffChunkHeader();
   protected int fmode = 0;
   protected RandomAccessFile file = null;


   public RiffFile() {
      this.riff_header.ckID = FourCC("RIFF");
      this.riff_header.ckSize = 0;
   }

   public int CurrentFileMode() {
      return this.fmode;
   }

   public int Open(String var1, int var2) {
      int var3 = 0;
      if(this.fmode != 0) {
         var3 = this.Close();
      }

      if(var3 == 0) {
         byte[] var4;
         switch(var2) {
         case 1:
            try {
               this.file = new RandomAccessFile(var1, "rw");

               try {
                  var4 = new byte[8];
                  var4[0] = (byte)(this.riff_header.ckID >>> 24 & 255);
                  var4[1] = (byte)(this.riff_header.ckID >>> 16 & 255);
                  var4[2] = (byte)(this.riff_header.ckID >>> 8 & 255);
                  var4[3] = (byte)(this.riff_header.ckID & 255);
                  byte var5 = (byte)(this.riff_header.ckSize >>> 24 & 255);
                  byte var6 = (byte)(this.riff_header.ckSize >>> 16 & 255);
                  byte var7 = (byte)(this.riff_header.ckSize >>> 8 & 255);
                  byte var8 = (byte)(this.riff_header.ckSize & 255);
                  var4[4] = var8;
                  var4[5] = var7;
                  var4[6] = var6;
                  var4[7] = var5;
                  this.file.write(var4, 0, 8);
                  this.fmode = 1;
               } catch (IOException var11) {
                  this.file.close();
                  this.fmode = 0;
               }
            } catch (IOException var12) {
               this.fmode = 0;
               var3 = 3;
            }
            break;
         case 2:
            try {
               this.file = new RandomAccessFile(var1, "r");

               try {
                  var4 = new byte[8];
                  this.file.read(var4, 0, 8);
                  this.fmode = 2;
                  this.riff_header.ckID = var4[0] << 24 & -16777216 | var4[1] << 16 & 16711680 | var4[2] << 8 & '\uff00' | var4[3] & 255;
                  this.riff_header.ckSize = var4[4] << 24 & -16777216 | var4[5] << 16 & 16711680 | var4[6] << 8 & '\uff00' | var4[7] & 255;
               } catch (IOException var9) {
                  this.file.close();
                  this.fmode = 0;
               }
            } catch (IOException var10) {
               this.fmode = 0;
               var3 = 3;
            }
            break;
         default:
            var3 = 4;
         }
      }

      return var3;
   }

   public int Write(byte[] var1, int var2) {
      if(this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(var1, 0, var2);
            this.fmode = 1;
         } catch (IOException var4) {
            return 3;
         }

         this.riff_header.ckSize += var2;
         return 0;
      }
   }

   public int Write(short[] var1, int var2) {
      byte[] var3 = new byte[var2];
      int var4 = 0;

      for(int var5 = 0; var5 < var2; var5 += 2) {
         var3[var5] = (byte)(var1[var4] & 255);
         var3[var5 + 1] = (byte)(var1[var4++] >>> 8 & 255);
      }

      if(this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(var3, 0, var2);
            this.fmode = 1;
         } catch (IOException var6) {
            return 3;
         }

         this.riff_header.ckSize += var2;
         return 0;
      }
   }

   public int Write(RiffFile.RiffChunkHeader var1, int var2) {
      byte[] var3 = new byte[8];
      var3[0] = (byte)(var1.ckID >>> 24 & 255);
      var3[1] = (byte)(var1.ckID >>> 16 & 255);
      var3[2] = (byte)(var1.ckID >>> 8 & 255);
      var3[3] = (byte)(var1.ckID & 255);
      byte var4 = (byte)(var1.ckSize >>> 24 & 255);
      byte var5 = (byte)(var1.ckSize >>> 16 & 255);
      byte var6 = (byte)(var1.ckSize >>> 8 & 255);
      byte var7 = (byte)(var1.ckSize & 255);
      var3[4] = var7;
      var3[5] = var6;
      var3[6] = var5;
      var3[7] = var4;
      if(this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.write(var3, 0, var2);
            this.fmode = 1;
         } catch (IOException var9) {
            return 3;
         }

         this.riff_header.ckSize += var2;
         return 0;
      }
   }

   public int Write(short var1, int var2) {
      short var3 = (short)(var1 >>> 8 & 255 | var1 << 8 & '\uff00');
      if(this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.writeShort(var3);
            this.fmode = 1;
         } catch (IOException var5) {
            return 3;
         }

         this.riff_header.ckSize += var2;
         return 0;
      }
   }

   public int Write(int var1, int var2) {
      short var3 = (short)(var1 >>> 16 & '\uffff');
      short var4 = (short)(var1 & '\uffff');
      short var5 = (short)(var3 >>> 8 & 255 | var3 << 8 & '\uff00');
      short var6 = (short)(var4 >>> 8 & 255 | var4 << 8 & '\uff00');
      int var7 = var6 << 16 & -65536 | var5 & '\uffff';
      if(this.fmode != 1) {
         return 4;
      } else {
         try {
            this.file.writeInt(var7);
            this.fmode = 1;
         } catch (IOException var9) {
            return 3;
         }

         this.riff_header.ckSize += var2;
         return 0;
      }
   }

   public int Read(byte[] var1, int var2) {
      byte var3 = 0;

      try {
         this.file.read(var1, 0, var2);
      } catch (IOException var5) {
         var3 = 3;
      }

      return var3;
   }

   public int Expect(String var1, int var2) {
      boolean var3 = false;
      int var4 = 0;

      try {
         byte var7;
         do {
            if(var2-- == 0) {
               return 0;
            }

            var7 = this.file.readByte();
         } while(var7 == var1.charAt(var4++));

         return 3;
      } catch (IOException var6) {
         return 3;
      }
   }

   public int Close() {
      byte var1 = 0;
      switch(this.fmode) {
      case 1:
         try {
            this.file.seek(0L);

            try {
               byte[] var2 = new byte[]{(byte)(this.riff_header.ckID >>> 24 & 255), (byte)(this.riff_header.ckID >>> 16 & 255), (byte)(this.riff_header.ckID >>> 8 & 255), (byte)(this.riff_header.ckID & 255), (byte)(this.riff_header.ckSize & 255), (byte)(this.riff_header.ckSize >>> 8 & 255), (byte)(this.riff_header.ckSize >>> 16 & 255), (byte)(this.riff_header.ckSize >>> 24 & 255)};
               this.file.write(var2, 0, 8);
               this.file.close();
            } catch (IOException var4) {
               var1 = 3;
            }
         } catch (IOException var5) {
            var1 = 3;
         }
         break;
      case 2:
         try {
            this.file.close();
         } catch (IOException var3) {
            var1 = 3;
         }
      }

      this.file = null;
      this.fmode = 0;
      return var1;
   }

   public long CurrentFilePosition() {
      long var1;
      try {
         var1 = this.file.getFilePointer();
      } catch (IOException var4) {
         var1 = -1L;
      }

      return var1;
   }

   public int Backpatch(long var1, RiffFile.RiffChunkHeader var3, int var4) {
      if(this.file == null) {
         return 4;
      } else {
         try {
            this.file.seek(var1);
         } catch (IOException var6) {
            return 3;
         }

         return this.Write(var3, var4);
      }
   }

   public int Backpatch(long var1, byte[] var3, int var4) {
      if(this.file == null) {
         return 4;
      } else {
         try {
            this.file.seek(var1);
         } catch (IOException var6) {
            return 3;
         }

         return this.Write(var3, var4);
      }
   }

   protected int Seek(long var1) {
      byte var3;
      try {
         this.file.seek(var1);
         var3 = 0;
      } catch (IOException var5) {
         var3 = 3;
      }

      return var3;
   }

   private String DDCRET_String(int var1) {
      switch(var1) {
      case 0:
         return "DDC_SUCCESS";
      case 1:
         return "DDC_FAILURE";
      case 2:
         return "DDC_OUT_OF_MEMORY";
      case 3:
         return "DDC_FILE_ERROR";
      case 4:
         return "DDC_INVALID_CALL";
      case 5:
         return "DDC_USER_ABORT";
      case 6:
         return "DDC_INVALID_FILE";
      default:
         return "Unknown Error";
      }
   }

   public static int FourCC(String var0) {
      byte[] var1 = new byte[]{(byte)32, (byte)32, (byte)32, (byte)32};
      var0.getBytes(0, 4, var1, 0);
      int var2 = var1[0] << 24 & -16777216 | var1[1] << 16 & 16711680 | var1[2] << 8 & '\uff00' | var1[3] & 255;
      return var2;
   }

   class RiffChunkHeader {

      public int ckID = 0;
      public int ckSize = 0;


   }
}
