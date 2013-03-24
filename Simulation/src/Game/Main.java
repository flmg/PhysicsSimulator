package Game;

import Game.ParticulesHandler;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private static final int DELAY = 10;
	private int elapsedTime;
	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler();
	private int rand;

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
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		elapsedTime += delta;

		if (elapsedTime >= DELAY) {
			elapsedTime = 0;
			rand = (int) (Math.random() * 100 + 1);
			Input ip = gc.getInput();
			particules.update(gc);

			// Mouse left clicked
			if (ip.isMouseButtonDown(0)) {
				Particule p = new Particule(ip.getMouseX(), ip.getMouseY(), 30,
						rand, 7000, texture);
				particules.addtolist(p);
			}

		}
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