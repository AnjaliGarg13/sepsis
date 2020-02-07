package com.nsut.spotsepsis.HeartRateMeasure.Math;


import org.apache.commons.math3.complex.Complex;

/**
 * 
 * It's written on the tin.
 *
 */
public class PoleZeroPair {

	public ComplexPair poles;
	public ComplexPair zeros;

	// single pole/zero
	public PoleZeroPair(Complex p, Complex z) {
		poles = new ComplexPair(p);
		zeros = new ComplexPair(z);
	}

	// pole/zero pair
	public PoleZeroPair(Complex p1, Complex z1, Complex p2, Complex z2) {
		poles = new ComplexPair(p1, p2);
		zeros = new ComplexPair(z1, z2);
	}

	public boolean isSinglePole() {
		return poles.second.equals(new Complex(0, 0))
				&& zeros.second.equals(new Complex(0, 0));
	}

	public boolean is_nan() {
		return poles.is_nan() || zeros.is_nan();
	}
};
