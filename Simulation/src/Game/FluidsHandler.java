package Game;

import Game.Water;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FluidsHandler {
	// Map dimensions
	public int w, h;
	public int scale;

	// Block types
	final int AIR = 0;
	final int BLOCK = 1;
	final int WATER = 2;
	final int SAND = 3;
	final int WETSAND = 4;

	// Data structures
	public int[][] cells, new_cells;

	// Climate
	public boolean isRaining;

	// Fluids
	public Water water = new Water();
	public Sand sand = new Sand();

	public enum type {
		Water, Block, Eraser, Sand
	};

	public type particuleType;

	public FluidsHandler(GameContainer gc, int s) {
		this.scale = s;
		this.isRaining = false;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		cells = new int[w + 2][h + 2];
		new_cells = new int[w + 2][h + 2];
		particuleType = type.Water;
		clear();
	}

	public void clear() {
		for (int j = 1; j <= h; j++) {
			for (int i = 1; i <= w; i++) {
				new_cells[i][j] = AIR;
			}
		}
		for (int x = 0; x < w + 2; x++) {
			new_cells[x][0] = BLOCK;
			new_cells[x][h + 1] = BLOCK;
		}
		for (int y = 0; y < h + 2; y++) {
			new_cells[0][y] = BLOCK;
			new_cells[w + 1][y] = BLOCK;
		}
		for (int i = 0; i <= w + 1; i++) {
			for (int j = 0; j <= h + 1; j++) {
				cells[i][j] = new_cells[i][j];
			}
		}
	}

	public boolean randomBoolean() {
		return Math.random() < 0.5;
	}

	public void update() throws SlickException {
		// update cells
		for (int y = h; y > 0; y--) {
			if (this.randomBoolean()) {
				// from left to right
				for (int x = 1; x <= w; x++) {
					if (cells[x][y] == BLOCK || cells[x][y] == AIR)
						continue;
					else {
						if (cells[x][y] == WATER) {
							water.update(x, y, cells, new_cells);
							continue;
						}
						if (cells[x][y] == SAND || cells[x][y] == WETSAND) {
							sand.update(x, y, cells, new_cells);
							continue;
						}
					}
				}
			} else {
				// from right to left
				for (int x = w; x >= 1; x--) {
					if (cells[x][y] == BLOCK || cells[x][y] == AIR)
						continue;
					else {
						if (cells[x][y] == WATER) {
							water.update(x, y, cells, new_cells);
							continue;
						}
						if (cells[x][y] == SAND || cells[x][y] == WETSAND) {
							sand.update(x, y, cells, new_cells);
							continue;
						}
					}
				}
			}
		}

		if (isRaining)
			rain();

		int count = 0;
		// update
		for (int y = h; y >= 1; y--) {
			for (int x = 1; x <= w; x++) {
				cells[x][y] = new_cells[x][y];
				if (cells[x][y] == WATER)
					count++;
			}
		}
		//System.out.print("Water cells: " + count + ".\n");
	}

	public void clearCell(int i, int j) {
		new_cells[i][j] = AIR;
	}

	public void makeRain() {
		isRaining = !isRaining;
	}

	public void rain() {
		for (int i = 1; i <= w; i++) {
			if (Math.random() < 0.005) // 1 chance over 100
				addParticule(i, 1, type.Water);
		}
	}

	public void addParticule(int i, int j, type particule) {
		// Ignore borders
		i++;
		j++;
		switch (particule) {
		case Water:
			if (cells[i][j] == AIR) {
				new_cells[i][j] = WATER;
			}
			break;
		case Sand:
			if (cells[i][j] == AIR) {
				new_cells[i][j] = SAND;
			}
			break;
		case Eraser:
			clearCell(i, j);
			if (i - 1 >= 1 && j - 1 >= 1) // up left
				clearCell(i - 1, j - 1);
			if (j - 1 >= 1) // up
				clearCell(i, j - 1);
			if (i + 1 <= w && j - 1 >= 1) // up right
				clearCell(i + 1, j - 1);
			if (i - 1 >= 1) // left
				clearCell(i - 1, j);
			if (i + 1 <= w) // right
				clearCell(i + 1, j);
			if (i - 1 >= 1 && j + 1 <= h) // down left
				clearCell(i - 1, j + 1);
			if (j + 1 <= h) // down
				clearCell(i, j + 1);
			if (i + 1 <= w && j + 1 <= h) // down right
				clearCell(i + 1, j + 1);
			break;
		case Block:
			new_cells[i][j] = BLOCK;
			if (i - 1 >= 1 && j - 1 >= 1) // up left
				new_cells[i - 1][j - 1] = BLOCK;
			if (j - 1 >= 1) // up
				new_cells[i][j - 1] = BLOCK;
			if (i + 1 <= w && j - 1 >= 1) // up right
				new_cells[i + 1][j - 1] = BLOCK;
			if (i - 1 >= 1) // left
				new_cells[i - 1][j] = BLOCK;
			if (i + 1 <= w) // right
				new_cells[i + 1][j] = BLOCK;
			if (i - 1 >= 1 && j + 1 <= h) // down left
				new_cells[i - 1][j + 1] = BLOCK;
			if (j + 1 <= h) // down
				new_cells[i][j + 1] = BLOCK;
			if (i + 1 <= w && j + 1 <= h) // down right
				new_cells[i + 1][j + 1] = BLOCK;
			break;
		default:
			break;
		}
	}

	public void changeParticules() {
		switch (particuleType) {
		case Water:
			particuleType = type.Block;
			break;
		case Block:
			particuleType = type.Sand;
			break;
		case Sand:
			particuleType = type.Eraser;
			break;
		case Eraser:
			particuleType = type.Water;
			break;
		default:
			break;
		}
	}

	public void render(Graphics g) throws SlickException {
		for (int j = 1; j <= h; j++) {
			for (int i = 1; i <= w; i++) {
				switch (cells[i][j]) {
				case WATER:
					g.setColor(Color.blue);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case BLOCK:
					g.setColor(Color.gray);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case SAND:
					g.setColor(Color.yellow);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case WETSAND:
					Color c = new Color(0.3f, 0.3f, 0f, 1f);
					g.setColor(c);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				default:
					break;
				}
			}
		}
	}
}
