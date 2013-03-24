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

	public void update(GameContainer gc, float dt) throws SlickException {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).update(gc, dt);
		}
		}
	}
