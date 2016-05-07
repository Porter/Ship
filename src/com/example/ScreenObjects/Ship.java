package com.example.ScreenObjects;

import java.util.ArrayList;

import com.example.util.FrUtils;
import com.example.util.Graphics;
import com.example.util.Line;
import com.example.util.Rect;
import com.example.util.Values;
import com.exmaple.manager.ArtifactManager;

public class Ship extends ObjectWithBounds {

	private float[] points, center, temp;
	private float rotation, scale, yOffset, x, y, dx, dy, speed, forceX, forceY;
	private final static int size = 5;
	private int ScreenWidth, ScreenHeight, r, g, b, hitX, hitY;
	private boolean dead, hit_b;
	private Line line = new Line(0, 0), line2 = new Line(0, 0), RockVector = new Line(0, 0);
	
	public final static float Idpoints[] = {
		-1, 1, // bottom left
		-1.f, -1 , //top left
		 0.f, -1.5f , // nose
		 1.f, -1.f , // top right
		 1.f, 1.f  // bottom right
};

	private void updatePoints() {
		if (dead) return;
		float[] p = new float[2];
		float[] Final = new float[2];
		//final[0] = final[1] = 0;
		for (int i = 0; i < size*2; i+=2) {
			p[0] = Idpoints[i];   p[1] = Idpoints[i+1];
			FrUtils.rotate(Final, p, center, rotation);
			FrUtils.scale(Final, Final, center, scale);
			points[i] = Final[0];
			points[i+1] = Final[1];
		}

		for (int i = 0; i < size*2; i+=2) {
			points[i]   += x;
			points[i+1] += yOffset;
		}
	}
	private void updateBounds() {
		int left, right, top, bottom;
		left = right = (int) points[0];
		top = bottom = (int) points[1];
		for (int i = 2; i < size*2; i+=2) {
			if (left   < points[i]  ) left   = (int) points[i];
			if (right  > points[i]  ) right  = (int) points[i];
			if (top    < points[i+1]) top    = (int) points[i+1];
			if (bottom > points[i+1]) bottom = (int) points[i+1];
		}
		bounds.set(right, bottom, left-right, top-bottom);
	}

	private void RockCollision(Rock rock, ArtifactManager artifacts) {}

	public Ship(int W, int H) {
		ScreenWidth = W;
		ScreenHeight = H;

		r = 255;
		g = b = 0;

		scale = (float) (ScreenWidth / 15.0);

		dead = hit_b = false;

		points = new float[size*2];
		center = new float[2];
		temp = new float[2];
		center[0] = center[1] = 0;

		for (int i = 0; i < size*2; i++) {
			if (i%2 == 0) center[0] += Idpoints[i];
			else 		  center[1] += Idpoints[i];
		}
		center[0] /= size;
		center[1] /= size;

		rotation = y = dx = dy = hitX = hitY = (int) (forceX = forceY = 0);
		x = .5f*Values.screenWidth;

		updatePoints();

		float hi = points[1], lo = points[1];
		for (int i = 0; i < size*2; i+= 2) {
			if (points[i+1] < lo) lo = points[i+1];
			if (points[i+1] > hi) hi = points[i+1];
		}
		float height = hi - lo;
		yOffset = ScreenHeight - height*.75f;

		speed = 1;

		updatePoints();
		bounds = new Rect(0, 0, 0, 0);
		updateBounds();
	}

	public void update() { }
	public void draw(Graphics gr) { }
	
	public void rotate(float amount) { setRotation(rotation + amount); }
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
		updatePoints();
	}

	public void resetHit() { hit_b = false; }

	public void FingerDown(float x) {
		
	}

	public void hit(float px, float py, Rock rock) {
		
	}
	public void die(ArtifactManager artifacts) {
		
	}

	public float[] getPoints() { return points; }
	
	public float getCenterX() { return center[0]; }
	public float getCenterY() { return center[1]; }
	
	public int getSize() { return size; }
	
	public double getSpeed() { return Math.sqrt(dx*dx + dy*dy); }
	public float getDx() { return dx; }
	public float getDy() { return dy; }

	public void CheckRockCollision(ArrayList<Rock> rocks, ArtifactManager artifacts) {
		
	}

	public boolean isDead() { return dead; }
	public boolean isHit() { return hit_b; }

	public int PredictedX(int frames) { return (int) (x + dx*frames); }
	public int PredictedY(int frames) { return (int) (y + dy*frames); }
	public int getRotaion() { return (int) rotation; }
};