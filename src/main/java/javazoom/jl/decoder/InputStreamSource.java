package javazoom.jl.decoder;

import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.Source;

public class InputStreamSource implements Source {

   private final InputStream in;


   public InputStreamSource(InputStream var1) {
      if(var1 == null) {
         throw new NullPointerException("in");
      } else {
         this.in = var1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      return var4;
   }

   public boolean willReadBlock() {
      return true;
   }

   public boolean isSeekable() {
      return false;
   }

   public long tell() {
      return -1L;
   }

   public long seek(long var1) {
      return -1L;
   }

   public long length() {
      return -1L;
   }
}
