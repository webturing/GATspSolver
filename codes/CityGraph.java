/** <b>Anhui Science and Technology University</b> <br>
 * Computer Department <br>
 * 
 * @author:<b>ZHAO Jing</b>
 * @Email:<b>zj.ahstu@gmail.com</b>
 * @IM:<b>33470027</b> */
package zj.rs.tspSolver;
/** A graph, which can be initialized with points. */
public class CityGraph extends Graph {
	public CityGraph(Cities p) {
		super(p.size());
		int i, j;
		for (i = 0; i < nSize; i++)
			for (j = 0; j < nSize; j++) {
				double dx = p.X[i] - p.X[j];
				double dy = p.Y[i] - p.Y[j];
				double dist = Math.sqrt(p.dZoomX * p.dZoomX * dx * dx + p.dZoomY * p.dZoomY * dy * dy);
				setDist(i, j, dist);
			}
	}
}
