package planetaryGravity;

public class Node {	
	private double x;
	private double y;

	private Vector velocity;
	private Vector acceleration;

	private double size;
	private double radius;
	
	private boolean collided;

	public Node(int x, int y, double xV, double yV, double xA, double yA, double radius) {
		this.x = x;
		this.y = y;

		this.velocity = new Vector(0, 0);
		this.acceleration = new Vector(0, 0);

		this.radius = radius;
		this.collided = false;
		
		calculateSize();
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
		calculateSize();
	}
	
	private void calculateSize() {
		size = radius * radius * Math.PI * 10;
	}
	
	public void setVelocity(double velocity, double degree) {
		this.velocity.setMagnitude(velocity);
		this.velocity.setDirection(degree);
	}
	
	public double getSize() {
		return(size);
	}
	
	public double getRadius() {
		return(radius);
	}

	public double getX() {
		return(x);
	}

	public double getY() {
		return(y);
	}
	
	public boolean isCollided() {
		return(collided);
	}

	public void tick() {
		x += velocity.getX();
		y += velocity.getY();
		
		velocity = velocity.addVector(acceleration);
	}

	public void modify(Node other) {

		double xDiff = other.getX() - x; // X goes to the right from 0 (like a Cartesian plane)
		double yDiff = other.getY() - y; // Y goes to down from 0 (not like a Cartesian plan)

		double distanceSq = Math.pow(xDiff, 2) + Math.pow(yDiff, 2);
		if (distanceSq == 0) return;
		else if (distanceSq < Math.pow(getRadius() + other.getRadius(), 2)) {
			Node.collide(this, other);
		}

		double gravitationalAcceleration = ((Constants.gravitationalConstant * other.getSize()) / distanceSq);
		
		Vector temp = new Vector(gravitationalAcceleration, Vector.getDegree(xDiff, yDiff));
		acceleration = acceleration.addVector(temp);
		
		tick();
	}

	public boolean equals(Node node) {
		if (node == null) return false;
		return (x == node.getX() && y == node.getY());
	}
	
	public void collided() {
		this.collided = true;
	}
	
	// Need to make a better collision animation
	// Also need to implement momentum after collision
	public static void collide(Node n1, Node n2) {
		if (n1.getSize() > n2.getSize()) {
			// n1 collides INTO n2
			n1.setRadius(n1.getRadius() + n2.getRadius());
			n2.collided();
		}
		else {
			// n2 collides INTO n1
			n2.setRadius(n1.getRadius() + n2.getRadius());
			n1.collided();
		}
	}
}