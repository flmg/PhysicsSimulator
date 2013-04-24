package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Oil extends Fluid {

	public Oil() {
		super();
	}

	public void add(int i, int j, int[][] new_cells) {
		if (Math.random() < 0.25) {
			if (new_cells[i][j] == AIR)
				new_cells[i][j] = OIL;
		}
		if (Math.random() < 0.25) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j + 1] == AIR) // down left
					new_cells[i - 1][j + 1] = OIL;
			} else {
				if (new_cells[i + 1][j + 1] == AIR) // down right
					new_cells[i + 1][j + 1] = OIL;
			}
		}
	}

	public boolean swap(int i, int j, int x, int y, int[][] cells,
			int[][] new_cells) {
		if (new_cells[x][y] == AIR) {
			int temp = new_cells[i][j];
			new_cells[i][j] = new_cells[x][y];
			new_cells[x][y] = temp;
			return true;
		}
		if (new_cells[x][y] == SAND) {
			return false;
		}
		if (new_cells[x][y] == WETSAND) {
			return false;
		}
		if (new_cells[x][y] == METAL) {
			return false;
		}
		return false;
	}

	public void update(int x, int y, int[][] cells, int[][] new_cells)
			throws SlickException {
		// Down
		if (swap(x, y, x, y + 1, cells, new_cells))
			return;
		// Down right/down left
		if (this.randomBoolean()) {
			// right first
			if (swap(x, y, x + 1, y + 1, cells, new_cells))
				return;
		} else {
			// left first
			if (swap(x, y, x - 1, y + 1, cells, new_cells))
				return;
		}
		// left/right
		if (this.randomBoolean()) {
			if (swap(x, y, x + 1, y, cells, new_cells))
				return;
		} else {
			if (swap(x, y, x - 1, y, cells, new_cells))
				return;
		}
	}
}
