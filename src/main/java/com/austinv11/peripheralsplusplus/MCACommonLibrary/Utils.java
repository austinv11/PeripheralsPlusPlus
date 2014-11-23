package com.austinv11.peripheralsplusplus.MCACommonLibrary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utils {
	 /**
	   * Make a direct NIO FloatBuffer from an array of floats
	   * @param arr The array
	   * @return The newly created FloatBuffer
	   */
	  public static FloatBuffer makeFloatBuffer(float[] arr) {
	    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
	    bb.order(ByteOrder.nativeOrder());
	    FloatBuffer fb = bb.asFloatBuffer();
	    fb.put(arr);
	    fb.position(0);
	    return fb;
	  }
	  
	  /**
	   * Make a direct NIO ByteBuffer from an array of floats
	   * @param arr The array
	   * @return The newly created FloatBuffer
	   */
	  public static ByteBuffer makeByteBuffer(byte[] arr) {
	    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length);
	    bb.order(ByteOrder.nativeOrder());
	    bb.put(arr);
	    bb.position(0);
	    return bb;
	  }
}
