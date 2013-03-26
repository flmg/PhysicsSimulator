package Game;

import java.util.Random;

import org.newdawn.slick.*;

public class Fluid {

	public int w, h;
	public Color c;
	public int scale;
	public int type;

	public Fluid(GameContainer gc, Color col, int[][] m, int s, int t) {
		this.scale = s;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		this.c = col;
		this.type = t;
	}

	public boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	public void add(int x, int y, int[][] map) {
			
			map[x][y] = type;
	}

	public void free(int x, int y, int[][] map) {
		map[x][y] = 0;
	}
	// return true if success
	public boolean adddown(int i, int j, int[][] map) {
		if (j + 1 < h && map[i][j + 1] == 0) {
			add(i, j + 1, map);
			free(i, j, map);
			return true;
		}
		return false;
	}

	public boolean addright(int i, int j, int[][] map) {
		if (i + 1 < w && map[i + 1][j] == 0) {
			add(i + 1, j, map);
			free(i, j, map);
			return true;
		}
		return false;
	}

	public boolean addleft(int i, int j, int[][] map) {
		if (i - 1 >= 0 && map[i - 1][j] == 0) {
			add(i - 1, j, map);
			free(i, j, map);
			return true;
		}
		return false;
	}

	public boolean add_down_right(int i, int j, int[][] map) {
		if (i + 1 < w && j + 1 < h && map[i + 1][j + 1] == 0) {
			add(i + 1, j + 1, map);
			free(i, j, map);
			return true;
		}
		return false;
	}

	public boolean add_down_left(int i, int j, int[][] map) {
		if (i - 1 >= 0 && j + 1 < h && map[i - 1][j + 1] == 0) {
			add(i - 1, j + 1, map);
			free(i, j, map);
			return true;
		}
		return false;
	}
}
