package Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class MaterialsPanel {

	public MaterialsPanel() {
	}

	public void render(Graphics g, TrueTypeFont font, FluidsHandler fluids)
			throws SlickException {

		// materials backgrounds
		Color col = new Color(0.3f, 0.3f, 0.3f, 1f);
		g.setColor(col);
		g.fillRect(0, 190, 100, 240);
		col = new Color(0.1f, 0.1f, 0.1f, 1f);
		g.setColor(col);
		g.fillRect(2, 192, 96, 236);

		col = new Color(0.5f, 0.5f, 0.5f, 0.7f);
		g.setColor(col);
		int offset = fluids.particuleType.ordinal();
		g.fillRect(5, 200 + 25 * offset, 90, 20); // current selection

		font.drawString(10, 200, "Water");
		font.drawString(10, 225, "Oil");
		font.drawString(10, 250, "Block");
		font.drawString(10, 275, "Sand");
		font.drawString(10, 300, "Metal");
		font.drawString(10, 325, "Fire");
		font.drawString(10, 350, "Ice");
		font.drawString(10, 375, "Lava");
		font.drawString(10, 400, "Eraser");

		g.setColor(Color.blue); // water
		g.fillRect(72, 202, 16, 16);

		col = new Color(0.3f, 0.1f, 0.1f, 1f); // oil
		g.setColor(col);
		g.fillRect(72, 227, 16, 16);

		col = new Color(0.8f, 0.8f, 0.8f, 1f); // block
		g.setColor(col);
		g.fillRect(72, 252, 16, 16);

		g.setColor(Color.yellow); // sand
		g.fillRect(72, 277, 16, 16);

		col = new Color(0.5f, 0.5f, 0.5f, 1f); // metal
		g.setColor(col);
		g.fillRect(72, 302, 16, 16);

		col = new Color(0.9f, 0.3f, 0f, 1f); // fire
		g.setColor(col);
		g.fillRect(72, 327, 16, 16);

		g.setColor(Color.white); // ice
		g.fillRect(72, 352, 16, 16);

		g.setColor(Color.red); // fire
		g.fillRect(72, 377, 16, 16);
	}

}
