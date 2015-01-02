package javazoom.jl.decoder;


final class BitReserve {

   private static final int BUFSIZE = 32768;
   private static final int BUFSIZE_MASK = 32767;
   private int offset = 0;
   private int totbit = 0;
   private int buf_byte_idx = 0;
   private final int[] buf = new int['\u8000'];
   private int buf_bit_idx;


   public int hsstell() {
      return this.totbit;
   }

   public int hgetbits(int var1) {
      this.totbit += var1;
      int var2 = 0;
      int var3 = this.buf_byte_idx;
      if(var3 + var1 < '\u8000') {
         while(var1-- > 0) {
            var2 <<= 1;
            var2 |= this.buf[var3++] != 0?1:0;
         }
      } else {
         while(var1-- > 0) {
            var2 <<= 1;
            var2 |= this.buf[var3] != 0?1:0;
            var3 = var3 + 1 & 32767;
         }
      }

      this.buf_byte_idx = var3;
      return var2;
   }

   public int hget1bit() {
      ++this.totbit;
      int var1 = this.buf[this.buf_byte_idx];
      this.buf_byte_idx = this.buf_byte_idx + 1 & 32767;
      return var1;
   }

   public void hputbuf(int var1) {
      int var2 = this.offset;
      this.buf[var2++] = var1 & 128;
      this.buf[var2++] = var1 & 64;
      this.buf[var2++] = var1 & 32;
      this.buf[var2++] = var1 & 16;
      this.buf[var2++] = var1 & 8;
      this.buf[var2++] = var1 & 4;
      this.buf[var2++] = var1 & 2;
      this.buf[var2++] = var1 & 1;
      if(var2 == '\u8000') {
         this.offset = 0;
      } else {
         this.offset = var2;
      }

   }

   public void rewindNbits(int var1) {
      this.totbit -= var1;
      this.buf_byte_idx -= var1;
      if(this.buf_byte_idx < 0) {
         this.buf_byte_idx += '\u8000';
      }

   }

   public void rewindNbytes(int var1) {
      int var2 = var1 << 3;
      this.totbit -= var2;
      this.buf_byte_idx -= var2;
      if(this.buf_byte_idx < 0) {
         this.buf_byte_idx += '\u8000';
      }

   }
}
