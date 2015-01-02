package javazoom.jl.decoder;

import javazoom.jl.decoder.DecoderErrors;
import javazoom.jl.decoder.JavaLayerException;

public class DecoderException extends JavaLayerException implements DecoderErrors {

   private int errorcode;


   public DecoderException(String var1, Throwable var2) {
      super(var1, var2);
      this.errorcode = 512;
   }

   public DecoderException(int var1, Throwable var2) {
      this(getErrorString(var1), var2);
      this.errorcode = var1;
   }

   public int getErrorCode() {
      return this.errorcode;
   }

   public static String getErrorString(int var0) {
      return "Decoder errorcode " + Integer.toHexString(var0);
   }
}
