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
/**
 * A path in a graph. From[i] is the index of the point leading to i. To[i] the
 * index of the point after i. The path can optimize itself in a graph.
 */
public class Tour {
	Graph	graph;
	int		nSize;
	double	dCost;
	public int	iSrc[], iDst[];
	public Tour(Graph g) {
		nSize = g.size();
		graph = g;
		iSrc = new int[nSize];
		iDst = new int[nSize];
	}
	@Override public Object clone() {// return a clone path
		Tour p = new Tour(graph);
		p.dCost = dCost;
		System.arraycopy(iSrc, 0, p.iSrc, 0, nSize);
		System.arraycopy(iDst, 0, p.iDst, 0, nSize);
		return p;
	}
	public void shuffle() {
		int i, j, ii, jj, k;
		for (i = 0; i < nSize; i++)
			iDst[i] = -1;
		for (ii = i = 0; i < nSize - 1; i++) {
			j = (new Random()).nextInt(nSize - i);
			iDst[ii] = 0;
			for (jj = k = 0; k < j; k++) {
				jj++;
				while (iDst[jj] != -1)
					jj++;
			}
			while (iDst[jj] != -1)
				jj++;
			iDst[ii] = jj;
			iSrc[jj] = ii;
			ii = jj;
		}
		iDst[ii] = 0;
		iSrc[0] = ii;
		getFitness();
	}
	public double length() {
		return dCost;
	}
	/**
	 * // try to find another path with shorter length // using removals of
	 * points j and inserting i,j,i+1
	 * 
	 * @return
	 */
	public boolean localImprove() {
		int i, j, h;
		double d1, d2;
		double differDist[] = new double[nSize];
		for (i = 0; i < nSize; i++)
			differDist[i] = -graph.getDist(iSrc[i], i) - graph.getDist(i, iDst[i]) + graph.getDist(iSrc[i], iDst[i]);
		for (i = 0; i < nSize; i++) {
			d1 = -graph.getDist(i, iDst[i]);
			j = iDst[iDst[i]];
			while (j != i) {
				d2 = differDist[j] + graph.getDist(i, j) + graph.getDist(j, iDst[i]) + d1;
				if (d2 < -1e-10) {
					h = iSrc[j];
					iDst[h] = iDst[j];
					iSrc[iDst[j]] = h;
					h = iDst[i];
					iDst[i] = j;
					iDst[j] = h;
					iSrc[h] = j;
					iSrc[j] = i;
					dCost += d2;
					return true;
				}
				j = iDst[j];
			}
		}
		return false;
	}
	public boolean cross() {
		// improve the path locally, using replacements
		// of i,i+1 and j,j+1 with i,j and i+1,j+1
		int i, j, h, h1, hj;
		double d1, d2, d;
		for (i = 0; i < nSize; i++) {
			d1 = -graph.getDist(i, iDst[i]);
			j = iDst[iDst[i]];
			d2 = 0;
			d = 0;
			while (iDst[j] != i) {
				d += graph.getDist(j, iSrc[j]) - graph.getDist(iSrc[j], j);
				d2 = d1 + graph.getDist(i, j) + d + graph.getDist(iDst[i], iDst[j]) - graph.getDist(j, iDst[j]);
				if (d2 < -1e-10) {
					h = iDst[i];
					h1 = iDst[j];
					iDst[i] = j;
					iDst[h] = h1;
					iSrc[h1] = h;
					hj = i;
					while (j != h) {
						h1 = iSrc[j];
						iDst[j] = h1;
						iSrc[j] = hj;
						hj = j;
						j = h1;
					}
					iSrc[j] = hj;
					dCost += d2;
					return true;
				}
				j = iDst[j];
			}
		}
		return false;
	}
	public double getFitness() {
		// compute the length of the path
		dCost = 0;
		int i;
		for (i = 0; i < nSize; i++) {
			dCost += graph.getDist(i, iDst[i]);
		}
		return dCost;
	}
	void localOptimize() {
		// find a local optimum starting from this path
		do {
			while (localImprove());
		} while (cross());
	}
	void randomChange() {
		Random rx = new Random();
		int i = rx.nextInt(nSize);
		int j = rx.nextInt(nSize);
		if (rx.nextInt() % 1 == 0) {
			if (iDst[j] == i || j == i) return;
			// remove i from path
			dCost -= graph.getDist(iSrc[i], i) + graph.getDist(i, iDst[i]) + graph.getDist(j, iDst[j]);
			dCost += graph.getDist(iSrc[i], iDst[i]);
			iDst[iSrc[i]] = iDst[i];
			iSrc[iDst[i]] = iSrc[i];
			// insert i after j
			iSrc[i] = j;
			iDst[i] = iDst[j];
			iSrc[iDst[j]] = i;
			iDst[j] = i;
			dCost += graph.getDist(j, i) + graph.getDist(i, iDst[i]);
		} else {
			if (i == j) return;
			int hi = iDst[i], hj = iDst[j];
			iDst[i] = hj;
			iDst[j] = hi;
			iSrc[hi] = j;
			iSrc[hj] = i;
			dCost -= graph.getDist(i, hi) + graph.getDist(j, hj);
			dCost += graph.getDist(i, hj) + graph.getDist(j, hi);
		}
	}
	public String toString() {
		String p = "[";
		for (int i = 0; i < nSize; i++)
			p += iDst[i] + ",";
		return p + "]";
	}
	public Tour readBest(String filename) {
		try {
			Scanner s = new Scanner(new BufferedReader(new FileReader("tspdata\\" + filename + ".opt.tour")));
			while (!s.nextLine().contains("TOUR_SECTION"));
			for (int i = 0; i < nSize; i++)
				iSrc[i] = iDst[i] = Integer.parseInt(s.nextLine()) - 1;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}
