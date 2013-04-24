package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Fire extends Fluid {

	public int[][] life;
	private int w, h;

	public Fire(int wi, int he) {
		w = wi;
		h = he;
		life = new int[w + 2][h + 2];
		for (int i = 0; i <= w + 1; i++) {
			for (int j = 0; j <= h + 1; j++)
				life[i][j] = 0;
		}
	}

	public void add(int i, int j, int[][] new_cells) {
		if (Math.random() < 0.5) {
			if (new_cells[i][j] == AIR) {
				new_cells[i][j] = FIRE;
				life[i][j] = 15;
			}
		}
		if (Math.random() < 0.5) {
			if (randomBoolean()) {
				if (new_cells[i - 1][j + 1] == AIR) { // down left
					new_cells[i - 1][j + 1] = FIRE;
					life[i - 1][j + 1] = 10;
				}
			} else {
				if (new_cells[i + 1][j + 1] == AIR) { // down right
					new_cells[i + 1][j + 1] = FIRE;
					life[i + 1][j + 1] = 10;
				}
			}
		}
	}

	public int getLife(int i, int j) {
		return life[i][j];
	}

	public void clearCell(int i, int j, int[][] new_cells) {
		new_cells[i][j] = AIR;
		life[i][j] = 0;
	}

	public boolean swap(int i, int j, int x, int y, int[][] cells,
			int[][] new_cells) {
		if (new_cells[x][y] == AIR) {
			int temp = new_cells[i][j];
			new_cells[i][j] = new_cells[x][y];
			new_cells[x][y] = temp;
			temp = life[i][j];
			life[i][j] = life[x][y];
			life[x][y] = temp;
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

	public void update(int i, int j, int[][] cells, int[][] new_cells)
			throws SlickException {
		life[i][j]--;
		if (getLife(i, j) <= 0)
			clearCell(i, j, new_cells);
		else {
			if(new_cells[i-1][j] == OIL){
				new_cells[i-1][j] = FIRE;
				life[i-1][j] = 10;
			}
			if(new_cells[i+1][j] == OIL){
				new_cells[i+1][j] = FIRE;
				life[i+1][j] = 10;
			}
			if(new_cells[i-1][j+1] == OIL){
				new_cells[i-1][j+1] = FIRE;
				life[i-1][j+1] = 10;
			}
			if(new_cells[i+1][j+1] == OIL){
				new_cells[i+1][j+1] = FIRE;
				life[i+1][j+1] = 10;
			}
			if (j > 1)
				swap(i, j, i, j - 1, cells, new_cells);
		}
	}
}
