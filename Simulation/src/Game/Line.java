package Game;

public class Line {
	
	public float x0;
	public float y0;
	public float x1;
	public float y1;
	public boolean isBound;
	
	public Line(float x0, float y0, float x1, float y1, boolean b)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.isBound = b;
		swap();
	}
	
	private void swap()
	{
		if (this.x0 > this.x1)
		{
			float temp = x0;
			x0 = x1;
			x1 = temp;
		}
	}
}
