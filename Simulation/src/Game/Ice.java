package Game;

import Game.Fluid;

public class Ice extends Fluid {

	public Ice(int wi, int he) {
		super(wi, he);
	}

	public void add(int i, int j, int[][] new_cells) {
		if (new_cells[i][j] == AIR) {
			new_cells[i][j] = ICE;
		}
	}

	public void emit(int i, int j, int[][] new_cells) {
		add(i, j, new_cells); // center
		add(i, j - 1, new_cells); // up
		add(i, j + 1, new_cells); // down
		add(i - 1, j, new_cells); // left
		add(i + 1, j, new_cells); // right
		add(i + 1, j - 1, new_cells); // up right
		add(i - 1, j - 1, new_cells); // up left
		add(i + 1, j + 1, new_cells); // down right
		add(i - 1, j + 1, new_cells); // down left
		if (j - 2 >= 1) { // up line
			add(i, j - 2, new_cells);
			add(i - 1, j - 2, new_cells);
			add(i + 1, j - 2, new_cells);
		}
		if (j + 2 <= h) { // down line
			add(i, j + 2, new_cells);
			add(i - 1, j + 2, new_cells);
			add(i + 1, j + 2, new_cells);
		}
		if (i + 2 <= w) { // right line
			add(i + 2, j, new_cells);
			add(i + 2, j - 1, new_cells);
			add(i + 2, j + 1, new_cells);
		}
		if (i - 2 >= 1) { // left line
			add(i - 2, j, new_cells);
			add(i - 2, j - 1, new_cells);
			add(i - 2, j + 1, new_cells);
		}
	}
}
