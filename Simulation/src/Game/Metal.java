package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Metal extends Fluid {

	public Metal(int wi, int he) {
		super(wi, he);
	}

	public void add(int i, int j, int[][] new_cells, int[][] life) {
		if (new_cells[i][j] == AIR) {
			new_cells[i][j] = METAL;
			life[i][j] = 1000;
		}
	}

	public void emit(int i, int j, int[][] new_cells, int[][] life) {
		add(i, j, new_cells, life); // center
		add(i, j - 1, new_cells, life); // up
		add(i, j + 1, new_cells, life); // down
		add(i - 1, j, new_cells, life); // left
		add(i + 1, j, new_cells, life); // right
		add(i + 1, j - 1, new_cells, life); // up right
		add(i - 1, j - 1, new_cells, life); // up left
		add(i + 1, j + 1, new_cells, life); // down right
		add(i - 1, j + 1, new_cells, life); // down left
		if (j - 2 >= 1) { // up line
			add(i, j - 2, new_cells, life);
			add(i - 1, j - 2, new_cells, life);
			add(i + 1, j - 2, new_cells, life);
		}
		if (j + 2 <= h) { // down line
			add(i, j + 2, new_cells, life);
			add(i - 1, j + 2, new_cells, life);
			add(i + 1, j + 2, new_cells, life);
		}
		if (i + 2 <= w) { // right line
			add(i + 2, j, new_cells, life);
			add(i + 2, j - 1, new_cells, life);
			add(i + 2, j + 1, new_cells, life);
		}
		if (i - 2 >= 1) { // left line
			add(i - 2, j, new_cells, life);
			add(i - 2, j - 1, new_cells, life);
			add(i - 2, j + 1, new_cells, life);
		}
	}

	public int getLife(int i, int j, int[][] life) {
		return life[i][j];
	}

	public void clearCell(int i, int j, int[][] new_cells, int[][] life) {
		new_cells[i][j] = AIR;
		life[i][j] = 0;
	}

	public void update(int x, int y, int[][] new_cells, int[][] life) throws SlickException {
		if (getLife(x, y, life) <= 0)
			clearCell(x, y, new_cells, life);
		else if (new_cells[x][y - 1] == WATER)
			life[x][y]--;
	}
}
