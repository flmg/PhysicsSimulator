package Game;

public class Fluid {

	// Block types
	final int AIR = 0;
	final int BLOCK = 1;
	final int WATER = 2;
	final int SAND = 3;
	final int WETSAND = 4;

	public Fluid() {
	}

	public boolean randomBoolean(){
	    return Math.random() < 0.5;
	}
	

}
