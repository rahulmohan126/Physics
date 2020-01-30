package projectileMotion;

public class Projectile {
	public int endElevation;
	
	public double velocityX;
	public double velocityY;
	
	public Node object;
	
	public boolean done;
	
	public Projectile(int endElevation, double launchAngle, double launchVelocity, Node object) {
		this.endElevation = endElevation;
		this.object = object;
		
		velocityX = launchVelocity * Math.cos((launchAngle));
		velocityY = - launchVelocity * Math.sin((launchAngle));
		
		done = false;
	}
	
	public void calculate() {
		// Calculates in ms
		
		if (Math.round(object.getY()) + 21 >= endElevation) done = true;
		
		int time = 100;
		
		object.setX((object.getX() + (velocityX / time)));
		object.setY((object.getY() + (velocityY / time)));
		
		velocityY += (9.81 / time);
	}
	
	public Node getObject() {
		return(object);
	}
	
	public boolean isDone() {
		return(done);
	}
}
