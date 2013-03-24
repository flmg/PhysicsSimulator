package Game;

import java.util.LinkedList;

import Game.Sphere;
import org.newdawn.slick.*;

public class ParticulesHandler {

	private LinkedList<Sphere> list;

	public ParticulesHandler() {
		this.list = new LinkedList<Sphere>();
	}

	public void addtolist(Sphere p) {
		this.list.addLast(p);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < list.size(); i++)
			list.get(i).render(gc, g);
	}

	public void update(GameContainer gc, double dt) throws SlickException {
		for (int i = 0; i < list.size(); i++) {
			// We check for collisions for all other spheres
			for (int j = i + 1; j < list.size(); j++)
				collision(list.get(i),list.get(j),dt);
				
			list.get(i).update(gc, dt);
		}
	}
	
	// Returns true if two balls are moving towards each other
	private boolean movingToBall (Sphere A, Sphere B)
	{
	  return ((B.posX - A.posX) * (A.velX - B.velX) + (B.posY - A.posY) * (A.velY - B.velY) > 0);
	}
	
	// Returns the distance between two objects
	private double distance(Sphere A, Sphere B)
	{
		double dX = Math.pow(((B.posX + B.size) - (A.posX + A.size)), 2);
		double dY = Math.pow(((B.posY + B.size) - (A.posY + A.size)), 2);
		// We don't root square because it's a very slow operation 
		// We'd rather square the other side
		return (dX + dY);
	}
	
	// Collision between spheres
	void collision(Sphere A, Sphere B, double dt)
	{
	  // if balls are moving toward each other and they are close 
	  // the 10^-9 is here to compensate the eventual rounding error
	  if (movingToBall(A, B) && distance(A,B) <= Math.pow(A.size + B.size, 2) + Math.pow(10,-9))
	    {
	      // Calculation of the resulting impulse for each ball
		  double nx = (A.posX - B.posX) / (A.size + B.size); // Normalized vector in X
	      double ny = (A.posY - B.posY) / (A.size + B.size); // Normalized vector in Y
	      double a1 = A.velX * nx + A.velY * ny; // A's impulse
	      double a2 = B.velX * nx + B.velY * ny; // B's impulse
	      double p = (a1 - a2) / (A.mass + B.mass); // Resultant impulse
	      //===================================================
	      
	      // Re-positionning if the collision has gone too far
	      // And if balls are overlapping
	      double angle = Math.atan2(B.posY - A.posY,B.posX - A.posX);
	      double tomove = B.size + A.size - Math.sqrt(distance(A, B));
	      if (tomove > Math.pow(10,-9))
	        {
	          B.posX += Math.cos(angle) * (tomove);
	          B.posY += Math.sin(angle) * (tomove);
	        }
	      //===================================================

	      A.velX -= (1+A.e) * p * nx * B.mass;
	      A.velY -= (1+A.e) *  p * ny * B.mass;

	      B.velX += (1+B.e) * p * nx * A.mass;
	      B.velY += (1+B.e) * p * ny * A.mass;
	    }
	}

}
