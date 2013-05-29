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
	private boolean gravity;

	public Sphere(double x, double y, int w, int h, double rest, double sc, Image t, boolean g) {
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
		this.gravity = g;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (this.texture != null)
			this.texture.draw((float)posX, (float)posY, (float)scale);
	}
	// Normal update
	public void update(double dt, LinkedList<Line> lines, LinkedList<Rectangle> rects) throws SlickException {

		// Gravity
		if (gravity)
			velY += 9.8 * dt * 100;

		// Line collision
		for (int i = 0; i < lines.size(); i++)
			collisionL2S(lines.get(i));
		// Rectangle collision
		for (int i = 0; i < rects.size(); i++)
			collisionR2S(rects.get(i));

		checkBounds();

		// Movements
		posX += velX * dt;
		posY += velY * dt;

	}
	// Update for selected mode
	public void update(double x, double y, double dt) throws SlickException {
		posX = x - size;
		posY = y - size;

		velX = (posX - old_posX) / (dt * mass * 0.35f);
		velY = (posY - old_posY) / (dt * mass * 0.35f);

		old_posX = posX;
		old_posY = posY;
	}

	private boolean inRange(double x, double y, double range) 
	{
		double dX = Math.pow(((x) - (posX + size)), 2);
		double dY = Math.pow(((y) - (posY + size)), 2);
		// We don't root square because it's a very slow operation 
		// We'd rather square the other side of the (in)equation
		return (dX + dY <= Math.pow(range, 2));
	}

	private double dot(double uX, double uY, double vX, double vY)
	{
		return (uX * vX + uY * vY);
	}
	private boolean collisionEndpoint(float x1, float y1)
	{
		// Calculation of the resulting impulse for each ball
		final double endMass = mass;
		double collisiondist = Math.sqrt(Math.pow(x1 - (posX + size), 2) + Math.pow(y1 - (posY + size), 2));
		double n_x = (x1 - (posX + size)) / collisiondist;
		double n_y = (y1 - (posY + size)) / collisiondist;
		double p = 2 * (velX * n_x + velY * n_y) / (mass + endMass);
		double w_x = velX - p * mass * n_x - p * endMass * n_x;
		double w_y = velY - p * mass * n_y - p * endMass * n_y;
		velX = w_x;
		velY = w_y;
		return true;
	}
	private void collisionR2S(Rectangle r)
	{
		for (int i = 0; i < 4; i++)
		{
			if (collisionL2S(r.edges[i]))
				break;
		}
	}
	private boolean collisionL2S(Line l) { 
		if (!l.isBound && inRange(l.x0, l.y0, size))
			return collisionEndpoint(l.x0, l.y0);
		else if (!l.isBound && inRange(l.x1, l.y1, size))
			return collisionEndpoint(l.x1, l.y1);
		else
		{
			int t = 0;
			double vX = (l.x1 + t) - (l.x0 - t);
			double vY = l.y1 - l.y0;

			double wX = (posX + size) - (l.x0 - t);
			double wY = (posY + size) - l.y0;

			double c1 = dot(wX,wY,vX,vY);
			double c2 = dot(vX,vY,vX,vY);

			double b = c1 / c2;
			double newX = (l.x0 - t) + b * vX;
			double newY = l.y0 + b * vY;

			if (c1 > 0 && c2 > c1)
			{
				if (inRange(newX, newY, size))
				{
					if (inRange(newX, newY, size - 1))
					{
						if (l.isVertical())
						{
							if (velX > 0)
								newX -= size * 2;

							newY -= size;
						}
						else if (l.isHorizontal())
						{
							if (velY > 0)
								newY -= size * 2;

							newX -= size;
						}
						else
						{
							double s1 = l.slope(); 
							double s2 = (new Line((l.x0 - t), l.y0, (float)(posX + size), (float)(posY + size), false)).slope();

							double coefX = 1 - Math.min(1, s1); 
							double coefY = 2;

							if (s2 <= s1)
							{
								newX -= size * coefX;
								newY -= size * coefY;
							}
						}

						posX = newX;
						posY = newY;
					}

					double l0 = Math.sqrt(Math.pow((l.x1 + t) - (l.x0 - t), 2) + Math.pow(l.y1 - l.y0, 2));

					short signX = (short) Math.signum(l.y0 - l.y1);
					if (signX == 0)
						signX = 1;
					double sY = -signX * ((l.y1 - l.y0) / l0);
					double sX = signX * (((l.x1 + t) - (l.x0 - t)) / l0);


					reflectionV(sY,sX);
					return true;
				}
			}
		}
		return false;
	}

	// Calculates the reflection vector
	private void reflectionV(double a, double b)
	{
		double x = ((1+e)*(a*(velX * a + velY * b))) / (1 / mass);
		double y = ((1+e)*(b*(velX * a + velY * b))) / (1 / mass);

		velX -= x * (1 / mass);
		velY -= y * (1 / mass);
	}

	// We check if the ball is outside the bounds
	private void checkBounds()
	{
		if (posX < 0)
			posX = 2;
		if (posX + size * 2 > WIDTH)
			posX = WIDTH - 2 - size * 2;
		if (posY < 0)
			posY = 2;
		if (posY + size * 2 > HEIGHT)
			posY = HEIGHT - 2 - size * 2;
	}

	public void gravityChanger()
	{
		this.gravity = !this.gravity;
	}
}