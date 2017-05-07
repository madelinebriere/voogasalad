package util;

public class MathUtil {

	/**
	 * @param x1 x loc of coordinate 1
	 * @param x2 x loc of coordinate 2
	 * @param y1 y loc of coordinate 1
	 * @param y2 y loc of coordinate 2
	 * @return the distance between coordinate 1 and coordinate 2
	 */
	static public double distance(double x1, double x2, double y1, double y2){
		double xDifSquared = Math.pow(x2-x1, 2);
		double yDifSquared = Math.pow(y2-y1, 2);
		return Math.pow(xDifSquared + yDifSquared, 0.5);
	}
	
}
