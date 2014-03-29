package com.labun.surf;

import java.io.Serializable;

// TODO move IJ-related code into IJFacade

/** Interest Point class. */
public class InterestPointPersistent implements Serializable {

    private static final long serialVersionUID = 1L;

	/** Interpolated X-coordinate. */
	public float x;

	/** Interpolated Y-coordinate. */
	public float y;

	/** Value of the hessian determinant (blob response) means the strength of the interest point. */
	public float strength;

	/** Trace of the hessian determinant. */
	float trace;

	/** Sign of hessian traces (laplacian sign).<br>
	 * <code>true</code> means >= 0, <code>false</code> means < 0.
	 * (Signs are saved separately for better matching performance.) */
	public boolean sign;

	/** Detected scale. */
	public float scale;

	/** Orientation measured anti-clockwise from +ve x-axis. The default is 0 (i.e. upright SURF). */
	public float orientation;

	/** Vector of descriptor components. */
	float[] descriptor;

	/** Point motion (can be used for frame to frame motion analysis). */
	float dx;

	/** Point motion (can be used for frame to frame motion analysis). */
	float dy;

	public InterestPointPersistent(float x, float y, float strength, float trace, float scale) {
		this.x = x;
		this.y = y;
		this.strength = strength;
		this.trace = trace;
		this.scale = scale;
		this.sign = (trace >= 0);
	}

    public InterestPointPersistent(float x, float y, float strength, float trace, float scale, float[] descriptor) {
        this.x = x;
        this.y = y;
        this.strength = strength;
        this.trace = trace;
        this.scale = scale;
        this.sign = (trace >= 0);
        this.descriptor = descriptor;
    }

    public InterestPoint createInterestPoint(){
        return new InterestPoint(x, y, strength, trace, scale, descriptor);
    }
}