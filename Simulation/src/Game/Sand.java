package Game;

import org.newdawn.slick.SlickException;

import Game.Fluid;

public class Sand extends Fluid {

	public Sand() {
		super();
	}

	public boolean swap(int i, int j, int x, int y, int[][] cells, int[][] new_cells) {
		if (new_cells[x][y] == AIR) {
			int temp = new_cells[i][j];
			new_cells[i][j] = new_cells[x][y];
			new_cells[x][y] = temp;
			return true;
		}
		if (new_cells[x][y] == WATER) {
			new_cells[i][j] = AIR;
			new_cells[x][y] = WETSAND;
			return true;
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
	}
}
