package Game;

import org.newdawn.slick.*;

public class Sphere {

	double size;
	int HEIGHT, WIDTH;
	double mass;
	double posX;
	double posY;
	double velX;
	double velY;
	double e;
	float scale;
	Image texture;

	public Sphere(double x, double y, int w, int h, double rest, float sc, Image t) {
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

		this.mass = sc * 100;
		this.texture = t;
		this.velX = 0;
		this.velY = 0;
		this.e = rest;
		this.scale = sc;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		this.texture.draw((int)posX, (int)posY, scale);
	}

	public void update(GameContainer gc, double dt) throws SlickException {

		// Gravity
		velY += 9.8 * dt * 100;

		// Movements
		distPlane(dt);
	}

	//Collision between objects and edges
	private void distPlane(double dt)
	{
		if (posX - Math.pow(10,-9) <= 0 && (velX < 0)) {
			reflectionV(1, 0);
		}

		if ((posY + size * 2 + Math.pow(10,-9) >= HEIGHT) && (velY > 0)) {
			reflectionV(0, -1);
			posY = Math.min(posY, HEIGHT - size * 2);
		}

		if (posX + size * 2  + Math.pow(10,-9) >= WIDTH && (velX > 0)) {
			reflectionV(-1, 0);
		}

		if (posY - Math.pow(10,-9)  <= 0 && (velY < 0)) {
			reflectionV(0, 1);
		}

		posX += velX * dt;
		posY += velY * dt;

	}
	
	// Calculates the reflection vector
	private void reflectionV(int a, int b)
	{
		double x = ((1+e)*(a*(velX * a + velY * b))) / (1 / mass);
		double y = ((1+e)*(b*(velX * a + velY * b))) / (1 / mass);

		velX -= x * (1 / mass);
		velY -= y * (1 / mass);
	}
}
