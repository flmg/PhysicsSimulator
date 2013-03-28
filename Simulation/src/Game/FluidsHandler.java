package Game;

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

	// Water properties
	final float MaxMass = 1.0f; // The normal, un-pressurized mass of a full
								// water cell
	final float MaxCompress = 0.02f; // How much excess water a cell can store,
										// compared to the cell above it
	final float MinMass = 0.0001f; // Ignore cells that are almost dry

	final float MaxSpeed = 1f; // max units of water moved out of one block to
								// another, per timestep

	final float MinFlow = 0.01f;

	public enum type {
		Water, Block
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

	public float constrain(float x, float low, float high) {
		if (x < low)
			return low;
		else if (x > high)
			return high;
		else
			return x;
	}

	/*
	 * Compression-based fluid dynamic simulator. uses w, h, mass, new_mass,
	 * cells, MaxMass, MaxCompression
	 * 
	 * cells can contain more than MaxMass water.
	 */
	public void update() throws SlickException {

		float Flow = 0;
		float remaining_mass;

		// Calculate and apply flow for each block
		for (int x = 1; x <= w; x++) {
			for (int y = 1; y <= h; y++) {

				// Skip inert ground cells
				if (cells[x][y] == BLOCK)
					continue;

				// Custom push-only flow
				Flow = 0;
				remaining_mass = mass[x][y];
				if (remaining_mass <= 0)
					continue;

				// The block below this one
				if ((cells[x][y + 1] != BLOCK)) {
					Flow = get_stable_state_b(remaining_mass + mass[x][y + 1])
							- mass[x][y + 1];
					if (Flow > MinFlow) {
						Flow *= 0.5; // leads to smoother flow
					}
					Flow = constrain(Flow, 0,
							Math.min(MaxSpeed, remaining_mass));

					new_mass[x][y] -= Flow;
					new_mass[x][y + 1] += Flow;
					remaining_mass -= Flow;
				}

				if (remaining_mass <= 0)
					continue;

				// Left
				if (cells[x - 1][y] != BLOCK) {
					// Equalize the amount of water in this block and it's
					// neighbour
					Flow = (mass[x][y] - mass[x - 1][y]) / 4;
					if (Flow > MinFlow) {
						Flow *= 0.5;
					}
					Flow = constrain(Flow, 0, remaining_mass);

					new_mass[x][y] -= Flow;
					new_mass[x - 1][y] += Flow;
					remaining_mass -= Flow;
				}

				if (remaining_mass <= 0)
					continue;

				// Right
				if (cells[x + 1][y] != BLOCK) {
					// Equalize the amount of water in this block and it's
					// neighbour
					Flow = (mass[x][y] - mass[x + 1][y]) / 4;
					if (Flow > MinFlow) {
						Flow *= 0.5;
					}
					Flow = constrain(Flow, 0, remaining_mass);

					new_mass[x][y] -= Flow;
					new_mass[x + 1][y] += Flow;
					remaining_mass -= Flow;
				}

				if (remaining_mass <= 0)
					continue;

				// Up. Only compressed water flows upwards.
				if (cells[x][y - 1] != BLOCK) {
					Flow = remaining_mass
							- get_stable_state_b(remaining_mass
									+ mass[x][y - 1]);
					if (Flow > MinFlow) {
						Flow *= 0.5;
					}
					Flow = constrain(Flow, 0,
							Math.min(MaxSpeed, remaining_mass));

					new_mass[x][y] -= Flow;
					new_mass[x][y - 1] += Flow;
					remaining_mass -= Flow;
				}

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
				if (mass[x][y] > MinMass) {
					cells[x][y] = WATER;
				} else {
					cells[x][y] = AIR;
				}
			}
		}
	}

	public void addParticule(int i, int j) {
		//Borders
		i++;
		j++;
		if (cells[i][j] == AIR) {
			if (this.particuleType == type.Water) {
				cells[i][j] = WATER;
				mass[i][j] = 1.0f;
				new_mass[i][j] = 1.0f;
			} else if (this.particuleType == type.Block) {
				cells[i][j] = BLOCK;
				if(i-1 >= 1 && j-1 >= 1)	//up left
					cells[i-1][j-1] = BLOCK;
				if(j-1 >= 1)				//up
					cells[i][j-1] = BLOCK;
				if(i+1 <= w && j-1 >= 1)	//up right
					cells[i+1][j-1] = BLOCK;
				if(i-1 >= 1)				//left
					cells[i-1][j] = BLOCK;
				if(i+1 <= w)				//right
					cells[i+1][j] = BLOCK;
				if(i-1 >= 1 && j+1 <= h)	//down left
					cells[i-1][j+1] = BLOCK;
				if(j+1 <= h)				//down
					cells[i][j+1] = BLOCK;
				if(i+1 <= w && j+1 <= h)	//down right
					cells[i+1][j+1] = BLOCK;

			}
		}
	}

	public void changeParticules() {
		if (particuleType == type.Water)
			particuleType = type.Block;
		else
			particuleType = type.Water;
	}

	// Take an amount of water and calculate how it should be split among two
	// vertically adjacent cells. Returns the amount of water that should be in
	// the bottom cell.
	float get_stable_state_b(float total_mass) {
		if (total_mass <= 1) {
			return 1;
		} else if (total_mass < 2 * MaxMass + MaxCompress) {
			return (MaxMass * MaxMass + total_mass * MaxCompress)
					/ (MaxMass + MaxCompress);
		} else {
			return (total_mass + MaxCompress) / 2;
		}
	}

	public void render(Graphics g) throws SlickException {
		for (int j = 1; j <= h; j++) {
			for (int i = 1; i <= w; i++) {
				// if water
				if (cells[i][j] == WATER) {
					Color c = new Color(0f,0f,1f,mass[i][j]);
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
