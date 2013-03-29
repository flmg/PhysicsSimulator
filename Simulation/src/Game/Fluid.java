package Game;

public class Fluid {

	// Properties
	final float MaxMass;
	final float MaxCompress;
	final float MinMass;
	final float MaxSpeed;
	final float MinFlow;

	// Block types
	final int AIR = 0;
	final int BLOCK = 1;
	final int WATER = 2;

	public Fluid(float MaxM, float MaxC, float MinM, float MaxS, float MinF) {
		MaxMass = MaxM;
		MaxCompress = MaxC;
		MinMass = MinM;
		MaxSpeed = MaxS;
		MinFlow = MinF;
	}

	public float constrain(float x, float low, float high) {
		if (x < low)
			return low;
		else if (x > high)
			return high;
		else
			return x;
	}

	public float get_stable_state_b(float total_mass) {
		if (total_mass <= 1) {
			return 1;
		} else if (total_mass < 2 * MaxMass + MaxCompress) {
			return (MaxMass * MaxMass + total_mass * MaxCompress)
					/ (MaxMass + MaxCompress);
		} else {
			return (total_mass + MaxCompress) / 2;
		}
	}
}
