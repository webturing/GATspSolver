/** <b>Anhui Science and Technology University</b> <br>
 * Computer Department <br>
 * 
 * @author:<b>ZHAO Jing</b>
 * @Email:<b>zj.ahstu@gmail.com</b>
 * @IM:<b>33470027</b> */
package zj.rs.tspSolver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;
public class Plot extends JPanel {
	static Random				r					= new Random();
	private static final long	serialVersionUID	= 1L;
	Cities						cities				= null;
	Tour						currentTour			= null;
	public CityGraph			cityGraph;
	final int col(double x) {
		return (int) (x * (this.getWidth() - 50)) + 10;
	}
	final int row(double y) {
		return (int) (y * (this.getHeight() - 50)) + 10;
	}
	/**
	 * draw the points
	 * 
	 * @param g
	 */
	void drawpoints(Graphics g) {
		if (cities == null) return;
		final int THICKNESS = 10 / (int) (Math.log(cities.size()) / Math.log(2) + 1);
		int i, c, r;
		for (i = 0; i < cities.size(); i++) {
			g.setColor(Color.black);
			c = col(cities.X[i]);
			r = row(cities.Y[i]);
			g.drawOval(c - THICKNESS, r - THICKNESS, THICKNESS * 2, THICKNESS * 2);
			g.setColor(Color.gray);
			if (cities.size() <= 100) g.drawString(new Integer(i + 1).toString(), c, r);// 必要的时候打印出城市标号
		}
	}
	void frame(Graphics g) {
		g.setColor(Color.white);
		Dimension size = size();
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(Color.yellow);
		g.drawRect(2, 2, getWidth() + 50, getHeight() + 50);
	}
	/**
	 * connect the points of the graph, following the path.
	 * 
	 * @param g
	 */
	void drawpath(Graphics g) {
		if (currentTour == null || cities == null) return;
		g.setColor(Color.green);
		int i = 0, j = currentTour.iDst[i];
		while (j != 0) {
			g.drawLine(col(cities.X[i]), row(cities.Y[i]), col(cities.X[j]), row(cities.Y[j]));
			j = currentTour.iDst[j];
			i = currentTour.iSrc[j];
		}
		g.drawLine(col(cities.X[i]), row(cities.Y[i]), col(cities.X[j]), row(cities.Y[j]));
		int p = r.nextInt(currentTour.nSize);
		g.setColor(Color.red);
		g.drawString("" + currentTour.getFitness(), col(cities.X[p]), row(cities.Y[p]));
	}
	/** paint everything */
	@Override public void paint(Graphics g) {
		frame(g);
		drawpoints(g);
		drawpath(g);
	}
	public void setCities(Cities p) {
		cities = p;
		cityGraph = new CityGraph(p);
	}
	public void setTour(Tour tour) {
		currentTour = tour;
	}
	public void clear() {
		cities = null;
		currentTour = null;
		cityGraph = null;
	}
}