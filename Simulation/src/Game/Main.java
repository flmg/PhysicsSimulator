package Game;

import Game.ParticulesHandler;
import Game.FluidsHandler;
import org.newdawn.slick.*;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules = new ParticulesHandler(1024, 500);
	private double bt;
	private FluidsHandler fluids;
	private int mouseX, mouseY;

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
		mouseX = ip.getMouseX();
		mouseY = ip.getMouseY();
		particules.update(bt * (double) delta / 1000f, mouseX, mouseY);
		fluids.update();

		// Mouse left clicked
		if (ip.isMousePressed(0)) {
			if (ip.isKeyDown(Input.KEY_LCONTROL))
				particules.addLine(mouseX, mouseY);
			else
				particules.addSphere(mouseX, mouseY, 1024, 500, texture);
		}
		// right click (add particle)
		if (ip.isMouseButtonDown(1)) {
			fluids.addParticule(mouseX / fluids.scale, mouseY / fluids.scale,
					fluids.particuleType);
		}
		// mode selection
		if (ip.isKeyPressed(Input.KEY_M)) {
			fluids.changeParticules();
		}
		// rain
		if (ip.isKeyPressed(Input.KEY_R)) {
			fluids.makeRain();
		}
		// scale selection
		if (ip.isKeyPressed(Input.KEY_SUBTRACT)) {
			if (fluids.scale >= 2) {
				int new_scale = fluids.scale / 2;
				fluids = new FluidsHandler(gc, new_scale);
			}
		}
		if (ip.isKeyPressed(Input.KEY_ADD)) {
			if (fluids.scale <= 4) {
				int new_scale = fluids.scale * 2;
				fluids = new FluidsHandler(gc, new_scale);
			}
		}
		// speed
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

		// gravitation
		if (ip.isKeyPressed(Input.KEY_G))
			particules.gravityChanger();

		// Press tab to restart
		if (ip.isKeyPressed(Input.KEY_TAB)) {
			// bt = 1.0f;
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
		particules.render(gc, g, mouseX, mouseY);
		fluids.render(g);
		g.setColor(Color.white);
		g.drawString(
				String.format("%d ball(s) (TAB to reset)", particules.count()),
				10, 35);
		g.drawString(
				String.format("Click (balls) ; Ctrl+Click (line)",
						particules.count()), 10, 60);
		g.drawString(String.format("Speed: %.2f", bt), 10, 85);
		g.drawString("Particule: " + fluids.particuleType.toString()
				+ " (M to switch)", 10, 110);
		g.drawString("Scale: " + fluids.scale + " (+ / -)", 10, 135);
		g.drawString("R (rain)", 10, 160);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}