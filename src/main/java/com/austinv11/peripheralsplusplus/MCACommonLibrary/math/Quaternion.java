package com.austinv11.peripheralsplusplus.MCACommonLibrary.math;

import java.lang.Math;

/**
 * A 4 element unit quaternion represented by single precision floating
 * point x,y,z,w coordinates.  The quaternion is always normalized.
 */
public class Quaternion implements java.io.Serializable {

	public static final Quaternion EMPTY = new Quaternion(0,0,0,0);
	final static double EPS = 0.000001;

	public float x;
	public float y;
	public float z;
	public float w;

	public Quaternion(float x, float y, float z, float w)
	{
		float mag;
		mag = (float)(1.0/Math.sqrt( x*x + y*y + z*z + w*w ));
		this.x =  x*mag;
		this.y =  y*mag;
		this.z =  z*mag;
		this.w =  w*mag;
	}

	public Quaternion()
	{
		this(0F, 0F, 0F, 0F);
	}

	public Quaternion(Quaternion q1)
	{
		this.x = q1.x;
		this.y = q1.y;
		this.z = q1.z;
		this.w = q1.w;
	}

	/** Aassumes axis is already normalised. */
	public Quaternion(Vector3f axis, float angle) {
		double s = Math.sin(angle/2);
		x = (float) (axis.x * s);
		y = (float) (axis.y * s);
		z = (float) (axis.z * s);
		w = (float) Math.cos(angle/2);
	}

	public Quaternion(Matrix4f mat)
	{
		double T = 1 + mat.m00 + mat.m11 + mat.m22;
		if( T > 0.00000001 ) //to avoid large distortions!
		{
			double S = Math.sqrt(T) * 2;
			this.x = (float) (( mat.m12 - mat.m21 ) / S);
			this.y = (float) (( mat.m02 - mat.m20 ) / S);
			this.z = (float) (( mat.m10 - mat.m01 ) / S);
			this.w = (float) (0.25 * S);
		} else if(T == 0)
		{
			if ( mat.m00 > mat.m11 && mat.m00 > mat.m22 )  {	// Column 0: 
				double S  = Math.sqrt( 1.0 + mat.m00 - mat.m11 - mat.m22 ) * 2;
				this.x = (float) (0.25 * S);
				this.y = (float) ((mat.m10 + mat.m01 ) / S);
				this.z = (float) ((mat.m02 + mat.m20 ) / S);
				this.w = (float) ((mat.m21 - mat.m12 ) / S);
			} else if ( mat.m11 > mat.m22 ) {			// Column 1: 
				double S  = Math.sqrt( 1.0 + mat.m11 - mat.m00 - mat.m22 ) * 2;
				this.x = (float) ((mat.m10 + mat.m01 ) / S);
				this.y = (float) (0.25 * S);
				this.z = (float) ((mat.m21 + mat.m12 ) / S);
				this.w = (float) ((mat.m02 - mat.m20 ) / S);
			} else {						// Column 2:
				double S  = Math.sqrt( 1.0 + mat.m22 - mat.m00 - mat.m11 ) * 2;
				this.x = (float) ((mat.m02 + mat.m20 ) / S);
				this.y = (float) ((mat.m21 + mat.m12 ) / S);
				this.z = (float) (0.25 * S);
				this.w = (float) ((mat.m10 - mat.m01 ) / S);
			}
		}
	}

	/** Sets the value of this quaternion to the conjugate of quaternion q1 */
	public final void conjugate(Quaternion q1)
	{
		this.x = -q1.x;
		this.y = -q1.y;
		this.z = -q1.z;
		this.w = q1.w;
	}

