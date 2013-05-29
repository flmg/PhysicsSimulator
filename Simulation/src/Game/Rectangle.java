package Game;

import org.newdawn.slick.Graphics;

public class Rectangle {
	
	public Line[] edges;

	public Rectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4)
	{
		edges = new Line[4];
		// 0 : Top line
		edges[0] = new Line(x1, y1, x2, y2, false);
		// 1 : Bottom line
		edges[1] = new Line(x3, y3, x4, y4, false);
		// 2 : Left line
		edges[2] = new Line(x1, y1, x3, y3, false);
		// 3 : Right line
		edges[3] = new Line(x2, y2, x4, y4, false);
	}
	
	public void render(Graphics g)
	{
		for (int i = 0; i < 4; i++)
		{
			g.drawLine(edges[i].x0, edges[i].y0, edges[i].x1, edges[i].y1);
		}
	}
}
