package Game;

// import java.awt.event.KeyEvent; // Unused 

import Game.ParticulesHandler;
import Game.Fluid;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler();
	private Fluid water;
	private boolean old_MouseButtonDown;
	private double bt;

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

		this.water = new Fluid(gc, Color.blue, 4);
		bt = 1.0f;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input ip = gc.getInput();
		particules.update(gc, bt * (double) delta / 1000f);
		water.update(gc, delta);

		// Mouse left clicked
		if (ip.isMouseButtonDown(0) && !old_MouseButtonDown) {
			particules.addtolist(ip.getMouseX(), ip.getMouseY(), 1024, 500,
					texture);
		}
		// Mouse right clicked (add sand)
		if (ip.isMouseButtonDown(1)) {
			water.add(ip.getMouseX() / water.scale, ip.getMouseY()
					/ water.scale);
		}
		if (ip.isKeyPressed(Input.KEY_UP)) {
			if (bt < 2)
				bt += 0.1f;
		} else if (ip.isKeyPressed(Input.KEY_DOWN)) {
			if (bt > 0.1)
				bt -= 0.1f;
		} else if (ip.isKeyPressed(Input.KEY_P)) {
			if (bt > 0)
				bt = 0.0f;
			else
				bt = 1.0f;
		}
		old_MouseButtonDown = ip.isMouseButtonDown(0);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		particules.render(gc, g);
		water.render(gc, g);
		g.drawString(String.format("Speed : %.2f", bt), 10, 35);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}