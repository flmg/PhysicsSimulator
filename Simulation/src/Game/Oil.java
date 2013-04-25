package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Oil extends Fluid {

	public Oil(int wi, int he) {
		super(wi, he);
	}

	public void emit(int i, int j, int[][] new_cells) {
		if (Math.random() < 0.25) {
			if (new_cells[i][j] == AIR) // center
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
		if (Math.random() < 0.25) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j - 1] == AIR) // up left
					new_cells[i - 1][j - 1] = OIL;
			} else {
				if (new_cells[i + 1][j - 1] == AIR) // up right
					new_cells[i + 1][j - 1] = OIL;
			}
		}
		if (j - 2 >= 1 && Math.random() < 0.25) {
			if (new_cells[i][j - 2] == AIR) // up up
				new_cells[i][j - 2] = OIL;
		}
		if (i - 2 >= 1 && Math.random() < 0.25) {
			if (new_cells[i - 2][j] == AIR) // left left
				new_cells[i - 2][j] = OIL;
		}
		if (i + 2 <= w && Math.random() < 0.25) {
			if (new_cells[i + 2][j] == AIR) // right right
				new_cells[i + 2][j] = OIL;
		}

		if (i - 3 >= 1 && Math.random() < 0.25) {
			if (new_cells[i - 3][j + 1] == AIR) // down left left left
				new_cells[i - 3][j + 1] = OIL;
		}
		if (i + 3 <= w && Math.random() < 0.25) {
			if (new_cells[i + 3][j + 1] == AIR) // down right right right
				new_cells[i + 3][j + 1] = OIL;
		}
	}

	public boolean swap(int i, int j, int x, int y, int[][] new_cells) {
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

	public void update(int x, int y, int[][] new_cells) throws SlickException {
		// some randomness in direction
		if (Math.random() < 0.01f) {
			// Down right/down left
			if (randomBoolean()) {
				if (swap(x, y, x + 1, y + 1, new_cells))
					return;
			} else {
				if (swap(x, y, x - 1, y + 1, new_cells))
					return;
			}
			// Down
			if (swap(x, y, x, y + 1, new_cells))
				return;
		} else {
			// Down
			if (swap(x, y, x, y + 1, new_cells))
				return;
			// Down right/down left
			if (randomBoolean()) {
				if (swap(x, y, x + 1, y + 1, new_cells))
					return;
			} else {
				if (swap(x, y, x - 1, y + 1, new_cells))
					return;
			}
		}
		// left/right
		if (randomBoolean()) {
			if (swap(x, y, x + 1, y, new_cells))
				return;
		} else {
			if (swap(x, y, x - 1, y, new_cells))
				return;
		}
	}
}
