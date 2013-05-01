package Game;

import java.util.LinkedList;

import Game.Line;
import Game.Sphere;
import org.newdawn.slick.*;

public class ParticulesHandler 
{	
	private LinkedList<Sphere> spheres;
	private LinkedList<Line> lines;
	private int selected;
	private float tempX0;
	private float tempY0;
	private int w,h;
	private boolean gravity;
	public int scale;

	public ParticulesHandler(int w, int h) {
		this.spheres = new LinkedList<Sphere>();
		this.lines = new LinkedList<Line>();

		this.w = w;
		this.h = h;
		genBoundaries();

		selected = -1;
		this.tempX0 = -1;
		this.tempY0 = -1;

		this.gravity = true;
	}


	private void genBoundaries()
	{
		this.lines.add(new Line(1, 0, 1, h, true));
		this.lines.add(new Line(0, 1, w, 1, true));
		this.lines.add(new Line(w - 1, 0, w - 1, h, true));
		this.lines.add(new Line(0, h - 1, w, h - 1, true));
	}

	public double random(double x, double y)
	{
		return (Math.random() * (y-x)) + x;
	}

	public void render(GameContainer gc, Graphics g, int x, int y) throws SlickException {

		g.setColor(Color.red);
		for (int i = 0; i < spheres.size(); i++)
			spheres.get(i).render(gc, g);

		for (int i = 0; i < lines.size(); i++)
		{
			Line l = lines.get(i);
			g.drawLine(l.x0, l.y0, l.x1, l.y1);
		}
		if (tempX0 != -1)
			g.drawLine(tempX0, tempY0, x, y);
	}

