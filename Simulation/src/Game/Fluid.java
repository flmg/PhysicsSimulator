package Game;

import java.util.Random;

import org.newdawn.slick.*;

public class Fluid {

	public int[][] map;
	public int w, h;
	public Color c;
	public int scale;

	public Fluid(GameContainer gc, Color col, int s) {
		this.scale = s;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		this.c = col;
		map = new int[w][h];
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				map[i][j] = 0;
			}
		}
	}

	public boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	public void add(int x, int y) {
		map[x][y] = 1;
	}

	public void free(int x, int y) {
		map[x][y] = 0;
	}

	// return true if success
	public boolean adddown(int i, int j) {
		if (j + 1 < h && map[i][j + 1] == 0) {
			add(i, j + 1);
			free(i, j);
			return true;
		}
		return false;
	}

	public boolean addright(int i, int j) {
		if (i + 1 < w && map[i + 1][j] == 0) {
			add(i + 1, j);
			free(i, j);
			return true;
		}
		return false;
	}

	public boolean addleft(int i, int j) {
		if (i - 1 >= 0 && map[i - 1][j] == 0) {
			add(i - 1, j);
			free(i, j);
			return true;
		}
		return false;
	}

	public boolean add_down_right(int i, int j) {
		if (i + 1 < w && j + 1 < h && map[i + 1][j + 1] == 0) {
			add(i + 1, j + 1);
			free(i, j);
			return true;
		}
		return false;
	}

	public boolean add_down_left(int i, int j) {
		if (i - 1 >= 0 && j + 1 < h && map[i - 1][j + 1] == 0) {
			add(i - 1, j + 1);
			free(i, j);
			return true;
		}
		return false;
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				if (map[i][j] == 1) {
					g.setColor(c);
					g.fillRect(i * scale, j * scale, scale, scale);
					g.flush();
				}
			}
		}
	}

	public void update(GameContainer gc, double dt) throws SlickException {
		for (int j = h - 1; j >= 0; j--) {
			for (int i = 0; i < w; i++) {
				// if there is fluid
				if (map[i][j] == 1) {
					// try to add down first
					if (!adddown(i, j)) {
						// 50% chance for it to go right
						if (getRandomBoolean()) {
							if (!addright(i, j))
								addleft(i, j);
						} else {
							if (!addleft(i, j))
								addright(i, j);
						}
					}
				}
			}
		}
	}
}
