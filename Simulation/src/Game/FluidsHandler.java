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
	final int METAL = 5;
	final int FIRE = 6;
	final int OIL = 7;
	final int ICE = 8;

	// Data structures
	public int[][] cells, new_cells;

	// Climate
	public boolean isRaining;

	// Fluids
	public Water water;
	public Sand sand;
	public Metal metal;
	public Fire fire;
	public Oil oil;
	public Ice ice;

	public enum type {
		Water, Oil, Block, Sand, Metal, Fire, Ice, Eraser
	};

	public type particuleType;

	public FluidsHandler(GameContainer gc, int s) {
		this.scale = s;
		this.isRaining = false;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		cells = new int[w + 2][h + 2];
		new_cells = new int[w + 2][h + 2];
		water = new Water(w, h);
		oil = new Oil(w, h);
		sand = new Sand(w, h);
		metal = new Metal(w, h);
		fire = new Fire(w, h);
		ice = new Ice(w, h);
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
					switch (cells[x][y]) {
					case AIR:
						break;
					case BLOCK:
						break;
					case ICE:
						break;
					case WATER:
						water.update(x, y, new_cells);
						break;
					case SAND:
						sand.update(x, y, new_cells);
						break;
					case WETSAND:
						sand.update(x, y, new_cells);
						break;
					case METAL:
						metal.update(x, y, new_cells);
						break;
					case FIRE:
						fire.update(x, y, new_cells);
						break;
					case OIL:
						oil.update(x, y, new_cells);
						break;
					default:
						break;
					}
				}
			} else {
				// from right to left
				for (int x = w; x >= 1; x--) {
					switch (cells[x][y]) {
					case AIR:
						break;
					case BLOCK:
						break;
					case ICE:
						break;
					case WATER:
						water.update(x, y, new_cells);
						break;
					case SAND:
						sand.update(x, y, new_cells);
						break;
					case WETSAND:
						sand.update(x, y, new_cells);
						break;
					case METAL:
						metal.update(x, y, new_cells);
						break;
					case FIRE:
						fire.update(x, y, new_cells);
						break;
					case OIL:
						oil.update(x, y, new_cells);
						break;
					default:
						break;
					}
				}
			}
		}
		if (isRaining)
			rain();
		// update map
		for (int y = h; y >= 1; y--) {
			for (int x = 1; x <= w; x++) {
				cells[x][y] = new_cells[x][y];
			}
		}
	}

	public void clearCell(int i, int j) {
		new_cells[i][j] = AIR;
	}

	public void makeRain() {
		isRaining = !isRaining;
	}

	public void rain() {
		for (int i = 1; i <= w; i++) {
			if (Math.random() < 0.003)
				water.addParticle(i, 1, new_cells);
		}
	}

	public int constrain(int x, int low, int high) {
		if (x < low)
			return low;
		else if (x > high)
			return high;
		else
			return x;
	}

	public void emitParticles(int i, int j, type particule) {
		// Ignore borders
		i++;
		j++;
		i = constrain(i, 1, w);
		j = constrain(j, 1, h);
		switch (particule) {
		case Water:
			water.emit(i, j, new_cells);
			break;
		case Oil:
			oil.emit(i, j, new_cells);
			break;
		case Sand:
			sand.emit(i, j, new_cells);
			break;
		case Fire:
			fire.add(i, j, new_cells);
			break;
		case Ice:
			ice.emit(i, j, new_cells);
			break;
		case Metal:
			metal.emit(i, j, new_cells);
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
			particuleType = type.Oil;
			break;
		case Oil:
			particuleType = type.Block;
			break;
		case Block:
			particuleType = type.Sand;
			break;
		case Sand:
			particuleType = type.Metal;
			break;
		case Metal:
			particuleType = type.Fire;
			break;
		case Fire:
			particuleType = type.Ice;
			break;
		case Ice:
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
				case OIL:
					Color b = new Color(0.3f, 0.1f, 0.1f, 1f);
					g.setColor(b);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case BLOCK:
					Color c = new Color(0.8f, 0.8f, 0.8f, 1f);
					g.setColor(c);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case SAND:
					g.setColor(Color.yellow);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case WETSAND:
					Color d = new Color(0.3f, 0.3f, 0f, 1f);
					g.setColor(d);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case METAL:
					Color e = new Color(
							0.5f + (0.5f - (metal.getLife(i, j) / 2000f)),
							0.5f, 0.5f, 1f);
					g.setColor(e);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case FIRE:
					Color f = new Color(0.9f, 0f, 0f, 1f);
					g.setColor(f);
					g.fillRect((i - 1) * scale, (j - 1) * scale, scale, scale);
					g.flush();
					break;
				case ICE:
					g.setColor(Color.white);
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
