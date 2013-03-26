package Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FluidsHandler {

	public int[][] map;
	public int w, h;
	public int scale;
	public Water water;
	public Sand sand;

	public enum type {
		Water, Sand
	};

	public type particuleType;

	public FluidsHandler(GameContainer gc, int s) {
		this.scale = s;
		w = gc.getWidth() / scale;
		h = gc.getHeight() / scale;
		map = new int[w][h];
		clear();
		this.water = new Water(gc, Color.blue, map, scale, 1);
		this.sand = new Sand(gc, Color.yellow, map, scale, 2);
		this.particuleType = type.Water;
	}

	public void clear() {
		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {
				map[i][j] = 0;
			}
		}
	}

	public void addParticule(int x, int y) {
		if (particuleType == type.Water)
			water.add(x, y, map);
		else
			sand.add(x, y, map);
	}
	
	public void changeParticules() {
		if (particuleType == type.Water)
			particuleType = type.Sand;
		else
			particuleType = type.Water;
	}

	public void update(GameContainer gc, double dt) throws SlickException {
		for (int j = h - 1; j >= 0; j--) {
			for (int i = 0; i < w; i++) {
				// if water
				if (map[i][j] == 1)
					water.update(map, i, j);
				else
					sand.update(map, i, j);
			}
		}

	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		for (int j = h - 1; j >= 0; j--) {
			for (int i = 0; i < w; i++) {
				// if water
				if (map[i][j] == 1) {
					g.setColor(Color.blue);
					g.fillRect(i * scale, j * scale, scale, scale);
					g.flush();
				} else if (map[i][j] == 2){
					g.setColor(Color.yellow);
					g.fillRect(i * scale, j * scale, scale, scale);
					g.flush();
				}
			}
		}

	}

}
