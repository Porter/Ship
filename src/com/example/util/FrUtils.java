package com.example.util;

public class FrUtils {
	
	public static void rotate(float[] rotated, float[] to_rotate, float[] center, float degrees) {
		float s = (float) Math.sin(3.1415 * degrees / 180.);
		float c = (float) Math.cos(3.1415 * degrees / 180.);

		rotated[0] = (to_rotate[0] - center[0]) * c - (to_rotate[1] - center[1]) * s;
		rotated[1] = (to_rotate[0] - center[0]) * s + (to_rotate[1] - center[1]) * c;

		rotated[0] += center[0];
		rotated[1] += center[1];
	}
	
	public static void rotate(float[] rotated, float[] to_rotate, float degrees) {
		float s = (float) Math.sin(degrees);
		float c = (float) Math.cos(degrees);

		rotated[0] = to_rotate[0] * c - to_rotate[1] * s;
		rotated[1] = to_rotate[0] * s + to_rotate[1] * c;
	}

	public static void scale(float[] scaled, float[] to_scale, float[] center, float amount) {
		scaled[0] = ((to_scale[0] - center[0]) * amount) + center[0];
		scaled[1] = ((to_scale[1] - center[1]) * amount) + center[1];
	}
	public static void scale(float[] scaled, float[] to_scale, float amount) {
		scaled[0] = to_scale[0] * amount;
		scaled[1] = to_scale[1] * amount;
	}

	public static boolean PointInPoly(float testx, float testy, float[] x, float[] y, int size) {
		int i, j;
		boolean c = false;
		for (i = 0, j = size-1; i < size; j = i++) {
			if ( ((y[i]>testy) != (y[j]>testy)) &&
					(testx < (x[j]-x[i]) * (testy-y[i]) / (y[j]-y[i]) + x[i]) )
				c = !c;
		}
		return c;
	}
	public static boolean PointInPoly(float testx, float testy, float[] points, int size) {
		int i, j;
		boolean c = false;
		for (i = 0, j = 2*size-2; i < size*2; j = i, i += 2) {
			if ( ((points[i+1]>testy) != (points[j+1]>testy)) &&
					(testx < (points[j]-points[i]) * (testy-points[i+1]) / (points[j+1]-points[i+1]) + points[i]) )
				c = !c;
		}
		return c;
	}
}
