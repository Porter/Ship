package com.example.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import android.opengl.GLES20;
import android.util.Log;

import com.example.ScreenObjects.Artifact;
import com.example.util.Graphics;
import com.example.util.Rect;
import com.example.util.Values;

public class Triangle extends Artifact {
	protected short[] x, y, moddedx, moddedy;

	protected double centerX, centerY, TransX, TransY, rotation, dx, dy, dr;
	
	private boolean dead = false;

	protected int r, g, b, a;
	
	private int mProgram;

	protected Rect bounds;

	Random gen = new Random();
	
	private FloatBuffer vertexBuffer;
	private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = { // in counterclockwise order:
         0f,  0f, 00.0f,   // top
         300f, 300f,  00.0f,   // bottom left
         0f,  300f, 0.0f    // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };


	public Triangle(int[] x, int[] y)  {
		init(x[0], y[0], x[1], y[1], x[2], y[2]);
		bounds = new Rect(0, 0, 0, 0);
	}

	public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		init(x1, y1, x2, y2, x3, y3);
		bounds = new Rect(0, 0, 0, 0);
		
		Log.v("Width: ", ""+x1);
		Log.v("Height: ", ""+y1);
		
		triangleCoords = new float[]{ // in counterclockwise order:
		         0.0f,  0.622008459f, 0.0f,   // top
		         -0.5f, -0.311004243f, 0.0f,   // bottom left
		          0.5f, -0.311004243f, 0.0f    // bottom right
		     };
		
		ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        
        int vertexShader = Values.loadShader(GLES20.GL_VERTEX_SHADER, Values.vertexShaderCode);
        int fragmentShader = Values.loadShader(GLES20.GL_FRAGMENT_SHADER, Values.fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // creates OpenGL ES program executables

		
	}

	public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, double launchingAngle, double speed) {
		init(x1, y1, x2, y2, x3, y3, launchingAngle, speed);
		bounds = new Rect(0, 0, 0, 0);
	}


	protected void init(int x1, int y1, int x2, int y2, int x3, int y3) {
		x = new short[3];
		y = new short[3];
		moddedx = new short[3];
		moddedy = new short[3];
	
		x[0] = (short) x1; x[1] = (short) x2; x[2] = (short) x3;
		y[0] = (short) y1; y[1] = (short) y2; y[2] = (short) y3;
	
		moddedx[0] = (short) x1; moddedx[1] = (short) x2; moddedx[2] = (short) x3;
		moddedy[0] = (short) y1; moddedy[1] = (short) y2; moddedy[2] = (short) y3;
	
		centerX = (x1 + x2 + x3) / 3;
		centerY = (y1 + y2 + y3) / 3;
	
		TransX = TransY = rotation = dx = dy = dr = 0;
		r = gen.nextInt(170) + 85;
		g = gen.nextInt(170) + 85;
		b = gen.nextInt(170) + 85;
		a = gen.nextInt(128) + 128;
	}

	void init(int x1, int y1, int x2, int y2, int x3, int y3, double launchAngle, double speed) {
		init(x1, y1, x2, y2, x3, y3);
		dx = Math.cos(3.1415*launchAngle/180.0)*speed;
		dy = -Math.sin(3.1415*launchAngle/180.0)*speed;
		dr = ((gen.nextInt(100)) - 50) / 1000.0;
	}
	
	void updateBounds() {
		int x = moddedx[0], r = x;
		int y = moddedy[0], b = y;
		for (int i = 1; i < 3; i++) {
			if (moddedx[i] < x) x = moddedx[i];
			if (moddedx[i] > r) r = moddedx[i];
			if (moddedy[i] < y) y = moddedy[i];
			if (moddedy[i] > b) b = moddedy[i];
		}
		bounds.set(x, y, r-x, b-y);
	}
	
	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);
		

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
       // triangleCoords[6] -= 1;
        
       // triangleCoords[0]+=.001;
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);
		
	} 
	
	public void update() { 
		TranslateX(dx);
		TranslateY(dy);
		rotate(dr);
	
		updateBounds();
		if (!bounds.onScreen()) { dead = true; }
	}
	
	void TranslateX(double dx) { TransX += dx; }
	void TranslateY(double dy) { TransY += dy; }
	
	void rotate(double degrees) {
		setRotation(rotation + degrees);
	}
	
	void setRotation(double degrees) {
		rotation = degrees;
	
		double dx, dy;
	
		for (int i = 0; i < 3; i++) {
			dx = x[i] - centerX;
			dy = y[i] - centerY;
	
			double s = Math.sin(degrees);
			double c = Math.cos(degrees);
	
			double xnew = dx*c - dy*s;
			double ynew = dx*s + dy*c;
	
			moddedx[i] = (short) (xnew + centerX + TransX);
			moddedy[i] = (short) (ynew + centerY + TransY);
		}
	}
	
	void setRGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	void multiplySpeed(double n) {
		dx *= n;
		dy *= n;
	}
	
	void multiplyRotationSpeed(double n) {
		dr *= n;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("TAG", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
	
}
