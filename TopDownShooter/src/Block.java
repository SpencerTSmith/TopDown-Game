import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Block extends GameObject 
{
	int w = 32;
	int h = 32;
	
	private BufferedImage blockTexture;
	
	public Block(int x, int y, ID id, Handler handler, SpriteSheet ss) 
	{
		super(x, y, id, handler, ss);
		
		blockTexture = ss.grabImage(2, 2, 32, 32);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(blockTexture, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}
}
