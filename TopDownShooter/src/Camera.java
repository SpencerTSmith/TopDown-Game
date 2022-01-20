
public class Camera {

	private float x, y;
	
	public Camera(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject object)
	{
		x += (((object.getX() - x) - 1000/2) * 0.10f);
		y += (((object.getY() - y) - 563/2) * 0.10f);
		
		//clamp camera down
		if(x <= -200) x = -200;
		if(x >= 1200) x = 1200;
		if(y <= -200) y = -200;
		if(y >= 1700) y = 1700;
	}

	public float getX() 
	{
		return x;
	}

	public void setX(float x) 
	{
		this.x = x;
	}

	public float getY() 
	{
		return y;
	}

	public void setY(float y) 
	{
		this.y = y;
	}
}
