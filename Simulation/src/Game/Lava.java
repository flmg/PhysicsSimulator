package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Lava extends Fluid {

	public Lava(int wi, int he) {
		super(wi, he);
	}

	public void emit(int i, int j, int[][] new_cells) {
		if (Math.random() < 0.25) {
			if (new_cells[i][j] == AIR) // center
				new_cells[i][j] = LAVA;
		}
		if (Math.random() < 0.25) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j + 1] == AIR) // down left
					new_cells[i - 1][j + 1] = LAVA;
			} else {
				if (new_cells[i + 1][j + 1] == AIR) // down right
					new_cells[i + 1][j + 1] = LAVA;
			}
		}
		if (Math.random() < 0.25) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j - 1] == AIR) // up left
					new_cells[i - 1][j - 1] = LAVA;
			} else {
				if (new_cells[i + 1][j - 1] == AIR) // up right
					new_cells[i + 1][j - 1] = LAVA;
			}
		}
		if (j - 2 >= 1 && Math.random() < 0.25) {
			if (new_cells[i][j - 2] == AIR) // up up
				new_cells[i][j - 2] = LAVA;
		}
		if (i - 2 >= 1 && Math.random() < 0.25) {
			if (new_cells[i - 2][j] == AIR) // left left
				new_cells[i - 2][j] = LAVA;
		}
		if (i + 2 <= w && Math.random() < 0.25) {
			if (new_cells[i + 2][j] == AIR) // right right
				new_cells[i + 2][j] = LAVA;
		}

		if (i - 3 >= 1 && Math.random() < 0.25) {
			if (new_cells[i - 3][j + 1] == AIR) // down left left left
				new_cells[i - 3][j + 1] = LAVA;
		}
		if (i + 3 <= w && Math.random() < 0.25) {
			if (new_cells[i + 3][j + 1] == AIR) // down right right right
				new_cells[i + 3][j + 1] = LAVA;
		}
	}

	public boolean swap(int i, int j, int x, int y, int[][] new_cells,
			int[][] life) {
		if (new_cells[x][y] == AIR) {
			int temp = new_cells[i][j];
			new_cells[i][j] = new_cells[x][y];
			new_cells[x][y] = temp;
			return true;
		}
		if (new_cells[x][y] == ICE || new_cells[x][y] == SAND
				|| new_cells[x][y] == METAL || new_cells[x][y] == WETSAND
				|| new_cells[x][y] == OIL) {
			new_cells[i][j] = AIR;
			new_cells[x][y] = FIRE;
			life[x][y] = 70;
			return true;
		}
		if (new_cells[x][y] == WATER) {
			new_cells[i][j] = AIR;
			new_cells[x][y] = ROCK;
			return true;
		}
		return false;
	}

	public void update(int x, int y, int[][] new_cells, int[][] life)
			throws SlickException {
		// some randomness in direction
		if (Math.random() < 0.01f) {
			// Down right/down left
			if (randomBoolean()) {
				if (swap(x, y, x + 1, y + 1, new_cells, life))
					return;
			} else {
				if (swap(x, y, x - 1, y + 1, new_cells, life))
					return;
			}
			// Down
			if (swap(x, y, x, y + 1, new_cells, life))
				return;
		} else {
			// Down
			if (swap(x, y, x, y + 1, new_cells, life))
				return;
			// Down right/down left
			if (randomBoolean()) {
				if (swap(x, y, x + 1, y + 1, new_cells, life))
					return;
			} else {
				if (swap(x, y, x - 1, y + 1, new_cells, life))
					return;
			}
		}
		// left/right
		if (randomBoolean()) {
			if (swap(x, y, x + 1, y, new_cells, life))
				return;
		} else {
			if (swap(x, y, x - 1, y, new_cells, life))
				return;
		}
	}
}
