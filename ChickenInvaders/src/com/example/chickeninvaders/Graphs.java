package com.example.chickeninvaders;

public class Graphs {
	/**
	 * method to return y-coordinate of half circle if y = -1 the half circle is
	 * complete it is supposed that y is always +ve
	 * 
	 * @param x
	 * @param radius
	 * @param cenX
	 * @param cenY
	 * @return
	 */
	public static int getYHalfCircle(int x, int radius, int cenX, int cenY) {
		int CradiusSqr = (int) Math.pow(radius, 2);
		double dif1 = CradiusSqr - Math.pow(x - cenX, 2);
		if (dif1 < 0)
			return -1;
		return (int) (-Math.sqrt(dif1) + cenY);
	}

	/**
	 * method to return y-coordinate of half ellipse if y = -1 the half circle
	 * is complete it is supposed that y is always +ve
	 * 
	 * @param x
	 * @param radius
	 * @param cenX
	 * @param cenY
	 * @return
	 */
	public static int getYHalfEllipse(int x, int radiusX, int radiusY,
			int cenX, int cenY) {
		int rXsq = (int) Math.pow(radiusX, 2);
		int rYsq = (int) Math.pow(radiusY, 2);
		int diff = (int) (rYsq - (rYsq / rXsq) * Math.pow((x - cenX), 2));
		if (diff < 0)
			return -1;
		return (int) (-Math.sqrt(diff) + cenY);
	}
}
