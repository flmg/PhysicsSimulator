package Game;

public class Fluid {

	// Block types
	final int AIR = 0;
	final int BLOCK = 1;
	final int WATER = 2;
	final int SAND = 3;
	final int WETSAND = 4;
	final int METAL = 5;
	final int FIRE = 6;
	final int OIL = 7;

	
	public int w, h;
	
	public Fluid(int wi, int he) {
		this.w = wi;
		this.h = he;
	}

	public boolean randomBoolean(){
	    return Math.random() < 0.5;
	}
	

}
