package ca.gkelly.engine.collision;

import ca.gkelly.engine.util.Vertex;

class Edge {
	double slope;
	double b;
	Vertex v1, v2;
	boolean undefined = false;

	Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;

		if((v2.x - v1.x) != 0) {// If the line isn't vertical
			slope = ((v2.y - v1.y) / (v2.x - v1.x));
			b = v1.y - slope * (v1.x);
		} else { // If the line is vertical
			undefined = true;
		}
	}

	public Vertex getIntersect(Edge e) {
		double x, y;

		if(undefined) { // If this line is vertical, the x is it's
			x = v1.x;
			y = e.slope * x + e.b;
		} else if(e.undefined) { // If the other line is vertical
			x = e.v1.x;
			y = slope * x + b;
		} else {
			x = (e.b - b) / (slope - e.slope);
			y = slope * x + b;
		}

		// If the point exists within the span of the lines
		if(inRange(x, y) && e.inRange(x, y)) {
			return new Vertex(x,y);
		} else {
			return null;
		}

	}


	public boolean inRange(double x, double y) {
		return ((x <= v1.x && x >= v2.x) || (x >= v1.x && x <= v2.x)) && ((y <= v1.y && y >= v2.y) || (y >= v1.y && y <= v2.y));
	}
	
	public boolean inRange(Vertex v) {
		return inRange(v.x, v.y);
	}
	
}