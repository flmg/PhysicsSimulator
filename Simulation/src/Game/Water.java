package Game;

import org.newdawn.slick.SlickException;
import Game.Fluid;

public class Water extends Fluid {

	public Water(float MaxM, float MaxC, float MinM, float MaxS, float MinF) {
		super(MaxM, MaxC, MinM, MaxS, MinF);
	}

	public void update(int x, int y, int[][] cells, float[][] mass,
			float[][] new_mass) throws SlickException {

		float Flow = 0;
		float remaining_mass;

		// Custom push-only flow
		Flow = 0;
		remaining_mass = mass[x][y];
		if (remaining_mass <= 0)
			return;

		// The block below this one
		if ((cells[x][y + 1] != BLOCK)) {
			Flow = get_stable_state_b(remaining_mass + mass[x][y + 1])
					- mass[x][y + 1];

			if (Flow > MinFlow)
				Flow *= 0.5; // leads to smoother flow

			Flow = constrain(Flow, 0, Math.min(MaxSpeed, remaining_mass));

			new_mass[x][y] -= Flow;
			new_mass[x][y + 1] += Flow;
			remaining_mass -= Flow;
		}

		if (remaining_mass <= 0)
			return;

		// Left
		if (cells[x - 1][y] != BLOCK) {
			// Equalize the amount of water in this block and it's
			// neighbour
			Flow = (mass[x][y] - mass[x - 1][y]) / 4;

			if (Flow > MinFlow)
				Flow *= 0.5;

			Flow = constrain(Flow, 0, remaining_mass);

			new_mass[x][y] -= Flow;
			new_mass[x - 1][y] += Flow;
			remaining_mass -= Flow;
		}

		if (remaining_mass <= 0)
			return;

		// Right
		if (cells[x + 1][y] != BLOCK) {
			// Equalize the amount of water in this block and it's
			// neighbour
			Flow = (mass[x][y] - mass[x + 1][y]) / 4;

			if (Flow > MinFlow)
				Flow *= 0.5;

			Flow = constrain(Flow, 0, remaining_mass);

			new_mass[x][y] -= Flow;
			new_mass[x + 1][y] += Flow;
			remaining_mass -= Flow;
		}

		if (remaining_mass <= 0)
			return;

		// Up. Only compressed water flows upwards.
		if (cells[x][y - 1] != BLOCK) {
			Flow = remaining_mass
					- get_stable_state_b(remaining_mass + mass[x][y - 1]);

			if (Flow > MinFlow)
				Flow *= 0.5;

			Flow = constrain(Flow, 0, Math.min(MaxSpeed, remaining_mass));

			new_mass[x][y] -= Flow;
			new_mass[x][y - 1] += Flow;
			remaining_mass -= Flow;
		}
	}
}
