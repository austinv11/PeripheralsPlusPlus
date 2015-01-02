package javazoom.jl.decoder;

import javazoom.jl.decoder.BitstreamErrors;
import javazoom.jl.decoder.JavaLayerException;

public class BitstreamException extends JavaLayerException implements BitstreamErrors {

   private int errorcode;


   public BitstreamException(String var1, Throwable var2) {
      super(var1, var2);
      this.errorcode = 256;
   }

   public BitstreamException(int var1, Throwable var2) {
      this(getErrorString(var1), var2);
      this.errorcode = var1;
   }

   public int getErrorCode() {
      return this.errorcode;
   }

   public static String getErrorString(int var0) {
      return "Bitstream errorcode " + Integer.toHexString(var0);
   }
}
