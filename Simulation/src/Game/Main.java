package Game;

import Game.ParticulesHandler;
import Game.FluidsHandler;
import org.newdawn.slick.*;

import java.awt.Font;

public class Main extends BasicGame {

	private Image texture;
	private ParticulesHandler particules;
	private double bt;
	private FluidsHandler fluids;
	private MaterialsPanel panel;
	private int mouseX, mouseY;
	TrueTypeFont font;
	int width, height;
	
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
		
		width = gc.getWidth();
		height = gc.getHeight();
		
		this.fluids = new FluidsHandler(gc, 2);
		this.panel = new MaterialsPanel(100, 240);
		bt = 1.0f;
		// load a default java font
		Font awtFont = new Font("Arial", Font.TRUETYPE_FONT, 18);
		font = new TrueTypeFont(awtFont, true);
		// time between updates
		gc.setMinimumLogicUpdateInterval(15);
		gc.setMaximumLogicUpdateInterval(15);
		
		particules = new ParticulesHandler(width, height);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input ip = gc.getInput();
		mouseX = ip.getMouseX();
		mouseY = ip.getMouseY();

		// if unpaused
		if (bt > 0) {
			particules.update(bt * (double) delta / 1000f, mouseX, mouseY, fluids.cells);
			fluids.update();
			// Mouse left clicked
			if (ip.isMousePressed(0)) {
				// if panel is clicked
				if (panel.isDrawn && mouseX >= 0 && mouseX <= panel.width
						&& mouseY <= 190 + panel.height && mouseY >= 190) {
					fluids.changeParticules(mouseY);
				} else {
					if (ip.isKeyDown(Input.KEY_LCONTROL))
						particules.addLine(mouseX, mouseY);
					else if (ip.isKeyDown(Input.KEY_LSHIFT))
						particules.addSquare(mouseX, mouseY);
					else
						particules.addSphere(mouseX, mouseY, width, height, texture);
				}
			}
			// right click (add particle)
			if (ip.isMouseButtonDown(1)) {
				fluids.emitParticles(mouseX / fluids.scale, mouseY
						/ fluids.scale, fluids.particuleType);
			}
			// inventory
			if (ip.isKeyPressed(Input.KEY_I)) {
				panel.isDrawn = !panel.isDrawn;
			}
			// rain
			if (ip.isKeyPressed(Input.KEY_R)) {
				fluids.makeRain();
			}
			// scale selection
			if (ip.isKeyPressed(Input.KEY_SUBTRACT)) {
				if (fluids.scale >= 2) {
					int new_scale = fluids.scale / 2;
					particules.scale = new_scale;
					fluids = new FluidsHandler(gc, new_scale);
				}
			}
			if (ip.isKeyPressed(Input.KEY_ADD)) {
				if (fluids.scale <= 4) {
					int new_scale = fluids.scale * 2;
					particules.scale = new_scale;
					fluids = new FluidsHandler(gc, new_scale);
				}
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
			fluids.clearMap();
		}
		// Press ESC to quit
		if (ip.isKeyPressed(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
		if (ip.isKeyPressed(Input.KEY_SPACE)) {
			particules.getBlocks(fluids.cells);
		}
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// particles
		g.setColor(Color.white);
		// text
		g.setColor(Color.white);
		g.drawString(
				String.format("%d ball(s) (TAB to reset)", particules.count()),
				10, 35);
		g.drawString(
				String.format("Click (balls) ; Ctrl+Click (line)",
						particules.count()), 10, 60);
		g.drawString(String.format("Speed: %.2f", bt), 10, 85);
		g.drawString("R (rain) I (inventory)", 10, 110);
		g.drawString("Scale: " + fluids.scale + " (+/-)", 10, 135);
		// materials panel
		panel.render(g, font, fluids);

		fluids.render(g);
		particules.render(gc, g, mouseX, mouseY);
		
		g.flush();
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main());
		app.setDisplayMode(1024, 500, false);
		app.start();
	}
}