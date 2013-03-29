package Game;

import java.util.LinkedList;

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

	public void update(double dt, LinkedList<Line> lines) throws SlickException {

		// Gravity
		velY += 9.8 * dt * 100;

		// Line collision
		for (int i = 0; i < lines.size(); i++)
			collisionL2S(lines.get(i));

		checkBounds();

		// Movements
		posX += velX * dt;
		posY += velY * dt;

	}
	public void update(double x, double y, double dt) throws SlickException {
		posX = x - size;
		posY = y - size;

		velX = (posX - old_posX) / (dt * mass * 0.35f);
		velY = (posY - old_posY) / (dt * mass * 0.35f);

		old_posX = posX;
		old_posY = posY;
	}

	private void collisionL2S(Line l)
	{
		// Translate everything so that line segment start point to (0, 0)
		double a = l.x1 - l.x0; // Line segment end point horizontal coordinate
		double b = l.y1 - l.y0; // Line segment end point vertical coordinate
		double c = (posX + size) - l.x0; // Circle center horizontal coordinate
		double d = (posY + size) - l.y0; // Circle center vertical coordinate

		// If collision is possible
		if (Math.pow(d*a - c*b, 2) - Math.pow(size, 2)*(Math.pow(a, 2) + Math.pow(b, 2)) <= 0) {
			// We check if we're between the end-points
			boolean inMiddle = 
					posX >= l.x0 && 						   
					posX + size * 2 <= l.x1 &&
					posY + size * 2 >= Math.min(l.y0,l.y1) &&
					posY <= Math.max(l.y0,l.y1);
			// We check if we're near the end-points
			boolean atStart = false;//inRange(l.x0, l.y0, size) ||  inRange(l.x1, l.y1, size);
					if (l.isBound || inMiddle || atStart) {

						double l0 = Math.sqrt(Math.pow(l.x1 - l.x0, 2) + Math.pow(l.y1 - l.y0, 2));

						short signX = (short) Math.signum(l.y0 - l.y1);
						if (signX == 0)
							signX = 1;

						double sY = -signX*((l.y1 - l.y0) / l0);
						double sX = signX*((l.x1 - l.x0) / l0);

						closestpointonline(l.x0,l.y0,l.x1,l.y1,posX + size,posY + size * 2);
						reflectionV(sY,sX);
					}

		}
	}
	
	private boolean inRange(double x, double y, double range) 
	{
		double dX = Math.pow(((x + size) - (posX + size)), 2);
		double dY = Math.pow(((y + size) - (posY + size)), 2);
		// We don't root square because it's a very slow operation 
		// We'd rather square the other side of the (in)equation
		return (dX + dY <= Math.pow(range, 2));
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
			
			if (inRange(cx, cy, size) && posY > cy)
			{
				if (A1 == 0 || B1 == 0)
				{
					if (A1 != 0) {
						if (velX > 0)
							posX = cx - size * 2;
						else
							posX = cx;
					}
					if (B1 != 0) {
						if (velY > 0)
							posY = cy - size * 2;
						else
							posY = cy;
					}
				}
				else
				{
					posX = cx - size;
					posY = cy - size * 2;
				}
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

	private void checkBounds()
	{
		if (posX < 0)
			posX = 0;
		if (posX + size * 2 > WIDTH)
			posX = WIDTH - size * 2;
		if (posY < 0)
			posY = 0;
		if (posY + size * 2 > HEIGHT)
			posY = HEIGHT - size * 2;
	}
}