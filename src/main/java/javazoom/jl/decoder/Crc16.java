package javazoom.jl.decoder;


public final class Crc16 {

   private static short polynomial = -32763;
   private short crc = -1;


   public void add_bits(int var1, int var2) {
      int var3 = 1 << var2 - 1;

      do {
         if((this.crc & '\u8000') == 0 ^ (var1 & var3) == 0) {
            this.crc = (short)(this.crc << 1);
            this.crc ^= polynomial;
         } else {
            this.crc = (short)(this.crc << 1);
         }
      } while((var3 >>>= 1) != 0);

   }

   public short checksum() {
      short var1 = this.crc;
      this.crc = -1;
      return var1;
   }

}
