package Game;

import org.newdawn.slick.*;

public class Sphere {

	public double size;
	private int HEIGHT, WIDTH;
	double mass;
	double posX;
	double posY;
	double old_posX;
	double old_posY;
	double velX;
	double velY;
	double e;
	private double scale;
	private Image texture;
	public boolean selected;

	public Sphere(double x, double y, int w, int h, double rest, double sc, Image t) {
		this.WIDTH = w;
		this.HEIGHT = h;
		this.scale = sc;
		this.size = (96 * scale) / 2;

		if (x <= 0)
			this.posX = 0;
		else if (x >= WIDTH - (size * 2))
			this.posX = WIDTH - (size * 2);
		else 
			this.posX = x;

		if (y <= 0)
			this.posY = 0;
		else if (y >= HEIGHT - (size * 2))
			this.posY = HEIGHT - (size * 2);
		else 
			this.posY = y;

		old_posX = posX;
		old_posY = posY;

		this.mass = sc * 100;
		this.texture = t;
		this.velX = 0;
		this.velY = 0;
		this.e = rest;
		this.scale = sc;
		this.selected = false;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		this.texture.draw((float)posX, (float)posY, (float)scale);
	}

	public void update(double dt) throws SlickException {

		// Gravity
		velY += 9.8 * dt * 100;

		// Line collision
		collisionL2S();

		// Movements
		distPlane(dt);

	}
	public void update(double x, double y, double dt) throws SlickException {
		posX = x - size;
		posY = y - size;

		velX = (posX - old_posX) / (dt * mass * 0.35f);
		velY = (posY - old_posY) / (dt * mass * 0.35f);

		old_posX = posX;
		old_posY = posY;
	}

	//Collision between objects and edges
	private void distPlane(double dt)
	{
		// Left edge
		if (posX - Math.pow(10,-9) <= 0 && (velX <= 0)) {
			reflectionV(1, 0);
			posX = Math.max(posX, 0); // Repositioning
		}
		// Right edge
		else if (posX + size * 2  + Math.pow(10,-9) >= WIDTH && (velX >= 0)) {
			reflectionV(-1, 0);
			posX = Math.min(posX, WIDTH - size * 2); // Repositioning
		}

		// Bottom edge
		if ((posY + size * 2 + Math.pow(10,-9) >= HEIGHT) && (velY >= 0)) {
			reflectionV(0, -1);
			posY = Math.min(posY, HEIGHT - size * 2); // Repositioning
		}
		// Top edge
		else if (posY - Math.pow(10,-9)  <= 0 && (velY <= 0)) {
			reflectionV(0, 1);
			posY = Math.max(posY, 0); // Repositioning
		}

		posX += velX * dt;
		posY += velY * dt;

	}

	private boolean collisionL2S()
	{

		double x0 = 0;
		double y0 = 300;
		double x1 = 1024;
		double y1 = 400;

		// Translate everything so that line segment start point to (0, 0)
		double a = x1-x0; // Line segment end point horizontal coordinate
		double b = y1-y0; // Line segment end point vertical coordinate
		double c = (posX + size) - x0; // Circle center horizontal coordinate
		double d = (posY + size) - y0; // Circle center vertical coordinate

		if (Math.pow(d*a - c*b, 2) - Math.pow(size, 2)*(Math.pow(a, 2) + Math.pow(b, 2)) <= 0) {
			// Collision is possible
			double l0 = Math.sqrt(x0*x0 + y0*y0);
			double l1 = Math.sqrt(x1*x1 + y1*y1);
			
			double sY = (-(y1 - y0) / l0);
			double sX = ((x1 - x0) / l1);
			reflectionV(sY,sX);
			
			closestpointonline(x0,y0,x1,y1,posX + size,posY + size * 2);
			
			return true;
		}
		return false;
	}

	private void closestpointonline(double lx1, double ly1, double lx2, double ly2, double x0, double y0) { 
		double A1 = ly2 - ly1; 
		double B1 = lx1 - lx2; 
		double C1 = A1*lx1 + B1*ly1; 
		double C2 = -B1*x0 + A1*y0; 
		double det = A1*A1 - -B1*B1; 

		if(det != 0) 
		{ 
			double cx = (double)((A1*C1 - B1*C2)/det); 
			double cy = (double)((A1*C2 - -B1*C1)/det); 

			double dX = Math.pow(((cx + size) - (posX + size)), 2);
			double dY = Math.pow(((cy + size) - (posY + size)), 2);
			// We don't root square because it's a very slow operation 
			// We'd rather square the other side of the (in)equation
			if (dX + dY > Math.pow(size, 2))
			{
				posX = cx - size;
				posY = cy - size* 2;
			}
			
		}
	}

	// Calculates the reflection vector
	private void reflectionV(double a, double b)
	{
		double x = ((1+e)*(a*(velX * a + velY * b))) / (1 / mass);
		double y = ((1+e)*(b*(velX * a + velY * b))) / (1 / mass);

		velX -= x * (1 / mass);
		velY -= y * (1 / mass);
	}
}