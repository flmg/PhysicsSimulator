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

	// Data structures
	public int[][] cells;
	public float[][] mass, new_mass;

	// Fluids
	public Water water = new Water(1.0f, 0.02f, 0.0001f, 1f, 0.01f);

	public enum type {
		Water, Block, Eraser
	};

	public type particuleType;

	public FluidsHandler(GameContainer gc, int s) {
		this.scale = s;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		cells = new int[w + 2][h + 2];
		mass = new float[w + 2][h + 2];
		new_mass = new float[w + 2][h + 2];
		particuleType = type.Water;
		clear();
	}

	public void clear() {
		for (int j = 1; j <= h; j++) {
			for (int i = 1; i <= w; i++) {
				cells[i][j] = AIR;
				mass[i][j] = 0;
				new_mass[i][j] = 0;
			}
		}
		for (int x = 0; x < w + 2; x++) {
			cells[x][0] = BLOCK;
			mass[x][0] = 0;
			new_mass[x][0] = 0;
			cells[x][h + 1] = BLOCK;
			mass[x][h + 1] = 0;
			new_mass[x][h + 1] = 0;
		}
		for (int y = 0; y < h + 2; y++) {
			cells[0][y] = BLOCK;
			mass[0][y] = 0;
			new_mass[0][y] = 0;
			cells[w + 1][y] = BLOCK;
			mass[w + 1][y] = 0;
			new_mass[w + 1][y] = 0;
		}

	}

	public void update() throws SlickException {

		// Calculate and apply flow for each block
		for (int x = 1; x <= w; x++) {
			for (int y = 1; y <= h; y++) {
				// Skip inert ground cells
				if (cells[x][y] == BLOCK)
					continue;
				else
					water.update(x, y, cells, mass, new_mass);
			}
		}
		// Copy the new mass values to the mass array
		for (int x = 1; x <= w; x++) {
			for (int y = 1; y <= h; y++) {
				mass[x][y] = new_mass[x][y];
			}
		}
		for (int x = 1; x <= w; x++) {
			for (int y = 1; y <= h; y++) {
				// Skip ground cells
				if (cells[x][y] == BLOCK)
					continue;
				// Flag/unflag water cells
				if (mass[x][y] > water.MinMass) {
					cells[x][y] = WATER;
				} else {
					cells[x][y] = AIR;
				}
			}
		}
	}

	public void clearCell(int i, int j) {
		cells[i][j] = AIR;
		mass[i][j] = 0f;
	}

	public void addParticule(int i, int j) {
		// Ignore borders
		i++;
		j++;
		switch (this.particuleType) {
		case Water:
			if (cells[i][j] == AIR) {
				cells[i][j] = WATER;
				mass[i][j] = 1.0f;
				new_mass[i][j] = 1.0f;
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
			cells[i][j] = BLOCK;
			if (i - 1 >= 1 && j - 1 >= 1) // up left
				cells[i - 1][j - 1] = BLOCK;
			if (j - 1 >= 1) // up
				cells[i][j - 1] = BLOCK;
			if (i + 1 <= w && j - 1 >= 1) // up right
				cells[i + 1][j - 1] = BLOCK;
			if (i - 1 >= 1) // left
				cells[i - 1][j] = BLOCK;
			if (i + 1 <= w) // right
				cells[i + 1][j] = BLOCK;
			if (i - 1 >= 1 && j + 1 <= h) // down left
				cells[i - 1][j + 1] = BLOCK;
			if (j + 1 <= h) // down
				cells[i][j + 1] = BLOCK;
			if (i + 1 <= w && j + 1 <= h) // down right
				cells[i + 1][j + 1] = BLOCK;
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
				// if water
				if (cells[i][j] == WATER) {
					Color c = new Color(0f, 0f, 1f, mass[i][j]);
					g.setColor(c);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
				} else if (cells[i][j] == BLOCK) {
					g.setColor(Color.yellow);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
				}
			}
		}
	}
}
