package com.example.ship;


import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.example.objects.Triangle;
import com.example.util.Values;
import com.exmaple.manager.RockManager;

public class MyRenderer implements GLSurfaceView.Renderer {


	Triangle tri;
	RockManager rocks;
	private final float[] mProjMatrix = new float[16], mVMatrix = new float[16], mMVPMatrix = new float[16];
	
	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        for (int i = 0; i < 16; i++) {
        	//mMVPMatrix[i] = i;
        }
        
        // Draw triangle
        rocks.update(null);
        //tri.draw(mMVPMatrix);
       // long time = System.currentTimeMillis();
        rocks.draw(mMVPMatrix);
        //Log.v("Time", "" + (System.currentTimeMillis() - time) + "ms");
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		Values.screenWidth = width;
		Values.screenHeight = height;
		tri = new Triangle(width, height, 0, 0, 0, 0);
		rocks = new RockManager(width, height);
		
		final float ratio = (float) width / height;
	    final float left = -ratio;
	    final float right = ratio;
	    final float bottom = 1f;
	    final float top = -1f;
	    final float near = 1.0f;
	    final float far = 10.0f;
	 
	    Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	    Matrix.setLookAtM(mVMatrix, 0, 0, 0, -1, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	public void onSurfaceCreated(GL10 arg0, javax.microedition.khronos.egl.EGLConfig arg1) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		

	}
}
