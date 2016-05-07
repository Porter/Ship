package com.example.ScreenObjects;

import com.example.util.Graphics;

public abstract class Artifact {
	
	protected boolean isDone;
	
	public boolean isDone() { return isDone; }
	
	public abstract void update();
	public abstract void draw(Graphics g);

}
