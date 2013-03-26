package Game;

import java.util.LinkedList;

import Game.Sphere;
import org.newdawn.slick.*;

public class ParticulesHandler 
{	
	private LinkedList<Sphere> list;
	private int selected;

	public ParticulesHandler() {
		this.list = new LinkedList<Sphere>();
		selected = -1;
	}

	public double random(double x, double y)
	{
		return (Math.random() * (y-x)) + x;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < list.size(); i++)
			list.get(i).render(gc, g);
	}

	public void update(double dt, double x, double y) throws SlickException {
		for (int i = 0; i < list.size(); i++) {
			// We check for collisions for all other spheres
			for (int j = i + 1; j < list.size(); j++)
				collision(list.get(i),list.get(j));
			if (list.get(i).selected)
				list.get(i).update(x, y, dt);
			else
				list.get(i).update(dt);
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
		// We'd rather square the other side of the (in)equation
		return (dX + dY);
	}
	private double distance(Sphere A, double x, double y, double size)
	{
		double dX = Math.pow(((x + size) - (A.posX + A.size)), 2);
		double dY = Math.pow(((y + size) - (A.posY + A.size)), 2);
		// We don't root square because it's a very slow operation 
		// We'd rather square the other side of the (in)equation
		return (dX + dY);
	}

	// Collision between spheres
	private void collision(Sphere A, Sphere B)
	{
		// if balls are moving toward each other and they are close 
		// the 10^-9 is here to compensate the eventual rounding error
		if (movingToBall(A, B) && distance(A,B) <= Math.pow(A.size + B.size + Math.pow(10,-9), 2))
		{ 
			// Calculation of the resulting impulse for each ball
			double nx = (A.posX - B.posX) / (A.size + B.size); // Normalized vector in X
			double ny = (A.posY - B.posY) / (A.size + B.size); // Normalized vector in Y
			double a1 = A.velX * nx + A.velY * ny; // A's impulse
			double a2 = B.velX * nx + B.velY * ny; // B's impulse
			double p = (a1 - a2) / (A.mass + B.mass); // Resultant impulse
			//===================================================

			// Repositioning if the collision has gone too far
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

	// Calculates the smallest time before a collision occurs collision 
	@SuppressWarnings("unused")
	private double[] timeToCollision() 
	{
		double[] res= { 9999, -1, -1 };
		double A, B, C, D, DISC; 

		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {

				if (movingToBall(list.get(i), list.get(j))) 
				{
					// Breaking down the very long formula for t  
					A = Math.pow(list.get(i).velX, 2) + Math.pow(list.get(i).velY, 2) - 2 * list.get(i).velX * list.get(j).velX + Math.pow(list.get(j).velX, 2) - 2 * list.get(i).velY * list.get(j).velY + Math.pow(list.get(j).velY, 2); 
					B = -list.get(i).posX * list.get(i).velX - list.get(i).posY * list.get(i).velY + list.get(i).velX * list.get(j).posX + list.get(i).velY * list.get(j).posY + list.get(i).posX * list.get(j).velX - list.get(j).posX * list.get(j).velX + list.get(i).posY * list.get(j).velY - list.get(j).posY * list.get(j).velY; 
					C = Math.pow(list.get(i).velX, 2) + Math.pow(list.get(i).velY, 2) - 2 * list.get(i).velX * list.get(j).velX + Math.pow(list.get(j).velX, 2) - 2 * list.get(i).velY * list.get(j).velY + Math.pow(list.get(j).velY, 2);
					D = Math.pow(list.get(i).posX, 2) + Math.pow(list.get(i).posY, 2) - Math.pow(list.get(i).size, 2) - 2 * list.get(i).posX * list.get(j).posX + Math.pow(list.get(j).posX, 2) - 2 * list.get(i).posY * list.get(j).posY + Math.pow(list.get(j).posY, 2) - 2 * list.get(i).size * list.get(j).size - Math.pow(list.get(j).size, 2); 
					DISC = Math.pow((-2 * B), 2) - 4 * C * D;


					if (DISC >= 0) {
						// We want the smallest time
						res[0] = Math.min(Math.min(res[0], 0.5 * (2 * B - Math.sqrt(DISC)) / A), 0.5 * (2 * B + Math.sqrt(DISC)) / A); 
					}
				}
			}

		}
		return res;
	} 


	// Basic public operation on list
	public int count() {
		return list.size(); 
	}

	public void addtolist(int x, int y, int WIDTH, int HEIGHT, Image texture) {
		if (selected == -1)
		{
			double s = random(0.25f,1f);
			double size = (96 * s) / 2;
			// Prevents from creating a sphere if it overlaps another one
			for (int i = 0; i < list.size(); i++) {
				if (distance(list.get(i), x - size, y - size, size) <= Math.pow(list.get(i).size + size, 2)) {
					list.get(i).selected = true;
					selected = i;
					return;
				}
			}
			Sphere p = new Sphere(x - size, y - size, WIDTH, HEIGHT, random(0.1f,0.9f), (float)s, texture);
			this.list.addLast(p);
		}
		else {
			list.get(selected).selected = false;
			selected = -1;
		}
	}

	public void reset() {
		this.list.clear();
	}
	// ==============================

}