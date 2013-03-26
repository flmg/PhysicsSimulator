package Game;

// import java.awt.event.KeyEvent; // Unused 

import Game.ParticulesHandler;
import Game.FluidsHandler;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler();
	private double bt;
	private FluidsHandler fluids;

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
		this.fluids = new FluidsHandler(gc, 4);
		bt = 1.0f;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input ip = gc.getInput();
		particules.update(bt * (double) delta / 1000f, ip.getMouseX(), ip.getMouseY());
		fluids.update(gc, delta);

		// Mouse left clicked
		if (ip.isMousePressed(0)) {
			particules.addtolist(ip.getMouseX(), ip.getMouseY(), 1024, 500,
					texture);
		}
		// Mouse right clicked (add sand)
		if (ip.isMouseButtonDown(1)) {
			fluids.addParticule(ip.getMouseX()/fluids.scale, ip.getMouseY()/fluids.scale);
		}
		// mode selection
		if (ip.isKeyPressed(Input.KEY_M)) {
			fluids.changeParticules();
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

		// Press tab to restart
		if (ip.isKeyPressed(Input.KEY_TAB)) {
			bt = 1.0f;
			particules.reset();
			fluids.clear();	
		}

		// Press ESC to quit
		if (ip.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		particules.render(gc, g);
		fluids.render(gc, g);
		g.setColor(Color.white);
		g.drawString(
				String.format("%d balls (TAB to reset session)",
						particules.count()), 10, 35);
		g.drawString(String.format("Speed : %.2f", bt), 10, 60);
		g.drawString("Particule Type : " + fluids.particuleType.toString()
				+ " (Press M to switch)", 10, 85);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}