package javazoom.jl.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import javazoom.jl.decoder.JavaLayerHook;

public class JavaLayerUtils {

   private static JavaLayerHook hook = null;
   static Class class$javazoom$jl$decoder$JavaLayerUtils;


   public static Object deserialize(InputStream var0, Class var1) throws IOException {
      if(var1 == null) {
         throw new NullPointerException("cls");
      } else {
         Object var2 = deserialize(var0, var1);
         if(!var1.isInstance(var2)) {
            throw new InvalidObjectException("type of deserialized instance not of required class.");
         } else {
            return var2;
         }
      }
   }

   public static Object deserialize(InputStream var0) throws IOException {
      if(var0 == null) {
         throw new NullPointerException("in");
      } else {
         ObjectInputStream var1 = new ObjectInputStream(var0);

         try {
            Object var2 = var1.readObject();
            return var2;
         } catch (ClassNotFoundException var4) {
            throw new InvalidClassException(var4.toString());
         }
      }
   }

   public static Object deserializeArray(InputStream var0, Class var1, int var2) throws IOException {
      if(var1 == null) {
         throw new NullPointerException("elemType");
      } else if(var2 < -1) {
         throw new IllegalArgumentException("length");
      } else {
         Object var3 = deserialize(var0);
         Class var4 = var3.getClass();
         if(!var4.isArray()) {
            throw new InvalidObjectException("object is not an array");
         } else {
            Class var5 = var4.getComponentType();
            if(var5 != var1) {
               throw new InvalidObjectException("unexpected array component type");
            } else {
               if(var2 != -1) {
                  int var6 = Array.getLength(var3);
                  if(var6 != var2) {
                     throw new InvalidObjectException("array length mismatch");
                  }
               }

               return var3;
            }
         }
      }
   }

   public static Object deserializeArrayResource(String var0, Class var1, int var2) throws IOException {
      InputStream var3 = getResourceAsStream(var0);
      if(var3 == null) {
         throw new IOException("unable to load resource \'" + var0 + "\'");
      } else {
         Object var4 = deserializeArray(var3, var1, var2);
         return var4;
      }
   }

   public static void serialize(OutputStream var0, Object var1) throws IOException {
      if(var0 == null) {
         throw new NullPointerException("out");
      } else if(var1 == null) {
         throw new NullPointerException("obj");
      } else {
         ObjectOutputStream var2 = new ObjectOutputStream(var0);
         var2.writeObject(var1);
      }
   }

   public static synchronized void setHook(JavaLayerHook var0) {
      hook = var0;
   }

   public static synchronized JavaLayerHook getHook() {
      return hook;
   }

   public static synchronized InputStream getResourceAsStream(String var0) {
      InputStream var1 = null;
      if(hook != null) {
         var1 = hook.getResourceAsStream(var0);
      } else {
         Class var2 = class$javazoom$jl$decoder$JavaLayerUtils == null?(class$javazoom$jl$decoder$JavaLayerUtils = class$("javazoom.jl.decoder.JavaLayerUtils")):class$javazoom$jl$decoder$JavaLayerUtils;
         var1 = var2.getResourceAsStream(var0);
      }

      return var1;
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

}
