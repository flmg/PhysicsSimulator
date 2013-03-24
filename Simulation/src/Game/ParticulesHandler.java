package Game;

import java.util.LinkedList;

import Game.Particule;
import org.newdawn.slick.*;

public class ParticulesHandler {

	private LinkedList<Particule> list;

	public ParticulesHandler() {
		this.list = new LinkedList<Particule>();
	}

	public void addtolist(Particule p) {
		this.list.addLast(p);
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int i = 0; i < list.size(); i++)
			list.get(i).render(gc, g);
	}

	public void update(GameContainer gc) throws SlickException {
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).life >= 0) {
				list.get(i).update(gc);
			} else {
				list.remove(i);
				i--;
			}
	}
}