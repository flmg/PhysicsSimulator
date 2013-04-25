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
		if (new_cells[i][j] == AIR) {
			if (j - 2 >= 1) { // first horizontal line
				add(i, j - 2, new_cells);
				add(i - 1, j - 2, new_cells);
				add(i + 1, j - 2, new_cells);
				if (i - 2 >= 1)
					add(i - 2, j - 2, new_cells);
				if (i + 2 <= w)
					add(i + 2, j - 2, new_cells);
			}
			add(i, j - 1, new_cells); // second horizontal line
			add(i + 1, j - 1, new_cells);
			add(i - 1, j - 1, new_cells);
			if (i - 2 >= 1)
				add(i - 2, j - 1, new_cells);
			if (i + 2 <= w)
				add(i + 2, j - 1, new_cells);

			int lenght;
			lenght = (int) (Math.random() * 4f);
			if (lenght > 0) { // random vertical line
				add(i, j, new_cells);
				if (lenght > 1)
					add(i, j + 1, new_cells);
				if (j + 2 <= h && lenght > 2)
					add(i, j + 2, new_cells);
			}
		}
	}
}
