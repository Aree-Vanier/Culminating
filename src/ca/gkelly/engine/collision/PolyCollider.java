package ca.gkelly.engine.collision;

import java.awt.Polygon;
import java.util.ArrayList;

import ca.gkelly.engine.util.Vertex;

public class PolyCollider extends Collider {

	public PolyCollider(Polygon p) {
		this(p.xpoints, p.ypoints, p.npoints);
	}
	
	private PolyCollider(int[] verticesX, int[] verticesY, int vertexCount) {
		double[] x = new double[vertexCount];
		double[] y = new double[vertexCount];
		for(int i = 0;i < vertexCount;i++) {
			x[i] = verticesX[i];
			y[i] = verticesY[i];
		}
		setVertices(x, y, vertexCount);
	}
	
	public PolyCollider(Vertex[] vertices) {
		setVertices(vertices);
	}
	

	public PolyCollider(ArrayList<Vertex> vertices) {
		setVertices(vertices.toArray(new Vertex[vertices.size()]));
	}
	
	public PolyCollider(double[] verticesX, double[] verticesY, int vertexCount) {
		setVertices(verticesX, verticesY, vertexCount);
	}
	
	public Polygon getPoly() {
		int[] x = new int[vertices.length];
		int[] y = new int[vertices.length];
		for(int i = 0;i < vertices.length;i++) {
			x[i] = (int) vertices[i].x;
			y[i] = (int) vertices[i].y;
		}
		return new Polygon(x, y, vertices.length);
	}

}
