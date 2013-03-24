package Game;

import org.newdawn.slick.*;

public class Particule {

	int size;
	int weight;
	int posx;
	int posy;
	int life; // en ms
	Image texture;

	public Particule(int x, int y, int s, int w, int l, Image t) {
		this.posx = x;
		this.posy = y;
		this.size = s;
		this.weight = w;
		this.life = l;
		this.texture = t;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (life >= 3000)
			this.texture.draw(posx, posy);
		else
			this.texture.draw(posx, posy,
					new Color(1f, 1f, 1f, 0.00333f * life));
	}

	public void update(GameContainer gc) throws SlickException {
		life -= 10;
		if (this.posy + 30 <= gc.getHeight())
			posy += weight;
	}

}
