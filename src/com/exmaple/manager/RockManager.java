package com.exmaple.manager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.opengl.GLES20;
import android.util.Log;

import com.example.ScreenObjects.Rock;
import com.example.ScreenObjects.Ship;
import com.example.util.Values;

public class RockManager {
	private ArrayList<Rock> rocks, recycled;

	Random gen = new Random();
	
	private int count, sh, sw;
	private double speed;
	
	private FloatBuffer vertexBuffer;
	int mProgram;
	
	 float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

	public RockManager(int ScreenW, int ScreenH) {
		sh = ScreenH;
		sw = ScreenW;
		count = 0;
		speed = 2;
		
		rocks = new ArrayList<Rock>();
		recycled = new ArrayList<Rock>();
		
		ByteBuffer bb = ByteBuffer.allocateDirect(
                // (4 bytes/float, 100 rocks max)
                Rock.TrianglesLength() * 4 * 100);
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        
        int vertexShader = Values.loadShader(GLES20.GL_VERTEX_SHADER, Values.vertexShaderCode);
        int fragmentShader = Values.loadShader(GLES20.GL_FRAGMENT_SHADER, Values.fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);      
		
	}
	public void update(Ship ship) {
		
		for (Iterator<Rock> itr = rocks.iterator(); itr.hasNext();) {
			Rock r = itr.next();
			r.update();
			if (r.isDone()) {
				recycled.add(r);
				itr.remove();
				Log.v("Removing", "Removing");
			}
		}
		if (--count < 0) {
			Log.v("Adding", "Adding");
			addRock(ship);
			count = (int) (130*(1/speed));
		}
	}
	
		
	public void draw(float[] MVPMatrix) {
		vertexBuffer.position(0);
		//Log.v("rocks", "" + rocks.size());
		for (Rock r : rocks) {

			float[] tris = r.getTriangles();
			Log.v("S", "" + r.getY());
			
	        vertexBuffer.put(tris);
	        //Log.v("SPeed", r.getSpeed() + "");
		}
			
			
		GLES20.glUseProgram(mProgram);
		
	    
		// get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
	       
        // Prepare the triangle coordinate data
	    GLES20.glVertexAttribPointer(mPositionHandle, 3*rocks.size(),
	    		GLES20.GL_FLOAT, false,
	    		12*rocks.size(), vertexBuffer);

	    // get handle to fragment shader's vColor member
	    int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	    
	    // Set color for drawing the triangle
	    GLES20.glUniform4fv(mColorHandle, 1, color, 0);
	    
	    // get handle to shape's transformation matrix
		int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		        
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MVPMatrix, 0);
	
		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, rocks.size()*Rock.verticies*3);
	
		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
			
	}
	
	
	private void addRock(Ship ship) {
		Rock r;
		if (recycled.isEmpty()) {
			r = new Rock(sh, sw, gen.nextInt(360), 0, -1f + gen.nextFloat()*2, -1f, 0, .01f, true);
		}
		else {
			r = recycled.get(0);
			recycled.remove(0);
			r.init(sh, sw, gen.nextInt(360), 0, -1f + gen.nextFloat()*2, -1f, 0, .01f, true);
		}


		float Speed = .025f;
		if (rocks.isEmpty()) {
			if (ship != null) {
				r.setX(ship.getCenterX());
				r.fireAt(r.getX(), 1, Speed);
			}
		}

		if (ship == null) {
			r.fireAt(r.getX(), 1, Speed);
		}
		else {
			int frames = r.FramesTillY(ship.getCenterY());
			frames = 0;
			r.fireAt(ship.PredictedX(frames), ship.PredictedY(frames), 0);
		}

		rocks.add(r);
	}
	
}