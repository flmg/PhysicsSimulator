package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Fire extends Fluid {

	public Fire(int wi, int he) {
		super(wi, he);
	}

	public void add(int i, int j, int[][] new_cells, int[][] life) {
		if (Math.random() < 0.5) {
			if (new_cells[i][j] == AIR) {
				new_cells[i][j] = FIRE;
				life[i][j] = 60;
			}
		}
		if (Math.random() < 0.5) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j + 1] == AIR) { // down left
					new_cells[i - 1][j + 1] = FIRE;
					life[i - 1][j + 1] = 50;
				}
			} else {
				if (new_cells[i + 1][j + 1] == AIR) { // down right
					new_cells[i + 1][j + 1] = FIRE;
					life[i + 1][j + 1] = 50;
				}
			}
		}
	}

	public int getLife(int i, int j, int[][] life) {
		return life[i][j];
	}

	public void clearCell(int i, int j, int[][] new_cells, int[][] life) {
		new_cells[i][j] = AIR;
		life[i][j] = 0;
	}

	public boolean swap(int i, int j, int x, int y, int[][] new_cells, int[][] life) {
		if (new_cells[x][y] == AIR) {
			int temp = new_cells[i][j];
			new_cells[i][j] = new_cells[x][y];
			new_cells[x][y] = temp;
			temp = life[i][j];
			life[i][j] = life[x][y];
			life[x][y] = temp;
			return true;
		}
		if (new_cells[x][y] == ICE) {
			clearCell(i, j, new_cells, life);
			new_cells[x][y] = WATER;
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

	public void update(int i, int j, int[][] new_cells, int[][] life) throws SlickException {
		life[i][j]--;
		if (getLife(i, j, life) <= 0)
			clearCell(i, j, new_cells, life);
		else {
			if (Math.random() < 0.25f) { // fire spread
				if (new_cells[i - 1][j] == OIL) { // left
					new_cells[i - 1][j] = FIRE;
					life[i - 1][j] = 70;
				}
				if (new_cells[i + 1][j] == OIL) { // right
					new_cells[i + 1][j] = FIRE;
					life[i + 1][j] = 70;
				}
				if (new_cells[i][j - 1] == OIL) { // up
					new_cells[i][j - 1] = FIRE;
					life[i][j - 1] = 70;
				}
				if (new_cells[i][j + 1] == OIL) { // down
					new_cells[i][j + 1] = FIRE;
					life[i][j + 1] = 70;
				}
				if (new_cells[i - 1][j + 1] == OIL) { // down left
					new_cells[i - 1][j + 1] = FIRE;
					life[i - 1][j + 1] = 70;
				}
				if (new_cells[i + 1][j + 1] == OIL) { // down right
					new_cells[i + 1][j + 1] = FIRE;
					life[i + 1][j + 1] = 70;
				}
				if (j - 1 >= 1)
					swap(i, j, i, j - 1, new_cells, life);
			}
		}
	}
}