	/** Sets the value of this quaternion to the conjugate of itself */
	public final void conjugate()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/**
	 * Sets the value of this quaternion to the quaternion product of
	 * quaternions q1 and q2 (this = q1 * q2). 
	 * Note that this is safe for aliasing (e.g. this can be q1 or q2).
	 * This operation is used for adding the 2 orientations.
	 */
	public final void mul(Quaternion q1, Quaternion q2)
	{
		if (this != q1 && this != q2) {
			this.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			this.x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			this.y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
		} else {
			float x, y, w;

			w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
			x = q1.w*q2.x + q2.w*q1.x + q1.y*q2.z - q1.z*q2.y;
			y = q1.w*q2.y + q2.w*q1.y - q1.x*q2.z + q1.z*q2.x;
			this.z = q1.w*q2.z + q2.w*q1.z + q1.x*q2.y - q1.y*q2.x;
			this.w = w;
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Multiplies quaternion q1 by the inverse of quaternion q2 and places
	 * the value into this quaternion.  The value of both argument quaternions
	 * is preservered (this = q1 * q2^-1)
	 */
	public final void mulInverse(Quaternion q1, Quaternion q2)
	{  
		Quaternion  tempQuat = new Quaternion(q2); 

		tempQuat.inverse();
		this.mul(q1, tempQuat);
	}

	/**
	 * Sets the value of this quaternion to quaternion inverse of quaternion q1.
	 * @param q1 the quaternion to be inverted
	 */
	public final void inverse(Quaternion q1)
	{
		float norm;

		norm = 1.0f/(q1.w*q1.w + q1.x*q1.x + q1.y*q1.y + q1.z*q1.z);
		this.w =  norm*q1.w;
		this.x = -norm*q1.x;
		this.y = -norm*q1.y;
		this.z = -norm*q1.z;
	}

	/**
	 * Sets the value of this quaternion to the quaternion inverse of itself.
	 */
	public final void inverse()
	{
		float norm; 

		norm = 1.0f/(this.w*this.w + this.x*this.x + this.y*this.y + this.z*this.z);
		this.w *=  norm;
		this.x *= -norm;
		this.y *= -norm;
		this.z *= -norm;
	}

	/**
	 * Sets the value of this quaternion to the normalized value
	 * of quaternion q1.
	 * @param q1 the quaternion to be normalized.
	 */
	public final void normalize(Quaternion q1)
	{
		float norm;

		norm = (q1.x*q1.x + q1.y*q1.y + q1.z*q1.z + q1.w*q1.w);

		if (norm > 0.0f) {
			norm = 1.0f/(float)Math.sqrt(norm);
			this.x = norm*q1.x;
			this.y = norm*q1.y;
			this.z = norm*q1.z;
			this.w = norm*q1.w;
		} else {
			this.x = (float) 0.0;
			this.y = (float) 0.0;
			this.z = (float) 0.0;
			this.w = (float) 0.0;
		}
	}


	/**
	 * Normalizes the value of this quaternion in place.
	 */
	public final void normalize()
	{
		float norm;

		norm = (this.x*this.x + this.y*this.y + this.z*this.z + this.w*this.w);

		if (norm > 0.0f) {
			norm = 1.0f / (float)Math.sqrt(norm);
			this.x *= norm;
			this.y *= norm;
			this.z *= norm;
			this.w *= norm;
		} else {
			this.x = (float) 0.0;
			this.y = (float) 0.0;
			this.z = (float) 0.0;
			this.w = (float) 0.0;
		}
	}

	/**
	 *  Performs a great circle interpolation between quaternion q1
	 *  and quaternion q2 and places the result into this quaternion.
	 *  This is called SLERP
	 *  @param q1  the first quaternion
	 *  @param q2  the second quaternion
	 *  @param alpha  the alpha interpolation parameter
	 */  
	public final void interpolate(Quaternion q1, Quaternion q2, float alpha) {
		// From "Advanced Animation and Rendering Techniques"
		// by Watt and Watt pg. 364, function as implemented appeared to be
		// incorrect.  Fails to choose the same quaternion for the double
		// covering. Resulting in change of direction for rotations.
		// Fixed function to negate the first quaternion in the case that the
		// dot product of q1 and this is negative. Second case was not needed.

		double dot,s1,s2,om,sinom;

		dot = q2.x*q1.x + q2.y*q1.y + q2.z*q1.z + q2.w*q1.w;

		if ( dot < 0 ) {
			// negate quaternion
			q1.x = -q1.x;  q1.y = -q1.y;  q1.z = -q1.z;  q1.w = -q1.w;
			dot = -dot;
		}

		if ( (1.0 - dot) > EPS ) {
			om = Math.acos(dot);
			sinom = Math.sin(om);
			s1 = Math.sin((1.0-alpha)*om)/sinom;
			s2 = Math.sin( alpha*om)/sinom;
		} else{
			s1 = 1.0 - alpha;
			s2 = alpha;
		}
		w = (float)(s1*q1.w + s2*q2.w);
		x = (float)(s1*q1.x + s2*q2.x);
		y = (float)(s1*q1.y + s2*q2.y);
		z = (float)(s1*q1.z + s2*q2.z);
	}
}
