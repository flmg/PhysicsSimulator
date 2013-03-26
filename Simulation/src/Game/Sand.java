package Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import Game.Fluid;

public class Sand extends Fluid {

	public Sand(GameContainer gc, Color col, int s) {
		super(gc, col, s);
	}

	@Override
	public void update(double dt) throws SlickException {
		for (int j = h - 1; j >= 0; j--) {
			for (int i = 0; i < w; i++) {
				// if there is fluid
				if (map[i][j] == 1) {
					// try to add down first
					if (!adddown(i, j)) {
						// 50% chance for it to go down right
						if (getRandomBoolean()) {
							if (!add_down_right(i, j))
								add_down_left(i, j);
						} else {
							if (!add_down_left(i, j))
								add_down_right(i, j);
						}
					}
				}
			}
		}
	}

}
