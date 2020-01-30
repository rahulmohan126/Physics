package planetaryGravity;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.List;

public class Visual extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private JFrame window;

	private List<Node> nodes;
	private boolean running;
	private boolean clicked;

	private Node editing;

	private Timer timer;

	private char currentKey = (char) 0;

	public Visual() {

		nodes = new ArrayList<Node>();
		running = false;

		// Set up

		window = new JFrame();
		window.setContentPane(this);
		window.setTitle("Gravitational Force Visualizer");
		window.getContentPane().setPreferredSize(new Dimension(750, 500));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		setSize(750, 500);
		setLayout(null);
		setVisible(true);

		// Listeners

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		window.addKeyListener(this);
		window.addMouseListener(this);

		timer = new Timer(0, this);
		clicked = false;
	}

	// Run will initialize finder and start swing timer.
	public void run() {

		System.out.println("STATUS: RUNNING");

		running = true;

		timer.start();
	}

	// Action performed triggered every time the swing timer is set off and Pathfinder's find method is called
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			for (int z = 0; z < Constants.timeMultiplier; z++){
				int a = 0;
				while (a < nodes.size()) {
					if (nodes.get(a).isCollided()) {
						nodes.remove(a);
					}
					else {
						a++;
					}
				}
	
				Node thisNode;
	
				int i = 0;
				while (i < nodes.size()) {
					thisNode = nodes.get(i);
	
					int j = 0;
					while (j < nodes.size()) {
						if (thisNode.equals(nodes.get(j))) {
							j++;
							continue;
						}
						else {
							thisNode.modify(nodes.get(j));
						}
						
						j++;
					}
	
					i++;
				}
			}
			repaint();
			timer.restart();
		}
		else {
			complete();
		}
	}

	// Once complete stops finding and prints status
	public void complete() {
		System.out.println("STATUS: DONE");
		timer.stop();
		repaint();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		setBackground(Color.BLACK);

		g.setColor(Color.WHITE);

		for (Node item : nodes) {
			g.fillOval((int) item.getX() - (int) item.getRadius(), (int) item.getY()  - (int) item.getRadius(), 2 * (int) item.getRadius(), 2 * (int) item.getRadius());
		}
	}

	// Handles all incoming mouse movements with corresponding keys
	public void eventHandler(MouseEvent e) {
		if (currentKey == (char) 0) {
			if (clicked) {
				Vector recent = new Vector((int) editing.getX() - e.getX(), (int) editing.getY() - e.getY());
				editing.setRadius(Math.sqrt(recent.getX() * recent.getX() + recent.getY() * recent.getY()));
				repaint();
			}
			else if (editing == null) {
				clicked = true;

				editing = new Node(e.getX(), e.getY(), 0, 0, 0, 0, 50);

				for (Node item : nodes) {
					if (item.equals(editing)) return;
				}

				nodes.add(editing);

				repaint();
			}
		}
		else {
			if (clicked) {
				Vector recent = new Vector((int) editing.getX() - e.getX(), (int) editing.getY() - e.getY());
				double x = Math.sqrt(Math.pow(recent.getX(), 2) + Math.pow(recent.getY(), 2));
				editing.setVelocity(x / 10000, Vector.getDegree(recent.getY(), recent.getX()));
				repaint();
			}
			else if (editing == null) {
				clicked = true;

				for (Node item : nodes) {
					double distance = Math.sqrt(Math.pow(item.getX() - e.getX(), 2) + Math.pow(item.getY() - e.getY(), 2));
					if (distance < item.getRadius()) {
						editing = item;
					}
				}
				repaint();
			}
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		eventHandler(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		eventHandler(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		clicked = false;
		editing = null;
	}

	@Override
	// Changes the paint type once a key bind is pressed
	public void keyPressed(KeyEvent e) {

		char key = e.getKeyChar();
		currentKey = key;

		// Space triggers the path finding
		if (currentKey == ' ') {
			if (!running) run();
			else running = false;
		}
		//else if (currentKey == 'v') {
		//	clicked = true;
		//}
		// Force quits program
		else if (currentKey == 'q') {
			System.exit(0);
		}
		// Clears the screen of all Nodes, nodes, etc.
		else if (currentKey == 'c') {
			if (nodes != null || !nodes.isEmpty()) nodes.clear();

			System.out.println("STATUS: CLEARING");

			repaint();
		}
	}

	@Override
	// Sets the paint type to zero after releasing key
	public void keyReleased(KeyEvent e) {
		currentKey = (char) 0;
	}


	// Unimplemented methods
	@Override public void keyTyped(KeyEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}

	public static void main(String[] args)
	{
		new Visual();
	}
}