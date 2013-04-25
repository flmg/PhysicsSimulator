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
		if (new_cells[i][j] == AIR) {
			new_cells[i][j] = METAL;
			life[i][j] = 1000;
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

	public int getLife(int i, int j) {
		return life[i][j];
	}

	public void clearCell(int i, int j, int[][] new_cells) {
		new_cells[i][j] = AIR;
		life[i][j] = 0;
	}

	public void update(int x, int y, int[][] new_cells) throws SlickException {
		if (getLife(x, y) <= 0)
			clearCell(x, y, new_cells);
		else if (new_cells[x][y - 1] == WATER)
			life[x][y]--;
	}
}
