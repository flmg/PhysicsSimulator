package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Metal extends Fluid {

	public int[][] life;

	public Metal(int wi, int he) {
		super(wi, he);
		life = new int[w + 2][h + 2];
		for (int i = 0; i <= w + 1; i++) {
			for (int j = 0; j <= h + 1; j++)
				life[i][j] = 0;
		}
	}

	public void add(int i, int j, int[][] new_cells) {
		new_cells[i][j] = METAL;
		life[i][j] = 1000;
	}

	public int getLife(int i, int j) {
		return life[i][j];
	}

	public void clearCell(int i, int j, int[][] new_cells) {
		new_cells[i][j] = AIR;
		life[i][j] = 0;
	}

	public void update(int x, int y, int[][] cells, int[][] new_cells)
			throws SlickException {
		if (getLife(x, y) <= 0)
			clearCell(x, y, new_cells);
		else if (cells[x][y - 1] == WATER)
			life[x][y]--;
	}
}
