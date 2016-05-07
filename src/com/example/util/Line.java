package com.example.util;



public class Line {
	
	private double slope, c, InterX, InterY, ClosestX, ClosestY, ReflectionX, ReflectionY;
	private boolean infinitySlope;
	private Line temp;

	public Line() {
		this.slope = 0;
		this.c = 0;
		InterY = InterX = ClosestX = ClosestY = ReflectionX = ReflectionY = 0;
		infinitySlope = false;
		temp = null;
}
	public Line(double slope, double c) {
	this.slope = slope;
	this.c = c;
	InterY = InterX = ClosestX = ClosestY = ReflectionX = ReflectionY = 0;
	infinitySlope = false;
	temp = new Line();
}
	public Line(int x1, int y1, int x2, int y2) {
	reload(x1, y1, x2, y2);
	temp = new Line();
}
	public void reload(int x1, int y1, int x2, int y2) {
	if (x1 == x2 && y1 == y2) {  }
		InterY = InterX = 0;
		infinitySlope = false;

		if (y1 == y2) { slope = 0; c = y1;/* or y2 i don't care*/ return;}

		if (x1 == x2) { slope = y1 > y2 ? -1: 1; infinitySlope = true; c = x1; return; }

		slope = ((double)y1-y2)/(x1-x2);
		//y = slope*x + ?
		//? = -slope*x + y
		c = -(slope*x1) + y1;
}
	public void translate(double x, double y) {
	if (infinitySlope) { c += x; return; }
	c += y;
	c += -x*slope;
}
	public boolean hasInfinitySlope() { return infinitySlope; }
	public double getSlope() { return slope; }
	public double getC() { return c; }
	public boolean CanIntersect(Line line) {
	if (line.hasInfinitySlope() && this.hasInfinitySlope()) { return false; }
	if (line.hasInfinitySlope() || this.hasInfinitySlope()) {
		Line infinity; if (this.hasInfinitySlope()) {infinity = this;} else {infinity=line;}
		Line normal;if (this.hasInfinitySlope()) {normal = line;} else {normal=this;}
		InterY = normal.YatX(infinity.getC());
		InterX = normal.XatY(InterY);

		return true;
	}
	if (line.getSlope() == this.getSlope()) return false;

	///       y = s1x + c1;         y = s2x + c2;
	double s1 = this.getSlope(), c1 = this.getC();
	double s2 = line.getSlope(), c2 = line.getC();
	///               s1x + c1 = s2x + c2
	///              (s1-s2)x = c2 - c1
	double leftSide = s1 - s2, rightSide = c2 - c1;
	InterX = rightSide/leftSide;
	InterY = this.getSlope()*InterX + this.getC();

	return true;
}
	public void closestPoint(double px, double py)  {
	if (infinitySlope) {
		ClosestX = c;
		ClosestY = py;
		return;
	}
	if (slope*slope < .001) {
		ClosestX = px;
		ClosestY = c;
		return;
	}

	// temp is orthogonal line
	temp.setSlope(-1/slope);
	temp.setCForPoint(px, py);
	if (CanIntersect(temp)) {
		ClosestX = interX();
		ClosestY = interY();
	}
	else {
		System.out.println("This should never happen. Go to Line.cpp, and Line::closestPoint(double px, double py)");

		 //temp is an ortogonal Line to this, but for some reason the function CanIntersect returned false.
	}
}
	public double interX() { return InterX; }
	public double interY() { return InterY; }
	public double closestX() { return ClosestX; }
	public double closestY() { return ClosestY; }
	public double reflectionX() { return ReflectionX; }
	public double reflectionY() { return ReflectionY; }
	public double YatX(double x) {
		// y = slope*x + c
		return slope*x + c;
}
	public double XatY(double y) {
		// y = slope*x + c
		// (y-c)/slope = x
		//if (slope == 0) return ~0;
		return (y-c)/slope;
}
	public void reflection(Line line) {
	if (CanIntersect(line)) {
		double x = interX() + 10;
		double y = line.YatX(x);
		closestPoint(x, y);
		double cx = closestX();
		double cy = closestY();
		double vx = x - cx, vy = y - cy;

		ReflectionX = x - 2*vx;
		ReflectionY = y - 2*vy;
		}
}
	public void setSlope(double slope) { this.slope = slope; }
	public void setC(double c) { this.c = c; }
	public void setCForPoint(double px, double py) {
	if (infinitySlope) {
		c = px;
		return;
	}
	c = 0;
	translate(px, py);
}
}