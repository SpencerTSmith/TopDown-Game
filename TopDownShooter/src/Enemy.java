import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject{
	
	Random r = new Random();
	private int choose = 0;
	private int hp = 1;
	
	private float diffX, diffY, distance;
	
	private BufferedImage enemyTexture;

	public Enemy(int x, int y, ID id, Handler handler, SpriteSheet ss) {
		super(x, y, id, handler, ss);
		
		enemyTexture = ss.grabImage(2, 1, 32, 32);
	}

	public void tick() {
		x+= velX;
		y+= velY;
		
		collisionAndAi();
		
		if(hp <= 0)
		{
			handler.removeObject(this);
		}
		
	}

	public void render(Graphics g) {
		g.drawImage(enemyTexture, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}
	
	//collision check
	public void collisionAndAi()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			
			//get distance from player
			if(tempObject.getId() == ID.Player)
			{
				diffX = x - tempObject.getX();
				diffY = y - tempObject.getY();
				distance = (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
			}
			
			//wall collision and enemy ai
			if(tempObject.getId() == ID.Block)
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					x += (int) ((velX * 3.25) * -1);
					y += (int) ((velY * 3.25) * -1);
					velX = -3;
					velY = -3;
				}
				
				else if(distance < 200)
				{
					velX = (-3/distance) * diffX;
					velY = (-3/distance) * diffY;
				}
				
				/*
				else
				{
					velX = 0;
					velY = 0;
				}
				*/
				
				else 
				{
					choose = r.nextInt(10);
				
					if(choose == 0)
					{
						velX = (r.nextInt(4 - -4) + -4);
						velY = (r.nextInt(4 - -4) + -4);
					}
				}
			}
			
			//player melee attack collision
			if(tempObject.getId() == ID.Melee)
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					hp -= 1;
				}
			}
		}
	}
}
