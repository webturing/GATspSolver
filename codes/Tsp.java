/** <b>Anhui Science and Technology University</b> <br>
 * Computer Department <br>
 * 
 * @author:<b>ZHAO Jing</b>
 * @Email:<b>zj.ahstu@gmail.com</b>
 * @IM:<b>33470027</b> */
package zj.rs.tspSolver;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
/** Show a plane graph, a path and its length. */
/** Contains a plot of the graph and the path, and several buttons. */
public class Tsp extends JFrame implements ActionListener {
	static Map<String, Double>	Mp	= new TreeMap<String, Double>();
	static {
		try {
			Scanner s = new Scanner(new FileReader("tspdata/bestAnswer.txt"));
			while (s.hasNext()) {
				Mp.put(s.next(), Double.parseDouble(s.next()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Tsp() {
		super("Faster TspSolver using GAs by ZHAO Jing");
		lblMySolver = new JLabel("");
		lblLibMark = new JLabel("");
		plotArea = new Plot();
		final JPanel pnlDataArea = new JPanel();
		final JPanel pnlSolveArea = new JPanel();
		btnRandomize = new JButton("Random");
		btnRandomize.addActionListener(this);
		btnGridize = new JButton("Rectangle Grid");
		btnGridize.addActionListener(this);
		txtnSquare = new JTextField("", 4);
		cmbxFileChooser = new JComboBox();
		String[] filename = {"a280", "ali535", "att48", "att532", "bayg29", "bays29", "berlin52", "bier127", "brazil58", "brd14051", "brg180", "burma14", "ch130", "ch150", "d198", "d493", "d657", "d1291", "d1655", "d2103", "d15112", "d18512", "dantzig42", "dsj1000", "dsj1000", "eil51", "eil76", "eil101", "fl417", "fl1400", "fl1577", "fl3795", "fnl4461", "fri26", "gil262", "gr17", "gr21", "gr24", "gr48", "gr96", "gr120", "gr137", "gr202", "gr229", "gr431", "gr666", "hk48", "kroA100", "kroB100", "kroC100", "kroD100", "kroE100", "kroA150", "kroB150", "kroA200", "kroB200", "lin105", "lin318", "linhp318", "nrw1379", "p654", "pa561", "pcb442", "pcb1173", "pcb3038", "pla7397", "pla33810", "pla85900", "pr76", "pr107", "pr124", "pr136", "pr144", "pr152", "pr226", "pr264", "pr299", "pr439", "pr1002", "pr2392", "rat99", "rat195", "rat575", "rat783", "rd100", "rd400", "rl1304", "rl1323", "rl1889", "rl5915", "rl5934", "rl11849", "si175", "si535", "si1032", "st70", "swiss42", "ts225", "tsp225", "u159", "u574", "u724", "u1060", "u1432", "u1817", "u2152"};
		class Comp implements Comparator {
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				Integer a = Tsp.parserCityNum(s1);
				Integer b = Tsp.parserCityNum(s2);
				return a.compareTo(b);
			}
		}
		// Collections.sort(Arrays.asList(filename), new Comp());
		for (int i = 0; i < filename.length; i++)
			cmbxFileChooser.addItem(filename[i]);
		cmbxFileChooser.addActionListener(this);
		btnSolve = new JButton("Best Local");
		btnSolve.addActionListener(this);
		pnlDataArea.add(lblMySolver);
		pnlDataArea.add(btnRandomize);
		pnlDataArea.add(btnGridize);
		pnlDataArea.add(txtnSquare);
		pnlDataArea.add(cmbxFileChooser);
		pnlDataArea.add(btnSolve);
		pnlDataArea.add(lblLibMark);
		setLayout(gridbag);
		constrain(plotArea, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 1.0, 1.0);
		add(plotArea);
		constrain(pnlDataArea, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 1.0, 0.0);
		add(pnlDataArea);
		constrain(pnlSolveArea, 0, 3, 1, 1, GridBagConstraints.NONE, GridBagConstraints.CENTER, 1.0, 0.0);
		add(pnlSolveArea);
		// this.setSize(1024, 768);
		this.setSize(1280, 800);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new Tsp();
	}
	Map<String, Double>			best		= new HashMap<String, Double>();
	private static final double	INFINITY	= Double.MAX_VALUE;
	Plot						plotArea;
	JButton						btnRandomize, btnGridize, btnSolve;
	GridBagLayout				gridbag		= new GridBagLayout();
	int							nRandom		= 10, nIterator = 5, nSpecified = 10, nWidth, nHeight;
	String						strFileName;
	Random						rndSeed		= new Random();
	JTextField					txtnSquare, txtFileName;
	JLabel						lblLibMark, lblMySolver;
	JComboBox					cmbxFileChooser;
	int							nMaxGen		= 100, nGen = 50;
	double						lmin;
	void constrain(final Component c, final int gx, final int gy, final int gw, final int gh, final int fill, final int anchor, final double wx, final double wy) {
		final GridBagConstraints g = new GridBagConstraints();
		g.gridx = gx;
		g.gridy = gy;
		g.gridwidth = gw;
		g.gridheight = gh;
		g.fill = fill;
		g.anchor = anchor;
		g.weightx = wx;
		g.weighty = wy;
		gridbag.setConstraints(c, g);
	}
	public static int parserCityNum(final String filename) {
		String str = new String();
		final int len = filename.length();
		for (int i = 0; i < len; i++) {
			final char ch = filename.charAt(i);
			if (ch < '0' || ch > '9') continue;
			else str += ch;
		}
		return Integer.parseInt(str.trim());
	}
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == cmbxFileChooser) {
			strFileName = (String) cmbxFileChooser.getSelectedItem();
			nSpecified = parserCityNum(strFileName);
			txtnSquare.setText("" + nSpecified);
			final Cities p = new Cities(nSpecified);
			lblLibMark.setText("Problem " + strFileName + " has " + p.size() + " Points, answer is: " + Mp.get(strFileName));
			p.genTspCity(strFileName);
			plotArea.clear();
			plotArea.setCities(p);
			plotArea.repaint();
			lmin = INFINITY;
			return;
		}
		if (e.getSource() == btnRandomize) {
			try {
				nRandom = Integer.parseInt(txtnSquare.getText());
			} catch (final Exception ex) {}
			final Cities p = new Cities(nRandom);
			// showStatus(p.size() + " Points generated");
			p.genRandCities(rndSeed);
			plotArea.clear();
			plotArea.setCities(p);
			plotArea.repaint();
			return;
		}
		if (e.getSource() == btnGridize) {// create grid points
			try {
				nWidth = nHeight = (int) Math.sqrt(Integer.parseInt(txtnSquare.getText())) + 2;
			} catch (final Exception ex) {}
			final Cities p = new Cities(nWidth * nHeight);
			// showStatus(p.size() + " Points generated");
			p.genGridCities(nWidth, nHeight);
			plotArea.clear();
			plotArea.setCities(p);
			plotArea.repaint();
			return;
		}
		if (e.getSource() == btnSolve) {
			if (plotArea.cityGraph == null) return;
			final Tour pa = new Tour(plotArea.cityGraph);
			// pa.readBest(strFileName);//try to read tsp benchmarks ans;
			lblMySolver.setText(pa.getFitness() + "");
			lblLibMark.setText("" + Mp.get(strFileName));
			Tour pmin = null;
			lmin = INFINITY;
			// pmin=null
			int count = 0;
			do {
				pa.shuffle();
				pa.localOptimize();
				if (pa.length() < lmin) {
					pmin = (Tour) pa.clone();
					lmin = pmin.length();
					pmin.getFitness();
					plotArea.setTour(pmin);
					plotArea.paint(plotArea.getGraphics());
					count = 0;
				} else count++;
			} while (count < nIterator);
			lblMySolver.setText("Computing over!" + lmin);
			return;
		}
		return;
	}
}
