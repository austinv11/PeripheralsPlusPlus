package javazoom.jl.player;

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;

public abstract class AudioDeviceBase implements AudioDevice {

   private boolean open = false;
   private Decoder decoder = null;


   public synchronized void open(Decoder var1) throws JavaLayerException {
      if(!this.isOpen()) {
         this.decoder = var1;
         this.openImpl();
         this.setOpen(true);
      }

   }

   protected void openImpl() throws JavaLayerException {}

   protected void setOpen(boolean var1) {
      this.open = var1;
   }

   public synchronized boolean isOpen() {
      return this.open;
   }

   public synchronized void close() {
      if(this.isOpen()) {
         this.closeImpl();
         this.setOpen(false);
         this.decoder = null;
      }

   }

   protected void closeImpl() {}

   public void write(short[] var1, int var2, int var3) throws JavaLayerException {
      if(this.isOpen()) {
         this.writeImpl(var1, var2, var3);
      }

   }

   protected void writeImpl(short[] var1, int var2, int var3) throws JavaLayerException {}

   public void flush() {
      if(this.isOpen()) {
         this.flushImpl();
      }

   }

   protected void flushImpl() {}

   protected Decoder getDecoder() {
      return this.decoder;
   }
}
