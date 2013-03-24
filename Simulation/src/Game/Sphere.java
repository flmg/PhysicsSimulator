package Game;

import org.newdawn.slick.*;

public class Sphere {

	int size;
	int HEIGHT, WIDTH;
	float mass;
	float posX;
	float posY;
	float velX;
	float velY;
	float e;
	Image texture;

	public Sphere(float x, float y, int s, float m, Image t, int w, int h, float rest) {
		this.posX = x;
		this.posY = y;
		this.size = s / 2;
		this.mass = m;
		this.texture = t;
		this.velX = 0;
		this.velY = 0;
		this.WIDTH = w;
		this.HEIGHT = h;
		this.e = rest;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		this.texture.draw(posX, posY);
	}

	public void update(GameContainer gc, float dt) throws SlickException {

		// Gravity
		velY += 9.8 * dt * 100;

		// Movements
		distPlane(dt);
	}

	//Collision between objects and limits handler
	private void distPlane(float dt)
	{
		if (posX - Math.pow(10,-9) <= 0 && (velX < 0)) {
			reflectionV(1, 0);
		}

		if ((posY + size * 2 + Math.pow(10,-9) >= HEIGHT) && (velY > 0)) {
			reflectionV(0, -1);
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

	void reflectionV(int a, int b)
	{
		double x = ((1+e)*(a*(velX * a + velY * b))) / (1 / mass);
		double y = ((1+e)*(b*(velX * a + velY * b))) / (1 / mass);

		velX -= x * (1 / mass);
		velY -= y * (1 / mass);
	}

}
