package javazoom.jl.decoder;

import java.io.PrintStream;

public class JavaLayerException extends Exception {

   private Throwable exception;


   public JavaLayerException() {}

   public JavaLayerException(String var1) {
      super(var1);
   }

   public JavaLayerException(String var1, Throwable var2) {
      super(var1);
      this.exception = var2;
   }

   public Throwable getException() {
      return this.exception;
   }

   public void printStackTrace() {
      this.printStackTrace(System.err);
   }

   public void printStackTrace(PrintStream var1) {
      if(this.exception == null) {
         super.printStackTrace(var1);
      } else {
         this.exception.printStackTrace();
      }

   }
}
