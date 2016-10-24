/** <b>Anhui Science and Technology University</b> <br>
 * Computer Department <br>
 * 
 * @author:<b>ZHAO Jing</b>
 * @Email:<b>zj.ahstu@gmail.com</b>
 * @IM:<b>33470027</b> */
package zj.rs.tspSolver;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;
/** Contains a vector of points with x- and y-coordinate */
public class Cities {
	int			nDimensions;	// city size or tsp dimensions
	public int	iCityId[];
	public double	X[], Y[];
	public double	dZoomX, dZoomY; // for auto-adapting ploting
	public Cities(int n) {
		iCityId = new int[n];
		X = new double[n];
		Y = new double[n];
		nDimensions = n;
		dZoomX = 1;
		dZoomY = 1;
	}
	public Cities(String filename) {
		this(Tsp.parserCityNum(filename));
		genTspCity(filename);
	}
	// add another point to the vector
	public void addCity(double xn, double yn) {
		double x[] = new double[nDimensions + 1];
		double y[] = new double[nDimensions + 1];
		int i;
		for (i = 0; i < nDimensions; i++) {
			x[i] = X[i];
			y[i] = Y[i];
		}
		x[nDimensions] = xn;
		y[nDimensions] = yn;
		X = x;
		Y = y;
		nDimensions++;
	}
	/**
	 * generate random points
	 * 
	 * @param r
	 */
	public void genRandCities(Random r) {
		int i;
		for (i = 0; i < nDimensions; i++) {
			iCityId[i] = i;
			X[i] = r.nextDouble();
			Y[i] = r.nextDouble();
		}
	}
	/**
	 * generate specified points from tsplib data files
	 * 
	 * @param filename
	 */
	public void genTspCity(String filename) {
		try {
			Scanner s = new Scanner(new BufferedReader(new FileReader("tspdata\\" + filename + ".tsp")));
			String temp = "";
			do {
				temp = s.nextLine();
			} while (!temp.contains("NODE_COORD_SECTION") && !temp.contains("DISPLAY_DATA_SECTION") && !temp.contains("EDGE_WEIGHT_SECTION"));
			if (temp.contains("NODE_COORD_SECTION") || temp.contains("DISPLAY_DATA_SECTION")) {
				JOptionPane.showMessageDialog(null, "Data read success!,using tsplib data");
				for (int i = 0; i < nDimensions; i++) {
					iCityId[i] = s.nextInt();
					X[i] = s.nextDouble();
					Y[i] = s.nextDouble();
				}
				unitify(X, Y);
			}
			// else if (temp.contains("EDGE_WEIGHT_SECTION")) {
			// for (int i = 0; i < nDimensions; i++)
			// for (int j = 0; j < nDimensions; j++) {
			// s.nextDouble();
			// }
			// }
			else {
				JOptionPane.showMessageDialog(null, "DataType error!,using random data");
				Random r = new Random();
				for (int i = 0; i < nDimensions; i++) {
					iCityId[i] = i;
					X[i] = r.nextDouble();
					Y[i] = r.nextDouble();
				}
				unitify(X, Y);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * generate points in a nxm grid
	 * 
	 * @param n
	 * @param m
	 */
	public void genGridCities(final int n, final int m) {
		int i, j, k = 0;
		for (i = 0; i < n; i++) {
			for (j = 0; j < m; j++) {
				X[k] = i / (double) (n - 1);
				Y[k] = j / (double) (m - 1);
				k++;
			}
		}
	}
	public final int size() {
		return nDimensions;
	}
	/**
	 * scale function for auto_plotting
	 * 
	 * @param x
	 * @param y
	 */
	private void unitify(double[] x, double[] y) {
		double maxX = maxArr(x), maxY = maxArr(y);
		double minX = minArr(x), minY = minArr(y);
		dZoomX = (maxX - minX);
		dZoomY = (maxY - minY);
		for (int i = 0; i < nDimensions; i++) {
			x[i] -= minX;
			y[i] -= minY;
			x[i] /= dZoomX;
			y[i] /= dZoomY;
		}
	}
	private double minArr(double[] a) {
		double ans = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] < ans) ans = a[i];
		}
		return ans;
	}
	private double maxArr(double[] a) {
		double ans = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > ans) ans = a[i];
		}
		return ans;
	}
}