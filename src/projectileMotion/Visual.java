package projectileMotion;
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

public class Visual extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private JFrame window;
	private Timer timer;
	
	private char currentKey = (char) 0;
	
	private Projectile projectile;
	
	private Node start;
	
	private int endElevation;
	
	private double launchAngle;
	private double launchVelocity;	
	
	private boolean keepGoing;

	public Visual() {		
		// Set up
		
		double degreeAngle = 80;
		
		launchAngle = Math.PI * degreeAngle / 180.0;
		launchVelocity = 40;
		start = new Node(200, 200);
		endElevation = 475;
				
		timer = new Timer(0, this);
		currentKey = (char) 0;

		window = new JFrame();
		window.setTitle("Projectile Motion Visualizer");
		window.setContentPane(this);
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
	}
	
	// Run will initialize finder and start swing timer.
	public void run() {
		
		projectile = new Projectile(endElevation, launchAngle, launchVelocity, new Node(start.getX(), start.getY()));

		System.out.println("STATUS: RUNNING");
	
		timer.start();
		
		keepGoing = true;
	}

	// Action performed triggered every time the swing timer is set off and Pathfinder's find method is called
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!keepGoing || projectile == null) return;
		
		if (!projectile.isDone()) {
			projectile.calculate();
			
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
		
		keepGoing = false;
		
		repaint();
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (projectile == null) {
			g.drawOval((int) start.getX(), (int) start.getY(), 20, 20);
			g.drawString("V: " + Math.round(launchVelocity), (int) (start.getX() - 30), (int) start.getY());
			g.drawString("A: " + Visual.toDegree(launchAngle), (int) (start.getX() + 20), (int) start.getY());
		}
		else {
			g.drawOval((int) projectile.getObject().getX(), (int) projectile.getObject().getY(), 20, 20);
		}
		
		g.drawLine(0, (int) start.getY() + 20, 220, (int) start.getY() + 20);
		g.drawLine(0, endElevation, getWidth(), endElevation);
	}
	
	public static int toDegree(double rad) {
		return( (int) Math.round(rad / Math.PI * 180 ));
	}

	// Handles all incoming mouse movements
	public void eventHandler(MouseEvent e) {
		
		if (projectile != null) {
			if (projectile.isDone()) return;
		}
		
		if (currentKey == 'e') {
			endElevation = e.getY();
		}
		else if (currentKey == 'v') {
			calculateLaunchVector(new Node(e.getX(), e.getY()));
		}
		
		repaint();
		
	}
	
	public void calculateLaunchVector(Node launchPoint) {
		
		double xDiff = launchPoint.getX() - start.getX();
		double yDiff = start.getY() - launchPoint.getY();
		
		launchAngle = Math.atan(yDiff / xDiff);
		launchVelocity = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 0));
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
	// Changes the paint type once a key bind is pressed
	public void keyPressed(KeyEvent e) {
		
		char key = e.getKeyChar();
		currentKey = key;

		// Space triggers the path finding
		if (currentKey == ' ') {
			run();
		}
		// Force quits program
		else if (currentKey == 'q') {
			System.exit(0);
		}
		else if (currentKey == 'r') {
			System.out.println("STATUS: RESETTING");
			projectile = null;
			repaint();
		}
	}

	@Override
	// Sets the paint type to walls after releasing key
	public void keyReleased(KeyEvent e) {
		currentKey = (char) 0;
	}


	// Required implement methods
	@Override public void mouseReleased(MouseEvent e) {}
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