package Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import Game.Fluid;

public class Sand extends Fluid {

	public Sand(GameContainer gc, Color col, int[][] map, int s, int t) {
		super(gc, col, map, s, t);
	}

	public void update(int[][] map, int i, int j) throws SlickException {
		// if there is fluid
		if (map[i][j] == 1 || map[i][j] == 2) {
			// try to add down first
			if (!adddown(i, j, map)) {
				// 50% chance for it to go down right
				if (getRandomBoolean()) {
					if (!add_down_right(i, j, map))
						add_down_left(i, j, map);
				} else {
					if (!add_down_left(i, j, map))
						add_down_right(i, j, map);
				}
			}
		}
	}
}
