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
	final int ICE = 8;
	final int LAVA = 9;
	final int ROCK = 10;
	final int SMOKE = 11;
	
	public int w, h;
	
	public Fluid(int wi, int he) {
		this.w = wi;
		this.h = he;
	}

	public boolean randomBoolean(){
	    return Math.random() < 0.5;
	}
	

}
