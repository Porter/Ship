package com.example.ScreenObjects;

import com.example.util.FrUtils;
import com.example.util.Graphics;
import com.example.util.Rect;
import com.example.util.Values;





public class Rock extends ObjectWithBounds {
	
	private static final float scale = .10f;
	public static final int verticies = 6;
	
	private float x, y, w, h, dx, dy, rotation, Drotation, originalLife, ScreenWidth;
	private float[] IdPoints, points, triangles;
	private int FramesSinceHit;
	private boolean done, mainScreen, inited;
	private final static int r = 139, g = 69, b = 19;
	
	public Rock(int sh, int sw, float rotation, float Drotation, float x, float y, float dx, float dy, boolean main) {
		inited = false;
		init(sh, sw, rotation, Drotation, x, y, dx, dy, main);
	}

	public void init(int sh, int sw, float rotation, float Drotation, float x, float y, float dx, float dy, boolean main) {
		if (!inited) {
			bounds = new Rect(0, 0, 0, 0);
			LoadIdentityPoints();
			points = new float[verticies*2];
			triangles = new float[(verticies-2) * 9];
		}
		done = false;
		ScreenWidth = sw;
	
	
	
		this.rotation = rotation;
	
		this.Drotation = Drotation;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mainScreen = main;
	
	
		updatePoints();
		updateBounds();
	
		inited = true;
}


	public void LoadIdentityPoints() {
		IdPoints = new float[verticies*2];
		float angle = 360.0f / verticies;
	
		for (int i = 0; i < verticies; i++) {
			IdPoints[2*i]     = (float) (Math.cos(3.1415 * i*angle / 180.0) - Math.sin(3.1415 * i*angle / 180.0));
			IdPoints[2*i + 1] = (float) (Math.sin(3.1415 * i*angle / 180.0) + Math.cos(3.1415 * i*angle / 180.0));
		}
	}
	
	public void updatePoints() {
		float[] Final = new float[2];
		float[] id = new float[2];
		for (int i = 0; i < verticies; i++) {
			id[0] = IdPoints[2*i]; id[1] = IdPoints[2*i + 1];
			FrUtils.rotate(Final, id, rotation);
			FrUtils.scale(Final, Final, scale);
			points[2*i]     = Final[0] + x;
			points[2*i + 1] = Final[1] + y;
		}
	}
	
	public void updateBounds() {
		float left = points[0], right = points[0], top = points[1], bottom = points[1];
		for (int i = 2; i < verticies*2; i+=2) {
			if (left   < points[i]  ) { left   = points[i]; }
			if (right  > points[i]  ) { right  = points[i]; }
			if (top    < points[i+1]) { top    = points[i+1];}
			if (bottom > points[i+1]) { bottom = points[i+1];}
		}
		bounds.set((int)right, (int)bottom, (int)(left-right), (int)(top-bottom));
	}
	
	
	public static int TrianglesLength() { return (verticies-2) * 9; }
	public void updateTriangles()  {
		for (int i = 4; i < verticies*2; i += 2) {
			int itr = (i-4)/2;
			triangles[itr*9] = points[0];
			triangles[itr*9 + 1] = points[1];
			
			triangles[itr*9 + 3] = points[i];
			triangles[itr*9 + 4] = points[i+1];
			
			triangles[itr*9 + 6] = points[i-2];
			triangles[itr*9 + 7] = points[i-1];
		}
	}
	
	public void update() {
		rotation += Drotation;
		x += dx + Values.dx;
		y += dy + Values.dy;
		updatePoints();
		updateBounds();
		updateTriangles();
		if (bounds.y > 1) { done = true; }
		FramesSinceHit++;
	}
	
	public void hit() { this.FramesSinceHit = 0; }
	
	public float[] getPoints() { return points; }
	public float[] getTriangles() { return triangles; }
	
	public int size() { return verticies*2; }
	
	public void fireAt(float x, float y) {
		fireAt(x, y, (float)Math.sqrt(dx*dx + dy*dy));
	}
	
	public void fireAt(float x, float y, float speed) {
	
		dx = (x - this.x);
		dy = (y - this.y);
	
		float Speed = (float) Math.sqrt(dx*dx + dy*dy);
	
		dx /= Speed;
		dy /= Speed;
	
		dx *= speed;
		dy *= speed;
	}
	
	public void setX(float x) { this.x = x; }
	public void setY(float y) { this.y = x; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	
	public boolean isDone() { return y > 2; }
	
	public int FramesTillY(float y) {
		return (int) ((y - this.y)/dy);
	}
	
	public int FramesTillX(float y) {
		return (int) ((x - this.x)/dx);
	}

	public double getSpeed() { return Math.sqrt(dx*dx + dy*dy); }



}