	public void update(double dt, double x, double y, int cells[][]) throws SlickException {
		for (int i = 0; i < spheres.size(); i++) {
			// We check for collisions for all other spheres
			for (int j = i + 1; j < spheres.size(); j++)
				collision(spheres.get(i),spheres.get(j));
			if (spheres.get(i).selected)
				spheres.get(i).update(x, y, dt);
			else
				spheres.get(i).update(dt, lines);
		}
	}
	public void getBlocks(int cells[][])
	{
		boolean end = false;
		for (int j = 1; j <= h / scale && !end; j++) {
			for (int i = 1; i <= w / scale && !end; i++) {
				if (cells[i][j] == 1 && (cells[i][j - 1] == 0 || cells[i][j + 1] == 0 || cells[i - 1][j] == 0 || cells[i + 1][j] == 0))
				{
					int x0 = (i - 1) * scale, y0 = (j - 1)  * scale;
					// start top line
					addLine(x0, y0);
					int x1 = i, y1 = j;
					for (; (cells[i][j - 1] == 0 || cells[i][j + 1] == 0) && cells[x1][y1] == 1 && x1 <= w / scale; x1++);
					if (x1 - i > 1)
						// end top line
						addLine((x1 - 1) * scale, (y1 - 1)  * scale);
					else
					{
						tempX0 = -1;
						tempY0 = -1;
					}

					// start left side line
					addLine(x0, y0);
					int x2 = i, y2 = j;
					for (; (cells[i - 1][j] == 0 || cells[i + 1][j] == 0) && cells[x2][y2] == 1 && y2 <= h / scale; y2++);
					if (y2 - j > 1)
					{
						// end left side line
						addLine((x2 - 1) * scale, (y2 - 1)  * scale);

						//  bottom line
						addLine((x2 - 1) * scale, (y2 - 1)  * scale);
						addLine((x1 - 1) * scale, (y2 - 1)  * scale);

						// right side line
						addLine((x1 - 1) * scale, (y1 - 1)  * scale);
						addLine((x1 - 1) * scale, (y2 - 1)  * scale);
					}
					else
					{
						tempX0 = -1;
						tempY0 = -1;
					}
				}
			}
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
		double dist = distance(A,B);
		double err = Math.pow(10,-9);
		if (movingToBall(A, B) &&  dist <= Math.pow(A.size + B.size + err, 2))
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
			double tomove = B.size + A.size - Math.sqrt(dist);
			if (tomove > err)
			{
				double angle = Math.atan2(B.posY - A.posY,B.posX - A.posX);
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

		for (int i = 0; i < spheres.size(); i++) {
			for (int j = i + 1; j < spheres.size(); j++) {

				if (movingToBall(spheres.get(i), spheres.get(j))) 
				{
					// Breaking down the very long formula for t  
					A = Math.pow(spheres.get(i).velX, 2) + Math.pow(spheres.get(i).velY, 2) - 2 * spheres.get(i).velX * spheres.get(j).velX + Math.pow(spheres.get(j).velX, 2) - 2 * spheres.get(i).velY * spheres.get(j).velY + Math.pow(spheres.get(j).velY, 2); 
					B = -spheres.get(i).posX * spheres.get(i).velX - spheres.get(i).posY * spheres.get(i).velY + spheres.get(i).velX * spheres.get(j).posX + spheres.get(i).velY * spheres.get(j).posY + spheres.get(i).posX * spheres.get(j).velX - spheres.get(j).posX * spheres.get(j).velX + spheres.get(i).posY * spheres.get(j).velY - spheres.get(j).posY * spheres.get(j).velY; 
					C = Math.pow(spheres.get(i).velX, 2) + Math.pow(spheres.get(i).velY, 2) - 2 * spheres.get(i).velX * spheres.get(j).velX + Math.pow(spheres.get(j).velX, 2) - 2 * spheres.get(i).velY * spheres.get(j).velY + Math.pow(spheres.get(j).velY, 2);
					D = Math.pow(spheres.get(i).posX, 2) + Math.pow(spheres.get(i).posY, 2) - Math.pow(spheres.get(i).size, 2) - 2 * spheres.get(i).posX * spheres.get(j).posX + Math.pow(spheres.get(j).posX, 2) - 2 * spheres.get(i).posY * spheres.get(j).posY + Math.pow(spheres.get(j).posY, 2) - 2 * spheres.get(i).size * spheres.get(j).size - Math.pow(spheres.get(j).size, 2); 
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
		return spheres.size(); 
	}

	public void addSphere(int x, int y, int WIDTH, int HEIGHT, Image texture) {
		if (tempX0 == -1)
		{
			if (selected == -1)
			{
				double s = random(0.25f,0.75f);
				double size = (96 * s) / 2;
				// Prevents from creating a sphere if it overlaps another one
				for (int i = 0; i < spheres.size(); i++) {
					if (distance(spheres.get(i), x - size, y - size, size) <= Math.pow(spheres.get(i).size + size, 2)) {
						spheres.get(i).selected = true;
						selected = i;
						return;
					}
				}

				Sphere p = new Sphere(x - size, y - size, WIDTH, HEIGHT, random(0.4f,0.9f), (float)s, texture, gravity);
				this.spheres.add(p);
			}
			else {
				spheres.get(selected).selected = false;
				selected = -1;
			}
		}
		else
			addLine(x,y);
	}
	public void addLine(int x, int y)
	{
		// First step : start point
		if (tempX0 == -1)
		{
			tempX0 = x;
			tempY0 = y;
		}
		// Second step : end point
		else
		{
			//			float dx = x - tempX0; //delta x
			//			float dy = y - tempY0; //delta y
			//
			//            double linelength = Math.sqrt(dx * dx + dy * dy);
			//            dx /= linelength; 
			//            dy /= linelength;
			//            
			//            //Ok, (dx, dy) is now a unit vector pointing in the direction of the line
			//            int thickness = 25;
			//            float px = thickness / 2 * -dy; //vector with length width/2
			//            float py = thickness / 2 * dx;
			//            
			//            // top and bottom
			//            this.lines.add(new Line(tempX0 + px-py, tempY0 + py+px,  x + px+py, y + py-px, false));
			//            this.lines.add(new Line(x - px+py, y - (py+px),  tempX0 - (px+py), tempY0 - (py-px), false));
			//            // sides
			//            this.lines.add(new Line(x + px+py, y + py-px, x - px+py, y - (py+px), false));
			//            this.lines.add(new Line(tempX0 + px-py, tempY0 + py+px,  tempX0 - (px+py), tempY0 - (py-px), false));
			//

			this.lines.add(new Line(tempX0, tempY0, x, y, false));
			tempX0 = -1;
			tempY0 = -1;
		}
	}

	public void reset() {
		tempX0 = -1;
		tempY0 = -1;
		this.spheres.clear();
		this.lines.clear();
		genBoundaries();
	}
	// ==============================
	public void gravityChanger()
	{
		gravity = !gravity;
		for (int i = 0; i < spheres.size(); i++)
			spheres.get(i).gravityChanger();
	}
}