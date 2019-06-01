package ca.gkelly.engine.collision;

import java.awt.Polygon;

import ca.gkelly.engine.util.Vector;

public class PolyCollider extends Collider {

	public PolyCollider(Polygon p) {
		this(p.xpoints, p.ypoints, p.npoints);
	}
	
	public PolyCollider(int[] verticesX, int[] verticesY, int vertexCount) {
		double[] x = new double[vertexCount];
		double[] y = new double[vertexCount];
		for(int i = 0;i < vertexCount;i++) {
			x[i] = verticesX[i];
			y[i] = verticesY[i];
		}
		setVertices(x, y, vertexCount);
	}
	
	public PolyCollider(double[] verticesX, double[] verticesY, int vertexCount) {
		setVertices(verticesX, verticesY, vertexCount);
	}
	
	public Polygon getPoly() {
		int[] x = new int[vertexCount];
		int[] y = new int[vertexCount];
		for(int i = 0;i < vertexCount;i++) {
			x[i] = (int) verticesX[i];
			y[i] = (int) verticesY[i];
		}
		return new Polygon(x, y, vertexCount);
	}

}
