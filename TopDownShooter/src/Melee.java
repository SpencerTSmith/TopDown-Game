import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Melee extends GameObject{

	private BufferedImage meleeTexture;
	
	public Melee(int x, int y, ID id, Handler handler, int mouseX, int mouseY, SpriteSheet ss) {
		super(x, y, id, handler, ss);
		
		velX = (mouseX - x)/20;
		velY = (mouseY - y)/20;
		
		meleeTexture = ss.grabImage(3, 1, 32, 32);
		
	}

	public void tick() {
		x+= velX;
		y+= velY;
		
		//can't attack enemies through walls
		wallCollision();
		
	}

	public void render(Graphics g) {
		g.drawImage(meleeTexture, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}
	
	public void wallCollision()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Block)
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					handler.removeObject(this);
				}
			}
		}
	}

	
	
}
