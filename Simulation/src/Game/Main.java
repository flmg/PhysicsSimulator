package Game;

// import java.awt.event.KeyEvent; // Unused 

import Game.ParticulesHandler;
import Game.FluidsHandler;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler(1024, 500);
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
		this.fluids = new FluidsHandler(gc, 2);
		bt = 1.0f;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input ip = gc.getInput();
		particules.update(bt * (double) delta / 1000f, ip.getMouseX(), ip.getMouseY());
		fluids.update();

		// Mouse left clicked
		if (ip.isMousePressed(0)) {
			if (ip.isKeyDown(Input.KEY_LCONTROL))
				particules.addLine(ip.getMouseX(), ip.getMouseY());
			else
				particules.addSphere(ip.getMouseX(), ip.getMouseY(), 1024, 500,	texture);
		}
		// Mouse right clicked (add particule)
		if (ip.isMouseButtonDown(1)) {
			fluids.addParticule(ip.getMouseX()/fluids.scale, ip.getMouseY()/fluids.scale);
		}
		// mode selection
		if (ip.isKeyPressed(Input.KEY_M)) {
			fluids.changeParticules();
		}
		// scale selection
		if (ip.isKeyPressed(Input.KEY_SUBTRACT)) {
			if(fluids.scale >= 2) {
				int new_scale = fluids.scale / 2;						
				fluids = new FluidsHandler(gc, new_scale);
			}
		}
		if (ip.isKeyPressed(Input.KEY_ADD)) {
			if(fluids.scale <= 4) {
				int new_scale = fluids.scale * 2;						
				fluids = new FluidsHandler(gc, new_scale);
			}
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
		fluids.render(g);
		g.setColor(Color.white);
		g.drawString(
				String.format("%d balls (TAB to reset session)",
						particules.count()), 10, 35);
		g.drawString(String.format("Speed : %.2f", bt), 10, 60);
		g.drawString("Particule Type : " + fluids.particuleType.toString()
				+ " (Press M to switch)", 10, 85);
		g.drawString("scale : " + fluids.scale
				+ " (Press + and - to change)", 10, 110);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}