package Game;

import Game.ParticulesHandler;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler();
	private int rand;
	private boolean old_MouseButtonDown;

	public Main() {
		super("Physics Simulation");
	}
	
	public double random(double x, double y)
	{
		return (Math.random() * (y-x)) + x;
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		try {
			this.texture = new Image("images/ball.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		rand = (int) (Math.random() * 100 + 1);
		Input ip = gc.getInput();
		particules.update(gc, (double)delta / 1000f);

		// Mouse left clicked
		if (ip.isMouseButtonDown(0) && !old_MouseButtonDown) {
			double s = random(0.5f,1f);
			Sphere p = new Sphere(ip.getMouseX(), ip.getMouseY(), 30,
					rand, 1024, 720, s, (float)s, texture);
			particules.addtolist(p);
		}
		old_MouseButtonDown = ip.isMouseButtonDown(0);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		particules.render(gc, g);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 720, false);
		app.start();
	}
}