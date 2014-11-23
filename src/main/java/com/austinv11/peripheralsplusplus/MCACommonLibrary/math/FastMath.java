package com.austinv11.peripheralsplusplus.MCACommonLibrary.math;

public class FastMath {
    /** A "close to zero" double epsilon value for use*/
    public static final double DBL_EPSILON = 2.220446049250313E-16d;
    /** A "close to zero" float epsilon value for use*/
    public static final float FLT_EPSILON = 1.1920928955078125E-7f;
    /** A "close to zero" float epsilon value for use*/
    public static final float ZERO_TOLERANCE = 0.0001f;
    /** The value PI as a float (180 degrees). */
    public static final float PI = (float) Math.PI;
    
	/**
     * Returns the square root of a given value.
     */
    public static float sqrt(float fValue) {
        return (float) Math.sqrt(fValue);
    }
    
    /**
     * Returns 1/sqrt(fValue)
     */
    public static float invSqrt(float fValue) {
        return (float) (1.0f / Math.sqrt(fValue));
    }
    
    /**
     * Returns Absolute value of a float.
     */
    public static float abs(float fValue) {
        if (fValue < 0) {
            return -fValue;
        }
        return fValue;
    }
    
    public static float cos(float v) {
        return (float) Math.cos(v);
    }

    public static float sin(float v) {
        return (float) Math.sin(v);
    }
    
    /**
     * Returns the arc cosine of an angle given in radians.
     * Special cases:
     * If fValue is smaller than -1, then the result is PI.
     * If the argument is greater than 1, then the result is 0.
     */
    public static float acos(float fValue) {
        if (-1.0f < fValue) {
            if (fValue < 1.0f) {
                return (float) Math.acos(fValue);
            }

            return 0.0f;
        }

        return PI;
    }
}
