package com.example.util;

public class Rect {
	
	public float x, y, w, h;

	public Rect() {}
	
	public Rect(float x, float y, float w, float h) {
		set(x, y, w, h);
	}
	
	public void set(Rect r) {
		set(r.x, r.y, r.w, r.h);
	}
	
	public void set(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public boolean sharesSpace(Rect r) {
		return (x + w > r.x && x < r.x + r.w && y + h > r.y && y < r.y + r.h);
	}
	public void print() {
		System.out.println( x + ", " + y + ", " + w + ", " + h);
	}

	public void draw(Graphics gr, int r, int g, int b) {
		
	}

	public boolean onScreen() {
		return !(x > Values.screenWidth || x + w < 0 || y > Values.screenHeight || y + h < 0);
	}
};
