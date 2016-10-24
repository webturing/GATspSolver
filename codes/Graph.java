/** <b>Anhui Science and Technology University</b> <br>
 * Computer Department <br>
 * 
 * @author:<b>ZHAO Jing</b>
 * @Email:<b>zj.ahstu@gmail.com</b>
 * @IM:<b>33470027</b> */
package zj.rs.tspSolver;
/** Contains a Matrix of distances for a graph. */
class Graph {
	protected int				nSize;							// city size;
	double						dDist[][];						// dist matrix
	private final static double	INFINITY	= Double.MAX_VALUE;
	public Graph(int nPointNum) {
		this.nSize = nPointNum;
		dDist = new double[nPointNum][nPointNum];
		int i, j;
		// initially disconnect all points
		for (i = 0; i < nPointNum; i++)
			for (j = 0; j < nPointNum; j++) {
				if (i != j) setDist(i, j, INFINITY);
				else setDist(i, j, 0);
			}
	}
	void setDist(int i, int j, double x) {
		dDist[i][j] = x;
	}
	final double getDist(int i, int j) {
		return dDist[i][j];
	}
	final int size() {
		return nSize;
	}
}