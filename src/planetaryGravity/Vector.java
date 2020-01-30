package planetaryGravity;

public class Vector {
	private double magnitude;
	private double direction;
	
	private double x;
	private double y;
	
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(double magnitude, double direction) {
		this.magnitude = magnitude;
		this.direction = direction;
		
		this.calculateComponents();
	}
	
	public void calculateComponents() {
		x = magnitude * Math.cos(direction);
		y = magnitude * Math.sin(direction);
	}
	
	public Vector addVector(Vector vector) {
		// Finds new (x and y) components
		double xC = x + vector.getX();
		double yC = y + vector.getY();
		
		double newMagnitude = Math.sqrt(xC * xC + yC * yC);
		double newDirection = getDegree(xC, yC);
		
		return(new Vector(newMagnitude, newDirection));
	}

	public Vector addValue(double value) {
		return(new Vector(magnitude + value, direction));
	}
	
	public Vector negate() {
		return(new Vector(magnitude, 360.0 - direction));
	}
	
	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
		this.calculateComponents();
	}

	public void setDirection(double direction) {
		this.direction = direction;
		this.calculateComponents();
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public double getDirection() {
		return direction;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public static double getDegree(double xC, double yC) {
		double newDirection;
		
		if (xC == 0) {
			if (yC > 0) {
				// Going up
				newDirection = Math.PI / 2;
			}
			else {
				// Going down
				newDirection = (Math.PI / 2) + Math.PI;
			}
		}
		else {
			newDirection = Math.atan(yC / xC);
			
			if (xC > 0 && yC < 0) {
				// Lower, right
				// Takes the angle (which is negative in this scenario) and convert it
				// to an actual angle by subtracting from 2pi
				newDirection = (2 * Math.PI) - Math.abs(newDirection);
			}
			else if (xC < 0 && yC > 0) {
				// Upper, left
				// Takes the complement of the angle
				newDirection = Math.PI - Math.abs(newDirection);
			}
			else if (xC > 0 && yC > 0) {
				// Upper, right
				// No changes needed
			}
			else if (xC < 0 && yC < 0){
				// Lower, left
				// Takes opposite of the angle
				newDirection = Math.PI + Math.abs(newDirection);
			}
		}
		
		return(newDirection);
	}
}
