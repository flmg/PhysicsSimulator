package Game;

import Game.ParticulesHandler;
import Game.Fluid;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler();
	private Fluid water;
	private boolean old_MouseButtonDown;

	public Main() {
		super("Physics Simulation");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		try {
			this.texture = new Image("images/ball.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.water = new Fluid(gc, Color.blue,4);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input ip = gc.getInput();
		particules.update(gc, (double)delta / 1000f);
		water.update(gc, delta);
		
		// Mouse left clicked
		if (ip.isMouseButtonDown(0) && !old_MouseButtonDown) {
			particules.addtolist(ip.getMouseX(), ip.getMouseY(), 1024, 500, texture);
		}
		// Mouse right clicked (add sand)
		if (ip.isMouseButtonDown(1)) {
			water.add(ip.getMouseX()/water.scale, ip.getMouseY()/water.scale);
		}
		old_MouseButtonDown = ip.isMouseButtonDown(0);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		particules.render(gc, g);
		water.render(gc, g);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}